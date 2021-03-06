package com.example.houserentproject.additionalActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.houserentproject.HomePageDataModel;
import com.example.houserentproject.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class EditPostActivityController extends AppCompatActivity {

    SupportMapFragment supportMapFragment;
    //FusedLocationProviderClient client;
    int REQUEST_CODE = 111, count;
    ConnectivityManager manager;
    NetworkInfo networkInfo;
    Marker mM;
    GoogleMap mMap;
    Geocoder geocoder;
    double selectedLat, selectedLon, hostelLat, hostelLon;
    List<Address> addresses;
    String selectedAddress;
    TextView mapAddress, inDeTV;
    AppBarLayout mapAppBar;

    HomePageDataModel editDataModel;
    Button chooseImgBtn;

    String[] locationDropDownArray, selectedMonthDropDownArray, desireRentDropdownArray, successiveNumDropdownArray;
    TextInputLayout locationTextInputLayout, monthTextInputLayout, desireRentTextInputLayout;
    AutoCompleteTextView dropDownText, selectedMonthText, desireRentText, selectedFloorNumText, selectedTotalRoomText, selectedTotalWashroomText, selectedTotalBalconyText;

    ImageView homeImage, incrementBtn, decrementBtn;
    Uri uri;
    EditText txtRentedAmount, txtBuildingName, txtDetailsAddress, txtElectricityBill, txtGasBill, txtWifiBill, txtOthersBill, txtFlatSize;

    ChipGroup genderChipGroup;
    Chip genderChip;

    RadioGroup securityRadioGroup, parkingRadioGroup, generatorRadioGroup, elevatorRadioGroup;

    RadioButton security, parking, generator, elevator;

    String imageUrl;

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    String userId;

    private DatabaseReference rootRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_post);

        this.setTitle("Edit Post Page");

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

        editDataModel = (HomePageDataModel) getIntent().getSerializableExtra("editPostModel");

        count = Integer.parseInt(editDataModel.getValueOfRentCount());


        supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.googleMapId);
        //client = LocationServices.getFusedLocationProviderClient(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {

            getPostLocation();

        } else {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);

        }


        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        userId = Objects.requireNonNull(fAuth.getCurrentUser()).getUid();
        rootRef = FirebaseDatabase.getInstance().getReference();
        chooseImgBtn = (Button) findViewById(R.id.imageChooseButton);
        chooseImgBtn.setBackgroundColor(Color.GRAY);


        locationDropDownArray = getResources().getStringArray(R.array.location_spinner);
        locationTextInputLayout = (TextInputLayout) findViewById(R.id.locationTextInputLayoutId);
        dropDownText = (AutoCompleteTextView) findViewById(R.id.dropDownTextId);

        selectedMonthDropDownArray = getResources().getStringArray(R.array.month_spinner);
        monthTextInputLayout = (TextInputLayout) findViewById(R.id.selectMonthId);
        selectedMonthText = (AutoCompleteTextView) findViewById(R.id.selectMonthTextId);

        desireRentDropdownArray = getResources().getStringArray(R.array.desireRent_spinner);
        desireRentTextInputLayout = (TextInputLayout) findViewById(R.id.desireRentTextInputLayoutId);
        desireRentText = (AutoCompleteTextView) findViewById(R.id.desireRentTextId);

        successiveNumDropdownArray = getResources().getStringArray(R.array.successive_Num);

        selectedFloorNumText = (AutoCompleteTextView) findViewById(R.id.selectFloorNumTextId);
        selectedTotalRoomText = (AutoCompleteTextView) findViewById(R.id.selectTotalRoomTextId);
        selectedTotalWashroomText = (AutoCompleteTextView) findViewById(R.id.selectTotalWashRoomTextId);
        selectedTotalBalconyText = (AutoCompleteTextView) findViewById(R.id.selectTotalBalconyTextId);

        incrementBtn = (ImageView) findViewById(R.id.incrementId);
        decrementBtn = (ImageView) findViewById(R.id.decrementId);
        inDeTV = (TextView) findViewById(R.id.inDeTVId);


        ArrayAdapter<String> locationArrayAdapter = new ArrayAdapter<>(this, R.layout.sample_spinner_view, locationDropDownArray);
        dropDownText.setAdapter(locationArrayAdapter);

        ArrayAdapter<String> monthArrayAdapter = new ArrayAdapter<>(this, R.layout.sample_spinner_view, selectedMonthDropDownArray);
        selectedMonthText.setAdapter(monthArrayAdapter);

        ArrayAdapter<String> desireRentArrayAdapter = new ArrayAdapter<>(this, R.layout.sample_spinner_view, desireRentDropdownArray);
        desireRentText.setAdapter(desireRentArrayAdapter);

        ArrayAdapter<String> successiveArrayAdapter = new ArrayAdapter<>(EditPostActivityController.this, R.layout.sample_spinner_view, successiveNumDropdownArray);

        selectedFloorNumText.setAdapter(successiveArrayAdapter);
        selectedTotalRoomText.setAdapter(successiveArrayAdapter);
        selectedTotalWashroomText.setAdapter(successiveArrayAdapter);
        selectedTotalBalconyText.setAdapter(successiveArrayAdapter);

        homeImage = (ImageView) findViewById(R.id.postHomeImageId);

        txtRentedAmount = (EditText) findViewById(R.id.rentedAmountId);
        txtBuildingName = (EditText) findViewById(R.id.buildingNameId);
        txtFlatSize = (EditText) findViewById(R.id.flatSizeId);
        txtDetailsAddress = (EditText) findViewById(R.id.detailsAddressId);
        genderChipGroup = (ChipGroup) findViewById(R.id.genderChipGroupId);
        securityRadioGroup = (RadioGroup) findViewById(R.id.securityRadioGroupId);
        parkingRadioGroup = (RadioGroup) findViewById(R.id.parkingRadioGroupId);
        generatorRadioGroup = (RadioGroup) findViewById(R.id.generatorRadioGroupId);
        elevatorRadioGroup = (RadioGroup) findViewById(R.id.elevatorRadioGroupId);
        txtElectricityBill = (EditText) findViewById(R.id.electricityBillId);
        txtGasBill = (EditText) findViewById(R.id.gasBillId);
        txtWifiBill = (EditText) findViewById(R.id.wifiBillId);
        txtOthersBill = (EditText) findViewById(R.id.othersBillId);
        mapAddress = (TextView) findViewById(R.id.mapAddressId);

        mapAppBar = (AppBarLayout) findViewById(R.id.appBarMapLayoutId);

        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) mapAppBar.getLayoutParams();
        AppBarLayout.Behavior behavior = new AppBarLayout.Behavior();
        behavior.setDragCallback(new AppBarLayout.Behavior.DragCallback() {
            @Override
            public boolean canDrag(AppBarLayout appBarLayout) {
                return false;
            }
        });
        params.setBehavior(behavior);

        final CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbarLayoutId);

        collapsingToolbarLayout.setTitle("Click on Map to Change Hostel Location");
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(R.color.transparent)); // transparent color = #00000000
        collapsingToolbarLayout.setCollapsedTitleTextColor(Color.rgb(255, 255, 255)); //Color of your title


        incrementBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count++;
                inDeTV.setText("" + count);
            }
        });

        decrementBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (count <= 1) count = 1;
                else
                    count--;

                inDeTV.setText("" + count);
            }
        });

        setTextForEditPost();



    }

    private void setTextForEditPost() {

        if (editDataModel != null){
            txtRentedAmount.setText(editDataModel.getRentAmount());
            dropDownText.setText(editDataModel.getLocation());
            txtBuildingName.setText(editDataModel.getBuildingName());
            selectedFloorNumText.setText(editDataModel.getFloorNumber());
            txtDetailsAddress.setText(editDataModel.getDetailsAboutHostel());
            selectedMonthText.setText(editDataModel.getDatePick());
            desireRentText.setText(editDataModel.getValueOfRentType());
            inDeTV.setText(editDataModel.getValueOfRentCount());
            txtElectricityBill.setText(editDataModel.getElectricityBill());
            txtGasBill.setText(editDataModel.getGasBill());
            txtWifiBill.setText(editDataModel.getWifiBill());
            txtOthersBill.setText(editDataModel.getOthersBill());
            selectedTotalRoomText.setText(editDataModel.getTotalRoom());
            txtFlatSize.setText(editDataModel.getFlatSize());
            selectedTotalWashroomText.setText(editDataModel.getTotalWashroom());
            selectedTotalBalconyText.setText(editDataModel.getTotalBalcony());

        }
    }


    public void getPostLocation() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {

                mMap = googleMap;

                double postLat = editDataModel.getHostelLat();
                double postLon = editDataModel.getHostelLon();

                LatLng postLatLon = new LatLng(postLat, postLon);

                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(postLatLon, 14));

                getAddress(postLat, postLon);

                mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {

                        checkConnection();

                        if (networkInfo.isConnected() && networkInfo.isAvailable()) {

                            selectedLat = latLng.latitude;
                            selectedLon = latLng.longitude;

                            getAddress(selectedLat, selectedLon);

                        } else {
                            Toast.makeText(EditPostActivityController.this, "Please Check Your Internet Connection", Toast.LENGTH_SHORT).show();
                        }

                    }
                });

            }
        });



    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == REQUEST_CODE) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                getPostLocation();

            }
        } else {
            Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
        }
    }

    private void checkConnection() {
        manager = (ConnectivityManager) getApplicationContext().getSystemService(CONNECTIVITY_SERVICE);
        networkInfo = manager.getActiveNetworkInfo();
    }


    private void getAddress(double mLat, double mLon) {

        hostelLat = mLat;
        hostelLon = mLon;

        geocoder = new Geocoder(this, Locale.getDefault());

        if (mLat != 0) {
            try {
                addresses = geocoder.getFromLocation(mLat, mLon, 1);
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (addresses != null) {

                String mAddress = addresses.get(0).getAddressLine(0);

                //String city = addresses.get(0).getLocality();
                //String knownName = addresses.get(0).getFeatureName();

                selectedAddress = mAddress;

                if (mAddress != null) {

                    if (mM != null) {
                        mM.remove();
                    }

                    MarkerOptions markerOptions = new MarkerOptions();
                    LatLng latLng = new LatLng(mLat, mLon);
                    markerOptions.position(latLng).title(selectedAddress);
                    // mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14));
                    mM = mMap.addMarker(markerOptions);
                    mapAddress.setText(selectedAddress);
                } else {
                    Toast.makeText(this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                }

            } else {
                Toast.makeText(this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "LatLng Null", Toast.LENGTH_SHORT).show();
        }
    }


    public void btnEditSelectImage(View view) {

        Intent photoPicker = new Intent(Intent.ACTION_PICK);
        photoPicker.setType("image/*");
        startActivityForResult(photoPicker, 1);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            uri = data.getData();
            homeImage.setImageURI(uri);

        } else Toast.makeText(this, "You Haven't Picked Any Image", Toast.LENGTH_SHORT).show();

    }

    public void uploadImage() {
        StorageReference storageReference = FirebaseStorage.getInstance()
                .getReference().child("HomeImage").child(uri.getLastPathSegment());

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Updating Post, Please Wait....");
        progressDialog.show();

        storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isComplete()) ;
                Uri urlImage = uriTask.getResult();
                imageUrl = urlImage.toString();

                submitData();
                progressDialog.dismiss();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
            }
        });
    }

    public void btnEditSubmitId(View view) {

        if (txtRentedAmount.getText().toString().isEmpty()) {
            txtRentedAmount.setError("Required Field");
            txtRentedAmount.requestFocus();
            return;
        }

        int genderSelectedId = genderChipGroup.getCheckedChipId();
        genderChip = (Chip) findViewById(genderSelectedId);

        int securitySelectedId = securityRadioGroup.getCheckedRadioButtonId();
        security = (RadioButton) findViewById(securitySelectedId);

        int parkingSelectedId = parkingRadioGroup.getCheckedRadioButtonId();
        parking = (RadioButton) findViewById(parkingSelectedId);

        int generatorSelectedId = generatorRadioGroup.getCheckedRadioButtonId();
        generator = (RadioButton) findViewById(generatorSelectedId);

        int elevatorSelectedId = elevatorRadioGroup.getCheckedRadioButtonId();
        elevator = (RadioButton) findViewById(elevatorSelectedId);

        uploadImage();

    }

    public void submitData() {

        String selectedRent = desireRentText.getText().toString();
        String selectRentCount = inDeTV.getText().toString();

                String myCurrentDateTime = editDataModel.getId();


        String postStatus = "Pending";


        HomePageDataModel homePageDataModel = new HomePageDataModel(
                imageUrl,
                txtRentedAmount.getText().toString(),
                dropDownText.getText().toString(),
                txtBuildingName.getText().toString(),
                selectedFloorNumText.getText().toString(),
                txtDetailsAddress.getText().toString(),
                genderChip.getText().toString(),
                selectedRent,
                selectRentCount,
                selectedMonthText.getText().toString(),
                userId,
                myCurrentDateTime,
                postStatus,
                hostelLat,
                hostelLon,
                txtElectricityBill.getText().toString(),
                txtGasBill.getText().toString(),
                txtWifiBill.getText().toString(),
                txtOthersBill.getText().toString(),
                security.getText().toString(),
                parking.getText().toString(),
                generator.getText().toString(),
                elevator.getText().toString(),
                selectedTotalRoomText.getText().toString(),
                txtFlatSize.getText().toString(),
                selectedTotalWashroomText.getText().toString(),
                selectedTotalBalconyText.getText().toString()



        );


        rootRef.child("Data")
                .child(userId)
                .child(editDataModel.getId()).setValue(homePageDataModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()) {
                    Toast.makeText(EditPostActivityController.this, "Updated Advertisement Successfully. An Admin Will Approve Your Post Shortly.", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(EditPostActivityController.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });


    }
}