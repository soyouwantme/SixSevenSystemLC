package com.kk.sixsevensystemlc;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.avos.avoscloud.AVObject;
import com.squareup.picasso.Picasso;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {

    private Context mContext;
    private List<AVObject> mOrderList;

    public OrderAdapter(List<AVObject> orderList,Context context){
        mOrderList =orderList;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(mContext == null){
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item,parent,false);
        final ViewHolder holder = new ViewHolder(view);
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("jkcpressed","pressed");
                /**
                 * 点击进入订单详情逻辑
                int position = holder.getAdapterPosition();
                AVObject order = mOrderList.get(position);
                Intent intent = new Intent(mContext,DetailActivity.class);
                intent.putExtra(DetailActivity.ORDER_NAME,(CharSequence)merchandise.get("name"));
                intent.putExtra(DetailActivity.MERCHANDISE_RATE,(CharSequence)merchandise.get("rate"));
                Object PRICE = order.get("price");
                intent.putExtra(DetailActivity.MERCHANDISE_PRICE,Double.parseDouble(PRICE.toString()));
                intent.putExtra(DetailActivity.MERCHANDISE_IMAGE_URL,mMerchandiseList.get(position).getAVFile("image").getUrl());
                mContext.startActivity(intent);*/
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.orderName.setText((CharSequence)mOrderList.get(position).get("name"));

        int judge = Integer.parseInt(mOrderList.get(position).get("state").toString());
        Log.d("jkcjudge",judge+" ");
        switch (judge){
            case 0:
                holder.orderState.setText("已下单");
                break;
            case 1:
                holder.orderState.setText("已发货");
                break;
            case 2:
                holder.orderState.setText("已完成");
                break;
            default:
                break;
        }
        holder.orderPrice.setText(mOrderList.get(position).get("price") == null ? "￥" : "￥" + mOrderList.get(position).get("price"));
        holder.orderNum.setText("×"+(CharSequence)mOrderList.get(position).get("num"));
        holder.orderSum.setText("￥"+mOrderList.get(position).get("summation"));
        //Log.d("urljkc",mMerchandiseList.get(position));
    }

    @Override
    public int getItemCount() {
        return mOrderList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        private TextView orderName;
        private TextView orderState;
        private TextView orderPrice;
        private TextView orderNum;
        private TextView orderSum;
        private LinearLayout linearLayout;

        public ViewHolder(View itemView){
            super(itemView);
            orderName=(TextView)itemView.findViewById(R.id.order_name);
            orderState = (TextView)itemView.findViewById(R.id.order_state);
            orderPrice=(TextView)itemView.findViewById(R.id.order_price);
            orderNum = (TextView)itemView.findViewById(R.id.order_num);
            orderSum = (TextView)itemView.findViewById(R.id.order_sum);
            linearLayout= (LinearLayout)itemView.findViewById(R.id.order_linearlayout);
        }
    }
}
