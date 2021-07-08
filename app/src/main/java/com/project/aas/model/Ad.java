package com.project.aas.model;

public class Ad {

    String id;
    String title;
    String datePosted;
    String location;
    String postedBy;
    String price;
    String sellerPhone;
    String imageUrl;

    public Ad() {}

    public Ad(String id, String title, String datePosted, String location, String postedBy, String price, String sellerPhone, String imageUrl) {
        this.id = id;
        this.title = title;
        this.datePosted = datePosted;
        this.location = location;
        this.postedBy = postedBy;
        this.price = price;
        this.sellerPhone = sellerPhone;
        this.imageUrl = imageUrl;
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
