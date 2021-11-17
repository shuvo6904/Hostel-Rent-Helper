package com.example.houserentproject.UserFragment;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.houserentproject.LoginActivity;
import com.example.houserentproject.Profile;
import com.example.houserentproject.R;
import com.example.houserentproject.ResetPassActivity;
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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AccountFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccountFragment extends Fragment implements View.OnClickListener {

    private CardView editProfileCard, resetPassCard, logoutCard, helpCard;
    private ImageView proImageFrag;
    private TextView userNameFrag;
    private DocumentReference documentReference;
    private StorageReference fragProfileImageRef;
    private String userID;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AccountFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AccountFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AccountFragment newInstance(String param1, String param2) {
        AccountFragment fragment = new AccountFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_account, container, false);

        editProfileCard = view.findViewById(R.id.editProfileCardId);
        resetPassCard = view.findViewById(R.id.resetPassCardId);
        logoutCard = view.findViewById(R.id.logoutCardId);
        helpCard = view.findViewById(R.id.helpCardId);
        proImageFrag = view.findViewById(R.id.profileIVId);
        userNameFrag = view.findViewById(R.id.proNameId);

        getUserData();

        editProfileCard.setOnClickListener(this);
        resetPassCard.setOnClickListener(this);
        logoutCard.setOnClickListener(this);
        helpCard.setOnClickListener(this);


        return view;
    }

    private void getUserData() {
        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        documentReference = FirebaseFirestore.getInstance().collection("users").document(userID);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                userNameFrag.setText(value.getString("fName"));
            }
        });

        fragProfileImageRef = FirebaseStorage.getInstance().getReference().child("Users/"+userID+"/profile.jpg");

        fragProfileImageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(proImageFrag);
            }
        });
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.editProfileCardId:
                startActivity(new Intent(getActivity(), Profile.class));
                break;

            case R.id.resetPassCardId:
                startActivity(new Intent(getActivity(), ResetPassActivity.class));
                break;

            case R.id.logoutCardId:
                FirebaseAuth.getInstance().signOut();
                removeSharedPreferences();
                startActivity(new Intent(getActivity(), LoginActivity.class));
                getActivity().finish();
                break;

            case R.id.helpCardId:
                break;

        }

    }

    private void removeSharedPreferences() {

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("adminSharedPreferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("isAdmin");
        editor.clear();
        editor.apply();

    }
}