package com.example.houserentproject;

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
import android.location.Location;
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

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
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
import java.text.DateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class CreatePostActivityController extends AppCompatActivity {

    SupportMapFragment supportMapFragment;
    FusedLocationProviderClient client;
    int REQUEST_CODE = 111, count = 1;
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
    Button chooseImageBtn;

    String imageUrl;

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    String userId;

    private DatabaseReference rootRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        this.setTitle("Create Post Page");

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


        supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.googleMapId);
        client = LocationServices.getFusedLocationProviderClient(CreatePostActivityController.this);

        if (ActivityCompat.checkSelfPermission(CreatePostActivityController.this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {

            getCurrentLocation();

        } else {

            ActivityCompat.requestPermissions(CreatePostActivityController.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);

        }


        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        chooseImageBtn = (Button) findViewById(R.id.imageChooseButton);
        chooseImageBtn.setBackgroundColor(Color.GRAY);

        userId = Objects.requireNonNull(fAuth.getCurrentUser()).getUid();
        rootRef = FirebaseDatabase.getInstance().getReference();


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


        ArrayAdapter<String> locationArrayAdapter = new ArrayAdapter<>(CreatePostActivityController.this, R.layout.sample_spinner_view, locationDropDownArray);
        dropDownText.setAdapter(locationArrayAdapter);

        ArrayAdapter<String> monthArrayAdapter = new ArrayAdapter<>(CreatePostActivityController.this, R.layout.sample_spinner_view, selectedMonthDropDownArray);
        selectedMonthText.setAdapter(monthArrayAdapter);

        ArrayAdapter<String> desireRentArrayAdapter = new ArrayAdapter<>(CreatePostActivityController.this, R.layout.sample_spinner_view, desireRentDropdownArray);
        desireRentText.setAdapter(desireRentArrayAdapter);

        ArrayAdapter<String> successiveArrayAdapter = new ArrayAdapter<>(CreatePostActivityController.this, R.layout.sample_spinner_view, successiveNumDropdownArray);

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

        inDeTV.setText("" + count);
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


    }


    public void getCurrentLocation() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }


        Task<Location> task = client.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {

                if (location != null) {

                    supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(GoogleMap googleMap) {

                            mMap = googleMap;

                            double currentLat = location.getLatitude();
                            double currentLon = location.getLongitude();

                            LatLng currentLatLon = new LatLng(currentLat, currentLon);

                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLon, 14));

                            getAddress(currentLat, currentLon);

                            mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                                @Override
                                public void onMapClick(LatLng latLng) {

                                    checkConnection();

                                    if (networkInfo.isConnected() && networkInfo.isAvailable()) {

                                        selectedLat = latLng.latitude;
                                        selectedLon = latLng.longitude;

                                        getAddress(selectedLat, selectedLon);

                                    } else {
                                        Toast.makeText(CreatePostActivityController.this, "Please Check Your Internet Connection", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });

                        }
                    });
                }


            }
        });


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == REQUEST_CODE) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                getCurrentLocation();

            }
        } else {
            Toast.makeText(CreatePostActivityController.this, "Permission Denied", Toast.LENGTH_SHORT).show();
        }
    }

    private void checkConnection() {
        manager = (ConnectivityManager) getApplicationContext().getSystemService(CONNECTIVITY_SERVICE);
        networkInfo = manager.getActiveNetworkInfo();
    }


    private void getAddress(double mLat, double mLon) {

        hostelLat = mLat;
        hostelLon = mLon;

        geocoder = new Geocoder(CreatePostActivityController.this, Locale.getDefault());

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


    public void btnSelectImage(View view) {

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

        if (uri == null){
            Toast.makeText(CreatePostActivityController.this, "Please choose a hostel image", Toast.LENGTH_LONG).show();
            return;
        }

        StorageReference storageReference = FirebaseStorage.getInstance()
                .getReference().child("HomeImage").child(uri.getLastPathSegment());

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Creating Post, Please Wait....");
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

    public void btnSubmitId(View view) {


        if (txtBuildingName.getText().toString().isEmpty()) {
            txtBuildingName.setError("Required Field");
            txtBuildingName.requestFocus();
            return;
        }

        if (selectedFloorNumText.getText().toString().isEmpty()) {
            //selectedFloorNumText.setError("Required Field");
            Toast.makeText(CreatePostActivityController.this, "Floor number is required field", Toast.LENGTH_LONG).show();
            selectedFloorNumText.requestFocus();
            return;
        }

        if (dropDownText.getText().toString().isEmpty()) {
            //dropDownText.setError("Required Field");
            Toast.makeText(CreatePostActivityController.this, "Hostel Address is required field", Toast.LENGTH_LONG).show();
            dropDownText.requestFocus();
            return;
        }

        if (selectedMonthText.getText().toString().isEmpty()) {
            //txtRentedAmount.setError("Required Field");
            Toast.makeText(CreatePostActivityController.this, "Month is required field", Toast.LENGTH_LONG).show();
            selectedMonthText.requestFocus();
            return;
        }



        if (txtRentedAmount.getText().toString().isEmpty()) {
            txtRentedAmount.setError("Required Field");
            txtRentedAmount.requestFocus();
            return;
        }

        if (desireRentText.getText().toString().isEmpty()) {
            //txtRentedAmount.setError("Required Field");
            Toast.makeText(CreatePostActivityController.this, "Rent type is required field", Toast.LENGTH_LONG).show();
            desireRentText.requestFocus();
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

                String myCurrentDateTime = DateFormat.getDateTimeInstance()
                .format(Calendar.getInstance().getTime());

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
                .child(myCurrentDateTime).setValue(homePageDataModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()) {
                    Toast.makeText(CreatePostActivityController.this, "Posted Advertisement Successfully. An Admin Will Approve Your Post Shortly.", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(CreatePostActivityController.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });


    }

}