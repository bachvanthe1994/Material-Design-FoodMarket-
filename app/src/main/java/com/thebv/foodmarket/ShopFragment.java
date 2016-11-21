package com.thebv.foodmarket;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.transition.ChangeBounds;
import android.transition.ChangeImageTransform;
import android.transition.ChangeTransform;
import android.transition.TransitionSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;

import java.util.Timer;
import java.util.TimerTask;


public class ShopFragment extends Fragment {

    private int position;
    private View view;
    private OnListenerPageClick listener;


    public ShopFragment() {
    }

    public void setOnPageClick(OnListenerPageClick listener) {
        this.listener = listener;
    }

    @SuppressLint("ValidFragment")
    public ShopFragment(int position) {
        this.position = position;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_shop, container, false);

        final String tag = "content" + position;

        view.findViewById(R.id.cvItem).setTag(tag);
        view.findViewById(R.id.cvItem).setTransitionName(tag);

        AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 0.0f);
        alphaAnimation.setDuration(0);
        alphaAnimation.setFillAfter(true);
        view.findViewById(R.id.llContent).startAnimation(alphaAnimation);

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        AlphaAnimation alphaAnimation = new AlphaAnimation(0.5f, 1.0f);
                        alphaAnimation.setDuration(500);
                        alphaAnimation.setFillAfter(true);
                        view.findViewById(R.id.llContent).startAnimation(alphaAnimation);
                    }
                });
            }
        }, 500);


        view.findViewById(R.id.cvItem).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null)
                    listener.onClick(position);
            }
        });

        ImageView img = (ImageView) view.findViewById(R.id.ivBanner);
        switch (position) {
            case 0:
                img.setImageResource(R.drawable.img1);
                break;
            case 1:
                img.setImageResource(R.drawable.img2);
                break;
            case 2:
                img.setImageResource(R.drawable.img3);
                break;
        }

        return view;
    }

    public static class DetailsTransition extends TransitionSet {
        public DetailsTransition() {
            setOrdering(ORDERING_TOGETHER);
            addTransition(new ChangeBounds()).
                    addTransition(new ChangeTransform()).
                    addTransition(new ChangeImageTransform());
        }
    }

    public static interface OnListenerPageClick {
        void onClick(int position);
    }
}
