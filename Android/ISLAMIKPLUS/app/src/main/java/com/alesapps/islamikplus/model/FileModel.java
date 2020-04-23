package com.alesapps.islamikplus.model;

import android.webkit.MimeTypeMap;
import com.parse.ParseFile;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;

public class FileModel {
	public static ParseFile createVideoParseFile(String fileName, String filePath) {
		ParseFile parseFile = null;

		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			FileInputStream fis = new FileInputStream(new File(filePath));

			byte[] buf = new byte[1024];
			int n;
			while (-1 != (n = fis.read(buf)))
				baos.write(buf, 0, n);

			byte[] videoBytes = baos.toByteArray();
			parseFile = new ParseFile(fileName, videoBytes, getMimeType(filePath));

		} catch (Exception e) {
			e.printStackTrace();
		}
		return parseFile;
	}

	public static String getMimeType(String url) {
		String type = null;
		String extension = MimeTypeMap.getFileExtensionFromUrl(url);
		if (extension != null) {
			type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
		}
		return type;
	}
}
