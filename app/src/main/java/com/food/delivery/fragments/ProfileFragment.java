package com.food.delivery.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.food.delivery.App;
import com.food.delivery.R;
import com.food.delivery.activities.OrderFormActivity;
import com.food.delivery.models.network.User;
import com.food.delivery.network.NetworkService;
import com.google.android.material.tabs.TabLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment {
    private App app;
    public ProfileFragment() {}

    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();
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
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        String TOKEN = this.app.getUSER_TOKEN();

        Button logoutB = view.findViewById(R.id.logout_user);

        logoutB.setOnClickListener(event -> {
            app.logoutUser();

            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            fragmentManager
                    .beginTransaction()
                    .setCustomAnimations(androidx.transition.R.anim.abc_fade_in, androidx.transition.R.anim.abc_fade_out)
                    .replace(R.id.fragmentContainer, LoginFragment.newInstance())
                    .commit();
        });

        TabLayout tabLayout = view.findViewById(R.id.profile_order_tablayout);

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

        fragmentManager
                .beginTransaction()
                .setCustomAnimations(androidx.transition.R.anim.abc_fade_in, androidx.transition.R.anim.abc_fade_out)
                .replace(R.id.profile_orders_container, ProfileCurrentOrderFragment.newInstance())
                .commit();

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        fragmentManager
                                .beginTransaction()
                                .setCustomAnimations(androidx.transition.R.anim.abc_fade_in, androidx.transition.R.anim.abc_fade_out)
                                .replace(R.id.profile_orders_container, ProfileCurrentOrderFragment.newInstance())
                                .commit();

                        break;

                    case 1:
//                        fragmentManager
//                                .beginTransaction()
//                                .setCustomAnimations(androidx.transition.R.anim.abc_fade_in, androidx.transition.R.anim.abc_fade_out)
//                                .replace(R.id.profile_orders_container, ProfileFragment.newInstance())
//                                .commit();

                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });


        NetworkService
                .getInstance()
                .getRoutes()
                .getCurrentUser(app.getUSER_TOKEN())
                .enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        TextView userName = view.findViewById(R.id.username);
                        userName.setText(response.body().getFirstName() + " " + response.body().getLastName());
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {

                    }
                });


        return view;
    }
}