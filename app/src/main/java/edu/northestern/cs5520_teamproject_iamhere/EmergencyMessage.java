package edu.northestern.cs5520_teamproject_iamhere;

public class EmergencyMessage {
    private double latitude;
    private double longitude;
    private long expireTime;

    public EmergencyMessage() {}

    public EmergencyMessage(double latitude, double longitude, long expireTime) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.expireTime = expireTime;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public long getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(long expireTime) {
        this.expireTime = expireTime;
    }
}

