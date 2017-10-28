package bloop.honk;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import bloop.honk.Controller.AuthController;
import bloop.honk.Fragments.AdminFragment;
import bloop.honk.Model.Account;

/**
 * Created by Jun Hao Ng on 28/10/2017.
 */

public class AdminActivity extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    private Menu menu;
    private AuthController authController;
    private Account account;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        authController = new AuthController();

        //Creating a shared preference
        sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);

        if(sharedPreferences.getBoolean(Config.LOGGEDIN_SHARED_PREF, false)) {
            account = new Account(sharedPreferences.getString(Config.USERNAME_SHARED_PREF, ""));
        }
        else{
            account = null;
        }

        Fragment fragment = new AdminFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        // ft.addToBackStack(null); //uncomment to enable backpress to return to previous fragment
        ft.replace(R.id.admin_frame_container, fragment);
        ft.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Adding our menu to toolbar (top tool bar)
        this.menu = menu;
        getMenuInflater().inflate(R.menu.menu_header, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menuLogout) {
            //calling logout method when the logout button is clicked
            authController.logout(account, sharedPreferences);
            finish();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        else if(id == R.id.menuQuit){
            quitApp();
        }
        return super.onOptionsItemSelected(item);
    }

    public void quitApp(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Are you sure you want to quit?");
        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent homeIntent = new Intent(Intent.ACTION_MAIN);
                homeIntent.addCategory( Intent.CATEGORY_HOME );
                homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(homeIntent);
            }
        });
        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        //Showing the alert dialog
        alertDialogBuilder.create().show();
    }
}
