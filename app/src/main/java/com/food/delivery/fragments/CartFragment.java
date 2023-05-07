package com.food.delivery.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.food.delivery.App;
import com.food.delivery.R;

public class CartFragment extends Fragment {
    private App app;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.app = (App) getActivity().getApplication();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        TextView cartInfo = view.findViewById(R.id.cart_info);

        if (this.app.getCartState().getBody().size() == 0) {
            cartInfo.setText("Корзина пустая!");
        } else {
            cartInfo.setText("Корзина не пустая!");
        }

        return view;
    }
}