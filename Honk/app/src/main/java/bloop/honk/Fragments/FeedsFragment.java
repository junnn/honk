package bloop.honk.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Arrays;
import java.util.List;

import bloop.honk.R;

/**
 * Created by Jun Hao Ng on 6/9/2017.
 */

public class FeedsFragment extends Fragment {
    public List<FeedItem> posts;

    private Gson gson;

    private static final String ENDPOINT = "http://172.21.148.166/example/trafficfeed.php";

    private RequestQueue requestQueue;

    private RecyclerView recyclerView;

    private FeedsAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feeds, container, false);
        getActivity().setTitle("Feeds");//set the title on the toolbar

        recyclerView = view.findViewById(R.id.feedrecycler);

        requestQueue = Volley.newRequestQueue(getActivity());

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("M/d/yy hh:mm a");
        gson = gsonBuilder.create();

        fetchPosts();
        return view;
    }

    private void fetchPosts() {
        StringRequest request = new StringRequest(Request.Method.GET, ENDPOINT, onPostsLoaded, onPostsError);

        requestQueue.add(request);
    }

    private final Response.Listener<String> onPostsLoaded = new Response.Listener<String>() {

        @Override
        public void onResponse(String response) {
            posts = Arrays.asList(gson.fromJson(response, FeedItem[].class));

            Log.i("PostActivity", posts.size() + " posts loaded.");
            for (FeedItem feedItem : posts) {
                String date_time = feedItem.message.substring(0,feedItem.message.indexOf(' ')); // "72"
                String msg = feedItem.message.substring(feedItem.message.indexOf(' ')+1);
                Log.i("PostActivity", feedItem.type+":"+date_time + ": " + msg);
            }

                adapter = new FeedsAdapter(getActivity(), posts);
                //adapter.setClickListener(this);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        }

    };

    private final Response.ErrorListener onPostsError = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e("PostActivity", error.toString());
        }
    };

}
