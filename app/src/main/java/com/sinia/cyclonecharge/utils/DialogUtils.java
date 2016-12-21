package com.sinia.cyclonecharge.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.avast.android.butterknifezelezny.common.Utils;
import com.sinia.cyclonecharge.R;
import com.sinia.cyclonecharge.activity.ChargingPileActivity;
import com.sinia.cyclonecharge.activity.MainActivity;
import com.sinia.cyclonecharge.activity.RechargeActivity;
import com.sinia.cyclonecharge.city.CityPicker;
import com.sinia.cyclonecharge.wheelview.OnWheelScrollListener;
import com.sinia.cyclonecharge.wheelview.WheelView;
import com.sinia.cyclonecharge.wheelview.adapter.ArrayWheelAdapter;
import com.sinia.cyclonecharge.wheelview.adapter.NumericWheelAdapter;

import java.util.Calendar;

import static android.R.attr.format;
import static android.R.attr.type;
import static android.R.attr.value;
import static com.sinia.cyclonecharge.R.id.tv_female;
import static com.sinia.cyclonecharge.R.id.tv_male;
import static com.sinia.cyclonecharge.fragment.ChargeListFragment.WHAT_CHOOSE_BAIDU;
import static com.sinia.cyclonecharge.fragment.ChargeListFragment.WHAT_CHOOSE_GAODE;
import static com.sinia.cyclonecharge.fragment.ChargeListFragment.WHAT_SELECT_TIME;


/**
 * Created by 忧郁的眼神 on 2016/7/26.
 */
public class DialogUtils {

    public static Dialog dialog;

    private static Dialog initDialog(Context context, int resId, int theme) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(resId, null);
        Dialog dialog = new Dialog(context, theme);
        dialog.setContentView(v, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        return dialog;
    }

    public static Dialog createChooseNaviDialog(final Context context, final Handler handler, int type) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.dialog_choose_navigation, null);
        dialog = new Dialog(context, R.style.ActionSheetDialogStyle);
        dialog.show();
        Window window = dialog.getWindow();
        window.setGravity(Gravity.BOTTOM); // 此处可以设置dialog显示的位置
        WindowManager windowManager = ((Activity) context).getWindowManager();
        final Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = display.getWidth();
        dialog.getWindow().setAttributes(lp);
        dialog.setContentView(v, lp);
        v.findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        v.findViewById(R.id.tv_choose1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handler.sendEmptyMessage(WHAT_CHOOSE_BAIDU);
                dialog.dismiss();
            }
        });
        v.findViewById(R.id.tv_choose2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handler.sendEmptyMessage(WHAT_CHOOSE_GAODE);
                dialog.dismiss();
            }
        });
        switch (type) {
            case 0:
                //什么都没安装
                v.findViewById(R.id.tv_no).setVisibility(View.VISIBLE);
                v.findViewById(R.id.tv_choose1).setVisibility(View.GONE);
                v.findViewById(R.id.tv_choose2).setVisibility(View.GONE);
                break;
            case 1:
                //安装了百度
                v.findViewById(R.id.tv_no).setVisibility(View.GONE);
                v.findViewById(R.id.tv_choose2).setVisibility(View.GONE);
                break;
            case 2:
                //安装了高德
                v.findViewById(R.id.tv_no).setVisibility(View.GONE);
                v.findViewById(R.id.tv_choose1).setVisibility(View.GONE);
                break;
            case 3:
                break;
        }
        return dialog;
    }

    public static Dialog createDatePickerDialog(final Context context, final Handler handler, final int position) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.dialog_date_picker, null);
        dialog = new Dialog(context, R.style.ActionSheetDialogStyle);
        dialog.show();
        Window window = dialog.getWindow();
        window.setGravity(Gravity.BOTTOM); // 此处可以设置dialog显示的位置
        WindowManager windowManager = ((Activity) context).getWindowManager();
        final Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = display.getWidth();
        dialog.getWindow().setAttributes(lp);
        dialog.setContentView(v, lp);
        final WheelView time = (WheelView) v.findViewById(R.id.time);
        final WheelView min = (WheelView) v.findViewById(R.id.min);
        final WheelView sec = (WheelView) v.findViewById(R.id.sec);
        final String[] times = {"上午", "下午"};
        OnWheelScrollListener scrollListener = new OnWheelScrollListener() {
            @Override
            public void onScrollingStarted(WheelView wheel) {

            }

            @Override
            public void onScrollingFinished(WheelView wheel) {

            }
        };

        ArrayWheelAdapter<String> arrayWheelAdapter = new ArrayWheelAdapter<String>(context, times);
        time.setViewAdapter(arrayWheelAdapter);
        time.setCyclic(false);
        time.addScrollingListener(scrollListener);
        NumericWheelAdapter numericWheelAdapter3 = new NumericWheelAdapter(context, 1, 23, "%02d");
        numericWheelAdapter3.setLabel("");
        min.setViewAdapter(numericWheelAdapter3);
        min.setCyclic(true);
        min.addScrollingListener(scrollListener);
        NumericWheelAdapter numericWheelAdapter4 = new NumericWheelAdapter(context, 0, 59, "%02d");
        numericWheelAdapter4.setLabel("");
        sec.setViewAdapter(numericWheelAdapter4);
        sec.setCyclic(true);
        sec.addScrollingListener(scrollListener);

        time.setVisibleItems(5);
        min.setVisibleItems(5);
        sec.setVisibleItems(5);

        v.findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        v.findViewById(R.id.tv_ensure).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message msg = handler.obtainMessage();
                msg.what = WHAT_SELECT_TIME;
                msg.obj = String.format("%02d", (min.getCurrentItem
                        () + 1)) + ":" + String.format("%02d", (sec
                        .getCurrentItem())) + ":" + "00";
                msg.arg1 = position;
                handler.sendMessage(msg);
                dialog.dismiss();
            }
        });
        return dialog;
    }

    public static Dialog createDatePickerDialog2(final Context context, final Handler handler) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.dialog_date_picker, null);
        dialog = new Dialog(context, R.style.ActionSheetDialogStyle);
        dialog.show();
        Window window = dialog.getWindow();
        window.setGravity(Gravity.BOTTOM); // 此处可以设置dialog显示的位置
        WindowManager windowManager = ((Activity) context).getWindowManager();
        final Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = display.getWidth();
        dialog.getWindow().setAttributes(lp);
        dialog.setContentView(v, lp);
        final WheelView time = (WheelView) v.findViewById(R.id.time);
        final WheelView min = (WheelView) v.findViewById(R.id.min);
        final WheelView sec = (WheelView) v.findViewById(R.id.sec);
        final String[] times = {"上午", "下午"};
        OnWheelScrollListener scrollListener = new OnWheelScrollListener() {
            @Override
            public void onScrollingStarted(WheelView wheel) {

            }

            @Override
            public void onScrollingFinished(WheelView wheel) {

            }
        };

        ArrayWheelAdapter<String> arrayWheelAdapter = new ArrayWheelAdapter<String>(context, times);
        time.setViewAdapter(arrayWheelAdapter);
        time.setCyclic(false);
        time.addScrollingListener(scrollListener);
        NumericWheelAdapter numericWheelAdapter3 = new NumericWheelAdapter(context, 1, 23, "%02d");
        numericWheelAdapter3.setLabel("");
        min.setViewAdapter(numericWheelAdapter3);
        min.setCyclic(true);
        min.addScrollingListener(scrollListener);
        NumericWheelAdapter numericWheelAdapter4 = new NumericWheelAdapter(context, 0, 59, "%02d");
        numericWheelAdapter4.setLabel("");
        sec.setViewAdapter(numericWheelAdapter4);
        sec.setCyclic(true);
        sec.addScrollingListener(scrollListener);

        time.setVisibleItems(5);
        min.setVisibleItems(5);
        sec.setVisibleItems(5);

        v.findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        v.findViewById(R.id.tv_ensure).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message msg = handler.obtainMessage();
                msg.what = WHAT_SELECT_TIME;
                msg.obj = String.format("%02d", (min.getCurrentItem
                        () + 1)) + ":" + String.format("%02d", (sec
                        .getCurrentItem())) + ":" + "00";
                handler.sendMessage(msg);
                dialog.dismiss();
            }
        });
        return dialog;
    }

    public static Dialog createRemainLessDialog(final Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.dialog_remain_less, null);
        dialog = new Dialog(context, R.style.dialog);
        dialog.show();
        dialog.setContentView(v, new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER); // 此处可以设置dialog显示的位置
        WindowManager windowManager = ((Activity) context).getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = (display.getWidth() - AppInfoUtil.dip2px(context, 20)); // 设置宽度
        dialog.getWindow().setAttributes(lp);
        TextView tv_remain = (TextView) v.findViewById(R.id.tv_remain);
        v.findViewById(R.id.tv_dismiss).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        v.findViewById(R.id.tv_recharge).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                context.startActivity(new Intent(context, RechargeActivity.class));
            }
        });
        return dialog;
    }

    public static Dialog createShowHaveOrderDialog(final Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.dialog_show_tips, null);
        dialog = new Dialog(context, R.style.dialog);
        dialog.show();
        dialog.setContentView(v, new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER); // 此处可以设置dialog显示的位置
        WindowManager windowManager = ((Activity) context).getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = (display.getWidth() - AppInfoUtil.dip2px(context, 20)); // 设置宽度
        dialog.getWindow().setAttributes(lp);
        v.findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        return dialog;
    }

    public static Dialog createTipsDialog(final Context context, String msg, int height) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.dialog_tips, null);
        dialog = initDialog(context, R.layout.dialog_tips, R.style.ActionSheetDialogStyle2);
        dialog.show();
        Window window = dialog.getWindow();
        window.setGravity(Gravity.LEFT | Gravity.TOP);
        WindowManager windowManager = ((Activity) context).getWindowManager();
        final Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
//        lp.x = 0;
        lp.y = height + AppInfoUtil.dip2px(context, 48);
        lp.width = display.getWidth();
//        lp.height = AppInfoUtil.dip2px(context, 48);
        dialog.getWindow().setAttributes(lp);
        dialog.setContentView(v, lp);
        TextView tv_msg = (TextView) v.findViewById(R.id.tv_msg);
        tv_msg.setText(msg);
        v.findViewById(R.id.rl_root).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                context.startActivity(new Intent(context, ChargingPileActivity.class));
            }
        });
        return dialog;
    }

    public static int select = 0;

    public static Dialog createChooseSexDialog(final Context context, final Handler handler) {
        select = 0;
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.dialog_choose_sex, null);
        dialog = new Dialog(context, R.style.ActionSheetDialogStyle);
        dialog.show();
        Window window = dialog.getWindow();
        window.setGravity(Gravity.BOTTOM); // 此处可以设置dialog显示的位置
        WindowManager windowManager = ((Activity) context).getWindowManager();
        final Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = display.getWidth();
        dialog.getWindow().setAttributes(lp);
        dialog.setContentView(v, lp);
        v.findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        final TextView tv_male = (TextView) v.findViewById(R.id.tv_male);
        final TextView tv_female = (TextView) v.findViewById(R.id.tv_female);

        tv_male.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_male.setTextColor(Color.parseColor("#32C46B"));
                tv_male.setBackgroundResource(R.drawable.shape_select);
                tv_female.setTextColor(Color.parseColor("#848484"));
                tv_female.setBackgroundResource(R.drawable.shape_unselect);
                select = 0;
            }
        });
        tv_female.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_female.setTextColor(Color.parseColor("#32C46B"));
                tv_female.setBackgroundResource(R.drawable.shape_select);
                tv_male.setTextColor(Color.parseColor("#848484"));
                tv_male.setBackgroundResource(R.drawable.shape_unselect);
                select = 1;
            }
        });
        v.findViewById(R.id.tv_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Message msg = handler.obtainMessage();
                msg.what = 1;
                msg.arg1 = select;
                handler.sendMessage(msg);
                dialog.dismiss();
            }
        });
        return dialog;
    }

    public static Dialog createChooseAddressDialog(final Context context, final Handler handler) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.dialog_select_address, null);
        dialog = new Dialog(context, R.style.ActionSheetDialogStyle);
        dialog.show();
        Window window = dialog.getWindow();
        window.setGravity(Gravity.BOTTOM); // 此处可以设置dialog显示的位置
        WindowManager windowManager = ((Activity) context).getWindowManager();
        final Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = display.getWidth();
        lp.height = display.getHeight() / 2;
        dialog.getWindow().setAttributes(lp);
        dialog.setContentView(v, lp);
        v.findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        final CityPicker cityPicker = (CityPicker) v
                .findViewById(R.id.citypicker);
        v.findViewById(R.id.tv_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Message msg = new Message();
                msg.what = 2;
                msg.obj = cityPicker.getProvince() + " " + cityPicker.getCity() + " " + cityPicker.getCouny();
                handler.sendMessage(msg);
                dialog.dismiss();
            }
        });
        return dialog;
    }

    public interface OnOkListener {
        public void onClick();
    }

    public interface OnCancelListener {
        public void onClick();
    }
}
//

//

//}
