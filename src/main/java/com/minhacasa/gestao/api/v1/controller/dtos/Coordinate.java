package com.minhacasa.gestao.api.v1.controller.dtos;

public class Coordinate {

    private Double x;
    private Double y;
    private final boolean isVisible;

    public Coordinate(Double x, Double y, boolean visibility) {
        this.x = x;
        this.y = y;
        this.isVisible = visibility;
    }

    public static Coordinate buildCoordinate(double x, double y, boolean visibility) {
        return new Coordinate(x, y, visibility);
    }

    public Double getX() {
        return x;
    }

    public void setX(Double x) {
        this.x = x;
    }

    public Double getY() {
        return y;
    }

    public void setY(Double y) {
        this.y = y;
    }

    public boolean isVisible() {
        return isVisible;
    }
}
