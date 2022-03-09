package com.example.houserentproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivityController extends AppCompatActivity {

    EditText regFullName, regPhnNum, regEmail, regPass, regConPass;
    FirebaseAuth fAuth;
    ProgressBar progressBar;
    FirebaseFirestore fStore;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        this.setTitle("Register Page");

        ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#5DAFF1")));

        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.statusBarColor));
        }

        regFullName = findViewById(R.id.regFullNameId);
        regPhnNum = findViewById(R.id.regPhnNumId);
        regEmail = findViewById(R.id.regEmailId);
        regPass = findViewById(R.id.regPassId);
        regConPass = findViewById(R.id.regConPassId);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        progressBar = findViewById(R.id.regProgressBarId);



    }

    public void tvAlreadyRegistered(View view) {

        startActivity(new Intent(getApplicationContext(), LoginActivityController.class));
        finish();
    }

    public void register(View view) {

        String strFullName = regFullName.getText().toString().trim();
        String strPhnNum = regPhnNum.getText().toString().trim();
        String strEmail = regEmail.getText().toString().trim();
        String strPass = regPass.getText().toString().trim();
        String strConPass = regConPass.getText().toString().trim();

        if (strFullName.isEmpty()){
            //regFullName.setError("Full Name is Required");
            regFullName.requestFocus();
            return;
        }

        if (strPhnNum.isEmpty()){
            regPhnNum.setError("Phone Number is Required");
            regPhnNum.requestFocus();
            return;
        }

        if (strEmail.isEmpty()){
            regEmail.setError("Email is Required");
            regEmail.requestFocus();
            return;
        }

        if (strPass.isEmpty()){
            regPass.setError("Password is Required");
            regPass.requestFocus();
            return;
        }

        if (strPass.length() < 6){
            regPass.setError("Password Must be >= 6 Characters");
            regPass.requestFocus();
            return;
        }

        if (strConPass.isEmpty()){
            regConPass.setError("Confirm Password is Required");
            regConPass.requestFocus();
            return;
        }

        if (!strPass.equals(strConPass)){
            regConPass.setError("Password Don't Match");
            regConPass.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        fAuth.createUserWithEmailAndPassword(strEmail, strPass).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {

                userID = fAuth.getCurrentUser().getUid();

                DocumentReference documentReference = fStore.collection("users").document(userID);
                Map<String,Object> user = new HashMap<>();
                user.put("fName",strFullName);
                user.put("email",strEmail);
                user.put("PhnNumber",strPhnNum);
                user.put("profileImg", "");
                user.put("frontImageIdentity", "");
                user.put("emailVerification", "Unverified");
                user.put("isProfileCompleted", "");
                user.put("isAdmin", "");
                user.put("userID", userID);
                documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {


                    }
                });

                Toast.makeText(RegisterActivityController.this, "Registered Successfully", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);

                startActivity(new Intent(RegisterActivityController.this, DashboardActivityController.class));
                finish();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(RegisterActivityController.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);

            }
        });

    }
}