package bloop.honk.Controller;


import android.util.Log;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import java.util.Arrays;
import java.util.List;
import bloop.honk.FavouritesComponents.Bookmark;

/**
 * Created by Bryan Boey S-15 on 22/10/2017.
 */
//TRIED MAKING but complete failure
public class BookmarkController {
    private List<Bookmark> posts;
    private Gson gson;
    private static final String readBk = "http://172.21.148.166/example/dao/Hookdaoimpl.php?function=getBookMark&username=admin";


    public List<Bookmark> getBookmarkList(String username,RequestQueue r) {
        Response.Listener<String> onPostsLoaded = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                posts = Arrays.asList(gson.fromJson(response, Bookmark[].class));
            }
        };

        Response.ErrorListener onPostsError = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("PostActivity", error.toString());
            }
        };
        StringRequest request = new StringRequest(Request.Method.GET, readBk, onPostsLoaded, onPostsError);
        r.add(request);
        while(posts == null);
        return posts;
    }

}
