package bloop.honk.FeedsComponents;

public class FeedItem {

    private String Type;
    private Double Latitude;
    private Double Longtitude;
    private String Message;
    private String date_time;

    public FeedItem(String type, Double latitude, Double longtitude, String message, String date_time) {
        Type = type;
        Latitude = latitude;
        Longtitude = longtitude;
        Message = message;
        this.date_time = date_time;
    }

    public String getType() {
        return Type;
    }

    public Double getLatitude() {
        return Latitude;
    }

    public Double getLongtitude() {
        return Longtitude;
    }

    public String getMessage() {
        return Message;
    }

    public String getDate_time() {
        return date_time;
    }
}