package br.ufc.onibusfortaleza.onibusfortaleza.model;

/**
 * Created by inafalcao on 7/3/16.
 */

public class Stop {

    private Coordinates location;

    private String name;

    public Coordinates getCoordinates() {
        return location;
    }

    public void setCoordinates(Coordinates location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
