package com.example.houserentproject.additionalAdapter;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.houserentproject.R;

public class EmergencyContactViewHolder extends RecyclerView.ViewHolder {

    TextView name, phone;

    public EmergencyContactViewHolder(@NonNull View itemView) {
        super(itemView);

        name = itemView.findViewById(R.id.emergencyNameId);
        phone = itemView.findViewById(R.id.emergencyMobileTxtId);
    }
}
