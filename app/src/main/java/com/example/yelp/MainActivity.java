package com.example.yelp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.yelp.ui.main.VolleyCallBack;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity {
    Button submitbtn;
    Button resetbtn;
    ImageButton reservebtn;
    AutoCompleteTextView keyword;
    EditText distance;
    Spinner category;
    EditText location;
    CheckBox auto;
    ClickListener listiner;
    JSONObject resultResponse;
    ResultAdaptor adaptor;
    ArrayAdapter<String> autoAdaptor;
    List<String> autolist;
    TextView noresult_label;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //get layout item by id
        submitbtn = (Button)findViewById(R.id.submitbtn);
        resetbtn = (Button)findViewById(R.id.resetbtn);
        keyword = (AutoCompleteTextView) findViewById(R.id.keyword);
        distance = (EditText) findViewById(R.id.distance);
        category = (Spinner) findViewById(R.id.category);
        location = (EditText) findViewById(R.id.location);
        auto = (CheckBox) findViewById(R.id.autobox);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        reservebtn = (ImageButton)findViewById(R.id.reserveButton);
        noresult_label = findViewById(R.id.noresult_label);
        noresult_label.setVisibility(View.INVISIBLE);
        //define variables
        RequestQueue queue = Volley.newRequestQueue(this);
        final String[] latitude = {""};
        final String[] longitude = {""};
        List<ResultItem> list = new ArrayList<>();
        autolist = new ArrayList<>();
        //autolist.add("pizza");
        //define override function for clicklistiner of result table entry
        listiner = new ClickListener() {
            @Override
            public void click(int index) throws JSONException {
                Log.d("listiner", index + "is clicked");
                //open detail page
                String id = resultResponse.getJSONArray("businesses").getJSONObject(index).optString("id");
                String name = resultResponse.getJSONArray("businesses").getJSONObject(index).optString("name");
                Intent detailIntent = new Intent(MainActivity.this, detail_tabs.class);
                detailIntent.putExtra("businessId", id);
                detailIntent.putExtra("businessName", name);
                MainActivity.this.startActivity(detailIntent);
            }
        };
        //set adaptor
        adaptor = new ResultAdaptor(list, getApplication(), listiner);
        recyclerView.setAdapter(adaptor);
        //test autolist
        //autolist.add("pizza");
        autoAdaptor = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, autolist);
        //keyword.setAdapter(autoAdaptor);
        //autoAdaptor.getFilter().filter(keyword.getText(), null);
        autoAdaptor.setNotifyOnChange(true);
        keyword.setThreshold(0);
        //keyword.setAdapter(autoAdaptor);
        //submit button click
        submitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!checkValidation()){
                    Log.d("not valid form", "empty field");
                    return;
                }
                String serverapi_busi = "https://test1-365010.wl.r.appspot.com/api/searchyelp/";
                Integer radius = (int)Math.floor(Double.parseDouble(String.valueOf(distance.getText())) *1609.34);
                if(!auto.isChecked()){//get loc from location string
                    addr2loc(queue, latitude, longitude);
                }

                String url = serverapi_busi+ "term="+keyword.getText()+"&latitude=" + latitude[0] +"&longitude=" + longitude[0] + "&categories=" + category.getSelectedItem().toString() + "&radius=" + radius;
                Log.d("url", url);
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                        (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                Log.d("result", response.toString());
                                //show result table
                                try {
                                    resultResponse = response;
                                    showResultTable(response, recyclerView, list);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // TODO: Handle error
                            }
                        });
                //api call end
                queue.add(jsonObjectRequest);
            }
        });
        //reset button click
        resetbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                keyword.setText("");
                distance.setText("");
                category.setSelection(0);
                location.setText("");
                auto.setChecked(false);
                location.setEnabled(true);
                location.setVisibility(View.VISIBLE);
                noresult_label.setVisibility(View.INVISIBLE);
                //clear result table
                list.clear();
                adaptor.notifyDataSetChanged();
                latitude[0] = "34.02";//default value
                longitude[0] = "-118.28";//default value
            }
        });
        //checkbox check/uncheck
        auto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean checked = ((CheckBox) view).isChecked();
                if(checked){
                    location.setText("");
                    location.setEnabled(false);
                    location.setVisibility(View.INVISIBLE);
                    String url = "https://ipinfo.io/?token=cf377c256f76d5";
                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                            (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                                @Override
                                public void onResponse(JSONObject response) {
                                    Log.d("result", response.toString());
                                    latitude[0] = response.optString("loc").split(",")[0];
                                    longitude[0] = response.optString("loc").split(",")[1];
                                }
                            }, new Response.ErrorListener() {

                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    // TODO: Handle error

                                }
                            });
                    //api call end
                    queue.add(jsonObjectRequest);
                }
                else {
                    location.setEnabled(true);
                    location.setVisibility(View.VISIBLE);
                    latitude[0] = "34.02";//default value
                    longitude[0] = "-118.28";//default value
                }
            }
        });

        //keyword text changed
        keyword.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.d("before textchanged function test", "function start");
                Log.d("check dataset", "notify dataset changed");
                //autoAdaptor.notifyDataSetChanged();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(keyword.isPerformingCompletion()) return;
                Log.d("textchanged function test", "function start");
                getAutoResult(new VolleyCallBack() {
                    @Override
                    public void onSuccess() {
                        Log.d("check dataset", "notify dataset changed");
                        keyword.setAdapter(autoAdaptor);
                        //autoAdaptor.notifyDataSetChanged();
                        keyword.showDropDown();
                        Log.d("check autolist", autolist.toString());
                    }
                });

                Log.d("textchanged function test", "function end");
            }

            @Override
            public void afterTextChanged(Editable editable) {
                Log.d("after textchanged function test", "function start");
            }
        });
        //on click reserve btn on the toolbar
        reservebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent reservationIntent = new Intent(MainActivity.this, ReservationActivity.class);
                MainActivity.this.startActivity(reservationIntent);
            }
        });

    }

    private void getAutoResult(final VolleyCallBack callBack){
        RequestQueue queue = Volley.newRequestQueue(this);
        String s = String.valueOf(keyword.getText());
        String url = "https://test1-365010.wl.r.appspot.com/api/autowhynotwork/" + s;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("autocomplete result", response.toString());

                        try {
                            JSONArray terms = response.getJSONArray("terms");
                            JSONArray categories = response.getJSONArray("categories");
                            autolist.clear();
                            autoAdaptor.clear();
                            keyword.setAdapter(null);
                            //autoAdaptor.notifyDataSetChanged();
                            for(int i=0; i<terms.length();i++){
                                //autolist.add(terms.getJSONObject(i).optString("text"));
                                autoAdaptor.add(terms.getJSONObject(i).optString("text"));
                            }
                            for(int i=terms.length(); i< terms.length()+categories.length(); i++){
                                //autolist.add(categories.getJSONObject(i-terms.length()).optString("title"));
                                autoAdaptor.add(categories.getJSONObject(i-terms.length()).optString("title"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        autoAdaptor.notifyDataSetChanged();
                        //wait for call response
                        callBack.onSuccess();
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error

                    }
                });
        //api call end
        queue.add(jsonObjectRequest);
    }



    private void showResultTable(JSONObject response, RecyclerView recyclerView, List<ResultItem> list) throws JSONException {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        JSONArray array = response.getJSONArray("businesses");
        //deal with no result
        if(array.length() == 0){
            noresult_label.setVisibility(View.VISIBLE);
            return;
        }

        for(int i=0; i<array.length(); i++){
            ResultItem item = new ResultItem();
            item.name = array.getJSONObject(i).optString("name");
            item.img_url = array.getJSONObject(i).optString("image_url");
            item.rate = array.getJSONObject(i).optString("rating");
            String s = array.getJSONObject(i).optString("distance");
            int dis = (int)(Double.parseDouble(s)/1609.34);
            item.distance = String.valueOf(dis);
            list.add(item);
        }

    }

    private void addr2loc(RequestQueue queue, String[] latitude, String[] longitude){
        String addressKey = String.valueOf(location.getText()).trim().replace(", ", "+").replace(" ", "+").replace(",", "+");
        String geocodeURL = "https://maps.googleapis.com/maps/api/geocode/json?address="+ addressKey +"&key=";//add api key string from config
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, geocodeURL, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("geocodeURL", response.toString());
                        JSONObject obj= null;
                        try {
                            obj = response.getJSONArray("results").getJSONObject(0).getJSONObject("geometry").getJSONObject("location");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        latitude[0] = obj.optString("lat");
                        longitude[0] = obj.optString("lng");
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                    }
                });
        //api call end
        queue.add(jsonObjectRequest);
    }

    private boolean checkValidation(){
        if(keyword.getText().length()==0){
            keyword.setError("This field is required");
            return false;
        }
        if(distance.getText().length()==0){
            distance.setError("This field is required");
            return false;
        }
        if(location.getText().length()==0 && !auto.isChecked()){
            location.setError("This field is required");
            return false;
        }
        return true;
    }
}