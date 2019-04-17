package com.example.anggarisky.fidashboard.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.anggarisky.fidashboard.model.DemoItem;
import com.example.anggarisky.fidashboard.R;
import com.squareup.picasso.Picasso;

import java.util.List;



public class CommonAdapter extends RecyclerView.Adapter {
    List<DemoItem> demoItems;
    Context context;

    public CommonAdapter(List<DemoItem> demoItems, Context context) {
        this.demoItems = demoItems;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View row=inflater.inflate(R.layout.custom_row, parent, false);
        return new DemoItemHolder(row);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        DemoItem currentDemoItem = demoItems.get(position);
        DemoItemHolder demoItemHolder = (DemoItemHolder) holder;
        demoItemHolder.Title.setText(currentDemoItem.title);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
//        Picasso.get().load(currentDemoItem.imageUrl).placeholder(R.drawable.mascot1).centerCrop().resize(displayMetrics.widthPixels, displayMetrics.heightPixels / 3).into(demoItemHolder.Thumbnail);
        demoItemHolder.Thumbnail.setImageResource(currentDemoItem.imageUrl);

    }

    @Override
    public int getItemCount() {
        return demoItems.size();
    }

    public class DemoItemHolder extends RecyclerView.ViewHolder {
        ImageView Thumbnail;
        TextView Title;

        public DemoItemHolder(View itemView) {
            super(itemView);
            Thumbnail = itemView.findViewById(R.id.imageViewThumbnail);
            Title = itemView.findViewById(R.id.textViewTitle);
        }
    }
}
