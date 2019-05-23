package com.kk.sixsevensystemlc.restock;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.GetCallback;
import com.kk.sixsevensystemlc.R;
import com.squareup.picasso.Picasso;

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
    public void onBindViewHolder(@NonNull final CartAdapter.ViewHolder holder, final int position) {

        //通过order表关联数据获取商品表的商品名称name
        AVObject cart = AVObject.createWithoutData("Cart", mCartList.get(position).getObjectId());
        cart.fetchInBackground("merchandiseId", new GetCallback<AVObject>() {
            @Override
            public void done(AVObject avObject, AVException e) {
                // 获取商品
                AVObject merchandise = avObject.getAVObject("merchandiseId");
                String name = merchandise.get("name") + "";
                String price = merchandise.get("price")+"";
                String num = mCartList.get(position).get("cartNum")+"";
                String imgUrl = merchandise.getAVFile("image").getUrl();
                Picasso.with(mContext).load(imgUrl).into(holder.cartImage);
                holder.cartName.setText(name);
                holder.cartPrice.setText(price);
                holder.cartNum.setText(num);

            }
        });

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
        private ImageView cartImage;
        private TextView cartName;
        private TextView cartPrice;
        private TextView cartNum;

        private LinearLayout linearLayout;

        public ViewHolder(View itemView){
            super(itemView);
            cartImage=(ImageView)itemView.findViewById(R.id.cart_image);
            cartName=(TextView)itemView.findViewById(R.id.cart_name);
            cartPrice=(TextView)itemView.findViewById(R.id.cart_price);
            cartNum=itemView.findViewById(R.id.cart_num);
            linearLayout= (LinearLayout)itemView.findViewById(R.id.cart_linearlayout);
        }
    }
}
