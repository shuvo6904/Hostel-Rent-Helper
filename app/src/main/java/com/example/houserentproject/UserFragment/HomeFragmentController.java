package com.example.houserentproject.UserFragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.houserentproject.EmergencyContactActivityController;
import com.example.houserentproject.FavListActivityController;
import com.example.houserentproject.MyPostController;
import com.example.houserentproject.NstuContactActivityController;
import com.example.houserentproject.CreatePostActivityController;
import com.example.houserentproject.ProfileActivityController;
import com.example.houserentproject.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;


public class HomeFragmentController extends Fragment implements View.OnClickListener {

    CardView favouriteCard, createPostCard, myPostCard, profileCard, nstuContactCard, emergencyContactCard;

    TextView hiTv;

    String isProfileCompleted;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public HomeFragmentController() {

    }

    public static HomeFragmentController newInstance(String param1, String param2) {
        HomeFragmentController fragment = new HomeFragmentController();
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

        View view =inflater.inflate(R.layout.fragment_home, container, false);

        hiTv = (TextView) view.findViewById(R.id.hiTextViewId);
        favouriteCard = (CardView) view.findViewById(R.id.favouriteCardId);
        createPostCard = (CardView) view.findViewById(R.id.createPostCardId);
        myPostCard = (CardView) view.findViewById(R.id.myPostCardId);
        profileCard = (CardView) view.findViewById(R.id.profileCardId);
        nstuContactCard = (CardView) view.findViewById(R.id.nstuContactCardId);
        emergencyContactCard = (CardView) view.findViewById(R.id.emergencyContactCardId);


        DocumentReference documentReference = FirebaseFirestore.getInstance().collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                hiTv.setText("Hi, " + value.getString("fName"));
                isProfileCompleted = value.getString("isProfileCompleted");

            }
        });

        favouriteCard.setOnClickListener(this);
        createPostCard.setOnClickListener(this);
        myPostCard.setOnClickListener(this);
        profileCard.setOnClickListener(this);
        nstuContactCard.setOnClickListener(this);
        emergencyContactCard.setOnClickListener(this);


        return view;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.favouriteCardId:
                startActivity(new Intent(getActivity(), FavListActivityController.class));
                break;

            case R.id.createPostCardId:

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                if (user != null)
                    user.reload();

                if (isProfileCompleted.isEmpty() || !user.isEmailVerified()){

                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Are You a New User?");
                    builder.setMessage("To Create Advertisement Post, Please Complete Your Profile by Uploading Profile Picture, Identity Photo and Verifying Email");

                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(getActivity(), ProfileActivityController.class));
                        }
                    });

                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();


                }

                else {
                    startActivity(new Intent(getActivity(), CreatePostActivityController.class));
                }

                break;

            case R.id.myPostCardId:
                startActivity(new Intent(getActivity(), MyPostController.class));
                break;

            case R.id.profileCardId:
                startActivity(new Intent(getActivity(), ProfileActivityController.class));
                break;

            case R.id.nstuContactCardId:
                startActivity(new Intent(getActivity(), NstuContactActivityController.class));
                break;

            case R.id.emergencyContactCardId:
                startActivity(new Intent(getActivity(), EmergencyContactActivityController.class));
                break;

        }

    }

}