package bloop.honk.Model;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import bloop.honk.Controller.XmlParser;
import bloop.honk.R;
import bloop.honk.View.NewsAdapter;

/**
 * Created by Don on 25/10/2017.
 */

public class NewsManager {

    public void fetchNews(final RecyclerView recyclerView, final Activity activity, final NewsAdapter adapter, final List<News> news) {
        new DownloadXmlTask(activity, adapter, recyclerView, news).execute(Config.NEWS_URL);
    }

    public static class DownloadXmlTask extends AsyncTask<String, Void, String> {

        private Context context;
        private NewsAdapter newsAdapter;
        private RecyclerView recycler;
        private List<News> news;

        public DownloadXmlTask(Context context, NewsAdapter newsAdapter, RecyclerView recyclerView, List<News> news) {
            this.context = context;
            this.newsAdapter = newsAdapter;
            this.recycler = recyclerView;
            this.news = news;
        }

        @Override
        protected String doInBackground(String... urls) {
            try {
                return loadXmlFromNetwork(urls[0], news);
            } catch (IOException e) {
                return context.getResources().getString(R.string.connection_error);
            } catch (XmlPullParserException e) {
                return context.getResources().getString(R.string.xml_error);
            }
        }

        @Override
        protected void onPostExecute(String result) {
            newsAdapter.notifyDataSetChanged();
            //recycler.setAdapter(newsAdapter);
        }
    }

    private static String loadXmlFromNetwork(String urlString, List<News> news) throws XmlPullParserException, IOException {
        InputStream stream = null;
        XmlParser XmlParser = new XmlParser();
        news.clear();

        try {
            stream = downloadUrl(urlString);
            news.addAll(XmlParser.parse(stream));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (stream != null) {
                // Makes sure that the InputStream is closed after the app is
                // finished using it.
                stream.close();
            }
        }
        return "";
    }

    private static InputStream downloadUrl(String urlString) throws IOException {
        java.net.URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(10000 /* milliseconds */);
        conn.setConnectTimeout(15000 /* milliseconds */);
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        // Starts the query
        conn.connect();
        return conn.getInputStream();
    }
}
