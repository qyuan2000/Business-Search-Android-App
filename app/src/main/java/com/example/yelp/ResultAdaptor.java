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

public class ResultAdaptor extends RecyclerView.Adapter<ResultViewHolder>{

    List<ResultItem> list = Collections.emptyList();
    Context context;
    ClickListener listiner;

    public ResultAdaptor(List<ResultItem> list, Context context, ClickListener listiner){
        this.list = list;
        this.context = context;
        this.listiner = listiner;
    }
    @NonNull
    @Override
    public ResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.resulttb_entry, parent, false);
        ResultViewHolder resultViewHolder = new ResultViewHolder(view);
        return resultViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ResultViewHolder holder, int position) {
        Integer index = holder.getBindingAdapterPosition();
        Integer n = index+1;
        holder.Index.setText(n.toString());
        holder.Name.setText(list.get(index).name);
        holder.Rate.setText(list.get(index).rate);
        holder.Distance.setText(list.get(index).distance);
        Picasso.get().load(list.get(index).img_url).into(holder.Figure);

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                try {
                    listiner.click(index);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onAttachedToRecyclerView(
            RecyclerView recyclerView)
    {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public void ClearItems(){
        this.list.clear();
    }
}
