package com.sinia.cyclonecharge.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;
import com.sinia.cyclonecharge.R;
import com.sinia.cyclonecharge.activity.ChargingPileActivity;
import com.sinia.cyclonecharge.activity.MainActivity;
import com.sinia.cyclonecharge.adapter.ChargeListAdapter;
import com.sinia.cyclonecharge.base.BaseFragment;
import com.sinia.cyclonecharge.bean.ChargeStationItemBean;
import com.sinia.cyclonecharge.bean.TelectricpileItemBean;
import com.sinia.cyclonecharge.bean.UserCoordinateList;
import com.sinia.cyclonecharge.http.CoreHttpClient;
import com.sinia.cyclonecharge.http.HttpCallBackListener;
import com.sinia.cyclonecharge.utils.AppInfoUtil;
import com.sinia.cyclonecharge.utils.Constants;
import com.sinia.cyclonecharge.utils.DialogUtils;
import com.sinia.cyclonecharge.utils.MyApplication;
import com.sinia.cyclonecharge.utils.StringUtil;

import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.sinia.cyclonecharge.activity.MainActivity.currentLatitude;
import static com.sinia.cyclonecharge.activity.MainActivity.currentLongitude;
import static com.sinia.cyclonecharge.utils.MyApplication.context;

/**
 * Created by newLamp on 2016/11/3.
 */

public class ChargeListFragment extends BaseFragment {

    @Bind(R.id.listview)
    ListView listview;
    @Bind(R.id.tv_msg)
    TextView tvMsg;
    @Bind(R.id.rl_root)
    RelativeLayout rlRoot;
    private View rootView;
    private ChargeListAdapter chargeListAdapter;
    private ChargeStationItemBean chargeStationItemBean;
    private List<ChargeStationItemBean> list = new ArrayList<>();
    private String eqId;//成功预约的设备id
    private String scanTime;//成功预约的时间
    private String eqName;//成功预约的设备名称
    private double eqLat, eqLng;
    //宝铁龙4s店 118.718541,32.138804
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case WHAT_NAVIGATION:
                    int type = 0;
                    if (AppInfoUtil.isAvilible(getActivity(), AppInfoUtil.BaiduPackageName)) {
                        type = 1;
                    }
                    if (AppInfoUtil.isAvilible(getActivity(), AppInfoUtil.GaodePackageName)) {
                        type = 2;
                    }
                    if (AppInfoUtil.isAvilible(getActivity(), AppInfoUtil.BaiduPackageName) && AppInfoUtil
                            .isAvilible(getActivity(), AppInfoUtil.GaodePackageName)) {
                        type = 3;
                    }
                    DialogUtils.createChooseNaviDialog(getActivity(), handler, type);
                    chargeStationItemBean = (ChargeStationItemBean) msg.obj;
//                    DialogUtils.createShowHaveOrderDialog(getActivity());
                    break;
                case WHAT_CHOOSE_BAIDU:
                    double[] s_array = StringUtil.gaoDeToBaidu(MainActivity.currentLongitude, MainActivity
                            .currentLatitude);
//                    double[] e_array = StringUtil.gaoDeToBaidu(118.718541, 32.138804);
                    double s_lng = chargeStationItemBean.getLongitude();
                    double s_lat = chargeStationItemBean.getLatitude();

//                    double[] e_array = StringUtil.gaoDeToBaidu(118.909153, 32.138804);
                    double[] e_array = StringUtil.gaoDeToBaidu(s_lng, s_lat);
                    Log.d("lamp", "起点:经度=" + s_array[0] + ",纬度=" + s_array[1]);
                    Log.d("lamp", "终点:经度=" + e_array[0] + ",纬度=" + e_array[1]);
                    Intent intent = new Intent();
                    try {
                        intent = Intent.parseUri("intent://map/direction?" +
                                "origin=latlng:" + s_array[1] + "," + s_array[0] +
                                "|name:" + MainActivity.address +
                                "&destination=latlng:" + e_array[1] + "," + e_array[0] +
                                "|name:" + chargeStationItemBean.getEqName() +
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
                                    "&dlat=" + chargeStationItemBean.getLatitude() +
                                    "&dlon=" + chargeStationItemBean.getLongitude() +
                                    "&dname=" + chargeStationItemBean.getEqName() +
                                    "&dev=0" +
                                    "&m=0" +
                                    "&t=2"));
                    startActivity(intent2);
                    break;
                case WHAT_SHOW_DATEPICKER:
                    int position = msg.arg1;
                    DialogUtils.createDatePickerDialog(getActivity(), handler, position);
                    break;
                case WHAT_SELECT_TIME:
                    //选择时间成功
                    String s_time = msg.obj + "";
                    String cur_date = StringUtil.getCurYearAndMonth2();
                    String time = cur_date + " " + s_time;
                    String status_old = list.get(msg.arg1).getIsState();
                    String status_new = list.get(msg.arg1).getStatee();
                    if ((!StringUtil.isEmpty(status_old) && status_old.equals("3")) || (!StringUtil.isEmpty
                            (status_new) && status_new.equals("0"))) {
                        appointment(time, msg.arg1);
                    } else {
                        showToast("充电桩处于非空闲状态，暂时不可预约");
                    }
                    break;
                case 0x10:
                    Intent intent1 = new Intent(getActivity(), ChargingPileActivity.class);
                    intent1.putExtra("title", list.get(msg.arg1).getEqName());
                    intent1.putExtra("eqId", list.get(msg.arg1).getEqId());
                    intent1.putExtra("latitude", list.get(msg.arg1).getLatitude());
                    intent1.putExtra("longitude", list.get(msg.arg1).getLongitude());
                    intent1.putExtra("orderEqid", eqId);
                    intent1.putExtra("scanTime", scanTime);
                    startActivity(intent1);
                    break;
            }
        }
    };

    private UserCoordinateList userCoordinateList = new UserCoordinateList();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_charge_list, null);
        ButterKnife.bind(this, rootView);
        initView();
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        chargeStationList();
    }

    private void initView() {
        chargeListAdapter = new ChargeListAdapter(getActivity(), handler);
        listview.setAdapter(chargeListAdapter);
    }

    private void chargeStationList() {
        showLoad("");
        RequestParams params = new RequestParams();
        params.put("latitude", 34.198600 + "");
        params.put("longitude", 113.819400 + "");
        params.put("searchContent", "-1");
        if (MyApplication.getInstance().getBoolValue(Constants.SP_HELPER.IS_LOGIN)) {
            params.put("userId", MyApplication.getInstance().getLoginBean().getUserId());
        } else {
            params.put("userId", "-1");
        }
        Log.d("URL", Constants.BASE_URL + "chargeStationList&" + params.toString());
        CoreHttpClient.post("chargeStationList", params, new HttpCallBackListener() {
            @Override
            public void onSuccess(JSONObject json) {
                dismiss();
                if (json.optInt("state") == 0 && json.optInt("isSuccessful") == 0) {
                    Gson gson = new Gson();
                    userCoordinateList = gson.fromJson(json.toString(), UserCoordinateList.class);
                    eqId = userCoordinateList.getEqId();
                    eqName = userCoordinateList.getEqName();
                    scanTime = userCoordinateList.getScanTime();
                    eqLat = userCoordinateList.getLatitude();
                    eqLng = userCoordinateList.getLongitude();
                    if (!StringUtil.isEmpty(userCoordinateList.getEqId())) {
                        rlRoot.setVisibility(View.VISIBLE);
                        tvMsg.setText("您已成功预约" + userCoordinateList.getEqName() + " " + userCoordinateList
                                .getScanTime());
                        chargeListAdapter.isOrder = true;
                    } else {
                        chargeListAdapter.isOrder = false;
                        rlRoot.setVisibility(View.GONE);
                    }
                    Log.d("lamp", "chargeListAdapter.isOrder=" + chargeListAdapter.isOrder);
                    list = userCoordinateList.getChargeStationItem();
                    chargeListAdapter.data.clear();
                    chargeListAdapter.data.addAll(userCoordinateList.getChargeStationItem());
                    chargeListAdapter.notifyDataSetChanged();
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


    private void appointment(final String time, final int p) {
        showLoad("");
        RequestParams params = new RequestParams();
        params.put("userId", MyApplication.getInstance().getLoginBean().getUserId());
        params.put("eqId", list.get(p).getEqId());
        params.put("time", time);
        Log.d("URL", Constants.BASE_URL + "appointment&" + params.toString());
        CoreHttpClient.post("appointment", params, new HttpCallBackListener() {
            @Override
            public void onSuccess(JSONObject json) {
                dismiss();
                if (json.optInt("state") == 0 && json.optInt("isSuccessful") == 0) {
                    showToast("预约成功");
                    eqId = list.get(p).getEqId();
                    eqName = list.get(p).getEqName();
                    scanTime = time;
                    eqLat = list.get(p).getLatitude();
                    eqLng = list.get(p).getLongitude();

                    rlRoot.setVisibility(View.VISIBLE);
                    tvMsg.setText("您已成功预约" + list.get(p).getEqName() + " " + scanTime);
                    chargeListAdapter.isOrder = true;
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


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    public static final int WHAT_NAVIGATION = 1;
    public static final int WHAT_CHOOSE_BAIDU = 2;
    public static final int WHAT_CHOOSE_GAODE = 3;
    public static final int WHAT_SHOW_DATEPICKER = 4;
    public static final int WHAT_SELECT_TIME = 5;

    @OnClick(R.id.rl_root)
    public void onClick() {
        //已经预约的详情
        Intent intent1 = new Intent(getActivity(), ChargingPileActivity.class);
        intent1.putExtra("title", eqName);
        intent1.putExtra("eqId", eqId);
        intent1.putExtra("latitude", eqLat);
        intent1.putExtra("longitude", eqLng);
        intent1.putExtra("orderEqid", eqId);
        intent1.putExtra("scanTime", scanTime);
        startActivity(intent1);
    }
}
