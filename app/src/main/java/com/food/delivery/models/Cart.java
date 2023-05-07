package com.food.delivery.models;

import com.food.delivery.models.network.Food;

import java.util.ArrayList;

public class Cart {
    private ArrayList<Food> body;

    public Cart(ArrayList<Food> body) {
        this.body = body;
    }

    public void setBody(ArrayList<Food> body) {
        this.body = body;
    }

    public ArrayList<Food> getBody() {
        return body;
    }
}
