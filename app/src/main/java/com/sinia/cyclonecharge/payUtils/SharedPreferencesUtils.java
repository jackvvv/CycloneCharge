package com.sinia.cyclonecharge.payUtils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

public class SharedPreferencesUtils {
	private static String DEFAULT_SPACE = "jsmcczone";

	private static SharedPreferences initSharedPreferences(Context context,
			String space) {
		SharedPreferences sp;
		if (TextUtils.isEmpty(space)) {
			sp = context.getSharedPreferences(DEFAULT_SPACE, 0);
		} else {
			sp = context.getSharedPreferences(space, 0);
		}
		return sp;
	}

	public static void putShareValue(Context context, String key, Object value) {
		putShareValue(context, null, key, value);
	}

	public static void putShareValue(Context context, String space, String key,
			Object value) {

		SharedPreferences.Editor editor = initSharedPreferences(context, space)
				.edit();

		if (value instanceof Integer) {
			editor.putInt(key, (Integer) value);
		} else if (value instanceof String) {
			editor.putString(key, (String) value);
		} else if (value instanceof Float) {
			editor.putFloat(key, (Float) value);
		} else if (value instanceof Long) {
			editor.putLong(key, (Long) value);
		} else if (value instanceof Boolean) {
			editor.putBoolean(key, (Boolean) value);
		}
		editor.commit();
	}

	public static String getShareString(Context context, String key) {
		return getShareString(context, null, key);

	}

	public static String getShareString(Context context, String space,
			String key) {
		SharedPreferences sp = initSharedPreferences(context, space);

		return sp.getString(key, "");
	}

	public static Boolean getShareBoolean(Context context, String key) {
		return getShareBoolean(context, null, key);

	}

	public static Boolean getShareBoolean(Context context, String space,
			String key) {
		SharedPreferences sp = initSharedPreferences(context, space);

		return sp.getBoolean(key, false);
	}

	public static Integer getShareInteger(Context context, String key) {
		return getShareInteger(context, null, key);

	}

	public static Integer getShareInteger(Context context, String space,
			String key) {
		SharedPreferences sp = initSharedPreferences(context, space);

		return sp.getInt(key, 0);
	}

	public static Float getShareFloat(Context context, String key) {
		return getShareFloat(context, null, key);

	}

	public static Float getShareFloat(Context context, String space, String key) {
		SharedPreferences sp = initSharedPreferences(context, space);

		return sp.getFloat(key, 0F);
	}

	public static Long getShareLong(Context context, String key) {
		return getShareLong(context, null, key);

	}

	public static Long getShareLong(Context context, String space, String key) {
		SharedPreferences sp = initSharedPreferences(context, space);

		return sp.getLong(key, 0L);
	}

	public static void removeShareValue(Context context, String key) {
		removeShareValue(context, null, key);
	}

	public static void removeShareValue(Context context, String space,
			String key) {
		SharedPreferences.Editor editor = initSharedPreferences(context, space)
				.edit();

		editor.remove(key);
		editor.commit();

	}

	public static void clearAllShareValue(Context context) {
		clearShareValue(context, null);
	}

	public static void clearShareValue(Context context, String space) {
		SharedPreferences.Editor editor = initSharedPreferences(context, space)
				.edit();

		editor.clear();
		editor.commit();

	}

}
