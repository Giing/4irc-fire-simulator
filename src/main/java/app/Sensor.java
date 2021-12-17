package app;

public class Sensor {


    private final String id;
    private final Coord location;
    private final double range = 0.01929018172830706 / 2;
    private int intensity;

    public String getId() {
        return id;
    }

    public double getRange() {
        return range;
    }

    public Coord getLocation() { return this.location; }

    public int getIntensity() {
        return intensity;
    }


    public Sensor(String id, Coord location, int intensity) {
        this.id = id;
        this.location = location;
        this.intensity = intensity;
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
        return "Sensor{" +
                "id='" + id + '\'' +
                ", location='(" + this.location + ")'" +
                ", intensite='" + intensity + "\'" +
                ", range='" + range + "\'" +
                "}\n";
    }
}
