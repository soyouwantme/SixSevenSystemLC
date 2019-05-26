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
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.SaveCallback;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.Date;

public class ConsumerDetailActivity extends AppCompatActivity implements ConsumerDialogFragment.InListener {

    String merchandiseId;
    AVObject merchandise;
    String merchandiseName;


    public static final String OBJECT_ID = "object_id";
    public static final String MERCHANDISE_NAME = "merchandise_name";
    public static final String STOCK_IMAGE_URL = "merchandise_image_url";
    public static final String MERCHANDISE_PRICE = "merchandise_price";
    public static final String MERCHANDISE_RATE = "merchandise_rate";
    public static final String MERCHANDISE_DETAIL = "merchandise_detail";
    public static final String STOCK_ID = "stock_id";
    public static final String OBJECTID_ID = "object_id";
    public static final String STOCK_NAME = "stock_name";
    public static final String STOCK_LEFT = "stock_left";



    String stockId;

    int left;
    String objectId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.consumer_detail_activity);

        Intent intent = getIntent();
        merchandiseId = intent.getStringExtra(OBJECT_ID);
        merchandiseName = intent.getStringExtra(MERCHANDISE_NAME);
        String merchandiseImageURL = intent.getStringExtra(STOCK_IMAGE_URL);
        String merchandisePrice = intent.getStringExtra(MERCHANDISE_PRICE);
        int rate = Integer.parseInt(intent.getStringExtra(MERCHANDISE_RATE));
        String merchandiseDetail = intent.getStringExtra(MERCHANDISE_DETAIL);
        //Log.d("jkcrate",rate+" ");
        objectId = intent.getStringExtra(OBJECTID_ID);//订单唯一编号
        stockId = intent.getStringExtra(STOCK_ID);//对应的商品唯一编号
        merchandiseName = intent.getStringExtra(STOCK_NAME);
        left = intent.getIntExtra(STOCK_LEFT,0);

        //获取外键
        merchandise = AVObject.createWithoutData("Merchandise",merchandiseId);


        Toolbar toolbar = findViewById(R.id.customer_toolbar);
        final CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout)findViewById(R.id.customer_collapsing_toolbar);
        ImageView merchandiseImageView = findViewById(R.id.customer_detail_image);
        Picasso.with(getBaseContext()).load(merchandiseImageURL).into(merchandiseImageView);

        TextView merchandiseIdText = findViewById(R.id.customer_detail_id);
        merchandiseIdText.setText("货号：" + merchandiseId);

        TextView merchandiseContentText = findViewById(R.id.customer_detail_text);
        merchandiseContentText.setText(merchandiseDetail);

        TextView merchandisePriceText = findViewById(R.id.customer_detail_price);
        merchandisePriceText.setText("价格：￥" + merchandisePrice);

        TextView merchandiseNameText = findViewById(R.id.customer_detail_name);
        merchandiseNameText.setText("名称："+merchandiseName);

        RatingBar ratingBar = findViewById(R.id.customer_detail_rate);
        ratingBar.setNumStars(rate);

        Button addOrderBtn = findViewById(R.id.customer_buy);
        addOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //直接购买，即添加到订单的逻辑，需要打开提示窗口选择数量，然后确定。
                showcustomerstockDialog(v);

            }
        });

        AppBarLayout appBarLayout = findViewById(R.id.customer_app_bar);
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

    public void showcustomerstockDialog(View view)
    {
        ConsumerDialogFragment inStockDialogFragment = new ConsumerDialogFragment();
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
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onConsumerComplete(final int num) {

        if(num<=left) {
            if(num<left) {
                //更新库存
                AVObject todo = AVObject.createWithoutData("Stock", objectId);
                left = left - num;
                todo.put("left", left);
                todo.saveInBackground(null, new SaveCallback() {
                    @Override
                    public void done(AVException e) {
                        if (e == null) {
                            Log.e("jkcnum", "succeed");
                        } else {
                            Log.e("jkcnum", "fail" + e.toString());
                        }
                    }
                });
            }
            if(num==left)
            {
                AVObject todo = AVObject.createWithoutData("Stock", objectId);
                todo.deleteInBackground();
            }
            //添加销售表
            AVObject merchandise = AVObject.createWithoutData("Merchandise", stockId);
            AVObject record = new AVObject("Record");
            record.put("recordNum", num);
            //获取当前时间
            Date time = Calendar.getInstance().getTime();
            record.put("sellDate", time);
            record.put("merchandiseId", merchandise);//外键
            record.saveInBackground(null, new SaveCallback() {
                @Override
                public void done(AVException e) {
                    if (e == null) {
                        Log.e("jkcrecord", "succeed");
                    } else {
                        Log.e("jkcrecord", "fail" + e.toString());
                    }
                }
            });
            startActivity(new Intent(ConsumerDetailActivity.this, WebPayActivity.class));

            //Toast.makeText(this,"购买成功",Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this,"购买失败，库存仅剩"+left,Toast.LENGTH_SHORT).show();
        }
    }
}
