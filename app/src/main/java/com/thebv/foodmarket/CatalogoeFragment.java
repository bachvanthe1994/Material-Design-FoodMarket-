package com.thebv.foodmarket;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class CatalogoeFragment extends Fragment implements View.OnClickListener {


    private View view;
    private ViewPager viewPager;
    private int countProductAdd = 0;
    private TextView tvCountProductAdd;
    private ImageView ivPreviousPager;
    private ImageView ivNextPager;
    private ListProductAdapter adapter;
    private ImageView ivBack;
    private ImageView ivCart;

    private float oldX = -1;
    private float percent = -1;
    private float alpha = -1;
    private View currentPage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_calalogoe, container, false);

        Log.d("the::", "getArguments().getString(\"tag\") = " + getArguments().getString("tag"));

        init();

        setupViewPaper();

        return view;
    }

    private void init() {
        view.findViewById(R.id.rvContent).setTransitionName(getArguments().getString("tag"));
        tvCountProductAdd = (TextView) view.findViewById(R.id.tvCountProductAdd);
        ivPreviousPager = (ImageView) view.findViewById(R.id.ivPreviousPager);
        ivNextPager = (ImageView) view.findViewById(R.id.ivNextPager);
        ivBack = (ImageView) view.findViewById(R.id.ivBack);
        ivCart = (ImageView) view.findViewById(R.id.ivCart);

        ivCart.setOnClickListener(this);
        ivBack.setOnClickListener(this);
        ivPreviousPager.setOnClickListener(this);
        ivNextPager.setOnClickListener(this);
    }

    private void setupViewPaper() {
        viewPager = (ViewPager) view.findViewById(R.id.pager);
        adapter = new ListProductAdapter(getChildFragmentManager(), 3, new ListProductFragment.OnClickProduct() {
            @Override
            public void onAdd() {
                countProductAdd++;
                tvCountProductAdd.setVisibility(View.VISIBLE);
                tvCountProductAdd.setText(countProductAdd + "");
                postStatusCart();
            }

            @Override
            public void onRemove() {
                countProductAdd--;
                if (countProductAdd == 0)
                    tvCountProductAdd.setVisibility(View.GONE);
                else
                    tvCountProductAdd.setText(countProductAdd + "");
                postStatusCart();
            }
        });
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                ((TextView) view.findViewById(R.id.tvNameOfCatalogoe)).setText(((ListProductFragment) adapter.getItem(position)).getNameOfFragment());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 0.0f);
        alphaAnimation.setDuration(0);
        alphaAnimation.setFillAfter(true);
        view.findViewById(R.id.pager).startAnimation(alphaAnimation);

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
                        alphaAnimation.setDuration(500);
                        alphaAnimation.setFillAfter(true);
                        view.findViewById(R.id.pager).startAnimation(alphaAnimation);
                    }
                });
            }
        }, 500);

        viewPager.setPageMargin(20); // TODO Convert 'px' to 'dp'
        viewPager.setPageMarginDrawable(R.color.white);
        viewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                float mStartDragX = event.getX();
                switch (event.getAction()) {
                    case MotionEvent.ACTION_MOVE:
                        if (oldX == -1) {
                            oldX = event.getX();
                            for (int position = 0; position < adapter.getCount(); position++) {
                                if (position != viewPager.getCurrentItem()) {
                                    View view = viewPager.findViewWithTag("listproduct" + position);
                                    if (view != null) {
                                        AlphaAnimation alphaAnimation = new AlphaAnimation(1f, 1f);
                                        alphaAnimation.setDuration(0);
                                        alphaAnimation.setFillAfter(true);
                                        view.startAnimation(alphaAnimation);
                                    }
                                }
                            }
                        }

                        if (mStartDragX < oldX && viewPager.getCurrentItem() < (adapter.getCount() - 1)) {
                            float move = oldX - mStartDragX;
                            float width = oldX < viewPager.getWidth() - viewPager.getWidth() / 3 ? viewPager.getWidth() : oldX;
                            Log.d("the::", "percent< = " + (move / width));

                            percent = (move / width);
                            fadeViewPager(percent);
                        } else if (mStartDragX > oldX && viewPager.getCurrentItem() > 0) {
                            float move = mStartDragX - oldX;
                            float width = (viewPager.getWidth() - oldX) < viewPager.getWidth() - viewPager.getWidth() / 3 ? viewPager.getWidth() : (viewPager.getWidth() - oldX);
                            Log.d("the::", "percent> = " + (move / width));

                            percent = (move / width);
                            fadeViewPager(percent);
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        if (percent > 0.6) {
                            if (mStartDragX < oldX) {
                                if (viewPager.getCurrentItem() < adapter.getCount()) {
                                    viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                                }
                            } else if (mStartDragX > oldX) {
                                if (viewPager.getCurrentItem() > 0) {
                                    viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
                                }
                            }
                        }

                        if (currentPage != null) {
                            AlphaAnimation alphaAnimation = new AlphaAnimation(1 - percent, 1f);
                            alphaAnimation.setDuration(100);
                            alphaAnimation.setFillAfter(true);
                            currentPage.startAnimation(alphaAnimation);
                        }
                        oldX = -1;
                        CatalogoeFragment.this.alpha = -1;
                        break;
                }

                return false;
            }
        });
    }

    public void fadeViewPager(float alpha) {
        float fromAlpha;
        float toAlpha = 1 - alpha;;

        if (this.alpha == -1) {
            this.alpha = 1;
            fromAlpha = 1;
        } else
            fromAlpha = 1 - alpha;;

        currentPage = viewPager.findViewWithTag("listproduct" + viewPager.getCurrentItem());
        AlphaAnimation alphaAnimation = new AlphaAnimation(fromAlpha, toAlpha);
        alphaAnimation.setDuration(10);
        alphaAnimation.setFillAfter(true);
        currentPage.startAnimation(alphaAnimation);
    }

    public void postStatusCart() {
        TranslateAnimation animMoveUp = new TranslateAnimation(0, 0, 0, -5);
        animMoveUp.setDuration(25);
        animMoveUp.setAnimationListener(new TranslateAnimation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                TranslateAnimation animMoveDown = new TranslateAnimation(0, 0, -5, 0);
                animMoveDown.setDuration(25);
                animMoveDown.setAnimationListener(new TranslateAnimation.AnimationListener() {

                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {

                    }
                });

                tvCountProductAdd.startAnimation(animMoveDown);
            }
        });


        tvCountProductAdd.startAnimation(animMoveUp);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivNextPager:
                if (viewPager.getCurrentItem() + 1 <= adapter.getCount())
                    viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                break;
            case R.id.ivPreviousPager:
                if (viewPager.getCurrentItem() - 1 >= 0)
                    viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
                break;
            case R.id.ivBack:
                getActivity().onBackPressed();
                break;
            case R.id.ivCart:
                Fragment fragment = new CartFragment();
                MainActivity.getInstance().getSupportFragmentManager().beginTransaction()
                        .add(R.id.container, fragment)
                        .addToBackStack(fragment.getClass().getName())
                        .commit();
                break;
        }
    }
}
