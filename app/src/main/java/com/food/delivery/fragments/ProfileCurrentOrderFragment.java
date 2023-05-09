package com.food.delivery.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.food.delivery.App;
import com.food.delivery.R;
import com.food.delivery.adapters.FoodCatalogListAdapter;
import com.food.delivery.adapters.OrdersListAdapter;
import com.food.delivery.models.network.Order;
import com.food.delivery.network.NetworkService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileCurrentOrderFragment extends Fragment {
    App app;
    ArrayList<Order> currentOrderList = new ArrayList<>();
    RecyclerView recyclerView;
    TextView title;

    public static ProfileCurrentOrderFragment newInstance() {
        ProfileCurrentOrderFragment fragment = new ProfileCurrentOrderFragment();
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

        View view = inflater.inflate(R.layout.fragment_profile_current_order, container, false);

        recyclerView = view.findViewById(R.id.rv);
        title = view.findViewById(R.id.title);

        title.setVisibility(View.GONE);

        NetworkService
                .getInstance()
                .getRoutes()
                .getCurrentUserOrder(this.app.getUSER_TOKEN())
                .enqueue(new Callback<List<Order>>() {
                    @Override
                    public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                        for (Order order : response.body()) {
                            if (!order.getIsFinished()) {
                                currentOrderList.add(order);
                            }
                        }

                        if (currentOrderList.size() == 0) {
                            title.setVisibility(View.VISIBLE);

                        } else {
                            title.setVisibility(View.GONE);
                            renderCurrentOrderList();
                        }

                    }

                    @Override
                    public void onFailure(Call<List<Order>> call, Throwable t) {
                        System.out.println(t.getMessage());
                    }
                });

        return view;
    }

    public void renderCurrentOrderList() {
        OrdersListAdapter foodCatalogListAdapter = new OrdersListAdapter(getContext(), currentOrderList);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(foodCatalogListAdapter);
    }
}