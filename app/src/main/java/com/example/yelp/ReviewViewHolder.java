package com.example.yelp;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ReviewViewHolder extends RecyclerView.ViewHolder {
    TextView Name;
    TextView Rate;
    TextView Comment;
    TextView Date;
    View view;

    public ReviewViewHolder(@NonNull View itemView) {
        super(itemView);
        Name = (TextView)itemView.findViewById(R.id.username);
        Rate = (TextView) itemView.findViewById(R.id.rating);
        Comment = (TextView) itemView.findViewById(R.id.comment);
        Date = (TextView) itemView.findViewById(R.id.date);
        view  = itemView;
    }
}
