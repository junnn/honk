package bloop.honk.View;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import bloop.honk.Controller.TrafficFeedController;
import bloop.honk.Model.FeedItem;
import bloop.honk.R;

public class FeedsFragment extends Fragment {
    public List<FeedItem> posts = new ArrayList<>();
    private RecyclerView recyclerView;

    private FeedsAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feeds, container, false);
        getActivity().setTitle("Feeds");
        adapter = new FeedsAdapter(getContext(), posts);
        recyclerView = view.findViewById(R.id.feedrecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        TrafficFeedController TrafficFeedController = new TrafficFeedController(getActivity(), adapter);

        TrafficFeedController.fetchPost(recyclerView, posts);

        return view;
    }
}
