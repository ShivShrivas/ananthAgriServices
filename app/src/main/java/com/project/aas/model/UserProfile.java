package com.project.aas.model;

import java.util.List;

public class UserProfile {

    String id,name,phoneNumber,email, profilePictureUrl, contactInformation,description;
    List<String> publishedAds;

    public UserProfile(String id, String name, String phoneNumber, String email, String profilePictureUrl, String contactInformation, String description, List<String> publishedAds) {
        this.name = name;
        this.id = id;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.profilePictureUrl = profilePictureUrl;
        this.contactInformation = contactInformation;
        this.description = description;
        this.publishedAds = publishedAds;
    }

    public UserProfile() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }

    public void setProfilePictureUrl(String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
    }

    public String getContactInformation() {
        return contactInformation;
    }

    public void setContactInformation(String contactInformation) {
        this.contactInformation = contactInformation;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getPublishedAds() {
        return publishedAds;
    }

    public void setPublishedAds(List<String> publishedAds) {
        this.publishedAds = publishedAds;
    }
}
