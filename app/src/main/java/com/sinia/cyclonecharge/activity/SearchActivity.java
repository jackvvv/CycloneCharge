package com.sinia.cyclonecharge.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;
import com.sinia.cyclonecharge.R;
import com.sinia.cyclonecharge.adapter.ChargeListAdapter;
import com.sinia.cyclonecharge.base.BaseActivity;
import com.sinia.cyclonecharge.bean.ChargeStationItemBean;
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

/**
 * Created by 忧郁的眼神 on 2016/12/14 0014.
 */

public class SearchActivity extends BaseActivity {

    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.et_search)
    EditText etSearch;
    @Bind(R.id.listview)
    ListView listview;
    @Bind(R.id.tv_msg)
    TextView tvMsg;
    @Bind(R.id.rl_root)
    RelativeLayout rlRoot;

    private ChargeListAdapter chargeListAdapter;
    private ChargeStationItemBean chargeStationItemBean;
    private List<ChargeStationItemBean> list = new ArrayList<>();
    private String eqId;//成功预约的设备id
    private String scanTime;//成功预约的时间
    private String eqName;//成功预约的设备名称
    //宝铁龙4s店 118.718541,32.138804
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case WHAT_NAVIGATION:
                    int type = 0;
                    if (AppInfoUtil.isAvilible(SearchActivity.this, AppInfoUtil.BaiduPackageName)) {
                        type = 1;
                    }
                    if (AppInfoUtil.isAvilible(SearchActivity.this, AppInfoUtil.GaodePackageName)) {
                        type = 2;
                    }
                    if (AppInfoUtil.isAvilible(SearchActivity.this, AppInfoUtil.BaiduPackageName) && AppInfoUtil
                            .isAvilible(SearchActivity.this, AppInfoUtil.GaodePackageName)) {
                        type = 3;
                    }
                    DialogUtils.createChooseNaviDialog(SearchActivity.this, handler, type);
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
                    DialogUtils.createDatePickerDialog(SearchActivity.this, handler, position);
                    break;
                case WHAT_SELECT_TIME:
                    //选择时间成功
                    String s_time = msg.obj + "";
                    String status_old = list.get(msg.arg1).getIsState();
                    String status_new = list.get(msg.arg1).getStatee();
                    if ((!StringUtil.isEmpty(status_old) && status_old.equals("3")) || (!StringUtil.isEmpty
                            (status_new) && status_old.equals("0"))) {
                        appointment(s_time, msg.arg1);
                    } else {
                        showToast("充电桩处于非空闲状态，暂时不可预约");
                    }
                    break;
                case 0x10:
                    Intent intent1 = new Intent(SearchActivity.this, ChargingPileActivity.class);
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        back(back);
        initData();
    }

    private void initData() {
        chargeListAdapter = new ChargeListAdapter(this, handler);
        listview.setAdapter(chargeListAdapter);
        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (StringUtil.isEmpty(etSearch.getEditableText().toString().trim())) {
                        showToast("请输入搜索关键词");
                    } else {
                        search();
                    }
                }
                return false;
            }
        });
    }

    private void search() {
        showLoad("");
        RequestParams params = new RequestParams();
        params.put("latitude", 34.198600 + "");
        params.put("longitude", 113.819400 + "");
        params.put("searchContent", etSearch.getEditableText().toString().trim());
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
                    UserCoordinateList userCoordinateList = gson.fromJson(json.toString(), UserCoordinateList.class);
                    eqId = userCoordinateList.getEqId();
                    eqName = userCoordinateList.getEqName();
                    scanTime = userCoordinateList.getScanTime();
                    if (null != userCoordinateList && 0 != userCoordinateList.getChargeStationItem().size()) {
                        if (!StringUtil.isEmpty(userCoordinateList.getEqId())) {
                            rlRoot.setVisibility(View.VISIBLE);
                            tvMsg.setText("您已成功预约" + userCoordinateList.getEqName() + " " + userCoordinateList
                                    .getScanTime());
                            chargeListAdapter.isOrder = true;
                        } else {
                            chargeListAdapter.isOrder = false;
                        }
                        Log.d("lamp", "chargeListAdapter.isOrder=" + chargeListAdapter.isOrder);
                        list = userCoordinateList.getChargeStationItem();
                        chargeListAdapter.data.clear();
                        chargeListAdapter.data.addAll(userCoordinateList.getChargeStationItem());
                        chargeListAdapter.notifyDataSetChanged();
                    } else {
                        showToast("未查询到相关数据");
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

    private void appointment(String time, int p) {
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

    public static final int WHAT_NAVIGATION = 1;
    public static final int WHAT_CHOOSE_BAIDU = 2;
    public static final int WHAT_CHOOSE_GAODE = 3;
    public static final int WHAT_SHOW_DATEPICKER = 4;
    public static final int WHAT_SELECT_TIME = 5;
}
