package bloop.honk.Controller;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import bloop.honk.Model.CamItem;
import bloop.honk.Model.CameraManager;
import bloop.honk.View.CamsAdapter;


public class CameraController {
    private CameraManager man;

    public CameraController(Activity activity, CamsAdapter feedsAdapter) {
        man = new CameraManager(activity, feedsAdapter);
    }

    public void fetchCams(RecyclerView recyclerView, List<CamItem> posts) {
        man.fetchCams(recyclerView, posts);
    }
}