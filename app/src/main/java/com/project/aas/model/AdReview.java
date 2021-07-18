package com.project.aas.model;

import java.io.Serializable;
import java.util.List;

public class AdReview implements Serializable {
    double ratingValue;
    String numStars, numVotes;
    List<Review> reviewList;

    public List<Review> getReviewList() {
        return reviewList;
    }

    public void setReviewList(List<Review> reviewList) {
        this.reviewList = reviewList;
    }

    public AdReview(double ratingValue, String numStars, String numVotes) {
        this.ratingValue = ratingValue;
        this.numStars = numStars;
        this.numVotes = numVotes;
    }

    public AdReview() {}

    public double getRatingValue() {
        return ratingValue;
    }

    public void setRatingValue(double ratingValue) {
        this.ratingValue = ratingValue;
    }

    public String getNumStars() {
        return numStars;
    }

    public void setNumStars(String numStars) {
        this.numStars = numStars;
    }

    public String getNumVotes() {
        return numVotes;
    }

    public void setNumVotes(String numVotes) {
        this.numVotes = numVotes;
    }
}
