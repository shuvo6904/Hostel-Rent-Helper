package com.example.houserentproject.adminAdapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.houserentproject.R;
import com.example.houserentproject.adminModel.UserListModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
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

        String userID = userListModel.getUserID();

        /**if (userListModel.getProfileImg().isEmpty()) {
            holder.imageView.setImageResource(R.drawable.profile);
        } else{
            Picasso.get().load(userListModel.getProfileImg()).into(holder.imageView);
        } **/

        StorageReference userDetailsImageRef = FirebaseStorage.getInstance().getReference().child("Users/"+userID+"/profile.jpg");
        DocumentReference documentReference = FirebaseFirestore.getInstance().collection("users").document(userID);

        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {


                if (value.getString("userID").isEmpty()) {
                    holder.imageView.setImageResource(R.drawable.profile);
                } else{
                    userDetailsImageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Picasso.get().load(uri).into(holder.imageView);
                        }
                    });
                }


            }
        });

        holder.userName.setText(userListModel.getfName());
        holder.userEmail.setText(userListModel.getEmail());
        holder.userPhone.setText(userListModel.getPhnNumber());

    }

    @Override
    public int getItemCount() {
        return myUserDataList.size();
    }
}
