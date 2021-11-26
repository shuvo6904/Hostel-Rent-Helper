package com.example.houserentproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.UUID;

public class UserDetails extends AppCompatActivity {

    ImageView userImg, userIdentityImg;
    TextView userName, userEmail, userContact, userEmailVeriStatus;
    DocumentReference documentReference;
    StorageReference profileRef;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);

        userImg = (ImageView) findViewById(R.id.userDetailsProfileImageId);
        userIdentityImg = (ImageView) findViewById(R.id.userDetailsPhotoIdentityId);
        userName = (TextView) findViewById(R.id.userDetailsProfileNameId);
        userEmail = (TextView) findViewById(R.id.userDetailsEmailId);
        userContact = (TextView) findViewById(R.id.userDetailsContactId);
        userEmailVeriStatus = (TextView) findViewById(R.id.userDetailsEmailVeriId);

        String advertiserId = getIntent().getStringExtra("advertiserId");

        documentReference = FirebaseFirestore.getInstance().collection("users").document(advertiserId);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                userName.setText("User Name: " + value.getString("fName"));
                userEmail.setText("User Email: " + value.getString("email"));
                userContact.setText("User Contact: " + value.getString("PhnNumber"));
                userEmailVeriStatus.setText("Email Verification Status: " + value.getString("emailVerification"));

                //Picasso.get().load(value.getString("profileImg")).into(userImg);
                //Picasso.get().load(value.getString("frontImageIdentity")).into(userIdentityImg);

                StorageReference profileRef = FirebaseStorage.getInstance().getReference().child("Users/"+advertiserId+"/profile.jpg");
                StorageReference identityRef = FirebaseStorage.getInstance().getReference().child("Users/"+advertiserId+"/frontImage.jpg");

                if (value.getString("profileImg").isEmpty()) {
                    userImg.setImageResource(R.drawable.profile);
                } else{
                    profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Picasso.get().load(uri).into(userImg);
                        }
                    });
                }

                if (value.getString("frontImageIdentity").isEmpty()) {
                    userIdentityImg.setImageResource(R.drawable.ic_baseline_image_24);
                } else{
                    identityRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Picasso.get().load(uri).into(userIdentityImg);
                        }
                    });
                }

            }
        });

    }


}