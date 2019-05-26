package com.kk.sixsevensystemlc;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.GetCallback;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ConsumerStockAdapter extends RecyclerView.Adapter<ConsumerStockAdapter.ViewHolder>{

    private Context mContext;
    private List<AVObject> mStockList;

    public ConsumerStockAdapter(List<AVObject> stockList,Context context){
        mStockList =stockList;
        mContext = context;
    }

    public void ClearStockList()
    {
        mStockList.clear();
    }
    public void SetStockList(List<AVObject> stockList)
    {
        mStockList.addAll(stockList);
    }

    @Override
    public ConsumerStockAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(mContext == null){
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.consumer_detail_item,parent,false);
        final ConsumerStockAdapter.ViewHolder holder = new ConsumerStockAdapter.ViewHolder(view);
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                final AVObject stock = mStockList.get(position);
                final Intent intent = new Intent(mContext, ConsumerDetailActivity.class);
                final String objectId = stock.getObjectId();
                //通过order表关联数据获取商品表的商品名称name
                AVObject stock1 = AVObject.createWithoutData("Stock", mStockList.get(position).getObjectId());
                stock1.fetchInBackground("merchandiseId", new GetCallback<AVObject>() {
                    @Override
                    public void done(AVObject avObject, AVException e) {
                        // 获取商品
                        AVObject merchandise = avObject.getAVObject("merchandiseId");
                        String name = merchandise.get("name") + "";
                        String imgUrl = merchandise.getAVFile("image").getUrl();
                        String price = merchandise.get("sell")+"";
                        intent.putExtra(ConsumerDetailActivity.OBJECTID_ID,objectId);
                        intent.putExtra(ConsumerDetailActivity.STOCK_ID,merchandise.getObjectId());
                        intent.putExtra(ConsumerDetailActivity.STOCK_IMAGE_URL,imgUrl);
                        intent.putExtra(ConsumerDetailActivity.STOCK_NAME,name);
                        intent.putExtra(ConsumerDetailActivity.MERCHANDISE_PRICE,price);
                        intent.putExtra(ConsumerDetailActivity.MERCHANDISE_RATE,(CharSequence)merchandise.get("rate"));
                        intent.putExtra(ConsumerDetailActivity.STOCK_LEFT,Integer.parseInt(stock.get("left").toString()));
                        intent.putExtra(ConsumerDetailActivity.MERCHANDISE_DETAIL,(CharSequence)merchandise.get("detail"));
                        mContext.startActivity(intent);
                    }
                });
                String objectIdm = getobjectId(position);
                AVObject merchandise = AVObject.createWithoutData("Merchandise", objectIdm);
                merchandise.fetchInBackground(new GetCallback<AVObject>() {
                    @Override
                    public void done(AVObject merchandise, AVException e) {/*
                        // 获取商品
                        Object merchandiseId = merchandise.get("merchandiseId");
                        intent.putExtra(ConsumerDetailActivity.OBJECT_ID,merchandise.getObjectId());
                        intent.putExtra(ConsumerDetailActivity.MERCHANDISE_NAME,(CharSequence)merchandise.get("name"));
                        intent.putExtra(ConsumerDetailActivity.MERCHANDISE_RATE,(CharSequence)merchandise.get("rate"));
                        Object PRICE = merchandise.get("price");
                        intent.putExtra(ConsumerDetailActivity.MERCHANDISE_PRICE,Double.parseDouble(PRICE.toString()));
                        //intent.putExtra(DetailActivity.MERCHANDISE_IMAGE_URL,mMerchandiseList.get(position).getAVFile("image").getUrl());
                        intent.putExtra(ConsumerDetailActivity.MERCHANDISE_DETAIL,(CharSequence)merchandise.get("detail"));
                        //mContext.startActivity(intent);*/
                    }
                });
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ConsumerStockAdapter.ViewHolder holder, final int position) {
        //通过order表关联数据获取商品表的商品名称name
        AVObject stock = AVObject.createWithoutData("Stock", mStockList.get(position).getObjectId());
        stock.fetchInBackground("merchandiseId", new GetCallback<AVObject>() {
            @Override
            public void done(AVObject avObject, AVException e) {
                // 获取商品
                AVObject merchandise = avObject.getAVObject("merchandiseId");
                String name = merchandise.get("name") + "";
                String imgUrl = merchandise.getAVFile("image").getUrl();
                Picasso.with(mContext).load(imgUrl).into(holder.stockImage);
                holder.stockId.setText("货号："+merchandise.getObjectId());
                holder.stockName.setText("名称："+name);
                holder.stockLeft.setText("库存：" + mStockList.get(position).get("left"));
            }
        });
    }

    public String getobjectId(int pos){
        Object temp =mStockList.get(pos).get("merchandiseId");
        String str = temp.toString();
        int begin=str.indexOf("\"objectId\":\"");
        int end=str.indexOf("\",\"updatedAt\"");
        str=str.substring(begin+12,end);
        return str;
    }

    @Override
    public int getItemCount() {
        return mStockList.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView stockImage;
        private TextView stockId;
        private TextView stockName;
        private TextView stockLeft;
        private LinearLayout linearLayout;

        public ViewHolder(View stockView){
            super(stockView);
            stockImage=(ImageView)itemView.findViewById(R.id.customer_stock_image);
            stockId = (TextView)itemView.findViewById(R.id.customer_stock_id);
            stockName=(TextView)itemView.findViewById(R.id.customer_stock_name);
            stockLeft=(TextView)itemView.findViewById(R.id.customer_stock_left);
            linearLayout= (LinearLayout)itemView.findViewById(R.id.customer_stock_linearlayout);
        }
    }
}
