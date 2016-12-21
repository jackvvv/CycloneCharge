package com.sinia.cyclonecharge.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import com.sinia.cyclonecharge.R;
import com.sinia.cyclonecharge.base.BaseActivity;
import com.sinia.cyclonecharge.http.CoreHttpClient;
import com.sinia.cyclonecharge.http.HttpCallBackListener;
import com.sinia.cyclonecharge.utils.ActivityManager;
import com.sinia.cyclonecharge.utils.Constants;
import com.sinia.cyclonecharge.utils.MyApplication;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.smssdk.EventHandler;
import cn.smssdk.OnSendMessageHandler;
import cn.smssdk.SMSSDK;

/**
 * Created by newLamp on 2016/11/4.
 */

public class FindPwdActivity extends BaseActivity {
    @Bind(R.id.rl_close)
    RelativeLayout rlClose;
    @Bind(R.id.et_phone)
    EditText etPhone;
    @Bind(R.id.tv_verify)
    TextView tvVerify;
    @Bind(R.id.et_code)
    EditText etCode;
    @Bind(R.id.et_new_pwd)
    EditText etNewPwd;
    @Bind(R.id.et_confirm_pwd)
    EditText etConfirmPwd;
    @Bind(R.id.tv_ensure)
    TextView tvEnsure;
    @Bind(R.id.tv_login)
    TextView tvLogin;
    private int i = 60;// 限时60s
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case -9:
                    tvVerify.setText("重新发送(" + i + "s)");
                    break;
                case -8:
                    tvVerify.setText("获取验证码");
                    tvVerify.setTextColor(Color.parseColor("#ffffff"));
                    tvVerify.setClickable(true);
                    i = 60;
                    break;
                case 1:
//                    showToast("验证码输入正确");
                    forPassword();
                    break;
                case 2:
                    showToast("获取验证码成功");
                    break;
                case 3:
                    dismiss();
                    showToast("验证码输入有误");
                    break;
                case 4:
                    showToast("当前手机号发送短信的数量超过限额");
                    break;
                default:
                    break;
            }
        }
    };

    EventHandler eventHandler = new EventHandler() {
        @Override
        public void afterEvent(int event, int result, Object data) {
            if (result == SMSSDK.RESULT_COMPLETE) {
                //回调完成
                if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                    //提交验证码成功
                    handler.sendEmptyMessage(1);

                } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                    //获取验证码成功
                    handler.sendEmptyMessage(2);
                } else if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {
                    //返回支持发送验证码的国家列表
                }
            } else {
                String errorMessage = ((Throwable) data).getMessage();
                int status = 0;
                try {
                    JSONObject json = new JSONObject(errorMessage);
                    Log.d("lamp", "json=" + json.toString() + ",status=" + json.optInt("status"));
                    status = json.optInt("status");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                switch (status) {
                    case 468:
                        //验证码输入有误
                        handler.sendEmptyMessage(3);
                        break;
                    case 477:
                        //当前手机验证码发送超额
                        handler.sendEmptyMessage(4);
                        break;
                }
                Log.d("lamp", "data.getMessage=" + ((Throwable) data).getMessage());

                ((Throwable) data).printStackTrace();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_pwd);
        ButterKnife.bind(this);
        SMSSDK.registerEventHandler(eventHandler);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterEventHandler(eventHandler);
    }

    @OnClick({R.id.rl_close, R.id.tv_ensure, R.id.tv_login, R.id.tv_verify})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_close:
                finish();
                break;
            case R.id.tv_ensure:
                if (etPhone.getEditableText().toString().equals("")) {
                    showToast("请输入手机号");
                    return;
                }
                if (etCode.getEditableText().toString().equals("")) {
                    showToast("请输入验证码");
                    return;
                }
                if (etNewPwd.getEditableText().toString().equals("")) {
                    showToast("请输入密码");
                    return;
                }
                if (etConfirmPwd.getEditableText().toString().equals("")) {
                    showToast("请确认密码");
                    return;
                }
                if (!etNewPwd.getEditableText().toString().equals(etConfirmPwd.getEditableText().toString())) {
                    showToast("两次密码输入不一致");
                    return;
                }
                showLoad("");
                SMSSDK.submitVerificationCode("86", etPhone.getEditableText().toString(), etCode.getEditableText()
                        .toString());

                break;
            case R.id.tv_login:
//                finish();
                login();
                break;
            case R.id.tv_verify:
                if (etPhone.getEditableText().toString().equals("")) {
                    showToast("请输入手机号");
                    return;
                }
                SMSSDK.getVerificationCode("86", etPhone.getEditableText().toString(), new OnSendMessageHandler() {
                    @Override
                    public boolean onSendMessage(String s, String s1) {
                        return false;
                    }
                });
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        for (; i > 0; i--) {
                            handler.sendEmptyMessage(-9);
                            if (i <= 0) {
                                break;
                            }
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        handler.sendEmptyMessage(-8);
                    }
                }).start();
                break;
        }
    }

    private void login() {
        showLoad("登录中...");
        RequestParams params = new RequestParams();
        params.put("telephone", etPhone.getEditableText().toString());
        params.put("password", etNewPwd.getEditableText().toString());
        LogRequestUrl("login&" + params.toString());
        CoreHttpClient.post("login", params, new HttpCallBackListener() {
            @Override
            public void onSuccess(JSONObject json) {
                dismiss();
                if (json.optInt("state") == 0) {
                    if (json.optInt("isSuccessful") == 0) {
                        showToast("登录成功");
                        MyApplication.getInstance().setBooleanValue(Constants.SP_HELPER.IS_LOGIN,true);
                        startActivity(new Intent(FindPwdActivity.this,MainActivity.class));
                        ActivityManager.getInstance().finishActivity(LoginRegisterActivity.class);
                        finish();
                    } else {
                        showToast("登录失败");
                    }
                } else {
                    showToast("登录失败");
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

    private void forPassword() {
        RequestParams params = new RequestParams();
        params.put("telephone", etPhone.getEditableText().toString());
        params.put("password", etNewPwd.getEditableText().toString());
        LogRequestUrl("forPassword&" + params.toString());
        CoreHttpClient.post("forPassword", params, new HttpCallBackListener() {
            @Override
            public void onSuccess(JSONObject json) {
                dismiss();
                if (json.optInt("state") == 0) {
                    if (json.optInt("isSuccessful") == 0) {
                        showToast("修改成功");
                    } else {
                        showToast("修改失败");
                    }
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
