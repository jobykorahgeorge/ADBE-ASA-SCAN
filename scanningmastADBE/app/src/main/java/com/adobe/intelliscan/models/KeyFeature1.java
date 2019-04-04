package com.adobe.intelliscan.models;

import java.util.List;

/**
 * Created by arun on 3/11/19.
 * Purpose -
 */
public class KeyFeature1 {

    private List<String> category = null;

    private List<String> categoryFeatures = null;

    public List<String> getCategory() {
        return category;
    }

    public void setCategory(List<String> category) {
        this.category = category;
    }

    public List<String> getCategoryFeatures() {
        return categoryFeatures;
    }

    public void setCategoryFeatures(List<String> categoryFeatures) {
        this.categoryFeatures = categoryFeatures;
    }

}
