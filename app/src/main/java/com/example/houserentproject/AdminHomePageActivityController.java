package com.example.houserentproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import com.example.houserentproject.adminFragment.ApprovedPostFragmentController;
import com.example.houserentproject.adminFragment.PendingPostFragmentController;
import com.example.houserentproject.adminFragment.UserListFragmentController;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class AdminHomePageActivityController extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        if (savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().replace(R.id.adminFrameContainerId, new PendingPostFragmentController()).commit();
        }

        this.setTitle("");

        ActionBar bar = getSupportActionBar();
        //bar.hide();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFE500")));
        //bar.setDisplayHomeAsUpEnabled(true);
        //bar.setDisplayShowHomeEnabled(true);

        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.adminStatusBarColor));
        }

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.adminBottomNavId);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                Fragment tempFragment = null;

                switch (item.getItemId()){

                    case R.id.bottomMenuPendingPostId:
                        tempFragment = new PendingPostFragmentController();
                        break;

                    case R.id.bottomMenuApprovedPostId:
                        tempFragment = new ApprovedPostFragmentController();
                        break;

                    case R.id.bottomMenuUserListId:
                        tempFragment = new UserListFragmentController();
                        break;

                }

                getSupportFragmentManager().beginTransaction().replace(R.id.adminFrameContainerId, tempFragment).commit();


                return true;
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){

            case R.id.menuLogoutId:
                FirebaseAuth.getInstance().signOut();
                removeSharedPreference();
                startActivity(new Intent(getApplicationContext(), LoginActivityController.class));
                finish();
                return true;

            case R.id.menuResetPassId:
                startActivity(new Intent(getApplicationContext(), ResetPassActivityController.class));
                return true;

            case R.id.menuProfileId:
                startActivity(new Intent(getApplicationContext(), ProfileActivityController.class));
                return true;

            default:
                return super.onOptionsItemSelected(item);


        }

    }

    private void removeSharedPreference() {

        SharedPreferences sharedPreferences = getSharedPreferences("adminSharedPreferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("isAdmin");
        editor.clear();
        editor.apply();

    }
}