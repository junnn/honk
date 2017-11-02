package bloop.honk.View;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import bloop.honk.Model.News;
import bloop.honk.Controller.NewsAdapter;
import bloop.honk.Controller.XmlParser;
import bloop.honk.R;

public class oldNewsFragment extends Fragment {
    private static final String URL = "https://www.lta.gov.sg/apps/news/feed.aspx?svc=getnews&contenttype=rss&count=10&category=1&category=2&category=3";
    private RecyclerView recyclerView;
    private NewsAdapter adapter;

    List<News> entries;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news, container, false);
        getActivity().setTitle("News");//set the title on the toolbar

        if (isNetworkConnected())
            loadNews();
        else
            Toast.makeText(getActivity(), "No Network", Toast.LENGTH_SHORT).show();

        recyclerView = view.findViewById(R.id.recycler);

        return view;
    }

    public boolean isNetworkConnected() {
        final ConnectivityManager conMgr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo activeNetwork = conMgr.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.getState() == NetworkInfo.State.CONNECTED;
    }

    private void loadNews() {
        new DownloadXmlTask(getActivity()).execute(URL);
    }

    private class DownloadXmlTask extends AsyncTask<String, Void, String> {

        private Context context;

        public DownloadXmlTask(Context context) {
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

            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            adapter.setClickListener(new NewsAdapter.ItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    String url = "http://" + adapter.getItem(position).getLink();

                    Bundle bundle = new Bundle();
                    bundle.putString("Link", url);

                    Fragment fragment = new WebViewFragment();
                    fragment.setArguments(bundle);
                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                    ft.addToBackStack(null); //uncomment to enable backpress to return to previous fragment
                    ft.replace(R.id.main_frame_container, fragment);
                    ft.commit();
                }
            });
        }
    }

    private String loadXmlFromNetwork(String urlString) throws XmlPullParserException, IOException {
        InputStream stream = null;
        XmlParser XmlParser = new XmlParser();
        entries = null;

        try {
            stream = downloadUrl(urlString);
            entries = XmlParser.parse(stream);
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

    private InputStream downloadUrl(String urlString) throws IOException {
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