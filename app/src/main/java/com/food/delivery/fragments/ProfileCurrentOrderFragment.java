package com.food.delivery.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;
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
    ProgressBar progressBar;

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
        progressBar = view.findViewById(R.id.fetching_orders_indicator);

        title.setVisibility(View.GONE);

        SwipeRefreshLayout swipeRefreshLayout = view.findViewById(R.id.refresh_swipe);

        swipeRefreshLayout.setOnRefreshListener(() -> {
            fetchCurrentOrders();
            swipeRefreshLayout.setRefreshing(false);
        });

        fetchCurrentOrders();

        return view;
    }

    public void fetchCurrentOrders() {
        progressBar.setVisibility(View.VISIBLE);
        currentOrderList.clear();

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
    }

    public void renderCurrentOrderList() {
        progressBar.setVisibility(View.GONE);

        OrdersListAdapter foodCatalogListAdapter = new OrdersListAdapter(getContext(), currentOrderList);

        recyclerView.setLayoutAnimation(AnimationUtils.loadLayoutAnimation(getContext(), R.anim.layout_animation));
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.scheduleLayoutAnimation();
        recyclerView.setAdapter(foodCatalogListAdapter);
    }
}