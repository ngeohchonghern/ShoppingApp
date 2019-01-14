package com.raymondyang.shoppingapp;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.raymondyang.shoppingapp.model.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {

    public static final String TAG = RecyclerAdapter.class.getSimpleName();
    private List<Product> mProductList;
    private Context mContext;
    public RecyclerAdapter(Context context, List<Product> productList) {
        mProductList = productList;
        mContext = context;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        Log.i(TAG, "onCreateViewHolder()");
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.image_item, parent, false );
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int position) {
        Log.i(TAG, "onBindViewHolder()");

        final RequestOptions requestOptions = new RequestOptions().placeholder(R.drawable.ic_launcher_background);
        Glide.with(mContext)
                .setDefaultRequestOptions(requestOptions);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Glide.with(mContext)
                        .load(mProductList.get(position).getImage())
                        .into(myViewHolder.mImageView);
            }
        };
        android.os.Handler handler = new android.os.Handler();
        handler.postDelayed(runnable, 0);

        myViewHolder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ViewProductActivity.class);
                intent.putExtra("intent_product", mProductList.get(position));
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                mContext.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return mProductList.size();

    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView mImageView;
        CardView mCardView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.imageView);
            mCardView = itemView.findViewById(R.id.cardview);
        }
    }
}
