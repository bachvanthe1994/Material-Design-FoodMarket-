package com.thebv.foodmarket;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by thebv on 23/09/2016.
 */

public class ShopAdapter extends FragmentStatePagerAdapter {
    public int mNumOfTabs;
    public ShopFragment.OnListenerPageClick listenerPageClick;
    public Map<Integer, ShopFragment> mapFragment = new HashMap<>();

    public ShopAdapter(FragmentManager fm, int NumOfTabs, ShopFragment.OnListenerPageClick listenerPageClick) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
        this.listenerPageClick = listenerPageClick;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        //block destroy
    }

    @Override
    public void destroyItem(View container, int position, Object object) {
        //block destroy
    }

    @Override
    public Fragment getItem(int position) {
        if (mapFragment.get(position) == null) {
            ShopFragment fg = new ShopFragment(position);
            fg.setOnPageClick(listenerPageClick);
            mapFragment.put(position, fg);
        }

        return mapFragment.get(position);
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}