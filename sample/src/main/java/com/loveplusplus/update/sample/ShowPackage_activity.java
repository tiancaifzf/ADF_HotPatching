package com.loveplusplus.update.sample;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by max-fzf on 26.03.16.
 */
public class ShowPackage_activity extends Activity {
    private static Context mContext;
    Process localProcess = null;
    public String fullname;
    ArrayList<HashMap<String, Object>> applist = new ArrayList<HashMap<String, Object>>();
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         String fullname;
        setContentView(R.layout.activity_show);
         SharedPreferences spref = getPreferences(MODE_PRIVATE);
         SharedPreferences.Editor editor = spref.edit();
         mContext = this.getApplicationContext();
        List<PackageInfo> packages = getPackageManager().getInstalledPackages(0);
        for (int i = 0; i < packages.size(); i++) {
            PackageInfo packageInfo = packages.get(i);
            AppInfo tmpinfo = new AppInfo();
            HashMap<String, Object> map = new HashMap<String, Object>();
            if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == ApplicationInfo.FLAG_SYSTEM) {//过滤掉系统程序
                // Log.d("System software", "We met System " + i + "th software: " + packageInfo.packageName);
                continue;
            }


            PackageInfo packageinfo = null;
            try {
                packageinfo = getPackageManager().getPackageInfo(packageInfo.packageName.toString(), 0);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            if (packageinfo == null) {
                return;
            }
            // 创建一个类别为CATEGORY_LAUNCHER的该包名的Intent
            Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
            resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
            resolveIntent.setPackage(packageinfo.packageName);

            // 通过getPackageManager()的queryIntentActivities方法遍历
            List<ResolveInfo> resolveinfoList = getPackageManager()
                    .queryIntentActivities(resolveIntent, 0);
            if (resolveinfoList.iterator().hasNext()) {
                ResolveInfo resolveinfo = resolveinfoList.iterator().next();
            if (resolveinfo != null) {
                // packagename = 参数packname
                String packageName = resolveinfo.activityInfo.packageName;
                // 这个就是我们要找的该APP的LAUNCHER的Activity[组织形式：packagename.mainActivityname]
                fullname = resolveinfo.activityInfo.name;
                Log.d("##############", "Activityname:" + fullname);
                map.put("packagename", fullname);
            }

            map.put("appicon", packageInfo.applicationInfo.loadIcon(getPackageManager()).getCurrent());
            map.put("appname", tmpinfo.appName = packageInfo.applicationInfo.loadLabel(getPackageManager()).toString());
            // String fullname=packageInfo.packageName.toString()+packageInfo.applicationInfo.className.toString();
            //map.put("packagename", tmpinfo.packagename = packageInfo.applicationInfo.className.toString());
            map.put("ItemButton", R.drawable.android_logo);
            applist.add(map);
        }
        }
        ListView listView = (ListView) findViewById(R.id.listView);
        // SimpleAdapter listadapter = new SimpleAdapter(this, applist, R.layout.simple, new String[]{"appicon", "appname", "packagename"}, new int[]{R.id.ItemImage, R.id.ItemName, R.id.ItemInfo});
        //listView.setAdapter(listadapter);



        final LvButtonAdapter listItemAdapter = new LvButtonAdapter(
                this,
                applist,//数据源
                R.layout.simple,//ListItem的XML实现
                mContext
        );
        listView.setAdapter(listItemAdapter);
    }




    public static void edit_true(Context ctx,String app_name){
        SharedPreferences spref = ctx.getSharedPreferences("ShowPackage_activity", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = spref.edit();
        editor.putBoolean(app_name,true);
        editor.apply();
        editor.commit();
    }
    public static void edit_false(Context ctx,String app_name){
        SharedPreferences spref = ctx.getSharedPreferences("ShowPackage_activity", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = spref.edit();
        editor.putBoolean(app_name,false);
        editor.apply();
        editor.commit();
    }
    public static boolean boolean_statue(Context ctx,String name){
        SharedPreferences spref = ctx.getSharedPreferences("ShowPackage_activity", Context.MODE_PRIVATE);
        boolean boolvalue=spref.getBoolean(name, false);
        return boolvalue;
    }
    public static void Hook_function(String packagename) throws IOException {
        char[] a=packagename.toCharArray();
        for(int i=0;i<a.length;i++)
        {
            if(a[i]=='.')
            {
              a[i]='/';
            }
        }
        String after="L"+new String(a)+";,VL,onCreate,Lcom/example/max_fzf/hook/MainActivity;,VL,onCreate";
        MainActivity.handle(after);
    }
    public static void App_disable_rewrite(Context ctx, ArrayList<HashMap<String, Object>> appList) throws IOException {
          Log.d("注意！！！","Disable_app 补丁被重新写入！");
            SharedPreferences spref = ctx.getSharedPreferences("ShowPackage_activity", Context.MODE_PRIVATE);
            MainActivity.Delete_df_file();
            for (int i = 0; i < appList.size(); i++) {
                HashMap<String, Object> appInfo = appList.get(i);
                String info = (String) appInfo.get("packagename");
                boolean boolvalue = spref.getBoolean(info, false);
                Log.d("注意！",info+"="+boolvalue);
                if (boolvalue) {
                    char[] a = info.toCharArray();
                    for (int j = 0; j < a.length; j++) {
                        if (a[j] == '.') {
                            a[j] = '/';
                        }
                    }
                    String after = "L" + new String(a) + ";,VL,onCreate,Lcom/example/max_fzf/hook/MainActivity;,VL,onCreate";
                    MainActivity.handle(after);
                }
            }

    }
}
