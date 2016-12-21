package com.sinia.cyclonecharge.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.services.poisearch.PoiSearch;
import com.bumptech.glide.Glide;
import com.sinia.cyclonecharge.R;
import com.sinia.cyclonecharge.bean.NewsItemsBean;
import com.sinia.cyclonecharge.utils.AppInfoUtil;
import com.sinia.cyclonecharge.utils.Utils;
import com.sinia.cyclonecharge.utils.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import static com.sinia.cyclonecharge.utils.MyApplication.context;

/**
 * Created by Jin on 2016/11/3.
 */

public class NewsAdapter extends BaseAdapter {
    private Context mContext;

    public List<NewsItemsBean> data = new ArrayList<>();

    public NewsAdapter(Context context) {
        mContext = context;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_news, null);
        }
        ImageView iv_news = ViewHolder.get(view, R.id.iv_news);
        TextView tv_title = ViewHolder.get(view, R.id.tv_title);
        TextView tv_time = ViewHolder.get(view, R.id.tv_time);
        ImageView iv_news2 = ViewHolder.get(view, R.id.iv_news2);
        TextView tv_title2 = ViewHolder.get(view, R.id.tv_title2);
        TextView tv_time2 = ViewHolder.get(view, R.id.tv_time2);
        LinearLayout ll_root = ViewHolder.get(view, R.id.ll_root);
        RelativeLayout root = ViewHolder.get(view, R.id.root);
        NewsItemsBean bean = data.get(i);
        if (i == 0) {
            root.setVisibility(View.VISIBLE);
            ll_root.setVisibility(View.GONE);
            setWidthAndHeight(root);
            Glide.with(mContext).load(bean.getPath()).centerCrop().placeholder(R.mipmap.default_img).crossFade().into
                    (iv_news2);
            tv_title2.setText(bean.getTitle());
            tv_time2.setText(bean.getTime());
        } else {
            root.setVisibility(View.GONE);
            ll_root.setVisibility(View.VISIBLE);
            Glide.with(mContext).load(bean.getPath()).centerCrop().placeholder(R.mipmap.default_img).crossFade().into
                    (iv_news);
            tv_title.setText(bean.getTitle());
            tv_time.setText(bean.getTime());
        }

        return view;
    }

    private void setWidthAndHeight(View view) {
        int width = AppInfoUtil.getScreenWidth(mContext);
        view.getLayoutParams().width = width;
        view.getLayoutParams().height = width * 4 / 9;
    }
}
