package module.model;

public class Coord {
    private double latitude;
    private double longitude;


    @Override
    public String toString() {
        return "(" + this.latitude + ", " + this.longitude + ")";
    }

    public String toApi() {
        return this.latitude + "," + this.longitude;
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

    public double getDistance(Coord c2) {
        return Math.sqrt(Math.pow((this.latitude - c2.getLatitude()), 2) + Math.pow((this.longitude - c2.getLongitude()), 2));
    }
}
