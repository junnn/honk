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
                    String confirmPassword = confirmPasswordEditText.getText().toString().trim();
                    authController.register(user, confirmPassword, getActivity(), sharedPreferences);
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
}
