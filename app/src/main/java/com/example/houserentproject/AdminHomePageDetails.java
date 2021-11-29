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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class AdminHomePageDetails extends AppCompatActivity {

    TextView rentedAmount, homeLocation, buildingName, floorNumber, detailsAboutHostel, genderValue, rentTypeValue, rentDate, advertiserUsrName, advertiserPhnNum, postDescription, electricityBill, gasBill, wifiBill, othersBill, security, parking, generator, elevator;
    ImageView homeImage, userImage;
    ImageButton callButton;
    private StorageReference adStorageReference, adProfileStorageRef;
    private FirebaseAuth mAuth;
    private FirebaseFirestore fStore;
    private String currentUserId;
    private HomePageData model;

    String advertiserUserId, userName, userPhnNumber;

    double latitude, longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home_page_details);

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

        rentedAmount = (TextView) findViewById(R.id.adminRentedAmountId);
        homeLocation = (TextView) findViewById(R.id.adminHomeLocationId);
        homeImage = (ImageView) findViewById(R.id.adminIv2Id);
        buildingName = (TextView) findViewById(R.id.adminBuildingNameId);
        floorNumber = (TextView) findViewById(R.id.adminFloorNumberId);
        detailsAboutHostel = (TextView) findViewById(R.id.adminDetailsAboutHostelId);
        genderValue = (TextView) findViewById(R.id.adminGenderValueId);
        rentTypeValue = (TextView) findViewById(R.id.adminRentTypeValueId);
        rentDate = (TextView) findViewById(R.id.adminDatePickerId);
        userImage = (ImageView) findViewById(R.id.adminActivityUserProfileImageViewId);
        advertiserUsrName = (TextView) findViewById(R.id.adminActivityUserNameId);
        advertiserPhnNum = (TextView) findViewById(R.id.adminActivityUserNumberId);
        callButton = (ImageButton) findViewById(R.id.adminActivityCallId);
        fStore = FirebaseFirestore.getInstance();
        postDescription = (TextView) findViewById(R.id.adminDescriptionId);
        electricityBill = (TextView) findViewById(R.id.adminElectricityId);
        gasBill = (TextView) findViewById(R.id.adminGasId);
        wifiBill = (TextView) findViewById(R.id.adminWifiId);
        othersBill = (TextView) findViewById(R.id.adminOthersId);
        security = (TextView) findViewById(R.id.adminSecurityTvId);
        parking = (TextView) findViewById(R.id.adminParkingTvId);
        generator = (TextView) findViewById(R.id.adminGeneratorTvId);
        elevator = (TextView) findViewById(R.id.adminElevatorTvId);

        model = (HomePageData) getIntent().getSerializableExtra("adminPostModel");

        if (model != null){

            postDescription.setText(model.getValueOfRentCount() + " " + model.getValueOfRentType() + " will be rented in the " + model.getLocation() + " from " + model.getDatePick() + ".");
            rentedAmount.setText(" " + model.getRentAmount() + " Taka");
            homeLocation.setText(" " + model.getLocation());
            buildingName.setText(" " + model.getBuildingName());
            floorNumber.setText(" " + model.getFloorNumber() + " Floor");
            detailsAboutHostel.setText(model.getDetailsAboutHostel());
            genderValue.setText(" " + model.getValueOfGender());
            rentTypeValue.setText(" " + model.getValueOfRentType());
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

        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentCall = new Intent(Intent.ACTION_DIAL);
                intentCall.setData(Uri.parse("tel:" + userPhnNumber));
                startActivity(intentCall);
            }
        });

        userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent userDetailsIntent = new Intent(getApplicationContext(), UserDetails.class);
                userDetailsIntent.putExtra("advertiserId",advertiserUserId);
                startActivity(userDetailsIntent);
            }
        });

    }

    private void getUserId() {

        mAuth = FirebaseAuth.getInstance();
        currentUserId = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){

            case R.id.mapLocaionMenuId:

                Intent intent = new Intent(AdminHomePageDetails.this, MapsActivity.class);
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