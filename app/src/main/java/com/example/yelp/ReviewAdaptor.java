package com.example.yelp;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.util.Collections;
import java.util.List;

public class ReviewAdaptor extends RecyclerView.Adapter<ReviewViewHolder>{
    List<ReviewItem> list = Collections.emptyList();
    Context context;

    public ReviewAdaptor(List<ReviewItem> list, Context context){
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.reviewlist_entry, parent, false);
        ReviewViewHolder reviewViewHolder = new ReviewViewHolder(view);
        return reviewViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        Integer index = holder.getBindingAdapterPosition();
        holder.Name.setText(list.get(index).user_name);
        holder.Rate.setText(list.get(index).rating);
        holder.Comment.setText(list.get(index).comment);
        holder.Date.setText(list.get(index).date);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void ClearItems(){
        this.list.clear();
    }
}
