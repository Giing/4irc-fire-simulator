package module.model;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

public class Sensor {


    private final String id;
    private final Coord location;
    private final double radius = 0.01929018172830706 / 2;
    private int intensity;

    public String getEmergencyId() {
        return emergencyId;
    }

    private String emergencyId;

    public static JsonDeserializer<Sensor> jsonAdapter = new SensorAdapter();

    public String getId() {
        return id;
    }

    public double getRadius() {
        return radius;
    }

    public Coord getLocation() { return this.location; }

    public int getIntensity() {
        return intensity;
    }

    public void setEmergencyId(String id) { this.emergencyId = id; }

    public Sensor(String id, Coord location, int intensity) {
        this.id = id;
        this.location = location;
        this.emergencyId = null;
        this.intensity = intensity;
    }

    public Sensor(String id, double longitude, double latitude, int intensity) {
        this.id = id;
        this.location = new Coord(longitude, latitude);
        this.emergencyId = null;
        this.intensity = intensity;
    }

    public Sensor(String id, double longitude, double latitude, int intensity, String emergencyId) {
        this.id = id;
        this.location = new Coord(longitude, latitude);
        this.intensity = intensity;
        this.emergencyId = emergencyId;
    }

    public Sensor(String id, Coord coord, int intensity, String emergencyId) {
        this.id = id;
        this.location = coord;
        this.intensity = intensity;
        this.emergencyId = emergencyId;
    }

    public boolean isInRadius(Coord coordinates) {
        return radius < this.location.getDistance(coordinates);
    }

    public void incrementIntensite(int inc) {
        this.intensity += inc;
    }

    public void decrementIntensity(int dec) throws Exception {
        if (this.intensity - dec > 0)
            this.intensity -= dec;
        else
            throw new Exception("Intensity cannot be negative");
    }

    @Override
    public boolean equals(Object obj) {
        boolean res = false;
        if (obj instanceof Sensor) {
            Sensor other = (Sensor) obj;
            res = this.id.equals(other.getId());
        }
        return res;
    }

    @Override
    public String toString() {
        return "{" +
                "id='" + id + '\'' +
                ", location='" + this.location + "'" +
                ", intensite='" + intensity + "\'" +
                ", radius='" + radius + "\'" +
                "}\n";
    }

    public String toJSON() {
        return "{\"id\":" + this.id +
                ", \"latitude\":" + this.location.getLatitude() +
                ", \"longitude\":" + this.location.getLongitude() +
                ", \"radius\":" + this.radius +
                ", \"emergencyId\":" + this.emergencyId +
                ", \"intensity\":" + this.intensity +
                "}";
    }
}
