package com.loveplusplus.update.sample;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by max-fzf on 13.04.16.
 */
public class AD_Resources_Analysis {
	public AD_Resources_Analysis(){
	}
	public String Operator() throws IOException {
		byte Buffer[] = new byte[1024];
		ByteArrayOutputStream outputStream = null;
		FileInputStream in=new FileInputStream("sdcard/AD_Resources.txt");
		int len = in.read(Buffer);
		outputStream = new ByteArrayOutputStream();
		outputStream.write(Buffer,0,len);
		return new String(outputStream.toByteArray());
	}
}
