package com.technotech.technotechapplication.util;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.technotech.technotechapplication.R;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

	static Context context;
	private int progressStatus = 0;
	private Handler handler = new Handler();
	InputMethodManager imm;
	public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;
	public Utils(Context c) {
		this.context = c;
	}
	
	/**
	 * Convert Dp to Pixel
	 * 将dp转换为pixel
	 */
	public static int dpToPx(float dp, Resources resources){
		float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.getDisplayMetrics());
		return (int) px;
	}
	
	/**
	 * @param value
	 * @return 将dip或者dp转为float
	 */
	public static float dipOrDpToFloat(String value) {
		if (value.indexOf("dp") != -1) {
			value = value.replace("dp", "");
		}
		else {
			value = value.replace("dip", "");
		}
		return Float.parseFloat(value);
	}
	
	
	public static int getRelativeTop(View myView) {
//	    if (myView.getParent() == myView.getRootView())
	    if(myView.getId() == android.R.id.content)
	        return myView.getTop();
	    else
	        return myView.getTop() + getRelativeTop((View) myView.getParent());
	}
	
	public static int getRelativeLeft(View myView) {
//	    if (myView.getParent() == myView.getRootView())
		if(myView.getId() == android.R.id.content)
			return myView.getLeft();
		else
			return myView.getLeft() + getRelativeLeft((View) myView.getParent());
	}
	

	public static String getFormatedDate(String date, SimpleDateFormat input,
                                         SimpleDateFormat output) throws ParseException {
		/*
		 * SimpleDateFormat input = new SimpleDateFormat("yyyy-dd-MM hh:mm");
		 * SimpleDateFormat output = new SimpleDateFormat("dd MMMM yyyy");
		 */
		Date dateFormated = input.parse(date);
		Log.v("getFormatedDate",
				"formated Date: " + output.format(dateFormated));
		return output.format(dateFormated);
	}
	public int getScreenDensity() 
	{
		DisplayMetrics metrics = context.getResources().getDisplayMetrics();
		Log.v("Density ", "density "+ metrics.densityDpi);
		return metrics.densityDpi;
	}
	
	
	public int[] getScreenWidthHeight()
	{
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		int width = display.getWidth();  // deprecated
		int height = display.getHeight();  // deprecated
		
		int []wh={width,height};
		
		return wh;
	}
	
	public final  boolean isValidEmail(CharSequence target) {
	    if (target == null || target.length()<1) {
	        return false;
	    } else {
	        return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
	    }
	}
	public boolean isUrl(String url)
	{
		String urlRegEx = "^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
	    Pattern pattern = Pattern.compile(urlRegEx);
	    Matcher m = pattern.matcher(url);
	    if(m.matches())
	   	    return true;
	    else
	    	return false;
	}
	public String getDayOfMonthSuffix(final int n) {
	   
	    if (n >= 11 && n <= 13) {
	        return "th";
	    }
	    switch (n % 10) {
	        case 1:  return "st";
	        case 2:  return "nd";
	        case 3:  return "rd";
	        default: return "th";
	    }
	}
	public void ButtonClickEffect(final View v)
	{
//		  v.setEnabled(false);
		
		AlphaAnimation obja = new AlphaAnimation(1.0f, 0.3f);
		obja.setDuration(5);
		obja.setFillAfter(false);
		v.startAnimation(obja);
	}	

	public boolean checkInternetConnection()
	{
		/*ConnectivityManager conMgr = (ConnectivityManager) context.getSystemService (Context.CONNECTIVITY_SERVICE);
		if(! (conMgr.getActiveNetworkInfo() != null && conMgr.getActiveNetworkInfo().isAvailable() 
				&&    conMgr.getActiveNetworkInfo().isConnected())) 
		{
			System.out.println("Internet Connection Not Present");
			return false;

		}
		else
		{
			return true;
		}*/
		ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null)
        {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED)
                    {
                        return true;
                    }

        }
        return false;
	}
	/*public  static Typeface getsansregular()
	{
		return Typeface.createFromAsset(context.getAssets(),
				"fonts/futrathin.ttf");
	}
	public  static Typeface getsansregularbold()
	{
		return Typeface.createFromAsset(context.getAssets(),
				"fonts/futrabold.ttf");
	}
	public static Typeface geteuro()
	{
		return Typeface.createFromAsset(context.getAssets(),
				"fonts/eurostileextended.ttf");
	}
	public Typeface getAGBookstencil()
	{
		return Typeface.createFromAsset(context.getAssets(),
				"fonts/AG Book Stencil.ttf");    	
	}
	
	public Typeface getComfortaa_light()
	{
		return Typeface.createFromAsset(context.getAssets(),
				"fonts/Comfortaa-Light.ttf");    	
	}*/
	
	public boolean isAppOnForeground(Context context) {
	    ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
	    List<RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
	    if (appProcesses == null) {
	      return false;
	    }
	    final String packageName = context.getPackageName();
	    for (RunningAppProcessInfo appProcess : appProcesses) {
	      if (appProcess.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND && appProcess.processName.equals(packageName)) {
	        return true;
	      }
	    }
	    return false;
	  }
	
	public void hideKeyboard()
	{
		 imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
		imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
	}
	
	public void showKeyboard()
	{
		 imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
	   
	}
	public void setupOutSideTouchHideKeyboard(final View view) {

	    //Set up touch listener for non-text box views to hide keyboard.
	    if(!(view instanceof EditText)) {
	        view.setOnTouchListener(new OnTouchListener() {

	            @Override
				public boolean onTouch(View v, MotionEvent event) {

						hideKeyboard();
					return false;
	            }

	        });
	    }
	    //If a layout container, iterate over children and seed recursion.
	    if (view instanceof ViewGroup) {
	        for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
	            View innerView = ((ViewGroup) view).getChildAt(i);
	            setupOutSideTouchHideKeyboard(innerView);
	        }
	    }
	}
	
	
	public Uri getOutputMediaFileUri(String dirName) {
		File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
								dirName);

		// Create the storage directory if it does not exist
		if (!mediaStorageDir.exists()) {
			if (!mediaStorageDir.mkdirs()) {
				Log.d(dirName, "Oops! Failed create "
						+ dirName + " directory");
				return null;
			}
		}

		// Create a media file name
		
		File mediaFile;
		int n = 10000;
		Random generator = new Random();

		n = generator.nextInt(n);
		mediaFile = new File(mediaStorageDir.getPath() + File.separator
				+ "IMG-" + n + ".jpg");

		return Uri.fromFile(mediaFile);
	}
	
	
	public int convertToPixel(int value)
	{
		Resources r = context.getResources();
		float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 14, r.getDisplayMetrics());
		return (int)px;
	}
	public String getAppVersion()
	{
		String version = "";
		try {
			PackageInfo pInfo = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0);
			version = pInfo.versionName;
		} catch (Exception e) {
			e.printStackTrace();
		}
//		Log.v("getAppVersion", "App Version: "+ version);
		return version;
	}
	
	public void generateHashKey()
	{
		/**This method is generate Hash key for Facebook*/
		try {
            PackageInfo info = context.getPackageManager().getPackageInfo("com.gravitate", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String hashKey=Base64.encodeToString(md.digest(), Base64.DEFAULT);
                Log.v("Hash Key:", hashKey);
                }
        } catch (NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
	}

	public void customToast(String text)
	{
		Toast toast = Toast.makeText(context,
				text,
				Toast.LENGTH_LONG);
		TextView tv=(TextView) toast.getView().findViewById(android.R.id.message);
		tv.setTextColor(context.getResources().getColor(R.color.d1white));
		//tv.setTypeface(getsansregular());
		toast.getView().setBackgroundColor(context.getResources().getColor(R.color.d1purple));
		toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
		View vieew = toast.getView();
		//  vieew.setBackgroundColor(Color.parseColor("#BD8BDC"));
		vieew.setBackgroundResource(R.drawable.toastback);
		toast.setView(vieew);
		/*if (Constants.Accent!=0){
			vieew.setBackgroundColor(Constants.Accent);
		}
		if (Constants.Primary!=0){
			toast.getView().setBackgroundColor(Constants.Primary);
		}*/
		toast.show();
	}

	public void setSpans(Spannable text, int size) {

		int blueColor = context.getResources().getColor(R.color.d1gold)/*0xff0000ff*//*Color.parseColor(String.valueOf(R.color.textColorPrimary))*/;
		ForegroundColorSpan blue = new ForegroundColorSpan(blueColor);
		text.setSpan(new StyleSpan(Typeface.BOLD), 0,  size, 0);
		text.setSpan(blue, 0, size, 0);
		//text.setSpan(red, 0, namesplit[1].length(), 0);
		Log.v("","");
	}
	public void setSpanwhite(TextView tv, String msg, int size) {
		Spannable text = new SpannableStringBuilder(msg);
		int Color = context.getResources().getColor(R.color.d1gold)/*0xff0000ff*//*Color.parseColor(String.valueOf(R.color.textColorPrimary))*/;
		ForegroundColorSpan blue = new ForegroundColorSpan(Color);
		text.setSpan(new StyleSpan(Typeface.BOLD), 0,  size, 0);
		text.setSpan(blue, 0, size, 0);
		tv.setText(text);
		//text.setSpan(red, 0, namesplit[1].length(), 0);
		Log.v("","");
	}

	public void setSpanstext(TextView tv, String msg, int size) {
		Spannable text = new SpannableStringBuilder(msg);
		int Color = context.getResources().getColor(R.color.d1gold)/*0xff0000ff*//*Color.parseColor(String.valueOf(R.color.textColorPrimary))*/;
		ForegroundColorSpan blue = new ForegroundColorSpan(Color);
		text.setSpan(new StyleSpan(Typeface.BOLD), 0,  size, 0);
		text.setSpan(blue, 0, size, 0);
		tv.setText(text);
		//text.setSpan(red, 0, namesplit[1].length(), 0);
		Log.v("","");
	}


	public String date(Date date)
	{
		String datestring=null;
		SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		try {
			datestring = format.format(date);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return datestring;
	}
	public Date dateconvert(String date)
	{
		Date stringdate=null;
		SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		try {
			stringdate = format.parse(date);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return stringdate;
	}


	public static boolean checkPermission(final Context context)
	{
		int currentAPIVersion = Build.VERSION.SDK_INT;
		if(currentAPIVersion>= Build.VERSION_CODES.M)
		{
			if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
				if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.READ_EXTERNAL_STORAGE)) {
					AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
					alertBuilder.setCancelable(true);
					alertBuilder.setTitle("Permission necessary");
					alertBuilder.setMessage("External storage permission is necessary");
					alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
						@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
						public void onClick(DialogInterface dialog, int which) {
							ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
						}
					});
					AlertDialog alert = alertBuilder.create();
					alert.show();

				} else {
					ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
				}
				return false;
			} else {
				return true;
			}
		} else {
			return true;
		}
	}
	public static boolean checkPermissionwifi(final Context context)
	{
		int currentAPIVersion = Build.VERSION.SDK_INT;
		if(currentAPIVersion>= Build.VERSION_CODES.M)
		{
			if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_WIFI_STATE) != PackageManager.PERMISSION_GRANTED) {
				if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.ACCESS_WIFI_STATE)) {
					AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
					alertBuilder.setCancelable(true);
					alertBuilder.setTitle("Permission necessary");
					alertBuilder.setMessage("Access Wifi permission is necessary");
					alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
						@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
						public void onClick(DialogInterface dialog, int which) {
							ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.ACCESS_WIFI_STATE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
						}
					});
					AlertDialog alert = alertBuilder.create();
					alert.show();

				} else {
					ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.ACCESS_WIFI_STATE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
				}
				return false;
			} else {
				return true;
			}
		} else {
			return true;
		}
	}
	public  boolean isPackageExisted(Context c, String targetPackage) {

		PackageManager pm = c.getPackageManager();
		try {
			PackageInfo info = pm.getPackageInfo(targetPackage,
					PackageManager.GET_META_DATA);
		} catch (NameNotFoundException e) {
			return false;
		}
		return true;
	}
	/*public ArrayList<item> getlistarray()    {
		ArrayList<item> listarray = new ArrayList<item>();
		item iteminner;
		iteminner = new item();
		iteminner.drawableId = R.mipmap.copyrite_icon;
		iteminner.name = "Company Profile";
		listarray.add(iteminner);
		iteminner = new item();
		iteminner.drawableId = R.mipmap.bnranch_icon;
		iteminner.name = "Branches";
		listarray.add(iteminner);
		iteminner = new item();
		iteminner.drawableId = R.mipmap.banner_icon;
		iteminner.name = "Banners";
		listarray.add(iteminner);
		iteminner = new item();
		iteminner.drawableId = R.mipmap.product_icon;
		iteminner.name = "Products";
		listarray.add(iteminner);
		iteminner = new item();
		iteminner.drawableId = R.mipmap.catlog_icon;
		iteminner.name = "Catalogue";
		listarray.add(iteminner);
		iteminner = new item();
		iteminner.drawableId = R.mipmap.followers;
		iteminner.name = "Follower List";
		listarray.add(iteminner);
		iteminner = new item();
		iteminner.drawableId = R.mipmap.social_icon;
		iteminner.name = "Social Media";
		listarray.add(iteminner);
		iteminner = new item();
		iteminner.drawableId = R.mipmap.offer_icon;
		iteminner.name = "Offers";
		listarray.add(iteminner);
		iteminner = new item();
		iteminner.drawableId = R.drawable.time;
		iteminner.name = "Opening Time";
		listarray.add(iteminner);
		iteminner = new item();
		iteminner.drawableId = R.drawable.share;
		iteminner.name = "Share";
		listarray.add(iteminner);
		iteminner = new item();
		iteminner.drawableId = R.mipmap.logout_icon;
		iteminner.name = "Log out";
		listarray.add(iteminner);
		return  listarray;
	}
*/

	public void editextnextfocus(EditText v)
	{
		v.setImeOptions(EditorInfo.IME_ACTION_NEXT);
	}

	public Bitmap loadBitmap(String url)
	{
		Bitmap bm = null;
		InputStream is = null;
		BufferedInputStream bis = null;
		try
		{
			URLConnection conn = new URL(url).openConnection();
			conn.connect();
			is = conn.getInputStream();
			bis = new BufferedInputStream(is, 8192);
			bm = BitmapFactory.decodeStream(bis);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally {
			if (bis != null)
			{
				try
				{
					bis.close();
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
			if (is != null)
			{
				try
				{
					is.close();
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		}
		return bm;
	}
	public String getRealPathFromURI(Context context, Uri contentURI) {
//        Cursor cursor = getActivity().getContentResolver().query(contentURI, null, null, null, null);
		String[] projection = { MediaStore.Images.Media.DATA };
		Cursor cursor = context.getContentResolver().query(contentURI, projection, null, null, null);
		//Some of gallery application is return null
		if (cursor == null) {
			return contentURI.getPath();
		} else {
			cursor.moveToFirst();
			int columnIndex = cursor.getColumnIndex(projection[0]);
			String picturePath = cursor.getString(columnIndex);
			cursor.close();
			return picturePath;
		}
	}

	public String getpath(Context context, Uri contentURI)
	{
		String path = null;
		if (Build.VERSION.SDK_INT < 11)
			path = Realpathutil.getRealPathFromURI_BelowAPI11(context, contentURI);
		else if (Build.VERSION.SDK_INT < 19)
			path = Realpathutil.getRealPathFromURI_API11to18(context, contentURI);
		else
			path = Realpathutil.getRealPathFromURI_API19(context, contentURI);

		return path;
	}

	public Uri bitmapToUriConverter(Bitmap mBitmap) {
		Uri uri = null;
		try {
			final BitmapFactory.Options options = new BitmapFactory.Options();
			// Calculate inSampleSize
			// options.inSampleSize = calculateInSampleSize(options, 100, 100);
			// Decode bitmap with inSampleSize set
			options.inJustDecodeBounds = false;
			Bitmap newBitmap = Bitmap.createScaledBitmap(mBitmap, 200, 200,
					true);
			File file = new File(context.getFilesDir(), "Image"
					+ new Random().nextInt() + ".jpeg");
			FileOutputStream out =context.openFileOutput(file.getName(),
					Context.MODE_WORLD_READABLE);
			newBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
			out.flush();
			out.close();
			//get absolute path
			String realPath = file.getAbsolutePath();
			File f = new File(realPath);
			uri = Uri.fromFile(f);
		} catch (Exception e) {
			Log.e("Your Error Message", e.getMessage());
		}
		return uri;
	}

	public static Drawable drawableFromUrl(String url) throws IOException {
		Bitmap x;

		HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
		connection.connect();
		InputStream input = connection.getInputStream();

		x = BitmapFactory.decodeStream(input);
		return new BitmapDrawable(x);
	}

	public static SpannableString changeTextFont(Typeface typeface, CharSequence string) {
		SpannableString s = new SpannableString(string);
		s.setSpan(new TypefaceSpanText(typeface), 0, s.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		return s;
	}
	public static SpannableString[] changeTextFont(Typeface typeface, String[] string) {
		SpannableString[] s = new SpannableString[string.length];
		for(int i=0;i<s.length;i++)
		{
			s[i]=new SpannableString(string[i]);
			s[i].setSpan(new TypefaceSpanText(typeface), 0, s[i].length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			//s[i].setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.d1purple)), 0, s[i].length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		}
		return s;
	}

	public static List changeTextFont(Typeface typeface, List<Object> string) {
		SpannableString[] s = new SpannableString[string.size()];
		ArrayList<Object> ls=new ArrayList(string.size());
		for(int i=0;i<s.length;i++)
		{
			s[i]=new SpannableString((CharSequence) string.get(i));
			s[i].setSpan(new TypefaceSpanText(typeface), 0, s[i].length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			ls.add(s[i]);

			//s[i].setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.d1purple)), 0, s[i].length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		}

		return ls;
	}

}
