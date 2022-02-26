package com.example.houserentproject.additionalActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.houserentproject.R;
import com.example.houserentproject.ReviewModel;
import com.example.houserentproject.additionalAdapter.ReviewAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.List;

public class HelpActivityController extends AppCompatActivity {

    TextView helpNum, helpEmail;
    String num, email, strReview, userName;
    EditText review;
    Button reviewButton;

    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    DocumentReference userDocument;
    FirebaseFirestore firebaseFirestore;
    DatabaseReference rootRef, databaseReference;
    ValueEventListener valueEventListener;
    RecyclerView recyclerView;
    ReviewAdapter reviewAdapter;
    List<ReviewModel> reviewModelList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        this.setTitle("Help");

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


        helpNum = (TextView) findViewById(R.id.helpNumId);
        helpEmail = (TextView) findViewById(R.id.helpEmailId);
        review = (EditText) findViewById(R.id.reviewId);
        reviewButton = (Button) findViewById(R.id.reviewButtonId);
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();
        rootRef = FirebaseDatabase.getInstance().getReference();
        userDocument = firebaseFirestore.collection("users").document(user.getUid());

        num = helpNum.getText().toString();
        email = helpEmail.getText().toString();

        recyclerView = (RecyclerView) findViewById(R.id.recyclerReviewId);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        reviewModelList = new ArrayList<>();
        reviewAdapter = new ReviewAdapter(this, reviewModelList);
        recyclerView.setAdapter(reviewAdapter);
        getReview();

        review.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (review.hasFocus()){
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    switch (event.getAction() & MotionEvent.ACTION_MASK){
                        case MotionEvent.ACTION_SCROLL:
                            v.getParent().requestDisallowInterceptTouchEvent(false);
                            return true;
                    }
                }
                return false;
            }
        });

        helpNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentCall = new Intent(Intent.ACTION_DIAL);
                intentCall.setData(Uri.parse("tel:" + num));
                startActivity(intentCall);
            }
        });

        helpEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentEmail = new Intent(Intent.ACTION_VIEW);
                intentEmail.setData(Uri.parse("mailto:" + email));
                startActivity(intentEmail);
            }
        });

        userDocument.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                userName = value.getString("fName");

            }
        });

        reviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                strReview = review.getText().toString();

                ReviewModel model = new ReviewModel(
                        userName,
                        strReview
                );

                rootRef.child("Reviews")
                        .child(user.getUid()).setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()){
                            Toast.makeText(HelpActivityController.this, "Review Added Successfully", Toast.LENGTH_SHORT).show();
                            review.setText("");
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(HelpActivityController.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();

                    }
                });



            }
        });

    }

    private void getReview() {
        databaseReference = FirebaseDatabase.getInstance().getReference("Reviews");

        valueEventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                reviewModelList.clear();

                for (DataSnapshot dataSnapshot: snapshot.getChildren()){

                    ReviewModel reviewModel = dataSnapshot.getValue(ReviewModel.class);
                    reviewModelList.add(reviewModel);
                }

                reviewAdapter.notifyDataSetChanged();
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(HelpActivityController.this, error.getMessage().toString(), Toast.LENGTH_SHORT).show();

            }
        });
    }

}