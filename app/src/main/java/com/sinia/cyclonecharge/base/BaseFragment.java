package com.sinia.cyclonecharge.base;

import android.app.Dialog;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import com.sinia.cyclonecharge.R;
import com.sinia.cyclonecharge.view.loadingview.LoadingView;


/**
 * Created by 忧郁的眼神 on 2016/7/29.
 */
public class BaseFragment extends Fragment {
    private Dialog dialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void showToast(String text) {
        Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
    }

    public void showToast(int id) {
        showToast(id + "");
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    protected void startActivityForNoIntent(Class forwordClass) {
        Intent intent = new Intent(getActivity(), forwordClass);
        startActivity(intent);
    }

    private View mDialogContentView;
    private LoadingView mLoadingView;

    public void showLoad(String text) {
        if(text.equals("")){
            text = "请稍后...";
        }
        dialog = new Dialog(getActivity(), R.style.custom_dialog);
        mDialogContentView = LayoutInflater.from(getActivity()).inflate(
                R.layout.layout_loading_dialog, null);
        mLoadingView = (LoadingView) mDialogContentView
                .findViewById(R.id.loadView);
        mLoadingView.setLoadingText(text);
        Display d = getActivity().getWindowManager().getDefaultDisplay();
        dialog.show();
        dialog.setContentView(mDialogContentView, new ViewGroup.LayoutParams((int) (d.getWidth() * 0.5),
                (int) (d.getHeight() * 0.3)));
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        dialog.setCanceledOnTouchOutside(false);
    }

    public void dismiss() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    public boolean onBackPressed() {
        return false;
    }
}
