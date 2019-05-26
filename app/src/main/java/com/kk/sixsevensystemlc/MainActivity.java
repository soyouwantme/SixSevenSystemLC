package com.kk.sixsevensystemlc;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;


import com.avos.avoscloud.AVUser;
import com.kk.sixsevensystemlc.restock.RestockFragment;
import com.kk.sixsevensystemlc.sell.SellFragment;
import com.kk.sixsevensystemlc.stock.StockFragment;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.BottomBarTab;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity implements View.OnClickListener {

    private BottomBar bottomBar;
    private BottomBarTab sell;

    //声明ViewPager
    private ViewPager mViewPager;
    //适配器
    private FragmentPagerAdapter mAdapter;
    //装载Fragment的集合
    private List<Fragment> mFragments;
    //三个Tab对应的布局
    private LinearLayout mTabRestock;
    private LinearLayout mTabSell;
    private LinearLayout mTabStock;
    //四个Tab对应的ImageButton
    private ImageButton mImgRestock;
    private ImageButton mImgSell;
    private ImageButton mImgStock;
    private ImageButton mImgLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setContentView(R.layout.activity_main);
        initViews();//初始化控件
        initEvents();//初始化事件
        initDatas();//初始化数据
        /** 测试 SDK 是否正常工作的代码
         AVObject testObject = new AVObject("TestObject");
         testObject.put("words","Hello World!");
         testObject.saveInBackground(new SaveCallback() {
        @Override public void done(AVException e) {
        if(e == null){
        Log.d("saved","success!");
        }
        }
        });*/
    }



    private void initDatas() {
        mFragments = new ArrayList<>();
        //将3个Fragment加入集合中
        mFragments.add(new RestockFragment());
        mFragments.add(new SellFragment());
        mFragments.add(new StockFragment());

        //初始化适配器
        mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {//从集合中获取对应位置的Fragment
                return mFragments.get(position);
            }

            @Override
            public int getCount() {//获取集合中Fragment的总数
                return mFragments.size();
            }

        };
        //设置ViewPager的适配器
        mViewPager.setAdapter(mAdapter);
        //设置ViewPager的切换监听
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            //页面滚动事件
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            //页面选中事件
            @Override
            public void onPageSelected(int position) {
                //设置position对应的集合中的Fragment
                mViewPager.setCurrentItem(position);
                resetImgs();
                selectTab(position);
            }

            @Override
            //页面滚动状态改变事件
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initEvents() {
        //设置四个Tab的点击事件
        mTabRestock.setOnClickListener(this);
        mTabSell.setOnClickListener(this);
        mTabStock.setOnClickListener(this);
        mImgLogout.setOnClickListener(this);

    }

    //初始化控件
    private void initViews() {
        mViewPager = (ViewPager) findViewById(R.id.id_viewpager);

        mTabRestock = (LinearLayout) findViewById(R.id.id_tab_restock);
        mTabSell = (LinearLayout) findViewById(R.id.id_tab_sell);
        mTabStock = (LinearLayout) findViewById(R.id.id_tab_stock);

        mImgRestock = (ImageButton) findViewById(R.id.id_tab_restock_img);
        mImgSell = (ImageButton) findViewById(R.id.id_tab_sell_img);
        mImgStock = (ImageButton) findViewById(R.id.id_tab_stock_img);
        mImgLogout = findViewById(R.id.log_out);

    }

    @Override
    public void onClick(View v) {
        //先将四个ImageButton置为灰色
        resetImgs();

        //根据点击的Tab切换不同的页面及设置对应的ImageButton为绿色
        switch (v.getId()) {
            case R.id.id_tab_restock:
                selectTab(0);
                break;
            case R.id.id_tab_sell:
                selectTab(1);
                break;
            case R.id.id_tab_stock:
                selectTab(2);
                break;
            case R.id.log_out:
                mImgLogout.setImageResource(R.drawable.tab_logout_pressed);
                AVUser.logOut();// 清除缓存用户对象
                SharedPreferences sharedPreferences =
                        getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("email", null);
                editor.putString("role",null);
                editor.commit();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                MainActivity.this.finish();
                break;

        }
    }

    private void selectTab(int i) {
        //根据点击的Tab设置对应的ImageButton为绿色
        switch (i) {
            case 0:
                mImgRestock.setImageResource(R.drawable.tab_restock_pressed);
                break;
            case 1:
                mImgSell.setImageResource(R.drawable.tab_sell_pressed);
                break;
            case 2:
                mImgStock.setImageResource(R.drawable.tab_stock_pressed);
                break;
        }
        //设置当前点击的Tab所对应的页面
        mViewPager.setCurrentItem(i);
    }

    //将四个ImageButton设置为灰色
    private void resetImgs() {
        mImgRestock.setImageResource(R.drawable.tab_restock_normal);
        mImgSell.setImageResource(R.drawable.tab_sell_normal);
        mImgStock.setImageResource(R.drawable.tab_stock_normal);
    }
}


