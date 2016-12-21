package com.sinia.cyclonecharge.http;

import org.json.JSONObject;

public interface HttpCallBackListener {

	void onSuccess(JSONObject json);
	void onRequestFailed();
	void onException();
}
