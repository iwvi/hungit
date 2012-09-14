package com.fonenet.fonemarket;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

public class FoneNetUntils {

	private static int BUFF_SIZE = 8 * 1024;

	public static void upZipFile(File zFile, String folderPath)
			throws ZipException, IOException {
		File desDir = new File(folderPath);
		if (!desDir.exists()) {
			desDir.mkdirs();
		}
		ZipFile zf = new ZipFile(zFile);
		for (Enumeration<?> entries = zf.entries(); entries.hasMoreElements();) {
			ZipEntry entry = ((ZipEntry) entries.nextElement());
			InputStream in = zf.getInputStream(entry);
			String str = folderPath + File.separator + entry.getName();
			str = new String(str.getBytes("8859_1"), "GB2312");
			File desFile = new File(str);
			if (!desFile.exists()) {
				File fileParentDir = desFile.getParentFile();
				if (!fileParentDir.exists()) {
					fileParentDir.mkdirs();
				}
				desFile.createNewFile();
			}
			OutputStream out = new FileOutputStream(desFile);
			byte buffer[] = new byte[BUFF_SIZE];
			int realLength;
			while ((realLength = in.read(buffer)) > 0) {
				out.write(buffer, 0, realLength);
			}
			in.close();
			out.close();
		}
	}

	public static String getFileNameEx(String fileName) {
		int start = fileName.lastIndexOf("/");
		int end = fileName.lastIndexOf(".");
		if (start != -1 && end != -1) {
			return fileName.substring(start + 1, end);
		} else {
			return null;
		}
	}

	public static void installApk(String paramString, Context context) {

		File localFile = new File(paramString);
		Intent localIntent = new Intent();
		// localIntent.addFlags(268435456);
		localIntent.setAction("android.intent.action.VIEW");
		localIntent.setDataAndType(Uri.fromFile(localFile),
				"application/vnd.android.package-archive");
		context.startActivity(localIntent);
		Log.e("success", "the end");
	}

}
