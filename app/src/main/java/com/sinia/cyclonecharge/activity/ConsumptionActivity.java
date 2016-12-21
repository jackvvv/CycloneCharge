package com.sinia.cyclonecharge.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;
import com.sinia.cyclonecharge.R;
import com.sinia.cyclonecharge.adapter.ConsumptionAdapter;
import com.sinia.cyclonecharge.base.BaseActivity;
import com.sinia.cyclonecharge.bean.AccountItemsList;
import com.sinia.cyclonecharge.http.CoreHttpClient;
import com.sinia.cyclonecharge.http.HttpCallBackListener;
import com.sinia.cyclonecharge.utils.MyApplication;

import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by newLamp on 2016/11/4.
 */

public class ConsumptionActivity extends BaseActivity {
    @Bind(R.id.listview)
    ListView listview;
    private ConsumptionAdapter consumptionAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consumption, "消费记录");
        ButterKnife.bind(this);
        getDoingView().setVisibility(View.GONE);
        initView();
        moneyList();
    }

    private void initView() {
        consumptionAdapter = new ConsumptionAdapter(this);
        listview.setAdapter(consumptionAdapter);
    }

    private void moneyList() {
        showLoad("");
        RequestParams params = new RequestParams();
        params.put("userId", MyApplication.getInstance().getLoginBean().getUserId());
        LogRequestUrl("moneyList&" + params.toString());
        CoreHttpClient.post("moneyList", params, new HttpCallBackListener() {
            @Override
            public void onSuccess(JSONObject json) {
                dismiss();
                if (json.optInt("state") == 0 && json.optInt("isSuccessful") == 0) {
                    Gson gson = new Gson();
                    AccountItemsList list = gson.fromJson(json.toString(), AccountItemsList.class);
                    consumptionAdapter.data.clear();
                    consumptionAdapter.data.addAll(list.getAccountItems());
                    consumptionAdapter.notifyDataSetChanged();
                } else {

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
