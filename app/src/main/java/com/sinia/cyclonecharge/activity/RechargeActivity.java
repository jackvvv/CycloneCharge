package com.sinia.cyclonecharge.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.loopj.android.http.RequestParams;
import com.sinia.cyclonecharge.R;
import com.sinia.cyclonecharge.adapter.MoneyAdapter;
import com.sinia.cyclonecharge.base.BaseActivity;
import com.sinia.cyclonecharge.http.CoreHttpClient;
import com.sinia.cyclonecharge.http.HttpCallBackListener;
import com.sinia.cyclonecharge.payUtils.PayResult;
import com.sinia.cyclonecharge.payUtils.SignUtils;
import com.sinia.cyclonecharge.utils.Constants;
import com.sinia.cyclonecharge.utils.MyApplication;
import com.sinia.cyclonecharge.utils.StringUtil;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.sinia.cyclonecharge.R.id.view;
import static com.sinia.cyclonecharge.utils.Constants.PARTNER;
import static com.sinia.cyclonecharge.utils.Constants.RSA_PRIVATE;
import static com.sinia.cyclonecharge.utils.Constants.SELLER;

/**
 * Created by newLamp on 2016/11/4.
 */

public class RechargeActivity extends BaseActivity {


    @Bind(R.id.gridview)
    GridView gridview;
    @Bind(R.id.iv_alipay)
    ImageView ivAlipay;
    @Bind(R.id.rl_alipay)
    RelativeLayout rlAlipay;
    @Bind(R.id.iv_wechat)
    ImageView ivWechat;
    @Bind(R.id.rl_wechat)
    RelativeLayout rlWechat;
    @Bind(R.id.tv_recharge)
    TextView tvRecharge;
    @Bind(R.id.et_money)
    EditText etMoney;

    private MoneyAdapter moneyAdapter;
    private boolean clickFlag = true;
    private String RechargeNo;
    private String[] moneyArray = new String[]{"30", "50", "100", "150", "200", "300", "400", "500"};
    private static final int SDK_PAY_FLAG = 1;
    private static final int SDK_CHECK_FLAG = 2;
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    int pos = msg.arg1;
                    etMoney.setText(moneyArray[pos]);
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge, "充值金额");
        getDoingView().setVisibility(View.GONE);
        ButterKnife.bind(this);
        initView();
        RechargeNo = StringUtil.getOutTradeNo();
    }

    private void initView() {
        moneyAdapter = new MoneyAdapter(this,handler);
        gridview.setAdapter(moneyAdapter);
        etMoney.setText("100");
        etMoney.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String money = s.toString();
                int pos = -1;
                for (int i = 0; i < moneyArray.length; i++) {
                    if (money.equals(moneyArray[i])) {
                        pos = i;
                    }
                }
                moneyAdapter.selectIndex = pos;
                moneyAdapter.notifyDataSetChanged();
            }
        });
    }

    @OnClick({R.id.rl_alipay, R.id.rl_wechat, R.id.tv_recharge})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_alipay:
                clickFlag = true;
                ivAlipay.setImageResource(R.mipmap.icon_checked);
                ivWechat.setImageResource(R.mipmap.icon_uncheck);
                break;
            case R.id.rl_wechat:
                clickFlag = false;
                ivAlipay.setImageResource(R.mipmap.icon_uncheck);
                ivWechat.setImageResource(R.mipmap.icon_checked);
                break;
            case R.id.tv_recharge:
                if (etMoney.getEditableText().toString().equals("")) {
                    showToast("请选择充值金额");
                    return;
                }
                if (clickFlag) {
                    alipay();
//                    rechange();
                }
                break;
        }
    }

    private void rechange() {
        showLoad("");
        RequestParams params = new RequestParams();
        params.put("userId", MyApplication.getInstance().getLoginBean().getUserId());
        params.put("money", etMoney.getEditableText().toString());
        params.put("changeType", "1");
        LogRequestUrl("rechange&" + params.toString());
        CoreHttpClient.post("rechange", params, new HttpCallBackListener() {
            @Override
            public void onSuccess(JSONObject json) {
                dismiss();
                if (json.optInt("state") == 0 && json.optInt("isSuccessful") == 0) {
                    showToast("充值成功");
                } else {
                    showToast("充值失败");
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

    // --------------------------------------支付宝-------------------------------------------
    // 支付宝
    public void alipay() {
        if (TextUtils.isEmpty(Constants.PARTNER) || TextUtils.isEmpty(RSA_PRIVATE)
                || TextUtils.isEmpty(SELLER)) {
            new AlertDialog.Builder(this)
                    .setTitle("警告")
                    .setMessage("需要配置PARTNER | RSA_PRIVATE| SELLER")
                    .setPositiveButton("确定",
                            new DialogInterface.OnClickListener() {
                                public void onClick(
                                        DialogInterface dialoginterface, int i) {
                                    //
                                    finish();
                                }
                            }).show();
            return;
        }
        // 订单
        String orderInfo = getOrderInfo("旋风充", "旋风充平台支付", "0.01");

        // 对订单做RSA 签名
        String sign = sign(orderInfo);
        try {
            // 仅需对sign 做URL编码
            sign = URLEncoder.encode(sign, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        // 完整的符合支付宝参数规范的订单信息
        final String payInfo = orderInfo + "&sign=\"" + sign + "\"&"
                + getSignType();
        Log.e("tag", "payInfo...." + payInfo);

        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask alipay = new PayTask(RechargeActivity.this);
                // 调用支付接口，获取支付结果
                String result = alipay.pay(payInfo);

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    /**
     * create the order info. 创建订单信息
     */
    public String getOrderInfo(String subject, String body, String price) {

        // 签约合作者身份ID
        String orderInfo = "partner=" + "\"" + PARTNER + "\"";

        // 签约卖家支付宝账号
        orderInfo += "&seller_id=" + "\"" + SELLER + "\"";

        // 商户网站唯一订单号
        orderInfo += "&out_trade_no=" + "\"" + RechargeNo + "\"";

        // 商品名称
        orderInfo += "&subject=" + "\"" + subject + "\"";

        // 商品详情
        orderInfo += "&body=" + "\"" + body + "\"";

        // 商品金额
        orderInfo += "&total_fee=" + "\"" + price + "\"";

        // 服务器异步通知页面路径
        orderInfo += "&notify_url=" + "\"" + "http://notify.msp.hk/notify.htm"
                + "\"";

        // 服务接口名称， 固定值
        orderInfo += "&service=\"mobile.securitypay.pay\"";

        // 支付类型， 固定值
        orderInfo += "&payment_type=\"1\"";

        // 参数编码， 固定值
        orderInfo += "&_input_charset=\"utf-8\"";

        // 设置未付款交易的超时时间
        // 默认30分钟，一旦超时，该笔交易就会自动被关闭。
        // 取值范围：1m～15d。
        // m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
        // 该参数数值不接受小数点，如1.5h，可转换为90m。
        orderInfo += "&it_b_pay=\"30m\"";

        // extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
        // orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

        // 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
        orderInfo += "&return_url=\"m.alipay.com\"";

        // 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
        // orderInfo += "&paymethod=\"expressGateway\"";

        return orderInfo;
    }

    // 交易时间
    public String getDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");
        String date = sdf.format(new java.util.Date());
        Log.e("tag", "交易日期：" + date);
        return date;
    }

    /**
     * sign the order info. 对订单信息进行签名
     *
     * @param content 待签名订单信息
     */
    public String sign(String content) {
        return SignUtils.sign(content, RSA_PRIVATE);
    }

    /**
     * get the sign type we use. 获取签名方式
     */
    public String getSignType() {
        return "sign_type=\"RSA\"";
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    PayResult payResult = new PayResult((String) msg.obj);

                    // 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
                    String resultInfo = payResult.getResult();

                    String resultStatus = payResult.getResultStatus();

                    Log.e("tag", "resultInfo....." + payResult.toString());
                    Log.e("tag", "resultStatus....." + resultStatus);

                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        Toast.makeText(RechargeActivity.this, "支付成功",
                                Toast.LENGTH_SHORT).show();
                        rechange();
                    } else {
                        // 判断resultStatus 为非“9000”则代表可能支付失败
                        // “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            Toast.makeText(RechargeActivity.this, "支付结果确认中",
                                    Toast.LENGTH_SHORT).show();

                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            Toast.makeText(RechargeActivity.this, "支付失败",
                                    Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                    break;
                }
                case SDK_CHECK_FLAG: {
                    Toast.makeText(RechargeActivity.this, "检查结果为：" + msg.obj,
                            Toast.LENGTH_SHORT).show();
                    break;
                }
                default:
                    break;
            }
        }

        ;
    };
}
