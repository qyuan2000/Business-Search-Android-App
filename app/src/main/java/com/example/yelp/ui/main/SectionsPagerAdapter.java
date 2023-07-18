package com.example.yelp.ui.main;

import android.content.Context;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.yelp.R;
import com.example.yelp.Tab_detail_Fragment;
import com.example.yelp.Tab_review_Fragment;
import com.example.yelp.map_tab_Fragment;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.tab_text_1, R.string.tab_text_2, R.string.tab_text_3};
    private final Context mContext;
    private String params_for_detail;
    private Tab_detail_Fragment tab1;
    private map_tab_Fragment tab2;
    private Tab_review_Fragment tab3;

    public SectionsPagerAdapter(Context context, FragmentManager fm, String params_for_detail) {
        super(fm);
        mContext = context;
        //this.params_for_detail = params_for_detail;
        Log.d("page adaptor constructor ", params_for_detail);
        tab1 = Tab_detail_Fragment.newInstance(params_for_detail,"");
        tab2 = new map_tab_Fragment(params_for_detail);
        tab3 = Tab_review_Fragment.newInstance(params_for_detail,"");
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        switch (position){
            case 0:
                Log.d("getItem", "get tab1");
                return tab1;
            case 1:
                return tab2;
            case 2:
                return tab3;
        }
        return null;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        // Show 2 total pages.
        return 3;
    }
}