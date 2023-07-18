package com.example.yelp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Tab_review_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Tab_review_Fragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RecyclerView recyclerView;
    private List<ReviewItem> list;

    public Tab_review_Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Tab_review_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Tab_review_Fragment newInstance(String param1, String param2) {
        Tab_review_Fragment fragment = new Tab_review_Fragment();
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
        }
        list = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tab_review_, container, false);
        recyclerView = view.findViewById(R.id.reviewlist);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        //get reviews from api call
        list.clear();
        getReviews(new VolleyCallBack() {
            @Override
            public void onSuccess() {
                //after getting reviews

            }
        }, mParam1);

        return view;
    }

    public void getReviews(final VolleyCallBack callBack, String id){
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String url = "https://test1-365010.wl.r.appspot.com/api/reviewyelp/" + id;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("reviews", response.toString());
                        //show result table
                        try {
                            JSONArray reviews = response.getJSONArray("reviews");
                            for(int i=0; i< reviews.length();i++){
                                ReviewItem item = new ReviewItem();
                                item.user_name = reviews.getJSONObject(i).getJSONObject("user").optString("name");
                                item.rating = "Rating: " + reviews.getJSONObject(i).optString("rating") + "/5";
                                item.comment = reviews.getJSONObject(i).optString("text");
                                item.date = reviews.getJSONObject(i).optString("time_created").split(" ")[0];
                                list.add(item);
                            }
                            ReviewAdaptor adaptor = new ReviewAdaptor(list, getActivity());
                            recyclerView.setAdapter(adaptor);

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