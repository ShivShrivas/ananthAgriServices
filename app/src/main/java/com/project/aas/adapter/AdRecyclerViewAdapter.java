package com.project.aas.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.project.aas.R;
import com.project.aas.model.Ad;

import java.util.List;


public class AdRecyclerViewAdapter extends RecyclerView.Adapter<AdRecyclerViewAdapter.ViewHolder>{

    private final List<Ad> mAdsList;
    private final Context mContext;
    private static String TAG = "AdRecyclerViewAdapter";

    public AdRecyclerViewAdapter(List<Ad> mAdsList, Context context) {
        this.mAdsList = mAdsList;
        this.mContext = context;
    }

    @NonNull
    @Override
    public AdRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_ad,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdRecyclerViewAdapter.ViewHolder holder, int position) {
        Glide.with(mContext)
                .load(mAdsList.get(position).getImageUrl())
                .placeholder(R.drawable.ic_baseline_broken_image_24)
                .into(holder.img);

        Log.i(TAG, "onBindViewHolder: Loaded Image #"+position);

        holder.title.setText(mAdsList.get(position).getTitle());
        holder.location.setText(mAdsList.get(position).getLocation());
        holder.postedBy.setText(mAdsList.get(position).getPostedBy());
        holder.datePosted.setText(mAdsList.get(position).getDatePosted());
        holder.price.setText(mAdsList.get(position).getPrice());
    }

    @Override
    public int getItemCount() {
        Log.i(TAG, "getItemCount: "+ mAdsList.size());
        return mAdsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView img;
        TextView title,postedBy,datePosted,location,price;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.iv_ad_image);
            title = itemView.findViewById(R.id.tv_ad_title);
            postedBy = itemView.findViewById(R.id.tv_posted_by);
            datePosted = itemView.findViewById(R.id.tv_date_posted);
            location = itemView.findViewById(R.id.tv_location);
            price = itemView.findViewById(R.id.tv_price);
        }
    }
}
