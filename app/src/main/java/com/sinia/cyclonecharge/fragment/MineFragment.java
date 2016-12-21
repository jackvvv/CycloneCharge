package com.sinia.cyclonecharge.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;
import com.sinia.cyclonecharge.R;
import com.sinia.cyclonecharge.activity.AboutUsActivity;
import com.sinia.cyclonecharge.activity.ContactUsActivity;
import com.sinia.cyclonecharge.activity.LoginRegisterActivity;
import com.sinia.cyclonecharge.activity.MyWalletActivity;
import com.sinia.cyclonecharge.activity.PersonDataActivity;
import com.sinia.cyclonecharge.activity.RechargeActivity;
import com.sinia.cyclonecharge.activity.SettingActivity;
import com.sinia.cyclonecharge.base.BaseFragment;
import com.sinia.cyclonecharge.bean.PersonDetailBean;
import com.sinia.cyclonecharge.http.CoreHttpClient;
import com.sinia.cyclonecharge.http.HttpCallBackListener;
import com.sinia.cyclonecharge.utils.Constants;
import com.sinia.cyclonecharge.utils.MyApplication;
import com.sinia.cyclonecharge.utils.StringUtil;
import com.sinia.cyclonecharge.view.RoundImageView;

import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.sinia.cyclonecharge.R.id.tv_remain;

/**
 * Created by Jin on 2016/11/3.
 */

public class MineFragment extends BaseFragment {
    @Bind(R.id.iv_head)
    RoundImageView ivHead;
    @Bind(R.id.tv_name)
    TextView tvName;
    @Bind(tv_remain)
    TextView tvRemain;
    @Bind(R.id.rl_my_wallet)
    RelativeLayout rlMyWallet;
    @Bind(R.id.rl_recharge)
    RelativeLayout rlRecharge;
    @Bind(R.id.rl_about_us)
    RelativeLayout rlAboutUs;
    @Bind(R.id.rl_contact_us)
    RelativeLayout rlContactUs;
    @Bind(R.id.rl_setting)
    RelativeLayout rlSetting;
    @Bind(R.id.rl_root)
    RelativeLayout rlRoot;
    private View rootView;
    private String remain;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_mine, null);
        ButterKnife.bind(this, rootView);
        initView();
        return rootView;
    }

    private void initView() {
        ViewGroup.LayoutParams params = rlRoot.getLayoutParams();
        Display display = getActivity().getWindowManager().getDefaultDisplay(); // 为获取屏幕宽、高
        params.width = display.getWidth();
        params.height = display.getWidth() * 4 / 9;
        rlRoot.setLayoutParams(params);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (MyApplication.getInstance().getBoolValue(Constants.SP_HELPER.IS_LOGIN)) {
            personDetailById();
        } else {
            ivHead.setImageResource(R.mipmap.default_head);
            tvName.setText("昵称");
            tvRemain.setText("余额¥0");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    private void personDetailById() {
        showLoad("");
        RequestParams params = new RequestParams();
        params.put("userId", MyApplication.getInstance().getLoginBean().getUserId());
        CoreHttpClient.post("personDetailById", params, new HttpCallBackListener() {
            @Override
            public void onSuccess(JSONObject json) {
                dismiss();
                if (json.optInt("state") == 0 && json.optInt("isSuccessful") == 0) {
                    Gson gson = new Gson();
                    PersonDetailBean personDetailBean = gson.fromJson(json.toString(), PersonDetailBean.class);
                    Glide.with(getActivity()).load(personDetailBean.getImageurl()).placeholder(R.mipmap
                            .default_head).crossFade().into(ivHead);
                    tvName.setText(personDetailBean.getNickname());
                    tvRemain.setText("余额¥" + personDetailBean.getBalance());
                    remain = personDetailBean.getBalance();
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

    @OnClick({R.id.rl_my_wallet, R.id.rl_recharge, R.id.rl_about_us, R.id.rl_contact_us, R.id.rl_setting, R.id.iv_head})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_my_wallet:
                if (MyApplication.getInstance().getBoolValue(Constants.SP_HELPER.IS_LOGIN)) {
                    startActivity(new Intent(getActivity(), MyWalletActivity.class).putExtra("remain", remain));
                } else {
                    startActivityForNoIntent(LoginRegisterActivity.class);
                }
                break;
            case R.id.rl_recharge:
                if (MyApplication.getInstance().getBoolValue(Constants.SP_HELPER.IS_LOGIN)) {
                    startActivityForNoIntent(RechargeActivity.class);
                } else {
                    startActivityForNoIntent(LoginRegisterActivity.class);
                }
                break;
            case R.id.rl_about_us:
                startActivityForNoIntent(AboutUsActivity.class);
                break;
            case R.id.rl_contact_us:
                startActivityForNoIntent(ContactUsActivity.class);
                break;
            case R.id.rl_setting:
                startActivityForNoIntent(SettingActivity.class);
                break;
            case R.id.iv_head:
                if (MyApplication.getInstance().getBoolValue(Constants.SP_HELPER.IS_LOGIN)) {
                    startActivityForNoIntent(PersonDataActivity.class);
                } else {
                    startActivityForNoIntent(LoginRegisterActivity.class);
                }
                break;
        }
    }
}
