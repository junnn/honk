package bloop.honk;

/**
 * Created by Bryan Boey S-15 on 2/10/2017.
 */

import java.util.Date;
import com.google.gson.annotations.SerializedName;

public class Post {

    @SerializedName("id")
    public long ID;

    @SerializedName("date")
    Date dateCreated;

    public String title;
    String author;
    String url;
    String body;

}
