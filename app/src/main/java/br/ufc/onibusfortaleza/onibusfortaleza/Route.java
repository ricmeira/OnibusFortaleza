package br.ufc.onibusfortaleza.onibusfortaleza;

/**
 * Created by eduardo on 16-06-22.
 */
public class Route {
    private int id;
    private String origin;
    private String destiny;
    private String busName;
    private String route;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestiny() {
        return destiny;
    }

    public void setDestiny(String destiny) {
        this.destiny = destiny;
    }

    public String getBusName() {
        return busName;
    }

    public void setBusName(String busName) {
        this.busName = busName;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    @Override
    public String toString() {
        return "Route{" +
                "id=" + id +
                ", origin='" + origin + '\'' +
                ", destiny='" + destiny + '\'' +
                '}';
        //return "zchonark";
    }
}
