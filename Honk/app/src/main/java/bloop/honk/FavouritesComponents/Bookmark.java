package bloop.honk.FavouritesComponents;

/**
 * Created by Bryan Boey S-15 on 2/10/2017.
 */
import com.google.gson.annotations.SerializedName;

public class Bookmark {
    @SerializedName("favourite")
    public String name;

    String longitude;
    String latitude;
}
