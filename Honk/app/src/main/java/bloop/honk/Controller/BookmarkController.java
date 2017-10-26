package bloop.honk.Controller;


import android.app.Activity;
import android.content.Context;
import android.util.Log;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bloop.honk.FavouritesComponents.Bookmark;

/**
 * Created by Bryan Boey S-15 on 22/10/2017.
 */
//TRIED MAKING but complete failure
public class BookmarkController {
    private static Gson gson;
    private static List<Bookmark> posts = new ArrayList<Bookmark>();
    public static RequestQueue r;
    private boolean wait = true;
    private static final String readBk = "http://172.21.148.166/example/dao/Hookdaoimpl.php?function=getBookMark&username=";

    public BookmarkController(String username,Activity activity) {
        getBookmark(username,activity);
    }
    public void getBookmark(String username, Activity activity) {
        RequestQueue queue = Volley.newRequestQueue(activity);
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("M/d/yy hh:mm a");
        gson = gsonBuilder.create();
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, readBk+username,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        posts = Arrays.asList(gson.fromJson(response, Bookmark[].class));
                        wait = false;
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
// Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    public boolean getstate() {
        return wait;
    }

    public List<Bookmark> returnList() {
        return posts;
    }
}
