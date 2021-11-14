package com.example.houserentproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import com.example.houserentproject.fragment.AccountFragment;
import com.example.houserentproject.fragment.HomeFragment;
import com.example.houserentproject.fragment.SearchFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class DashboardActivity extends AppCompatActivity {

    BottomNavigationView bottomNavView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        if (savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().replace(R.id.frameContainerId, new HomeFragment()).commit();
        }

        this.setTitle("");

        ActionBar bar = getSupportActionBar();
        //bar.hide();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#5DAFF1")));
        //bar.setDisplayHomeAsUpEnabled(true);
        //bar.setDisplayShowHomeEnabled(true);

        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.statusBarColor));
        }

        bottomNavView = (BottomNavigationView) findViewById(R.id.bottomNavId);
        bottomNavView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                Fragment tempFrag = null;

                switch (item.getItemId()){

                    case R.id.bottomMenuHomeId:
                        tempFrag = new HomeFragment();
                        break;

                    case R.id.bottomMenuSearchId:
                        tempFrag = new SearchFragment();
                        break;

                    case R.id.bottomMenuAccountId:
                        tempFrag = new AccountFragment();
                        break;

                }

                getSupportFragmentManager().beginTransaction().replace(R.id.frameContainerId, tempFrag).commit();
                return true;
            }
        });

    }
}