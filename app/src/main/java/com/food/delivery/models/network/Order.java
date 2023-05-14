package com.food.delivery.models.network;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Order {
    @SerializedName("id")
    @Expose
    private int ID;
    @SerializedName("public_id")
    @Expose
    private String publicID;

    @SerializedName("cart")
    @Expose
    private List<Food> cart;

    @SerializedName("delivery_address")
    @Expose
    private String deliveryAddress;

    @SerializedName("is_finished")
    @Expose
    private boolean isFinished;

    public String getPublicID() {
        return publicID;
    }

    public int getID() {
        return ID;
    }

    public boolean getIsFinished() {
        return isFinished;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }
}
