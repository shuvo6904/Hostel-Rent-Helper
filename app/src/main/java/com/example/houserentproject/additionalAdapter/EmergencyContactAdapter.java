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

public class EmergencyContactAdapter extends RecyclerView.Adapter<EmergencyContactViewHolder> {
    Context context;
    String [] name, phone;

    public EmergencyContactAdapter(Context context, String[] name, String[] phone) {
        this.context = context;
        this.name = name;
        this.phone = phone;
    }

    @NonNull
    @Override
    public EmergencyContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.emergency_contact_single_row, parent, false);
        return new EmergencyContactViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EmergencyContactViewHolder holder, int position) {

        holder.name.setText(name [position]);
        holder.phone.setText(phone [position]);

        holder.phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentCall = new Intent(Intent.ACTION_DIAL);
                intentCall.setData(Uri.parse("tel:" + holder.phone.getText().toString()));
                context.startActivity(intentCall);
            }
        });

    }

    @Override
    public int getItemCount() {
        return name.length;
    }
}
