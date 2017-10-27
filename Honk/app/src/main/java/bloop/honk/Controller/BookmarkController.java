package bloop.honk.Controller;


import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.Toast;

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
import bloop.honk.FavouritesComponents.BookmarkAdapter;

/**
 * Created by Bryan Boey S-15 on 22/10/2017.
 */
//TRIED MAKING but complete failure
public class BookmarkController {
    private static Gson gson;
    private static List<Bookmark> posts = new ArrayList<Bookmark>();
    public static RequestQueue r;
    private static final String readBk = "http://172.21.148.166/example/dao/Hookdaoimpl.php?function=getBookMark&username=";
    private static final String delBk = "http://172.21.148.166/example/dao/Hookdaoimpl.php?function=deletebookmark";
    private BookmarkAdapter adapter;
    private Activity activity;

    public BookmarkController(Activity activity, BookmarkAdapter adapter) {
        this.activity = activity;
        this.adapter = adapter;
    }
    public void getBookmark(String username, final RecyclerView recyclerView, final List<Bookmark> posts) {
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
                        if(response.isEmpty()){
                            Toast.makeText(activity.getApplicationContext(), "You currently have no bookmarks", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            if(posts.isEmpty()){
                                posts.clear();
                            }
                            posts.addAll(Arrays.asList(gson.fromJson(response, Bookmark[].class)));
                            //adapter = new BookmarkAdapter(activity, posts);
                            recyclerView.setAdapter(adapter);// data to populate the RecyclerView with
                            //adapter.notifyDataSetChanged();

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
// Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    public void deleteBookmark(final String username, final String bkmk) {

        RequestQueue requestQueue = Volley.newRequestQueue(activity);
        StringRequest MyStringRequest = new StringRequest(Request.Method.POST, delBk, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //fetchPosts();
                //This code is executed if the server responds, whether or not the response contains data.
                //The String 'response' contains the server's response.
            }
        }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
            @Override
            public void onErrorResponse(VolleyError error) {
                //This code is executed if there is an error.
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> MyData = new HashMap<String, String>();
                MyData.put("username", username); //Add the data you'd like to send to the server.
                MyData.put("bookmarkname", bkmk);
                return MyData;
            }
        };
        requestQueue.add(MyStringRequest);
    }
}
