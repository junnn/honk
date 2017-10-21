package bloop.honk.Controller;

import android.app.Activity;
import android.content.SharedPreferences;
import android.util.Base64;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import bloop.honk.Model.Account;
import bloop.honk.Model.User;

/**
 * Created by Jun Hao Ng on 21/10/2017.
 */

public class AuthController {

    public String hashPassword(Account account){
        return account.hashPassword(account.getPassword().toCharArray(), account.getUsername().getBytes());
    }

    public void login(Account account, Activity activity, SharedPreferences sharedPreferences){
        account.login(account.getUsername(), account.getPassword(), activity, sharedPreferences);
    }

    public void logout(Account account, SharedPreferences sharedPreferences){
        account.logout(sharedPreferences);
    }

    public void register(User user, String confirmPassword, Activity activity, SharedPreferences sharedPreferences){
        user.register(user, confirmPassword, activity, sharedPreferences);
    }
}
