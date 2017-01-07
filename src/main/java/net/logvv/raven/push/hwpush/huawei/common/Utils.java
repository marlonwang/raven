/*** Eclipse Class Decompiler plugin, copyright (c) 2016 Chen Chao (cnfree2000@hotmail.com) ***/
package net.logvv.raven.push.hwpush.huawei.common;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;

public class Utils {
	private static final Charset CHARSET_UTF8 = Charset.forName("UTF-8");
	private static final int BYTE_LEN = 1024;

	public static String newString(byte[] bytes) {
		return new String(bytes, CHARSET_UTF8);
	}

	public static byte[] getBytes(String str) {
		if (str == null) {
			return null;
		}
		return str.getBytes(CHARSET_UTF8);
	}

	public static byte[] readStream(InputStream input) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		byte[] b = new byte[1024];
		int length = -1;
		while (-1 != (length = input.read(b, 0, 1024))) {
			out.write(b, 0, length);
		}
		return out.toByteArray();
	}

	public static String getHost(String url) {
		try {
			URL u = new URL(url);
			return u.getHost();
		} catch (MalformedURLException e) {
			e.getCause();

			if ((url == null) || (url.length() == 0)) {
				return "";
			}
		}
		int doubleslash = url.indexOf("//");
		if (doubleslash == -1) {
			doubleslash = 0;
		} else {
			doubleslash += 2;
		}

		int end = url.indexOf(58, doubleslash);
		if (end == -1) {
			end = url.indexOf(47, doubleslash);
			end = (end >= 0) ? end : url.length();
		}

		return url.substring(doubleslash, end);
	}

	public static boolean isEmptyString(String str) {
		return ((str == null) || (str.length() == 0));
	}
}