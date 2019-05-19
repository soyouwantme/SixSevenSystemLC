package com.kk.sixsevensystemlc;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.avos.avoscloud.AVObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.ViewHolder> {

    private Context mContext;
    private List<AVObject> mRecordList;

    public RecordAdapter(List<AVObject> recordList,Context context){
        mRecordList =recordList;
        mContext = context;
    }

    @Override
    public RecordAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(mContext == null){
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.record_item,parent,false);
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

        Date date = mRecordList.get(position).getDate("date");
        SimpleDateFormat dateFormat = new SimpleDateFormat("M.d");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        holder.recordDate.setText(dateFormat.format(date));
        holder.recordTime.setText(timeFormat.format(date));
        holder.recordName.setText((CharSequence)mRecordList.get(position).get("name"));
        holder.recordNum.setText("×"+mRecordList.get(position).get("num"));
        holder.recordSum.setText("￥"+mRecordList.get(position).get("sum"));
    }

    @Override
    public int getItemCount() {
        return mRecordList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        private TextView recordDate;
        private TextView recordTime;
        private TextView recordName;
        private TextView recordNum;
        private TextView recordSum;
        private LinearLayout linearLayout;

        public ViewHolder(View itemView){
            super(itemView);
            recordDate = (TextView)itemView.findViewById(R.id.record_date);
            recordTime = (TextView)itemView.findViewById(R.id.record_time);
            recordName=(TextView)itemView.findViewById(R.id.record_name);
            recordNum = (TextView)itemView.findViewById(R.id.orecord_num);
            recordSum = (TextView)itemView.findViewById(R.id.record_sum);
            linearLayout= (LinearLayout)itemView.findViewById(R.id.record_linearlayout);
        }
    }
}
