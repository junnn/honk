package bloop.honk;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SlidingDrawer;
import android.widget.Toast;

import bloop.honk.Fragments.CamerasFragment;
import bloop.honk.Fragments.FavouritesFragment;
import bloop.honk.Fragments.FeedsFragment;
import bloop.honk.Fragments.MapFragment;
import bloop.honk.Fragments.NewsFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private SharedPreferences sharedPreferences;
    private Menu menu;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
                break;
            case R.id.nav_favourites:
                fragment = new FavouritesFragment();
                break;
            case R.id.nav_login:
                if(sharedPreferences.getBoolean(Config.LOGGEDIN_SHARED_PREF, false)){
                    //Creating editor to store values to shared preferences
                    SharedPreferences.Editor editor = sharedPreferences.edit();

                    //Adding values to editor
                    editor.putBoolean(Config.LOGGEDIN_SHARED_PREF, false);
                    editor.putString(Config.USERNAME_SHARED_PREF, "");
                    //Saving values to editor
                    editor.apply();

                    menu = navigationView.getMenu();
                    menu.findItem(R.id.nav_login).setTitle("Login");

                    Toast.makeText(this, "Logged out...", Toast.LENGTH_SHORT).show();
                    break;
                }
                else{
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
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
}
