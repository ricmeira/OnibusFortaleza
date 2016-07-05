package br.ufc.onibusfortaleza.onibusfortaleza.model;

/**
 * Created by inafalcao on 7/3/16.
 */

public class TransitDetails {

    private Stop arrival_stop;

    private Stop departure_stop;

    private String headsign;

    private Line line;

    private int num_stops;

    public Stop getArrival_stop() {
        return arrival_stop;
    }

    public void setArrival_stop(Stop arrival_stop) {
        this.arrival_stop = arrival_stop;
    }

    public Stop getDeparture_stop() {
        return departure_stop;
    }

    public void setDeparture_stop(Stop departure_stop) {
        this.departure_stop = departure_stop;
    }

    public String getHeadsign() {
        return headsign;
    }

    public void setHeadsign(String headsign) {
        this.headsign = headsign;
    }

    public Line getLine() {
        return line;
    }

    public void setLine(Line line) {
        this.line = line;
    }

    public int getNum_stops() {
        return num_stops;
    }

    public void setNum_stops(int num_stops) {
        this.num_stops = num_stops;
    }
}
