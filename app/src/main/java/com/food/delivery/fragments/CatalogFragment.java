package com.food.delivery.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.food.delivery.App;
import com.food.delivery.R;
import com.food.delivery.adapters.FoodCatalogListAdapter;
import com.food.delivery.models.network.Food;
import com.food.delivery.network.NetworkService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CatalogFragment extends Fragment {
    private App app;
    private View view;
    private ArrayList<Food> foodList;
    private RecyclerView foodCatalogListRecyclerView;
    private ProgressBar progressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_catalog, container, false);

        this.view = view;
        this.app = (App) getActivity().getApplication();
        this.foodList = new ArrayList<>();
        this.foodCatalogListRecyclerView = view.findViewById(R.id.food_list_rv);
        this.progressBar = view.findViewById(R.id.is_food_loading);
        SwipeRefreshLayout swipeRefreshLayout = view.findViewById(R.id.refresh_swipe);

        progressBar.setVisibility(View.VISIBLE);

        swipeRefreshLayout.setOnRefreshListener(() -> {
            callGetFoodRequest(swipeRefreshLayout);
        });

        callGetFoodRequest(swipeRefreshLayout);

        return view;
    }

    public void renderFoodCatalogList(Context context, ArrayList<Food> foodList, RecyclerView foodRecyclerView) {
        FoodCatalogListAdapter foodCatalogListAdapter = new FoodCatalogListAdapter(context, foodList, this.app);

        foodRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        foodRecyclerView.setAdapter(foodCatalogListAdapter);
    }

    public void callGetFoodRequest(SwipeRefreshLayout swipeRefreshLayout) {
        foodList.clear();

        NetworkService
                .getInstance()
                .getRoutes()
                .getFoodList()
                .enqueue(new Callback<List<Food>>() {
                    @Override
                    public void onResponse(Call<List<Food>> call, Response<List<Food>> response) {
                        swipeRefreshLayout.setRefreshing(false);

                        foodList.addAll(response.body());
                        renderFoodCatalogList(view.getContext(), foodList, foodCatalogListRecyclerView);
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFailure(Call<List<Food>> call, Throwable t) {}
                });
    }
}