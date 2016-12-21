package com.sinia.cyclonecharge.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;
import com.sinia.cyclonecharge.R;
import com.sinia.cyclonecharge.activity.NewsDetailActivity;
import com.sinia.cyclonecharge.adapter.NewsAdapter;
import com.sinia.cyclonecharge.base.BaseFragment;
import com.sinia.cyclonecharge.bean.NewsItemsBean;
import com.sinia.cyclonecharge.bean.NewsItemsList;
import com.sinia.cyclonecharge.http.CoreHttpClient;
import com.sinia.cyclonecharge.http.HttpCallBackListener;
import com.sinia.cyclonecharge.utils.Constants;
import com.sinia.cyclonecharge.view.XListView;

import org.json.JSONObject;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.sinia.cyclonecharge.R.id.listView;

/**
 * Created by Jin on 2016/11/3.
 */

public class NewsFragment extends BaseFragment {
    private XListView listview;
    private View rootView;
    private NewsAdapter newsAdapter;
    private ImageView iv_news;
    private TextView tv_title, tv_time;
    private NewsItemsBean newsItemsBean;
    private int pageNo = 1;
    private final int pageSize = 5;
    private boolean refreshOrLoad = true;
    public static long time;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    pageNo = 1;
                    refreshOrLoad = true;
                    newsList();
                    break;
                case 2:
                    refreshOrLoad = false;
                    newsList();
                    break;
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_news, null);
        initView();
        newsList();
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void initView() {
        time = System.currentTimeMillis();
        listview = (XListView) rootView.findViewById(listView);
        newsAdapter = new NewsAdapter(getActivity());
        View headView = LayoutInflater.from(getActivity()).inflate(R.layout.head_layout_news, null);
        RelativeLayout root = (RelativeLayout) headView.findViewById(R.id.root);
        ViewGroup.LayoutParams params = root.getLayoutParams();
        Display display = getActivity().getWindowManager().getDefaultDisplay(); // 为获取屏幕宽、高
        params.width = display.getWidth();
        params.height = display.getWidth() * 4 / 9;
        root.setLayoutParams(params);
        root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), NewsDetailActivity.class).putExtra("NewsItemsBean",
                        newsItemsBean));
            }
        });
        iv_news = (ImageView) headView.findViewById(R.id.iv_news);
        tv_title = (TextView) headView.findViewById(R.id.tv_title);
        tv_time = (TextView) headView.findViewById(R.id.tv_time);
//        listview.addHeaderView(headView);
        listview.setAdapter(newsAdapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(getActivity(), NewsDetailActivity.class).putExtra("NewsItemsBean",
                        newsAdapter.data.get(position - 1)));
            }
        });
        listview.setPullLoadEnable(true);
        listview.setPullRefreshEnable(true);
        listview.setLastUpdateTime(String.valueOf(System.currentTimeMillis()));
//        listview.addHeaderView(headView);
        listview.setAdapter(newsAdapter);
        listview.setXListViewListener(new XListView.IXListViewListener() {

            @Override
            public void onRefresh() {
                handler.sendEmptyMessage(1);
            }

            @Override
            public void onLoadMore() {
                handler.sendEmptyMessage(2);
            }
        });
    }

    private void newsList() {
        showLoad("");
        RequestParams params = new RequestParams();
        params.put("pageNo", pageNo + "");
        params.put("pageSize", pageSize + "");
        Log.d("URL", Constants.BASE_URL + "newsList&" + params.toString());
        CoreHttpClient.post("newsList", params, new HttpCallBackListener() {
            @Override
            public void onSuccess(JSONObject json) {

                dismiss();
                if (json.optInt("state") == 0 && json.optInt("isSuccessful") == 0) {
                    Gson gson = new Gson();
                    NewsItemsList newsItemsList = gson.fromJson(json.toString(), NewsItemsList.class);
                    List<NewsItemsBean> data = newsItemsList.getNewsItems();
                    if (data.size() > 0) {
//                        newsItemsBean = data.remove(0);
                        Log.d("lamp", "data.size=" + data.size());
                        if (refreshOrLoad) {
//                            Glide.with(getActivity()).load(newsItemsBean.getPath()).centerCrop().placeholder(R.mipmap
//                                    .default_img).crossFade().into(iv_news);
//                            tv_title.setText(newsItemsBean.getTitle());
//                            tv_time.setText(newsItemsBean.getTime());
                            newsAdapter.data.clear();
                            listview.stopRefresh();
                        } else {
                            pageNo++;
                            listview.stopLoadMore();
                        }
                        newsAdapter.data.addAll(data);
                        newsAdapter.notifyDataSetChanged();
                    } else {
                        showToast("没有更多数据了");
                        listview.notifyDidNoMore();
                    }
                    time = System.currentTimeMillis();
                }
            }

            @Override
            public void onRequestFailed() {
                listview.stopRefresh();
                dismiss();
                showToast("请求失败");
            }

            @Override
            public void onException() {
                listview.stopRefresh();
                dismiss();
                showToast("请求失败");
            }
        });
    }

}
