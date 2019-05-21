package com.kk.sixsevensystemlc.restock;

import android.os.Bundle;
import android.os.StrictMode;
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
import com.avos.avoscloud.GetCallback;
import com.kk.sixsevensystemlc.R;

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
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
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
        AVQuery<AVObject> avQueryOrder = new AVQuery<>("Order");
        avQueryOrder.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (e == null) {
                    orderList.addAll(list);
                    Log.d("jkctime","times");
                    orderAdapter.notifyDataSetChanged();
                } else {
                    e.printStackTrace();
                }
            }
        });
    }
}
