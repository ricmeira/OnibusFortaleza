package br.ufc.onibusfortaleza.onibusfortaleza.model;

import java.util.List;

/**
 * Created by inafalcao on 7/3/16.
 */

public class ApiResponse {

    private List<Route> routes;

    public List<Route> getRoutes() {
        return routes;
    }

    public void setRoutes(List<Route> routes) {
        this.routes = routes;
    }
}
