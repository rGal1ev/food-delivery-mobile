package com.food.delivery.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.food.delivery.App;
import com.food.delivery.R;
import com.food.delivery.models.network.Message;
import com.food.delivery.network.NetworkService;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginFragment extends Fragment {
    App app;
    String messageText = "";

    public LoginFragment() {}

    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();

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

        View view = inflater.inflate(R.layout.fragment_login, container, false);

        Button loginButton = view.findViewById(R.id.login_button);
        Button registerButton = view.findViewById(R.id.register_button);

        registerButton.setOnClickListener(event -> {
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            fragmentManager
                    .beginTransaction()
                    .setCustomAnimations(androidx.transition.R.anim.abc_fade_in, androidx.transition.R.anim.abc_fade_out)
                    .replace(R.id.fragmentContainer, RegisterFragment.newInstance())
                    .commit();
        });

        TextInputEditText loginField = view.findViewById(R.id.login_field);
        TextInputEditText passwordField = view.findViewById(R.id.password_field);

        loginButton.setOnClickListener(event -> {
            String login = loginField.getText().toString();
            String password = passwordField.getText().toString();

            if (login.isEmpty() || password.isEmpty()) {
                Snackbar snackbar = Snackbar.make(view, "Заполните все поля", Snackbar.LENGTH_LONG);
                snackbar.show();
            }

            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("login", login)
                    .addFormDataPart("password", password)
                    .build();

            NetworkService
                    .getInstance()
                    .getRoutes()
                    .authEmployee(requestBody)
                    .enqueue(new Callback<Message>() {
                        @Override
                        public void onResponse(Call<Message> call, Response<Message> response) {
                            String token = response.body().getToken();

                            if (token != null) {
                                app.rememberUserAuth(0, token);

                                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                fragmentManager
                                        .beginTransaction()
                                        .setCustomAnimations(androidx.transition.R.anim.abc_fade_in, androidx.transition.R.anim.abc_fade_out)
                                        .replace(R.id.fragmentContainer, ProfileFragment.newInstance())
                                        .commit();
                            } else {
                                Snackbar snackbar = Snackbar.make(view, response.body().getMessage(), Snackbar.LENGTH_LONG);
                                snackbar.show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Message> call, Throwable t) {
                            System.out.println(t.getMessage());
                        }
                    });
        });

        return view;
    }
}