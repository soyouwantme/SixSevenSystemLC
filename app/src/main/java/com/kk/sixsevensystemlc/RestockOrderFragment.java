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

public class RestockOrderFragment extends Fragment {

    private View view;

    private OrderAdapter orderAdapter;
    private List<AVObject> orderList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.restock_order_fragment, container, false);
        initRecyclerview();
        initOrder();
        return view;
    }

    private void initRecyclerview(){
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycle_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getActivity());
        recyclerView.setLayoutManager(layoutManager);
        orderAdapter = new OrderAdapter(orderList,getContext());
        recyclerView.setAdapter(orderAdapter);
    }

    private void initOrder(){
        orderList.clear();
        AVQuery<AVObject> avQuery = new AVQuery<>("Order");
        //avQuery.orderByDescending("createdAt");
        avQuery.include("name");
        avQuery.include("state");
        avQuery.include("price");
        avQuery.include("num");
        avQuery.include("summation");
        avQuery.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (e == null) {
                    orderList.addAll(list);
                    Log.d("jkclist",orderList.toString());
                    orderAdapter.notifyDataSetChanged();
                } else {
                    e.printStackTrace();
                }
            }
        });
    }
}
