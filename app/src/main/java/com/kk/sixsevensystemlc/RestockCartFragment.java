package com.kk.sixsevensystemlc;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;

import java.util.ArrayList;
import java.util.List;


public class RestockCartFragment extends Fragment {

    View view;

    private CartAdapter cartAdapter;
    private List<AVObject> cartList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.restock_cart_fragment, container, false);
        initRecyclerview();
        initCart();
        return view;
    }

    private void initRecyclerview(){
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycle_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getActivity());
        recyclerView.setLayoutManager(layoutManager);
        cartAdapter = new CartAdapter(cartList,getContext());
        recyclerView.setAdapter(cartAdapter);
    }

    private void initCart(){
        cartList.clear();
        AVQuery<AVObject> avQuery = new AVQuery<>("Cart");
        //avQuery.orderByDescending("createdAt");
        avQuery.include("name");
        avQuery.include("price");
        avQuery.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (e == null) {
                    cartList.addAll(list);
                    Log.d("jkccartlist",cartList.toString());
                    cartAdapter.notifyDataSetChanged();
                } else {
                    e.printStackTrace();
                }
            }
        });/**
         for(int i=0;i<200;i++){
         Merchandise apple=new Merchandise("Apple",R.drawable.apple_pic,100.0);
         merchandiseList.add(apple);
         }*/
    }

}