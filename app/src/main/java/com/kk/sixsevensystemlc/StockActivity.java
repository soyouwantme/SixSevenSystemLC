package com.kk.sixsevensystemlc;

import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
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
import com.avos.avoscloud.SaveCallback;
import com.kk.sixsevensystemlc.R;
import com.kk.sixsevensystemlc.restock.OrderAdapter;
import com.kk.sixsevensystemlc.sell.RecordAdapter;
import com.kk.sixsevensystemlc.sell.SellRecordFragment;
import com.kk.sixsevensystemlc.stock.InStockDialogFragment;
import com.kk.sixsevensystemlc.stock.NeedDialogFragment;
import com.kk.sixsevensystemlc.stock.OutStockDialogFragment;
import com.kk.sixsevensystemlc.stock.StockItemFragment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static java.lang.Thread.sleep;

public class StockActivity extends AppCompatActivity implements OutStockDialogFragment.OutListener,InStockDialogFragment.InListener, NeedDialogFragment.NeedListener {

    private RecordAdapter stockAdapter;
    private List<AVObject> recordList = new ArrayList<>();

    String stockId;
    String merchandiseName;
    int left;
    String objectId;

    public static final String STOCK_ID = "stock_id";
    public static final String OBJECTID_ID = "object_id";
    public static final String STOCK_NAME = "stock_name";
    public static final String STOCK_LEFT = "stock_left";
    public static final String STOCK_IMAGE_URL = "stock_image_url";

    private SwipeRefreshLayout swipeRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stock_activity);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        objectId = intent.getStringExtra(OBJECTID_ID);//订单唯一编号
        stockId = intent.getStringExtra(STOCK_ID);//对应的商品唯一编号
        merchandiseName = intent.getStringExtra(STOCK_NAME);
        String merchandiseImageURL = intent.getStringExtra(STOCK_IMAGE_URL);
        left = intent.getIntExtra(STOCK_LEFT,0);

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
                showInstockDialog(v);
            }
        });

        Button outBtn = findViewById(R.id.out_btn);
        outBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //出货逻辑
                showOutstockDialog(v);
                if(left<=10){
                    showNeedDialog(v);
                    addToMessage(merchandiseName);
                }
            }
        });

        Button inBtn = findViewById(R.id.add_to_order_btn);
        inBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //进货/下订单逻辑
                showInstockDialog(v);

            }
        });

        swipeRefresh = (SwipeRefreshLayout)findViewById(R.id.swipe_refresh);
        swipeRefresh.setColorSchemeColors(getResources().getColor(R.color.primary));
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshStock();
            }
        });

        initStockList();
        initRecyclerview();
    }

    private void addToMessage(final String name){
        AVQuery<AVObject> query = new AVQuery<>("message");
        query.whereEqualTo("detail",name);
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> avObjects, AVException avException) {
                if(avObjects.size()==0){
                    AVObject message = new AVObject("message");
                    message.put("detail",name);
                    message.saveInBackground();
                }
            }
        });
    }

    private void refreshStock(){
        stockAdapter.notifyDataSetChanged();
        swipeRefresh.setRefreshing(false);

    }

    public void showOutstockDialog(View view)
    {
        OutStockDialogFragment outStockDialogFragment = new OutStockDialogFragment();
        outStockDialogFragment.show(getSupportFragmentManager(),"tag");
    }

    public void showInstockDialog(View view)
    {
        InStockDialogFragment inStockDialogFragment = new InStockDialogFragment();
        inStockDialogFragment.show(getSupportFragmentManager(),"tag");
    }

    public void showNeedDialog(View view)
    {
        NeedDialogFragment needDialogFragment = new NeedDialogFragment();
        needDialogFragment.show(getSupportFragmentManager(),"tag");
    }


    private void initStockList(){
        recordList.clear();
        AVObject merchandise = AVObject.createWithoutData("Merchandise",stockId);
        AVQuery<AVObject> query = new AVQuery<>("Record");
        //Log.e("jkc",merchandiseName);
        query.whereEqualTo("merchandiseId",merchandise);
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (e==null) {
                    recordList.addAll(list);
                    //Log.e("jkcrecordlist", recordList.toString());
                    stockAdapter.notifyDataSetChanged();
                }else{
                    Log.e("stock",e.toString());
                }
            }
        });
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
    public void onOutComplete(int num) {
        //更新库存
        AVObject todo = AVObject.createWithoutData("Stock", objectId);
        left = left - num;
        todo.put("left",left);
        todo.saveInBackground(null, new SaveCallback() {
            @Override
            public void done(AVException e) {
                if (e==null) {
                    Log.e("jkcnum", "succeed");
                }else {
                    Log.e("jkcnum", "fail"+e.toString());
                }
            }
        });
        //添加销售表
        AVObject merchandise = AVObject.createWithoutData("Merchandise",stockId);
        AVObject record = new AVObject("Record");
        record.put("recordNum",num);
        //获取当前时间
        Date time = Calendar.getInstance().getTime();
        record.put("sellDate",time);
        record.put("merchandiseId",merchandise);//外键
        record.saveInBackground(null, new SaveCallback() {
            @Override
            public void done(AVException e) {
                if (e==null) {
                    Log.e("jkcrecord", "succeed");
                }else {
                    Log.e("jkcrecord", "fail"+e.toString());
                }
            }
        });
    }


    @Override
    public void onInComplete(int num) {
        //更新库存
        AVObject todo = AVObject.createWithoutData("Stock", objectId);
        left = left + num;
        todo.put("left",left);
        todo.saveInBackground(null, new SaveCallback() {
            @Override
            public void done(AVException e) {
                if (e==null) {
                    Log.e("jkcnum", "succeed");
                }else {
                    Log.e("jkcnum", "fail"+e.toString());
                }
            }
        });
        //获取外键
        AVObject merchandise = AVObject.createWithoutData("Merchandise",stockId);
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
                    Log.e("jkcrecord", "succeed");
                }else {
                    Log.e("jkcrecord", "fail"+e.toString());
                }
            }
        });
    }

    @Override
    public void onNeedComplete(int num) {

    }
}
