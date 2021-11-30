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

import com.example.houserentproject.UserFragment.AccountFragment;
import com.example.houserentproject.UserFragment.HomeFragment;
import com.example.houserentproject.UserFragment.AllAdvertisementFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class DashboardActivity extends AppCompatActivity {

    BottomNavigationView bottomNavView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        ActionBar bar = getSupportActionBar();
        bar.setTitle("Hostel Rent Helper");
        //bar.hide();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#5DAFF1")));
        //bar.setDisplayHomeAsUpEnabled(true);
        bar.setHomeAsUpIndicator(R.drawable.ic_rent_icon);
        bar.setDisplayHomeAsUpEnabled(true);
        //bar.setLogo(R.drawable.ic_rent_icon);
        //bar.setDisplayUseLogoEnabled(true);


        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.statusBarColor));
        }

        if (savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().replace(R.id.frameContainerId, new HomeFragment()).commit();
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

                    case R.id.bottomMenuAllAdsId:
                        tempFrag = new AllAdvertisementFragment();
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