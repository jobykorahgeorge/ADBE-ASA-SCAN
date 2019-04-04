package com.adobe.intelliscan.network;

import com.adobe.intelliscan.model2.ProductResponse;
import com.adobe.intelliscan.models.ProductModel;

import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * Created by arun on 2/27/19.
 * Purpose -
 */
public interface GetDataService {

//    @FormUrlEncoded
    @POST("token/")
    Call<String> userLogin(
            @Query("username") String email,
            @Query("password") String password
    );

    @GET
    Call<ProductModel> loadData(@Url String url

    );


    @GET
    Call<ProductResponse> loadData1(@Url String url

    );

//    @FormUrlEncoded
//    @Multipart
//    @POST("addToCart.php")
//    Call<String> addToCart(
//            @PartMap Map<String,RequestBody> params
//           /* @Url String url,*/
//           /*@Part("sku") RequestBody sku,
//           @Part("username") RequestBody username,
//           @Part("password") RequestBody password*/
//
//    );
}
