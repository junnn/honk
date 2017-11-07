package bloop.honk.Model;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import bloop.honk.Controller.DownloadUrl;
import bloop.honk.Controller.XmlParser;
import bloop.honk.R;
import bloop.honk.View.NewsAdapter;
import bloop.honk.View.NewsFragment;
import bloop.honk.View.WebViewFragment;

public class NewsManager {

    public void fetchNews(Activity activity, NewsAdapter adapter, List<News> news) {
        new DownloadXmlTask(activity, adapter, news).execute(Config.NEWS_URL);
    }

    public void launchWebView(NewsFragment newsFragment, String url) {
        Bundle bundle = new Bundle();
        bundle.putString("Link", url);

        Fragment fragment = new WebViewFragment();
        fragment.setArguments(bundle);
        FragmentTransaction ft = ((NewsFragment)newsFragment).getActivity().getSupportFragmentManager().beginTransaction();
        ft.addToBackStack(null); //uncomment to enable backpress to return to previous fragment
        ft.replace(R.id.main_frame_container, fragment);
        ft.commit();
    }

    public static class DownloadXmlTask extends AsyncTask<String, Void, String> {
        private Context context;
        private NewsAdapter newsAdapter;
        private List<News> news;

        public DownloadXmlTask(Context context, NewsAdapter newsAdapter, List<News> news) {
            this.context = context;
            this.newsAdapter = newsAdapter;
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
        }
    }

    private static String loadXmlFromNetwork(String urlString, List<News> news) throws XmlPullParserException, IOException {
        InputStream stream = null;
        XmlParser XmlParser = new XmlParser();
        news.clear();
        DownloadUrl downloadUrl = new DownloadUrl();
        try {
            stream = downloadUrl.readXML(urlString);
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
}
