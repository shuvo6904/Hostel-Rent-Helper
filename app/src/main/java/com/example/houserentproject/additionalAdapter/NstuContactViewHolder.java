package com.example.houserentproject.additionalAdapter;


import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.houserentproject.R;

public class NstuContactViewHolder extends RecyclerView.ViewHolder {

    TextView designation, name, mobile, phone, email, website;

    public NstuContactViewHolder(@NonNull View itemView) {
        super(itemView);

        designation = itemView.findViewById(R.id.designationId);
        name = itemView.findViewById(R.id.nstuNameId);
        mobile = itemView.findViewById(R.id.nstuMobileId);
        phone= itemView.findViewById(R.id.nstuPhoneId);
        email = itemView.findViewById(R.id.nstuEmailId);
        website = itemView.findViewById(R.id.nstuWebsiteId);



    }
}
