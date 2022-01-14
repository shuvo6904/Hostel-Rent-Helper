package com.example.houserentproject.additionalAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.houserentproject.R;
import com.example.houserentproject.ReviewModel;

import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    private Context context;
    private List<ReviewModel> reviewModelList;

    public ReviewAdapter(Context context, List<ReviewModel> reviewModelList) {
        this.context = context;
        this.reviewModelList = reviewModelList;
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.review_item_row, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {

        holder.reviewerName.setText(reviewModelList.get(position).getName());
        holder.reviewerDetails.setText(reviewModelList.get(position).getReview());

    }

    @Override
    public int getItemCount() {
        return reviewModelList.size();
    }

    public class ReviewViewHolder extends RecyclerView.ViewHolder {

        TextView reviewerName, reviewerDetails;

        public ReviewViewHolder(@NonNull View itemView) {
            super(itemView);

            reviewerName = itemView.findViewById(R.id.reviewerNameId);
            reviewerDetails = itemView.findViewById(R.id.reviewerDetailsId);

        }
    }
}
