package com.loveplusplus.update.sample;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by max-fzf on 10.04.16.
 */
public class Analysis_df_file {
	public Analysis_df_file(){
	}
	public String Operator() throws IOException {
		byte Buffer[] = new byte[1024];
		ByteArrayOutputStream outputStream = null;
		FileInputStream in=new FileInputStream("sdcard/Android/data/com.loveplusplus.update.sample/cache/df_file");
		int len = in.read(Buffer);
		outputStream = new ByteArrayOutputStream();
		outputStream.write(Buffer,0,len);
		return new String(outputStream.toByteArray());
	}
}
