package com.project.aas.model;

import com.google.firebase.auth.FirebaseAuth;

import java.io.Serializable;
import java.util.List;

public class SavedAdsModel implements Serializable {

    String id;
    String title;
    String datePosted; // Autogenerate
    String location;
    String postedBy; // Seller Id
    String price;
    String sellerPhone;
    List<String> imageUrls;
    String description;
    String category;
    String subCategory;
    String pinCode;
    String priceType;
    String priceUnit;
    String sellerWhatsapp;
    String adType;

    public SavedAdsModel() {}

    public SavedAdsModel(String id, String title, String datePosted, String location, String postedBy, String price, String sellerPhone, List<String> imageUrls, String description, String category, String subCategory, String pinCode, String priceType, String priceUnit, String sellerWhatsapp, String adType) {
        this.id = id;
        this.title = title;
        this.datePosted = datePosted;
        this.location = location;
        this.postedBy = postedBy;
        this.price = price;
        this.sellerPhone = sellerPhone;
        this.imageUrls = imageUrls;
        this.description = description;
        this.category = category;
        this.subCategory = subCategory;
        this.pinCode = pinCode;
        this.priceType = priceType;
        this.priceUnit = priceUnit;
        this.sellerWhatsapp = sellerWhatsapp;
        this.adType = adType;
    }

    public String getAdId(){
        return id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
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
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
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

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
    }

    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }

    public String getPriceType() {
        return priceType;
    }

    public void setPriceType(String priceType) {
        this.priceType = priceType;
    }

    public String getPriceUnit() {
        return priceUnit;
    }

    public void setPriceUnit(String priceUnit) {
        this.priceUnit = priceUnit;
    }

    public String getSellerWhatsapp() {
        return sellerWhatsapp;
    }

    public void setSellerWhatsapp(String sellerWhatsapp) {
        this.sellerWhatsapp = sellerWhatsapp;
    }

    public String getAdType() {
        return adType;
    }

    public void setAdType(String adType) {
        this.adType = adType;
    }
}
