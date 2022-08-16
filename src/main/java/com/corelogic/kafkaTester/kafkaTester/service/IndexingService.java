package com.corelogic.kafkaTester.kafkaTester.service;

import com.google.common.geometry.*;
import org.geotools.geometry.jts.WKBReader;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.LinearRing;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.io.ParseException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class IndexingService {

    public ArrayList<String> coverPoly(byte[] poly, Integer maxCells, Integer maxLevel) throws ParseException {
        Long startTime = System.nanoTime();
        //Parse WKB to a Polygon object
        WKBReader wkbReader = new WKBReader();
        Polygon geo = (Polygon) wkbReader.read(poly);
        System.out.println("Parsed WKB");

        //Break polygon into rings.
        ArrayList<LinearRing> linearRings = new ArrayList<>();
        linearRings.add(geo.getExteriorRing());
        for(int i = 0; i < geo.getNumInteriorRing(); i++){
            linearRings.add(geo.getInteriorRingN(i));
        }

        //Use rings in building an S2polygon
        S2PolygonBuilder builder = new S2PolygonBuilder();
        for(LinearRing lr : linearRings) {
            Coordinate[] coordinates = lr.getCoordinates();
            for (int i = 0; i < coordinates.length - 1; i++) {
                S1Angle lat1 = S1Angle.degrees(coordinates[i].y);
                S1Angle lng1 = S1Angle.degrees(coordinates[i].x);
                S1Angle lat2 = S1Angle.degrees(coordinates[i + 1].y);
                S1Angle lng2 = S1Angle.degrees(coordinates[i + 1].x);

                builder.addEdge(new S2LatLng(lat2, lng2).toPoint(), new S2LatLng(lat1, lng1).toPoint());
            }

            S1Angle lat1 = S1Angle.degrees(coordinates[coordinates.length - 1].y);
            S1Angle lng1 = S1Angle.degrees(coordinates[coordinates.length - 1].x);
            S1Angle lat2 = S1Angle.degrees(coordinates[0].y);
            S1Angle lng2 = S1Angle.degrees(coordinates[0].x);
            builder.addEdge(new S2LatLng(lat2, lng2).toPoint(), new S2LatLng(lat1, lng1).toPoint());
        }

        ArrayList<S2Loop> loops = new ArrayList<>();
        builder.assembleLoops(loops, null);
        for(S2Loop loop : loops)
            builder.addLoop(loop);


        S2Polygon s2Polygon = builder.assemblePolygon();

        //Cover the region and return cellIds
        S2RegionCoverer coverer = S2RegionCoverer.builder().setMaxCells(maxCells).setMaxLevel(maxLevel).build();
        S2CellUnion covering = coverer.getCovering(s2Polygon);
        ArrayList<S2CellId> s2CellIds = covering.cellIds();
        ArrayList<String> index = new ArrayList<>();

        for(S2CellId id : s2CellIds){
            index.add(Long.toHexString(id.id()));
        }
        Long endTime = System.nanoTime();

        //Useful metrics
        // Max cells/level
        System.out.println("Max cells: " + coverer.maxCells());
        System.out.println("Max level: " + coverer.maxLevel());
        //# of Cells used
        System.out.println("Number of cells used: " + s2CellIds.size());
        //Covering area/polygon area
        final double ratio = covering.approxArea() / s2Polygon.getArea();
        System.out.println("Covering ratio: " + ratio);
        //Time to go from WKB to S2Covering
        final double processingTime = (endTime-startTime) / 1000000.0;
        System.out.println("Covering time: " + processingTime + " ms");

        return index;
    }

    //TODO: Move logic for going from LinearRings to S2Loops into this function
    private void ringsToBuilderLoops(S2PolygonBuilder builder, LinearRing ring){

    }
}
