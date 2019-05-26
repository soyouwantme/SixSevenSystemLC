package com.kk.sixsevensystemlc;

import android.content.Intent;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
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

import com.avos.avoscloud.AVObject;
import com.kk.sixsevensystemlc.R;
import com.kk.sixsevensystemlc.restock.InCartDialogFragment;
import com.kk.sixsevensystemlc.stock.InStockDialogFragment;
import com.squareup.picasso.Picasso;

public class SupplierDetail extends AppCompatActivity {

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
        setContentView(R.layout.supplier_detail_activity);

        Intent intent = getIntent();
        merchandiseId = intent.getStringExtra(OBJECT_ID);
        merchandiseName = intent.getStringExtra(MERCHANDISE_NAME);
        String merchandiseImageURL = intent.getStringExtra(MERCHANDISE_IMAGE_URL);
        double merchandisePrice = intent.getDoubleExtra(MERCHANDISE_PRICE,0);
        String rate = intent.getStringExtra(MERCHANDISE_RATE);
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

        TextView merchandiseNameText = findViewById(R.id.detail_name);
        merchandiseNameText.setText("名称：" + merchandiseName);

        TextView merchandisePriceText = findViewById(R.id.detail_price);
        merchandisePriceText.setText("价格" + "：￥" + merchandisePrice);

        TextView merchandiseRateText = findViewById(R.id.detail_rate);
        merchandiseRateText.setText("评级："+rate);

        TextView merchandiseContentText = findViewById(R.id.detail_text);
        merchandiseContentText.setText(merchandiseDetail);


        /*AppBarLayout appBarLayout = findViewById(R.id.app_bar);
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
        });*/
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
