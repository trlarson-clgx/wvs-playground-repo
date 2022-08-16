package com.corelogic.kafkaTester.kafkaTester.pojo;

public class S2CoveringMetric {
    int maxCells;
    int maxCellLevel;
    int cellsUsed;
    double coveringArea;
    double polygonArea;
    double processingTimeMs;

    public int getMaxCells() {
        return maxCells;
    }

    public S2CoveringMetric setMaxCells(int maxCells) {
        this.maxCells = maxCells;
        return this;
    }

    public int getMaxCellLevel() {
        return maxCellLevel;
    }

    public S2CoveringMetric setMaxCellLevel(int maxCellLevel) {
        this.maxCellLevel = maxCellLevel;
        return this;
    }

    public int getCellsUsed() {
        return cellsUsed;
    }

    public S2CoveringMetric setCellsUsed(int cellsUsed) {
        this.cellsUsed = cellsUsed;
        return this;
    }

    public double getCoveringArea() {
        return coveringArea;
    }

    public S2CoveringMetric setCoveringArea(double coveringArea) {
        this.coveringArea = coveringArea;
        return this;
    }

    public double getPolygonArea() {
        return polygonArea;
    }

    public S2CoveringMetric setPolygonArea(double polygonArea) {
        this.polygonArea = polygonArea;
        return this;
    }

    public double getProcessingTimeMs() {
        return processingTimeMs;
    }

    public S2CoveringMetric setProcessingTimeMs(double processingTimeMs) {
        this.processingTimeMs = processingTimeMs;
        return this;
    }
}
