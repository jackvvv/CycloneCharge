package com.sinia.cyclonecharge.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sinia.cyclonecharge.R;
import com.sinia.cyclonecharge.bean.AccountItemsBean;
import com.sinia.cyclonecharge.utils.ViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by newLamp on 2016/11/4.
 */

public class ConsumptionAdapter extends BaseAdapter {
    private Context mContext;
    public List<AccountItemsBean> data = new ArrayList<>();

    public ConsumptionAdapter(Context context) {
        mContext = context;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_consumption_record, null);
        }
        TextView tv_time = ViewHolder.get(convertView, R.id.tv_time);
        TextView tv_message = ViewHolder.get(convertView, R.id.tv_message);
        TextView tv_money = ViewHolder.get(convertView, R.id.tv_money);
        AccountItemsBean accountItemsBean = data.get(position);
        tv_time.setText(accountItemsBean.getConsumeTime());
        tv_message.setText(accountItemsBean.getContent());
        if (accountItemsBean.getAccStauts().equals("1")) {
            tv_message.setText("充电消费");
            tv_money.setText("-" + accountItemsBean.getMoney());
            tv_money.setTextColor(mContext.getResources().getColor(R.color.red));
        } else {
            tv_message.setText("充电余额");
            tv_money.setText("+" + accountItemsBean.getMoney());
            tv_money.setTextColor(mContext.getResources().getColor(R.color.blue));
        }
        return convertView;
    }
}
