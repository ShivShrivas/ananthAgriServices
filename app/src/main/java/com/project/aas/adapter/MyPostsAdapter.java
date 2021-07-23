package com.project.aas.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.project.aas.AdDetail;
import com.project.aas.R;
import com.project.aas.model.AdPost;
import com.project.aas.ui.slideshow.MyOrders;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MyPostsAdapter extends RecyclerView.Adapter<MyPostsAdapter.ViewHolder>{

    private final List<AdPost> myAdsList;
    private final Context mContext;
    private static String TAG = "AdRecyclerViewAdapter";

    public MyPostsAdapter(List<AdPost> mAdsList, Context context) {
        this.myAdsList = mAdsList;
        this.mContext = context;
    }
    @NonNull
    @Override
    public MyPostsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_ad,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {


        Glide.with(mContext)
                .load(myAdsList.get(position).getImageUrls().get(0))
                .placeholder(R.drawable.ic_baseline_broken_image_24)
                .into(holder.img);

        Log.i(TAG, "onBindViewHolder: Loaded Image #"+position);

        holder.title.setText(myAdsList.get(position).getTitle());
        holder.location.setText(myAdsList.get(position).getLocation());
        holder.postedBy.setText(myAdsList.get(position).getPostedBy());
        holder.datePosted.setText("Posted On "+myAdsList.get(position).getDatePosted());
        holder.price.setText("Rs. " + myAdsList.get(position).getPrice());

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, AdDetail.class);
                intent.putExtra("AdObject",myAdsList.get(position));
                mContext.startActivity(intent);
            }
        });

    }
    @Override
    public int getItemCount() {
        return myAdsList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView img;
        ConstraintLayout layout;
        TextView title,postedBy,datePosted,location,price;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.iv_ad_image);
            title = itemView.findViewById(R.id.tv_ad_title);
            postedBy = itemView.findViewById(R.id.tv_posted_by);
            datePosted = itemView.findViewById(R.id.tv_date_posted);
            location = itemView.findViewById(R.id.tv_location);
            price = itemView.findViewById(R.id.tv_price);
            layout = itemView.findViewById(R.id.row_layout);
        }
    }
}
