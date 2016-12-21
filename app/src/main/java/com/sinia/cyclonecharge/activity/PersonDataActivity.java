package com.sinia.cyclonecharge.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;
import com.sinia.cyclonecharge.R;
import com.sinia.cyclonecharge.base.BaseActivity;
import com.sinia.cyclonecharge.bean.PersonDetailBean;
import com.sinia.cyclonecharge.http.CoreHttpClient;
import com.sinia.cyclonecharge.http.HttpCallBackListener;
import com.sinia.cyclonecharge.utils.CacheUtils;
import com.sinia.cyclonecharge.utils.Constants;
import com.sinia.cyclonecharge.utils.DialogUtils;
import com.sinia.cyclonecharge.utils.MyApplication;
import com.sinia.cyclonecharge.utils.StringUtil;
import com.sinia.cyclonecharge.view.RoundImageView;
import com.sinia.cyclonecharge.widget.ActionSheetDialog;

import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UploadFileListener;

import static com.sinia.cyclonecharge.R.id.iv_head;
import static com.sinia.cyclonecharge.R.id.launch_product_query;
import static com.sinia.cyclonecharge.R.id.view;
import static com.sinia.cyclonecharge.utils.MyApplication.context;

/**
 * Created by newLamp on 2016/11/4.
 */

public class PersonDataActivity extends BaseActivity {
    @Bind(iv_head)
    RoundImageView ivHead;
    @Bind(R.id.rl_head)
    RelativeLayout rlHead;
    @Bind(R.id.et_name)
    EditText etName;
    @Bind(R.id.tv_sex)
    TextView tvSex;
    @Bind(R.id.rl_sex)
    RelativeLayout rlSex;
    @Bind(R.id.tv_address)
    TextView tvAddress;
    @Bind(R.id.rl_address)
    RelativeLayout rlAddress;
    @Bind(R.id.tv_email)
    TextView tvEmail;
    @Bind(R.id.rl_email)
    RelativeLayout rlEmail;
    @Bind(R.id.tv_phone)
    TextView tvPhone;
    private String imgPath, dateTime;
    private String imgUrl = "";
    private Bitmap bitmap;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    int select = msg.arg1;  //0男，1女
                    if (select == 0) {
                        tvSex.setText("男");
                    } else {
                        tvSex.setText("女");
                    }
                    tvSex.setTextColor(Color.parseColor("#242424"));
                    upDeById();
                    break;
                case 2:
                    tvAddress.setText(msg.obj + "");
                    tvAddress.setTextColor(Color.parseColor("#242424"));
                    upDeById();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_data, "个人资料");
        ButterKnife.bind(this);
        getDoingView().setVisibility(View.GONE);
        personDetailById();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }

    private void personDetailById() {
        showLoad("");
        RequestParams params = new RequestParams();
        params.put("userId", MyApplication.getInstance().getLoginBean().getUserId());
        LogRequestUrl("personDetailById&" + params.toString());
        CoreHttpClient.post("personDetailById", params, new HttpCallBackListener() {
            @Override
            public void onSuccess(JSONObject json) {
                dismiss();
                if (json.optInt("state") == 0 && json.optInt("isSuccessful") == 0) {
                    Gson gson = new Gson();
                    PersonDetailBean personDetailBean = gson.fromJson(json.toString(), PersonDetailBean.class);
                    Glide.with(PersonDataActivity.this).load(personDetailBean.getImageurl()).placeholder(R.mipmap
                            .default_head).crossFade().into(ivHead);
                    etName.setEnabled(false);
                    etName.setText(personDetailBean.getNickname());
                    if (!StringUtil.isEmpty(personDetailBean.getSex())) {
                        if (!personDetailBean.getSex().equals("未设置")) {
                            tvSex.setTextColor(Color.parseColor("#242424"));
                            if (personDetailBean.getSex().equals("1")) {
                                //男
                                tvSex.setText("男");
                            } else {
                                //女
                                tvSex.setText("女");
                            }
                        }
                    } else {
                        tvSex.setTextColor(Color.parseColor("#EDA968"));
                        tvSex.setText("未设置");
                    }

                    if (!StringUtil.isEmpty(personDetailBean.getAddress())) {
                        if (!personDetailBean.getAddress().equals("未设置")) {
                            tvAddress.setTextColor(Color.parseColor("#242424"));
                            tvAddress.setText(personDetailBean.getAddress());
                        }
                    } else {
                        tvAddress.setTextColor(Color.parseColor("#EDA968"));
                        tvAddress.setText("未设置");
                    }

                    if (!StringUtil.isEmpty(personDetailBean.getEmail())) {
                        if (!personDetailBean.getEmail().equals("未设置")) {
                            tvEmail.setTextColor(Color.parseColor("#242424"));
                            tvEmail.setText(personDetailBean.getEmail());
                        }
                    } else {
                        tvEmail.setTextColor(Color.parseColor("#EDA968"));
                        tvEmail.setText("未设置");
                    }
                    tvPhone.setText(personDetailBean.getPhone());
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

    private void upDeById() {
        showLoad("");
        RequestParams params = new RequestParams();
        params.put("userId", MyApplication.getInstance().getLoginBean().getUserId());
        String sex = tvSex.getText().toString();
        if (sex.equals("男")) {
            sex = "1";
        } else if (sex.equals("女")) {
            sex = "0";
        } else {
            sex = "-1";
        }
        params.put("sex", sex);
        String address = tvAddress.getText().toString();
        if (address.equals("去设置")) {
            address = "-1";
        }
        params.put("address", address);
        String email = tvEmail.getText().toString();
        if (email.equals("去设置")) {
            email = "-1";
        }
        params.put("email", email);
        params.put("imageUrl", imgUrl.equals("") ? "-1" : imgUrl);
        LogRequestUrl("upDeById&" + params.toString());
        CoreHttpClient.post("upDeById", params, new HttpCallBackListener() {
            @Override
            public void onSuccess(JSONObject json) {
                dismiss();
                if (json.optInt("state") == 0 && json.optInt("isSuccessful") == 0) {
                    showToast("修改成功");
                }else{
                    showToast("修改失败");
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

    @OnClick({R.id.rl_head, R.id.rl_sex, R.id.rl_address, R.id.rl_email})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_head:
                selectImage();
                break;
            case R.id.rl_sex:
                DialogUtils.createChooseSexDialog(this, handler);
                break;
            case R.id.rl_address:
                DialogUtils.createChooseAddressDialog(this, handler);
                break;
            case R.id.rl_email:
                Intent it = new Intent(this, InputEmailActivity.class);
                it.putExtra("email", tvEmail.getText().toString());
                startActivityForResult(it, 4);
                break;
        }
    }


    private void selectImage() {
        new ActionSheetDialog(this)
                .builder()
                .setCancelable(true)
                .setCanceledOnTouchOutside(true)
                .addSheetItem("拍照选择", ActionSheetDialog.SheetItemColor.Blue,
                        new ActionSheetDialog.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int which) {
                                Date date1 = new Date(System
                                        .currentTimeMillis());
                                dateTime = date1.getTime() + "";
                                getAvataFromCamera();
                            }
                        })
                .addSheetItem("从手机相册选择", ActionSheetDialog.SheetItemColor.Blue,
                        new ActionSheetDialog.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int which) {
                                Intent intent = new Intent();
                                intent.setAction(Intent.ACTION_PICK);
                                intent.setType("image/*");
                                intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(intent, 2);
                            }
                        }).show();
    }

    protected void getAvataFromCamera() {
        File f = new File(CacheUtils.getCacheDirectory(this, true,
                "icon") + dateTime + "avatar.jpg");
        if (f.exists()) {
            f.delete();
        }
        try {
            f.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Uri uri = Uri.fromFile(f);
        Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        camera.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(camera, 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == -1) {
            switch (requestCode) {
                case 1:
                    String files = CacheUtils.getCacheDirectory(this,
                            true, "icon") + dateTime + "avatar.jpg";
                    File file = new File(files);
                    if (file.exists() && file.length() > 0) {
                        Uri uri = Uri.fromFile(file);
                        startPhotoZoom(uri);
                    }
                    break;
                case 2:
                    if (data == null) {
                        return;
                    }
                    startPhotoZoom(data.getData());
                    break;
                case 3:
                    if (data != null) {
                        Bundle extras = data.getExtras();
                        if (extras != null) {
                            bitmap = extras.getParcelable("data");
                            imgPath = saveToSdCard(bitmap);
                            Log.i("lamp", "iconUrl---" + imgPath);

                            updateIcon(imgPath);
                        }
                    }
                    break;
                case 4:
                    String email = data.getStringExtra("email");
                    tvEmail.setText(email);
                    tvEmail.setTextColor(Color.parseColor("#242424"));
                    upDeById();
                    break;
            }
        }
    }

    private void updateIcon(String avataPath) {
        if (avataPath != null) {
            showLoad("正在上传图片");
            final BmobFile file = new BmobFile(new File(avataPath));
            file.uploadblock(new UploadFileListener() {

                @Override
                public void done(BmobException arg0) {
                    dismiss();
                    if (arg0 == null) {
                        Log.i("temp", "图片上传成功" + file.getFileUrl());
                        // if (imageUrl != null) {
                        ivHead.setImageBitmap(bitmap);
                        imgUrl = file.getFileUrl();
                        showToast("图片上传成功");
                        upDeById();
                        dismiss();
                    } else {
                        showToast("图片上传失败");
                    }
                }
            });
        }
    }

    public String saveToSdCard(Bitmap bitmap) {
        String files = CacheUtils
                .getCacheDirectory(this, true, "icon")
                + dateTime
                + "_11.jpg";
        File file = new File(files);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            if (bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)) {
                fos.flush();
                fos.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file.getAbsolutePath();
    }

    public void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 280);
        intent.putExtra("outputY", 280);
        intent.putExtra("crop", "true");
        intent.putExtra("scale", true);// 黑边
        intent.putExtra("scaleUpIfNeeded", true);// 黑边
        intent.putExtra("return-data", true);// 选择返回数据
        startActivityForResult(intent, 3);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
}
