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

public class ListProductAdapter extends FragmentStatePagerAdapter {
    public int mNumOfTabs;
    public ListProductFragment.OnClickProduct onClickProduct;
    public Map<Integer, ListProductFragment> mapItem = new HashMap<>();

    public ListProductAdapter(FragmentManager fm, int NumOfTabs, ListProductFragment.OnClickProduct onClickProduct) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
        this.onClickProduct = onClickProduct;
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
        if (mapItem.get(position) == null) {
            ListProductFragment fm = new ListProductFragment();
            switch (position) {
                case 0:
                    fm.setNameOfFragment("MEAT");
                    break;
                case 1:
                    fm.setNameOfFragment("SEAFOOD");
                    break;
                case 2:
                    fm.setNameOfFragment("VEGETABLE");
                    break;
            }
            fm.setPosition(position);
            fm.setOnClickProduct(onClickProduct);
            mapItem.put(position, fm);
        }

        return mapItem.get(position);
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}