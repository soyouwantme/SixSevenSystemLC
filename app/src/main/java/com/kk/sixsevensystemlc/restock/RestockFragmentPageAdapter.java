package com.kk.sixsevensystemlc.restock;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.view.ViewGroup;

import java.util.HashMap;

public class RestockFragmentPageAdapter extends FragmentPagerAdapter {

    private String[] mTitles = new String[]{"进货清单", "商品列表", "我的订单"};

    private HashMap<Integer, Fragment> mFragmentHashMap = new HashMap<>();

    public RestockFragmentPageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        Fragment f = createFragment(position);
        Log.d("jkcfra","getItem"+ position);
        return f;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        Log.d("jkcfra","instant"+ position);
        //destroyItem();
        return super.instantiateItem(container, position);
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        Log.d("jkcfra","destroy"+ position);
        super.destroyItem(container, position, object);
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        Log.d("jkcfra",object.toString());
        Log.d("jkcfra",POSITION_NONE+"");
        return POSITION_NONE;
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
            Log.d("jkcfra",pos+"");
        }
        return fragment;
    }
    //ViewPager与TabLayout绑定后，这里获取到PageTitle就是Tab的Text
    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles[position];
    }

}
