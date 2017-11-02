package bloop.honk.View;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import bloop.honk.Controller.NewsAdapter;
import bloop.honk.Controller.NewsController;
import bloop.honk.R;


public class NewsFragment extends Fragment {

    private RecyclerView recyclerView;
    private NewsAdapter adapter;

    //List<News> newsList = new ArrayList<News>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news, container, false);
        getActivity().setTitle("News");//set the title on the toolbar

        NewsController con = new NewsController(getActivity());

        recyclerView = view.findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new NewsAdapter(getActivity(), con.getNewsList());

        if (isNetworkConnected()) {
            con.fetchNews(getActivity(), adapter);

            //new NewsManager.DownloadXmlTask(getActivity(), adapter).execute(URL);
            //task.execute(URL);
            //new NewsManager.DownloadXmlTask(getActivity()).execute(URL);
        }
        else
            Toast.makeText(getActivity(), "No Network", Toast.LENGTH_SHORT).show();

        recyclerView.swapAdapter(adapter, true);

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

        return view;
    }

    public boolean isNetworkConnected() {
        final ConnectivityManager conMgr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo activeNetwork = conMgr.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.getState() == NetworkInfo.State.CONNECTED;
    }

}