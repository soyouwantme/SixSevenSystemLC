package com.kk.sixsevensystemlc;

import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.SaveCallback;
import com.bumptech.glide.Glide;
import com.kk.sixsevensystemlc.R;
import com.kk.sixsevensystemlc.restock.InCartDialogFragment;
import com.kk.sixsevensystemlc.stock.InStockDialogFragment;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DetailActivity extends AppCompatActivity implements InStockDialogFragment.InListener, InCartDialogFragment.InCartListener {

    String merchandiseId;
    AVObject merchandise;
    String merchandiseName;


    public static final String OBJECT_ID = "object_id";
    public static final String MERCHANDISE_NAME = "merchandise_name";
    public static final String MERCHANDISE_IMAGE_URL = "merchandise_image_url";
    public static final String MERCHANDISE_PRICE = "merchandise_price";
    public static final String MERCHANDISE_RATE = "merchandise_rate";
    public static final String MERCHANDISE_DETAIL = "merchandise_detail";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_activity);

        Intent intent = getIntent();
        merchandiseId = intent.getStringExtra(OBJECT_ID);
        merchandiseName = intent.getStringExtra(MERCHANDISE_NAME);
        String merchandiseImageURL = intent.getStringExtra(MERCHANDISE_IMAGE_URL);
        double merchandisePrice = intent.getDoubleExtra(MERCHANDISE_PRICE,0);
        int rate = Integer.parseInt(intent.getStringExtra(MERCHANDISE_RATE));
        String merchandiseDetail = intent.getStringExtra(MERCHANDISE_DETAIL);
        //Log.d("jkcrate",rate+" ");

        //获取外键
        merchandise = AVObject.createWithoutData("Merchandise",merchandiseId);


        Toolbar toolbar = findViewById(R.id.toolbar);
        final CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout)findViewById(R.id.collapsing_toolbar);
        ImageView merchandiseImageView = findViewById(R.id.detail_image);
        Picasso.with(getBaseContext()).load(merchandiseImageURL).into(merchandiseImageView);

        TextView merchandiseIdText = findViewById(R.id.detail_id);
        merchandiseIdText.setText("货号：" + merchandiseId);

        TextView merchandiseContentText = findViewById(R.id.detail_text);
        merchandiseContentText.setText(merchandiseDetail);

        TextView merchandisePriceText = findViewById(R.id.detail_price);
        merchandisePriceText.setText("进价：￥" + merchandisePrice);

        TextView merchandiseNameText = findViewById(R.id.detail_name);
        merchandiseNameText.setText("名称："+merchandiseName);

        RatingBar ratingBar = findViewById(R.id.detail_rate);
        ratingBar.setNumStars(rate);

        final Button addCartBtn = findViewById(R.id.add_to_cart_btn);
        addCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //添加到清单的逻辑
                showInCartDialog(v);
            }
        });

        Button addOrderBtn = findViewById(R.id.add_to_order_btn);
        addOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //直接购买，即添加到订单的逻辑，需要打开提示窗口选择数量，然后确定。
                showInstockDialog(v);

            }
        });

        AppBarLayout appBarLayout = findViewById(R.id.app_bar);
        appBarLayout.addOnOffsetChangedListener(new AppBarStateChangeListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state) {
                Log.d("jkcSTATE",state.name());
                if(state == State.COLLAPSED){
                    //折叠
                    collapsingToolbar.setTitle("商品详情");
                }else{
                    //非折叠
                    collapsingToolbar.setTitle(" ");
                    //setTheme(R.style.ActivityTheme2);
                }
            }
        });
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

    }

    public void showInCartDialog(View view)
    {
        InCartDialogFragment inCartDialogFragment = new InCartDialogFragment();
        inCartDialogFragment.show(getSupportFragmentManager(),"tag");
    }

    public void showInstockDialog(View view)
    {
        InStockDialogFragment inStockDialogFragment = new InStockDialogFragment();
        inStockDialogFragment.show(getSupportFragmentManager(),"tag");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onInComplete(final int num) {

        //获取对象来修改库存
        AVQuery<AVObject> query = new AVQuery<>("Stock");
        //查找库存表
        query.whereEqualTo("merchandiseId",merchandise);
        query.getFirstInBackground(new GetCallback<AVObject>() {
            @Override
            public void done(AVObject stock, AVException avException) {
                //Log.e("jkc走库存","succeed");
                if(stock == null){
                    //Log.e("jkc库存无","succeed");
                    AVObject newStock = new AVObject("Stock");
                    newStock.put("left",num);
                    newStock.put("merchandiseId",merchandise);
                    newStock.saveInBackground();
                }else {
                    //更新库存,取得当前库存量
                    //Log.e("jkc库存有","succeed");
                    int left = Integer.parseInt(stock.get("left") + "");
                    AVObject todo = AVObject.createWithoutData("Stock", stock.getObjectId());
                    //库存修改（增加）
                    left = left + num;
                    todo.put("left", left);
                    todo.put("merchandiseId",merchandise);
                    todo.saveInBackground(null, new SaveCallback() {
                        @Override
                        public void done(AVException e) {
                            if (e == null) {
                                //Log.e("jkc库存更新", "succeed");
                            } else {
                                //Log.e("jkcn库存更新", "fail" + e.toString());
                            }
                        }
                    });
                }


                //添加订单表
                AVObject order = new AVObject("Order");
                order.put("orderNum",num);
                order.put("merchandiseId",merchandise);//外键
                //获取当前时间
                Date time = Calendar.getInstance().getTime();
                order.put("orderDate",time);
                order.saveInBackground(null, new SaveCallback() {
                    @Override
                    public void done(AVException e) {
                        if (e==null) {
                            //Log.e("jkc订单更新", "succeed");
                        }else {
                            //Log.e("jkc订单更新", "fail"+e.toString());
                        }
                    }
                });


            }
        });


        Snackbar.make(getWindow().getDecorView(),"添加成功",Snackbar.LENGTH_SHORT).show();

    }

    @Override
    public void onInCartComplete(final int num) {/*
        AVQuery<AVObject> query = new AVQuery<>("Cart");
        query.whereEqualTo("merchandiseId",merchandise);
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> avObjects, AVException avException) {
                if (avObjects.size() == 0){*/
                    AVObject cart = new AVObject("Cart");
                    cart.put("merchandiseId",merchandise);
                    cart.put("cartNum",num);
                    cart.saveInBackground();/*
                }else {
                    AVObject cart = avObjects.get(0);
                    int cartNum = Integer.parseInt(cart.get("cartNum")+"");
                    cartNum = cartNum + num;
                    cart.put("cartNum",cartNum);
                    cart.saveInBackground();
                }
            }
        });*/


    }
}
