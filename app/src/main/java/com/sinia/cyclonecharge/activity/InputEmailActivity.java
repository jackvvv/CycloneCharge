package com.sinia.cyclonecharge.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.sinia.cyclonecharge.R;
import com.sinia.cyclonecharge.base.BaseActivity;
import com.sinia.cyclonecharge.utils.StringUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by newLamp on 2016/11/22.
 */

public class InputEmailActivity extends BaseActivity {
    @Bind(R.id.et_email)
    EditText etEmail;
    @Bind(R.id.tv_save)
    TextView tvSave;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_email, "邮箱");
        ButterKnife.bind(this);
        getDoingView().setVisibility(View.GONE);
        email = getIntent().getStringExtra("email");
        if(!email.equals("未设置")){
            etEmail.setText(email);
        }
    }

    @OnClick(R.id.tv_save)
    public void onClick() {
        String emailNew = etEmail.getEditableText().toString();
        if (!StringUtil.isEmail(emailNew)) {
            showToast("请输入正确的邮箱地址");
            return;
        }

        Intent it = getIntent();
        it.putExtra("email", emailNew);
        setResult(RESULT_OK, it);
        finish();
    }
}
