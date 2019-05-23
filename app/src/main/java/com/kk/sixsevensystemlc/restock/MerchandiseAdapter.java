package com.kk.sixsevensystemlc.restock;

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
import com.kk.sixsevensystemlc.DetailActivity;
import com.kk.sixsevensystemlc.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MerchandiseAdapter extends RecyclerView.Adapter<MerchandiseAdapter.ViewHolder> {

    private Context mContext;
    private List<AVObject> mMerchandiseList;

    public MerchandiseAdapter(List<AVObject> merchandiseList,Context context){
        mMerchandiseList =merchandiseList;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(mContext == null){
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.merchandise_item,parent,false);
        final ViewHolder holder = new ViewHolder(view);
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log.d("jkcpressed","pressed");
                int position = holder.getAdapterPosition();
                AVObject merchandise = mMerchandiseList.get(position);
                Intent intent = new Intent(mContext, DetailActivity.class);
                Object merchandiseId = merchandise.get("merchandiseId");
                intent.putExtra(DetailActivity.OBJECT_ID,merchandise.getObjectId());
                intent.putExtra(DetailActivity.MERCHANDISE_NAME,(CharSequence)merchandise.get("name"));
                intent.putExtra(DetailActivity.MERCHANDISE_RATE,(CharSequence)merchandise.get("rate"));
                Object PRICE = merchandise.get("price");
                intent.putExtra(DetailActivity.MERCHANDISE_PRICE,Double.parseDouble(PRICE.toString()));
                intent.putExtra(DetailActivity.MERCHANDISE_IMAGE_URL,mMerchandiseList.get(position).getAVFile("image").getUrl());
                intent.putExtra(DetailActivity.MERCHANDISE_DETAIL,(CharSequence)merchandise.get("detail"));
                mContext.startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Picasso.with(mContext).load(mMerchandiseList.get(position).getAVFile("image").getUrl()).into(holder.merchandiseImage);
        holder.merchandiseId.setText("货号："+mMerchandiseList.get(position).getObjectId());
        holder.merchandiseName.setText("名称："+mMerchandiseList.get(position).get("name"));
        holder.merchandisePrice.setText("进价：￥" + mMerchandiseList.get(position).get("price"));
    }

    @Override
    public int getItemCount() {
        return mMerchandiseList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView merchandiseImage;
        private TextView merchandiseId;
        private TextView merchandiseName;
        private TextView merchandisePrice;
        private LinearLayout linearLayout;

        public ViewHolder(View itemView){
            super(itemView);
            merchandiseImage=(ImageView)itemView.findViewById(R.id.merchandise_image);
            merchandiseId = (TextView)itemView.findViewById(R.id.merchandise_id);
            merchandiseName=(TextView)itemView.findViewById(R.id.merchandise_name);
            merchandisePrice=(TextView)itemView.findViewById(R.id.merchandise_price);
            linearLayout= (LinearLayout)itemView.findViewById(R.id.merchandise_linearlayout);
        }
    }
}
