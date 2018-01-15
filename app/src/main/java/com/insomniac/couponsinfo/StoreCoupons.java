package com.insomniac.couponsinfo;

import java.util.List;

/**
 * Created by Sanjeev on 1/15/2018.
 */

public class StoreCoupons {

    private String store;
    private String totalCoupons;

    public String getStore() {
        return store;
    }

    public void setStore(String store) {
        this.store = store;
    }

    public String getTotalCoupons() {
        return totalCoupons;
    }

    public void setTotalCoupons(String totalCoupons) {
        this.totalCoupons = totalCoupons;
    }

    public String getMaxCashBack() {
        return maxCashBack;
    }

    public void setMaxCashBack(String maxCashBack) {
        this.maxCashBack = maxCashBack;
    }

    public List<Coupon> getCoupons() {
        return coupons;
    }

    public void setCoupons(List<Coupon> coupons) {
        this.coupons = coupons;
    }

    private String maxCashBack;
    private List<Coupon> coupons;


}
