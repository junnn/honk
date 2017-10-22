package bloop.honk.FavouritesComponents;

/**
 * Created by Bryan Boey S-15 on 2/10/2017.
 */
import com.google.gson.annotations.SerializedName;

public class Bookmark {
    @SerializedName("favourite")
    private String name;
    private String longitude;
    private String latitude;

    public Bookmark(String name, String longitude, String latitude) {
        this.name = name;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public String getLongitude(){
        return longitude;
    }

    public String getLatitude(){
        return latitude;
    }

    public String getName(){
        return name;
    }
}


