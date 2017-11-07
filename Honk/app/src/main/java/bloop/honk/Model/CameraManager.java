package bloop.honk.Model;


import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
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

import bloop.honk.View.CamsAdapter;

/**
 * Created by Don on 25/10/2017.
 */

public class CameraManager {
    private Activity activity;
    private CamsAdapter camsAdapter;
    private RequestQueue requestQueue;
    private Gson gson;

    public CameraManager(Activity activity, CamsAdapter camsAdapter) {
        this.activity = activity;
        this.camsAdapter = camsAdapter;
    }

    public void fetchCams(final RecyclerView recyclerView, final List<CamItem> cameraList) {
        requestQueue = Volley.newRequestQueue(activity);

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("M/d/yy hh:mm a");
        gson = gsonBuilder.create();

        StringRequest request = new StringRequest(Request.Method.GET, Config.CAMERA_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String json = response.substring(response.indexOf("["), response.length() - 1);

                cameraList.clear();
                cameraList.addAll(Arrays.asList(gson.fromJson(json, CamItem[].class)));
                recyclerView.setAdapter(camsAdapter);


                camsAdapter.setClickListener(new CamsAdapter.ItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Toast.makeText(activity, camsAdapter.getItem(position).getCameraID(), Toast.LENGTH_SHORT).show();
                    }
                });

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("PostActivity", error.toString());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                //Map<String, String> params = new HashMap<String, String>();
                HashMap<String, String> headers = new HashMap<>();
                headers.put("AccountKey", "prxNO+dOSVaCs0F5/UX0rw==");
                headers.put("accept", "application/json");
                return headers;
            }
        };
        requestQueue.add(request);
    }
}
