/*** Eclipse Class Decompiler plugin, copyright (c) 2016 Chen Chao (cnfree2000@hotmail.com) ***/
package net.logvv.raven.push.hwpush.huawei.nsp;

import com.alibaba.fastjson.JSON;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import net.logvv.raven.push.hwpush.huawei.common.NSPResponse;
import net.logvv.raven.push.hwpush.huawei.common.AbsNSPClient;
import net.logvv.raven.push.hwpush.huawei.common.NSPException;
import net.logvv.raven.push.hwpush.huawei.common.Utils;

public class NSPClient extends AbsNSPClient {
	public static final String JSON_RPC = "json-rpc";
	public static final String FMT_JSON = "JSON";
	private String accessToken = "";

	public NSPClient(String accessToken) {
		System.setProperty("sun.net.http.allowRestrictedHeaders", "true");
		this.accessToken = accessToken;
	}

	public Object getInterfaceInstance(String service, Class interfaceClass)
			throws NSPException {
		return getInterfaceInstance(service, interfaceClass, "json-rpc", null);
	}

	public Object getInterfaceInstance(String service, Class interfaceClass,
			String formatType, Map<String, Object> attributes)
			throws NSPException {
		String format = formatType;
		if ((null == service) || ("".equals(service))) {
			return null;
		}

		Object f = null;

		if ((null == format) || ("".equals(format))) {
			format = "json-rpc";
		}

		HWInvokeMethodProxy handler = new HWInvokeMethodProxy(this, service,
				formatType, attributes);
		try {
			Class proxyClass = Proxy.getProxyClass(
					interfaceClass.getClassLoader(),
					new Class[] { interfaceClass });

			f = proxyClass.getConstructor(
					new Class[] { InvocationHandler.class }).newInstance(
					new Object[] { handler });
		} catch (Exception e) {
			throw new NSPException(1, "", e);
		}

		return f;
	}

	protected NSPResponse callService(String service,
									  Map<String, Object> params, Map<String, Object> attributes)
			throws NSPException {
		Map ps = new HashMap();

		if (Utils.isEmptyString(this.accessToken)) {
			throw new NSPException(1, "Access_token is empty.");
		}
		ps.put("access_token", this.accessToken);

		if (Utils.isEmptyString(service)) {
			throw new NSPException(1, "Service name is empty.");
		}

		ps.put("nsp_svc", service);
		try {
			if ((null != attributes) && (0 != attributes.size())) {
				ps.put("nsp_ctx", JSON.toJSONString(attributes));
			}
		} catch (Exception e) {
			throw new NSPException(1, "Parameter json-serialize failed."
					+ e.getMessage());
		}

		try {
			for (Map.Entry entry : params.entrySet()) {
				String k = (String) entry.getKey();
				Object v = entry.getValue();
				if (v instanceof String) {
					ps.put(k, (String) v);
				} else {
					ps.put(k, JSON.toJSONString(v));
				}
			}
		} catch (Exception e) {
			throw new NSPException(1, "Parameter json-serialize failed.", e);
		}
		ps.put("nsp_fmt", "JSON");
		ps.put("nsp_ts",
				String.valueOf(TimeUnit.SECONDS.convert(
						System.currentTimeMillis(), TimeUnit.MILLISECONDS)));
		String data = getPostData(ps);
		NSPResponse response = request(this.apiUrl, data, null, hostApi);
		if (response == null) {
			throw new NSPException(1, "Server No Response");
		}
		return response;
	}

	@Deprecated
	public NSPResponse call(String service, Map<String, String> params)
			throws NSPException {
		params.put("access_token", this.accessToken);
		params.put("nsp_svc", service);
		params.put("nsp_fmt", "JSON");

		params.put(
				"nsp_ts",
				String.valueOf(TimeUnit.SECONDS.convert(
						System.currentTimeMillis(), TimeUnit.MILLISECONDS)));
		String data = getPostData(params);
		NSPResponse response = request(this.apiUrl, data, null, hostApi);
		if (response == null) {
			throw new NSPException(1, "Server No Response");
		}
		return response;
	}

	protected NSPResponse callJSONRPCService(String service, Object[] params,
			Map<String, Object> attributes) throws NSPException {
		Map ps = new HashMap();

		if (Utils.isEmptyString(this.accessToken)) {
			throw new NSPException(1, "Access_token is empty.");
		}
		ps.put("access_token", this.accessToken);

		if (Utils.isEmptyString(service)) {
			throw new NSPException(1, "Service name is empty.");
		}

		ps.put("nsp_svc", service);
		try {
			if ((null != attributes) && (0 != attributes.size())) {
				ps.put("nsp_ctx", JSON.toJSONString(attributes));
			}
		} catch (Exception e) {
			throw new NSPException(1, "Parameter json-serialize failed.", e);
		}

		try {
			ps.put("nsp_params", JSON.toJSONString(params));
		} catch (Exception e) {
			throw new NSPException(1, "Parameter json-serialize failed.", e);
		}
		ps.put("nsp_fmt", "json-rpc");

		ps.put("nsp_ts",
				String.valueOf(TimeUnit.SECONDS.convert(
						System.currentTimeMillis(), TimeUnit.MILLISECONDS)));
		String data = getPostData(ps);
		NSPResponse response = request(this.apiUrl, data, null, hostApi);
		if (response == null) {
			throw new NSPException(1, "Server No Response");
		}
		return response;
	}

	private static class HWInvokeMethodProxy implements InvocationHandler {
		private String serviceName = null;

		private String format = null;

		private NSPClient client = null;

		private Map<String, Object> attributes = null;

		public HWInvokeMethodProxy(NSPClient client, String serviceName,
				String fmt, Map<String, Object> attributes) {
			this.client = client;
			this.serviceName = serviceName;
			this.format = fmt;
			this.attributes = attributes;
		}

        public Object invoke(Object proxy, Method method, Object[] args)
                throws NSPException {
            return client.call(this.serviceName + "." + method.getName(),
                    args, method.getGenericReturnType(), this.attributes, null);
        }
	}
}