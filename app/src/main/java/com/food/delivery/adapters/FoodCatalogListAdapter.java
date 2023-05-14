package com.food.delivery.adapters;

import android.content.Context;
import android.media.Image;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.food.delivery.App;
import com.food.delivery.R;
import com.food.delivery.models.network.Food;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.snackbar.Snackbar;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class FoodCatalogListAdapter extends RecyclerView.Adapter<FoodCatalogListAdapter.ViewHolder> {
    private final Context context;
    private final BottomNavigationView bottomNavigationView;
    private final ArrayList<Food> foodList;
    private App app;

    public FoodCatalogListAdapter(Context context, ArrayList<Food> foodList, App app, BottomNavigationView bottomNavigationView) {
        this.context = context;
        this.foodList = foodList;
        this.app = app;
        this.bottomNavigationView = bottomNavigationView;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_card, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Food foodModel = foodList.get(position);

        holder.foodTitleTV.setText(foodModel.getTitle());
        holder.foodDescriptionTV.setText(foodModel.getDescription());
        holder.foodPriceTV.setText(Math.round(foodModel.getPrice()) + " ₽");

        if (foodInCart(foodModel)) {
            holder.foodStatusBox.setVisibility(View.VISIBLE);

            holder.foodStatusBox.setOnClickListener(event -> {
                Food foodInCart = null;

                for (Food food : this.app.getCartState().getBody()) {
                    if (food.getId() == foodModel.getId()) {
                        foodInCart = food;
                    }
                }

                this.notifyItemChanged(position);

                if (foodInCart != null) {
                    this.app.getCartState().getBody().remove(foodInCart);
                }
            });
        } else {
            holder.foodStatusBox.setVisibility(View.GONE);
        }

        Glide.with(context)
                .load(foodModel.getImageURL())
                .centerCrop()
                .into(holder.foodIV);

        holder.itemView.setOnClickListener(view -> {
            showFoodCardBottomSheet(foodModel, position);
        });

        if (this.app.getCartState().getBody().size() != 0) {
            BadgeDrawable foodCountBadge = bottomNavigationView.getOrCreateBadge(R.id.cart);
            foodCountBadge.setVisible(true);
            foodCountBadge.setNumber(this.app.getCartState().getBody().size());
        } else {
            BadgeDrawable foodCountBadge = bottomNavigationView.getOrCreateBadge(R.id.cart);
            foodCountBadge.setVisible(false);
        }
    }

    @Override
    public int getItemCount() {
        return foodList.size();
    }

    private void showFoodCardBottomSheet(Food foodModel, int position) {
        BottomSheetDialog foodCardBottomSheet = new BottomSheetDialog(context);
        foodCardBottomSheet.setContentView(R.layout.food_card_bottom_sheet);

        ImageView food_iv = foodCardBottomSheet.findViewById(R.id.food_image);
        TextView food_title_tv = foodCardBottomSheet.findViewById(R.id.food_title);
        TextView food_description_tv = foodCardBottomSheet.findViewById(R.id.food_description);
        Button add_to_cart_button = foodCardBottomSheet.findViewById(R.id.add_to_cart_button);

        TextView food_price = foodCardBottomSheet.findViewById(R.id.food_price);

        food_title_tv.setText(foodModel.getTitle());
        food_description_tv.setText(foodModel.getDescription());
        food_price.setText(Math.round(foodModel.getPrice()) + " ₽");

        if (foodInCart(foodModel)) {
            add_to_cart_button.setText("Удалить из корзины");
        }

        add_to_cart_button.setOnClickListener(view -> {
            if (!foodInCart(foodModel)) {
                this.app.getCartState().getBody().add(foodModel);
                add_to_cart_button.setText("Удалить из корзины");



                this.notifyItemChanged(position);

                Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    v.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE));
                } else {
                    v.vibrate(100);
                }

                if (this.app.getCartState().getBody().size() != 0) {
                    BadgeDrawable foodCountBadge = bottomNavigationView.getOrCreateBadge(R.id.cart);
                    foodCountBadge.setVisible(true);
                    foodCountBadge.setNumber(this.app.getCartState().getBody().size());
                } else {
                    BadgeDrawable foodCountBadge = bottomNavigationView.getOrCreateBadge(R.id.cart);
                    foodCountBadge.setVisible(false);
                }

                Snackbar snackbar = Snackbar.make(view, "Товар был добавлен в корзину", Snackbar.LENGTH_LONG);
                snackbar.show();

            } else {
                Food foodInCart = null;

                for (Food food : this.app.getCartState().getBody()) {
                    if (food.getId() == foodModel.getId()) {
                        foodInCart = food;
                    }
                }

                this.notifyItemChanged(position);

                if (foodInCart != null) {
                    this.app.getCartState().getBody().remove(foodInCart);
                }

                add_to_cart_button.setText("Добавить в корзину");

                if (this.app.getCartState().getBody().size() != 0) {
                    BadgeDrawable foodCountBadge = bottomNavigationView.getOrCreateBadge(R.id.cart);
                    foodCountBadge.setVisible(true);
                    foodCountBadge.setNumber(this.app.getCartState().getBody().size());
                } else {
                    BadgeDrawable foodCountBadge = bottomNavigationView.getOrCreateBadge(R.id.cart);
                    foodCountBadge.setVisible(false);
                }

                Snackbar snackbar = Snackbar.make(view, "Товар был удален из корзины", Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        });

        Glide.with(context)
                .load(foodModel.getImageURL())
                .centerCrop()
                .into(food_iv);

        foodCardBottomSheet.show();
    }

    private boolean foodInCart(Food foodModel) {
        boolean foodInCart = false;

        for (Food food : this.app.getCartState().getBody()) {
            if (food.getId() == foodModel.getId()) {
                foodInCart = true;
            }
        }

        return foodInCart;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView foodTitleTV;
        private final TextView foodDescriptionTV;
        private final ImageView foodIV;
        private final TextView foodPriceTV;
        private final RelativeLayout foodStatusBox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            foodIV = itemView.findViewById(R.id.food_image);
            foodTitleTV = itemView.findViewById(R.id.food_title);
            foodDescriptionTV = itemView.findViewById(R.id.food_description);
            foodPriceTV = itemView.findViewById(R.id.food_price);
            foodStatusBox = itemView.findViewById(R.id.food_status_container);
        }
    }
}
