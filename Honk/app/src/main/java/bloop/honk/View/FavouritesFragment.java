package bloop.honk.View;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.RequestQueue;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import bloop.honk.Controller.BookmarkController;
import bloop.honk.Model.Bookmark;
import bloop.honk.Model.Config;
import bloop.honk.Controller.BookmarkAdapter;
import bloop.honk.R;

/**
 * Created by Jun Hao Ng on 6/9/2017.
 */

public class FavouritesFragment extends Fragment {

    private List<Bookmark> posts = new ArrayList<>();
    private Bookmark bob = new Bookmark("bob","1","2");
    private Gson gson;
    private RecyclerView recyclerView;
    private RequestQueue requestQueue;
    private static BookmarkController bookmarkController;
    private BookmarkAdapter adapter;
    private SharedPreferences sharedPreferences;
    private String username;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle("Favourites");
        super.onCreate(savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_favourites, container, false);

        sharedPreferences = getActivity().getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);

        if(!sharedPreferences.getBoolean(Config.LOGGEDIN_SHARED_PREF, false)) {//if LOGGEDIN == false
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
        }
        else{
            username = sharedPreferences.getString(Config.USERNAME_SHARED_PREF,""); //used this to get current username

            recyclerView = (RecyclerView) rootView.findViewById(R.id.postview);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

            adapter = new BookmarkAdapter(getActivity(), posts);
            bookmarkController = new BookmarkController(getActivity(), adapter);
            bookmarkController.getBookmarkList(username, recyclerView, posts);

            adapter.setClickListener(new BookmarkAdapter.ItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    switch (view.getId()) {
                        case R.id.favImageButton:
                            bookmarkController.deleteBookmark(username,posts,recyclerView, adapter.getItem(position).getName());
                            //bookmarkController.getBookmarkList(username, recyclerView, posts);
                            break;
                        default:
                            Bookmark bookmark = adapter.getItem(position);
                            String latLng = bookmark.getLatitude() + "," + bookmark.getLongitude();
                            Uri gmmIntentUri = Uri.parse("google.navigation:q=" + latLng);
                            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                            mapIntent.setPackage("com.google.android.apps.maps");
                            startActivity(mapIntent);
                    }
                }
            });
        }


        return rootView;
    }

    @Override
    public void onResume(){ //if user press back on favourite, will redirect to news instead of favorite fragment
        super.onResume();
        if(!sharedPreferences.getBoolean(Config.LOGGEDIN_SHARED_PREF, false)){
            Fragment fragment = new NewsFragment();

            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            // ft.addToBackStack(null); //uncomment to enable backpress to return to previous fragment
            ft.replace(R.id.main_frame_container, fragment);
            ft.commit();
        }
    }
}

