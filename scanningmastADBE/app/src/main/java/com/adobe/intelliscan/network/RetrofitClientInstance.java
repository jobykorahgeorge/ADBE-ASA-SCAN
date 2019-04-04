package com.adobe.intelliscan.network;

/**
 * Created by arun on 2/27/19.
 * Purpose -
 */
import com.adobe.intelliscan.App;
import com.adobe.intelliscan.utils.Preferences;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;


public class RetrofitClientInstance {

    private static Retrofit retrofit=null;
    private static Preferences preferences =new Preferences(App.getContext());
    private static final String BASE_URL = "";
//    private static final String BASE_URL = "http://192.168.1.16:3000";

    private static Gson gson = new GsonBuilder()
            .setLenient()
            .create();

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create(gson)).addConverterFactory(ScalarsConverterFactory.create()).build();
        }
        return retrofit;
    }
}
