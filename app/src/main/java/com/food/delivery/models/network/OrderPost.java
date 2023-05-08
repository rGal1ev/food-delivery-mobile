package com.food.delivery.models.network;

import java.util.ArrayList;

public class OrderPost {
    final String user_name;
    final String user_phone;
    final String delivery_address;
    final String payment_type;
    final ArrayList<Food> cart;

    public OrderPost(String user_name, String user_phone, String delivery_address, String payment_type, ArrayList<Food> cart) {
        this.user_name = user_name;
        this.user_phone = user_phone;
        this.delivery_address = delivery_address;
        this.payment_type = payment_type;
        this.cart = cart;
    }
}
