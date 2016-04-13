package com.loveplusplus.update.sample;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class LvButtonAdapter extends BaseAdapter {

	private ArrayList<HashMap<String, Object>> mAppList;
	private LayoutInflater mInflater;
	private Context mContext;
	private String[] keyString;
	private int[] valueViewID;
	private ItemView itemView;
	public  Context hcontext;
    public static boolean tu=false;
	//SharedPreferences spref = getPreferences(MODE_PRIVATE);
	private class ItemView {
		ImageView ItemImage;
		TextView ItemName;
		TextView ItemInfo;
		Button ItemButton;
		Boolean check_boolean;
	}

	public LvButtonAdapter(Context c, ArrayList<HashMap<String, Object>> appList, int resource, Context context) {
		mAppList = appList;
		mContext = c;
		hcontext=context;
		mInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		//return 0;
		return mAppList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		//return null;
		return mAppList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		//return 0;
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		//return null;
		if (convertView != null) {
			itemView = (ItemView) convertView.getTag();
		} else {
			convertView = mInflater.inflate(R.layout.simple, null);
			itemView = new ItemView();
			itemView.ItemImage = (ImageView)convertView.findViewById(R.id.ItemImage);
			itemView.ItemName = (TextView)convertView.findViewById(R.id.ItemName);
			itemView.ItemInfo = (TextView)convertView.findViewById(R.id.ItemInfo);
			itemView.ItemButton = (CheckBox)convertView.findViewById(R.id.ItemButton);
			convertView.setTag(itemView);
		}
		final HashMap<String, Object> appInfo = mAppList.get(position);
		if (appInfo != null) {
			final String name = (String) appInfo.get("appname");
			final String info = (String) appInfo.get("packagename");
			itemView.ItemName.setText(name);
			itemView.ItemInfo.setText(info);
			itemView.ItemImage.setImageDrawable((Drawable) appInfo.get("appicon"));
		}
		final CheckBox c= (CheckBox) itemView.ItemButton;
		String a=itemView.ItemInfo.getText().toString();
		c.setChecked(ShowPackage_activity.boolean_statue(hcontext, a));
			c.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v) {
					if(c.isChecked())
					{
						Log.d("FFFFFFFFFFFFF", "Try to Turn on!");
						tu=true;
						ShowPackage_activity.edit_true(hcontext, (String) mAppList.get(position).get("packagename"));
						Log.d("!!!!!!", "INFO:" + mAppList.get(position).get("packagename"));
						try {
							ShowPackage_activity.Hook_function((String) mAppList.get(position).get("packagename"));
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					else
					{
						Log.d("OOOOOOOOOOOOOO","Try to Turn off!");
						ShowPackage_activity.edit_false(hcontext, (String) mAppList.get(position).get("packagename"));
						try {
							ShowPackage_activity.App_disable_rewrite(hcontext,mAppList);
							Log.d("注意！！", String.valueOf(MainActivity.is_hotpatching));
							MainActivity.hotpatching_rewrite();
							MainActivity.AD_blocker_rewrite();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			});

		/*
		{
			c.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v) {
					Log.d("OOOOOOOOOOOOOO","Turn on!");
					//ShowPackage_activity.edit_true(hcontext, (String) mAppList.get(position).get("packagename"));
					//Log.d("!!!!!!", "INFO:" + mAppList.get(position).get("packagename"));
					//try {
					//	ShowPackage_activity.Hook_function((String) mAppList.get(position).get("packagename"));
					//} catch (IOException e) {
					//	e.printStackTrace();
					//}
				}
			});
		}
        */
		return convertView;
	}
}