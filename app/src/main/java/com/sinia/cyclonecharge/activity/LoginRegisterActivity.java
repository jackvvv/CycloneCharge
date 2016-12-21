package com.sinia.cyclonecharge.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import com.sinia.cyclonecharge.R;
import com.sinia.cyclonecharge.base.BaseActivity;
import com.sinia.cyclonecharge.bean.LoginBean;
import com.sinia.cyclonecharge.http.CoreHttpClient;
import com.sinia.cyclonecharge.http.HttpCallBackListener;
import com.sinia.cyclonecharge.utils.ActivityManager;
import com.sinia.cyclonecharge.utils.Constants;
import com.sinia.cyclonecharge.utils.MyApplication;
import com.sinia.cyclonecharge.utils.StringUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.smssdk.EventHandler;
import cn.smssdk.OnSendMessageHandler;
import cn.smssdk.SMSSDK;

import static com.sinia.cyclonecharge.R.id.et_name;
import static com.sinia.cyclonecharge.R.id.et_pwd;
import static com.sinia.cyclonecharge.R.id.et_register_pwd;
import static com.sinia.cyclonecharge.R.id.et_tel;
import static com.sinia.cyclonecharge.R.id.tv_verify;


/**
 * Created by newLamp on 2016/10/20.
 */

public class LoginRegisterActivity extends BaseActivity {
    @Bind(R.id.rl_close)
    RelativeLayout rlClose;
    @Bind(R.id.iv_login)
    ImageView ivLogin;
    @Bind(R.id.ll_login)
    LinearLayout llLogin;
    @Bind(R.id.iv_register)
    ImageView ivRegister;
    @Bind(R.id.ll_register)
    LinearLayout llRegister;
    @Bind(et_tel)
    EditText etTel;
    @Bind(et_pwd)
    EditText etPwd;
    @Bind(R.id.tv_login)
    TextView tvLogin;
    @Bind(R.id.et_code)
    TextView etCode;
    @Bind(R.id.tv_forget_pwd)
    TextView tvForgetPwd;
    @Bind(R.id.ll_show_login)
    LinearLayout llShowLogin;
    @Bind(et_name)
    EditText etName;
    @Bind(R.id.ll1)
    LinearLayout ll1;
    @Bind(R.id.et_account)
    EditText etAccount;
    @Bind(R.id.ll2)
    LinearLayout ll2;
    @Bind(tv_verify)
    TextView tvVerify;
    @Bind(et_register_pwd)
    EditText etRegisterPwd;
    @Bind(R.id.ll3)
    LinearLayout ll3;
    @Bind(R.id.tv_register)
    TextView tvRegister;
    @Bind(R.id.ll_show_register)
    LinearLayout llShowRegister;

    private int type;
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
                    registerUser();
                    break;
                case 2:
                    showToast("获取验证码成功");
                    dismiss();
                    break;
                case 3:
                    dismiss();
                    showToast("验证码输入有误");
                    break;
                case 4:
                    showToast("当前手机号发送短信的数量超过限额");
                    dismiss();
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
        setContentView(R.layout.activity_login_register);
        ButterKnife.bind(this);
        SMSSDK.registerEventHandler(eventHandler);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterEventHandler(eventHandler);
    }

    @OnClick({R.id.rl_close, R.id.ll_login, R.id.ll_register, R.id.tv_login, R.id.tv_forget_pwd, tv_verify, R.id
            .tv_register})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_close:
                finish();
                break;
            case R.id.ll_login:
                type = 0;
                changeView();
                break;
            case R.id.ll_register:
                type = 1;
                changeView();
                break;
            case R.id.tv_login:
//                startActivityForNoIntent(MainActivity.class);
                if (etTel.getEditableText().toString().equals("")) {
                    showToast("请输入手机号");
                    return;
                }

                if (etPwd.getEditableText().toString().trim().equals("")) {
                    showToast("请输入密码");
                    return;
                }

                login();
                break;
            case R.id.tv_forget_pwd:
                startActivityForNoIntent(FindPwdActivity.class);
                break;
            case tv_verify:
                String phone = etAccount.getEditableText().toString();
                if (TextUtils.isEmpty(phone)) {
                    showToast("请输入手机号");
                    return;
                }
                if (!StringUtil.isPhoneNum(phone)) {
                    showToast("输入的手机号不正确");
                    return;
                }
                showLoad("");
                SMSSDK.getVerificationCode("86", phone, new OnSendMessageHandler() {
                    @Override
                    public boolean onSendMessage(String s, String s1) {
                        return false;
                    }
                });
                tvVerify.setClickable(false);
                tvVerify.setText("重新发送(" + i + "s)");
                tvVerify.setTextColor(Color.parseColor("#000000"));
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
            case R.id.tv_register:
                if (etName.getEditableText().toString().equals("")) {
                    showToast("请输入昵称");
                    return;
                }
                if (etAccount.getEditableText().toString().equals("")) {
                    showToast("请输入手机号");
                    return;
                }
                if (etCode.getEditableText().toString().equals("")) {
                    showToast("请输入验证码");
                    return;
                }
                if (etRegisterPwd.getEditableText().toString().equals("")) {
                    showToast("请输入密码");
                    return;
                }
                showLoad("注册中...");
                SMSSDK.submitVerificationCode("86", etAccount.getEditableText().toString(), etCode.getEditableText()
                        .toString());
                break;
        }
    }


    private void changeView() {
        if (type == 0) {
            llShowLogin.setVisibility(View.VISIBLE);
            llShowRegister.setVisibility(View.GONE);
            ivLogin.setVisibility(View.VISIBLE);
            ivRegister.setVisibility(View.INVISIBLE);
        } else {
            llShowLogin.setVisibility(View.GONE);
            llShowRegister.setVisibility(View.VISIBLE);
            ivLogin.setVisibility(View.INVISIBLE);
            ivRegister.setVisibility(View.VISIBLE);
        }
    }

    private void registerUser() {
        RequestParams params = new RequestParams();
        params.put("nickname", etName.getEditableText().toString());
        params.put("telephone", etAccount.getEditableText().toString());
        params.put("password", etRegisterPwd.getEditableText().toString());
        LogRequestUrl("registerUser&" + params.toString());
        CoreHttpClient.post("registerUser", params, new HttpCallBackListener() {
            @Override
            public void onSuccess(JSONObject json) {
                dismiss();
                if (json.optInt("state") == 0) {
                    switch (json.optInt("isSuccessful")) {
                        case 0:
                            showToast("注册成功");
                            type = 0;
                            changeView();
                            break;
                        case 1:
                            showToast("您的手机号已经注册过了");
                            break;
                        case 2:
                            showToast("注册失败");
                            break;
                    }
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

    private void login() {
        showLoad("登录中...");
        RequestParams params = new RequestParams();
        params.put("telephone", etTel.getEditableText().toString());
        params.put("password", etPwd.getEditableText().toString());
        LogRequestUrl("login&" + params.toString());
        CoreHttpClient.post("login", params, new HttpCallBackListener() {
            @Override
            public void onSuccess(JSONObject json) {
                dismiss();
                if (json.optInt("state") == 0) {
                    if (json.optInt("isSuccessful") == 0) {
                        showToast("登录成功");
                        MyApplication.getInstance().setBooleanValue(Constants.SP_HELPER.IS_LOGIN,true);
                        LoginBean loginBean = MyApplication.getInstance().getLoginBean();
                        loginBean.setUserId(json.optString("userId"));
                        MyApplication.getInstance().setLoginBean(loginBean);
                        startActivity(new Intent(LoginRegisterActivity.this,MainActivity.class));
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
}
