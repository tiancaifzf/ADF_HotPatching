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
            UpdateChecker.checkForDialog(MainActivity.this, APP_UPDATE_SERVER_URL,MainActivity.this);
        Log.d("!!!!!!!!!!!", "44444444444");
            if(UpdateChecker.error()){
                Toast.makeText(mContext, "沒有網路連接！", Toast.LENGTH_SHORT).show();
            }
        SharedPreferences spref = getPreferences(MODE_PRIVATE);
        final SharedPreferences.Editor editor = spref.edit();
        Switch ss=(Switch)findViewById(R.id.switch1);
        final boolean boolvalue=spref.getBoolean("KEY_Boolean",false);
        ss.setChecked(boolvalue);
        Button rb= (Button) findViewById(R.id.button);
        Button jump= (Button) findViewById(R.id.button2);
        Button check= (Button) findViewById(R.id.button3);
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

                try {
                    localProcess = Runtime.getRuntime().exec("su");
                    localOutputStream = localProcess.getOutputStream();
                    DataOutputStream localDataOutputStream = new DataOutputStream(localOutputStream);
                    localDataOutputStream.writeBytes("mount -o remount,rw /system\n");
                    localDataOutputStream.writeBytes("mv sdcard/Android/data/com.loveplusplus.update.sample/cache/hook.apk /system/dynamic_framework/hook.apk\n");
                    localDataOutputStream.writeBytes("mv sdcard/Android/data/com.loveplusplus.update.sample/cache/df_file /system/df_file\n");
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
                        Log.d("@@@@@","11111");
                        localProcess = Runtime.getRuntime().exec("su");
                        Log.d("@@@@@","22222");
                        localOutputStream = localProcess.getOutputStream();
                        DataOutputStream localDataOutputStream = new DataOutputStream(localOutputStream);
                        String path = "\"Lcom/antutu/ABenchMark/ABenchMarkStart;,VL,onCreate,Lcom/example/max_fzf/hook/MainActivity;,VL,onCreate\"";
                        localDataOutputStream.writeBytes("mount -o remount,rw /system\n");
                        localDataOutputStream.writeBytes("rm -r /system/df_file\n");
                        localDataOutputStream.writeBytes("echo \"Lcom/unity3d/ads/android/UnityAds;,Z,canShow,Lcom/example/max_fzf/hook/MainActivity;,Z,canShow\" > /system/df_file\n");
                        localDataOutputStream.writeBytes("echo \"Lcom/google/android/gms/ads/AdView;,VL,loadAd,Lcom/example/max_fzf/hook/MainActivity;,VL,deleteAd\" >> /system/df_file\n");
                        localDataOutputStream.writeBytes("chmod 644 /system/df_file\n");
                        localDataOutputStream.writeBytes("mount -o remount,ro /system\n");
                        Log.d("@@@@@", "33333");
                        //isChecked=true;
                        editor.putBoolean("KEY_Boolean", true);
                        editor.apply();
                        editor.commit();
                        Log.d("ADF!!", "turn on !success!");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        localProcess = Runtime.getRuntime().exec("su");
                        localOutputStream = localProcess.getOutputStream();
                        DataOutputStream localDataOutputStream = new DataOutputStream(localOutputStream);
                        String path = "\"Lcom/antutu/ABenchMark/ABenchMarkStart;,VL,onCreate,Lcom/example/max_fzf/hook/MainActivity;,VL,onCreate\"";
                        localDataOutputStream.writeBytes("mount -o remount,rw /system\n");
                        localDataOutputStream.writeBytes("rm -r /system/df_file\n");
                        //isChecked=false;
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
            Log.d("33333333333333333","333333333333333333"+packagename);
            localProcess = Runtime.getRuntime().exec("su");
            localOutputStream = localProcess.getOutputStream();
            DataOutputStream localDataOutputStream = new DataOutputStream(localOutputStream);
            localDataOutputStream.writeBytes("mount -o remount,rw /system\n");
            localDataOutputStream.writeBytes("rm -r /system/df_file\n");
            localDataOutputStream.writeBytes("echo \"" +packagename+"\" >> /system/df_file\n");
            localDataOutputStream.writeBytes("chmod 644 /system/df_file\n");
            localDataOutputStream.writeBytes("mount -o remount,ro /system\n");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
