package bloop.honk.Controller;

import android.app.Activity;

import java.util.List;

import bloop.honk.Model.News;
import bloop.honk.Model.NewsManager;
import bloop.honk.View.NewsAdapter;
import bloop.honk.View.NewsFragment;

public class NewsController {

    private NewsAdapter adapter;
    private Activity activity;
    private NewsManager newsMgr;

    public NewsController(Activity activity, NewsAdapter adapter) {
        this.activity = activity;
        this.adapter = adapter;
        this.newsMgr = new NewsManager();
    }

    public void fetchNews(final List<News> news) {
        newsMgr.fetchNews(activity, adapter, news);
    }

    public void launchWebView(NewsFragment newsFragment, String url) {
        newsMgr.launchWebView(newsFragment, url);
    }
}
