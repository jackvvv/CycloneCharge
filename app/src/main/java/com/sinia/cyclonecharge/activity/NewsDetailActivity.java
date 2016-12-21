package com.sinia.cyclonecharge.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.sinia.cyclonecharge.R;
import com.sinia.cyclonecharge.base.BaseActivity;
import com.sinia.cyclonecharge.bean.NewsItemsBean;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by newLamp on 2016/11/24.
 */

public class NewsDetailActivity extends BaseActivity {
    @Bind(R.id.webview)
    WebView webview;
    private NewsItemsBean newsItemsBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail, "");
        ButterKnife.bind(this);
        getDoingView().setVisibility(View.GONE);
        newsItemsBean = (NewsItemsBean) getIntent().getSerializableExtra("NewsItemsBean");
        initView();
        webview.loadUrl(newsItemsBean.getContent());
        webview.setWebViewClient(new WebViewClient(){

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
    }

    private void initView(){
        WebSettings webSettings = webview.getSettings();
        webSettings.setSupportZoom(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setBuiltInZoomControls(true);//support zoom
        webSettings.setUseWideViewPort(true);// 这个很关键
        webSettings.setLoadWithOverviewMode(true);
    }

}
