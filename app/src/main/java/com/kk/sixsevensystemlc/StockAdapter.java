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

import com.avos.avoscloud.AVObject;
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
                //Log.d("jkcpressed","pressed");
                int position = holder.getAdapterPosition();
                AVObject stock = mStockList.get(position);
                Intent intent = new Intent(mContext,StockActivity.class);
                Object stockId = stock.get("stockId");
                intent.putExtra(StockActivity.STOCK_ID,Integer.parseInt(stockId.toString()));
                intent.putExtra(StockActivity.STOCK_IMAGE_URL,mStockList.get(position).getAVFile("image").getUrl());
                intent.putExtra(StockActivity.STOCK_NAME,(CharSequence)stock.get("name"));
                intent.putExtra(StockActivity.STOCK_LEFT,Integer.parseInt(stock.get("left").toString()));
                mContext.startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull StockAdapter.ViewHolder holder, final int position) {
        Picasso.with(mContext).load(mStockList.get(position).getAVFile("image").getUrl()).into(holder.stockImage);
        holder.stockId.setText("货号："+mStockList.get(position).get("stockId"));
        holder.stockName.setText("名称："+mStockList.get(position).get("name"));
        holder.stockLeft.setText("库存：" + mStockList.get(position).get("left"));
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
