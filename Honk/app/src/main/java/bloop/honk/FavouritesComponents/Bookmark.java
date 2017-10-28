package bloop.honk.FavouritesComponents;

/**
 * Created by Bryan Boey S-15 on 2/10/2017.
 */

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bloop.honk.Config;

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


