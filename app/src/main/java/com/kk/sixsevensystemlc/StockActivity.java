package com.kk.sixsevensystemlc;

import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.kk.sixsevensystemlc.R;
import com.kk.sixsevensystemlc.restock.OrderAdapter;
import com.kk.sixsevensystemlc.sell.RecordAdapter;
import com.kk.sixsevensystemlc.stock.OutStockDialogFragment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class StockActivity extends AppCompatActivity {

    private RecordAdapter stockAdapter;
    private List<AVObject> recordList = new ArrayList<>();

    String stockId;

    public static final String STOCK_ID = "stock_id";
    public static final String STOCK_NAME = "stock_name";
    public static final String STOCK_LEFT = "stock_left";
    public static final String STOCK_IMAGE_URL = "stock_image_url";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stock_activity);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        stockId = intent.getStringExtra(STOCK_ID);
        String merchandiseName = intent.getStringExtra(STOCK_NAME);
        String merchandiseImageURL = intent.getStringExtra(STOCK_IMAGE_URL);
        int left = intent.getIntExtra(STOCK_LEFT,0);

        ImageView stockImageView = findViewById(R.id.stock_image);
        Picasso.with(getBaseContext()).load(merchandiseImageURL).into(stockImageView);

        TextView stockIdText = findViewById(R.id.stock_id);
        stockIdText.setText("货号：" + stockId);

        TextView stockNameText = findViewById(R.id.stock_name);
        stockNameText.setText("名称："+merchandiseName);

        TextView stockLeftText = findViewById(R.id.stock_left);
        stockLeftText.setText("库存：" + left);

        Button orderBtn  = findViewById(R.id.add_to_order_btn);
        orderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //进货逻辑
            }
        });

        Button outBtn = findViewById(R.id.out_btn);
        outBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //出货逻辑
                showOutstockDialog(v);
            }
        });

        //initStockList();
        initRecyclerview();
    }

    public void showOutstockDialog(View view)
    {
        OutStockDialogFragment outStockDialogFragment = new OutStockDialogFragment();
        outStockDialogFragment.show(getSupportFragmentManager(),"tag");
    }

    private void initStockList(){
        recordList.clear();
        AVQuery<AVObject> query = new AVQuery<>("Record");
        //query.whereEqualTo("objectId","5cde7bb7a5ef5700083bdda1");
        Log.e("jkc",stockId);
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                recordList.addAll(list);
                Log.e("jkcrecordlist",recordList.toString());
                stockAdapter.notifyDataSetChanged();
            }
        });/*
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (e == null) {
                    stockAdapter.notifyDataSetChanged();
                } else {
                    e.printStackTrace();
                }
            }
        });*/
    }

    //初始化每个商品的销售记录recyclerview
    private void initRecyclerview(){
          RecyclerView recyclerView = findViewById(R.id.stock_record_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        stockAdapter = new RecordAdapter(recordList,this);
        recyclerView.setAdapter(stockAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.delete:
                //删除库存逻辑
                break;
            case R.id.edit:
                //edit stock
                break;
            default:
                break;
        }
        return true;
    }
}
