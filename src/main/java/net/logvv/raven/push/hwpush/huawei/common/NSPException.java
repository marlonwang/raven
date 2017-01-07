/*** Eclipse Class Decompiler plugin, copyright (c) 2016 Chen Chao (cnfree2000@hotmail.com) ***/
package net.logvv.raven.push.hwpush.huawei.common;

public class NSPException extends Exception {
	public static final int EXP_UNKNOWN_ERROR = 1;
	public static final int EXP_SERVICE_UNAVAILABLE = 2;
	public static final int EXP_PHP_SERIALIZE_ERROR = 1;
	public static final int EXP_PHP_UNSERIALIZE_ERROR = 1;
	public static final int EXP_JSON_SERIALIZE_ERROR = 1;
	public static final int EXP_JSON_UNSERIALIZE_ERROR = 1;
	public static final int ACCESSTOKEN_SESSION_TIMEOUT = 6;
	private int code;

	public NSPException(int code, String message) {
		super(code + " " + message);
		this.code = code;
	}

	public NSPException(int code, String message, Exception ex) {
		super(code + " " + message, ex);
		this.code = code;
	}

	public int getCode() {
		return this.code;
	}
}