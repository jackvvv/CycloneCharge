package com.sinia.cyclonecharge.http;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.sinia.cyclonecharge.utils.Constants;

import org.json.JSONException;
import org.json.JSONObject;

public class CoreHttpClient {
    public static AsyncHttpClient client = new AsyncHttpClient();
    public static HttpResponseListener listen;

    public static void get(final Context context, final int requestCode,
                           String urlString) {
        try {
            client.get(context, Constants.BASE_URL + urlString,
                    new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int arg0, JSONObject json) {
                            Gson gson = new Gson();
                            Log.d("result", json.toString());
                            try {
                                if (json.has("state")
                                        && (json.getInt("state") == Constants.RESPONSE_TYPE.STATUS_SUCCESS)) {
                                    if (json.has("isSuccessful")
                                            && json.getInt("isSuccessful") == Constants.RESPONSE_TYPE.STATUS_SUCCESS) {
                                        // state=0 isSuccessful=0
                                        switch (requestCode) {
                                            default:
                                                break;
                                        }
                                    } else if (json.has("isSuccessful")
                                            && json.getInt("isSuccessful") == Constants.RESPONSE_TYPE.STATUS_EXCEPTION) {
                                        // state=0 isSuccessful=1
                                        switch (requestCode) {
                                            default:
                                                break;
                                        }
                                    } else if (json.has("isSuccessful")
                                            && json.getInt("isSuccessful") == Constants.RESPONSE_TYPE.STATUS_FAILED) {
                                        // state=0 successful=2
                                    } else if (json.has("isSuccessful")
                                            && json.getInt("isSuccessful") == Constants.RESPONSE_TYPE.STATUS_3) {
                                        // state=0 successful=3
                                    } else if (json.has("isSuccessful")
                                            && json.getInt("isSuccessful") == 4) {
                                        // state=0 successful=4
                                    } else if (json.has("isSuccessful")
                                            && json.getInt("isSuccessful") == 5) {
                                        // state=0 successful=5
                                    }

                                }
                                // state=1
                                else {
                                    if (json.has("isSuccessful")
                                            && json.getInt("isSuccessful") == Constants.RESPONSE_TYPE.STATUS_SUCCESS) {
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }

                        @Override
                        public void onFailure(Throwable arg0) {
                            listen.httpRequestFailed();
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void get(final Context context, String urlString,
                           final HttpCallBackListener httpCallBackListener) {
        client.get(context, Constants.BASE_URL + urlString,
                new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int arg0, JSONObject json) {
                        // TODO Auto-generated method stub
                        super.onSuccess(arg0, json);
                        httpCallBackListener.onSuccess(json);
                    }

                    @Override
                    public void onFailure(Throwable arg0) {
                        httpCallBackListener.onRequestFailed();
                    }
                });
    }

    public static void post(String method, RequestParams params, final HttpCallBackListener httpCallBackListener){
        client.post(Constants.BASE_URL+method,params,new AsyncHttpResponseHandler(){
            @Override
            public void onSuccess(int i, String s) {
                super.onSuccess(i, s);
                Log.d("result", s);
                JSONObject json = null;
                try {
                    json = new JSONObject(s);
                    httpCallBackListener.onSuccess(json);
                } catch (JSONException e) {
                    httpCallBackListener.onException();
//                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable throwable, String s) {
                super.onFailure(throwable, s);
                httpCallBackListener.onRequestFailed();
            }
        });
    }

    public static String getString(JSONObject json, String key) {
        String result = null;
        try {
            if (json.has(key)) {
                result = json.getString(key);
            } else {
                return "";
            }
        } catch (Exception e) {
            result = "";
        }
        return result;
    }

    public int getInt(JSONObject json, String key) {
        int result = 0;
        try {
            if (json.has(key)) {
                result = json.getInt(key);
            } else {
                return 0;
            }
        } catch (Exception e) {
            result = 0;
        }
        return result;
    }

    public double getDouble(JSONObject json, String key) {
        double result = 0;
        try {
            if (json.has(key)) {
                result = json.getDouble(key);
            } else {
                return 0;
            }
        } catch (Exception e) {
            result = 0;
        }
        return result;
    }
}
