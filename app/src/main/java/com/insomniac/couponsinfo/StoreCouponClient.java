package com.insomniac.couponsinfo;



import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Sanjeev on 1/15/2018.
 */

public interface StoreCouponClient {

    @GET("coupons/")
    Observable<StoreCoupons> getCoupons(@Query("status") String status);

    @GET("storeOffers/")
    Observable<StoreCoupons> getStoreInfo();

}
