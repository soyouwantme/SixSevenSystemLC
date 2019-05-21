package com.kk.sixsevensystemlc.restock;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.GetCallback;
import com.kk.sixsevensystemlc.R;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {

    AVObject merchandise;
    private Context mContext;
    private List<AVObject> mOrderList;

    public OrderAdapter(List<AVObject> orderList, Context context) {
        mOrderList = orderList;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("jkcpressed", "pressed");
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
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        //通过order表关联数据获取商品表的商品名称name
        AVObject order = AVObject.createWithoutData("Order", mOrderList.get(position).getObjectId());
        order.fetchInBackground("merchandiseId", new GetCallback<AVObject>() {
            @Override
            public void done(AVObject avObject, AVException e) {
                // 获取商品
                AVObject merchandise = avObject.getAVObject("merchandiseId");
                String t = merchandise.get("name") + "";
                holder.orderName.setText(t);
                //Log.d("xrjkc", t);
            }
        });

        holder.orderState.setText("已完成");
        holder.orderPrice.setText(mOrderList.get(position).get("price") == null ? "￥" : "￥" + mOrderList.get(position).get("price"));
        holder.orderNum.setText("×" + (CharSequence) mOrderList.get(position).get("orderNum"));
        float sum = Float.parseFloat(mOrderList.get(position).get("orderNum").toString()) * Float.parseFloat(mOrderList.get(position).get("price").toString());
        holder.orderSum.setText("￥" + String.format("%.2f", sum));
        //Log.d("urljkc",mMerchandiseList.get(position));
    }

    @Override
    public int getItemCount() {
        return mOrderList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView orderName;
        private TextView orderState;
        private TextView orderPrice;
        private TextView orderNum;
        private TextView orderSum;
        private LinearLayout linearLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            orderName = (TextView) itemView.findViewById(R.id.order_name);
            orderState = (TextView) itemView.findViewById(R.id.order_state);
            orderPrice = (TextView) itemView.findViewById(R.id.order_price);
            orderNum = (TextView) itemView.findViewById(R.id.order_num);
            orderSum = (TextView) itemView.findViewById(R.id.order_sum);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.order_linearlayout);
        }
    }
}
