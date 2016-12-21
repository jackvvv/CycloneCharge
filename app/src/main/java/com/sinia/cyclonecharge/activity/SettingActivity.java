package com.sinia.cyclonecharge.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sinia.cyclonecharge.R;
import com.sinia.cyclonecharge.base.BaseActivity;
import com.sinia.cyclonecharge.utils.AppInfoUtil;
import com.sinia.cyclonecharge.utils.Constants;
import com.sinia.cyclonecharge.utils.DataCleanManager;
import com.sinia.cyclonecharge.utils.MyApplication;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by newLamp on 2016/11/4.
 */

public class SettingActivity extends BaseActivity {
    @Bind(R.id.rl_change_pwd)
    RelativeLayout rlChangePwd;
    @Bind(R.id.tv_version)
    TextView tvVersion;
    @Bind(R.id.tv_cache)
    TextView tvCache;
    @Bind(R.id.rl_clear_cache)
    RelativeLayout rlClearCache;
    @Bind(R.id.rl_feedback)
    RelativeLayout rlFeedback;
    @Bind(R.id.tv_logout)
    TextView tvLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting, "通用设置");
        ButterKnife.bind(this);
        getDoingView().setVisibility(View.GONE);
        tvVersion.setText("v" + AppInfoUtil.getVersionName(this));
    }

    @OnClick({R.id.rl_change_pwd, R.id.rl_clear_cache, R.id.rl_feedback, R.id.tv_logout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_change_pwd:
                if (MyApplication.getInstance().getBoolValue(Constants.SP_HELPER.IS_LOGIN)) {
                    startActivityForNoIntent(ChangePwdActivity.class);
                }
                break;
            case R.id.rl_clear_cache:
                DataCleanManager.clearAllCache(SettingActivity.this);
                try {
                    tvCache.setText(DataCleanManager.getTotalCacheSize(this) + "");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.rl_feedback:
                if (MyApplication.getInstance().getBoolValue(Constants.SP_HELPER.IS_LOGIN)) {
                    startActivityForNoIntent(FeedbackActivity.class);
                } else {
                    startActivityForNoIntent(LoginRegisterActivity.class);
                }
                break;
            case R.id.tv_logout:
                if (MyApplication.getInstance().getBoolValue(Constants.SP_HELPER.IS_LOGIN)) {
                    MyApplication.getInstance().setBooleanValue(Constants.SP_HELPER.IS_LOGIN, false);
                    finish();
                }
                break;
        }
    }
}
