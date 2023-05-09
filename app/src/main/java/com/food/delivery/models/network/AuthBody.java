package com.food.delivery.models.network;

public class AuthBody {
    final String login;
    final String password;

    public AuthBody(String login, String password) {
        this.login = login;
        this.password = password;
    }
}
