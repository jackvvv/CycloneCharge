package com.sinia.cyclonecharge.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sinia.cyclonecharge.R;
import com.sinia.cyclonecharge.base.BaseActivity;
import com.sinia.cyclonecharge.utils.ActivityManager;
import com.sinia.cyclonecharge.zxing.CaptureActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by newLamp on 2016/11/4.
 */

public class InputCodeActivity extends BaseActivity {
    @Bind(R.id.rl_back)
    RelativeLayout rlBack;
    @Bind(R.id.et_code)
    EditText etCode;
    @Bind(R.id.ll_change)
    LinearLayout llChange;
    @Bind(R.id.tv_ensure)
    TextView tvEnsure;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_code);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.rl_back, R.id.ll_change, R.id.tv_ensure})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_back:
                finish();
                break;
            case R.id.ll_change:
                finish();
                break;
            case R.id.tv_ensure:
                Intent intent = new Intent(MainActivity.ACTION_SHOWCHARHING);
                intent.putExtra("showWhat",true);
                LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
                ActivityManager.getInstance().finishActivity(CaptureActivity.class);
                finish();
                break;
        }
    }
}
