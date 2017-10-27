package bloop.honk.NewsComponents;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import bloop.honk.Fragments.WebViewFragment;
import bloop.honk.R;

/**
 * Created by Don on 25/10/2017.
 */

public class NewsController {
    private static final String URL = "https://www.lta.gov.sg/apps/news/feed.aspx?svc=getnews&contenttype=rss&count=20&category=1&category=2&category=3";

    private static List<News> newsList = new ArrayList<News>();

    public static List<News> getNewsList() {
        return newsList;
    }

    private Context context;

    public NewsController(Context context) {
        this.context = context;
    }


    public static class DownloadXmlTask extends AsyncTask<String, Void, String> {

        private Context context;

        public DownloadXmlTask(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(String... urls) {
            try {
                return loadXmlFromNetwork(urls[0]);
            } catch (IOException e) {
                return context.getResources().getString(R.string.connection_error);
            } catch (XmlPullParserException e) {
                return context.getResources().getString(R.string.xml_error);
            }
        }

        @Override
        protected void onPostExecute(String result) {

        }
    }

    private static String loadXmlFromNetwork(String urlString) throws XmlPullParserException, IOException {
        InputStream stream = null;
        XmlParser XmlParser = new XmlParser();
        newsList = null;

        try {
            stream = downloadUrl(urlString);
            newsList = XmlParser.parse(stream);
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
