package com.example.houserentproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FavListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FirebaseAuth mAuth;
    private String myUserId;
    private List<HomePageData> favList;
    private MyAdapter myAdapter;

    private DatabaseReference rootRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fav_list);

        this.setTitle("");

        ActionBar bar = getSupportActionBar();
        //bar.hide();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#5DAFF1")));
        bar.setDisplayHomeAsUpEnabled(true);
        bar.setDisplayShowHomeEnabled(true);

        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.statusBarColor));
        }

        initRecyclerView();
        mAuth = FirebaseAuth.getInstance();
        myUserId = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
        rootRef = FirebaseDatabase.getInstance().getReference();
        favList = new ArrayList<>();
        myAdapter = new MyAdapter(this, favList);
        recyclerView.setAdapter(myAdapter);

        getData();
        title();
        backButton();
    }

    private void title() {
        if (getSupportActionBar() != null){
            getSupportActionBar().setTitle("Favourites");
        }
    }

    private void backButton() {
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    private void getData() {
        rootRef.child("favourite").child(myUserId).addListenerForSingleValueEvent(listener);
    }

    private void initRecyclerView() {
        recyclerView = findViewById(R.id.recyclerViewFav);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(FavListActivity.this,1);
        recyclerView.setLayoutManager(gridLayoutManager);
    }

    ValueEventListener listener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            if (snapshot.exists()){
                favList.clear();
                for (DataSnapshot snp : snapshot.getChildren()){
                    HomePageData homePageData = snp.getValue(HomePageData.class);
                    favList.add(homePageData);
                }
                myAdapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };
}