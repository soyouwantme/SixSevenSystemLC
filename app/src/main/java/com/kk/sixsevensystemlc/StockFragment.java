package com.kk.sixsevensystemlc;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class StockFragment extends Fragment {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private StockFragmentPageAdapter stockFragmentPageAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.stock_fragment, container, false);

        viewPager=(ViewPager) view.findViewById(R.id.view_pager);
        stockFragmentPageAdapter = new StockFragmentPageAdapter(getChildFragmentManager());
        viewPager.setAdapter(stockFragmentPageAdapter);

        tabLayout= (TabLayout) view.findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);
        return view;
    }
}
