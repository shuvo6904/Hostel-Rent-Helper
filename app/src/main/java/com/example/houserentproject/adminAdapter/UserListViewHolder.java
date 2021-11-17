package com.example.houserentproject.adminAdapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.houserentproject.R;

public class UserListViewHolder extends RecyclerView.ViewHolder {

    ImageView imageView;
    TextView userName, userEmail, userPhone;

    public UserListViewHolder(@NonNull View itemView) {
        super(itemView);


        imageView = itemView.findViewById(R.id.userListProfileImageViewId);
        userName = itemView.findViewById(R.id.userListNameId);
        userEmail = itemView.findViewById(R.id.userListEmailId);
        userPhone = itemView.findViewById(R.id.userListPhoneId);
    }
}
