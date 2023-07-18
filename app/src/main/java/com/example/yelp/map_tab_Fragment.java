package com.example.yelp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.yelp.ui.main.VolleyCallBack;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class map_tab_Fragment extends Fragment {
    Double lat;
    Double lng;
    String id;

    public  map_tab_Fragment(String id){
        this.id = id;
    }

    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        @Override
        public void onMapReady(GoogleMap googleMap) {

            //get business location
            //Double lat = ((detail_tabs)getActivity()).getLat();
            //Double lng = ((detail_tabs)getActivity()).getLng();
            getLocation(new VolleyCallBack() {
                @Override
                public void onSuccess() {
                    //after getting reviews
                    Log.d("check location in map", lat + " " + lng);
                    LatLng location = new LatLng(lat, lng);
                    googleMap.addMarker(new MarkerOptions().position(location).title("Marker of business"));
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15.0f));
                }
            }, id);


        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_map_tab_, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }

    public void getLocation(final VolleyCallBack callBack, String id){
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String url = "https://test1-365010.wl.r.appspot.com/api/detailyelp/" + id;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("reviews", response.toString());
                        //show result table
                        try {
                            lat = Double.parseDouble(response.getJSONObject("coordinates").optString("latitude"));
                            lng = Double.parseDouble(response.getJSONObject("coordinates").optString("longitude"));


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