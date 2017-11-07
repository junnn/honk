package bloop.honk.Model;

import com.google.gson.annotations.SerializedName;

public class Bookmark {
    @SerializedName("favourite")
    private String name;
    private String longtitude;
    private String latitude;

    public Bookmark() {
    }

    public Bookmark(String name, String longitude, String latitude) {
        this.name = name;
        this.longtitude = longitude;
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longtitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getName() {
        return name;
    }

}


