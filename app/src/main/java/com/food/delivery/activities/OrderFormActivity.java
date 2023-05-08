package com.food.delivery.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.widget.Button;

import com.food.delivery.App;
import com.food.delivery.R;
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
    TextInputEditText phoneTF;
    App app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_form);

        this.app = (App) getApplication();

        this.closeOrderFormB = findViewById(R.id.close_form_b);
        this.closeOrderFormB.setOnClickListener(view -> {
            System.out.println(phoneTF.getText().toString());
            finish();
        });

        this.phoneTF = findViewById(R.id.phone_tf);



        this.sendOrderB = findViewById(R.id.send_order_b);

        sendOrderB.setOnClickListener(view -> {
            NetworkService
                    .getInstance()
                    .getRoutes()
                    .sendOrder(new OrderPost("Рамиль", "asd", "ул. Ленина, 66", "Наличными", this.app.getCartState().getBody()))
                    .enqueue(new Callback<Message>() {
                        @Override
                        public void onResponse(Call<Message> call, Response<Message> response) {
                            Snackbar snackbar = Snackbar.make(view, response.body().getMessage(), Snackbar.LENGTH_LONG);
                            snackbar.show();

                            int responseCode = response.code();

                            if (responseCode == 200) {
                                app.getCartState().getBody().clear();
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