package com.kk.sixsevensystemlc.restock;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVSaveOption;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.SaveCallback;
import com.kk.sixsevensystemlc.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


public class RestockCartFragment extends Fragment {

    View view;
    private Button checkBtn;
    private CheckBox total_checkbox;

    private CartAdapter cartAdapter;
    private List<AVObject> cartList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.restock_cart_fragment, container, false);
        //checkBtn = (Button) view.findViewById(R.id.settlement);
        /*checkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("jkcpressedbtn", "ipressed");
            }
        });*/
        initRecyclerview();
//        ButtonSet();
        initCart();
        return view;
    }

    private void initRecyclerview() {
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycle_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getActivity());
        recyclerView.setLayoutManager(layoutManager);
        cartAdapter = new CartAdapter(cartList, getContext());
        cartAdapter.start();
        recyclerView.setAdapter(cartAdapter);
        cartAdapter.isSelected = new HashMap<Integer, Boolean>();
        for (int i = 0; i < cartAdapter.getItemCount(); i++) {
            cartAdapter.isSelected.put(i, false);
            //Log.e(i+"123",isSelected.get(i)+" "+i);
        }
        for (int i = 0; i < cartAdapter.getItemCount(); i++) {
            cartAdapter.finalnum[i] = 1;
            //Log.e(i+"123",num[i]+"123");
        }
    }


    private void ButtonSet() {
        //checkBtn = view.findViewById(R.id.settlement);
        //total_checkbox = view.findViewById(R.id.total_checkbox);
        //TvPrice=view.findViewById(R.id.tv_price);
        checkBtn.setEnabled(true);
        checkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("确认购买");
                String[] strArray = cartAdapter.Returnselected();
                Log.e("string",""+strArray);
                if (strArray.equals(null)){
                }
                builder.setItems(strArray, null);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        checkOut();
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                builder.create();
                builder.show();
            }
        });
        total_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //Log.e("123","1");
                if (total_checkbox.isChecked()) {
                    cartAdapter.setflag(1);
                    cartAdapter.notifyDataSetChanged();
                } else {
                    cartAdapter.setflag(2);
                    cartAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void initCart() {
        cartList.clear();
        AVQuery<AVObject> avQuery = new AVQuery<>("Cart");
        avQuery.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (e == null) {
                    cartList.addAll(list);
                    //Log.d("jkccartlist", cartList.toString());
                    cartAdapter.notifyDataSetChanged();
                } else {
                    e.printStackTrace();
                }
            }
        });
    }

    int i = 0;

    private void checkOut(){
        String[] ids = cartAdapter.getid();
        final int[] num = cartAdapter.getnumber();
        int size = cartAdapter.getItemCount();
        for (String id:ids){
            final AVObject merchandise = AVObject.createWithoutData("Merchandise",id);
            //获取对象来修改库存
            AVQuery<AVObject> query = new AVQuery<>("Stock");
            //查找库存表
            query.whereEqualTo("merchandiseId",merchandise);
            query.getFirstInBackground(new GetCallback<AVObject>() {
                @Override
                public void done(AVObject stock, AVException avException) {
                    Log.e("jkc走库存","succeed");
                    if(stock == null){
                        Log.e("jkc库存无","succeed");
                        AVObject newStock = new AVObject("Stock");
                        newStock.put("left",num[i]);
                        Log.d("merId",""+merchandise.toString());
                        newStock.put("merchandiseId",merchandise);
                        newStock.saveInBackground();
                    }else {
                        //更新库存,取得当前库存量
                        Log.e("jkc库存有","succeed");
                        int left = Integer.parseInt(stock.get("left") + "");
                        Log.d("stockid",stock.getObjectId()+"");
                        AVObject todo = AVObject.createWithoutData("Stock", stock.getObjectId());
                        //库存修改（增加）
                        left = left + num[i];
                        todo.put("left", left);
                        todo.put("merchandiseId",merchandise);
                        todo.saveInBackground(null, new SaveCallback() {
                            @Override
                            public void done(AVException e) {
                                if (e == null) {
                                    Log.e("jkc库存更新", "succeed");
                                } else {
                                    Log.e("jkcn库存更新", "fail" + e.toString());
                                }
                            }
                        });
                    }


                    //添加订单表
                    AVObject order = new AVObject("Order");
                    order.put("orderNum",num[i]);
                    order.put("merchandiseId",merchandise);//外键
                    //获取当前时间
                    Date time = Calendar.getInstance().getTime();
                    order.put("orderDate",time);
                    AVSaveOption option = new AVSaveOption();
                    order.saveInBackground(option, new SaveCallback() {
                        @Override
                        public void done(AVException e) {
                            if (e==null) {
                                Log.e("jkc订单更新", "succeed");
                            }else {
                                Log.e("jkc订单更新", "fail"+e.toString());
                            }
                        }
                    });

                    i++;

                }
            });
        }

    }
}