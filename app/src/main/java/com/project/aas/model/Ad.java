package com.project.aas.model;

import java.io.Serializable;

public class Ad implements Serializable {

    String id;
    String title;
    String datePosted;
    String location;
    String postedBy; // Seller Id
    String price;
    String sellerPhone;
    String imageUrl;
    String description;

    public Ad() {}

    public Ad(String id, String title, String description, String datePosted, String location, String postedBy, String price, String sellerPhone, String imageUrl) {
        this.id = id;
        this.title = title;
        this.datePosted = datePosted;
        this.location = location;
        this.postedBy = postedBy;
        this.price = price;
        this.sellerPhone = sellerPhone;
        this.imageUrl = imageUrl;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDatePosted() {
        return datePosted;
    }

    public void setDatePosted(String datePosted) {
        this.datePosted = datePosted;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPostedBy() {
        return postedBy;
    }

    public void setPostedBy(String postedBy) {
        this.postedBy = postedBy;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getSellerPhone() {
        return sellerPhone;
    }

    public void setSellerPhone(String sellerPhone) {
        this.sellerPhone = sellerPhone;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
