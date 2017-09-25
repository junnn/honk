package bloop.honk.Fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import bloop.honk.Fragments.XmlParser.Entry;
import bloop.honk.R;

public class NewsFragment extends Fragment implements NewsAdapter.ItemClickListener{
    private RecyclerView recyclerView;
    private NewsAdapter adapter;
    List<XmlParser.Entry> entries;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_news, container, false);
        getActivity().setTitle("News");//set the title on the toolbar
        loadPage();
        //getActivity().setContentView(R.layout.sample_main);

        recyclerView = layout.findViewById(R.id.recycler);


        return layout;
    }

    private static final String URL =
            "https://www.lta.gov.sg/apps/news/feed.aspx?svc=getnews&contenttype=rss&count=10&category=2";

    private void loadPage() {
        new DownloadXmlTask().execute(URL);
    }

    private class DownloadXmlTask extends AsyncTask<String, Void, String> {

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
            //setContentView(R.layout.main);
            // Displays the HTML string in the UI via a WebView
            //WebView myWebView = (WebView) findViewById(R.id.webview);
            //myWebView.loadData(result, "text/html", null);

            adapter = new NewsAdapter(getActivity(), entries);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        }
    }

    private String loadXmlFromNetwork(String urlString) throws XmlPullParserException, IOException {
        InputStream stream = null;
        XmlParser XmlParser = new XmlParser();
        entries = null;
        String title = null;
        String url = null;
        String summary = null;

        try {
            stream = downloadUrl(urlString);
            entries = XmlParser.parse(stream);
            // Makes sure that the InputStream is closed after the app is
            // finished using it.
           // for (Entry entry : entries) {
            //    Log.i("Entry", entry.getLink());
           // }
        }
        //catch (Exception e){
        //    e.printStackTrace();
        //}
        finally {
            if (stream != null) {
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

    public void onItemClick(View view, int position){
        Toast.makeText(getActivity(), adapter.getLink(position), Toast.LENGTH_LONG);
    }
}