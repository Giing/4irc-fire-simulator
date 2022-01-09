package module.model;

import com.google.gson.JsonDeserializer;

import module.model.Coord;

public class Station {
    private String id;
    private Coord location;
    private String name;
    private String streetName;
    private Integer cp;
    private double radius;

    public static JsonDeserializer<Station> jsonAdapter = new StationAdapter();

    public String getId() { return id; }
    public Coord getLocation() { return this.location; }
    public String getName() { return name; }
    public String getStreetName() { return streetName; }
    public double getCp() { return cp; }
    public double getRadius() { return radius; }

    public Station(String id, Coord coord, String name, String streetName, Integer cp, double radius) {
        this.id = id;
        this.location = coord;
        this.name = name;
        this.streetName = streetName;
        this.cp = cp;
        this.radius = radius;
    }

    public boolean isInRadius(Coord coordinates) {
        return radius >= this.location.getDistance(coordinates);
    }

    @Override
    public String toString() {
        return "{" +
                "id='" + id + '\'' +
                ", location='" + this.location + "'" +
                ", name='" + this.name + "\'" +
                ", radius='" + this.radius + "\'" +
                "}\n";
    }
}
