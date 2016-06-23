package br.ufc.onibusfortaleza.onibusfortaleza;

/**
 * Created by eduardo on 16-06-22.
 */
public class Route {
    private int id;
    private String origin;
    private String destiny;

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

    @Override
    public String toString() {
        return "Route{" +
                "id=" + id +
                ", origin='" + origin + '\'' +
                ", destiny='" + destiny + '\'' +
                '}';
    }
}
