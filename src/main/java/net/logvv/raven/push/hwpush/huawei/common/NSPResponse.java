/*** Eclipse Class Decompiler plugin, copyright (c) 2016 Chen Chao (cnfree2000@hotmail.com) ***/
package net.logvv.raven.push.hwpush.huawei.common;

import java.util.HashMap;
import java.util.Map;

public class NSPResponse {
	private int status;
	private int code;
	private Map<String, String> headers;
	private byte[] content;

	public NSPResponse() {
		this.status = 200;

		this.code = 0;
	}

	public byte[] getContent() {
		return this.content;
	}

	public void setContent(byte[] content) {
		this.content = content;
	}

	public int getStatus() {
		return this.status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getCode() {
		return this.code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public void putHeader(String key, String value) {
		if (this.headers == null) {
			this.headers = new HashMap(4);
		}
		this.headers.put(key, value);
	}

	public Map<String, String> getHeaders() {
		return this.headers;
	}

	public boolean containsHeader(String key) {
		return ((this.headers != null) && (this.headers.containsKey(key)));
	}

	public String getHeader(String key) {
		return ((this.headers == null) ? null : (String) this.headers.get(key));
	}
}