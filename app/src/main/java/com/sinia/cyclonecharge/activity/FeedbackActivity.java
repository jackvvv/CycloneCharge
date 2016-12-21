package com.sinia.cyclonecharge.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import com.sinia.cyclonecharge.R;
import com.sinia.cyclonecharge.base.BaseActivity;
import com.sinia.cyclonecharge.http.CoreHttpClient;
import com.sinia.cyclonecharge.http.HttpCallBackListener;
import com.sinia.cyclonecharge.utils.Constants;
import com.sinia.cyclonecharge.utils.MyApplication;
import com.sinia.cyclonecharge.utils.StringUtil;

import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by newLamp on 2016/11/4.
 */

public class FeedbackActivity extends BaseActivity {
    @Bind(R.id.et_advice)
    EditText etAdvice;
    @Bind(R.id.tv_ensure)
    TextView tvEnsure;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback, "意见反馈");
        ButterKnife.bind(this);
        getDoingView().setVisibility(View.GONE);
    }

    @OnClick(R.id.tv_ensure)
    public void onClick() {
        if (StringUtil.isEmpty(etAdvice.getEditableText().toString())) {
            showToast("请输入反馈内容");
        } else {
            appointmentDel();
        }
    }

    private void appointmentDel() {
        showLoad("");
        RequestParams params = new RequestParams();
        params.put("userId", MyApplication.getInstance().getLoginBean().getUserId());
        params.put("content", etAdvice.getEditableText().toString());
        Log.d("URL", Constants.BASE_URL + "addAdvice&" + params.toString());
        CoreHttpClient.post("addAdvice", params, new HttpCallBackListener() {
            @Override
            public void onSuccess(JSONObject json) {
                dismiss();
                if (json.optInt("state") == 0 && json.optInt("isSuccessful") == 0) {
                    showToast("反馈成功");
                    finish();
                } else {
                    showToast("请求失败");
                }
            }

            @Override
            public void onRequestFailed() {
                dismiss();
                showToast("请求失败");
            }

            @Override
            public void onException() {
                dismiss();
                showToast("请求失败");
            }
        });
    }
}
