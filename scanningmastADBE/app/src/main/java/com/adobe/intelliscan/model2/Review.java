package com.adobe.intelliscan.model2;

/**
 * Created by arun on 3/11/19.
 * Purpose -
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Review {

    @SerializedName("userName")
    @Expose
    private String userName;
    @SerializedName("userReview")
    @Expose
    private String userReview;
    @SerializedName("postDate")
    @Expose
    private String postDate;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserReview() {
        return userReview;
    }

    public void setUserReview(String userReview) {
        this.userReview = userReview;
    }

    public String getPostDate() {
        return postDate;
    }

    public void setPostDate(String postDate) {
        this.postDate = postDate;
    }

}