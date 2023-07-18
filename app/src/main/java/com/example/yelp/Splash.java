package com.example.yelp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Executor;

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        //startActivities(new Intent[]{new Intent(this, MainActivity.class)});

        startActivities(new Intent[]{new Intent(this, Splash2.class)});

    }

}