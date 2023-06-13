package com.minhacasa.gestao.domain.utils;

public class NumberUtils {

    public static double round(double value) {
        return Math.round(value * 1000.0) / 1000.0;
    }

}
