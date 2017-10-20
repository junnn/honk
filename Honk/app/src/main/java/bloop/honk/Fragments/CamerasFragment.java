package bloop.honk.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bloop.honk.CameraComponents.CamItem;
import bloop.honk.CameraComponents.CamsAdapter;
import bloop.honk.CameraComponents.RoadsAdapter;
import bloop.honk.R;
/**
 * Created by Jun Hao Ng on 6/9/2017.
 */

public class CamerasFragment extends Fragment {
    private static final String ENDPOINT = "http://datamall2.mytransport.sg/ltaodataservice/Traffic-Images";

    private RequestQueue requestQueue;
    private Gson gson;
    private RecyclerView recyclerView;
    private CamsAdapter camadapter;
    private RoadsAdapter roadadapter;
    Context context;
    static View view;
    List<CamItem> cams;
    List<String> roads = new ArrayList<>();

    public List<CamItem> getCams(){
        return cams;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_cameras_road, container, false);
        getActivity().setTitle("Cameras");//set the title on the toolbar

        requestQueue = Volley.newRequestQueue(getActivity());

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("M/d/yy hh:mm a");
        gson = gsonBuilder.create();


        fetchCams();
        recyclerView = view.findViewById(R.id.roadrecycler);
        //recyclerView = view.findViewById(R.id.camrecycler);


        roads.add("BKE");
        roads.add("SLE");

        roadadapter = new RoadsAdapter(getActivity(), roads);
        recyclerView.setAdapter(roadadapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        return view;
    }

    public void setViewLayout(int id){
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(id, null);
        ViewGroup rootView = (ViewGroup) getView();
        rootView.removeAllViews();
        rootView.addView(view);
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
            String json = response.substring(response.indexOf("["), response.length() - 1);

            cams = Arrays.asList(gson.fromJson(json, CamItem[].class));


//            camadapter = new CamsAdapter(getActivity(), cams);
//            recyclerView.setAdapter(camadapter);
            // recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


            roadadapter.setClickListener(new RoadsAdapter.ItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {

                    setViewLayout(R.layout.fragment_cameras);
                    recyclerView = view.findViewById(R.id.camrecycler);
                    camadapter = new CamsAdapter(getActivity(), cams);
                    recyclerView.setAdapter(camadapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    Toast.makeText(getActivity(), "camadapter.getItem(position)", Toast.LENGTH_SHORT).show();
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
}