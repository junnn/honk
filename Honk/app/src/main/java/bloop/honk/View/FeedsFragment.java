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

import bloop.honk.Model.FeedItem;
import bloop.honk.Controller.FeedsAdapter;
import bloop.honk.Controller.TrafficFeedController;
import bloop.honk.R;

/**
 * Created by Jun Hao Ng on 6/9/2017.
 */

public class FeedsFragment extends Fragment {
    public List<FeedItem> posts = new ArrayList<>();
    private RecyclerView recyclerView;

    private FeedsAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feeds, container, false);
        getActivity().setTitle("Feeds");//set the title on the toolbar
        adapter= new FeedsAdapter(getContext(), posts);
        recyclerView = view.findViewById(R.id.feedrecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        TrafficFeedController TrafficFeedController = new TrafficFeedController(getActivity(), adapter);

        if (isNetworkConnected())
            TrafficFeedController.fetchPost(recyclerView,posts);

        else
            Toast.makeText(getActivity(), "No Network", Toast.LENGTH_SHORT).show();

        return view;
    }

    public boolean isNetworkConnected() {
        final ConnectivityManager conMgr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo activeNetwork = conMgr.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.getState() == NetworkInfo.State.CONNECTED;
    }
}
