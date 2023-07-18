package com.example.yelp;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ReservationViewHolder extends RecyclerView.ViewHolder{
    TextView Index;
    TextView Name;
    TextView Date;
    TextView Time;
    TextView Email;
    View view;

    public ReservationViewHolder(@NonNull View itemView) {
        super(itemView);
        Index = (TextView)itemView.findViewById(R.id.reserve_index);
        Name = (TextView)itemView.findViewById(R.id.reserve_name);
        Date = (TextView) itemView.findViewById(R.id.reserve_date);
        Time = (TextView) itemView.findViewById(R.id.reserve_time);
        Email = (TextView) itemView.findViewById(R.id.reserve_email);
        view  = itemView;
    }
}
