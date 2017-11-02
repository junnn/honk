package bloop.honk.Controller;

import android.app.Activity;

import java.util.List;

import bloop.honk.Model.News;

/**
 * Created by Don on 27/10/2017.
 */

public class NewsController {
    private static final String URL = "https://www.lta.gov.sg/apps/news/feed.aspx?svc=getnews&contenttype=rss&count=10&category=1&category=2&category=3";

    private NewsManager man;

    public NewsController(Activity activity) {
        man = new NewsManager(activity);
    }

    public void fetchNews(Activity activity, NewsAdapter adapter){
        new NewsManager.DownloadXmlTask(activity, adapter).execute(URL);
    }

    public List<News> getNewsList() {
        return man.getNewsList();
    }
}

