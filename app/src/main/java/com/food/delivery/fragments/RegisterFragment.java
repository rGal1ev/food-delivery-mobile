package com.food.delivery.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.food.delivery.R;
import com.food.delivery.models.network.Message;
import com.food.delivery.network.NetworkService;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.w3c.dom.Text;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterFragment extends Fragment {

    public RegisterFragment() {}

    public static RegisterFragment newInstance() {
        RegisterFragment fragment = new RegisterFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        Button backToLogin = view.findViewById(R.id.back_button);
        Button registerButton = view.findViewById(R.id.register_button);

        TextInputEditText firstName = view.findViewById(R.id.firstname);
        TextInputEditText lastName = view.findViewById(R.id.lastname);
        TextInputEditText phone = view.findViewById(R.id.phone);
        TextInputEditText email = view.findViewById(R.id.email);
        TextInputEditText password = view.findViewById(R.id.password);
        TextInputEditText login = view.findViewById(R.id.login);

        registerButton.setOnClickListener(event -> {
            String firstNameT = firstName.getText().toString();
            String lastNameT = lastName.getText().toString();
            String phoneT = phone.getText().toString();
            String emailT = email.getText().toString();
            String passwordT = password.getText().toString();
            String loginT = login.getText().toString();

            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("firstname", firstNameT)
                    .addFormDataPart("lastname", lastNameT)
                    .addFormDataPart("phone", phoneT)
                    .addFormDataPart("email", emailT)
                    .addFormDataPart("login", loginT)
                    .addFormDataPart("password", passwordT)
                    .build();

            if (firstNameT.isEmpty() || phoneT.isEmpty() || emailT.isEmpty() || passwordT.isEmpty() || loginT.isEmpty()) {
                Snackbar snackbar = Snackbar.make(view, "Заполните обязательные поля", Snackbar.LENGTH_LONG);
                snackbar.show();

            } else {
                NetworkService
                        .getInstance()
                        .getRoutes()
                        .registerUser(requestBody)
                        .enqueue(new Callback<Message>() {
                            @Override
                            public void onResponse(Call<Message> call, Response<Message> response) {
                                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                fragmentManager
                                        .beginTransaction()
                                        .setCustomAnimations(androidx.transition.R.anim.abc_fade_in, androidx.transition.R.anim.abc_fade_out)
                                        .replace(R.id.fragmentContainer, LoginFragment.newInstance())
                                        .commit();

                                Snackbar snackbar = Snackbar.make(view, response.body().getMessage(), Snackbar.LENGTH_LONG);
                                snackbar.show();
                            }

                            @Override
                            public void onFailure(Call<Message> call, Throwable t) {

                            }
                        });
            }
        });

        backToLogin.setOnClickListener(event -> {
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            fragmentManager
                    .beginTransaction()
                    .setCustomAnimations(androidx.transition.R.anim.abc_fade_in, androidx.transition.R.anim.abc_fade_out)
                    .replace(R.id.fragmentContainer, LoginFragment.newInstance())
                    .commit();
        });

        return view;
    }
}