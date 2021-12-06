package com.example.houserentproject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<HomePageViewHolder>{

    private Context mContext;
    private List<HomePageData> myHomePageDataList;


    public MyAdapter(Context mContext, List<HomePageData> myHomePageDataList) {
        this.mContext = mContext;
        this.myHomePageDataList = myHomePageDataList;
    }

    @Override
    public HomePageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_row_item , parent, false);
        return new HomePageViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull HomePageViewHolder holder, int position) {
        
        HomePageData model = myHomePageDataList.get(position);

        Glide.with(mContext)
                .load(model.getImage())
                .into(holder.imageView);
        holder.mRentAmount.setText(" " + model.getRentAmount() + " Taka");
        //holder.mLocation.setText(model.getLocation());
        holder.rentType.setText(" "+ model.getValueOfRentType() + " " + "Rent");
        holder.description.setText(model.getValueOfRentCount() + " " + model.getValueOfRentType() + " will be rented in the " + model.getLocation() + " from " + model.getDatePick() + ".");

        holder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, DetailsActivity.class);

                intent.putExtra("model",model);

                mContext.startActivity(intent);
            }
        });

        holder.addQues.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DocumentReference documentReference = FirebaseFirestore.getInstance().collection("users").document(model.getAdUserId());
                documentReference.addSnapshotListener((Activity) mContext, new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                        String email = value.getString("email");

                        Intent intentEmail = new Intent(Intent.ACTION_VIEW);
                        intentEmail.setData(Uri.parse("mailto:" + email));
                        mContext.startActivity(intentEmail);
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return myHomePageDataList.size();
    }

    public void filteredList(ArrayList<HomePageData> filterList) {

        myHomePageDataList = filterList;
        notifyDataSetChanged();
    }
}

class HomePageViewHolder extends RecyclerView.ViewHolder{

    ImageView imageView;
    TextView mRentAmount, description, rentType, addQues;
    CardView mCardView;

    public HomePageViewHolder(View itemView) {
        super(itemView);

        imageView = itemView.findViewById(R.id.ivImageId);
        mRentAmount = itemView.findViewById(R.id.tvRentAmountId);
        description = itemView.findViewById(R.id.homePageDescriptionId);
        rentType = itemView.findViewById(R.id.homePageRentTypeValueId);
        //mLocation = itemView.findViewById(R.id.tvLocationId);
        mCardView = itemView.findViewById(R.id.myCardViewId);
        addQues = itemView.findViewById(R.id.addQuestionId);
    }
}
