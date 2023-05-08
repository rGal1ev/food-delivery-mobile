package com.food.delivery.network;

import com.food.delivery.models.network.Food;
import com.food.delivery.models.network.Message;
import com.food.delivery.models.network.OrderPost;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface Routes {
    @GET("/get/food")
    public Call<List<Food>> getFoodList();

    @POST("/add/order")
    public Call<Message> sendOrder(@Body OrderPost body);
}
