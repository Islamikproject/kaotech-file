package com.alesapps.islamik.model;

import com.parse.ParseFile;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class FileModel {

	public static ParseFile createParseFile(String filePath) {
		ParseFile parseFile = null;
		File audioFile = new File(filePath);
		if (audioFile != null) {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			BufferedInputStream in = null;
			try {
				in = new BufferedInputStream(new FileInputStream(audioFile));
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			int read;
			byte[] buff = new byte[1024];
			try {
				while ((read = in.read(buff)) > 0) {
					out.write(buff, 0, read);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				out.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			byte[] audioBytes = out.toByteArray();
			parseFile = new ParseFile(audioFile.getName() , audioBytes);
		}
		return parseFile;
	}
}
