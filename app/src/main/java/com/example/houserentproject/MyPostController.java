package com.example.houserentproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MyPostController extends AppCompatActivity {


    FirebaseAuth fPostAuth;
    String userPostId;
    MyPostAdapter postAdapter;
    TextView textView;

    RecyclerView postRecyclerView;
    List<HomePageDataModel> myPostPageDataList;

    private DatabaseReference postDatabaseReference;
    private ValueEventListener postEventListener;
    ProgressDialog postProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posts);

        this.setTitle("My Post");

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

        textView = (TextView) findViewById(R.id.myPostTVId);
        fPostAuth = FirebaseAuth.getInstance();
        userPostId = fPostAuth.getCurrentUser().getUid();

        postRecyclerView = (RecyclerView)findViewById(R.id.postsRecyclerViewId);

        GridLayoutManager postsGridLayoutManager = new GridLayoutManager(MyPostController.this,1);
        postRecyclerView.setLayoutManager(postsGridLayoutManager);

        postProgressDialog = new ProgressDialog(this);
        postProgressDialog.setMessage("Loading Data...");

        myPostPageDataList = new ArrayList<>();

        postAdapter = new MyPostAdapter(MyPostController.this, myPostPageDataList);
        postRecyclerView.setAdapter(postAdapter);

        postDatabaseReference = FirebaseDatabase.getInstance().getReference("Data").child(userPostId);

        postProgressDialog.show();

        postEventListener = postDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()){
                    myPostPageDataList.clear();

                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){

                        HomePageDataModel postHomePageDataModel = dataSnapshot.getValue(HomePageDataModel.class);
                        myPostPageDataList.add(postHomePageDataModel);

                    }

                    postAdapter.notifyDataSetChanged();
                    postProgressDialog.dismiss();
                }else {

                    textView.setVisibility(View.VISIBLE);
                    postProgressDialog.dismiss();
                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                postProgressDialog.dismiss();

            }
        });
    }
}
















