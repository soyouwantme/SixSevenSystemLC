package com.kk.sixsevensystemlc.restock;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
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
    private Button deleteBtn;
    private CheckBox total_checkbox;
    int begin=0;

    private SwipeRefreshLayout swipeRefresh;
    private CartAdapter cartAdapter;
    private List<AVObject> cartList = new ArrayList<>();
    final private String[] objectId=new String[100];

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.restock_cart_fragment, container, false);
        swipeRefresh=view.findViewById(R.id.cart_refresh);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshFruits();
            }
        });
        checkBtn = (Button) view.findViewById(R.id.settlement);
        checkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("jkcpressedbtn", "ipressed");
            }
        });
        begin=0;
        initRecyclerview();
        ButtonSet();
        initCart(false);
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
        checkBtn = view.findViewById(R.id.settlement);
        deleteBtn=view.findViewById(R.id.deletement);
        total_checkbox = view.findViewById(R.id.total_checkbox);
        //TvPrice=view.findViewById(R.id.tv_price);
        checkBtn.setEnabled(true);
        checkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("确认购买");
                String[] strArray = cartAdapter.Returnselected();
                final boolean flag=strArray[0].equals("对不起，没有选定物品");
                Log.e("string",""+strArray);
                if (strArray.equals(null)){
                }
                builder.setItems(strArray, null);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if(!flag)
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
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("删除");
                builder.setMessage("确认删除？");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        deleteout();
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

    private void initCart(final boolean flag) {
        cartList.clear();
        AVQuery<AVObject> avQuery = new AVQuery<>("Cart");
        avQuery.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (e == null) {
                    cartList.clear();
                    cartList.addAll(list);
                    for (int i = 0; i < list.size(); i++) {
                        objectId[i] = list.get(i).getObjectId();
                        Log.e("cartlist", "" + objectId[i]);
                    }
                    Log.e("jkccartlist", "123"+cartList.toString());
                    Log.e("jkccartlist", "123"+flag);
                    if(flag) {
                        Log.e("jkccartlist", cartList.size()+"");
                   /*
                    new Thread(new Runnable(){
                        @Override
                        public void run(){
                            try{
                                Thread.sleep(1000);
                                cartAdapter.resetcartlist(cartList);
                                Log.e("jkccartlist", "123");
                                cartAdapter.start();
                                cartAdapter.notifyDataSetChanged();
                            }catch (InterruptedException e){
                                e.printStackTrace();
                            }
                        }
                    });
                    }
                    if(!flag)
                    cartAdapter.notifyDataSetChanged();
                    */
                   //cartAdapter.clearlist();
                   //cartAdapter.resetcartlist(cartList);
                        cartAdapter.clearlist();
                   cartAdapter.start();
                   total_checkbox.setChecked(false);
                    }
                    cartAdapter.resetcartlist(cartList);
                    cartAdapter.notifyDataSetChanged();
                } else {
                    e.printStackTrace();
                }
            }
        });
    }

    int i = 0;

    private void checkOut(){
        final String[] ids = cartAdapter.getid();
        final int[] num = cartAdapter.getnumber();
        int size = cartAdapter.getItemCount();
        final String[] getid=new String[100];
        final int[] getleft=new int[100];
        final String[] getobid=new String[100];
        ///for (String id:ids){
           // final AVObject merchandise = AVObject.createWithoutData("Merchandise",id);
            //获取对象来修改库存
            AVQuery<AVObject> query = new AVQuery<>("Stock");
            //查找库存表
            //query.whereEqualTo("merchandiseId",merchandise);
            query.findInBackground(new FindCallback<AVObject>() {
                @Override
                public void done(List<AVObject> avObjects, AVException avException) {
                    Log.e("jck",avObjects.size()+"");
                    for(int i=0;i<avObjects.size();i++)
                    {
                        getid[i]=avObjects.get(i).get("merchandiseId").toString();
                        getobid[i]=avObjects.get(i).getObjectId();
                        //Log.e("jck",avObjects.size()+"");
                        //Log.e("getids",avObjects.get(i).get("merchandiseId").toString());
                        int begin=getid[i].indexOf("\"objectId\":\"");
                        int end=getid[i].indexOf("\",\"updatedAt\"");
                        getid[i]=getid[i].substring(begin+12,end);
                        Log.e("getids",getid[i]);
                        getleft[i]=Integer.parseInt(avObjects.get(i).get("left").toString());
                        Log.e("getids",getleft[i]+"");
                        //Log.e("jck",avObjects.size()+"");
                    }
                    for(int j=0;j<ids.length;j++)
                    {
                        int flag=-1;
                        for(int i=0;i<avObjects.size();i++)
                        {
                            if(ids[j].equals(getid[i]))flag=i;
                            //Log.e("jck",avObjects.size()+"");
                            getleft[i]=Integer.parseInt(avObjects.get(i).get("left").toString());
                            //Log.e("jck",avObjects.size()+"");
                        }
                        if( flag==-1){
                            Log.e("jkc库存无","succeed");
                            AVObject newStock = new AVObject("Stock");
                            newStock.put("left",num[i]);
                            Log.d("merId",""+ids[j]);
                            newStock.put("merchandiseId",ids[j]);
                            newStock.saveInBackground(null, new SaveCallback() {
                                @Override
                                public void done(AVException e) {
                                    if (e == null) {
                                        Log.e("jkc库存更新", "succeed");
                                    } else {
                                        Log.e("jkcn库存更新", "fail" + e.toString());
                                    }
                                }
                            });
                        }else {
                            //更新库存,取得当前库存量
                            Log.e("jkc库存有","succeed");
                            Log.e("jkc库存有",ids[j]);
                            AVObject todo = AVObject.createWithoutData("Stock", getobid[flag]);
                            //库存修改（增加）
                            //Log.e("jkc库存有",todo.get("left").toString());
                            //left = left + num[i];
                            Log.e("num",getleft[flag]+num[j]+"");
                            todo.put("left", num[j]+getleft[flag]);
                            //todo.put("merchandiseId",merchandise);
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
                }
                    //Log.e("cart","jinru");
                    for(int i=0;i<cartList.size();i++)
                    {
                        if(cartAdapter.isSelected.get(i))
                        {
                            AVObject todo = AVObject.createWithoutData("Cart", objectId[i]);
                            Log.e("cart","delete"+objectId[i]);
                            todo.deleteInBackground();
                        }
                    }
                    //initRecyclerview();
                    /*
                    new Thread(new Runnable(){
                        @Override
                        public void run(){
                            try{
                                Thread.sleep(1000);
                            }catch (InterruptedException e){
                                e.printStackTrace();
                            }
                        }
                    });*/
                    Log.e("123","123");
                    for(int i=0;i<10000;i++);
                    for(int i=0;i<10000;i++);
                    for(int i=0;i<10000;i++);
                    for(int i=0;i<10000;i++);
                    for(int i=0;i<10000;i++);
                    for(int i=0;i<10000;i++);
                    Log.e("123","123");
                    initCart(true);
            }
            /*
        {
                @Override
                public void done(AVObject stock, AVException avException) {
                    Log.e("jkc走库存","succeed");

                    }


                    */
   //     }

    });

}
    private void deleteout(){
        final String[] ids = cartAdapter.getid();
        final int[] num = cartAdapter.getnumber();
        final String[] getid=new String[100];
        final int[] getleft=new int[100];
        final String[] getobid=new String[100];
        ///for (String id:ids){
        // final AVObject merchandise = AVObject.createWithoutData("Merchandise",id);
        //获取对象来修改库存
        AVQuery<AVObject> query = new AVQuery<>("Stock");
        //查找库存表
        //query.whereEqualTo("merchandiseId",merchandise);
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> avObjects, AVException avException) {
                Log.e("jck",avObjects.size()+"");
                for(int j=0;j<ids.length;j++)
                {
                    for(int i=0;i<avObjects.size();i++)
                    {
                        if(ids[j].equals(getid[i]));
                        //Log.e("jck",avObjects.size()+"");
                        getleft[i]=Integer.parseInt(avObjects.get(i).get("left").toString());
                        //Log.e("jck",avObjects.size()+"");
                    }
                }
                //Log.e("cart","jinru");
                for(int i=0;i<cartList.size();i++)
                {
                    if(cartAdapter.isSelected.get(i))
                    {
                        AVObject todo = AVObject.createWithoutData("Cart", objectId[i]);
                        Log.e("cart","delete"+objectId[i]);
                        todo.deleteInBackground();
                    }
                }
                Log.e("123","123");
                for(int i=0;i<10000;i++);
                for(int i=0;i<10000;i++);
                for(int i=0;i<10000;i++);
                for(int i=0;i<10000;i++);
                for(int i=0;i<10000;i++);
                for(int i=0;i<10000;i++);
                Log.e("123","123");
                initCart(true);
            }
            /*
        {
                @Override
                public void done(AVObject stock, AVException avException) {
                    Log.e("jkc走库存","succeed");

                    }


                    */
            //     }

        });

    }
    public void refreshFruits(){
        new Thread(new Runnable()
        {
            @Override
            public void run() {
                try{Thread.sleep(2000);}catch (InterruptedException e){e.printStackTrace();}
                getActivity().runOnUiThread(new Runnable(){
                    @Override
                    public void run(){
                        initCart(false);
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }

        }).start();
    }

    public void onResume()
    {
        super.onResume();
        if(begin>=1)
        {
            cartAdapter.start();
            initCart(false);
            cartAdapter.resetcartlist(cartList);
        }
    }

}
