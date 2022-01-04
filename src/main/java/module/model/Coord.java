package module.model;

public class Coord {
    private double latitude;
    private double longitude;


    @Override
    public String toString() {
        return "(" + this.latitude + ", " + this.longitude + ")";
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public Coord(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
