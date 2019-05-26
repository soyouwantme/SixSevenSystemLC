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

public class SupplierActivity extends AppCompatActivity {

    private SupplierMerchandiseAdapter merchandiseAdapter;
    private List<AVObject> merchandiseList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.supplier_activity);
        Toolbar toolbar = findViewById(R.id.supplier_tool_bar);
        setSupportActionBar(toolbar);
        initRecyclerview();
        initMerchandise();
    }

    private void initRecyclerview(){
        RecyclerView recyclerView = findViewById(R.id.recycle_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        merchandiseAdapter = new SupplierMerchandiseAdapter(merchandiseList,this);
        recyclerView.setAdapter(merchandiseAdapter);
    }

    private void initMerchandise(){
        merchandiseList.clear();
        AVQuery<AVObject> avQuery = new AVQuery<>("Merchandise");
        avQuery.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (e == null) {
                    merchandiseList.addAll(list);
                    merchandiseAdapter.notifyDataSetChanged();
                } else {
                    e.printStackTrace();
                }
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.supplier_toolbar,menu);
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
                startActivity(new Intent(SupplierActivity.this, LoginActivity.class));
                SupplierActivity.this.finish();
                break;
            case R.id.add:
                startActivity(new Intent(SupplierActivity.this,SupplierAddActivity.class));
                break;
            default:
        }
        return true;
    }
}
