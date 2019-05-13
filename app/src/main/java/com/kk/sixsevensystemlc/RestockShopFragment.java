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
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;

import java.util.ArrayList;
import java.util.List;

import static android.icu.util.HebrewCalendar.AV;
import static com.avos.avoscloud.Messages.CommandType.error;

public class RestockShopFragment extends Fragment {

    private View view;

    private MerchandiseAdapter merchandiseAdapter;
    private List<AVObject> merchandiseList = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.restock_shop_fragment, container, false);
        initRecyclerview();
        initMerchandise();
        return view;
    }

    private void initRecyclerview(){
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycle_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getActivity());
        recyclerView.setLayoutManager(layoutManager);
        merchandiseAdapter = new MerchandiseAdapter(merchandiseList,getContext());
        recyclerView.setAdapter(merchandiseAdapter);
    }

    private void initMerchandise(){
        merchandiseList.clear();
        AVQuery<AVObject> avQuery = new AVQuery<>("Merchandise");
        //avQuery.orderByDescending("createdAt");
        avQuery.include("name");
        avQuery.include("price");
        avQuery.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (e == null) {
                    merchandiseList.addAll(list);
                    Log.d("jkclist",merchandiseList.toString());
                    merchandiseAdapter.notifyDataSetChanged();
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