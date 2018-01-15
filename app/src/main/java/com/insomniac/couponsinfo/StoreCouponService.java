package com.insomniac.couponsinfo;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Sanjeev on 1/15/2018.
 */

public class StoreCouponService {

    public static final String BASE_URL = "http://10.0.2.2:85/";
    private static Retrofit sRetrofit;

    public static Retrofit getRetrofitClient(){
        if(sRetrofit == null){
            sRetrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

        }

        return sRetrofit;
    }

}
