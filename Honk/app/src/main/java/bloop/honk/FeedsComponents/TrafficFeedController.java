package bloop.honk.FeedsComponents;

import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

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

/**
 * Created by Don on 25/10/2017.
 */

public class TrafficFeedController {
    private static final String NTUENDPOINT = "http://172.21.148.166/example/dao/Hookdaoimpl.php?function=getTrafficFeed";
    private Activity activity;
    private FeedsAdapter feedsAdapter;
    private RequestQueue requestQueue;
    private Gson gson;


    public TrafficFeedController(Activity activity, FeedsAdapter feedsAdapter){
        this.activity = activity;
        this.feedsAdapter = feedsAdapter;
    }

    public void fetchPostsNTU(final RecyclerView recyclerView, final List<FeedItem> feedList) {

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("M/d/yy hh:mm a");
        gson = gsonBuilder.create();
        requestQueue = Volley.newRequestQueue(activity);

        StringRequest request = new StringRequest(Request.Method.GET, NTUENDPOINT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                feedList.clear();
                feedList.addAll(Arrays.asList(gson.fromJson(response, FeedItem[].class)));
                recyclerView.setAdapter(feedsAdapter);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("PostActivity", error.toString());
            }
        });

        requestQueue.add(request);
    }
}
