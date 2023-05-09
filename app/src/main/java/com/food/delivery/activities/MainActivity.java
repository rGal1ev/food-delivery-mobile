package com.food.delivery.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.Window;

import com.food.delivery.App;
import com.food.delivery.R;
import com.food.delivery.fragments.LoginFragment;
import com.food.delivery.fragments.CartFragment;
import com.food.delivery.fragments.CatalogFragment;

import com.food.delivery.fragments.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    BottomNavigationView bottomNavigationView;
    CartFragment cartFragment = new CartFragment();
    CatalogFragment catalogFragment = new CatalogFragment();
    LoginFragment loginFragment = new LoginFragment();
    ProfileFragment profileFragment = new ProfileFragment();
    App app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        this.app = (App) getApplication();

        bottomNavigationView = findViewById(R.id.navbar);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.catalog);

        catalogFragment.setNavigationView(bottomNavigationView);
        cartFragment.setNavigationView(bottomNavigationView);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.catalog) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(androidx.transition.R.anim.abc_fade_in, androidx.transition.R.anim.abc_fade_out)
                    .replace(R.id.fragmentContainer, catalogFragment)
                    .commit();

            return true;

        } else if (item.getItemId() == R.id.cart) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(androidx.transition.R.anim.abc_fade_in, androidx.transition.R.anim.abc_fade_out)
                    .replace(R.id.fragmentContainer, cartFragment)
                    .commit();

            return true;
        } else if (item.getItemId() == R.id.admin_panel) {
            if (app.getUSER_TOKEN().isEmpty()) {

                getSupportFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(androidx.transition.R.anim.abc_fade_in, androidx.transition.R.anim.abc_fade_out)
                        .replace(R.id.fragmentContainer, loginFragment)
                        .commit();

            } else {
                getSupportFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(androidx.transition.R.anim.abc_fade_in, androidx.transition.R.anim.abc_fade_out)
                        .replace(R.id.fragmentContainer, profileFragment)
                        .commit();
            }

            return true;
        }

        return false;
    }
}