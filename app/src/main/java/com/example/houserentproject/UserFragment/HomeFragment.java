package com.example.houserentproject.UserFragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.houserentproject.DetailsActivity;
import com.example.houserentproject.FavListActivity;
import com.example.houserentproject.MainActivity;
import com.example.houserentproject.MyPosts;
import com.example.houserentproject.NstuContactActivity;
import com.example.houserentproject.PostActivity;
import com.example.houserentproject.Profile;
import com.example.houserentproject.R;
import com.example.houserentproject.UserDetails;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements View.OnClickListener {

    CardView favouriteCard, createPostCard, myPostCard, profileCard, nstuContactCard, emergencyContactCard;

    TextView hiTv;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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

        View view =inflater.inflate(R.layout.fragment_home, container, false);

        hiTv = (TextView) view.findViewById(R.id.hiTextViewId);
        favouriteCard = (CardView) view.findViewById(R.id.favouriteCardId);
        createPostCard = (CardView) view.findViewById(R.id.createPostCardId);
        myPostCard = (CardView) view.findViewById(R.id.myPostCardId);
        profileCard = (CardView) view.findViewById(R.id.profileCardId);
        nstuContactCard = (CardView) view.findViewById(R.id.nstuContactCardId);


        DocumentReference documentReference = FirebaseFirestore.getInstance().collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                hiTv.setText("Hi, " + value.getString("fName"));

            }
        });

        favouriteCard.setOnClickListener(this);
        createPostCard.setOnClickListener(this);
        myPostCard.setOnClickListener(this);
        profileCard.setOnClickListener(this);
        nstuContactCard.setOnClickListener(this);


        return view;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.favouriteCardId:
                startActivity(new Intent(getActivity(), FavListActivity.class));
                break;

            case R.id.createPostCardId:

                DocumentReference documentReference = FirebaseFirestore.getInstance().collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
                documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                        if (value.getString("isProfileCompleted").isEmpty()){

                            Toast.makeText(getActivity(), "Please Complete Your Profile. Then Try to Post Advertisement", Toast.LENGTH_SHORT).show();

                        }
                        else {
                            startActivity(new Intent(getActivity(), PostActivity.class));
                        }
                    }
                });

                break;

            case R.id.myPostCardId:
                startActivity(new Intent(getActivity(), MyPosts.class));
                break;

            case R.id.profileCardId:
                startActivity(new Intent(getActivity(), Profile.class));
                break;

            case R.id.nstuContactCardId:
                startActivity(new Intent(getActivity(), NstuContactActivity.class));
                break;

        }

    }
}