package com.adobe.intelliscan.model2;

/**
 * Created by arun on 3/11/19.
 * Purpose -
 */

import java.util.List;

import com.adobe.intelliscan.models.Color;
import com.adobe.intelliscan.models.SimilarProduct;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductResponse {

    @SerializedName("Product Title")
    @Expose
    private String productTitle;
    @SerializedName("Ratings")
    @Expose
    private String ratings;
    @SerializedName("videoPath")
    @Expose
    private String videoPath;
    @SerializedName("Key Features")
    @Expose
    private List<KeyFeature> keyFeatures = null;
    @SerializedName("Specifications")
    @Expose
    private List<String> specifications = null;
    @SerializedName("Similar Products")
    @Expose
    private List<SimilarProduct> similarProducts = null;
    @SerializedName("Colors")
    @Expose
    private List<Color> colors = null;
    @SerializedName("reviews")
    @Expose
    private List<Review> reviews = null;

    public String getProductTitle() {
        return productTitle;
    }

    public void setProductTitle(String productTitle) {
        this.productTitle = productTitle;
    }

    public String getRatings() {
        return ratings;
    }

    public void setRatings(String ratings) {
        this.ratings = ratings;
    }

    public String getVideoPath() {
        return videoPath;
    }

    public void setVideoPath(String videoPath) {
        this.videoPath = videoPath;
    }

    public List<KeyFeature> getKeyFeatures() {
        return keyFeatures;
    }

    public void setKeyFeatures(List<KeyFeature> keyFeatures) {
        this.keyFeatures = keyFeatures;
    }

    public List<String> getSpecifications() {
        return specifications;
    }

    public void setSpecifications(List<String> specifications) {
        this.specifications = specifications;
    }

    public List<SimilarProduct> getSimilarProducts() {
        return similarProducts;
    }

    public void setSimilarProducts(List<SimilarProduct> similarProducts) {
        this.similarProducts = similarProducts;
    }

    public List<Color> getColors() {
        return colors;
    }

    public void setColors(List<Color> colors) {
        this.colors = colors;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

}
