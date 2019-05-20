package com.kk.sixsevensystemlc;

import android.content.Context;
import android.net.Uri;
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

public class StockMessageFragment extends Fragment {

    private View view;

    private MessageAdapter messageAdapter;
    private List<AVObject> messageList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.stock_message_fragment, container, false);
        initRecyclerview();
        initMessage();
        return view;
    }

    private void initRecyclerview(){
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycle_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getActivity());
        recyclerView.setLayoutManager(layoutManager);
        messageAdapter = new MessageAdapter(messageList,getContext());
        recyclerView.setAdapter(messageAdapter);
    }

    private void initMessage(){
        messageList.clear();
        AVQuery<AVObject> avQuery = new AVQuery<>("message");
        //avQuery.orderByDescending("createdAt");
        avQuery.include("name");
        avQuery.include("state");
        avQuery.include("detail");
        avQuery.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (e == null) {
                    messageList.addAll(list);
                    Log.d("jkcmessagelist",messageList.toString());
                    messageAdapter.notifyDataSetChanged();
                } else {
                    e.printStackTrace();
                }
            }
        });
    }
}
