package com.example.roadkeeper;

public class TravelLog {
    private String id;
    private double latitude;
    private double longitude;
    private String imageUrl;
    private String description;
    private long timestamp;

    public TravelLog() {}

    public TravelLog(String id, double latitude, double longitude, String imageUrl, String description, long timestamp) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.imageUrl = imageUrl;
        this.description = description;
        this.timestamp = timestamp;
    }

    public String getId() { return id; }
    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }
    public String getImageUrl() { return imageUrl; }
    public String getDescription() { return description; }
    public long getTimestamp() { return timestamp; }
}
