package bloop.honk.Model;

/**
 * Created by Jun Hao Ng on 22/9/2017.
 */

public class Config {
    //SHARED PREFERENCE CONFIG
    //name of our shared preferences
    public static final String SHARED_PREF_NAME = "Honk";
    public static final String USERNAME_SHARED_PREF = "username";
    public static final String LOGGEDIN_SHARED_PREF = "isloggedin";
    public static final String ROLE_SHARED_PREF = "role";
    public static final String NAME_SHARED_PREF = "name";

    //URL to register.php
    public static final String REGISTER_URL = "https://172.21.148.166/example/dao/Hookdaoimpl.php?function=register";
    //URL to register.php
    public static final String LOGIN_URL = "https://172.21.148.166/example/dao/Hookdaoimpl.php?function=login";
    //URL to add bookmark
    public static final String ADDBM_URL = "http://172.21.148.166/example/dao/Hookdaoimpl.php?function=addbookmark";
    //URL to delete bookmark
    public static final String DELBM_URL = "http://172.21.148.166/example/dao/Hookdaoimpl.php?function=deletebookmark";
    //URL to get list of bookmarks
    public static final String GETBM_URL = "http://172.21.148.166/example/dao/Hookdaoimpl.php?function=getBookMark&username=";
    //URL for camera
    public static final String CAMERA_URL = "http://datamall2.mytransport.sg/ltaodataservice/Traffic-Images";
    //URL for news
    public static final String NEWS_URL = "https://www.lta.gov.sg/apps/news/feed.aspx?svc=getnews&contenttype=rss&count=10&category=1&category=2&category=3";
    //URL for feed
    public static final String FEED_URL = "http://172.21.148.166/example/dao/Hookdaoimpl.php?function=getTrafficFeed";

    //JSON TAGS WHEN PARSING LOGIN & REGISTER REQUEST
    public static final String TAG_USERNAME = "username";
    public static final String TAG_PASSWORD = "pwd";
}
