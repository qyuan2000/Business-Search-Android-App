package com.example.yelp;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.yelp.ui.main.VolleyCallBack;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RunnableScheduledFuture;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Tab_detail_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Tab_detail_Fragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String id;
    private List<String> detail_list;
    private String resulttest;//for async test
    private String busiaddr;
    private String price;
    private String phone;
    private boolean status;
    private String categ;
    private String busiurl;
    private List<String> imgurl;
    public String fburl;
    public String twiurl;
    Button reservenowbtn;
    Integer valid_reservation;



    public Tab_detail_Fragment(String param) {
        // Required empty public constructor
        id = param;
        Log.d("call constructor", "constructor");
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Tab_detail_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Tab_detail_Fragment newInstance(String param1, String param2) {
        Tab_detail_Fragment fragment = new Tab_detail_Fragment(param1);
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            Log.d("fragment on create", "set params");
        }
        Log.d("fragment on create", "oncreate is called");

        detail_list = new ArrayList<>();
        imgurl = new ArrayList<>();
        valid_reservation = 0;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tab_detail_, container, false);
        Log.d("check params", mParam1 + " " + mParam2);
        //clear variables

        getDetails(new VolleyCallBack() {
            @Override
            public void onSuccess() {

                Log.d("check response", resulttest);
            }
        }, id);
        //reserve now btn on click
        reservenowbtn = view.findViewById(R.id.reservebutton);
        reservenowbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //set dialog
                Dialog dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.custom_dialog);
                //set dialog components
                TextView title = dialog.findViewById(R.id.formtitle);
                title.setText(((detail_tabs)getActivity()).getName());
                EditText email = dialog.findViewById(R.id.email);
                EditText date = dialog.findViewById(R.id.editTextDate);
                EditText time = dialog.findViewById(R.id.editTextTime);
                //pop up date picker
                final String[] formatted_date = new String[1];
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int y, int m, int d) {
                        Integer i = m+1;
                        formatted_date[0] = i + "-" + d + "-" + y;
                        date.setText(formatted_date[0]);
                    }
                }, 2022, 11, 6);
                date.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                        datePickerDialog.show();
                    }
                });

                //pop up time picker
                final boolean[] timechecker = new boolean[1];
                timechecker[0] = true;
                final String[] formatted_time = new String[1];
                TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        if(i<10 | i>17) timechecker[0] = false;
                        formatted_time[0] = "";
                        if(i<10) formatted_time[0] +="0";
                        formatted_time[0] += i;
                        formatted_time[0] += ":";
                        if(i1<10) formatted_time[0] +="0";
                        formatted_time[0] += i1;
                        time.setText(formatted_time[0]);
                    }
                }, 12, 0, false);
                time.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        timePickerDialog.show();
                    }
                });
                //set cancel button
                TextView cancel = dialog.findViewById(R.id.canceltext);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                //set submit button
                TextView submit = dialog.findViewById(R.id.submittext);
                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //check validation
                        valid_reservation = 0;
                        String emailinput = String.valueOf(email.getText());
                        if(!Patterns.EMAIL_ADDRESS.matcher(emailinput).matches()){//invalid email
                            valid_reservation = 1;
                        }
                        if(!timechecker[0]){//invalid time
                            valid_reservation = 2;
                        }
                        //close dialog
                        dialog.dismiss();
                        //make reservation and show toast message
                        makeReservation(valid_reservation, ((detail_tabs)getActivity()).getName(), formatted_date[0], formatted_time[0], emailinput);

                    }
                });

                //show dialog
                dialog.show();
            }
        });

        return view;

    }

    public void makeReservation(Integer integer, String name, String date, String time, String email){
        if(integer==1){
            Toast.makeText(getActivity(), "Invalid Email Adrress",
                    Toast.LENGTH_LONG).show();
        }
        else if(integer==2){
            Toast.makeText(getActivity(), "Time should be between 10AM and 5PM",
                    Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(getActivity(), "Reservation Booked",
                    Toast.LENGTH_LONG).show();
            //store in local storage
            ReservationItem item = new ReservationItem(name, date, time, email);
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("shared preferences", 0);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            Gson gson = new Gson();
            if(sharedPreferences.getString("reservationList", "").equals("")){//empty local storage now
                ArrayList<ReservationItem> list = new ArrayList<>();
                list.add(item);
                String json = gson.toJson(list);
                editor.putString("reservationList", json);
                editor.apply();
            }
            else {//retrieve current storage first
                String json = sharedPreferences.getString("reservationList", null);
                Type type = new TypeToken<ArrayList<ReservationItem>>() {}.getType();
                ArrayList<ReservationItem> list = gson.fromJson(json, type);
                //check if repeat reservation
                for(int i=0; i< list.size();i++){
                    Log.d("check item", String.valueOf(i) + ": " + list.get(i).name + " " + item.name);
                    if(list.get(i).name.equals(item.name)){
                        list.remove(i);
                        Log.d("delete item", String.valueOf(i));
                        break;
                    }
                }
                list.add(item);
                String newjson = gson.toJson(list);
                editor.putString("reservationList", newjson);
                editor.apply();
            }

        }
    }

    public void getDetails(final VolleyCallBack callBack, String id){
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String url = "https://test1-365010.wl.r.appspot.com/api/detailyelp/" + id;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("result", response.toString());
                        resulttest = response.toString();
                        try {
                            JSONArray addr = response.getJSONObject("location").getJSONArray("display_address");
                            busiaddr = "";
                            for(int i=0; i< addr.length();i++){
                                busiaddr += addr.getString(i);
                                busiaddr += " ";
                            }
                            price = response.optString("price");
                            phone = response.optString("display_phone");
                            busiurl = response.optString("url");
                            status = response.getBoolean("is_closed");
                            categ = "";
                            JSONArray cat = response.getJSONArray("categories");
                            for(int i =0; i< cat.length(); i++){
                                categ += cat.getJSONObject(i).optString("title");
                                if(i < cat.length()-1) categ += " | ";
                            }
                            imgurl.clear();
                            JSONArray photos = response.getJSONArray("photos");
                            for(int i=0; i< photos.length(); i++){
                                imgurl.add(photos.getString(i));
                            }
                            fburl = "https://www.facebook.com/sharer/sharer.php?u=" + busiurl;
                            twiurl = "https://twitter.com/intent/tweet?text=Check%20"+ response.optString("name") + "%20on%20Yelp.&url=" + busiurl;
                            ((detail_tabs)getActivity()).setFbUrl(fburl);
                            ((detail_tabs)getActivity()).setTwiUrl(twiurl);
                            Double lat = Double.parseDouble(response.getJSONObject("coordinates").optString("latitude"));
                            Double lng = Double.parseDouble(response.getJSONObject("coordinates").optString("longitude"));
                            ((detail_tabs)getActivity()).setLocation(lat,lng);
                            //set layout value
                            TextView addrtext = (TextView) getView().findViewById(R.id.addrtext);
                            addrtext.setText(busiaddr);
                            TextView pricetext = (TextView) getView().findViewById(R.id.pricetext);
                            pricetext.setText(price);
                            TextView phonetext = (TextView) getView().findViewById(R.id.phonetext);
                            phonetext.setText(phone);
                            TextView stutustext = (TextView) getView().findViewById(R.id.statustext);
                            stutustext.setText(status?"Closed":"Open Now");
                            stutustext.setTextColor(status? Color.RED:Color.GREEN);
                            TextView cattext = (TextView) getView().findViewById(R.id.categtext);
                            cattext.setText(categ);
                            TextView linktext = (TextView) getView().findViewById(R.id.linktext);
                            linktext.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(busiurl));
                                    startActivity(myIntent);
                                }
                            });
                            //set image browse block
                            ImageView img1 = (ImageView) getView().findViewById(R.id.img1);
                            ImageView img2 = (ImageView) getView().findViewById(R.id.img2);
                            ImageView img3 = (ImageView) getView().findViewById(R.id.img3);
                            for(int i=0; i< imgurl.size(); i++){
                                if(i==0){
                                    Picasso.get().load(imgurl.get(i)).into(img1);
                                }
                                if(i==1){
                                    Picasso.get().load(imgurl.get(i)).into(img2);
                                }
                                if(i==2){
                                    Picasso.get().load(imgurl.get(i)).into(img3);
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


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

}