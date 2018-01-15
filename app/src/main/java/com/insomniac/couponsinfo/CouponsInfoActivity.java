package com.insomniac.couponsinfo;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class CouponsInfoActivity extends SingleFragmentActivity {


    @Override
    protected Fragment createFragment() {
        return CouponsInfoFragment.newInstance();
    }
}
