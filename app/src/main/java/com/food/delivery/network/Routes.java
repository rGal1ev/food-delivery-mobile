package com.food.delivery.network;

import com.food.delivery.models.network.AuthBody;
import com.food.delivery.models.network.Food;
import com.food.delivery.models.network.Message;
import com.food.delivery.models.network.Order;
import com.food.delivery.models.network.OrderPost;
import com.food.delivery.models.network.User;

import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface Routes {

    @GET("/get/food")
    Call<List<Food>> getFoodList();

    @POST("/add/order")
    Call<Message> sendOrder(@Header("x-access-token") String USER_TOKEN, @Body OrderPost body);

    @POST("/auth/user")
    Call<Message> authEmployee(@Body RequestBody body);

    @GET("get/user/current")
    Call<User> getCurrentUser(@Header("x-access-token") String USER_TOKEN);

    @POST("register/user")
    Call<Message> registerUser(@Body RequestBody body);


    @GET("get/current/user/orders")
    Call<List<Order>> getCurrentUserOrder(@Header("x-access-token") String USER_TOKEN);
}
