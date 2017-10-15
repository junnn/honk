package bloop.honk.Fragments;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import bloop.honk.R;
/**
 * Created by Jun Hao Ng on 6/9/2017.
 */

public class CamerasFragment extends Fragment {
    private static final String ENDPOINT = "http://datamall2.mytransport.sg/ltaodataservice/Traffic-Images";

    private RequestQueue requestQueue;
    private Gson gson;
    private RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cameras, container, false);
        getActivity().setTitle("Cameras");//set the title on the toolbar

        requestQueue = Volley.newRequestQueue(getActivity());

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("M/d/yy hh:mm a");
        gson = gsonBuilder.create();

        fetchCams();

        return view;
    }


    private void fetchCams() {
        StringRequest request = new StringRequest(Request.Method.GET, ENDPOINT, onPostsLoaded, onPostsError) {
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

    private final Response.Listener<String> onPostsLoaded = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {

            Log.i("PostActivity", response.toString());
            String json = response.substring(response.indexOf("["), response.length()-1);

            List<CamItem> cams = Arrays.asList(gson.fromJson(json, CamItem[].class));

            for (CamItem cam : cams) {
                Log.i("PostActivity", cam.getCameraID() + ": " + cam.getImageLink());
/*
                Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
                double lat = cam.getLatitude();
                double lng = cam.getLongitude();
                try {
                    List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
                    Log.i("PostActivity", cam.getCameraID() + ": " + addresses.get(0));
                } catch (IOException e) {
                    e.printStackTrace();
                }
               */
            }
        }
    };

    private final Response.ErrorListener onPostsError = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e("PostActivity", error.toString());
        }
    };
}