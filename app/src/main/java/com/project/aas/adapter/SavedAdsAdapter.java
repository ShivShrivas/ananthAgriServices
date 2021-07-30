package com.project.aas.adapter;

import android.content.Context;
import android.content.Intent;
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
import com.project.aas.model.SavedAdsModel;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SavedAdsAdapter extends RecyclerView.Adapter<SavedAdsAdapter.ViewHolder>{

    private final List<SavedAdsModel> savedAdsList;
    private final Context mContext;

    public SavedAdsAdapter(List<SavedAdsModel> savedadsList, Context context) {
        this.savedAdsList = savedadsList;
        this.mContext = context;
    }


    @NonNull
    @Override
    public SavedAdsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_ad,parent,false);
        return new SavedAdsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        Glide.with(mContext)
                .load(savedAdsList.get(position).getImageUrls().get(0))
                .placeholder(R.drawable.ic_baseline_broken_image_24)
                .into(holder.img);

        holder.title.setText(savedAdsList.get(position).getTitle());
        holder.location.setText(savedAdsList.get(position).getLocation());
        holder.postedBy.setText(savedAdsList.get(position).getPostedBy());
        holder.datePosted.setText("Posted On "+savedAdsList.get(position).getDatePosted());
        holder.price.setText("Rs. " + savedAdsList.get(position).getPrice());

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, AdDetail.class);
                intent.putExtra("AdObject",savedAdsList.get(position));
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return savedAdsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
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
