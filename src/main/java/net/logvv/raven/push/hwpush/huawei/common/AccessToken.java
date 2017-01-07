/*** Eclipse Class Decompiler plugin, copyright (c) 2016 Chen Chao (cnfree2000@hotmail.com) ***/
package net.logvv.raven.push.hwpush.huawei.common;

import java.util.Map;

public class AccessToken {
	private String accessToken;
	private int expiresIn;
	private String refreshToken;
	private Map<String, Object> attrs;

	public String getAccess_token() {
		return this.accessToken;
	}

	public void setAccess_token(String at) {
		this.accessToken = at;
	}

	public int getExpires_in() {
		return this.expiresIn;
	}

	public void setExpires_in(int expiresInner) {
		this.expiresIn = expiresInner;
	}

	public String getRefresh_token() {
		return this.refreshToken;
	}

	public void setRefresh_token(String refreshTk) {
		this.refreshToken = refreshTk;
	}

	public Map<String, Object> getAttrs() {
		return this.attrs;
	}

	public void setAttrs(Map<String, Object> attrs) {
		this.attrs = attrs;
	}
}