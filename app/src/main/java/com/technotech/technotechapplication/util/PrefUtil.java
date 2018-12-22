package com.technotech.technotechapplication.util;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class PrefUtil {


	public static void setverifybroadcast(SharedPreferences prefs, String result) {
		Editor editor = prefs.edit();
		editor.putString("verify", result);
		editor.commit();

	}

	public static String getverifybroadcast(SharedPreferences prefs) {
		String result = null;
		try {
			result = prefs.getString("verify", "");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public static void setverifyotpdate(SharedPreferences prefs, String result) {
		Editor editor = prefs.edit();
		editor.putString("date", result);
		editor.commit();

	}

	public static String getverifyotpdate(SharedPreferences prefs) {
		String result = null;
		try {
			result = prefs.getString("date", null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	public static void setverifyotpdateforgot(SharedPreferences prefs, String result) {
		Editor editor = prefs.edit();
		editor.putString("dateforgot", result);
		editor.commit();

	}

	public static String getverifyotpdateforgot(SharedPreferences prefs) {
		String result = null;
		try {
			result = prefs.getString("dateforgot", null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	public static void setverifyotpforgot(SharedPreferences prefs, String result) {
		Editor editor = prefs.edit();
		editor.putString("otpforgot", result);
		editor.commit();

	}

	public static String getverifyotpforgot(SharedPreferences prefs) {
		String result = null;
		try {
			result = prefs.getString("otpforgot", null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	public static void setverifyotp(SharedPreferences prefs, String result) {
		Editor editor = prefs.edit();
		editor.putString("otp", result);
		editor.commit();

	}

	public static String getverifyotp(SharedPreferences prefs) {
		String result = null;
		try {
			result = prefs.getString("otp", null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	public static void setpasscode(SharedPreferences prefs, String result) {
		Editor editor = prefs.edit();
		editor.putString("passcode", result);
		editor.commit();

	}

	public static String getpasscode(SharedPreferences prefs) {
		String result = null;
		try {
			result = prefs.getString("passcode", null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	// Profile Picture Preference
	public static void savelogo(SharedPreferences prefs, String profilePic) {
		Editor editor = prefs.edit();
		editor.putString("logo_url", profilePic);
		editor.commit();
	}

	public static String getlogo(SharedPreferences prefs) {
		String profilePic;
		profilePic = prefs.getString("logo_url", "");
		return profilePic;
	}

	public static void saveUser_id(SharedPreferences prefs, String profilePic) {
		Editor editor = prefs.edit();
		editor.putString("user_id", profilePic);
		editor.commit();
	}

	public static String getuser_id(SharedPreferences prefs) {
		String profilePic;
		profilePic = prefs.getString("user_id", "");
		return profilePic;
	}
	public static void saveusername(SharedPreferences prefs, String Useraccount) {
		Editor editor = prefs.edit();
		editor.putString("username", Useraccount);
		editor.commit();
	}

	public static String getusername(SharedPreferences prefs) {
		String profilePic;
		profilePic = prefs.getString("username", null);
		return profilePic;
	}
	public static void saveMeterarray(SharedPreferences prefs, String profilePic) {
		Editor editor = prefs.edit();
		editor.putString("meterarray", profilePic);
		editor.commit();
	}

	public static String getMeterarray(SharedPreferences prefs) {
		String profilePic;
		profilePic = prefs.getString("meterarray", "");
		return profilePic;
	}

	public static void saveskip(SharedPreferences prefs, String profilePic) {
		Editor editor = prefs.edit();
		editor.putString("skip", profilePic);
		editor.commit();
	}

	public static String getskip(SharedPreferences prefs) {
		String profilePic;
		profilePic = prefs.getString("skip", "");
		return profilePic;
	}
	public static void savedate(SharedPreferences prefs, String profilePic) {
		Editor editor = prefs.edit();
		editor.putString("date", profilePic);
		editor.commit();
	}

	public static String getdate(SharedPreferences prefs) {
		String profilePic;
		profilePic = prefs.getString("date", "");
		return profilePic;
	}

	public static void SaveNotificationarray(SharedPreferences prefs, String regId) {
		Editor editor = prefs.edit();
		editor.putString("notification", regId);
		editor.commit();
	}

	public static String GetNotificationarray(SharedPreferences prefs) {
		String strcity = null;
		try {
			strcity = prefs.getString("notification", null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return strcity;
	}




}
