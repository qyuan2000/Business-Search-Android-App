package com.example.yelp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.List;

public class ReservationAdaptor extends RecyclerView.Adapter<ReservationViewHolder>{
    List<ReservationItem> list = Collections.emptyList();
    Context context;

    public ReservationAdaptor(List<ReservationItem> list, Context context){
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ReservationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.reservetb_entry, parent, false);
        ReservationViewHolder reservationViewHolder = new ReservationViewHolder(view);
        return reservationViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ReservationViewHolder holder, int position) {
        Integer index = holder.getBindingAdapterPosition();
        Integer n = index +1;
        holder.Index.setText(n.toString());
        holder.Name.setText(list.get(index).name);
        holder.Date.setText(list.get(index).date);
        holder.Time.setText(list.get(index).time);
        holder.Email.setText(list.get(index).email);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    public void ClearItems(){
        this.list.clear();
    }
}
