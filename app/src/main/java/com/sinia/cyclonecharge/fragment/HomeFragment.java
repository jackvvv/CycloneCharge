package com.sinia.cyclonecharge.fragment;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.sinia.cyclonecharge.R;
import com.sinia.cyclonecharge.activity.LoginRegisterActivity;
import com.sinia.cyclonecharge.activity.SearchActivity;
import com.sinia.cyclonecharge.base.BaseFragment;
import com.sinia.cyclonecharge.libraryfragmenttransactionextended.FragmentTransactionExtended;
import com.sinia.cyclonecharge.utils.Constants;
import com.sinia.cyclonecharge.utils.MyApplication;
import com.sinia.cyclonecharge.zxing.CaptureActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.sinia.cyclonecharge.R.id.et_search;
import static com.sinia.cyclonecharge.R.id.iv_list_or_map;

/**
 * Created by newLamp on 2016/11/2.
 */

public class HomeFragment extends BaseFragment {
    @Bind(R.id.iv_query)
    ImageView ivQuery;
    @Bind(et_search)
    TextView etSearch;
    @Bind(R.id.frame_content)
    FrameLayout frameContent;
    private View rootView;
    private FragmentManager mFragmentManager;
    private MapFragment mapFragment;
    private ChargeListFragment chargeListFragment;
    public static boolean showFlag = true;
    public static ImageView iv_list_or_map;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_home, null);
        ButterKnife.bind(this, rootView);
        initData();
        return rootView;
    }

    private void initData() {
        iv_list_or_map = (ImageView) rootView.findViewById(R.id.iv_list_or_map);
        mFragmentManager = getFragmentManager();
        mapFragment = new MapFragment();
        mFragmentManager.beginTransaction().replace(R.id.frame_content, mapFragment).commit();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.iv_query, R.id.iv_list_or_map, R.id.et_search})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_query:
                startActivityForNoIntent(CaptureActivity.class);
                break;
            case R.id.iv_list_or_map:
                if (showFlag) {
                    showFlag = false;
                    if (MyApplication.getInstance().getBoolValue(Constants.SP_HELPER.IS_LOGIN)) {
                        if (chargeListFragment == null) {
                            chargeListFragment = new ChargeListFragment();
                        }
                        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
                        FragmentTransactionExtended fragmentTransactionExtended = new FragmentTransactionExtended
                                (getActivity(), fragmentTransaction, mapFragment,
                                        chargeListFragment, R.id.frame_content);
                        fragmentTransactionExtended.addTransition(7, "");
                        fragmentTransactionExtended.commit();
                        iv_list_or_map.setImageResource(R.mipmap.change_map_icon);
                    } else {
                        startActivityForNoIntent(LoginRegisterActivity.class);
                    }

                } else {
                    showFlag = true;
                    mFragmentManager.popBackStack();
                    iv_list_or_map.setImageResource(R.mipmap.icon_list);
                }
                break;
            case R.id.et_search:
                if (MyApplication.getInstance().getBoolValue(Constants.SP_HELPER.IS_LOGIN)) {
                    startActivityForNoIntent(SearchActivity.class);
                } else {
                    startActivityForNoIntent(LoginRegisterActivity.class);
                }

                break;
        }
    }

}
