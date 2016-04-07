package com.loveplusplus.update.sample;

import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by max-fzf on 05.04.16.
 */
public class Unzip {
	private static final String TAG = "!!UNZIP!!";

	public static void unzip(String zipFileName, String outputDirectory)
			throws Exception
	{
		ZipInputStream in = new ZipInputStream(new FileInputStream(zipFileName));
		ZipEntry z;
		String name = "";
		String extractedFile = "";
		int counter = 0;

		while ((z = in.getNextEntry()) != null)
		{
			name = z.getName();
			Log.d(TAG, "unzipping file: " + name);
			if (z.isDirectory())
			{
				Log.d(TAG, name + "is a folder");
				// get the folder name of the widget
				name = name.substring(0, name.length() - 1);
				File folder = new File(outputDirectory + File.separator + name);
				folder.mkdirs();
				if (counter == 0) {
					extractedFile = folder.toString();
				}
				counter++;
				Log.d(TAG, "mkdir " + outputDirectory + File.separator + name);
			}
			else
			{
				Log.d(TAG, name + "is a normal file");
				File file = new File(outputDirectory + File.separator + name);
				file.createNewFile();
				// get the output stream of the file
				FileOutputStream out = new FileOutputStream(file);
				int ch;
				byte[] buffer = new byte[1024];
				// read (ch) bytes into buffer
				while ((ch = in.read(buffer)) != -1) {
					// write (ch) byte from buffer at the position 0
					out.write(buffer, 0, ch);
					out.flush();
				}
				out.close();
			}
		}
	}
}
