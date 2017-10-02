package bloop.honk;

/**
 * Created by Bryan Boey S-15 on 2/10/2017.
 */
import com.google.gson.annotations.SerializedName;

public class Bookmark {
    @SerializedName("favourite")
    long name;

    String longitude;
    String latitude;
}
