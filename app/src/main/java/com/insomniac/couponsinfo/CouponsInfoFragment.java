package com.insomniac.couponsinfo;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Sanjeev on 1/15/2018.
 */

public class CouponsInfoFragment extends Fragment {

    public static Fragment newInstance(){
        return new CouponsInfoFragment();
    }

    public static final String BASE_URL = "http://10.0.2.2:85/";
    private CompositeDisposable mCompositeDisposable;
    private RecyclerView mCouponsRecyclerView;
    private CouponsAdapter mCouponsAdapter;
    private Retrofit mRetrofit;
    private Button mTopCouponButton;
    private Button mCouponButton;
    private List<Coupon> mCouponList = new ArrayList<>();
    private TextView mStoreNameTextView;
    private TextView mCoupoCountTextView;
    private TextView mMaxCashBackTextView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        mRetrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_coupons_info,container,false);

        mCouponsRecyclerView = (RecyclerView) view.findViewById(R.id.coupon_rv);
        mCouponsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mTopCouponButton = (Button) view.findViewById(R.id.show_coupons_topstore);
        mTopCouponButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getStoreCouponData();
            }
        });

        mCouponButton = (Button) view.findViewById(R.id.show_coupons);
        mCouponButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getCouponData();
            }
        });

        mStoreNameTextView = (TextView) view.findViewById(R.id.store_name);
        mCoupoCountTextView = (TextView) view.findViewById(R.id.coupon_count);
        mMaxCashBackTextView = (TextView) view.findViewById(R.id.max_cashback);

        return view;
    }

    private void getStoreCouponData(){

        Observable.just(mRetrofit.create(StoreCouponClient.class)).subscribeOn(Schedulers.computation())
                .flatMap(s -> {
                    Observable<StoreCoupons> couponsObservable = s.getCoupons("topcoupons").subscribeOn(Schedulers.io());

                    Observable<StoreCoupons> storeCouponsObservable = s.getStoreInfo().subscribeOn(Schedulers.io());

                    return Observable.concatArray(couponsObservable,storeCouponsObservable);
                }).observeOn(AndroidSchedulers.mainThread()).subscribe(this::handleResults,this::handleError);
    }

    private void getCouponData(){

        mRetrofit.create(StoreCouponClient.class).getCoupons("topcoupons")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handleResults,this::handleError);
    }

    private void handleResults(StoreCoupons storeCoupons){
        if(storeCoupons != null){
           setAdapter(storeCoupons.getCoupons());
        }else {
            mStoreNameTextView.setText(storeCoupons.getStore());
            mCoupoCountTextView.setText(storeCoupons.getTotalCoupons());
            mMaxCashBackTextView.setText(storeCoupons.getMaxCashBack());
        }
    }

    private void setAdapter(List<Coupon> couponList){
        Toast.makeText(getActivity(),"Size = " + couponList.size(),Toast.LENGTH_SHORT).show();
        if(mCouponsAdapter == null){
            Toast.makeText(getActivity(),"if",Toast.LENGTH_SHORT).show();
            mCouponsAdapter = new CouponsAdapter(couponList);
            mCouponsRecyclerView.setAdapter(mCouponsAdapter);
        }else{
            Toast.makeText(getActivity(),"else",Toast.LENGTH_SHORT).show();
            mCouponsAdapter.setCoupons(couponList);
        }
    }

    private void handleError(Throwable throwable){
        Toast.makeText(getActivity(),"size1 " + mCouponList.size(),Toast.LENGTH_SHORT).show();
        Toast.makeText(getActivity(), "ERROR IN GETTING COUPONS" + throwable, Toast.LENGTH_LONG).show();
    }

    private class CouponsHolder extends RecyclerView.ViewHolder{

        public Coupon mCoupon;

        public TextView mStoreTextView;
        public TextView mCouponTextView;
        public TextView mExpiryDateTextView;
        public TextView mCodeTextView;

        public CouponsHolder(View itemView){
            super(itemView);

            mStoreTextView = (TextView) itemView.findViewById(R.id.store);
            mCouponTextView = (TextView) itemView.findViewById(R.id.coupon);
            mExpiryDateTextView = (TextView) itemView.findViewById(R.id.expiry);
            mCodeTextView = (TextView) itemView.findViewById(R.id.coupon_code);
        }

        public void bindHolder(Coupon coupon){
            mCoupon = coupon;
            mStoreTextView.setText(mCoupon.getStore());
            mCouponTextView.setText(mCoupon.getCoupon());
            mExpiryDateTextView.setText(mCoupon.getExpiryDate());
            mCodeTextView.setText(mCoupon.getCouponCode());
        }
    }

    private class CouponsAdapter extends RecyclerView.Adapter<CouponsHolder>{

        List<Coupon> mCouponList = new ArrayList<>();

        public CouponsAdapter(List<Coupon> couponList){
            mCouponList = couponList;
        }

        @Override
        public CouponsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.coupon_item,parent,false);
            return new CouponsHolder(view);
        }

        @Override
        public void onBindViewHolder(CouponsHolder holder, int position) {
            Coupon coupon = mCouponList.get(position);
            holder.bindHolder(coupon);
        }

        @Override
        public int getItemCount() {
            Toast.makeText(getActivity(),"size0 " + mCouponList.size(),Toast.LENGTH_SHORT).show();
            return mCouponList.size();
        }

        public void setCoupons(List<Coupon> coupons){
            mCouponList = coupons;
            notifyDataSetChanged();
            Toast.makeText(getActivity(),"setCoupons " + mCouponList.size(),Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroy() {
        if(mCompositeDisposable != null && !mCompositeDisposable.isDisposed())
            mCompositeDisposable.clear();
        super.onDestroy();
    }
}
