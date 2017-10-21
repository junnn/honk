package bloop.honk.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
import bloop.honk.Model.User;
import bloop.honk.R;

/**
 * Created by Jun Hao Ng on 22/9/2017.
 */

public class RegisterFragment extends Fragment {
    private EditText usernameEditText, passwordEditText, confirmPasswordEditText;
    private Button registerButton;
    private SharedPreferences sharedPreferences;
    private AuthController authController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        //Creating a shared preference
        sharedPreferences = getActivity().getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        authController = new AuthController();

        //init all widgets
        findAllViewById(view);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(usernameEditText.getText().toString().isEmpty() || passwordEditText.getText().toString().isEmpty() || confirmPasswordEditText.getText().toString().isEmpty()){
                    Toast.makeText(getContext(), "Please ensure all fields are filled.", Toast.LENGTH_SHORT).show();
                }
                else {
                    User user = new User(usernameEditText.getText().toString().trim(), passwordEditText.getText().toString().trim());
                    user.setPassword(authController.hashPassword(user.getPassword().toCharArray(), user.getUsername().getBytes()));
                    String confirmPassword = authController.hashPassword(confirmPasswordEditText.getText().toString().trim().toCharArray(), user.getUsername().getBytes());
                    register(user.getUsername(), user.getPassword(), confirmPassword);
                }
            }
        });

        return view;
    }

    //initialise all buttons, edittext, textview & etc here
    private void findAllViewById(View view){
        usernameEditText = view.findViewById(R.id.username_editText);
        passwordEditText = view.findViewById(R.id.password_editText);
        confirmPasswordEditText = view.findViewById(R.id.confirm_password_editText);
        registerButton = view.findViewById(R.id.register_button);
    }

    private void register(final String username, final String password, String confirmPassword){
        if(password.equals(confirmPassword)){
            //perform register [POST]
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.REGISTER_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (response.equals("success")) {
                                //Creating editor to store values to shared preferences
                                SharedPreferences.Editor editor = sharedPreferences.edit();

                                //Adding values to editor
                                editor.putBoolean(Config.LOGGEDIN_SHARED_PREF, true);
                                editor.putString(Config.USERNAME_SHARED_PREF, username);
                                editor.putString(Config.ROLE_SHARED_PREF, "user");
                                //Saving values to editor
                                editor.apply();

                                Toast.makeText(getContext(), "Registered & logged in", Toast.LENGTH_SHORT).show();
                                getActivity().finish();

                            } else {
                                Toast.makeText(getContext(), "Username Taken", Toast.LENGTH_SHORT).show();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getContext(), "Unexpected error occured, registration not successful.", Toast.LENGTH_LONG).show();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put(Config.TAG_USERNAME, username);
                    params.put(Config.TAG_PASSWORD, password);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(getContext());
            requestQueue.add(stringRequest);
        }
        else{
            Toast.makeText(getContext(), "Password does not match", Toast.LENGTH_SHORT).show();
        }
    }
}
