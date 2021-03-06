package com.example.houserentproject.UserFragment;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.houserentproject.HomePageDataModel;
import com.example.houserentproject.MyAdapter;
import com.example.houserentproject.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AllAdvertisementFragmentController#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AllAdvertisementFragmentController extends Fragment {

    MyAdapter myAdapter;
    RecyclerView myRecyclerView;
    List<HomePageDataModel> myHomePageDataModelList;
    DatabaseReference databaseReference;
    ValueEventListener eventListener;
    ProgressDialog progressDialog;
    EditText search;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AllAdvertisementFragmentController() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AllAdvertisementFragmentController newInstance(String param1, String param2) {
        AllAdvertisementFragmentController fragment = new AllAdvertisementFragmentController();
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

        View view = inflater.inflate(R.layout.fragment_all_advertisement, container, false);

        myRecyclerView = (RecyclerView) view.findViewById(R.id.allPostRecyclerViewId);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),1);
        myRecyclerView.setLayoutManager(gridLayoutManager);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading Post...");

        myHomePageDataModelList = new ArrayList<>();

        myAdapter = new MyAdapter(getActivity(), myHomePageDataModelList);
        myRecyclerView.setAdapter(myAdapter);

        setHomePagePost();

        search = view.findViewById(R.id.searchId);

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                filter(s.toString());

            }
        });


        return view;
    }

    private void setHomePagePost() {

        databaseReference = FirebaseDatabase.getInstance().getReference("Data");
        progressDialog.show();

        eventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                myHomePageDataModelList.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()){

                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){

                        HomePageDataModel homePageDataModel = dataSnapshot1.getValue(HomePageDataModel.class);

                        if (homePageDataModel.getPostStatus().contains("Approve")){
                            myHomePageDataModelList.add(homePageDataModel);
                        }


                    }

                }

                myAdapter.notifyDataSetChanged();
                progressDialog.dismiss();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressDialog.dismiss();

            }
        });



    }

    private void filter(String text) {

        ArrayList<HomePageDataModel> filterList = new ArrayList<>();

        for (HomePageDataModel item : myHomePageDataModelList){
            if (item.getLocation().toLowerCase().contains(text.toLowerCase())){
                filterList.add(item);
            }
        }

        myAdapter.filteredList(filterList);
    }
}