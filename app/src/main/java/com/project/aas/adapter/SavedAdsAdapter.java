package com.project.aas.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.project.aas.AdDetail;
import com.project.aas.R;
import com.project.aas.model.AdPost;
import com.project.aas.model.SavedAdsModel;
import com.project.aas.ui.slideshow.SavedAds;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SavedAdsAdapter extends FirebaseRecyclerAdapter<AdPost,SavedAdsAdapter.ViewHolder> {

    Context context;
    public SavedAdsAdapter(Context context,FirebaseRecyclerOptions<AdPost> options) {
        super(options);
        this.context=context;
    }

    @Override
    protected void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position, @NonNull @NotNull AdPost model) {
        Glide.with(holder.img).load(model.getImageUrls().get(0))
                .into(holder.img);
        holder.title.setText(model.getTitle());
        holder.location.setText(model.getLocation());
        holder.postedBy.setText(model.getPostedBy());
        holder.datePosted.setText("Posted On "+model.getDatePosted());
        holder.price.setText("Rs. " + model.getPrice());
        SavedAds.progressDialog.dismiss();
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AdDetail.class);
                intent.putExtra("AdObject",model);
                context.startActivity(intent);
            }
        });
    }



    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.row_ad,parent,false);
        return new ViewHolder(view);
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
