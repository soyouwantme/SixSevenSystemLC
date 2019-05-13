package com.kk.sixsevensystemlc;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.avos.avoscloud.AVObject;

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
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.merchandise_item,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.merchandisePrice.setText(mMerchandiseList.get(position).get("price") == null ? "￥" : "￥" + mMerchandiseList.get(position).get("price"));
        holder.merchandiseName.setText((CharSequence)mMerchandiseList.get(position).get("name"));
        //Picasso.with(mContext).load(mList.get(position).getAVFile("image") == null ? "www" : mList.get(position).getAVFile("image").getUrl()).transform(new RoundedTransformation(9, 0)).into(holder.mPicture);
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("jkcpressed",position + "pressed");
            }
        });
    }

    @Override
    public int getItemCount() {
        return mMerchandiseList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        //ImageView merchandiseImage;
        private TextView merchandiseName;
        private TextView merchandisePrice;
        private LinearLayout linearLayout;

        public ViewHolder(View itemView){
            super(itemView);
            //merchandiseImage=(ImageView)itemView.findViewById(R.id.merchandise_image);
            merchandiseName=(TextView)itemView.findViewById(R.id.merchandise_name);
            merchandisePrice=(TextView)itemView.findViewById(R.id.merchandise_price);
            linearLayout= (LinearLayout)itemView.findViewById(R.id.merchandise_linearlayout);
        }
    }
}
