package bloop.honk.Controller;


import android.app.Activity;
import android.support.v7.widget.RecyclerView;

import com.android.volley.RequestQueue;

import java.util.List;

import bloop.honk.Model.Bookmark;

/**
 * Created by Bryan Boey S-15 on 22/10/2017.
 */
//TRIED MAKING but complete failure
public class BookmarkController {
    public static RequestQueue r; //test

    private BookmarkAdapter adapter;
    private Activity activity;
    private BookmarkManager bkMan = new BookmarkManager();

    public BookmarkController(Activity activity, BookmarkAdapter adapter) {
        this.activity = activity;
        this.adapter = adapter;
    }
    public void addBookmark(String username,String address, double lat, double lng ,final String url) {
        bkMan.addBookmark(username,address,lat,lng,url,activity);
    }
    public void getBookmarkList(String username, final RecyclerView recyclerView, final List<Bookmark> posts) {
        bkMan.getBookmarkList(username, recyclerView, posts, activity, adapter);
    }

    public void deleteBookmark(final String username,final List<Bookmark> posts, final RecyclerView recyclerView, final String bkmk) {
        bkMan.deleteBookmark(username,recyclerView,posts, bkmk, activity,adapter);
    }
}
