package com.kk.sixsevensystemlc;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import java.util.HashMap;

public class StockFragmentPageAdapter extends FragmentPagerAdapter {

    private String[] mTitles = new String[]{"消息", "库存信息"};

    private HashMap<Integer, Fragment> mFragmentHashMap = new HashMap<>();

    public StockFragmentPageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return createFragment(position);
    }

    @Override
    public int getCount() {
        return mTitles.length;
    }

    private Fragment createFragment(int pos) {
        Fragment fragment = mFragmentHashMap.get(pos);

        if (fragment == null) {
            switch (pos) {
                case 0:
                    fragment = new StockMessageFragment();
                    Log.i("fragment", "fragment1");
                    break;
                case 1:
                    fragment = new StockItemFragment();
                    Log.i("fragment", "fragment2");
                    break;
                default:
                    break;
            }
            mFragmentHashMap.put(pos, fragment);
        }
        return fragment;
    }
    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles[position];
    }

}