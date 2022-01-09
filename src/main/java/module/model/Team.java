package module.model;

import java.util.Timer;

import com.google.gson.JsonDeserializer;

public class Team {
    private String id;
    private Coord location;
    private Integer level;
    private String stationId;
    private String emergencyId;
    private final double radius = 0.01929018172830706;

    public Boolean isHandling = false;

    public static JsonDeserializer<Team> jsonAdapter = new TeamAdapter();

    public String getId() { return id; }
    public Coord getLocation() { return this.location; }
    public void setLocation(Coord location) { this.location = location; }
    public Integer getLevel() { return level; }
    public String getStationId() { return stationId; }
    public String getEmergencyId() { return emergencyId; }
    public void setEmergencyId(String emergencyId) { this.emergencyId = emergencyId; }

    public Boolean isAvailable() { return emergencyId == null; }

    public Team(String id, Coord coord, Integer level, String stationId) {
        this.id = id;
        this.location = coord;
        this.level = level;
        this.stationId = stationId;
    }

    public Team(String id, Coord coord, Integer level, String stationId, String emergencyId) {
        this.id = id;
        this.location = coord;
        this.level = level;
        this.stationId = stationId;
        this.emergencyId = emergencyId;
    }

    public boolean isHandlingFromCoord(Coord location) {
        return this.getLocation().getDistance(location) <= this.radius;
    }

    @Override
    public String toString() {
        return "{" +
                "id='" + id + '\'' +
                ", location='" + this.location + "'" +
                ", level='" + this.level + "\'" +
                ", radius='" + radius + "\'" +
                ", emergencyId='" + emergencyId + "\'" +
                "}\n";
    }
}
