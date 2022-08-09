package com.corelogic.kafkaTester.kafkaTester.service;

import com.google.common.geometry.*;
import org.geotools.geometry.jts.WKBReader;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKBHexFileReader;
import org.locationtech.jts.io.WKTReader;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Reader;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.HexFormat;

@Service
public class IndexingService {

    public ArrayList<String> coverPoly(byte[] poly) throws ParseException {
        WKBReader wkbReader = new WKBReader();
        Geometry geo = wkbReader.read(poly);
        Coordinate[] coordinates = geo.getCoordinates();
        System.out.println("Parsed WKB");

        //Building a region to cover
        S2PolygonBuilder builder = new S2PolygonBuilder();

        for(int i = 0; i<coordinates.length-1; i++){
            S1Angle lat1 = S1Angle.degrees(coordinates[i].y);
            S1Angle lng1 = S1Angle.degrees(coordinates[i].x);
            S1Angle lat2 = S1Angle.degrees(coordinates[i+1].y);
            S1Angle lng2 = S1Angle.degrees(coordinates[i+1].x);

            builder.addEdge(new S2LatLng(lat2, lng2).toPoint(), new S2LatLng(lat1, lng1).toPoint());
        }

        S1Angle lat1 = S1Angle.degrees(coordinates[coordinates.length - 1].y);
        S1Angle lng1 = S1Angle.degrees(coordinates[coordinates.length - 1].x);
        S1Angle lat2 = S1Angle.degrees(coordinates[0].y);
        S1Angle lng2 = S1Angle.degrees(coordinates[0].x);

        builder.addEdge(new S2LatLng(lat2, lng2).toPoint(), new S2LatLng(lat1, lng1).toPoint());
        ArrayList<S2Loop> loops = new ArrayList<>();
        builder.assembleLoops(loops, null);
        builder.addLoop(loops.get(0));


        S2Polygon s2Polygon = builder.assemblePolygon();
        System.out.println("Polygon area: " + s2Polygon.getArea());
        System.out.println("s2Polygon Built");

        //Cover the region and return cellIds
        S2RegionCoverer coverer = S2RegionCoverer.builder().setMaxCells(1000).setMaxLevel(30).build();
        System.out.println("Covering built");
        ArrayList<S2CellId> s2CellIds = coverer.getCovering(s2Polygon).cellIds();

        ArrayList<String> index = new ArrayList<>();

        for(S2CellId id : s2CellIds){
            index.add(Long.toHexString(id.id()));
        }
        return index;
    }
}
