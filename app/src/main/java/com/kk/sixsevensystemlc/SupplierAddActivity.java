package com.kk.sixsevensystemlc;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.SaveCallback;

public class SupplierAddActivity extends AppCompatActivity {

    private TextView name;
    private TextView price;
    private double sell;
    private TextView rate;
    private TextView detail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.supplier_add_activity);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        name = findViewById(R.id.add_name);
        price = findViewById(R.id.add_price);
        rate = findViewById(R.id.add_rate);
        detail = findViewById(R.id.add_detail);

        Button addBtn = findViewById(R.id.add_button);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean flag=checkinput();
                Log.e("flag",flag+"");
                //添加商品逻辑
                if(flag) {
                    AVObject merchandise = new AVObject("Merchandise");
                    // 存储一个带有图片的 Todo 到 LeanCloud 云端
                    //AVFile image = new AVFile("test.jpg", "http://www.zgjm.org/uploads/allimg/150812/1_150812103912_1.jpg", new HashMap<String, Object>());
                    merchandise.put("name", name.getText().toString());
                    merchandise.put("price", Integer.parseInt(price.getText().toString()));
                    Log.e("error", "" + price.getText().toString());
                    sell = Integer.parseInt(price.getText().toString()) / 0.6;
                    Log.e("sell", sell + "");
                    merchandise.put("sell", sell);
                    merchandise.put("rate", rate.getText().toString());
                    merchandise.put("detail", detail.getText().toString());
                    //merchandise.put("images",image);
                    merchandise.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(AVException e) {
                            if (e != null) {
                                Log.e("error", e.toString());
                            }
                        }
                    });

                }
                else
                    Toast.makeText(getApplicationContext(), "请正确输入", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public boolean checkinput(){
        Log.e("flag",name.getText().toString()+"1"+price.getText().toString()+2+rate.getText().toString()+3+detail.getText().toString());
        if (name.getText().toString().isEmpty()||price.getText().toString().isEmpty()||rate.getText().toString().isEmpty()||detail.getText().toString().isEmpty()){
            Toast.makeText(getApplicationContext(),"请正确填写",Toast.LENGTH_SHORT).show();
            return false;
        }else
        return true;
    }
}
