package com.kk.sixsevensystemlc.stock;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.avos.avoscloud.AVObject;
import com.kk.sixsevensystemlc.R;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    private Context mContext;
    private List<AVObject> mMessageList;


    public MessageAdapter(List<AVObject> orderList,Context context){
        mMessageList =orderList;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(mContext == null){
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_item,parent,false);
        final ViewHolder holder = new ViewHolder(view);
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("jkcpressed","pressed");
                /**
                 * 点击进入补货/下单界面
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

        int judge = Integer.parseInt(mMessageList.get(position).get("state").toString());
        //GradientDrawable mGrad = (GradientDrawable) holder.messageState.getBackground();
        //Log.d("jkcjudgemessage",judge+" ");
        switch (judge){
            case 0:
                //mGrad.setColor(Color.parseColor("#ff0000"));
                holder.messageName.setText("库存不足");
                break;
            case 1:
                //mGrad.setColor(Color.parseColor("#00ff00"));
                holder.messageName.setText("新入库");
                break;
            default:
                break;
        }
        holder.messageDetail.setText((CharSequence)mMessageList.get(position).get("detail"));
        //Log.d("urljkc",mMerchandiseList.get(position));
    }

    @Override
    public int getItemCount() {
        return mMessageList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        private TextView messageState;
        private TextView messageName;
        private TextView messageDetail;
        private LinearLayout linearLayout;

        public ViewHolder(View itemView){
            super(itemView);
            messageState= (TextView)itemView.findViewById(R.id.message_state);
            messageName=(TextView)itemView.findViewById(R.id.message_name);
            messageDetail = (TextView)itemView.findViewById(R.id.message_detail);
            linearLayout= (LinearLayout)itemView.findViewById(R.id.message_linearlayout);
        }
    }
}