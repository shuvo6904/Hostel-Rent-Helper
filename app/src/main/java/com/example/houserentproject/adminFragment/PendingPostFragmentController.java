package com.example.houserentproject.adminFragment;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.houserentproject.AdminAdapter;
import com.example.houserentproject.HomePageDataModel;
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
 * Use the {@link PendingPostFragmentController#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PendingPostFragmentController extends Fragment {

    AdminAdapter adminAdapter;
    RecyclerView adminRecyclerView;
    List<HomePageDataModel> adminPageDataList;
    DatabaseReference adminDatabaseReference;
    ValueEventListener adminEventListener;
    ProgressDialog adminProgressDialog;
    private TextView pendingTextView;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PendingPostFragmentController() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PendingPost.
     */
    // TODO: Rename and change types and number of parameters
    public static PendingPostFragmentController newInstance(String param1, String param2) {
        PendingPostFragmentController fragment = new PendingPostFragmentController();
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

        View view = inflater.inflate(R.layout.fragment_pending_post, container, false);

        adminRecyclerView = (RecyclerView) view.findViewById(R.id.pendingPostRecyclerId);
        pendingTextView = view.findViewById(R.id.pendingTVId);

        GridLayoutManager adminGridLayoutManager = new GridLayoutManager(getActivity(),1);
        adminRecyclerView.setLayoutManager(adminGridLayoutManager);
        adminProgressDialog = new ProgressDialog(getActivity());
        adminProgressDialog.setMessage("Loading Pending Post...");
        adminPageDataList = new ArrayList<>();
        adminAdapter = new AdminAdapter(getActivity(), adminPageDataList);
        adminRecyclerView.setAdapter(adminAdapter);

        adminDatabaseReference = FirebaseDatabase.getInstance().getReference("Data");

        adminProgressDialog.show();
        adminEventListener = adminDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                    adminPageDataList.clear();

                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){

                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){

                            HomePageDataModel adminPageData = dataSnapshot1.getValue(HomePageDataModel.class);

                            if (adminPageData.getPostStatus().contains("Pending")){
                                adminPageDataList.add(adminPageData);
                            }else {
                                pendingTextView.setVisibility(View.VISIBLE);
                                adminProgressDialog.dismiss();

                        }

                    }


                }

                adminAdapter.notifyDataSetChanged();
                adminProgressDialog.dismiss();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                adminProgressDialog.dismiss();

            }
        });

        return view;
    }
}