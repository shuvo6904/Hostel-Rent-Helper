package com.example.houserentproject.adminAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.houserentproject.R;
import com.example.houserentproject.adminModel.UserListModel;
import com.squareup.picasso.Picasso;

import java.util.List;

public class UserListAdapter extends RecyclerView.Adapter<UserListViewHolder> {

    Context context;
    List<UserListModel> myUserDataList;

    public UserListAdapter(Context context, List<UserListModel> myUserDataList) {
        this.context = context;
        this.myUserDataList = myUserDataList;
    }

    @NonNull
    @Override
    public UserListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_row_user_list, parent, false);

        return new UserListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserListViewHolder holder, int position) {

        UserListModel userListModel = myUserDataList.get(position);
        if (userListModel.getProfileImg().isEmpty()) {
            holder.imageView.setImageResource(R.drawable.profile);
        } else{
            Picasso.get().load(userListModel.getProfileImg()).into(holder.imageView);
        }

        holder.userName.setText(userListModel.getfName());
        holder.userEmail.setText(userListModel.getEmail());
        holder.userPhone.setText(userListModel.getPhnNumber());

    }

    @Override
    public int getItemCount() {
        return myUserDataList.size();
    }
}
