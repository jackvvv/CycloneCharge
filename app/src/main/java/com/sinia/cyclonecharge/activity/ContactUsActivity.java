package com.sinia.cyclonecharge.activity;

import android.os.Bundle;
import android.view.View;

import com.sinia.cyclonecharge.R;
import com.sinia.cyclonecharge.base.BaseActivity;

/**
 * Created by newLamp on 2016/11/4.
 */

public class ContactUsActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us,"联系我们");
        getDoingView().setVisibility(View.GONE);
    }
}
