package br.ufc.onibusfortaleza.onibusfortaleza.model;

/**
 * Created by inafalcao on 7/3/16.
 */

public class Bounds {

    private Coordinates northeast;

    private Coordinates southwest;

    public Coordinates getNortheast() {
        return northeast;
    }

    public void setNortheast(Coordinates northeast) {
        this.northeast = northeast;
    }

    public Coordinates getSouthwest() {
        return southwest;
    }

    public void setSouthwest(Coordinates southwest) {
        this.southwest = southwest;
    }
}
