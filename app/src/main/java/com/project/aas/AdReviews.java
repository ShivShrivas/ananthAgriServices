package com.project.aas;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.project.aas.adapter.ReviewAdapter;
import com.project.aas.databinding.ActivityAdReviewsBinding;
import com.project.aas.model.AdReview;
import com.project.aas.model.Review;

import java.util.List;

public class AdReviews extends AppCompatActivity {

    private ActivityAdReviewsBinding binding;
    private RecyclerView mReviewRecyclerView;
    private List<Review> mReviewList;
    private AdReview mAdReviewObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAdReviewsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAdReviewObject = (AdReview) getIntent().getSerializableExtra("ReviewObject");
        mReviewList = mAdReviewObject.getReviewList();
        initRecyclerView();

        binding.btnReviewDetailBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    private void initRecyclerView() {
        mReviewRecyclerView = findViewById(R.id.review_rec_view);
        RecyclerView.LayoutManager reviewLayoutMgr = new LinearLayoutManager(this);

        mReviewRecyclerView.setHasFixedSize(true);
        mReviewRecyclerView.setNestedScrollingEnabled(false);
        mReviewRecyclerView.setLayoutManager(reviewLayoutMgr);

        ReviewAdapter adapter = new ReviewAdapter(mReviewList,this);
        mReviewRecyclerView.setAdapter(adapter);
    }
}