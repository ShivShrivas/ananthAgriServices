package com.project.aas.model;

import java.io.Serializable;

public class Review implements Serializable {

    double ratingValue;
    String authorName, comment, authorId;

    public Review(double ratingValue, String authorName, String comment, String authorId) {
        this.ratingValue = ratingValue;
        this.authorName = authorName;
        this.comment = comment;
        this.authorId = authorId;
    }

    public Review() {
    }

    public double getRatingValue() {
        return ratingValue;
    }

    public void setRatingValue(double ratingValue) {
        this.ratingValue = ratingValue;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }
}
