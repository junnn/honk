package bloop.honk.Fragments;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import bloop.honk.CameraComponents.CamItem;
import bloop.honk.CameraComponents.CameraController;
import bloop.honk.CameraComponents.CamsAdapter;
import bloop.honk.R;

/**
 * Created by Jun Hao Ng on 6/9/2017.
 */

public class CamerasFragment extends Fragment {

    private RecyclerView recyclerView;
    private CamsAdapter camsAdapter;
    private CameraController cameraController;
    View view;
    private List<CamItem> cams = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_cameras, container, false);
        getActivity().setTitle("Cameras");//set the title on the toolbar


        camsAdapter = new CamsAdapter(getActivity(), cams);

        cameraController = new CameraController(getActivity(), camsAdapter);

        recyclerView = view.findViewById(R.id.camrecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        if (isNetworkConnected())
            cameraController.fetchCams(recyclerView, cams);
        else
            Toast.makeText(getActivity(), "No Network", Toast.LENGTH_SHORT).show();

        recyclerView = view.findViewById(R.id.camrecycler);

        return view;
    }

    public boolean isNetworkConnected() {
        final ConnectivityManager conMgr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo activeNetwork = conMgr.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.getState() == NetworkInfo.State.CONNECTED;
    }


}