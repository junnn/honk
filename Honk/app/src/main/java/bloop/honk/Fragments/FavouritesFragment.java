package bloop.honk.Fragments;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import bloop.honk.Bookmark;
import bloop.honk.Config;
import bloop.honk.R;

/**
 * Created by Jun Hao Ng on 6/9/2017.
 */

public class FavouritesFragment extends Fragment {
    private static final String readBk = "http://172.21.148.166/example/fetchbookmark.php/?username=";
    private static final String delBk = "http://172.21.148.166/example/deletebookmark.php";
    private static List<Bookmark> posts;
    private static ArrayList<String> test = new ArrayList<>();
    private Gson gson;
    private RecyclerView recyclerView;
    private RequestQueue requestQueue;
    MyRecyclerViewAdapter adapter;
    private SharedPreferences sharedPreferences;
    private String username;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle("Favourites");
        super.onCreate(savedInstanceState);
        test = new ArrayList<>();
        View rootView = inflater.inflate(R.layout.fragment_favourites, container, false);

        sharedPreferences = getActivity().getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);

        if(!sharedPreferences.getBoolean(Config.LOGGEDIN_SHARED_PREF, true)) {//if LOGGEDIN == false
            Toast.makeText(getContext(), "Please login to access your favourties", Toast.LENGTH_LONG).show();
        }
        else{
            username = "";
                    //sharedPreferences.getString(Config.USERNAME_SHARED_PREF,""); //used this to get current username

            requestQueue = Volley.newRequestQueue(getActivity());
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.setDateFormat("M/d/yy hh:mm a");
            gson = gsonBuilder.create();
            fetchPosts();
            // data to populate the RecyclerView with
            recyclerView = (RecyclerView) rootView.findViewById(R.id.postview);
        }
        return rootView;
    }

    private void deleteBookmark(String bkmk) {
        final String bookmark = bkmk;
        String url = "http://172.21.148.166/example/deletebookmark.php";
        StringRequest MyStringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                test = new ArrayList<>();
                fetchPosts();
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
                MyData.put("bookmarkname", bookmark);
                return MyData;
            }
        };
        requestQueue.add(MyStringRequest);
    }


    private void fetchPosts() {
        StringRequest request = new StringRequest(Request.Method.GET, readBk + username, onPostsLoaded, onPostsError);
        requestQueue.add(request);
    }

    private final Response.Listener<String> onPostsLoaded = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            posts = Arrays.asList(gson.fromJson(response, Bookmark[].class));
            Log.i("PostActivity", posts.size() + " posts loaded.");
            for (Bookmark bookmark : posts) {
                test.add(bookmark.name);
                //Log.i("PostActivity", post.ID + ": " + post.title);
            }
            adapter = new MyRecyclerViewAdapter(getActivity(), test);
            recyclerView.setAdapter(adapter);
            adapter.setClickListener(new MyRecyclerViewAdapter.ItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    switch (view.getId()) {
                        case R.id.favImageButton:
                            deleteBookmark(adapter.getItem(position));
                            Toast.makeText(getActivity(), "You unbookmarked FavButton " + adapter.getItem(position) + " on row number " + position, Toast.LENGTH_SHORT).show();
                            break;
                        default: // NAVIGATION, i will do it just leave this portion alone
                            Toast.makeText(getActivity(), "You clicked ItemList " + adapter.getItem(position) + " on row number " + position, Toast.LENGTH_SHORT).show();
                    }
                }
            });
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        }
    };



    private final Response.ErrorListener onPostsError = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e("PostActivity", error.toString());
        }
    };
    //@Override
    //public void onItemClick(View view, int position) {
       //Toast.makeText(getActivity(), "You clicked " + adapter.getItem(position) + " on row number " + position, Toast.LENGTH_SHORT).show();
//}
}

