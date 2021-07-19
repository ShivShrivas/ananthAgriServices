package com.project.aas.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.aas.R;
import com.project.aas.model.Review;

import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder>{

    private List<Review> mReviews;
    private Context mContext;

    public ReviewAdapter(List<Review> mReviews,Context mContext) {
        this.mContext = mContext;
        this.mReviews = mReviews;
    }

    @NonNull
    @Override
    public ReviewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_row,parent,false);
        return new ReviewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewAdapter.ViewHolder holder, int position) {
        holder.name.setText(mReviews.get(position).getAuthorName());
        holder.comment.setText(mReviews.get(position).getComment());
        holder.ratingBar.setRating((float) mReviews.get(position).getRatingValue());
        //holder.stars.setText(String.valueOf(mReviews.get(position).getRatingValue()));
    }

    @Override
    public int getItemCount() {
        return mReviews.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name,comment;
        RatingBar ratingBar;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tv_reviewer_name);
            comment = itemView.findViewById(R.id.tv_reviewer_comment);
            // stars = itemView.findViewById(R.id.tv_reviewer_star_count);
            ratingBar = itemView.findViewById(R.id.ratingbar_reviews);
        }
    }
}
