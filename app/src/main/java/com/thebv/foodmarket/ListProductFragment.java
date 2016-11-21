package com.thebv.foodmarket;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ListProductFragment extends Fragment {

    private OnClickProduct onClickProduct;
    private int position;
    public String nameOfFragment = "";
    private View view;
    private RecyclerView rvListProduct;

    public ListProductFragment(String nameOfFragment, int position) {
        this.nameOfFragment = nameOfFragment;
        this.position = position;
    }

    public ListProductFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_list_product, container, false);
        view.setTag("listproduct" + position);

        init();
        fillData();

        return view;
    }

    private void init() {

    }


    private void fillData() {
        rvListProduct = (RecyclerView) view.findViewById(R.id.rvListProduct);

        ProductAdapter mAdapter = new ProductAdapter(getActivity(), 10);
        mAdapter.setOnClickProduct(new OnClickProduct() {
            @Override
            public void onAdd() {
                onClickProduct.onAdd();
            }

            @Override
            public void onRemove() {
                onClickProduct.onRemove();
            }
        });
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        rvListProduct.setLayoutManager(mLayoutManager);
        rvListProduct.setItemAnimator(new DefaultItemAnimator());
        rvListProduct.setAdapter(mAdapter);
        rvListProduct.addItemDecoration(new ProductAdapter.SimpleDividerItemDecoration(getActivity()));
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void setNameOfFragment(String nameOfFragment) {
        this.nameOfFragment = nameOfFragment;
    }

    public String getNameOfFragment() {
        return nameOfFragment;
    }

    public void setOnClickProduct(OnClickProduct onClickProduct) {
        this.onClickProduct = onClickProduct;
    }


    public interface OnClickProduct {
        void onAdd();

        void onRemove();
    }
}
