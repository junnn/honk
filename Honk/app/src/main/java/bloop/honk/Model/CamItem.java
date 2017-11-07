package bloop.honk.Model;

public class CamItem {
    private String CameraID;
    private Double Latitude;
    private Double Longitude;
    private String ImageLink;

    public CamItem(String cameraID, Double latitude, Double longitude, String imageLink) {
        CameraID = cameraID;
        Latitude = latitude;
        Longitude = longitude;
        ImageLink = imageLink;
    }

    public String getCameraID() {
        return CameraID;
    }

    public Double getLatitude() {
        return Latitude;
    }

    public Double getLongitude() {
        return Longitude;
    }

    public String getImageLink() {
        return ImageLink;
    }
}

