package com.kk.sixsevensystemlc;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import java.util.HashMap;

public class RestockFragmentPageAdapter extends FragmentPagerAdapter {

    private String[] mTitles = new String[]{"进货清单", "商品列表", "我的订单"};

    private HashMap<Integer, Fragment> mFragmentHashMap = new HashMap<>();

    public RestockFragmentPageAdapter(FragmentManager fm) {
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
                    fragment = new RestockCartFragment();
                    Log.i("fragment", "fragment1");
                    break;
                case 1:
                    fragment = new RestockShopFragment();
                    Log.i("fragment", "fragment2");
                    break;
                case 2:
                    fragment = new RestockOrderFragment();
                    Log.i("fragment", "fragment3");
                    break;
            }
            mFragmentHashMap.put(pos, fragment);
        }
        return fragment;
    }
    //ViewPager与TabLayout绑定后，这里获取到PageTitle就是Tab的Text
    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles[position];
    }

}
