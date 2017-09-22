package bloop.honk;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import bloop.honk.Fragments.LoginFragment;

/**
 * Created by Jun Hao Ng on 22/9/2017.
 */

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Fragment fragment = new LoginFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        // ft.addToBackStack(null); //uncomment to enable backpress to return to previous fragment
        ft.replace(R.id.login_frame_container, fragment);
        ft.commit();

    }

}
