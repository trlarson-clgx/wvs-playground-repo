package com.corelogic.kafkaTester.kafkaTester.pojo;

import java.time.LocalDate;

public class Contour {
    private Integer id;
    private Integer gid;
    private Integer pgid;
    private LocalDate ymd;
    private Float z;
    private String geom;

    public Integer getId() {
        return id;
    }

    public Contour setId(Integer id) {
        this.id = id;
        return this;
    }

    public Integer getGid() {
        return gid;
    }

    public Contour setGid(Integer gid) {
        this.gid = gid;
        return this;
    }

    public Integer getPgid() {
        return pgid;
    }

    public Contour setPgid(Integer pgid) {
        this.pgid = pgid;
        return this;
    }

    public LocalDate getYmd() {
        return ymd;
    }

    public Contour setYmd(LocalDate ymd) {
        this.ymd = ymd;
        return this;
    }

    public Float getZ() {
        return z;
    }

    public Contour setZ(Float z) {
        this.z = z;
        return this;
    }

    public String getGeom() {
        return geom;
    }

    public Contour setGeom(String geom) {
        this.geom = geom;
        return this;
    }
}
