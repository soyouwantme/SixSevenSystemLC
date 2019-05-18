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

public class SellFragment extends Fragment {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private SellFragmentPageAdapter sellFragmentPageAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sell_fragment, container, false);

        viewPager=(ViewPager) view.findViewById(R.id.view_pager);
        sellFragmentPageAdapter = new SellFragmentPageAdapter(getChildFragmentManager());
        viewPager.setAdapter(sellFragmentPageAdapter);

        tabLayout= (TabLayout) view.findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);

        return view;
    }
}
