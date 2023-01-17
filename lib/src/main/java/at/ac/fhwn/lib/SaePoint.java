package at.ac.fhwn.lib;

public class SaePoint{

    private double latitude;
    private double longitude;
    private String time;
    private int nrOfSatellites;
    private int fixQuality;

    public SaePoint(){}

    public SaePoint(String time, double latitude, double longitude,int nrOfSatellites, int fixQuality){
        this.time = time;
        this.latitude = latitude;
        this.longitude = longitude;
        this.nrOfSatellites = nrOfSatellites;
        this.fixQuality = fixQuality;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getTime() {
        return time;
    }

    public int getNrOfSatellites() {
        return nrOfSatellites;
    }

    public int getFixQuality() {
        return fixQuality;
    }

    @Override
    public String toString() {
        return "at.ac.fhwn.lib.SaePoint{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                ", time='" + time + '\'' +
                ", nrOfSatellites=" + nrOfSatellites +
                ", fixQuality=" + fixQuality +
                '}';
    }
}
