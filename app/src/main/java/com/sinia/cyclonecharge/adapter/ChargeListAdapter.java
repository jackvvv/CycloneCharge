package com.sinia.cyclonecharge.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sinia.cyclonecharge.R;
import com.sinia.cyclonecharge.activity.ChargingPileActivity;
import com.sinia.cyclonecharge.bean.ChargeStationItemBean;
import com.sinia.cyclonecharge.bean.TelectricpileItemBean;
import com.sinia.cyclonecharge.utils.Constants;
import com.sinia.cyclonecharge.utils.DialogUtils;
import com.sinia.cyclonecharge.utils.MyApplication;
import com.sinia.cyclonecharge.utils.StringUtil;
import com.sinia.cyclonecharge.utils.ViewHolder;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import static com.sinia.cyclonecharge.R.id.iv_navigation;
import static com.sinia.cyclonecharge.fragment.ChargeListFragment.WHAT_NAVIGATION;
import static com.sinia.cyclonecharge.fragment.ChargeListFragment.WHAT_SHOW_DATEPICKER;
import static com.sinia.cyclonecharge.utils.MyApplication.context;

/**
 * Created by newLamp on 2016/11/3.
 */

public class ChargeListAdapter extends BaseAdapter {
    private Context mContext;
    private Handler mHandler;
    public boolean isOrder;
    public List<ChargeStationItemBean> data = new ArrayList<>();

    public ChargeListAdapter(Context context, Handler handler) {
        mContext = context;
        mHandler = handler;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_charge_list, null);
        }
        TextView tv_title = ViewHolder.get(convertView, R.id.tv_title);
        TextView tv_area = ViewHolder.get(convertView, R.id.tv_area);
        TextView tv_identifier = ViewHolder.get(convertView, R.id.tv_identifier);
        TextView tv_coordinate = ViewHolder.get(convertView, R.id.tv_coordinate);
        TextView tv_distance = ViewHolder.get(convertView, R.id.tv_distance);
        TextView tv_order = ViewHolder.get(convertView, R.id.tv_order);
        TextView tv_see_detail = ViewHolder.get(convertView, R.id.tv_see_detail);
        final ChargeStationItemBean bean = data.get(position);
//        tv_title.setText(bean.getEqName());
        tv_title.setText("长葛新区政府充电站");
        tv_area.setText(bean.getAddress());
        tv_identifier.setText("设备编号：" + bean.getEqnum());
        tv_coordinate.setText("设备坐标：" + bean.getLatitude() + "," + bean.getLongitude());
        DecimalFormat df = new DecimalFormat("######0.00");
        tv_distance.setText(df.format(bean.getDistance()) + "km");
        ImageView iv_navigation = ViewHolder.get(convertView, R.id.iv_navigation);
        iv_navigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message msg = mHandler.obtainMessage();
                msg.what = WHAT_NAVIGATION;
                msg.obj = bean;
                mHandler.sendMessage(msg);
            }
        });
        tv_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MyApplication.getInstance().getBoolValue(Constants.SP_HELPER.IS_LOGIN)) {
                    if (isOrder) {
                        DialogUtils.createShowHaveOrderDialog(mContext);
                    } else {
                        Message msg = new Message();
                        msg.arg1 = position;
                        msg.what = WHAT_SHOW_DATEPICKER;
                        mHandler.sendMessage(msg);
                    }
                } else {
                    Toast.makeText(mContext, mContext.getResources().getString(R.string.not_logged_in), Toast
                            .LENGTH_SHORT).show();
                }

            }
        });
        tv_see_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message msg = new Message();
                msg.arg1 = position;
                msg.what = 0x10;
                mHandler.sendMessage(msg);
            }
        });
        return convertView;
    }
}
