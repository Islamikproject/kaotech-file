package com.alesapps.islamik.utils;

import android.os.Environment;
import java.io.File;
import java.io.IOException;

public class ResourceUtil {
	public static String RES_DIRECTORY = Environment.getExternalStorageDirectory() + "/alesapps/ISLAMIK/";
	public static String getVideoFilePath(String fileName) {
		String tempDirPath = RES_DIRECTORY;
		String tempFileName = fileName;

		File tempDir = new File(tempDirPath);
		if (!tempDir.exists())
			tempDir.mkdirs();
		File tempFile = new File(tempDirPath + tempFileName);
		if (!tempFile.exists())
			try {
				tempFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		return tempDirPath + tempFileName;
	}
}
