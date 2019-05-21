package com.kk.sixsevensystemlc.stock;

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
import com.kk.sixsevensystemlc.R;
import com.kk.sixsevensystemlc.StockActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

public class StockAdapter extends RecyclerView.Adapter<StockAdapter.ViewHolder>{

    private Context mContext;
    private List<AVObject> mStockList;

    public StockAdapter(List<AVObject> stockList,Context context){
        mStockList =stockList;
        mContext = context;
    }

    @Override
    public StockAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(mContext == null){
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.stock_item,parent,false);
        final StockAdapter.ViewHolder holder = new StockAdapter.ViewHolder(view);
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                final AVObject stock = mStockList.get(position);
                final Intent intent = new Intent(mContext, StockActivity.class);

                //通过order表关联数据获取商品表的商品名称name
                AVObject stock1 = AVObject.createWithoutData("Stock", mStockList.get(position).getObjectId());
                stock1.fetchInBackground("merchandiseId", new GetCallback<AVObject>() {
                    @Override
                    public void done(AVObject avObject, AVException e) {
                        // 获取商品
                        AVObject merchandise = avObject.getAVObject("merchandiseId");
                        String name = merchandise.get("name") + "";
                        String imgUrl = merchandise.getAVFile("image").getUrl();
                        intent.putExtra(StockActivity.STOCK_ID,merchandise.getObjectId());
                        intent.putExtra(StockActivity.STOCK_IMAGE_URL,imgUrl);
                        intent.putExtra(StockActivity.STOCK_NAME,name);
                        intent.putExtra(StockActivity.STOCK_LEFT,Integer.parseInt(stock.get("left").toString()));
                        mContext.startActivity(intent);
                    }
                });
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final StockAdapter.ViewHolder holder, final int position) {
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
            stockImage=(ImageView)itemView.findViewById(R.id.stock_image);
            stockId = (TextView)itemView.findViewById(R.id.stock_id);
            stockName=(TextView)itemView.findViewById(R.id.stock_name);
            stockLeft=(TextView)itemView.findViewById(R.id.stock_left);
            linearLayout= (LinearLayout)itemView.findViewById(R.id.stock_linearlayout);
        }
    }
}
