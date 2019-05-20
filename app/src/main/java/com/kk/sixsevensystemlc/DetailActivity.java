package com.kk.sixsevensystemlc;

import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
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

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    public static final String MERCHANDISE_ID = "merchandise_id";

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
        int merchandiseId = intent.getIntExtra(MERCHANDISE_ID,0);
        final String merchandiseName = intent.getStringExtra(MERCHANDISE_NAME);
        String merchandiseImageURL = intent.getStringExtra(MERCHANDISE_IMAGE_URL);
        double merchandisePrice = intent.getDoubleExtra(MERCHANDISE_PRICE,0);
        int rate = Integer.parseInt(intent.getStringExtra(MERCHANDISE_RATE));
        String merchandiseDetail = intent.getStringExtra(MERCHANDISE_DETAIL);
        //Log.d("jkcrate",rate+" ");

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        final CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout)findViewById(R.id.collapsing_toolbar);
        ImageView merchandiseImageView = (ImageView)findViewById(R.id.detail_image);
        Picasso.with(getBaseContext()).load(merchandiseImageURL).into(merchandiseImageView);

        TextView merchandiseIdText = (TextView)findViewById(R.id.detail_id);
        merchandiseIdText.setText("货号：" + merchandiseId);

        TextView merchandiseContentText = (TextView)findViewById(R.id.detail_text);
        merchandiseContentText.setText(merchandiseDetail);

        TextView merchandisePriceText = (TextView)findViewById(R.id.detail_price);
        merchandisePriceText.setText("进价：￥" + merchandisePrice);

        TextView merchandiseNameText = (TextView)findViewById(R.id.detail_name);
        merchandiseNameText.setText("名称："+merchandiseName);

        RatingBar ratingBar = (RatingBar)findViewById(R.id.detail_rate);
        ratingBar.setNumStars(rate);

        Button addCartBtn = (Button)findViewById(R.id.add_to_cart_btn);
        addCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //添加到清单的逻辑
            }
        });

        Button addOrderBtn = (Button)findViewById(R.id.add_to_order_btn);
        addOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //直接购买，即添加到订单的逻辑，需要打开提示窗口选择数量，然后确定。
            }
        });

        AppBarLayout appBarLayout = (AppBarLayout)findViewById(R.id.app_bar);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
