package com.kk.sixsevensystemlc;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
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
                Log.d("jkcpressed","pressed");
                int position = holder.getAdapterPosition();
                AVObject merchandise = mMerchandiseList.get(position);
                Intent intent = new Intent(mContext,DetailActivity.class);
                intent.putExtra(DetailActivity.MERCHANDISE_NAME,
                        (CharSequence)merchandise.get("name"));
                intent.putExtra(DetailActivity.MERCHANDISE_IMAGE_URL,
                        mMerchandiseList.get(position).getAVFile("image").getUrl());
                mContext.startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.merchandisePrice.setText(mMerchandiseList.get(position).get("price")
                == null ? "￥" : "￥" + mMerchandiseList.get(position).get("price"));
        holder.merchandiseName.setText((CharSequence)mMerchandiseList.get(position).get("name"));
        //Log.d("urljkc",mMerchandiseList.get(position));
        //String url = "https://simg.open-open.com/show/a3dc23eb2ed7b79ce7ea71c8e29e6ded.png";
        Picasso.with(mContext).load(mMerchandiseList.get(position).getAVFile("image")
                == null ? "www" : mMerchandiseList.get(position).getAVFile("image").getUrl()).into(holder.merchandiseImage);
        //Picasso.with(mContext).load(url).into(holder.merchandiseImage);

        /*holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("jkcpressed",position + "pressed");
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return mMerchandiseList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView merchandiseImage;
        private TextView merchandiseName;
        private TextView merchandisePrice;
        private LinearLayout linearLayout;

        public ViewHolder(View itemView){
            super(itemView);
            merchandiseImage=(ImageView)itemView.findViewById(R.id.merchandise_image);
            merchandiseName=(TextView)itemView.findViewById(R.id.merchandise_name);
            merchandisePrice=(TextView)itemView.findViewById(R.id.merchandise_price);
            linearLayout= (LinearLayout)itemView.findViewById(R.id.merchandise_linearlayout);
        }
    }
}
