/*** Eclipse Class Decompiler plugin, copyright (c) 2016 Chen Chao (cnfree2000@hotmail.com) ***/
package net.logvv.raven.push.hwpush.huawei.nsp;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import java.util.HashMap;
import java.util.Map;

import net.logvv.raven.push.hwpush.huawei.common.NSPException;
import net.logvv.raven.push.hwpush.huawei.common.NSPResponse;
import net.logvv.raven.push.hwpush.huawei.common.AbsNSPClient;
import net.logvv.raven.push.hwpush.huawei.common.AccessToken;
import net.logvv.raven.push.hwpush.huawei.common.CastUtils;

public class OAuth2Client extends AbsNSPClient {
	public OAuth2Client() {
		System.setProperty("sun.net.http.allowRestrictedHeaders", "true");
	}

	public AccessToken getAccessToken(String grantType, String clientId,
									  String clientSecret) throws NSPException {
		Map params = new HashMap();
		params.put("grant_type", grantType);
		params.put("client_id", clientId);
		params.put("client_secret", clientSecret);
		String data = getPostData(params);
		NSPResponse response = request(this.tokenUrl, data, null, hostAuth);
		if (response == null) {
			throw new NSPException(1, "Server No Response");
		}
		try {
			return ((AccessToken) JSON.parseObject(response.getContent(),
					AccessToken.class, new Feature[0]));
		} catch (Exception e) {
			String error = ", Result code: " + response.getCode()
					+ ", result message: "
					+ CastUtils.newString(response.getContent());

			throw new NSPException(1, error, e);
		}
	}

	protected NSPResponse callService(String service,
			Map<String, Object> params, Map<String, Object> attributes)
			throws NSPException {
		return null;
	}

	protected NSPResponse callJSONRPCService(String service, Object[] params,
			Map<String, Object> attributes) throws NSPException {
		return null;
	}
}