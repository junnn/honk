package bloop.honk.Controller;

import android.app.Activity;

import java.util.List;

import bloop.honk.Model.News;
import bloop.honk.Model.Config;
import bloop.honk.View.NewsAdapter;

/**
 * Created by Don on 27/10/2017.
 */

public class NewsController {

    private NewsManager man;

    public NewsController(Activity activity) {
        man = new NewsManager(activity);
    }

    public void fetchNews(Activity activity, NewsAdapter adapter){
        new NewsManager.DownloadXmlTask(activity, adapter).execute(Config.NEWS_URL);
    }

    public List<News> getNewsList() {
        return man.getNewsList();
    }
}

