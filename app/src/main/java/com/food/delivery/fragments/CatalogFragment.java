package com.food.delivery.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.food.delivery.App;
import com.food.delivery.R;
import com.food.delivery.adapters.FoodCatalogListAdapter;
import com.food.delivery.models.network.Food;
import com.food.delivery.network.NetworkService;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CatalogFragment extends Fragment {
    private App app;
    private View view;
    private ArrayList<Food> foodList = new ArrayList<>();
    private ArrayList<Food> searchedFoodList = new ArrayList<>();
    private RecyclerView foodCatalogListRecyclerView;
    private ProgressBar progressBar;
    private BottomNavigationView bottomNavigationView;

    private TextView notFoundFoodText;

    TextWatcher textWatcher = new TextWatcher() {
        @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
        @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}

        private Timer timer = new Timer();
        private final long DELAY = 1000;
        @Override
        public void afterTextChanged(Editable s) {
            String searchText = s.toString();

            timer.cancel();
            timer = new Timer();

            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    getActivity().runOnUiThread(() -> {
                        if (!searchText.isEmpty()) {
                            searchedFoodList.clear();

                            for (Food food : foodList) {
                                if (food.getTitle().toLowerCase().contains(searchText.toLowerCase()) || food.getDescription().toLowerCase().contains(searchText.toLowerCase())) {
                                    searchedFoodList.add(food);
                                }
                            }

                            if (searchedFoodList.size() != 0) {
                                notFoundFoodText.setVisibility(View.GONE);

                                foodCatalogListRecyclerView.setVisibility(View.VISIBLE);
                                renderFoodCatalogList(view.getContext(), searchedFoodList, foodCatalogListRecyclerView);

                            } else {
                                foodCatalogListRecyclerView.setVisibility(View.GONE);
                                notFoundFoodText.setVisibility(View.VISIBLE);
                            }

                        } else {
                            foodCatalogListRecyclerView.setVisibility(View.VISIBLE);
                            notFoundFoodText.setVisibility(View.GONE);
                            renderFoodCatalogList(view.getContext(), foodList, foodCatalogListRecyclerView);
                        }
                    });
                }

            }, DELAY);
        }
    };

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

        this.foodCatalogListRecyclerView = view.findViewById(R.id.food_list_rv);
        this.progressBar = view.findViewById(R.id.is_food_loading);
        this.notFoundFoodText = view.findViewById(R.id.not_found_text);

        this.notFoundFoodText.setVisibility(View.GONE);

        SwipeRefreshLayout swipeRefreshLayout = view.findViewById(R.id.refresh_swipe);

        swipeRefreshLayout.setOnRefreshListener(() -> {
            progressBar.setVisibility(View.VISIBLE);
            callGetFoodRequest(swipeRefreshLayout);
        });

        if (this.foodList.size() == 0) {
            progressBar.setVisibility(View.VISIBLE);
            callGetFoodRequest(swipeRefreshLayout);

        } else {
            progressBar.setVisibility(View.GONE);
            renderFoodCatalogList(view.getContext(), foodList, foodCatalogListRecyclerView);
        }

        TextInputEditText searchField = view.findViewById(R.id.search_field);
        searchField.addTextChangedListener(textWatcher);

        return view;
    }

    public void renderFoodCatalogList(Context context, ArrayList<Food> foodList, RecyclerView foodRecyclerView) {
        FoodCatalogListAdapter foodCatalogListAdapter = new FoodCatalogListAdapter(context, foodList, this.app, bottomNavigationView);

        foodRecyclerView.setLayoutAnimation(AnimationUtils.loadLayoutAnimation(getContext(), R.anim.layout_animation));
        foodRecyclerView.setLayoutManager(new LinearLayoutManager(context));

        foodRecyclerView.scheduleLayoutAnimation();
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

    public void setNavigationView(BottomNavigationView bottomNavigationView) {
        this.bottomNavigationView = bottomNavigationView;
    }
}