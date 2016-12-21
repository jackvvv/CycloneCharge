package com.sinia.cyclonecharge.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.sinia.cyclonecharge.R;
import com.sinia.cyclonecharge.base.BaseActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Jin on 2016/11/4.
 */

public class MyWalletActivity extends BaseActivity {
    @Bind(R.id.tv_remain)
    TextView tvRemain;
    @Bind(R.id.tv_recharge)
    TextView tvRecharge;
    @Bind(R.id.tv_record)
    TextView tvRecord;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_wallet, "我的钱包");
        ButterKnife.bind(this);
        getDoingView().setVisibility(View.INVISIBLE);
        tvRemain.setText(getIntent().getStringExtra("remain"));
    }

    @OnClick({R.id.tv_recharge, R.id.tv_record})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_recharge:
                startActivityForNoIntent(RechargeActivity.class);
                break;
            case R.id.tv_record:
                startActivityForNoIntent(ConsumptionActivity.class);
                break;
        }
    }
}
