package bloop.honk.FeedsComponents;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;

import java.util.List;

/**
 * Created by Don on 27/10/2017.
 */

public class TrafficFeedController {
    private TrafficFeedManager man;

    public TrafficFeedController(Activity activity, FeedsAdapter feedsAdapter) {
         man = new TrafficFeedManager(activity, feedsAdapter);
    }

    public void fetchPost(RecyclerView recyclerView, List<FeedItem> posts){
         man.fetchPosts(recyclerView, posts);
    }
}
