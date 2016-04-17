package com.loveplusplus.update;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.zip.GZIPInputStream;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

public class UpdateChecker extends Fragment {

	//private static final String NOTIFICATION_ICON_RES_ID_KEY = "resId";
	private static final String NOTICE_TYPE_KEY = "type";
    private static final String APP_UPDATE_SERVER_URL = "app_update_server_url";
	//private static final String SUCCESSFUL_CHECKS_REQUIRED_KEY = "nChecks";
	private static final int NOTICE_NOTIFICATION = 2;
	private static final int NOTICE_DIALOG = 1;
	private static final String TAG = "UpdateChecker";

	private FragmentActivity mContext;
	private Thread mThread;
	private int mTypeOfNotice;
	public static boolean a = false;
	public Context CC;
	/**
	 * Show a Dialog if an update is available for download. Callable in a
	 * FragmentActivity. Number of checks after the dialog will be shown:
	 * default, 5
	 * 
	 * @param fragmentActivity
	 *            Required.
	 */
	public static void checkForDialog(FragmentActivity fragmentActivity,String url,Context c) {

		FragmentTransaction content = fragmentActivity.getSupportFragmentManager().beginTransaction();
		UpdateChecker updateChecker = new UpdateChecker();
		updateChecker.getcontext(c);
		Bundle args = new Bundle();
		args.putInt(NOTICE_TYPE_KEY, NOTICE_DIALOG);
        args.putString(APP_UPDATE_SERVER_URL,url);
		//args.putInt(SUCCESSFUL_CHECKS_REQUIRED_KEY, 5);
		updateChecker.setArguments(args);
		content.add(updateChecker, null).commit();
	}

    public void getcontext(Context context){
		CC=context;
	}
    public static  boolean error(){
		if(a) {
			Log.d("!!!!!!!!!!!","2222222222222");
			return true;
		}
		else{
			Log.d("!!!!!!!!!!!","3333333333333");
			return false;
		}
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mContext = (FragmentActivity) activity;
		Bundle args = getArguments();
		mTypeOfNotice = args.getInt(NOTICE_TYPE_KEY);
        String url = args.getString(APP_UPDATE_SERVER_URL);
		//mSuccessfulChecksRequired = args.getInt(SUCCESSFUL_CHECKS_REQUIRED_KEY);
		//mNotificationIconResId = args.getInt(NOTIFICATION_ICON_RES_ID_KEY);
		checkForUpdates(url);
	}

	/**
	 * Heart of the library. Check if an update is available for download
	 * parsing the desktop Play Store page of the app
	 */
	private void checkForUpdates(final String url) {
		mThread = new Thread() {
			@Override
			public void run() {
				//if (isNetworkAvailable(mContext)) {

					String json = sendPost(url);
				   //Log.d("!!!!!!!!!","JSON:"+json.toString());
					if(json!=null){
						parseJson(json);
					}else{
						Log.e(TAG, "can't get app update json");
					}
				//}
			}

		};
		mThread.start();
	}

	protected String sendPost(String urlStr) {
		HttpURLConnection uRLConnection = null;
		InputStream is = null;
		BufferedReader buffer = null;
		String result = null;
		try {
			URL url = new URL(urlStr);
			Log.d("~~~~~~~~~~~~~","POSTURL:"+urlStr);
			//String urlStr=("http://140.112.29.194:8080/Root_test.apk");
			uRLConnection = (HttpURLConnection) url.openConnection();
			uRLConnection.setDoInput(true);
			uRLConnection.setDoOutput(true);
			uRLConnection.setRequestMethod("POST");
			uRLConnection.setUseCaches(false);
			uRLConnection.setConnectTimeout(10 * 1000);
			uRLConnection.setReadTimeout(10 * 1000);
			uRLConnection.setInstanceFollowRedirects(false);
			uRLConnection.setRequestProperty("Connection", "Keep-Alive");
			uRLConnection.setRequestProperty("Charset", "UTF-8");
			uRLConnection.setRequestProperty("Accept-Encoding", "gzip, deflate");
			uRLConnection.setRequestProperty("Content-Type", "application/json");
			uRLConnection.connect();
			is = uRLConnection.getInputStream();

			String content_encode = uRLConnection.getContentEncoding();

			if (null != content_encode && !"".equals(content_encode) && content_encode.equals("gzip")) {
				is = new GZIPInputStream(is);
			}

			buffer = new BufferedReader(new InputStreamReader(is));
			StringBuilder strBuilder = new StringBuilder();
			String line;
			while ((line = buffer.readLine()) != null) {
				strBuilder.append(line);
			}
			result = strBuilder.toString();
		} catch (Exception e) {
             a=true;
			//Toast.makeText(, "沒有網路連接！", Toast.LENGTH_SHORT).show();
			 Log.d("!!!!!!!!!!!","1111111111");
			Log.e(TAG, "http post error", e);
			uRLConnection.disconnect();
		} finally {
			if(buffer!=null){
				try {
					buffer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(is!=null){
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(uRLConnection!=null){
				uRLConnection.disconnect();
			}
		}
		return result;
	}

	
	private void parseJson(String json) {
		mThread.interrupt();
		Looper.prepare();
		try {
			
			JSONObject obj = new JSONObject(json);
			String updateMessage = obj.getString(Constants.APK_UPDATE_CONTENT);
			Log.d("$$$$","UpdateMessage:"+updateMessage);
			String apkUrl = obj.getString(Constants.APK_DOWNLOAD_URL);
			Log.d("$$$$","apkUrl:"+apkUrl);
			int apkCode = obj.getInt(Constants.APK_VERSION_CODE);
			Log.d("$$$$","apkCode:"+apkCode);
			int versionCode = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0).versionCode;
			Log.d("$$$$","VersionCode:"+versionCode);

			if (apkCode > versionCode) {
				if (mTypeOfNotice == NOTICE_NOTIFICATION) {
					//showNotification(updateMessage,apkUrl);
				} else if (mTypeOfNotice == NOTICE_DIALOG) {
					showDialog(updateMessage,apkUrl);
				}
			} else {
				//Toast.makeText(mContext, mContext.getString(R.string.app_no_new_update), Toast.LENGTH_SHORT).show();
			}

		} catch (PackageManager.NameNotFoundException ignored) {
		} catch (JSONException e) {
			Log.e(TAG, "parse json error", e);
		}
	}
	public void showDialog(String content,String apkUrl) {
        UpdateDialog d = new UpdateDialog();
		Bundle args = new Bundle();
		args.putString(Constants.APK_UPDATE_CONTENT, content);
		args.putString(Constants.APK_DOWNLOAD_URL, apkUrl);
		d.setArguments(args);
		d.show(mContext.getSupportFragmentManager(), null);
	}

	public static boolean isNetworkAvailable(Context context) {
		boolean connected = false;
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (cm != null) {
			NetworkInfo ni = cm.getActiveNetworkInfo();
			if (ni != null) {
				connected = ni.isConnected();
			}
		}
		return connected;
	}

	
}
