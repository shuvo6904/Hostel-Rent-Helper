package com.example.houserentproject.additionalAdapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.houserentproject.R;

public class NstuContactAdapter extends RecyclerView.Adapter<NstuContactViewHolder>{

    Context context;
    String [] designation, name, mobile, phone, email, website;

    public NstuContactAdapter(Context context, String[] designation, String[] name, String[] mobile, String[] phone, String[] email, String[] website) {
        this.context = context;
        this.designation = designation;
        this.name = name;
        this.mobile = mobile;
        this.phone = phone;
        this.email = email;
        this.website = website;
    }

    @NonNull
    @Override
    public NstuContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.nstu_contact_single_row,parent,false);
        return new NstuContactViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NstuContactViewHolder holder, int position) {

        holder.designation.setText(designation[position]);
        holder.name.setText(name[position]);
        holder.mobile.setText(mobile[position]);
        holder.phone.setText(phone[position]);
        holder.email.setText(email[position]);
        holder.website.setText(website[position]);

        holder.mobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentCall = new Intent(Intent.ACTION_DIAL);
                intentCall.setData(Uri.parse("tel:" + holder.mobile.getText().toString()));
                context.startActivity(intentCall);
            }
        });

        holder.phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentCall = new Intent(Intent.ACTION_DIAL);
                intentCall.setData(Uri.parse("tel:" + holder.phone.getText().toString()));
                context.startActivity(intentCall);
            }
        });

        holder.email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentEmail = new Intent(Intent.ACTION_VIEW);
                intentEmail.setData(Uri.parse("mailto:" + holder.email.getText().toString()));
                context.startActivity(intentEmail);
            }
        });

    }

    @Override
    public int getItemCount() {
        return designation.length;
    }
}
