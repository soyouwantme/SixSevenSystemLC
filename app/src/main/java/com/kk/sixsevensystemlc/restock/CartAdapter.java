package com.kk.sixsevensystemlc.restock;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.SaveCallback;
import com.kk.sixsevensystemlc.R;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    private Context mContext;
    private List<AVObject> mCartList;

    public HashMap<Integer, Boolean> isSelected;
    public int[] finalnum=new int[100];
    public int flag=0;
    public String[] finalname=new String[100];
    public String[] finalprice=new String[100];

    String name;

    public CartAdapter(List<AVObject> cartList,Context context){
        mCartList = cartList;
        mContext = context;
    }

    public void clearlist()
    {
        int k=0;
        for(int i=0;i<mCartList.size();i++) {
            if(isSelected.get(i+k)) {
                mCartList.remove(i);
                i--;
                k++;
            }
        }
        Log.e("incartlist","123"+mCartList.size());
    }

    public void  resetcartlist(List<AVObject> cartList)
    {
        Log.e("incartlist","123");
        Log.e("incartlist","123"+cartList.size());
        mCartList=cartList;
        for(int i=0;i<cartList.size();i++) {
            Log.e("incartlist ", "123"+mCartList.get(i).toString() );
            Log.e("incartlist ", "321"+cartList.get(i).toString() );
        }
    }


    //初始化函数
    public void start()
    {
        isSelected = new HashMap<Integer, Boolean>();
        for (int i = 0; i < mCartList.size(); i++) {
            isSelected.put(i, false);
            //Log.e(i+"123",isSelected.get(i)+" "+i);
        }
        for (int i = 0; i < mCartList.size(); i++) {
            finalnum[i] = 1;
            //Log.e(i+"123",num[i]+"123");
        }
        flag=0;
    }

    @Override
    public CartAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cart_item,parent,false);
        CartAdapter.ViewHolder holder = new CartAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final CartAdapter.ViewHolder holder, final int position) {

        //通过order表关联数据获取商品表的商品名称name
        AVObject cart = AVObject.createWithoutData("Cart", mCartList.get(position).getObjectId());
        Log.e("Cart","1:"+mCartList.size());

        cart.fetchInBackground("merchandiseId", new GetCallback<AVObject>() {
            @Override
            public void done(AVObject avObject, AVException e) {
                // 获取商品
                if (avObject != null) {
                    final AVObject merchandise = avObject.getAVObject("merchandiseId");
                    name = merchandise.get("name") + "";
                    Log.e("Cart","2:"+mCartList.size());
                    //Log.e("Cart",""+merchandise.get("name"));
                    String price = merchandise.get("price") + "";
                    Log.e("Cart","3:"+mCartList.size());
                    String num = mCartList.get(position).get("cartNum") + "";
                    String imgUrl = merchandise.getAVFile("image").getUrl();
                    Picasso.with(mContext).load(imgUrl).into(holder.cartImage);
                    holder.cartName.setText(name);
                    holder.cartPrice.setText(price);
                    holder.cartNum.setText(num);
                    finalname[position] = name;
                    finalprice[position] = price;
                    isSelected.put(position, false);
                    if(isSelected.get(position))
                        holder.determine_checkbox.setChecked(true);
                    else
                        holder.determine_checkbox.setChecked(false);

                    //finalnum =new int[100];
                    for (int i = 0; i < mCartList.size(); i++) {
                        finalnum[i] = Integer.valueOf(mCartList.get(i).get("cartNum").toString());
                    }


                    if (flag == 1) {
                        finalnum[position] = Integer.valueOf(holder.cartNum.getText().toString());
                        holder.determine_checkbox.setChecked(true);
                        isSelected = new HashMap<Integer, Boolean>();
                        for (int i = 0; i < mCartList.size(); i++) {
                            isSelected.put(i, true);
                        }
                    }

                    if (flag == 2) {
                        finalnum[position] = Integer.valueOf(holder.cartNum.getText().toString());
                        holder.determine_checkbox.setChecked(false);
                        isSelected = new HashMap<Integer, Boolean>();
                        isSelected.put(position, false);
                        for (int i = 0; i < mCartList.size(); i++) {
                            isSelected.put(i, false);
                        }
                    }

                    holder.determine_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if (holder.determine_checkbox.isChecked()) {
                                isSelected.put(position, true);
                            } else isSelected.put(position, false);
                        }
                    });
                    holder.cartAdd.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String numberString = holder.cartNum.getText().toString();
                            int number = Integer.valueOf(numberString);
                            number++;

                            AVObject cart = AVObject.createWithoutData("Cart", mCartList.get(position).getObjectId());
                            //Log.e("cartId",mCartList.get(position).getObjectId()+"");
                            cart.put("cartNum", number);
                            cart.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(AVException e) {
                                    if (e != null) {
                                        Log.e("error", e.toString());
                                    }
                                }
                            });

                            numberString = String.valueOf(number);
                            holder.cartNum.setText(numberString);
                            finalnum[position] = number;
                            flag = 0;
                        }
                    });
                    holder.cartReduce.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String numberString = holder.cartNum.getText().toString();
                            int number = Integer.valueOf(numberString).intValue();
                            if (number != 0) {
                                number--;

                                AVObject cart = AVObject.createWithoutData("Cart", mCartList.get(position).getObjectId());
                                //Log.e("cartId",mCartList.get(position).getObjectId()+"");
                                cart.put("cartNum", number);
                                cart.saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(AVException e) {
                                        if (e != null) {
                                            Log.e("error", e.toString());
                                        }
                                    }
                                });

                                numberString = String.valueOf(number);
                                holder.cartNum.setText(numberString);
                                finalnum[position] = number;
                                flag = 0;
                            }
                        }
                    });
                }
            }
        });
    }

    public void setflag(int i){
        flag=i;
    }

    @Override
    public int getItemCount() {
        return mCartList.size();
    }

    public String[] Returnselected() {
        int count=0,price=0,str_num=0;
        if (isSelected.size()==0){
            String[] strArray=new String[1];
            strArray[0]="对不起，没有选定物品";
            return strArray;
        }
        for(int i=0;i< isSelected.size(); i++){
            if(isSelected.get(i)){
                count++;
            }
        }
        if(count==0)
        {
            String[] strArray=new String[1];
            strArray[0]="对不起，没有选定物品";
            return strArray;
        }
        String[] strArray=new String[count+1];/*
        for(int i=0;i< mCartList.size(); i++) {
            Log.e("123", finalnum[i] + "+" + i);
            Log.e("123", isSelected.get(i) + "+" + i);
            Log.e("123", finalprice[i] + "+" + i);
            Log.e("123", finalname[i] + "+" + i);
        }*/
        for(int i=0;i< mCartList.size(); i++)
        {
            if(isSelected.get(i))
            {
                strArray[str_num]=finalname[i]+"   "+finalnum[i]+"*"+finalprice[i];
                price=price+finalnum[i]*Integer.valueOf(finalprice[i]);
                str_num++;
            }
        }
        strArray[str_num]="总价格："+"  "+price;
        return strArray;
    }

    public String[] getid(){
        int count=0,str_num=0;
        for(int i=0;i< mCartList.size(); i++)
        {
            if(isSelected.get(i))
            {
                count++;
            }
        }
        if(count==0)
        {
            String[] strArray=new String[1];
            strArray[0]="";
            return strArray;
        }
        String[] strArray=new String[count];
        for(int i=0;i< mCartList.size(); i++)
        {
            if(isSelected.get(i))
            {

                Object temp =mCartList.get(i).get("merchandiseId");
                strArray[str_num] = temp.toString();
                int begin=strArray[str_num].indexOf("\"objectId\":\"");
                int end=strArray[str_num].indexOf("\",\"updatedAt\"");
                strArray[str_num]=strArray[str_num].substring(begin+12,end);
                //Log.e("getid",strArray[i]);
                str_num++;
            }
        }
        return strArray;
    }

    public int[] getnumber(){
        int count=0,num_num=0;
        for(int i=0;i< mCartList.size(); i++)
        {
            if(isSelected.get(i))
            {
                count++;
            }
        }
        if(count==0)
        {
            int[] number=new int[1];
            number[0]=0;
            return number;
        }
        int[] number=new int[count];
        for(int i=0;i< mCartList.size(); i++)
        {
            if(isSelected.get(i))
            {
                number[num_num]=finalnum[i];
                Log.e("getnum",number[num_num]+"");
                num_num++;
            }
        }
        return number;
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView cartImage;
        private TextView cartName;
        private TextView cartPrice;
        private TextView cartNum;
        private TextView cartReduce;
        private TextView cartAdd;

        private LinearLayout linearLayout;

        private CheckBox determine_checkbox;

        public ViewHolder(View itemView){
            super(itemView);
            cartImage=(ImageView)itemView.findViewById(R.id.cart_image);
            cartName=(TextView)itemView.findViewById(R.id.cart_name);
            cartPrice=(TextView)itemView.findViewById(R.id.cart_price);
            cartNum=itemView.findViewById(R.id.cart_num);
            linearLayout= (LinearLayout)itemView.findViewById(R.id.cart_linearlayout);

            cartReduce=itemView.findViewById(R.id.cart_reduce);
            cartAdd=itemView.findViewById(R.id.cart_add);
            determine_checkbox=itemView.findViewById(R.id.determine_checkbox);
        }
    }
}
