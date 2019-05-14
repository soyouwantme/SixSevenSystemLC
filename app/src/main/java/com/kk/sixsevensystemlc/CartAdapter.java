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

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    private Context mContext;
    private List<AVObject> mCartList;

    public CartAdapter(List<AVObject> cartList,Context context){
        mCartList = cartList;
        mContext = context;
    }

    @Override
    public CartAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cart_item,parent,false);
        CartAdapter.ViewHolder holder = new CartAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.ViewHolder holder, final int position) {
        holder.cartPrice.setText(mCartList.get(position).get("price") == null ? "￥" : "￥" + mCartList.get(position).get("price"));
        holder.cartName.setText((CharSequence)mCartList.get(position).get("name"));
        //Picasso.with(mContext).load(mList.get(position).getAVFile("image") == null ? "www" : mList.get(position).getAVFile("image").getUrl()).transform(new RoundedTransformation(9, 0)).into(holder.mPicture);
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("jkccartpressed",position + "pressed");
            }
        });
    }

    @Override
    public int getItemCount() {
        return mCartList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        //ImageView merchandiseImage;
        private TextView cartName;
        private TextView cartPrice;
        private LinearLayout linearLayout;

        public ViewHolder(View itemView){
            super(itemView);
            //merchandiseImage=(ImageView)itemView.findViewById(R.id.merchandise_image);
            cartName=(TextView)itemView.findViewById(R.id.cart_name);
            cartPrice=(TextView)itemView.findViewById(R.id.cart_price);
            linearLayout= (LinearLayout)itemView.findViewById(R.id.cart_linearlayout);
        }
    }
}
