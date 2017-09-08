package com.thebv.foodmarket;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.SharedElementCallback;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

public class MainFragment extends Fragment {

    private View view;

    public ImageView ivNavigationIcon;
    public ImageView ivCart;
    public TextView tvTitle;
    public ImageView ivSearch;
    private ViewPager viewPager;
    private ShopAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_main, container, false);
        init();

        Log.d("the::", "MainFragment onCreateView");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getActivity().postponeEnterTransition();
        }

        setupViewPaper();

        return view;
    }


    private void init() {
        ivNavigationIcon = (ImageView) view.findViewById(R.id.ivNavigationIcon);
        ivCart = (ImageView) view.findViewById(R.id.ivCart);
        tvTitle = (TextView) view.findViewById(R.id.tvTitle);
        ivSearch = (ImageView) view.findViewById(R.id.ivSearch);

        ivNavigationIcon.setTransitionName("ivNavigationIcon");
        ivCart.setTransitionName("ivCart");
        tvTitle.setTransitionName("tvTitle");

        view.findViewById(R.id.tvShowOnlyFavorites).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void setupViewPaper() {
        viewPager = (ViewPager) view.findViewById(R.id.pager);
        adapter = new ShopAdapter
                (getChildFragmentManager(), 3, new ShopFragment.OnListenerPageClick() {
                    @Override
                    public void onClick(int position) {
                        String tag = "content" + position;
                        Fragment fragment = new CatalogoeFragment();
                        fragment.setSharedElementEnterTransition(new ShopFragment.DetailsTransition());
                        fragment.setEnterTransition(new ShopFragment.DetailsTransition());
                        setSharedElementReturnTransition(new ShopFragment.DetailsTransition());

                        Log.d("the::", "MainFragment.getViewPager().findViewWithTag(tag) = " + viewPager.findViewWithTag(tag));
                        Bundle bundle = new Bundle();
                        bundle.putString("tag", tag);
                        fragment.setArguments(bundle);

                        MainActivity.getInstance().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.container, fragment)
                                .addToBackStack(fragment.getClass().getName())
                                .addSharedElement(viewPager.findViewWithTag(tag), tag)
                                .addSharedElement(ivNavigationIcon, "ivNavigationIcon")
                                .addSharedElement(ivCart, "ivCart")
                                .addSharedElement(ivSearch, "ivSearch")
                                .addSharedElement(tvTitle, "tvTitle")
                                .commit();
                    }
                });
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                ((TextView) view.findViewById(R.id.tvStatusPage)).setText((position + 1) + " of 3");
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        int defaultGap = (int) getResources().getDimension(R.dimen._40sdp);
        viewPager.setClipToPadding(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            viewPager.setClipToOutline(false);
        }
        viewPager.setPadding(defaultGap,
                (int) getResources().getDimension(R.dimen._10sdp),
                defaultGap,
                (int) getResources().getDimension(R.dimen._20sdp));
        viewPager.setPageMargin(defaultGap / 2);
    }
}
