package com.sinia.cyclonecharge.adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sinia.cyclonecharge.R;
import com.sinia.cyclonecharge.utils.ViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by newLamp on 2016/11/4.
 */

public class MoneyAdapter extends BaseAdapter {
    private Context mContext;
    public List<String> data = new ArrayList<>();
    public int selectIndex = 2;
    private Handler mHandler;
    public MoneyAdapter(Context context,Handler handler) {
        mContext = context;
        mHandler = handler;
        data.add("30元");
        data.add("50元");
        data.add("100元");
        data.add("150元");
        data.add("200元");
        data.add("300元");
        data.add("400元");
        data.add("500元");
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_money, null);
        }
        TextView tv_money = ViewHolder.get(convertView, R.id.tv_money);
        tv_money.setText(data.get(position));
        if (selectIndex == position) {
            tv_money.setTextColor(Color.parseColor("#ffffff"));
            tv_money.setBackgroundResource(R.mipmap.bg_money2);
        }else{
            tv_money.setTextColor(Color.parseColor("#242424"));
            tv_money.setBackgroundResource(R.mipmap.bg_money1);
        }
        tv_money.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectIndex = position;
                Message msg = mHandler.obtainMessage();
                msg.what = 1;
                msg.arg1 = position;
                mHandler.sendMessage(msg);
                notifyDataSetChanged();
            }
        });
        return convertView;
    }
}
