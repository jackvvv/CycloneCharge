package com.sinia.cyclonecharge.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.sinia.cyclonecharge.R;
import com.sinia.cyclonecharge.base.BaseFragment;
import com.sinia.cyclonecharge.zxing.CaptureActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Jin on 2016/11/3.
 */

public class ChargeFragment extends BaseFragment {
    @Bind(R.id.iv_scan)
    ImageView ivScan;
    private View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_charge, null);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick(R.id.iv_scan)
    public void onClick() {
        startActivityForNoIntent(CaptureActivity.class);
    }
}
