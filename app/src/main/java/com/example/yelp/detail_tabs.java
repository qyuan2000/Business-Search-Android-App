package com.example.yelp;

import android.app.ActionBar;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.yelp.ui.main.SectionsPagerAdapter;
import com.example.yelp.databinding.ActivityDetailTabsBinding;

public class detail_tabs extends AppCompatActivity {

    private ActivityDetailTabsBinding binding;
    TextView title;
    String id;
    public double lat;
    public double lng;
    public String fburl;
    public String twiurl;
    ImageButton fbbtn;
    ImageButton twibtn;
    String businessName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        binding = ActivityDetailTabsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //get detail id
        Intent intent = getIntent();
        id = intent.getStringExtra("businessId");
        String name = intent.getStringExtra("businessName");
        title = binding.title;
        title.setText(name);
        businessName = name;

        //set action bar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        //button on toolbar
        fbbtn = findViewById(R.id.fbButton);
        twibtn = findViewById(R.id.twiButton);
        fbbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(fburl));
                startActivity(myIntent);
            }
        });
        twibtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(twiurl));
                startActivity(myIntent);
            }
        });

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager(), id);
        ViewPager viewPager = binding.viewPager;
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = binding.tabs;
        tabs.setupWithViewPager(viewPager);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return false;
    }

    public String getID(){
        return id;
    }

    public void setLocation(Double a, Double b){
        this.lat = a;
        this.lng = b;
    }
    public Double getLat(){
        return lat;
    }
    public Double getLng(){
        return lng;
    }
    public void setFbUrl(String url){
        this.fburl = url;
    }
    public void setTwiUrl(String url){
        this.twiurl = url;
    }
    public String getName(){
        return businessName;
    }

}