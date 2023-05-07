package com.food.delivery.network;

import com.food.delivery.models.network.Food;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface Routes {
    @GET("/get/food")
    public Call<List<Food>> getFoodList();

    @GET("/get/food/<id>")
    public Call<Food> getFood(int id);
}
