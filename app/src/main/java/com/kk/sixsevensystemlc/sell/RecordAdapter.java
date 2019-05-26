package com.kk.sixsevensystemlc.sell;

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

    public void ClearList()
    {
        mRecordList.clear();
    }

    public void SetList(List<AVObject> recordList)
    {
        mRecordList.addAll(recordList);
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
                 * 点击进入订单详情逻辑*/
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        Date date = mRecordList.get(position).getDate("sellDate");
        SimpleDateFormat dateFormat = new SimpleDateFormat("M.d");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        holder.recordDate.setText(dateFormat.format(date));
        holder.recordTime.setText(timeFormat.format(date));

        //通过order表关联数据获取商品表的商品名称name
        AVObject record = AVObject.createWithoutData("Record", mRecordList.get(position).getObjectId());
        record.fetchInBackground("merchandiseId", new GetCallback<AVObject>() {
            @Override
            public void done(AVObject avObject, AVException e) {
                // 获取商品
                AVObject merchandise = avObject.getAVObject("merchandiseId");
                String name = merchandise.get("name") + "";
                String price = merchandise.get("sell")+"";
                holder.recordName.setText(name);
                holder.recordNum.setText("×"+mRecordList.get(position).get("recordNum"));
                //总价计算
                float sum = Float.parseFloat(mRecordList.get(position).get("recordNum").toString()) * Float.parseFloat(price);
                holder.recordSum.setText("￥"+String.format("%.2f", sum));
            }
        });
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
