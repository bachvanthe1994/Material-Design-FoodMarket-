package com.thebv.foodmarket;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;

import java.util.Timer;
import java.util.TimerTask;


public class CartFragment extends Fragment {

    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_cart, container, false);


        AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 0.5f);
        alphaAnimation.setDuration(0);
        alphaAnimation.setFillAfter(true);
        view.startAnimation(alphaAnimation);
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        AlphaAnimation alphaAnimation = new AlphaAnimation(0.5f, 1.0f);
                        alphaAnimation.setDuration(200);
                        alphaAnimation.setFillAfter(true);
                        view.startAnimation(alphaAnimation);
                    }
                });
            }
        }, 50);


        return view;
    }

    @Override
    public void onDestroyView() {
        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
        alphaAnimation.setDuration(200);
        alphaAnimation.setFillAfter(true);
        view.startAnimation(alphaAnimation);

        super.onDestroyView();
    }
}
