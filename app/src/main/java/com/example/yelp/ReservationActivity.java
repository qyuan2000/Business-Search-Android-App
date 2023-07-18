package com.example.yelp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class ReservationActivity extends AppCompatActivity {
    ArrayList<ReservationItem> list;
    ReservationAdaptor adaptor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation);
        //set visibility
        TextView label = findViewById(R.id.noreservation_label);
        label.setVisibility(View.INVISIBLE);
        RecyclerView recyclerView = findViewById(R.id.reserve_recyclerView);
        recyclerView.setVisibility(View.INVISIBLE);
        //set toolbar
        Toolbar toolbar = findViewById(R.id.resevation_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        //retrieve local storage
        adaptor = null;
        Gson gson = new Gson();
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String json = sharedPreferences.getString("reservationList", null);
        //deal with no reservation
        if(json == null){
            label.setVisibility(View.VISIBLE);
        }
        else{//show reservation table
            Type type = new TypeToken<ArrayList<ReservationItem>>() {}.getType();
            list = gson.fromJson(json, type);
            //set adaptor
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            adaptor = new ReservationAdaptor(list, getApplication());
            recyclerView.setAdapter(adaptor);
            recyclerView.setVisibility(View.VISIBLE);
        }

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                ReservationItem deletedItem = list.get(viewHolder.getBindingAdapterPosition());
                int position = viewHolder.getBindingAdapterPosition();
                list.remove(position);
                adaptor.notifyItemRemoved(position);
                //update local storage
                if(list.size() == 0){
                    editor.remove("reservationList").commit();
                }
                else {
                    String newjson = gson.toJson(list);
                    editor.putString("reservationList", newjson);
                    editor.apply();
                }
                //show no reservation label if empty
                if(list.size() ==0){
                    recyclerView.setVisibility(View.INVISIBLE);
                    label.setVisibility(View.VISIBLE);
                }
                //show snackbar
                Snackbar snackbar = Snackbar.make(recyclerView, "Removing Existing Reservation",
                        Snackbar.LENGTH_LONG);
                snackbar.show();
            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                Integer itemHeight = viewHolder.itemView.getHeight();
                //draw background
                ColorDrawable background = new ColorDrawable();
                background.setColor(Color.RED);
                background.setBounds(viewHolder.itemView.getRight() + (int) Math.floor(dX), viewHolder.itemView.getTop(), viewHolder.itemView.getRight(), viewHolder.itemView.getBottom());
                background.draw(c);
                //draw delete icon
                Drawable deleteIcon = ContextCompat.getDrawable(getBaseContext(), R.drawable.ic_delete_white_24);
                Integer intrinsicHeight = deleteIcon.getIntrinsicHeight();
                Integer intrinsicWidth = deleteIcon.getIntrinsicWidth();
                Integer deleteIconTop = viewHolder.itemView.getTop() + (itemHeight - intrinsicHeight) / 2;
                Integer deleteIconMargin = (itemHeight - intrinsicHeight) / 2;
                Integer deleteIconLeft = viewHolder.itemView.getRight() - deleteIconMargin - intrinsicWidth;
                Integer deleteIconRight = viewHolder.itemView.getRight() - deleteIconMargin;
                Integer deleteIconBottom = deleteIconTop + intrinsicHeight;
                deleteIcon.setBounds(deleteIconLeft, deleteIconTop, deleteIconRight, deleteIconBottom);
                deleteIcon.draw(c);


                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }

        }).attachToRecyclerView(recyclerView);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return false;
    }
}