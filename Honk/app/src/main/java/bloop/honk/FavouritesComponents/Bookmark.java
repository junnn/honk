package bloop.honk.FavouritesComponents;

/**
 * Created by Bryan Boey S-15 on 2/10/2017.
 */
import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

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

public class Bookmark {
    @SerializedName("favourite")
    private String name;
    private String longtitude;
    private String latitude;
    private static Gson gson;
    private static final String readBk = "http://172.21.148.166/example/dao/Hookdaoimpl.php?function=getBookMark&username=";
    private static final String delBk = "http://172.21.148.166/example/dao/Hookdaoimpl.php?function=deletebookmark";

    public Bookmark(){}

    public Bookmark(String name, String longitude, String latitude) {
        this.name = name;
        this.longtitude = longitude;
        this.latitude = latitude;
    }

    public String getLongitude(){
        return longtitude;
    }

    public String getLatitude(){
        return latitude;
    }

    public String getName(){
        return name;
    }

    public void getBookmarkList(String username, final RecyclerView recyclerView, final List<Bookmark> posts, final Activity activity, final BookmarkAdapter adapter) {
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

                        if(response.equals("[]")){
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

    public void deleteBookmark(final String username, final String bkmk, final Activity activity) {

        RequestQueue requestQueue = Volley.newRequestQueue(activity);
        StringRequest MyStringRequest = new StringRequest(Request.Method.POST, delBk, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //fetchPosts();

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
}


