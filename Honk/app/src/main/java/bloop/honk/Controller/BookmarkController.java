package bloop.honk.Controller;


import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bloop.honk.FavouritesComponents.Bookmark;
import bloop.honk.FavouritesComponents.BookmarkAdapter;

/**
 * Created by Bryan Boey S-15 on 22/10/2017.
 */
//TRIED MAKING but complete failure
public class BookmarkController {
    private static List<Bookmark> posts = new ArrayList<Bookmark>();
    public static RequestQueue r; //test

    private BookmarkAdapter adapter;
    private Activity activity;
    private Bookmark bookmark = new Bookmark();

    public BookmarkController(Activity activity, BookmarkAdapter adapter) {
        this.activity = activity;
        this.adapter = adapter;
    }
    public void getBookmarkList(String username, final RecyclerView recyclerView, final List<Bookmark> posts) {
        bookmark.getBookmarkList(username, recyclerView, posts, activity, adapter);
    }

    public void deleteBookmark(final String username,final List<Bookmark> posts, final RecyclerView recyclerView, final String bkmk) {
        bookmark.deleteBookmark(username,recyclerView,posts, bkmk, activity,adapter);
    }
}
