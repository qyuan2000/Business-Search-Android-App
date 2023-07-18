package com.example.yelp;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;


public class ResultViewHolder extends RecyclerView.ViewHolder {

    TextView Index;
    TextView Name;
    TextView Rate;
    TextView Distance;
    ImageView Figure;
    View view;

    public ResultViewHolder(@NonNull View itemView) {
        super(itemView);
        Index = (TextView)itemView.findViewById(R.id.index);
        Name = (TextView) itemView.findViewById(R.id.businame);
        Rate = (TextView) itemView.findViewById(R.id.busirate);
        Distance = (TextView) itemView.findViewById(R.id.busidistance);
        Figure = (ImageView)itemView.findViewById(R.id.figure);
        view  = itemView;
    }
}
