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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

import bloop.honk.Controller.AuthController;
import bloop.honk.Config;
import bloop.honk.Model.Account;
import bloop.honk.R;

/**
 * Created by Jun Hao Ng on 22/9/2017.
 */

public class LoginFragment extends Fragment {
    private TextView registerLink;
    private EditText usernameEditText, passwordEditText;
    private Button loginButton;

    private SharedPreferences sharedPreferences;
    private AuthController authController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        //Creating a shared preference
        sharedPreferences = getActivity().getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        authController = new AuthController();

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
                if(usernameEditText.getText().toString().isEmpty() || passwordEditText.getText().toString().isEmpty()){
                    Toast.makeText(getContext(), "Please ensure all fields are filled.", Toast.LENGTH_SHORT).show();
                }
                else {
                    Account account = new Account(usernameEditText.getText().toString().trim(), passwordEditText.getText().toString().trim());
                    account.setPassword(authController.hashPassword(account));
                    authController.login(account, getActivity(), sharedPreferences);
                }
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
}

