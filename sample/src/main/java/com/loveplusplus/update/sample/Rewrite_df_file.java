package com.loveplusplus.update.sample;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by max-fzf on 10.04.16.
 */
public class Rewrite_df_file {

	public void Hot_patching_rewrite(){
		if(MainActivity.is_hotpatching==true){

			MainActivity.hotpatching_rewrite();
		}
	}
	public void AD_blocker_rewrite(){
		if(MainActivity.is_ADblocker==true) {
			MainActivity.AD_blocker_rewrite();
		}
	}
}
