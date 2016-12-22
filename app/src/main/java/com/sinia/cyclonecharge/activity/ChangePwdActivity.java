package com.sinia.cyclonecharge.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import com.sinia.cyclonecharge.R;
import com.sinia.cyclonecharge.base.BaseActivity;
import com.sinia.cyclonecharge.http.CoreHttpClient;
import com.sinia.cyclonecharge.http.HttpCallBackListener;
import com.sinia.cyclonecharge.utils.MyApplication;

import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by newLamp on 2016/11/4.
 */

public class ChangePwdActivity extends BaseActivity {
    @Bind(R.id.et_new_pwd)
    EditText etNewPwd;
    @Bind(R.id.et_confirm_pwd)
    EditText etConfirmPwd;
    @Bind(R.id.tv_ensure)
    TextView tvEnsure;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pwd, "修改密码");
        ButterKnife.bind(this);
        getDoingView().setVisibility(View.GONE);
    }


    @OnClick(R.id.tv_ensure)
    public void onClick() {
        if (etNewPwd.getEditableText().toString().equals("")) {
            showToast("请输入旧密码");
            return;
        }
        if (etConfirmPwd.getEditableText().toString().equals("")) {
            showToast("请输入新密码");
            return;
        }
        if (etConfirmPwd.getEditableText().toString().equals(etNewPwd.getEditableText().toString())) {
            showToast("新密码不能与旧密码相同");
            return;
        }
        upPassword();
    }

    private void upPassword() {
        showLoad("");
        RequestParams params = new RequestParams();
        params.put("userId", MyApplication.getInstance().getLoginBean().getUserId());
        params.put("oldPwd", etNewPwd.getEditableText().toString());
        params.put("newPwd", etConfirmPwd.getEditableText().toString());
        CoreHttpClient.post("upPassword", params, new HttpCallBackListener() {
            @Override
            public void onSuccess(JSONObject json) {
                dismiss();
                if (json.optInt("state") == 0 && json.optInt("isSuccessful") == 0) {
                    showToast("修改成功");
                    finish();
                } else {
                    showToast("修改失败");
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
