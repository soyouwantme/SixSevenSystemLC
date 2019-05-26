package com.kk.sixsevensystemlc;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;

import java.util.ArrayList;
import java.util.List;

public class ConsumerActivity extends AppCompatActivity {

    private ConsumerStockAdapter stockAdapter;
    private List<AVObject> stockList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.consumer_activity);
        Toolbar toolbar = findViewById(R.id.consumer_tool_bar);
        setSupportActionBar(toolbar);
        initRecyclerview();
        initStock();
    }

    private void initRecyclerview(){
        RecyclerView recyclerView = findViewById(R.id.recycle_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        stockAdapter = new ConsumerStockAdapter(stockList,this);
        recyclerView.setAdapter(stockAdapter);
    }

    private void initStock(){
        stockList.clear();
        AVQuery<AVObject> avQuery = new AVQuery<>("Stock");
        avQuery.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (e == null) {
                    stockList.addAll(list);
                    stockAdapter.notifyDataSetChanged();
                } else {
                    e.printStackTrace();
                }
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.consumer_toolbar,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.log_out:
                AVUser.logOut();// 清除缓存用户对象
                SharedPreferences sharedPreferences =
                        getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("email", null);
                editor.putString("role",null);
                editor.commit();
                startActivity(new Intent(ConsumerActivity.this, LoginActivity.class));
                ConsumerActivity.this.finish();
                break;
            case R.id.add:
                break;
            default:
        }
        return true;
    }
}
