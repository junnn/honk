package bloop.honk.View;

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

import bloop.honk.Controller.CameraController;
import bloop.honk.Model.CamItem;
import bloop.honk.R;

public class CamerasFragment extends Fragment {

    private RecyclerView recyclerView;
    private CamsAdapter camsAdapter;
    private CameraController cameraController;
    View view;
    private List<CamItem> cams = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_cameras, container, false);
        getActivity().setTitle("Cameras");


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