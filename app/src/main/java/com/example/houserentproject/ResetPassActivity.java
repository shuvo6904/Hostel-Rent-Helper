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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ResetPassActivity extends AppCompatActivity {

    EditText resetPassword, resetConPassword;
    FirebaseUser user;
    Button resetBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_pass);
        this.setTitle("Reset Password");

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

        resetPassword = (EditText) findViewById(R.id.resetPassId);
        resetConPassword = (EditText) findViewById(R.id.resetconPassId);
        resetBtn = (Button) findViewById(R.id.resetBtn);
        user = FirebaseAuth.getInstance().getCurrentUser();

        resetBtn.setBackgroundColor(Color.BLACK);



    }

    public void btnResetPassSaveId(View view) {

        if (resetPassword.getText().toString().isEmpty()){
            resetPassword.setError("Required Field");
            return;
        }

        if (resetConPassword.getText().toString().isEmpty()){
            resetConPassword.setError("Required Field");
            return;
        }

        if (!resetConPassword.getText().toString().equals(resetConPassword.getText().toString())){
            resetConPassword.setError("Password Don't Match");
            return;
        }

        user.updatePassword(resetPassword.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(ResetPassActivity.this, "Password Updated", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), EmergencyContactActivity.class));
                finish();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(ResetPassActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }
}