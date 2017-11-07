package bloop.honk.Controller;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import bloop.honk.Model.FeedItem;
import bloop.honk.Model.TrafficFeedManager;
import bloop.honk.View.FeedsAdapter;

public class TrafficFeedController {
    private TrafficFeedManager man;

    public TrafficFeedController(Activity activity, FeedsAdapter feedsAdapter) {
        man = new TrafficFeedManager(activity, feedsAdapter);
    }

    public void fetchPost(RecyclerView recyclerView, List<FeedItem> posts) {
        man.fetchPosts(recyclerView, posts);
    }
}
