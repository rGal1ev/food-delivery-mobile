package com.food.delivery.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.food.delivery.App;
import com.food.delivery.R;
import com.food.delivery.models.network.Order;
import com.food.delivery.network.NetworkService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrdersHistoryFragment extends Fragment {
    private ArrayList<Order> ordersList = new ArrayList<>();
    App app;

    public OrdersHistoryFragment() {}

    public static OrdersHistoryFragment newInstance() {
        OrdersHistoryFragment fragment = new OrdersHistoryFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.app = (App) getActivity().getApplication();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_orders_history, container, false);

        SwipeRefreshLayout swipeRefreshLayout = view.findViewById(R.id.refresh_swipe);

        swipeRefreshLayout.setOnRefreshListener(() -> {
            swipeRefreshLayout.setRefreshing(false);
            fetchOrders();
        });

        fetchOrders();

        return view;
    }

    public void fetchOrders() {
        ordersList.clear();

        NetworkService
                .getInstance()
                .getRoutes()
                .getCurrentUserOrder(this.app.getUSER_TOKEN())
                .enqueue(new Callback<List<Order>>() {
                    @Override
                    public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                        for (Order order : response.body()) {
                            System.out.println(order.getDeliveryAddress());
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Order>> call, Throwable t) {

                    }
                });
    }
}