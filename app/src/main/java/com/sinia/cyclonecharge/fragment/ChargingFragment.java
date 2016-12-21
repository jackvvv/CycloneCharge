package com.sinia.cyclonecharge.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bcgtgjyb.huanwen.customview.mylibrary.WindowsLoad2;
import com.gelitenight.waveview.library.WaveHelper;
import com.sinia.cyclonecharge.R;
import com.sinia.cyclonecharge.activity.MainActivity;
import com.sinia.cyclonecharge.base.BaseFragment;
import com.sinia.cyclonecharge.utils.DialogUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by newLamp on 2016/11/4.
 */

public class ChargingFragment extends BaseFragment {
    @Bind(R.id.tv_time)
    TextView tvTime;
    @Bind(R.id.tv_money)
    TextView tvMoney;
    @Bind(R.id.tv_electricity)
    TextView tvElectricity;
    @Bind(R.id.tv_voltage)
    TextView tvVoltage;
    @Bind(R.id.tv_current)
    TextView tvCurrent;
    @Bind(R.id.tv_finish)
    TextView tvFinish;
    @Bind(R.id.windowsLoad)
    WindowsLoad2 windowsLoad;
    @Bind(R.id.waveView)
    com.gelitenight.waveview.library.WaveView waveView;
    @Bind(R.id.tv_progress)
    TextView tvProgress;
    private View rootView;
    private WaveHelper mWaveHelper;
    private float s;
    private float s1;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    tvProgress.setText(s + "%");
                    mWaveHelper.setProgress(s1, s);
                    break;
                case 2:
                    mWaveHelper.cancel();
                    break;
                case 3:
                    windowsLoad.loading();
                    break;
            }
        }
    };
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_charging, null);
        ButterKnife.bind(this, rootView);
//        DialogUtils.createRemainLessDialog(getActivity());
        initView();
        return rootView;
    }

    private void initView(){
        waveView.setBorder(1, Color.parseColor("#FFFFFF"));
        mWaveHelper = new WaveHelper(waveView);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("lamp","ChargingFragment---onResume");
        new Thread() {
            @Override
            public void run() {
                while (true) {
                    try {
                        s1 = s;
                        s = s + 10;
                        mHandler.sendEmptyMessage(1);
                        if (s == 60) {
//                        mHandler.sendEmptyMessage(2);
                            return;
                        }
                        sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
//        windowsLoad.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        mWaveHelper.cancel();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick(R.id.tv_finish)
    public void onClick() {
//        Intent intent = new Intent(MainActivity.ACTION_SHOWCHARHING);
//        intent.putExtra("showWhat", false);
//        LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
        mHandler.sendEmptyMessageDelayed(3,0);
    }
}
