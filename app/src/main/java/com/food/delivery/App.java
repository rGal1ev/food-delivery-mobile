package com.food.delivery;

import android.app.Application;

import com.food.delivery.models.Cart;

import java.util.ArrayList;

public class App extends Application {
    private Cart cartState;

    @Override
    public void onCreate() {
        super.onCreate();

        this.cartState = new Cart(new ArrayList<>());
    }

    public void setCartState(Cart cartGlobalState) {
        this.cartState = cartGlobalState;
    }

    public Cart getCartState() {
        return cartState;
    }
}
