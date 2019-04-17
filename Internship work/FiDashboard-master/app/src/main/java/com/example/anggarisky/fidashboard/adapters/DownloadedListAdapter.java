package com.example.anggarisky.fidashboard.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.anggarisky.fidashboard.R;
import com.example.anggarisky.fidashboard.model.Item;
import com.example.anggarisky.fidashboard.ui.WebActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CartListAdapter extends RecyclerView.Adapter<CartListAdapter.MyViewHolder> {
    private Context context;
    private List<Item> cartList;
    private int arrayInt[] = new int[]{R.drawable.class_1, R.drawable.class_2, R.drawable.class_3, R.drawable.class_4, R.drawable.class_5, R.drawable.class_6, R.drawable.class_7, R.drawable.class_8, R.drawable.class_9, R.drawable.class_10, R.drawable.class_11, R.drawable.class_12, R.drawable.class_13, R.drawable.class_14, R.drawable.class_15, R.drawable.class_16, R.drawable.class_17, R.drawable.class_18, R.drawable.class_19, R.drawable.class_20, R.drawable.class_21, R.drawable.class_22, R.drawable.class_23, R.drawable.class_24, R.drawable.class_25, R.drawable.class_26, R.drawable.class_27, R.drawable.class_28, R.drawable.class_29, R.drawable.class_30};

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, description;
        public ImageView thumbnail;
        public RelativeLayout viewForeground;

        public MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            thumbnail = view.findViewById(R.id.thumbnail);
            viewForeground = view.findViewById(R.id.view_foreground);
        }
    }


    public CartListAdapter(Context context, List<Item> cartList) {
        this.context = context;
        this.cartList = cartList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_row_demo, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final Item item = cartList.get(position);
        holder.name.setText(item.getName().split(":", 2)[1]);
        holder.thumbnail.setImageResource(arrayInt[position]);
        holder.viewForeground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, WebActivity.class);
                i.putExtra("urlStart","https://manisoni28.github.io/");
                i.putExtra("url",item.getUrl());
                i.putExtra("chapterName",item.getName());
                context.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }


}