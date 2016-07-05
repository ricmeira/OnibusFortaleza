package br.ufc.onibusfortaleza.onibusfortaleza.model;

import java.util.List;

/**
 * Created by inafalcao on 7/3/16.
 */

public class Legs {

    private String end_address;

    private String start_address;

    private Coordinates start_location;

    private Coordinates end_location;

    private List<Steps> steps;

    public String getEnd_address() {
        return end_address;
    }

    public void setEnd_address(String end_address) {
        this.end_address = end_address;
    }

    public String getStart_address() {
        return start_address;
    }

    public void setStart_address(String start_address) {
        this.start_address = start_address;
    }

    public Coordinates getStart_location() {
        return start_location;
    }

    public void setStart_location(Coordinates start_location) {
        this.start_location = start_location;
    }

    public Coordinates getEnd_location() {
        return end_location;
    }

    public void setEnd_location(Coordinates end_location) {
        this.end_location = end_location;
    }

    public List<Steps> getSteps() {
        return steps;
    }

    public void setSteps(List<Steps> steps) {
        this.steps = steps;
    }
}
