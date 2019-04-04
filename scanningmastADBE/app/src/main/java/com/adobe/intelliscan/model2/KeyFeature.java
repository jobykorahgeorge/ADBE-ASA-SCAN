package com.adobe.intelliscan.model2;

/**
 * Created by arun on 3/11/19.
 * Purpose -
 */

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class KeyFeature {

    @SerializedName("category")
    @Expose
    private String category;
    @SerializedName("category-features")
    @Expose
    private List<String> categoryFeatures = null;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public List<String> getCategoryFeatures() {
        return categoryFeatures;
    }

    public void setCategoryFeatures(List<String> categoryFeatures) {
        this.categoryFeatures = categoryFeatures;
    }

}