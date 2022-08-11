package com.corelogic.kafkaTester.kafkaTester.data.repository;

import com.corelogic.kafkaTester.kafkaTester.pojo.Contour;
import io.r2dbc.spi.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class ContourRepository {
    @Qualifier("testDatabaseClient")
    @Autowired
    DatabaseClient databaseClient;
    String selectAllContours = "SELECT id, gid, pgid, ymd, z, geom FROM contours";
    String selectContoursByDate = "SELECT id, gid, pgid, ymd, z, geom FROM contours WHERE date = :date";

    private Contour rowToContour(Row row) {
        return new Contour()
                .setId(row.get("id", Integer.class))
                .setGid(row.get("gid", Integer.class))
                .setPgid(row.get("pgid", Integer.class))
                .setYmd(row.get("ymd", LocalDate.class))
                .setZ(row.get("z", Float.class))
                .setGeom(row.get("geom", String.class));
    }

    public List<Contour> getContours() {
         return databaseClient.sql(selectAllContours)
                 .map(this::rowToContour)
                 .all()
                 .collectList()
                 .block();
    }

    public List<Contour> getContoursByDate(LocalDate date) {
        return databaseClient.sql(selectContoursByDate)
                .bind("date", date)
                .map(this::rowToContour)
                .all()
                .collectList()
                .block();
    }
}
