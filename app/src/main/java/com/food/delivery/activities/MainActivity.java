package com.food.delivery.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.Window;

import com.food.delivery.R;
import com.food.delivery.fragments.AdminProfileFragment;
import com.food.delivery.fragments.CartFragment;
import com.food.delivery.fragments.CatalogFragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;


public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    BottomNavigationView bottomNavigationView;
    CartFragment cartFragment = new CartFragment();
    CatalogFragment catalogFragment = new CatalogFragment();
    AdminProfileFragment adminProfileFragment = new AdminProfileFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.navbar);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.catalog);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.catalog) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainer, catalogFragment)
                    .commit();

            return true;

        } else if (item.getItemId() == R.id.cart) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainer, cartFragment)
                    .commit();

            return true;
        } else if (item.getItemId() == R.id.admin_panel) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainer, adminProfileFragment)
                    .commit();

            return true;
        }

        return false;
    }
}