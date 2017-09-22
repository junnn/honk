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

import bloop.honk.Config;
import bloop.honk.R;

/**
 * Created by Jun Hao Ng on 22/9/2017.
 */

public class RegisterFragment extends Fragment {
    private EditText usernameEditText, passwordEditText, confirmPasswordEditText;
    private Button registerButton;
    private SharedPreferences sharedPreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        //Creating a shared preference
        sharedPreferences = getActivity().getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);

        //init all widgets
        findAllViewById(view);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = usernameEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();
                String confirmPassword = confirmPasswordEditText.getText().toString().trim();

                register(username, password, confirmPassword);

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

    private void register(String username, String password, String confirmPassword){
        if(password.equals(confirmPassword)){
            //perform register [POST] & etc

            //Creating editor to store values to shared preferences
            SharedPreferences.Editor editor = sharedPreferences.edit();

            //Adding values to editor
            editor.putBoolean(Config.LOGGEDIN_SHARED_PREF, true);
            editor.putString(Config.USERNAME_SHARED_PREF, username);
            //Saving values to editor
            editor.apply();

            Toast.makeText(getContext(), "success"+ sharedPreferences.getString(Config.USERNAME_SHARED_PREF, ""), Toast.LENGTH_SHORT).show();
            getActivity().finish();
        }
        else{
            Toast.makeText(getContext(), "Password does not match", Toast.LENGTH_SHORT).show();
        }
    }
}
