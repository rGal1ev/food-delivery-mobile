package com.food.delivery.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.widget.Button;
import android.widget.TextView;

import com.food.delivery.App;
import com.food.delivery.R;
import com.food.delivery.models.network.Food;
import com.food.delivery.models.network.Message;
import com.food.delivery.models.network.OrderPost;
import com.food.delivery.network.NetworkService;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderFormActivity extends AppCompatActivity {
    Button closeOrderFormB;
    Button sendOrderB;
    TextInputEditText location_tf;
    App app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_form);

        this.app = (App) getApplication();

        this.closeOrderFormB = findViewById(R.id.close_form_b);

        this.closeOrderFormB.setOnClickListener(view -> {
            Intent returnIntent = new Intent();
            returnIntent.putExtra("result", 1);
            setResult(Activity.RESULT_OK, returnIntent);
            finish();
        });

        double foodTotalPriceT = 0;
        int foodTotalCountT = 0;

        for (Food food : this.app.getCartState().getBody()) {
            foodTotalPriceT = foodTotalPriceT + food.getPrice() * food.getCount();
            foodTotalCountT = foodTotalCountT + food.getCount();
        }

        TextView totalFoodCount = findViewById(R.id.food_count);
        TextView totalFoodPrice = findViewById(R.id.total_food_price);

        totalFoodCount.setText("Всего товаров: " + foodTotalCountT);
        totalFoodPrice.setText("Общая цена: " + Math.round(foodTotalPriceT) + " руб");

        this.sendOrderB = findViewById(R.id.send_order_b);
        this.location_tf = findViewById(R.id.location_tf);

        sendOrderB.setOnClickListener(view -> {
            NetworkService
                    .getInstance()
                    .getRoutes()
                    .sendOrder(this.app.getUSER_TOKEN(), new OrderPost(location_tf.getText().toString(), "Наличными", this.app.getCartState().getBody()))
                    .enqueue(new Callback<Message>() {
                        @Override
                        public void onResponse(Call<Message> call, Response<Message> response) {
                            int responseCode = response.code();

                            if (responseCode == 200) {
                                app.getCartState().getBody().clear();

                                Intent returnIntent = new Intent();
                                returnIntent.putExtra("result", 0);
                                setResult(Activity.RESULT_OK, returnIntent);
                                finish();
                            }
                        }

                        @Override
                        public void onFailure(Call<Message> call, Throwable t) {
                            System.out.println(t.getMessage());
                        }
                    });
        });
    }
}