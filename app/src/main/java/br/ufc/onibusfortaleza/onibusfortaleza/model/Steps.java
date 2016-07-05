package br.ufc.onibusfortaleza.onibusfortaleza.model;

import java.util.List;

/**
 * Created by inafalcao on 7/3/16.
 */

public class Steps {

    private Coordinates start_location;

    private Coordinates end_location;

    private Polyline polyline;

    private String travel_mode;

    private List<Steps> steps;

    private TransitDetails transit_details;

    public TransitDetails getTransit_details() {
        return transit_details;
    }

    public void setTransit_details(TransitDetails transit_details) {
        this.transit_details = transit_details;
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

    public Polyline getPolyline() {
        return polyline;
    }

    public void setPolyline(Polyline polyline) {
        this.polyline = polyline;
    }

    public String getTravel_mode() {
        return travel_mode;
    }

    public void setTravel_mode(String travel_mode) {
        this.travel_mode = travel_mode;
    }

    public List<Steps> getSteps() {
        return steps;
    }

    public void setSteps(List<Steps> steps) {
        this.steps = steps;
    }
}
