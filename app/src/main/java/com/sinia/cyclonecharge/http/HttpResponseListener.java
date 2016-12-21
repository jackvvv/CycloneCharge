package com.sinia.cyclonecharge.http;

public interface HttpResponseListener {

	void requestSuccess();

	void requestFailed();

	void requestException();

	void registerSuccess();

	/** onFailure----请求失败 */
	void httpRequestFailed();
}
