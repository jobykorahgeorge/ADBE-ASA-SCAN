package com.adobe.intelliscan.models;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SimilarProduct {

    @SerializedName("Title")
    @Expose
    private String title;
    @SerializedName("Link")
    @Expose
    private String link;
    @SerializedName("Image")
    @Expose
    private String image;
    @SerializedName("Price")
    @Expose
    private String price;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

}