package bloop.honk.Controller;

import android.app.Activity;
import android.content.SharedPreferences;

import bloop.honk.Model.Account;
import bloop.honk.Model.AccountManager;
import bloop.honk.Model.User;

public class AuthController {
    private AccountManager accountManager = new AccountManager();

    public String hashPassword(Account account) {
        return accountManager.hashPassword(account.getPassword().toCharArray(), account.getUsername().getBytes());
    }

    public void login(Account account, Activity activity, SharedPreferences sharedPreferences) {
        accountManager.login(account.getUsername(), account.getPassword(), activity, sharedPreferences);
    }

    public void logout(Account account, SharedPreferences sharedPreferences) {
        accountManager.logout(sharedPreferences);
    }

    public void register(User user, String confirmPassword, Activity activity, SharedPreferences sharedPreferences) {
        accountManager.register(user, confirmPassword, activity, sharedPreferences);
    }
}
