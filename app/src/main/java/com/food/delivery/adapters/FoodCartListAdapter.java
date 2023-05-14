package com.food.delivery.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.food.delivery.App;
import com.food.delivery.R;
import com.food.delivery.fragments.CartFragment;
import com.food.delivery.models.Cart;
import com.food.delivery.models.network.Food;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class FoodCartListAdapter extends RecyclerView.Adapter<FoodCartListAdapter.ViewHolder> {
    private final ArrayList<Food> foodList;
    private final CartFragment cartFragment;
    private final Context context;
    private final App app;
    private final BottomNavigationView bottomNavigationView;

    public FoodCartListAdapter(Context context, ArrayList<Food> foodList, App app, CartFragment cartFragment, BottomNavigationView bottomNavigationView) {
        this.foodList = foodList;
        this.context = context;
        this.cartFragment = cartFragment;
        this.app = app;
        this.bottomNavigationView = bottomNavigationView;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_cart_card, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Food foodModel = foodList.get(position);

        holder.foodTitleTV.setText(foodModel.getTitle());
        holder.foodDescriptionTV.setText(foodModel.getDescription());
        holder.foodCountTV.setText(String.valueOf(foodModel.getCount()));
        holder.foodTotalItemPriceTV.setText(Math.round(foodModel.getPrice() * foodModel.getCount()) + " руб.");

        holder.addFoodCountB.setOnClickListener(view -> {
            foodModel.setCount(foodModel.getCount() + 1);

            holder.foodCountTV.setText(String.valueOf(foodModel.getCount()));
            holder.foodTotalItemPriceTV.setText(Math.round(foodModel.getPrice() * foodModel.getCount()) + " руб.");

            this.notifyItemChanged(position);

            if (this.app.getCartState().getBody().size() == 0) {
                cartFragment.setCartIsEmptyVisible();
            } else {
                cartFragment.setCartIsEmptyGone();
            }

            BadgeDrawable foodCountBadge = bottomNavigationView.getOrCreateBadge(R.id.cart);
            foodCountBadge.setVisible(true);
            foodCountBadge.setNumber(this.app.getCartState().getBody().size());

            cartFragment.updateBottomCartInfo();
        });

        holder.minusFoodCountB.setOnClickListener(view -> {
            if (foodModel.getCount() == 1) {
                Food foodInCart = null;

                for (Food food : this.app.getCartState().getBody()) {
                    if (food.getId() == foodModel.getId()) {
                        foodInCart = food;
                    }
                }

                this.app.getCartState().getBody().remove(foodInCart);

                foodList.remove(foodModel);
                this.notifyItemRemoved(position);

            } else {
                foodModel.setCount(foodModel.getCount() - 1);
                this.notifyItemChanged(position);
                holder.foodCountTV.setText(String.valueOf(foodModel.getCount()));
                holder.foodTotalItemPriceTV.setText(Math.round(foodModel.getPrice() * foodModel.getCount()) + " руб.");
            }

            if (this.app.getCartState().getBody().size() == 0) {
                cartFragment.setCartIsEmptyVisible();
            } else {
                cartFragment.setCartIsEmptyGone();
            }

            if (this.app.getCartState().getBody().size() != 0) {
                BadgeDrawable foodCountBadge = bottomNavigationView.getOrCreateBadge(R.id.cart);
                foodCountBadge.setVisible(true);
                foodCountBadge.setNumber(this.app.getCartState().getBody().size());
            } else {
                BadgeDrawable foodCountBadge = bottomNavigationView.getOrCreateBadge(R.id.cart);
                foodCountBadge.setVisible(false);
            }

            cartFragment.updateBottomCartInfo();
        });

        Glide.with(context)
                .load(foodModel.getImageURL())
                .centerCrop()
                .into(holder.foodIV);


        cartFragment.updateBottomCartInfo();
    }

    @Override
    public int getItemCount() {
        return foodList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView foodTitleTV;
        private final TextView foodDescriptionTV;
        private final TextView foodCountTV;
        private final ImageView foodIV;
        private final Button addFoodCountB;
        private final Button minusFoodCountB;
        private final TextView foodTotalItemPriceTV;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            foodIV = itemView.findViewById(R.id.food_image);
            foodTitleTV = itemView.findViewById(R.id.food_title);
            foodDescriptionTV = itemView.findViewById(R.id.food_description);
            foodCountTV = itemView.findViewById(R.id.food_count);
            addFoodCountB = itemView.findViewById(R.id.add_food_count);
            minusFoodCountB = itemView.findViewById(R.id.minus_food_count);
            foodTotalItemPriceTV = itemView.findViewById(R.id.food_price);
        }
    }
}
