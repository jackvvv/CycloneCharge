package com.sinia.cyclonecharge.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.sinia.cyclonecharge.R;
import com.sinia.cyclonecharge.base.BaseActivity;


/**
 * Created by newLamp on 2016/9/19.
 */

public class WelcomeActivity extends BaseActivity {
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    startActivity(new Intent(WelcomeActivity.this,MainActivity.class));
                    finish();
                    break;
                default:
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        handler.sendEmptyMessageDelayed(1,2000);
    }
}
