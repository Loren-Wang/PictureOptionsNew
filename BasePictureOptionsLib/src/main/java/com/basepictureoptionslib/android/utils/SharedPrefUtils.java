package com.basepictureoptionslib.android.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;


/**
 * Android prefence文件读写操作工具类
 * 
 * @author yynie
 * 
 */
public final class SharedPrefUtils {
	private static SharedPreferences mPref;

	public SharedPrefUtils setContext(Context context) {
		mPref = PreferenceManager
				.getDefaultSharedPreferences(context);
		return this;
	}

	public static boolean getBoolean(Context context,String key, boolean defValue) {
		if(mPref == null){
			mPref = PreferenceManager
					.getDefaultSharedPreferences(context);
		}
		return mPref.getBoolean(key, defValue);
	}

	public static boolean putBoolean(Context context,String key, boolean value) {
		if(mPref == null){
			mPref = PreferenceManager
					.getDefaultSharedPreferences(context);
		}
		SharedPreferences.Editor editor = mPref.edit();
		editor.putBoolean(key, value);
		return editor.commit();
	}
}
