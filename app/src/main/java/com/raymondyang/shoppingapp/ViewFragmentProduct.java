package com.raymondyang.shoppingapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.raymondyang.shoppingapp.model.Product;

public class ViewFragmentProduct extends Fragment {

    public ImageView mImageView;
    private TextView mTitle;
    private TextView mPrice;

    public Product mProduct;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();


        if(bundle != null){
            mProduct = bundle.getParcelable("intent_product");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_pager, container, false);
        mImageView = view.findViewById(R.id.imageView10);
        mTitle = view.findViewById(R.id.title);
//        mPrice = view.findViewById(R.id.price);
        setProduct();

        return view;
    }

    private void setProduct() {
        RequestOptions requestOptions = new RequestOptions().placeholder(R.drawable.ic_image_1000);

        Glide.with(this).setDefaultRequestOptions(requestOptions)

                .load(mProduct.getImage())
                .into(mImageView);
        mTitle.setText(mProduct.getTitle());
//        mPrice.setText("22.5");


    }


}
