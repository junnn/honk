package bloop.honk;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SlidingDrawer;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import java.security.SecureRandom;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import bloop.honk.Fragments.CamerasFragment;
import bloop.honk.Fragments.FavouritesFragment;
import bloop.honk.Fragments.FeedsFragment;
import bloop.honk.Fragments.MapFragment;
import bloop.honk.Fragments.NewsFragment;
import bloop.honk.MapComponents.GetAddress;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private SharedPreferences sharedPreferences;
    private Menu menu;
    private NavigationView navigationView;
    static final int LOGIN_REGISTER_REQUEST_CODE = 1;
    private final int REQUEST_CHECK_SETTINGS = 101;
    private MapFragment mapfrag = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //handle SSLHandshakeException due to Self-signed server certificate
        handleSSLHandshake();

        //Creating a shared preference
        sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        switchToSelectedFragment(R.id.nav_news);//the "default" page user will see upon logging in
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        //switch fragment based on selection
        switchToSelectedFragment(item.getItemId());

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    @Override
    public void onResume(){
        super.onResume();
        if(sharedPreferences.getBoolean(Config.LOGGEDIN_SHARED_PREF, false)){
            menu = navigationView.getMenu();
            menu.findItem(R.id.nav_login).setTitle("Log Out");

            Fragment fragment = new NewsFragment();

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            // ft.addToBackStack(null); //uncomment to enable backpress to return to previous fragment
            ft.replace(R.id.main_frame_container, fragment);
            ft.commit();
        }
        else{
            menu = navigationView.getMenu();
            menu.findItem(R.id.nav_login).setTitle("Login");
        }
    }

    private void switchToSelectedFragment(int itemId){

        //creating fragment object
        Fragment fragment = null;

        //initializing the fragment object which is selected
        switch (itemId) {
            case R.id.nav_news:
                fragment = new NewsFragment();
                break;
            case R.id.nav_feed:
                fragment = new FeedsFragment();
                break;
            case R.id.nav_cameras:
                fragment = new CamerasFragment();
                break;
            case R.id.nav_maps:
                fragment = new MapFragment();
                mapfrag = (MapFragment) fragment;
                break;
            case R.id.nav_favourites:
                fragment = new FavouritesFragment();
                break;
            case R.id.nav_login:
                if(sharedPreferences.getBoolean(Config.LOGGEDIN_SHARED_PREF, false)){//if LOGGEDIN == true
                    //Creating editor to store values to shared preferences
                    SharedPreferences.Editor editor = sharedPreferences.edit();

                    //clear values of editor
                    editor.putBoolean(Config.LOGGEDIN_SHARED_PREF, false);
                    editor.putString(Config.USERNAME_SHARED_PREF, "");
                    editor.putString(Config.ROLE_SHARED_PREF, "");
                    //Saving values to editor
                    editor.apply();

                    menu = navigationView.getMenu();
                    menu.findItem(R.id.nav_login).setTitle("Login");

                    Toast.makeText(this, "Logged out...", Toast.LENGTH_SHORT).show();
                    fragment = new NewsFragment();
                    break;
                }
                else{
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivityForResult(intent, LOGIN_REGISTER_REQUEST_CODE);
                    break;
                }

        }

        //replacing the fragment
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            // ft.addToBackStack(null); //uncomment to enable backpress to return to previous fragment
            ft.replace(R.id.main_frame_container, fragment);
            ft.commit();
        }
    }

    /**
     * Enables https connections
     */
    @SuppressLint("TrulyRandom")
    public static void handleSSLHandshake() {
        try {
            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }

                @Override
                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }};

            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String arg0, SSLSession arg1) {
                    return true;
                }
            });
        } catch (Exception ignored) {
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode != RESULT_CANCELED && data !=null){
            switch (requestCode) {
                // Check for the integer request code originally supplied to startResolutionForResult().
                case REQUEST_CHECK_SETTINGS:
                    switch (resultCode) {
                        case Activity.RESULT_OK:
                            mapfrag.setPermissionGranted(true);
                            mapfrag.requestLocationUpdates();
                            // Set up autoCompletePlaces here
                            mapfrag.setupAutoCompletePlaces();
                            break;
                    }
                    break;
            }
        } else {
            mapfrag.setPermissionGranted(false);
            Toast.makeText(this, "This application requires LocationInfo Service to be turned on!", Toast.LENGTH_LONG).show();
        }
    }

}
