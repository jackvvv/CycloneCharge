package com.sinia.cyclonecharge.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.Projection;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.animation.Animation;
import com.amap.api.maps.model.animation.RotateAnimation;
import com.amap.api.maps.model.animation.ScaleAnimation;
import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;
import com.sinia.cyclonecharge.R;
import com.sinia.cyclonecharge.activity.ChargingPileActivity;
import com.sinia.cyclonecharge.activity.LoginRegisterActivity;
import com.sinia.cyclonecharge.activity.MainActivity;
import com.sinia.cyclonecharge.base.BaseFragment;
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
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.sinia.cyclonecharge.activity.MainActivity.address;
import static com.sinia.cyclonecharge.activity.MainActivity.currentLatitude;
import static com.sinia.cyclonecharge.activity.MainActivity.currentLongitude;

/**
 * Created by newLamp on 2016/11/2.
 */

public class MapFragment extends BaseFragment implements AMap.OnMarkerClickListener, AMap
        .OnInfoWindowClickListener, AMap.InfoWindowAdapter, AMap.OnMapClickListener {
    @Bind(R.id.map)
    MapView mapView;
    @Bind(R.id.iv_location)
    ImageView ivLocation;
    @Bind(R.id.jia)
    ImageView jia;
    @Bind(R.id.jian)
    ImageView jian;
    private View rootView;
    private AMap aMap;
    private UiSettings mUiSettings;
    private AMapLocationClient mlocationClient;
    private Marker locationMarker;
    //声明定位回调监听器
    public AMapLocationListener mLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation amapLocation) {
            dismiss();
            if (amapLocation != null) {
                if (amapLocation != null
                        && amapLocation.getErrorCode() == 0) {
                    Log.d("lamp", "onLocationChanged---success");
                    //取出经纬度
                    LatLng latLng = new LatLng(amapLocation.getLatitude(), amapLocation.getLongitude());
                    currentLatitude = amapLocation.getLatitude();
                    currentLongitude = amapLocation.getLongitude();
                    address = amapLocation.getAddress();
                    //添加Marker显示定位位置
//                    if (locationMarker == null) {
                    //如果是空的添加一个新的,icon方法就是设置定位图标，可以自定义
                    locationMarker = aMap.addMarker(new MarkerOptions()
                            .position(latLng)
                            .icon(BitmapDescriptorFactory.fromResource(R.mipmap.icon_position)));
                    //然后可以移动到定位点,使用animateCamera就有动画效果
                    aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14));
                } else {
                    String errText = "定位失败," + amapLocation.getErrorCode() + ": " + amapLocation.getErrorInfo();
                    Log.e("AmapErr", errText);
                }
            }
        }
    };
    private AMapLocationClientOption mLocationOption;
    private UserCoordinateList userCoordinateList = new UserCoordinateList();
    private HashMap<String, ChargeStationItemBean> beanHashMap = new HashMap<>();
    private String eqId;//成功预约的设备id
    private String scanTime;//成功预约的时间
    private String eqName;//成功预约的设备名称
    private ChargeStationItemBean chargeStationItemBean;//导航bean
    private List<ChargeStationItemBean> list = new ArrayList<>();
    public static final int WHAT_NAVIGATION = 1;
    public static final int WHAT_CHOOSE_BAIDU = 2;
    public static final int WHAT_CHOOSE_GAODE = 3;
    public static final int WHAT_SHOW_DATEPICKER = 4;
    public static final int WHAT_SELECT_TIME = 5;
    private boolean isOrder = false;
    private Marker cuMarker;
    private ArrayList<Marker> markerList;

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
                    chargeStationItemBean = (ChargeStationItemBean) msg.obj;
                    DialogUtils.createDatePickerDialog2(getActivity(), handler);
                    break;
                case WHAT_SELECT_TIME:
                    //选择时间成功
                    String s_time = msg.obj + "";
                    String cur_date = StringUtil.getCurYearAndMonth2();
                    String time = cur_date + " " + s_time;
                    appointment(time);
                    break;
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_map, null);
        ButterKnife.bind(this, rootView);
        mapView.onCreate(savedInstanceState);
        initMap();
        chargeStationList();
        Log.d("lamp", "MapFragment---onCreateView");
        return rootView;
    }

    private void initMap() {
//        if (aMap == null) {
        aMap = mapView.getMap();
//        }
        mUiSettings = aMap.getUiSettings();
        mUiSettings.setCompassEnabled(false);
        mUiSettings.setMyLocationButtonEnabled(false); // 显示默认的定位按钮
        mUiSettings.setScaleControlsEnabled(false);//显示比例尺控件
        mUiSettings.setZoomControlsEnabled(false);//显示放大缩小控件
//        aMap.setTrafficEnabled(true);//显示实时路况图层
        aMap.setMyLocationEnabled(false);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        aMap.moveCamera(CameraUpdateFactory.zoomTo(14));
        aMap.setOnMarkerClickListener(this);
        aMap.setOnInfoWindowClickListener(this);
        aMap.setInfoWindowAdapter(this);
        aMap.setOnMapClickListener(this);
        mlocationClient = new AMapLocationClient(getActivity().getApplicationContext());
        mlocationClient.setLocationListener(mLocationListener);
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        mLocationOption.setOnceLocation(true);
//        mLocationOption.setInterval(5000);//5000ms定位一次
        mlocationClient.setLocationOption(mLocationOption);
        mlocationClient.startLocation();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
                    list = userCoordinateList.getChargeStationItem();
                    eqId = userCoordinateList.getEqId();
                    eqName = userCoordinateList.getEqName();
                    scanTime = userCoordinateList.getScanTime();
                    if (!StringUtil.isEmpty(eqId)) {
                        isOrder = true;
                    } else {
                        isOrder = false;
                    }
                    addMarker();
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

    private void appointment(String time) {
        showLoad("");
        RequestParams params = new RequestParams();
        params.put("userId", MyApplication.getInstance().getLoginBean().getUserId());
        params.put("eqId", chargeStationItemBean.getEqId());
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

    private void addMarker() {
        ArrayList<MarkerOptions> markerOptionlst = new ArrayList<MarkerOptions>();
        for (int i = 0; i < userCoordinateList.getChargeStationItem().size(); i++) {
            ChargeStationItemBean bean = userCoordinateList.getChargeStationItem().get(i);
            beanHashMap.put(bean.getEqnum(), bean);
            MarkerOptions markerOptions = new MarkerOptions();
            LatLng latLng = new LatLng(bean.getLatitude(),
                    bean.getLongitude());

            markerOptions.position(latLng);
            markerOptions.draggable(false);
//            markerOptions.title(bean.getEqnum());
//            markerOptions.title("长葛市新区政府充电站");
//            markerOptions.snippet(bean.getEqnum());

            String status_old = bean.getIsState();
            String status_new = bean.getStatee();
            if ((!StringUtil.isEmpty(status_old) && status_old.equals("3")) || (!StringUtil.isEmpty
                    (status_new) && status_new.equals("0"))) {
                //空闲
                markerOptions.icon(BitmapDescriptorFactory
                        .fromResource(R.mipmap.charge_clicked));
            }
            if ((!StringUtil.isEmpty(status_old) && status_old.equals("1") || status_old.equals("0")) || (!StringUtil
                    .isEmpty
                            (status_new) && status_new.equals("1"))) {
                //充点钟
                markerOptions.icon(BitmapDescriptorFactory
                        .fromResource(R.mipmap.ic_charging));
            }
            if ((!StringUtil.isEmpty(status_old) && status_old.equals("2")) || (!StringUtil.isEmpty
                    (status_new) && status_new.equals("2"))) {
                //已预约
                markerOptions.icon(BitmapDescriptorFactory
                        .fromResource(R.mipmap.ic_ordered));
            }
            markerOptionlst.add(markerOptions);
            aMap.addMarker(markerOptions);
        }
        markerList = aMap.addMarkers(markerOptionlst, true);
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
        Log.d("lamp", "MapFragment---onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
        Log.d("lamp", "MapFragment---onPause");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mapView != null) {
            mapView.onSaveInstanceState(outState);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("lamp", "MapFragment---onDestroy");
        if (mapView != null) {
            mapView.onDestroy();
        }
    }

    @OnClick({R.id.iv_location, R.id.jia, R.id.jian})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_location:
                showLoad("定位中...");
                mlocationClient.startLocation();
                break;
            case R.id.jia:
                aMap.animateCamera(CameraUpdateFactory.zoomIn());
                break;
            case R.id.jian:
                aMap.animateCamera(CameraUpdateFactory.zoomOut());
                break;
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        //点击大头针
        for (int i = 0; i < markerList.size(); i++) {
            if (marker.equals(markerList.get(i))) {
                cuMarker = marker;
                if (aMap != null) {
                    jumpPoint(marker, i);
                }
                createInfoWindowDialog(i);
            }
        }
        return false;
    }

    private void jumpPoint(final Marker marker, int position) {
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        Projection proj = aMap.getProjection();

        final ChargeStationItemBean itemBean = userCoordinateList.getChargeStationItem().get(position);
        final LatLng latlng = new LatLng(itemBean.getLatitude(), itemBean.getLongitude());
        Point startPoint = proj.toScreenLocation(latlng);
        startPoint.offset(0, -100);
        final LatLng startLatLng = proj.fromScreenLocation(startPoint);
        final long duration = 1500;

        final Interpolator interpolator = new BounceInterpolator();
        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                float t = interpolator.getInterpolation((float) elapsed
                        / duration);
                double lng = t * latlng.longitude + (1 - t)
                        * startLatLng.longitude;
                double lat = t * latlng.latitude + (1 - t)
                        * startLatLng.latitude;
                marker.setPosition(new LatLng(lat, lng));
                if (t < 1.0) {
                    handler.postDelayed(this, 16);
                } else {
                    marker.showInfoWindow();
                }
            }
        });
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        //点击气泡
        if (marker.isInfoWindowShown()) {
            marker.hideInfoWindow();
        }
    }

    private Dialog dialog;

    private void createInfoWindowDialog(int position) {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View v = inflater.inflate(R.layout.layout_map_infowindow, null);
        dialog = new Dialog(getActivity(), R.style.ActionSheetDialogStyle3);
        dialog.show();
        Window window = dialog.getWindow();
        window.setGravity(Gravity.BOTTOM); // 此处可以设置dialog显示的位置
        WindowManager windowManager = ((Activity) getActivity()).getWindowManager();
        final Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = display.getWidth();
        dialog.getWindow().setAttributes(lp);
        dialog.setContentView(v, lp);
        TextView tv_no = (TextView) v.findViewById(R.id.tv_no);
        TextView tv_location = (TextView) v.findViewById(R.id.tv_location);
        TextView tv_order = (TextView) v.findViewById(R.id.tv_order);
        TextView tv_go = (TextView) v.findViewById(R.id.tv_go);
        ImageView img_close = (ImageView) v.findViewById(R.id.img_close);
        RelativeLayout rl_detail = (RelativeLayout) v.findViewById(R.id.rl_detail);

//        final ChargeStationItemBean bean = beanHashMap.get(marker.getTitle());
//        final ChargeStationItemBean bean = beanHashMap.get(marker.getSnippet());
        final ChargeStationItemBean bean = userCoordinateList.getChargeStationItem().get(position);
        tv_no.setText("设备编号：" + bean.getEqnum());
        tv_location.setText("设备坐标：" + bean.getLatitude() + "," + bean.getLongitude());
        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        tv_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MyApplication.getInstance().getBoolValue(Constants.SP_HELPER.IS_LOGIN)) {
                    if (isOrder) {
                        DialogUtils.createShowHaveOrderDialog(getActivity());
                    } else {
                        String status_old = bean.getIsState();
                        String status_new = bean.getStatee();
                        if ((!StringUtil.isEmpty(status_old) && status_old.equals("3")) || (!StringUtil.isEmpty
                                (status_new) && status_new.equals("0"))) {
                            Message msg = new Message();
                            msg.obj = bean;
                            msg.what = WHAT_SHOW_DATEPICKER;
                            handler.sendMessage(msg);
                        } else {
                            showToast("充电桩处于非空闲状态，暂时不可预约");
                        }
                    }
                } else {
                    startActivityForNoIntent(LoginRegisterActivity.class);
                }
                dialog.dismiss();
            }
        });
        tv_go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                NavigationUtils.Navigation(latlng);
                Message msg = handler.obtainMessage();
                msg.what = WHAT_NAVIGATION;
                msg.obj = bean;
                handler.sendMessage(msg);
                dialog.dismiss();
            }
        });
        rl_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MyApplication.getInstance().getBoolValue(Constants.SP_HELPER.IS_LOGIN)) {
                    Intent intent1 = new Intent(getActivity(), ChargingPileActivity.class);
                    intent1.putExtra("title", bean.getEqName());
                    intent1.putExtra("eqId", bean.getEqId());
                    intent1.putExtra("latitude", bean.getLatitude());
                    intent1.putExtra("longitude", bean.getLongitude());
                    intent1.putExtra("orderEqid", eqId);
                    intent1.putExtra("scanTime", scanTime);
                    startActivity(intent1);
                } else {
                    startActivityForNoIntent(LoginRegisterActivity.class);
                }
                dialog.dismiss();
            }
        });
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
//        View v = LayoutInflater.from(getActivity()).inflate(R.layout.layout_map_infowindow, null);
//        render(marker, v);
//        这两个回调方法，首先检测getInfoWindow是否返回null，如果非null，则不检测getInfoContents，否则检测getInfoContents的返回值
        return null;
    }

    @Override
    public void onMapClick(LatLng latLng) {
        if (cuMarker.isInfoWindowShown()) {
            cuMarker.hideInfoWindow();
        }
    }
}
