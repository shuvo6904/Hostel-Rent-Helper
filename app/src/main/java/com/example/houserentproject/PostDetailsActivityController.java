package com.example.houserentproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class PostDetailsActivityController extends AppCompatActivity {

    TextView rentedAmount, homeLocation, buildingName, floorNumber, detailsAboutHostel, genderValue, rentTypeValue, rentDate, advertiserUsrName, advertiserPhnNum, postDescription, electricityBill, gasBill, wifiBill, othersBill, security, parking, generator, elevator, totalRoom, flatSize, totalWashroom, totalBalcony;
    ImageView homeImage, userImage;
    ImageButton callButton;
    private StorageReference adStorageReference, adProfileStorageRef;
    private FirebaseAuth mAuth;
    private FirebaseFirestore fStore;
    private String currentUserId;
    private HomePageDataModel model;
    private CheckBox checkBoxFavourite;
    private SessionController sessionController;
    String advertiserUserId, userName, userPhnNumber;

    double latitude, longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        this.setTitle("Hostel Details");

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

        getUserId();
        sessionController = new SessionController(this);
        rentedAmount = (TextView) findViewById(R.id.rentedAmountId);
        homeLocation = (TextView) findViewById(R.id.homeLocationId);
        homeImage = (ImageView) findViewById(R.id.ivImage2Id);
        buildingName = (TextView) findViewById(R.id.buildingNameId);
        floorNumber = (TextView) findViewById(R.id.floorNumberId);
        detailsAboutHostel = (TextView) findViewById(R.id.detailsAboutHostelId);
        genderValue = (TextView) findViewById(R.id.genderValueId);
        rentTypeValue = (TextView) findViewById(R.id.rentTypeValueId);
        rentDate = (TextView) findViewById(R.id.datePickerId);
        userImage = (ImageView) findViewById(R.id.userProfileImageViewId);
        advertiserUsrName = (TextView) findViewById(R.id.userNameId);
        advertiserPhnNum = (TextView) findViewById(R.id.userNumberId);
        callButton = (ImageButton) findViewById(R.id.callId);
        fStore = FirebaseFirestore.getInstance();
        postDescription = (TextView) findViewById(R.id.postDescriptionId);
        electricityBill = (TextView) findViewById(R.id.electricityId);
        gasBill = (TextView) findViewById(R.id.gasId);
        wifiBill = (TextView) findViewById(R.id.wifiId);
        othersBill = (TextView) findViewById(R.id.othersId);
        security = (TextView) findViewById(R.id.securityTvId);
        parking = (TextView) findViewById(R.id.parkingTvId);
        generator = (TextView) findViewById(R.id.generatorTvId);
        elevator = (TextView) findViewById(R.id.elevatorTvId);
        totalRoom = (TextView) findViewById(R.id.totalRoomId);
        flatSize = (TextView) findViewById(R.id.flatSizeId);
        totalWashroom = (TextView) findViewById(R.id.totalWashroomId);
        totalBalcony = (TextView) findViewById(R.id.totalBalconyId);

        model = (HomePageDataModel) getIntent().getSerializableExtra("model");

        if (model != null) {
            postDescription.setText(model.getValueOfRentCount() + " " + model.getValueOfRentType() + " will be rented in the " + model.getLocation() + " from " + model.getDatePick() + ".");
            rentedAmount.setText(" " + model.getRentAmount() + " Taka");
            homeLocation.setText(" " + model.getLocation());
            buildingName.setText(" " + model.getBuildingName());
            floorNumber.setText(" Floor No. " + model.getFloorNumber());
            detailsAboutHostel.setText(model.getDetailsAboutHostel());
            genderValue.setText(" " + model.getValueOfGender());
            rentTypeValue.setText(" " + model.getValueOfRentCount() + " " + model.getValueOfRentType());
            rentDate.setText(" " + model.getDatePick());
            advertiserUserId = model.getAdUserId().trim();
            latitude = model.getHostelLat();
            longitude = model.getHostelLon();
            electricityBill.setText(" " + model.getElectricityBill() + " Taka (Electricity)");
            gasBill.setText(" " + model.getGasBill() + " Taka (Gas)");
            wifiBill.setText(" " + model.getWifiBill() + " Taka (Wifi)");
            othersBill.setText(" " + model.getOthersBill() + " Taka (Others)");
            security.setText(model.getSecurity());
            parking.setText(model.getParking());
            generator.setText(model.getGenerator());
            elevator.setText(model.getElevator());
            totalRoom.setText(" " + model.getTotalRoom() + " Room");
            flatSize.setText(" " + model.getFlatSize() + " Square Feet");
            totalWashroom.setText(" " + model.getTotalWashroom() + " Washroom");
            totalBalcony.setText(" " + model.getTotalBalcony() + " Balcony");
            Glide.with(this)
                    .load(model.getImage())
                    .into(homeImage);
        }

        adStorageReference = FirebaseStorage.getInstance().getReference();
        adProfileStorageRef = adStorageReference.child("Users/" + advertiserUserId + "/profile.jpg");

        adProfileStorageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(userImage);
            }
        });

        DocumentReference documentReference = fStore.collection("users").document(advertiserUserId);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                userName = value.getString("fName");
                userPhnNumber = value.getString("PhnNumber");
                advertiserUsrName.setText(userName);
                advertiserPhnNum.setText(userPhnNumber);
            }
        });

        checkBoxFavourite = findViewById(R.id.checkboxFavourite);
        checkBoxFavourite.setOnCheckedChangeListener(favListener);

        setFavourite();

        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentCall = new Intent(Intent.ACTION_DIAL);
                intentCall.setData(Uri.parse("tel:" + userPhnNumber));
                startActivity(intentCall);
            }
        });
    }



    CompoundButton.OnCheckedChangeListener favListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            favouritePost(model, isChecked);
        }
    };

    private void setFavourite() {
        if (sessionController.isFavourite(model.getId())) {
            checkBoxFavourite.setOnCheckedChangeListener (null);
            checkBoxFavourite.setChecked(true);
            checkBoxFavourite.setOnCheckedChangeListener (favListener);
        }
    }

    private void getUserId() {
        mAuth = FirebaseAuth.getInstance();
        currentUserId = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
    }

    private void favouritePost(HomePageDataModel model, boolean isChecked) {
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        if (isChecked){
            rootRef.child("favourite").child(currentUserId).child(model.getId()).setValue(model)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                sessionController.addToFavourite(model.getId());
                                Toast.makeText(PostDetailsActivityController.this, "Added to Favourite", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
        else {
            rootRef.child("favourite").child(currentUserId).child(model.getId()).removeValue()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                sessionController.removeFromFavourite(model.getId());
                                Toast.makeText(PostDetailsActivityController.this, "Removed from Favourite", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){

            case R.id.mapLocaionMenuId:

                Intent intent = new Intent(PostDetailsActivityController.this, MapActivityController.class);
                intent.putExtra("lat",latitude);
                intent.putExtra("lon", longitude);
                startActivity(intent);
                return true;

            default:

                return super.onOptionsItemSelected(item);

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater locationInflater = getMenuInflater();
        locationInflater.inflate(R.menu.map_menu,menu);

        return true;
    }

}