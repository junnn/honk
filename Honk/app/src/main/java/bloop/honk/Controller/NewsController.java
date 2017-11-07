package bloop.honk.Controller;

import android.app.Activity;

import java.util.List;

import bloop.honk.Model.News;
import bloop.honk.Model.Config;
import bloop.honk.Model.NewsManager;
import bloop.honk.View.NewsAdapter;

/**
 * Created by Don on 27/10/2017.
 */

public class NewsController {

    private NewsAdapter adapter;
    private Activity activity;
    private NewsManager newsMgr = new NewsManager();

    public NewsController(Activity activity, NewsAdapter adapter) {
        this.activity = activity;
        this.adapter = adapter;

    }

    public void fetchNews(RecyclerView recyclerView){
        newMgr.fetchNews(recyclerView, activity, adapter)
        //new NewsManager.DownloadXmlTask(activity, adapter).execute(Config.NEWS_URL);
    }
}

