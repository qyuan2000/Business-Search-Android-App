package com.example.yelp;

import org.json.JSONException;

public abstract class ClickListener {
    public abstract void click(int index) throws JSONException;
}
