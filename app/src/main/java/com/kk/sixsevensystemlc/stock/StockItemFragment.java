package com.kk.sixsevensystemlc.stock;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
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
import com.kk.sixsevensystemlc.R;

import java.util.ArrayList;
import java.util.List;


public class StockItemFragment extends Fragment {
    private View view;

    int begin=0;
    private StockAdapter stockAdapter;
    private List<AVObject> stockList = new ArrayList<>();
    private SwipeRefreshLayout swipeRefresh;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int begin=0;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.stock_item_fragment, container, false);
        swipeRefresh = (SwipeRefreshLayout)view.findViewById(R.id.swipe_refresh);
        swipeRefresh.setColorSchemeColors(getResources().getColor(R.color.primary));
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshItem();
            }
        });
        initRecyclerview();
        initStock();
        return view;
    }

    private void refreshItem(){
        stockAdapter.notifyDataSetChanged();
        swipeRefresh.setRefreshing(false);

    }

    private void initRecyclerview(){
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.stock_recycle_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getActivity());
        recyclerView.setLayoutManager(layoutManager);
        stockAdapter = new StockAdapter(stockList,getContext());
        recyclerView.setAdapter(stockAdapter);
    }

    private void initStock(){
        stockList.clear();
        AVQuery<AVObject> avQuery = new AVQuery<>("Stock");
        avQuery.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (e == null) {
                    stockList.addAll(list);
                    stockAdapter.notifyDataSetChanged();
                } else {
                    e.printStackTrace();
                }
                begin++;
            }
        });
    }

    public void onResume()
    {
        super.onResume();
        if(begin>=1) {
            //Log.e("stock","123");
            stockAdapter.ClearStockList();
            //Log.e("stock",""+stockAdapter.getItemCount());
            initStock();
            //Log.e("stock","4:"+stockList.size());
            stockAdapter.SetStockList(stockList);
        }
    }

}
