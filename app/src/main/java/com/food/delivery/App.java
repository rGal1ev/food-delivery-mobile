package com.food.delivery;

import android.app.Application;
import android.content.SharedPreferences;

import com.food.delivery.models.Cart;

import java.util.ArrayList;

public class App extends Application {
    private Cart cartState;
    private int USER_ID = 0;
    private String USER_TOKEN = "";

    @Override
    public void onCreate() {
        super.onCreate();

        this.cartState = new Cart(new ArrayList<>());

        SharedPreferences userInfo = getSharedPreferences("USER_INFO", MODE_PRIVATE);

        USER_ID = userInfo.getInt("USER_ID", 0);
        USER_TOKEN = userInfo.getString("USER_TOKEN", "");
    }

    public void rememberUserAuth(int USER_ID, String USER_TOKEN) {
        SharedPreferences userInfo = getSharedPreferences("USER_INFO", MODE_PRIVATE);

        SharedPreferences.Editor editor = userInfo.edit();

        editor.putString("USER_TOKEN", USER_TOKEN);
        editor.putInt("USER_ID", USER_ID);

        this.USER_TOKEN = USER_TOKEN;

        editor.apply();
    }

    public void logoutUser() {
        SharedPreferences userInfo = getSharedPreferences("USER_INFO", MODE_PRIVATE);

        SharedPreferences.Editor editor = userInfo.edit();

        editor.remove("USER_ID");
        editor.remove("USER_TOKEN");

        editor.apply();

        USER_TOKEN = "";
    }

    public int getUSER_ID() {
        return USER_ID;
    }

    public String getUSER_TOKEN() {
        return USER_TOKEN;
    }

    public Cart getCartState() {
        return cartState;
    }

}
