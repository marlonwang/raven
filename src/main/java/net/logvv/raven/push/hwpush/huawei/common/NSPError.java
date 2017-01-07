/*** Eclipse Class Decompiler plugin, copyright (c) 2016 Chen Chao (cnfree2000@hotmail.com) ***/
package net.logvv.raven.push.hwpush.huawei.common;

public class NSPError {
	private String error;

	public NSPError(String e) {
		this.error = e;
	}

	public NSPError() {
	}

	public String getError() {
		return this.error;
	}

	public void setError(String error) {
		this.error = error;
	}
}