package com.kk.sixsevensystemlc.sell;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kk.sixsevensystemlc.R;

public class SellFragment extends Fragment {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private SellFragmentPageAdapter sellFragmentPageAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sell_fragment, container, false);

        viewPager=(ViewPager) view.findViewById(R.id.sell_view_pager);
        sellFragmentPageAdapter = new SellFragmentPageAdapter(getChildFragmentManager());
        viewPager.setAdapter(sellFragmentPageAdapter);
        NestedScrollView scrollView = (NestedScrollView)view.findViewById (R.id.nested_scrollview);
        scrollView.setFillViewport (true);//修正viewpager在nsv中显示不正常的问题
        tabLayout= (TabLayout) view.findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);

        return view;
    }
}
