package com.example.houserentproject;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.example.houserentproject.additionalAdapter.NstuContactAdapter;

public class NstuContactActivity extends AppCompatActivity {

    String [] designation, name, mobile, phone, email, website;

    RecyclerView recyclerView;

    NstuContactAdapter nstuContactAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nstu_contact);
        this.setTitle("NSTU Contact");

        ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#5DAFF1")));
        bar.setDisplayHomeAsUpEnabled(true);
        bar.setDisplayShowHomeEnabled(true);

        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.statusBarColor));
        }

        designation = getResources().getStringArray(R.array.designation);
        name = getResources().getStringArray(R.array.name);
        mobile = getResources().getStringArray(R.array.mobile);
        phone = getResources().getStringArray(R.array.phone);
        email= getResources().getStringArray(R.array.email);
        website = getResources().getStringArray(R.array.website);

        recyclerView = findViewById(R.id.nstuContactRecyclerId);
        nstuContactAdapter = new NstuContactAdapter(this, designation, name, mobile, phone, email, website);

        recyclerView.setAdapter(nstuContactAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}