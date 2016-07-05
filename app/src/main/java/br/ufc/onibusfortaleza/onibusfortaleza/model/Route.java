package br.ufc.onibusfortaleza.onibusfortaleza.model;

import java.util.List;

/**
 * Created by inafalcao on 7/3/16.
 */

public class Route {

    private Bounds bounds;

    private List<Legs> legs;

    private Polyline overview_polyline;

    public Polyline getOverview_polyline() {
        return overview_polyline;
    }

    public void setOverview_polyline(Polyline overview_polyline) {
        this.overview_polyline = overview_polyline;
    }

    public Bounds getBounds() {
        return bounds;
    }

    public void setBounds(Bounds bounds) {
        this.bounds = bounds;
    }

    public List<Legs> getLegs() {
        return legs;
    }

    public void setLegs(List<Legs> legs) {
        this.legs = legs;
    }
}
