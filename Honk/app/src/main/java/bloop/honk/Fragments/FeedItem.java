package bloop.honk.Fragments;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class FeedItem {

    @SerializedName("id")
    long ID;

    @SerializedName("date")
    Date dateCreated;

    String title;
    String author;
    String url;
    String body;

}