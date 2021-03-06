package bloop.honk.Model;

import android.app.Activity;
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

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bloop.honk.View.BookmarkAdapter;

public class BookmarkManager {
    private static Gson gson;

    public void getBookmarkList(String username, final RecyclerView recyclerView, final List<Bookmark> posts, final Activity activity, final BookmarkAdapter adapter) {
        RequestQueue queue = Volley.newRequestQueue(activity);
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("M/d/yy hh:mm a");
        gson = gsonBuilder.create();
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Config.GETBM_URL + username,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equals("[]")) {
                            posts.clear();
                            adapter.notifyItemRangeRemoved(0, 1);
                            Toast.makeText(activity.getApplicationContext(), "You currently have no bookmarks", Toast.LENGTH_SHORT).show();
                        } else {
                            if (posts.isEmpty()) {
                                posts.clear();
                            }
                            posts.clear();
                            posts.addAll(Arrays.asList(gson.fromJson(response, Bookmark[].class)));
                            recyclerView.setAdapter(adapter);// data to populate the RecyclerView with
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

    public void deleteBookmark(final String username, final RecyclerView recyclerView, final List<Bookmark> posts, final String bkmk, final Activity activity, final BookmarkAdapter adapter) {

        RequestQueue requestQueue = Volley.newRequestQueue(activity);
        StringRequest MyStringRequest = new StringRequest(Request.Method.POST, Config.DELBM_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                getBookmarkList(username, recyclerView, posts, activity, adapter);
                Toast.makeText(activity, "You unbookmarked FavButton " + bkmk, Toast.LENGTH_SHORT).show();
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

    public void addBookmark(final String username, final String address, final double lat, final double lng, final Activity activity) {

        RequestQueue requestQueue = Volley.newRequestQueue(activity);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.ADDBM_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equalsIgnoreCase("unsucessful")) {
                    Toast.makeText(activity, "Adding of Bookmark Failed", Toast.LENGTH_LONG).show();
                } else if (response.equalsIgnoreCase("duplicate")) {
                    Toast.makeText(activity, "This bookmark already exist in your list", Toast.LENGTH_LONG).show();
                } else if (response.equalsIgnoreCase("success")) {
                    Toast.makeText(activity, "You have added " + address + " into your Favourite List", Toast.LENGTH_SHORT).show();
                    Log.i("android", "address: " + address + " lat: " + lat + " lng: " + lng);
                } else {
                    Toast.makeText(activity, "Unknown Error Occurred!", Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //Adding parameters to request
                params.put("username", username);
                params.put("bookmarkname", address);
                params.put("latitude", Double.toString(lat));
                params.put("longtitude", Double.toString(lng));

                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
}
