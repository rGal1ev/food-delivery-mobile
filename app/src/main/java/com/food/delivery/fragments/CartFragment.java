package com.food.delivery.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.food.delivery.App;
import com.food.delivery.R;
import com.food.delivery.activities.OrderFormActivity;
import com.food.delivery.adapters.FoodCartListAdapter;
import com.food.delivery.models.network.Food;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class CartFragment extends Fragment {
    BottomNavigationView bottomNavigationView;
    private App app;
    private RecyclerView foodCartListRecyclerView;
    private TextView emptyCartTitle;
    private TextView emptyCartSubTitle;
    private TextView totalFoodCountTV;
    private TextView totalFoodPriceTV;
    private TextView fragmentHeaderTV;
    private Button openMakeOrderFormB;
    View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.app = (App) getActivity().getApplication();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.fragment_cart, container, false);

        this.emptyCartTitle = view.findViewById(R.id.empty_cart_title);
        this.emptyCartSubTitle = view.findViewById(R.id.empty_cart_subtitle);

        this.totalFoodCountTV = view.findViewById(R.id.food_count);
        this.totalFoodPriceTV = view.findViewById(R.id.total_food_price);
        this.openMakeOrderFormB = view.findViewById(R.id.open_order_form_b);
        this.fragmentHeaderTV = view.findViewById(R.id.header_title);

        openMakeOrderFormB.setOnClickListener(event -> {
            if (!(this.app.getCartState().getBody().size() == 0)) {
                if (app.getUSER_TOKEN().isEmpty() || app.getUSER_TOKEN() == null) {
                    Snackbar snackbar = Snackbar.make(view, "Для создания заказа необходимо авторизоваться", Snackbar.LENGTH_LONG);
                    snackbar.show();
                } else {
                    Intent intent = new Intent(getActivity().getApplicationContext(), OrderFormActivity.class);
                    startActivityForResult(intent, 200);
                }
            }
        });

        if (this.app.getCartState().getBody().size() != 0) {
            this.foodCartListRecyclerView = view.findViewById(R.id.food_cart_rv);

            ArrayList<Food> foodList = this.app.getCartState().getBody();
            renderFoodCartList(foodList, view.getContext());
            setCartIsEmptyGone();

        } else {
            setCartIsEmptyVisible();
        }

        updateBottomCartInfo();

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        int result = data.getExtras().getInt("result");

        if (result == 0) {
            if (this.app.getCartState().getBody().size() != 0) {
                this.foodCartListRecyclerView = view.findViewById(R.id.food_cart_rv);

                ArrayList<Food> foodList = this.app.getCartState().getBody();
                renderFoodCartList(foodList, view.getContext());
                setCartIsEmptyGone();

            } else {
                setCartIsEmptyVisible();
            }
        }

        updateBottomCartInfo();

        if (this.app.getCartState().getBody().size() != 0) {
            BadgeDrawable foodCountBadge = bottomNavigationView.getOrCreateBadge(R.id.cart);
            foodCountBadge.setVisible(true);
            foodCountBadge.setNumber(this.app.getCartState().getBody().size());
        } else {
            BadgeDrawable foodCountBadge = bottomNavigationView.getOrCreateBadge(R.id.cart);
            foodCountBadge.setVisible(false);
        }
    }

    public void renderFoodCartList(ArrayList<Food> foodList, Context context) {
        FoodCartListAdapter foodCartListAdapter = new FoodCartListAdapter(context, foodList, app, this, bottomNavigationView);

        foodCartListRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        foodCartListRecyclerView.setAdapter(foodCartListAdapter);
    }

    public void setCartIsEmptyVisible() {
        emptyCartTitle.setVisibility(View.VISIBLE);
        emptyCartSubTitle.setVisibility(View.VISIBLE);
        fragmentHeaderTV.setVisibility(View.GONE);
    }

    public void setCartIsEmptyGone() {
        emptyCartTitle.setVisibility(View.GONE);
        emptyCartSubTitle.setVisibility(View.GONE);
        fragmentHeaderTV.setVisibility(View.VISIBLE);
    }

    public void setNavigationView(BottomNavigationView bottomNavigationView) {
        this.bottomNavigationView = bottomNavigationView;
    }

    public void updateBottomCartInfo() {
        if (this.app.getCartState().getBody().size() != 0) {
            this.totalFoodPriceTV.setVisibility(View.VISIBLE);
            this.totalFoodCountTV.setVisibility(View.VISIBLE);
            this.openMakeOrderFormB.setVisibility(View.VISIBLE);

            double foodTotalPrice = 0;
            int foodTotalCount = 0;

            for (Food food : this.app.getCartState().getBody()) {
                foodTotalPrice = foodTotalPrice + food.getPrice() * food.getCount();
                foodTotalCount = foodTotalCount + food.getCount();
            }

            this.totalFoodCountTV.setText("Всего товаров: " + foodTotalCount);
            this.totalFoodPriceTV.setText("Общая цена: " + Math.round(foodTotalPrice) + " руб");

        } else {
            this.totalFoodPriceTV.setVisibility(View.GONE);
            this.totalFoodCountTV.setVisibility(View.GONE);
            this.openMakeOrderFormB.setVisibility(View.GONE);
        }
    }

}