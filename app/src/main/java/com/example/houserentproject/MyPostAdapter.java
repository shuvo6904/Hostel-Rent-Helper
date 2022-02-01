package com.example.houserentproject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.houserentproject.additionalActivity.EditPostActivityController;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class MyPostAdapter extends RecyclerView.Adapter<MyPostsHomePageViewHolder> {

    private Context myPostContext;
    private List<HomePageDataModel> myPostHomePageDataModelList;

    public MyPostAdapter(Context myPostContext, List<HomePageDataModel> myPostHomePageDataModelList) {
        this.myPostContext = myPostContext;
        this.myPostHomePageDataModelList = myPostHomePageDataModelList;
    }

    @NonNull
    @Override
    public MyPostsHomePageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View myPostView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_my_post_row_item , parent, false);
        return new MyPostsHomePageViewHolder(myPostView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyPostsHomePageViewHolder holder, int position) {

        HomePageDataModel myPostModel = myPostHomePageDataModelList.get(position);

        Glide.with(myPostContext)
                .load(myPostModel.getImage())
                .into(holder.myPostImageView);
        holder.myPostRentAmount.setText(myPostModel.getRentAmount());
        holder.myPostLocation.setText(myPostModel.getLocation());
        holder.myPostStatus.setText(myPostModel.getPostStatus());

        holder.myPostCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(myPostContext, PostDetailsActivity.class);
                intent.putExtra("postModel",myPostModel);
                myPostContext.startActivity(intent);
            }
        });

        holder.deleteImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(holder.deleteImageBtn.getContext());
                builder.setTitle("Delete Post");
                builder.setMessage("Are You Sure Want to Delete This Post?");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        FirebaseDatabase.getInstance().getReference().child("Data")
                                .child(myPostModel.getAdUserId()).child(myPostModel.getId()).removeValue();

                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                });

                builder.show();

            }
        });

        holder.editPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(myPostContext, EditPostActivityController.class);
                intent.putExtra("editPostModel",myPostModel);
                myPostContext.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return myPostHomePageDataModelList.size();
    }
}
