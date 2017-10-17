package bloop.honk.Fragments;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import bloop.honk.NewsComponents.Entry;
import bloop.honk.NewsComponents.NewsAdapter;
import bloop.honk.NewsComponents.XmlParser;
import bloop.honk.R;

public class NewsFragment extends Fragment{
    private static final String URL = "https://www.lta.gov.sg/apps/news/feed.aspx?svc=getnews&contenttype=rss&count=20&category=1&category=2&category=3";
    private RecyclerView recyclerView;
    private NewsAdapter adapter;

    List<Entry> entries;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news, container, false);
        getActivity().setTitle("News");//set the title on the toolbar

        loadPage();

        recyclerView = view.findViewById(R.id.recycler);

        return view;
    }

    private void loadPage() {
        new DownloadXmlTask(getActivity()).execute(URL);
    }

    private class DownloadXmlTask extends AsyncTask<String, Void, String> {

        private Context context;

        public DownloadXmlTask(Context context){
            this.context = context;
        }

        @Override
        protected String doInBackground(String... urls) {
            try {
                return loadXmlFromNetwork(urls[0]);
            } catch (IOException e) {
                return getResources().getString(R.string.connection_error);
            } catch (XmlPullParserException e) {
                return getResources().getString(R.string.xml_error);
            }
        }

        @Override
        protected void onPostExecute(String result) {
            adapter = new NewsAdapter(context, entries);
            //adapter.setClickListener(this);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        }

    }

    private String loadXmlFromNetwork(String urlString) throws XmlPullParserException, IOException {
        InputStream stream = null;
        XmlParser XmlParser = new XmlParser();
        entries = null;

        try {
            stream = downloadUrl(urlString);
            entries = XmlParser.parse(stream);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        finally {
            if (stream != null) {
                // Makes sure that the InputStream is closed after the app is
                // finished using it.
                stream.close();
            }
        }
        return "";
    }

    private InputStream downloadUrl(String urlString) throws IOException {
        java.net.URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(10000 /* milliseconds */);
        conn.setConnectTimeout(15000 /* milliseconds */);
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        // Starts the query
        conn.connect();
        InputStream stream = conn.getInputStream();
        return stream;
    }

}