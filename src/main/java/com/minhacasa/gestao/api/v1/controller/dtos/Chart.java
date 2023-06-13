package com.minhacasa.gestao.api.v1.controller.dtos;

import java.util.List;

public class Chart {

    private List<Coordinate> coordinates;
    private String functionLaw;

    public Chart() {
    }

    public Chart(List<Coordinate> coordinates, String functionLaw) {
        this.coordinates = coordinates;
        this.functionLaw = functionLaw;
    }

    public List<Coordinate> getCoordinates() {
        return coordinates;
    }

    public String getFunctionLaw() {
        return functionLaw;
    }
}
