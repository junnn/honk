package bloop.honk.Controller;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import bloop.honk.Model.CamItem;


/**
 * Created by Don on 27/10/2017.
 */

public class CameraController {
    private CameraManager man;

    public CameraController(Activity activity, CamsAdapter feedsAdapter) {
        man = new CameraManager(activity, feedsAdapter);
    }

    public void fetchCams(RecyclerView recyclerView, List<CamItem> posts){
        man.fetchCams(recyclerView, posts);
    }
}