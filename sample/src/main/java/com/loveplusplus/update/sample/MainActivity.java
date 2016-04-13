package com.loveplusplus.update.sample;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.DialogPreference;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.loveplusplus.update.UpdateChecker;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

public class MainActivity extends ActionBarActivity
{
    //public boolean ischecked;
    public static boolean is_hotpatching=false;
    public static boolean is_ADblocker=false;
    public static String result;
    public String AD_resources;
     Process localProcess = null;
    private static Context mContext ;
     OutputStream localOutputStream = null;
    public static final String KEY="com.example.max_fzf.root_test";
    protected static final String APP_UPDATE_SERVER_URL = "http://140.112.29.194:3333/update_check";
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext=this.getApplicationContext();
        UpdateChecker.checkForDialog(MainActivity.this, APP_UPDATE_SERVER_URL,MainActivity.this);
           /*
            if(UpdateChecker.error()){
                Log.d("!!!!!!!!!!!", "沒有網路連接！");
                Toast.makeText(mContext, "沒有網路連接！", Toast.LENGTH_SHORT).show();
            }
           */
        SharedPreferences spref = getPreferences(MODE_PRIVATE);
        final  SharedPreferences.Editor editor = spref.edit();
        Switch ss=(Switch)findViewById(R.id.switch1);
        final boolean boolvalue=spref.getBoolean("KEY_Boolean",false);
        ss.setChecked(boolvalue);
        Button rb= (Button) findViewById(R.id.button);
        Button jump= (Button) findViewById(R.id.button2);
        Button check= (Button) findViewById(R.id.button3);
        Button test_button= (Button) findViewById(R.id.button4);

        test_button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                AD_Resources_Analysis Operator=new AD_Resources_Analysis();
                try {
                    AD_resources=Operator.Operator();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Log.d("廣告讀入！！",AD_resources);
                try {
                    localProcess = Runtime.getRuntime().exec("su");
                    localOutputStream = localProcess.getOutputStream();
                    DataOutputStream localDataOutputStream = new DataOutputStream(localOutputStream);
                    localDataOutputStream.writeBytes("mount -o remount,rw /system\n");
                    localDataOutputStream.writeBytes("mv sdcard/Android/data/com.loveplusplus.update.sample/cache/hook.apk /system/dynamic_framework/hook.apk\n");
                    localDataOutputStream.writeBytes("echo \"" +AD_resources+"\" >> /system/df_file\n");
                    localDataOutputStream.writeBytes("mount -o remount,ro /system\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                /*
                Analysis_df_file operator=new Analysis_df_file();
                try {
                     result=operator.Operator();
                    Log.d("他走到了这样一部！！！！",result);
                    is_hotpatching=true;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    localProcess = Runtime.getRuntime().exec("su");
                    localOutputStream = localProcess.getOutputStream();
                    DataOutputStream localDataOutputStream = new DataOutputStream(localOutputStream);
                    localDataOutputStream.writeBytes("mount -o remount,rw /system\n");
                   // localDataOutputStream.writeBytes("mv sdcard/Android/data/com.loveplusplus.update.sample/cache/hook.apk /system/dynamic_framework/hook.apk\n");
                    localDataOutputStream.writeBytes("echo \"" +result+"\" >> /system/df_file\n");
                    localDataOutputStream.writeBytes("mount -o remount,ro /system\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            */}
        });




        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String filename="sdcard/Android/data/com.loveplusplus.update.sample/cache/hook.zip";
                String path="sdcard/Android/data/com.loveplusplus.update.sample/cache/";
                try {
                    Unzip.unzip(filename,path);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                Analysis_df_file operator=new Analysis_df_file();
                try {
                    String result=operator.Operator();
                    Log.d("NOTICE!!!!",result);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    localProcess = Runtime.getRuntime().exec("su");
                    localOutputStream = localProcess.getOutputStream();
                    DataOutputStream localDataOutputStream = new DataOutputStream(localOutputStream);
                    localDataOutputStream.writeBytes("mount -o remount,rw /system\n");
                    localDataOutputStream.writeBytes("mv sdcard/Android/data/com.loveplusplus.update.sample/cache/hook.apk /system/dynamic_framework/hook.apk\n");
                    localDataOutputStream.writeBytes("echo \"" +result+"\" >> /system/df_file\n");
                    localDataOutputStream.writeBytes("mount -o remount,ro /system\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }

                File fexit=new File("/system/dynamic_framework/hook.apk");
                File df_file=new File("/system/df_file");
                while(true)
                {
                    if (fexit.exists()&&df_file.exists()) {
                        //Toast.makeText(getApplicationContext(),"Hot Patching Successful!\n Please Reboot!",Toast.LENGTH_SHORT).show();
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setIcon(R.drawable.ic_launcher);
                        builder.setTitle("ADF Message");
                        builder.setMessage("补丁安装完成！");
                        builder.setPositiveButton("现在重启", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                              reboot();
                            }
                        });
                        builder.setNegativeButton("稍后重启", null);
                        builder.show();
                        break;
                    }
                }

            }
        });
        jump.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            jump();
            }
        });
        rb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                reboot();
            }
        });
        ss.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    try {
                        is_hotpatching=true;
                        localProcess = Runtime.getRuntime().exec("su");
                        localOutputStream = localProcess.getOutputStream();
                        DataOutputStream localDataOutputStream = new DataOutputStream(localOutputStream);
                        localDataOutputStream.writeBytes("mount -o remount,rw /system\n");
                        //localDataOutputStream.writeBytes("rm -r /system/df_file\n");
                        //Unity 广告
                      //  localDataOutputStream.writeBytes("echo \"Lcom/unity3d/ads/android/view/UnityAdsFUllscreenActivity;,VL,loadAds,Lcom/example/max_fzf/hook/MainActivity;,VL,deleteAd\" >> /system/df_file\n");
                       // localDataOutputStream.writeBytes("echo \"Lcom/unity3d/ads/android/UnityAds;,Z,canShow,Lcom/example/max_fzf/hook/MainActivity;,Z,canShow\" > /system/df_file\n");
                        //Google 广告
                      //  localDataOutputStream.writeBytes("echo \"Lcom/google/android/gms/ads/AdView;,VL,loadAd,Lcom/example/max_fzf/hook/MainActivity;,VL,deleteAd\" >> /system/df_file\n");
                        //MoPub 广告
                       // localDataOutputStream.writeBytes("echo \"Lcom/mopub/mobileads/MoPubView;,V,loadAd,Lcom/example/max_fzf/hook/MainActivity;,V,deleteAd1\" >> /system/df_file\n");
                       // localDataOutputStream.writeBytes("echo \"Lcom/mopub/mobileads/MoPubActivity;,VL,load,Lcom/example/max_fzf/hook/MainActivity;,VL,deleteAd\" >> /system/df_file\n");
                       // localDataOutputStream.writeBytes("echo \"Lcom/mopub/nativeads/MraidActivity;,VL,loadAds,Lcom/example/max_fzf/hook/MainActivity;,VL,deleteAd\" >> /system/df_file\n");
                       // localDataOutputStream.writeBytes("echo \"Lcom/mopub/nativeads/MraidVideoPlayerActivity;,VL,loadAds,Lcom/example/max_fzf/hook/MainActivity;,VL,deleteAd\" >> /system/df_file\n");
                        //Facebook 广告
                       // localDataOutputStream.writeBytes("echo \"Lcom/facebook/ads/InterstitialAd;,V,loadAd,Lcom/example/max_fzf/hook/MainActivity;,V,deleteAd1\" >> /system/df_file\n");
                       //  localDataOutputStream.writeBytes("echo \"Lcom/facebook/ads/AdView;,V,loadAd,Lcom/example/max_fzf/hook/MainActivity;,V,deleteAd1\" >> /system/df_file\n");
                       // localDataOutputStream.writeBytes("echo \"Lcom/facebook/ads/AdView;,V,setAdListener,Lcom/example/max_fzf/hook/MainActivity;,V,deleteAd1\" >> /system/df_file\n");
                        //.writeBytes("echo \"Lcom/facebook/ads/NativeAd;,Z,isAdLoaded,Lcom/example/max_fzf/hook/MainActivity;,Z,isAdLoaded\" >> /system/df_file\n");
                        //localDataOutputStream.writeBytes("echo \"Lcom/facebook/ads/NativeAd;,V,loadAd,Lcom/example/max_fzf/hook/MainActivity;,V,deleteAd1\" >> /system/df_file\n");
                        //localDataOutputStream.writeBytes("echo \"Lcom/cmcm/adsdk/interstitial/PicksInterstitialActivity;,VL,loadAds,Lcom/example/max_fzf/hook/MainActivity;,VL,deleteAd\" >> /system/df_file\n");
                        //其他類廣告
                        //localDataOutputStream.writeBytes("echo \"Lcom/cmcm/adsdk/interstitial/PicksInterstitialActivity;,Z,isAdLoaded,Lcom/example/max_fzf/hook/MainActivity;,Z,isAdLoaded\" >> /system/df_file\n");
                        //localDataOutputStream.writeBytes("echo \"Lcom/cmcm/picks/PicksLoadingActivity;,Z,isAdLoaded,Lcom/example/max_fzf/hook/MainActivity;,Z,isAdLoaded\" >> /system/df_file\n");

                        localDataOutputStream.writeBytes("chmod 644 /system/df_file\n");
                        localDataOutputStream.writeBytes("mount -o remount,ro /system\n");
                        editor.putBoolean("KEY_Boolean", true);
                        editor.apply();
                        editor.commit();
                        Log.d("ADF!!", "turn on !success!");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        is_ADblocker=false;
                        localProcess = Runtime.getRuntime().exec("su");
                        localOutputStream = localProcess.getOutputStream();
                        DataOutputStream localDataOutputStream = new DataOutputStream(localOutputStream);
                        String path = "\"Lcom/antutu/ABenchMark/ABenchMarkStart;,VL,onCreate,Lcom/example/max_fzf/hook/MainActivity;,VL,onCreate\"";
                        localDataOutputStream.writeBytes("mount -o remount,rw /system\n");
                        localDataOutputStream.writeBytes("rm -r /system/df_file\n");
                        Rewrite_df_file r=new Rewrite_df_file();
                        r.Hot_patching_rewrite();
                        //ShowPackage_activity.App_disable_rewrite();
                        editor.putBoolean("KEY_Boolean", false);
                        editor.commit();
                        Log.d("ADF!!", "turn off !success!");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }//非选中时 do some thing
                }
            }
        });

    }
    public void reboot(){
        {
            try {
                localProcess = Runtime.getRuntime().exec("su");
                localOutputStream = localProcess.getOutputStream();
                DataOutputStream localDataOutputStream = new DataOutputStream(localOutputStream);
                localDataOutputStream.writeBytes("reboot\n");

            } catch (IOException e) {
                e.printStackTrace();
            }
            localOutputStream = localProcess.getOutputStream();

        }
    }
    public void jump(){
        Intent J=new Intent(this,ShowPackage_activity.class);
        startActivity(J);
    }
    public static void handle(String packagename) throws IOException {



        Process localProcess = null;
        OutputStream localOutputStream = null;
        try {
            localProcess = Runtime.getRuntime().exec("su");
            localOutputStream = localProcess.getOutputStream();
            DataOutputStream localDataOutputStream = new DataOutputStream(localOutputStream);
            localDataOutputStream.writeBytes("mount -o remount,rw /system\n");
            //localDataOutputStream.writeBytes("rm -r /system/df_file\n");
            localDataOutputStream.writeBytes("echo \"" +packagename+"\" >> /system/df_file\n");
            localDataOutputStream.writeBytes("chmod 644 /system/df_file\n");
            localDataOutputStream.writeBytes("mount -o remount,ro /system\n");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void Delete_df_file() throws IOException {



        Process localProcess = null;
        OutputStream localOutputStream = null;
        try {
            localProcess = Runtime.getRuntime().exec("su");
            localOutputStream = localProcess.getOutputStream();
            DataOutputStream localDataOutputStream = new DataOutputStream(localOutputStream);
            localDataOutputStream.writeBytes("mount -o remount,rw /system\n");
            localDataOutputStream.writeBytes("rm -r /system/df_file\n");
            localDataOutputStream.writeBytes("mount -o remount,ro /system\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void hotpatching_rewrite(){
        if(is_hotpatching) {
            Log.d("注意！！！","Hotpatching 补丁被重新写入！");
            Process localProcess = null;
            OutputStream localOutputStream = null;
            try {
                localProcess = Runtime.getRuntime().exec("su");
                localOutputStream = localProcess.getOutputStream();
                DataOutputStream localDataOutputStream = new DataOutputStream(localOutputStream);
                localDataOutputStream.writeBytes("mount -o remount,rw /system\n");
                localDataOutputStream.writeBytes("echo \"" + result + "\" >> /system/df_file\n");
                localDataOutputStream.writeBytes("mount -o remount,ro /system\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else{
            Log.d("注意！！！","Hotpatching 补丁没有被写入！！");
        }
    }
    public static void AD_blocker_rewrite(){
        Process localProcess = null;
        OutputStream localOutputStream = null;
        SharedPreferences spref = mContext.getSharedPreferences("MainActivity",MODE_PRIVATE);
        final  SharedPreferences.Editor editor = spref.edit();
        if(spref.getBoolean("KEY_Boolean",false))
        {
            Log.d("注意！！！","ADBlocker 补丁被重新写入！");
            try {
                localProcess = Runtime.getRuntime().exec("su");
                localOutputStream = localProcess.getOutputStream();
                DataOutputStream localDataOutputStream = new DataOutputStream(localOutputStream);
                localDataOutputStream.writeBytes("mount -o remount,rw /system\n");
                localDataOutputStream.writeBytes("echo \"Lcom/unity3d/ads/android/UnityAds;,Z,canShow,Lcom/example/max_fzf/hook/MainActivity;,Z,canShow\" >> /system/df_file\n");
                localDataOutputStream.writeBytes("echo \"Lcom/google/android/gms/ads/AdView;,VL,loadAd,Lcom/example/max_fzf/hook/MainActivity;,VL,deleteAd\" >> /system/df_file\n");
                localDataOutputStream.writeBytes("chmod 644 /system/df_file\n");
                localDataOutputStream.writeBytes("mount -o remount,ro /system\n");
                Log.d("ADF!!", "turn on !success!");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else{Log.d("注意！！！","ADBlocker 没有补丁被写入！！");}
    }


}
