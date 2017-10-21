package bloop.honk.Fragments;

import android.content.Context;
import android.content.Intent;
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

import bloop.honk.AuthController;
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
                    account.setPassword(authController.hashPassword(account.getPassword().toCharArray(), account.getUsername().getBytes()));
                    login(account.getUsername(), account.getPassword());
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

    //login function
    private void login(final String username, final String password){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.LOGIN_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //If we are getting success from server
                        if(response.equalsIgnoreCase("failure")){
                            Toast.makeText(getContext(), "Invalid username/ password", Toast.LENGTH_LONG).show();
                        }
                        else{
                            //Creating editor to store values to shared preferences
                            SharedPreferences.Editor editor = sharedPreferences.edit();

                            //Adding values to editor
                            editor.putBoolean(Config.LOGGEDIN_SHARED_PREF, true);
                            editor.putString(Config.USERNAME_SHARED_PREF, username);
                            editor.putString(Config.ROLE_SHARED_PREF, response);
                            //Saving values to editor
                            editor.apply();

                            Toast.makeText(getActivity(), "Successfully login", Toast.LENGTH_SHORT).show();
                            getActivity().finish();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //You can handle error here if you want
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                //Adding parameters to request
                params.put(Config.TAG_USERNAME, username);
                params.put(Config.TAG_PASSWORD, password);

                return params; //return params to string request
            }
        };
        //Adding the string request to the queue
        RequestQueue requestQueue = Volley.newRequestQueue(this.getContext());//this is the login request.
        requestQueue.add(stringRequest);
    }
}

