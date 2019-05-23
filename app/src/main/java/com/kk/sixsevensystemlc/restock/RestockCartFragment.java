package com.kk.sixsevensystemlc.restock;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.kk.sixsevensystemlc.restock.CartAdapter;
import com.kk.sixsevensystemlc.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class RestockCartFragment extends Fragment {

    View view;
    private Button checkBtn;

    private CartAdapter cartAdapter;
    private List<AVObject> cartList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.restock_cart_fragment, container, false);
        checkBtn = (Button)view.findViewById(R.id.settlement);
        checkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("jkcpressedbtn","ipressed");
            }
        });
        initRecyclerview();
        initCart();
        return view;
    }

    private void initRecyclerview() {
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycle_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getActivity());
        recyclerView.setLayoutManager(layoutManager);
        cartAdapter = new CartAdapter(cartList, getContext());
        recyclerView.setAdapter(cartAdapter);
    }

    private void initCart() {
        cartList.clear();
        AVQuery<AVObject> avQuery = new AVQuery<>("Cart");
        avQuery.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (e == null) {
                    cartList.addAll(list);
                    //Log.d("jkccartlist", cartList.toString());
                    cartAdapter.notifyDataSetChanged();
                } else {
                    e.printStackTrace();
                }
            }
        });
        /*
        AVObject merchandiseXR = AVObject.createWithoutData("Merchandise", "5cde7bb7a5ef5700083bdda1");
        AVObject order1 = new AVObject("Order");
        order1.put("name", "RX1");
        //Log.d("order","orderrunned");
        order1.put("dependent", merchandiseXR);*/
    }

}