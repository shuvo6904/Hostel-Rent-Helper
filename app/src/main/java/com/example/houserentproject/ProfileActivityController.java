package com.example.houserentproject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class ProfileActivityController extends AppCompatActivity {

    private Button profileEmailVerifyButton;
    private BottomSheetDialog sheetDialog, photoIdentitySheetDialog;
    private FirebaseAuth fAuth;
    DocumentReference documentReference;
    FirebaseUser user;
    private ImageView profileImage, photoIdentityIV;
    private StorageReference storageReference, profileStorageRef, frontVeriStorageReference;
    private FirebaseFirestore firebaseFirestore;
    private TextView profileName, proEditableName, proEditablePhnNum, proEditableEmail, checkIsEmailVerified, editProfileTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        this.setTitle("User Profile");

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

        profileEmailVerifyButton = (Button) findViewById(R.id.verifiedEmailButtonId);
        editProfileTextView = (TextView) findViewById(R.id.editProfileTextViewId);
        profileImage = (ImageView) findViewById(R.id.editableProfileImageViewId);
        profileName = (TextView) findViewById(R.id.profileNameId);
        proEditableName = (TextView) findViewById(R.id.editableProfileNameId);
        proEditablePhnNum = (TextView) findViewById(R.id.editableProfilePhnNumId);
        proEditableEmail = (TextView) findViewById(R.id.editableProfileEmailId);
        checkIsEmailVerified = (TextView) findViewById(R.id.checkIsEmailVerifiedId);

        profileEmailVerifyButton.setBackgroundColor(Color.rgb(30, 79, 193));

        fAuth = FirebaseAuth.getInstance();
        user = fAuth.getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();
        documentReference = firebaseFirestore.collection("users").document(user.getUid());

        if (user != null)
            user.reload();

        if (!user.isEmailVerified()){

            profileEmailVerifyButton.setVisibility(View.VISIBLE);
            checkIsEmailVerified.setText("Email Unverified");

        }else{

            checkIsEmailVerified.setText("Email Verified");

            Map<String, Object> edited = new HashMap<>();
            edited.put("emailVerification", "Verified");
            documentReference.update(edited).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {

                    isProfileCompleted();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    Toast.makeText(ProfileActivityController.this, e.getMessage(), Toast.LENGTH_LONG).show();

                }
            });
        }

        storageReference = FirebaseStorage.getInstance().getReference();
        profileStorageRef = storageReference.child("Users/"+user.getUid()+"/profile.jpg");
        frontVeriStorageReference = storageReference.child("Users/"+user.getUid()+"/frontImage.jpg");


        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {


                profileName.setText(value.getString("fName"));
                proEditableName.setText(value.getString("fName"));
                proEditablePhnNum.setText(value.getString("PhnNumber"));
                proEditableEmail.setText(value.getString("email"));

                if (value.getString("profileImg").isEmpty()) {
                    profileImage.setImageResource(R.drawable.profile);
                } else{
                    profileStorageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            Picasso.get().load(uri).into(profileImage);
                        }
                    });
                }

            }
        });

        profileEmailVerifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        Toast.makeText(ProfileActivityController.this, "Email verification link has been sent.", Toast.LENGTH_LONG).show();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Log.d("tag", "onFailure : Email not sent " + e.getMessage());

                    }
                });

            }
        });

        editProfileTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sheetDialog = new BottomSheetDialog(ProfileActivityController.this, R.style.BottomSheetStyle);

                View view = LayoutInflater.from(ProfileActivityController.this).inflate(R.layout.bottomsheet_dialog,
                        (LinearLayout)findViewById(R.id.sheetLayoutId));

                EditText profileNameBottomSheet, profilePhnNumberBottomSheet, profileEmailBottomSheet;
                profileNameBottomSheet = (EditText) view.findViewById(R.id.editNameId);
                profilePhnNumberBottomSheet =(EditText) view.findViewById(R.id.editPhnNumId);
                profileEmailBottomSheet = (EditText) view.findViewById(R.id.editEmailId);


                documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                        profileNameBottomSheet.setText(value.getString("fName"));
                        profilePhnNumberBottomSheet.setText(value.getString("PhnNumber"));
                        profileEmailBottomSheet.setText(value.getString("email"));

                    }
                });

                Button updateProfileData = (Button) view.findViewById(R.id.updateButtonId);
                updateProfileData.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (profileNameBottomSheet.getText().toString().isEmpty() || profilePhnNumberBottomSheet.getText().toString().isEmpty() || profileEmailBottomSheet.getText().toString().isEmpty()){
                            Toast.makeText(ProfileActivityController.this, "One or Many Fields are Empty", Toast.LENGTH_LONG).show();
                            return;
                        }

                        String email = profileEmailBottomSheet.getText().toString();
                        user.updateEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {

                                Map<String, Object> edited = new HashMap<>();
                                edited.put("email", email);
                                edited.put("fName", profileNameBottomSheet.getText().toString());
                                edited.put("PhnNumber", profilePhnNumberBottomSheet.getText().toString());
                                documentReference.update(edited).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(ProfileActivityController.this, "Profile Updated Successfully", Toast.LENGTH_LONG).show();
                                        sheetDialog.dismiss();
                                        //startActivity(new Intent(Profile.this, EmergencyContactActivity.class));
                                        //finish();
                                    }
                                });
                                //Toast.makeText(Profile.this, "Email is Changed", Toast.LENGTH_LONG).show();

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(ProfileActivityController.this, e.getMessage(), Toast.LENGTH_LONG).show();

                            }
                        });
                    }
                });

                sheetDialog.setContentView(view);
                sheetDialog.show();

            }
        });


    }

    public void uploadIdentityPhoto(View view) {

        photoIdentitySheetDialog = new BottomSheetDialog(ProfileActivityController.this, R.style.BottomSheetStyle);

        View photoIdentityView = LayoutInflater.from(ProfileActivityController.this).inflate(R.layout.upload_photo_identity,
                (RelativeLayout)findViewById(R.id.identityLayoutId));

        FloatingActionButton changePhotoIdentity;

        photoIdentityIV = (ImageView) photoIdentityView.findViewById(R.id.imageViewPhotoIdentity);
        changePhotoIdentity = (FloatingActionButton) photoIdentityView.findViewById(R.id.changePhotoIdentityImageId);

        changePhotoIdentity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ImagePicker.with(ProfileActivityController.this)
                        .crop()	    			//Crop image(Optional), Check Customization for more option
                        .compress(1024)			//Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                        .start(2000);

            }
        });

        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                String frontImagePathLink = value.getString("frontImageIdentity");

                if (frontImagePathLink.isEmpty()) {
                    photoIdentityIV.setImageResource(R.drawable.ic_baseline_image_24);
                } else{
                    frontVeriStorageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Picasso.get().load(uri).into(photoIdentityIV);
                        }
                    });
                }


            }
        });

        photoIdentitySheetDialog.setContentView(photoIdentityView);
        photoIdentitySheetDialog.show();


    }

    public void isProfileCompleted() {

        if (user != null)
            user.reload();

        if (user.isEmailVerified()){

            Map<String, Object> edited = new HashMap<>();
            edited.put("emailVerification", "Verified");
            documentReference.update(edited).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    Toast.makeText(ProfileActivityController.this, e.getMessage(), Toast.LENGTH_LONG).show();

                }
            });

        }

        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value.getString("profileImg").isEmpty() || value.getString("frontImageIdentity").isEmpty()){
                }

                else {

                    Map<String, Object> edited = new HashMap<>();
                    edited.put("isProfileCompleted", "Yes");
                    documentReference.update(edited).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {

                            //Toast.makeText(Profile.this, "Profile Completed", Toast.LENGTH_SHORT).show();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            Toast.makeText(ProfileActivityController.this, e.getMessage(), Toast.LENGTH_LONG).show();

                        }
                    });

                }
            }
        });

    }

    public void fabChangeProPic(View view) {

        ImagePicker.with(ProfileActivityController.this)
                //.cameraOnly()	//User can only capture image using Camera
                .crop()	    			//Crop image(Optional), Check Customization for more option
                .compress(1024)			//Final image size will be less than 1 MB(Optional)
                .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                .start(1000);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1000){
            if (resultCode == Activity.RESULT_OK){
                Uri imageUri = data.getData();
                //profileImage.setImageURI(imageUri);

                uploadProfileImageToFirebase(imageUri);
            }
        }

        if (requestCode == 2000){
            if (resultCode == Activity.RESULT_OK){
                Uri imageUri = data.getData();
                //frontImageView.setImageURI(imageUri);

                uploadFrontImageToFirebase(imageUri);
            }
        }

    }



    private void uploadProfileImageToFirebase(Uri imageUri) {
        // upload image to firebase storage
        final StorageReference fileRef = storageReference.child("Users/"+fAuth.getCurrentUser().getUid()+"/profile.jpg");
        fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                String profileImageUri = imageUri.toString();

                Map<String, Object> edited = new HashMap<>();
                edited.put("profileImg", profileImageUri);
                documentReference.update(edited).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                        isProfileCompleted();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(ProfileActivityController.this, e.getMessage(), Toast.LENGTH_LONG).show();

                    }
                });


                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        Picasso.get().load(uri).into(profileImage);

                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(ProfileActivityController.this, e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });



    }

    private void uploadFrontImageToFirebase(Uri imageUri) {
        // upload front image to firebase storage
        final StorageReference fileRef = storageReference.child("Users/"+fAuth.getCurrentUser().getUid()+"/frontImage.jpg");
        fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                String frontImageUri = imageUri.toString();

                Map<String, Object> edited = new HashMap<>();
                edited.put("frontImageIdentity", frontImageUri);
                documentReference.update(edited).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                        isProfileCompleted();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(ProfileActivityController.this, e.getMessage(), Toast.LENGTH_LONG).show();

                    }
                });

                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        Picasso.get().load(uri).into(photoIdentityIV);

                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(ProfileActivityController.this, e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });


    }
}