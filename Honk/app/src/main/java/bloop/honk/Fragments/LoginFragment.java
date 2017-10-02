package bloop.honk.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import bloop.honk.Config;
import bloop.honk.R;

/**
 * Created by Jun Hao Ng on 22/9/2017.
 */

public class LoginFragment extends Fragment {
    private TextView registerLink;
    private EditText usernameEditText, passwordEditText;
    private Button loginButton;

    private SharedPreferences sharedPreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        //Creating a shared preference
        sharedPreferences = getActivity().getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);

        //init all widgets
        findAllViewById(view);

        registerLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchToRegisterFragment();
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = usernameEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();

                login(username, password);
            }
        });

        return view;
    }


    //switch to register fragment
    private void switchToRegisterFragment(){
        Fragment fragment = new RegisterFragment();
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.addToBackStack(null); //enable "backpress" to return to previous fragment
        ft.replace(R.id.login_frame_container, fragment);
        ft.commit();
    }

    //initialise all buttons, edittext, textview & etc here
    private void findAllViewById(View view){

        usernameEditText = view.findViewById(R.id.username_editText);
        passwordEditText = view.findViewById(R.id.password_editText);
        loginButton = view.findViewById(R.id.login_button);
        registerLink = view.findViewById(R.id.register_text_view);

    }

    //login function
    private void login(String username, String password){
        //FeedItem request
        boolean response = true;

        if(response){
            //Creating editor to store values to shared preferences
            SharedPreferences.Editor editor = sharedPreferences.edit();

            //Adding values to editor
            editor.putBoolean(Config.LOGGEDIN_SHARED_PREF, true);
            editor.putString(Config.USERNAME_SHARED_PREF, username);
            //Saving values to editor
            editor.apply();

            Toast.makeText(getActivity(), "Success" +  sharedPreferences.getString(Config.USERNAME_SHARED_PREF, ""), Toast.LENGTH_SHORT).show();
            getActivity().finish();
        }
        else{
            Toast.makeText(getActivity(), "Failure", Toast.LENGTH_SHORT).show();
        }
    }

}
