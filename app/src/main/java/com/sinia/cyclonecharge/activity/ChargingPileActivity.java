package com.sinia.cyclonecharge.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;
import com.sinia.cyclonecharge.R;
import com.sinia.cyclonecharge.base.BaseActivity;
import com.sinia.cyclonecharge.bean.ChargeStationDetailBean;
import com.sinia.cyclonecharge.http.CoreHttpClient;
import com.sinia.cyclonecharge.http.HttpCallBackListener;
import com.sinia.cyclonecharge.utils.AppInfoUtil;
import com.sinia.cyclonecharge.utils.Constants;
import com.sinia.cyclonecharge.utils.DialogUtils;
import com.sinia.cyclonecharge.utils.MyApplication;
import com.sinia.cyclonecharge.utils.StringUtil;
import com.sinia.cyclonecharge.view.SlideShowView;

import org.json.JSONObject;

import java.net.URISyntaxException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.sinia.cyclonecharge.R.id.ll_cancel;
import static com.sinia.cyclonecharge.R.id.tv_distance;
import static com.sinia.cyclonecharge.R.id.tv_title;
import static com.sinia.cyclonecharge.fragment.ChargeListFragment.WHAT_CHOOSE_BAIDU;
import static com.sinia.cyclonecharge.fragment.ChargeListFragment.WHAT_CHOOSE_GAODE;
import static com.sinia.cyclonecharge.fragment.ChargeListFragment.WHAT_NAVIGATION;
import static com.sinia.cyclonecharge.fragment.ChargeListFragment.WHAT_SELECT_TIME;
import static com.sinia.cyclonecharge.fragment.ChargeListFragment.WHAT_SHOW_DATEPICKER;

/**
 * Created by newLamp on 2016/11/4.
 */

public class ChargingPileActivity extends BaseActivity {

    @Bind(R.id.slideshowview)
    SlideShowView slideshowview;
    @Bind(R.id.iv_navi)
    ImageView ivNavi;
    @Bind(tv_title)
    TextView tvTitle;
    @Bind(R.id.tv_address)
    TextView tvAddress;
    @Bind(tv_distance)
    TextView tvDistance;
    @Bind(R.id.tv_identifier)
    TextView tvIdentifier;
    @Bind(R.id.tv_coordinate)
    TextView tvCoordinate;
    @Bind(R.id.tv_pay_type)
    TextView tvPayType;
    @Bind(R.id.tv_fee)
    TextView tvFee;
    @Bind(R.id.tv_service_fee)
    TextView tvServiceFee;
    @Bind(R.id.tv_stop_fee)
    TextView tvStopFee;
    @Bind(R.id.tv_msg)
    TextView tvMsg;
    @Bind(ll_cancel)
    LinearLayout llCancel;
    private String eqId, title;
    private String orderEqid, scanTime;
    private double longitude, latitude;
    /**
     * 首页轮播图的图片
     */
    private List<String> picList = new ArrayList<String>();
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case WHAT_NAVIGATION:
                    int type = 0;
                    if (AppInfoUtil.isAvilible(ChargingPileActivity.this, AppInfoUtil.BaiduPackageName)) {
                        type = 1;
                    }
                    if (AppInfoUtil.isAvilible(ChargingPileActivity.this, AppInfoUtil.GaodePackageName)) {
                        type = 2;
                    }
                    if (AppInfoUtil.isAvilible(ChargingPileActivity.this, AppInfoUtil.BaiduPackageName) && AppInfoUtil
                            .isAvilible(ChargingPileActivity.this, AppInfoUtil.GaodePackageName)) {
                        type = 3;
                    }
                    DialogUtils.createChooseNaviDialog(ChargingPileActivity.this, handler, type);
                    break;
                case WHAT_CHOOSE_BAIDU:
                    double[] s_array = StringUtil.gaoDeToBaidu(MainActivity.currentLongitude, MainActivity
                            .currentLatitude);
                    double[] e_array = StringUtil.gaoDeToBaidu(118.718541, 32.138804);
                    Log.d("lamp", "起点:经度=" + s_array[0] + ",纬度=" + s_array[1]);
                    Log.d("lamp", "终点:经度=" + e_array[0] + ",纬度=" + e_array[1]);
                    Intent intent = new Intent();
                    try {
                        intent = Intent.parseUri("intent://map/direction?" +
                                "origin=latlng:" + s_array[1] + "," + s_array[0] +
                                "|name:" + MainActivity.address +
                                "&destination=latlng:" + e_array[1] + "," + e_array[0] +
                                "|name:" + "栖霞区人民政府" +
                                "&mode=driving" +
                                "&src=Name|AppName" +
                                "#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end", 0);
                    } catch (URISyntaxException e) {
                        Log.e("lamp", "URISyntaxException : " + e.getMessage());
                        e.printStackTrace();
                    }
                    startActivity(intent);
                    break;
                case WHAT_CHOOSE_GAODE:
                    Intent intent2 = new Intent();
                    intent2.setData(Uri
                            .parse("androidamap://route?" +
                                    "sourceApplication=softname" +
                                    "&slat=" + MainActivity.currentLatitude +
                                    "&slon=" + MainActivity.currentLongitude +
                                    "&dlat=" + "32.096388" +
                                    "&dlon=" + "118.909153" +
                                    "&dname=" + "栖霞区人民政府" +
                                    "&dev=0" +
                                    "&m=0" +
                                    "&t=2"));
                    startActivity(intent2);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        latitude = getIntent().getDoubleExtra("latitude", 0);
        longitude = getIntent().getDoubleExtra("longitude", 0);
        eqId = getIntent().getStringExtra("eqId");
        title = getIntent().getStringExtra("title");
        orderEqid = getIntent().getStringExtra("orderEqid");
        scanTime = getIntent().getStringExtra("scanTime");
        setContentView(R.layout.activity_charging_pile, "长葛新区政府充电站");
        getDoingView().setVisibility(View.GONE);
        ButterKnife.bind(this);
        initView();
        getStationDetail();
    }

    private void getStationDetail() {
        showLoad("");
        RequestParams params = new RequestParams();
        params.put("longitude", longitude + "");
        params.put("latitude", latitude + "");
        params.put("eqId", eqId);
        params.put("userId", MyApplication.getInstance().getLoginBean().getUserId());
        Log.d("URL", Constants.BASE_URL + "chargeStationDetail&" + params.toString());
        CoreHttpClient.post("chargeStationDetail", params, new HttpCallBackListener() {
            @Override
            public void onSuccess(JSONObject json) {
                dismiss();
                if (json.optInt("state") == 0 && json.optInt("isSuccessful") == 0) {
                    Gson gson = new Gson();
                    ChargeStationDetailBean detailBean = gson.fromJson(json.toString(), ChargeStationDetailBean.class);
                    setData(detailBean);
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

    private void setData(ChargeStationDetailBean detailBean) {
        picList.clear();
        if (null != detailBean.getImageItems()) {
            for (int i = 0; i < detailBean.getImageItems().size(); i++) {
                picList.add(detailBean.getImageItems().get(i).getImageUrl());
            }
        }
        slideshowview.setImagePath(picList);
        slideshowview.startPlay();
//        tvTitle.setText(title);
        tvTitle.setText("长葛新区政府充电站");
        tvAddress.setText(detailBean.getEqAddress());
        DecimalFormat df = new DecimalFormat("######0.00");
        tvDistance.setText(df.format(detailBean.getDistance()) + "km");
        tvIdentifier.setText("设备编号：" + detailBean.getEqnum());
        tvCoordinate.setText("设备坐标：" + detailBean.getLatitude() + "," + detailBean.getLongitude());
        tvFee.setText(detailBean.getPrice() + "元/度");
        tvServiceFee.setText(detailBean.getServerPrice());
        tvStopFee.setText(detailBean.getParkingPrice());
    }

    private void initView() {
        int h = AppInfoUtil.getScreenWidth(this) * 5 / 8;
        slideshowview.getLayoutParams().height = h;
        slideshowview.setOnItemClickListener(new SlideShowView.ViewPagerItemCLickListener() {
            @Override
            public void onItemClick(View pager, int position) {
            }
        });
        if (MyApplication.getInstance().getBoolValue(Constants.SP_HELPER.IS_LOGIN)) {
            if (!StringUtil.isEmpty(orderEqid) && orderEqid.equals(eqId)) {
                llCancel.setVisibility(View.VISIBLE);
                tvMsg.setText("您已成功预约充电桩，今天" + scanTime);
            } else {
                llCancel.setVisibility(View.GONE);
            }
        } else {
            llCancel.setVisibility(View.GONE);
        }
    }

    private void appointment(String time, String eqId) {
        showLoad("");
        RequestParams params = new RequestParams();
        params.put("userId", MyApplication.getInstance().getLoginBean().getUserId());
        params.put("eqId", eqId);
        params.put("time", time);
        Log.d("URL", Constants.BASE_URL + "appointment&" + params.toString());
        CoreHttpClient.post("appointment", params, new HttpCallBackListener() {
            @Override
            public void onSuccess(JSONObject json) {
                dismiss();
                if (json.optInt("state") == 0 && json.optInt("isSuccessful") == 0) {
                    showToast("预约成功");
                } else {
                    showToast("预约失败");
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

    private void appointmentDel(String eqId) {
        showLoad("");
        RequestParams params = new RequestParams();
        params.put("userId", MyApplication.getInstance().getLoginBean().getUserId());
        params.put("eqId", eqId);
        Log.d("URL", Constants.BASE_URL + "appointmentDel&" + params.toString());
        CoreHttpClient.post("appointmentDel", params, new HttpCallBackListener() {
            @Override
            public void onSuccess(JSONObject json) {
                dismiss();
                if (json.optInt("state") == 0 && json.optInt("isSuccessful") == 0) {
                    showToast("取消预约成功");
                    finish();
                } else {
                    showToast("取消预约失败");
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

    @OnClick({R.id.iv_navi, ll_cancel})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_navi:
                handler.sendEmptyMessage(WHAT_NAVIGATION);
                break;
            case ll_cancel:
                appointmentDel(eqId);
                break;
        }
    }
}
