package com.kk.sixsevensystemlc.sell;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.kk.sixsevensystemlc.R;

import java.util.ArrayList;
import java.util.List;


public class SellRecordFragment extends Fragment {

    private View view;

    public RecordAdapter recordAdapter;
    private List<AVObject> recordList = new ArrayList<>();
    private SwipeRefreshLayout swipeRefresh;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.sell_record_fragment, container, false);
        swipeRefresh = (SwipeRefreshLayout)view.findViewById(R.id.swipe_refresh);
        swipeRefresh.setColorSchemeColors(getResources().getColor(R.color.primary));
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshItem();
            }
        });
        initRecyclerview();
        initRecord();
        return view;


    }

    public void refreshItem(){
        recordAdapter.notifyDataSetChanged();
        swipeRefresh.setRefreshing(false);

    }
    private void initRecyclerview(){
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.record_recycle_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recordAdapter = new RecordAdapter(recordList,getContext());
        recyclerView.setAdapter(recordAdapter);
    }

    private void initRecord(){
        recordList.clear();
        AVQuery<AVObject> avQuery = new AVQuery<>("Record");
        //avQuery.orderByDescending("createdAt");
        avQuery.include("date");
        avQuery.include("name");
        avQuery.include("num");
        avQuery.include("sum");
        avQuery.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (e == null) {
                    recordList.addAll(list);
                    Log.d("jkcrecordlist",recordList.toString());
                    recordAdapter.notifyDataSetChanged();
                } else {
                    e.printStackTrace();
                }
            }
        });
    }
}

