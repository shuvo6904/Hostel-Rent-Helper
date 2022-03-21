package com.example.houserentproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivityController extends AppCompatActivity {

    EditText username, password;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore fStore;
    String userId;
    AlertDialog.Builder reset_alert;
    LayoutInflater inflater;
    ProgressBar loginProgressBar;
    //private Button loginBtn, registerBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ActionBar bar = getSupportActionBar();
        bar.setTitle("Login Page");

        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#5DAFF1")));
        bar.setHomeAsUpIndicator(R.drawable.ic_rent_icon);
        bar.setDisplayHomeAsUpEnabled(true);

        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.statusBarColor));
        }

        username = findViewById(R.id.loginEmailId);
        password = findViewById(R.id.loginPasswordId);
        //loginBtn = findViewById(R.id.loginBtnId);
        //registerBtn = findViewById(R.id.registerBtnId);

        //loginBtn.setBackgroundColor(Color.rgb(93, 175, 241));
        //registerBtn.setBackgroundColor(Color.rgb(93, 175, 241));
        //loginBtn.setBackground();

        firebaseAuth = FirebaseAuth.getInstance();

        reset_alert = new AlertDialog.Builder(this);
        inflater = this.getLayoutInflater();

        loginProgressBar = findViewById(R.id.loginProgressBarId);

        fStore = FirebaseFirestore.getInstance();


    }


    public void buttonRegister(View view) {

        startActivity(new Intent(getApplicationContext(), RegisterActivityController.class));
        finish();
    }

    public void buttonLogin(View view) {

        if (username.getText().toString().isEmpty()){
            username.setError("Required Field");
            username.requestFocus();
            return;
        }

        if (password.getText().toString().isEmpty()){
            password.setError("Required Field");
            password.requestFocus();
            return;
        }

        /*if (password.length() < 6){
            password.setError("Password Must be >= 6 Characters");
            password.requestFocus();
            return;
        }*/

        loginProgressBar.setVisibility(View.VISIBLE);


        /**if (username.getText().toString().equals("admin.hostelrent@info.com") && password.getText().toString().equals("120331")){
            startActivity(new Intent(LoginActivity.this, AdminHomeActivity.class));
            finish();
            return;
        }**/


        firebaseAuth.signInWithEmailAndPassword(username.getText().toString(), password.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {

                userId = firebaseAuth.getCurrentUser().getUid();
                DocumentReference documentReference = fStore.collection("users").document(userId);
                documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()){
                            String isAdminValue = documentSnapshot.getString("isAdmin");

                            if (isAdminValue.isEmpty()){
                                saveAdminSharedPreferences(false);
                                Toast.makeText(LoginActivityController.this, "Logged In Successfully", Toast.LENGTH_SHORT).show();
                                loginProgressBar.setVisibility(View.GONE);
                                startActivity(new Intent(LoginActivityController.this, DashboardActivityController.class));
                                finish();

                            } else {
                                saveAdminSharedPreferences(true);
                                Toast.makeText(LoginActivityController.this, "Logged In Successfully As Admin", Toast.LENGTH_SHORT).show();
                                loginProgressBar.setVisibility(View.GONE);
                                startActivity(new Intent(LoginActivityController.this, AdminHomePageActivityController.class));
                                finish();
                            }
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                loginProgressBar.setVisibility(View.GONE);
                Toast.makeText(LoginActivityController.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void saveAdminSharedPreferences(Boolean adminValue) {
        SharedPreferences sharedPreferences = getSharedPreferences("adminSharedPreferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putBoolean("isAdmin", adminValue);
        editor.apply();



    }

    @Override
    protected void onStart() {
        super.onStart();

        SharedPreferences sharedPreferences = getSharedPreferences("adminSharedPreferences", MODE_PRIVATE);
        if (firebaseAuth.getCurrentUser() != null && sharedPreferences.getBoolean("isAdmin", false) == false){

            startActivity(new Intent(LoginActivityController.this, DashboardActivityController.class));
            finish();

        }

        else if (firebaseAuth.getCurrentUser() != null && sharedPreferences.getBoolean("isAdmin", false) == true){

            startActivity(new Intent(LoginActivityController.this, AdminHomePageActivityController.class));
            finish();

        }
    }

    public void forgotPassword(View view) {
        View view1 = inflater.inflate(R.layout.reset_pop, null);

        reset_alert.setTitle("Reset Forgot Password ?")
                .setMessage("Enter Your Email to get Password Reset link.")
                .setPositiveButton("Reset", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //validate the email address
                        EditText email = view1.findViewById(R.id.forgotEmailId);
                        if (email.getText().toString().isEmpty()){
                            email.setError("Required Field");
                            return;
                        }
                        //send the reset link

                        firebaseAuth.sendPasswordResetEmail(email.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(LoginActivityController.this, "Reset Email sent", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(LoginActivityController.this,e.getMessage(), Toast.LENGTH_SHORT).show();

                            }
                        });

                    }
                }).setNegativeButton("Cancel", null)
                .setView(view1)
                .create().show();
    }
}