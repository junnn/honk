package bloop.honk.CameraComponents;

/**
 * Created by Don on 2017/10/13.
 */

public class CamItem {
    String CameraID;
    Double Latitude;
    Double Longitude;
    String ImageLink;

    public String getCameraID(){
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

