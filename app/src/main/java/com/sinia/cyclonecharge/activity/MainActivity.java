package com.sinia.cyclonecharge.activity;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sinia.cyclonecharge.R;
import com.sinia.cyclonecharge.base.BaseActivity;
import com.sinia.cyclonecharge.fragment.ChargeFragment;
import com.sinia.cyclonecharge.fragment.ChargingFragment;
import com.sinia.cyclonecharge.fragment.HomeFragment;
import com.sinia.cyclonecharge.fragment.MineFragment;
import com.sinia.cyclonecharge.fragment.NewsFragment;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class MainActivity extends BaseActivity {

    @Bind(R.id.fragment_container)
    RelativeLayout fragmentContainer;
    @Bind(R.id.iv_home)
    ImageView ivHome;
    @Bind(R.id.tv_home)
    TextView tvHome;
    @Bind(R.id.ll_home)
    LinearLayout llHome;
    @Bind(R.id.iv_charge)
    ImageView ivCharge;
    @Bind(R.id.tv_charge)
    TextView tvCharge;
    @Bind(R.id.ll_charge)
    LinearLayout llCharge;
    @Bind(R.id.iv_news)
    ImageView ivNews;
    @Bind(R.id.tv_news)
    TextView tvNews;
    @Bind(R.id.ll_news)
    LinearLayout llNews;
    @Bind(R.id.iv_mine)
    ImageView ivMine;
    @Bind(R.id.tv_mine)
    TextView tvMine;
    @Bind(R.id.ll_mine)
    LinearLayout llMine;
    @Bind(R.id.ll_bottom)
    LinearLayout llBottom;

    int currentSelect;
    private FragmentManager mFragmentManager;
    private HomeFragment homeFragment;
    private ChargeFragment chargeFragment;
    private NewsFragment newsFragment;
    private MineFragment mineFragment;
    private ChargingFragment chargingFragment;

    public static double currentLatitude, currentLongitude;
    public static String address;
    public static final String ACTION_SHOWCHARHING = "showCharhing";

    LocalBroadcastManager broadcastManager;
    IntentFilter intentFilter;
    BroadcastReceiver mReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initData();
    }

    private void initData() {
        mFragmentManager = getFragmentManager();
        homeFragment = new HomeFragment();
        mFragmentManager.beginTransaction().add(R.id.fragment_container, homeFragment).show(homeFragment).commit();
        changeView();
        initBroad();
    }

    private void initBroad() {
        broadcastManager = LocalBroadcastManager.getInstance(this);
        intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_SHOWCHARHING);
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                //收到广播后所作的操作
                if (intent.getAction().equals(ACTION_SHOWCHARHING)) {
                    boolean showWhat = intent.getBooleanExtra("showWhat",false);
                    if(showWhat){
                        if (chargingFragment == null) {
                            chargingFragment = new ChargingFragment();
                        }
                        if (!chargingFragment.isAdded()) {
                            mFragmentManager.beginTransaction().add(R.id.fragment_container, chargingFragment).commitAllowingStateLoss();
                        }
                        hideFragments2(homeFragment, chargeFragment, mineFragment, newsFragment);
                        showFragment2(chargingFragment);
                    }else{
                        mFragmentManager.beginTransaction().remove(chargingFragment);
                        hideFragments2(homeFragment, newsFragment, mineFragment,chargingFragment);
                        showFragment2(chargeFragment);
                    }
                }
            }
        };
        broadcastManager.registerReceiver(mReceiver, intentFilter);
    }

    @OnClick({R.id.ll_home, R.id.ll_charge, R.id.ll_news, R.id.ll_mine})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_home:
                currentSelect = 0;
                hideFragments(chargeFragment, newsFragment, mineFragment, chargingFragment);
                showFragment(homeFragment);
                break;
            case R.id.ll_charge:
                currentSelect = 1;
                if (chargeFragment == null) {
                    chargeFragment = new ChargeFragment();
                }
                if (!chargeFragment.isAdded()) {
                    mFragmentManager.beginTransaction().add(R.id.fragment_container, chargeFragment).commit();
                }
                hideFragments(homeFragment, newsFragment, mineFragment,chargingFragment);
                showFragment(chargeFragment);
                break;
            case R.id.ll_news:
                currentSelect = 2;
                if (newsFragment == null) {
                    newsFragment = new NewsFragment();
                }
                if (!newsFragment.isAdded()) {
                    mFragmentManager.beginTransaction().add(R.id.fragment_container, newsFragment).commit();
                }
                hideFragments(homeFragment, chargeFragment, mineFragment, chargingFragment);
                showFragment(newsFragment);
                break;
            case R.id.ll_mine:
                currentSelect = 3;
                if (mineFragment == null) {
                    mineFragment = new MineFragment();
                }
                if (!mineFragment.isAdded()) {
                    mFragmentManager.beginTransaction().add(R.id.fragment_container, mineFragment).commit();
                }
                hideFragments(homeFragment, chargeFragment, newsFragment, chargingFragment);
                showFragment(mineFragment);
                break;
            default:
                break;
        }
        changeView();
    }

    private void showFragment(Fragment fragment) {
        FragmentTransaction ft = mFragmentManager.beginTransaction();
        ft.show(fragment).commit();
    }

    private void showFragment2(Fragment fragment) {
        FragmentTransaction ft = mFragmentManager.beginTransaction();
        ft.show(fragment).commitAllowingStateLoss();
    }

    private void hideFragments(Fragment... event) {
        FragmentTransaction ft = mFragmentManager.beginTransaction();
        for (int i = 0; i < event.length; i++) {
            if (null != event[i]) {
                ft.hide(event[i]);
            }
        }
        ft.commit();
    }


    private void hideFragments2(Fragment... event) {
        FragmentTransaction ft = mFragmentManager.beginTransaction();
        for (int i = 0; i < event.length; i++) {
            if (null != event[i]) {
                ft.hide(event[i]);
            }
        }
        ft.commitAllowingStateLoss();
    }
    private void changeView() {
        switch (currentSelect) {
            case 0:
                ivHome.setImageResource(R.mipmap.home_click);
                tvHome.setTextColor(Color.parseColor("#1EE577"));
                ivCharge.setImageResource(R.mipmap.charge_unclick);
                tvCharge.setTextColor(Color.parseColor("#9D9D9D"));
                ivNews.setImageResource(R.mipmap.news_unclick);
                tvNews.setTextColor(Color.parseColor("#9D9D9D"));
                ivMine.setImageResource(R.mipmap.mine_unclick);
                tvMine.setTextColor(Color.parseColor("#9D9D9D"));
                break;
            case 1:
                ivHome.setImageResource(R.mipmap.home_unclick);
                tvHome.setTextColor(Color.parseColor("#9D9D9D"));
                ivCharge.setImageResource(R.mipmap.charge_click);
                tvCharge.setTextColor(Color.parseColor("#1EE577"));
                ivNews.setImageResource(R.mipmap.news_unclick);
                tvNews.setTextColor(Color.parseColor("#9D9D9D"));
                ivMine.setImageResource(R.mipmap.mine_unclick);
                tvMine.setTextColor(Color.parseColor("#9D9D9D"));
                break;
            case 2:
                ivHome.setImageResource(R.mipmap.home_unclick);
                tvHome.setTextColor(Color.parseColor("#9D9D9D"));
                ivCharge.setImageResource(R.mipmap.charge_unclick);
                tvCharge.setTextColor(Color.parseColor("#9D9D9D"));
                ivNews.setImageResource(R.mipmap.news_click);
                tvNews.setTextColor(Color.parseColor("#1EE577"));
                ivMine.setImageResource(R.mipmap.mine_unclick);
                tvMine.setTextColor(Color.parseColor("#9D9D9D"));
                break;
            case 3:
                ivHome.setImageResource(R.mipmap.home_unclick);
                tvHome.setTextColor(Color.parseColor("#9D9D9D"));
                ivCharge.setImageResource(R.mipmap.charge_unclick);
                tvCharge.setTextColor(Color.parseColor("#9D9D9D"));
                ivNews.setImageResource(R.mipmap.news_unclick);
                tvNews.setTextColor(Color.parseColor("#9D9D9D"));
                ivMine.setImageResource(R.mipmap.mine_click);
                tvMine.setTextColor(Color.parseColor("#1EE577"));
                break;
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        broadcastManager.unregisterReceiver(mReceiver);
    }
}
