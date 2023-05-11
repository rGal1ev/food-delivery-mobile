package com.food.delivery.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.food.delivery.R;

public class OrdersHistoryFragment extends Fragment {

    public OrdersHistoryFragment() {}

    public static OrdersHistoryFragment newInstance() {
        OrdersHistoryFragment fragment = new OrdersHistoryFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_orders_history, container, false);
    }
}