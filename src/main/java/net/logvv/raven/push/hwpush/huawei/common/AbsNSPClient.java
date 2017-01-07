/*** Eclipse Class Decompiler plugin, copyright (c) 2016 Chen Chao (cnfree2000@hotmail.com) ***/
package net.logvv.raven.push.hwpush.huawei.common;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

public abstract class AbsNSPClient {
    protected static final String ACCESS_TOKEN = "access_token";
    protected static final String GRANT_TYPE = "grant_type";
    protected static final String CLIENT_ID = "client_id";
    protected static final String CLIENT_SECRET = "client_secret";
    protected static final String NSP_SVC = "nsp_svc";
    protected static final String NSP_TS = "nsp_ts";
    protected static final String NSP_STATUS = "NSP_STATUS";
    protected static final String NSP_PARAMS = "nsp_params";
    protected static final String NSP_FMT = "nsp_fmt";
    protected static final String NSP_CTX = "nsp_ctx";
    protected static final String NSP_SG = "nsp_sg";
    protected static String hostApi = "api.vmall.com";

    protected static String hostAuth = "login.vmall.com";
    protected static final String CONNECT_TIMEOUT = "connectTimeout";
    protected static final String READ_TIMEOUT = "readTimeout";
    private static ThreadLocal<Map<String, Integer>> times = new ThreadLocal();
    protected String apiUrl;
    protected String tokenUrl;
    protected HttpConnectionAdaptor connectionAdaptor;
    protected HttpContext context;

    public AbsNSPClient() {
        this.apiUrl = "https://api.vmall.com/rest.php";

        this.tokenUrl = "https://login.vmall.com/oauth2/token";

        this.connectionAdaptor = new HttpConnectionAdaptor();

        this.context = new BasicHttpContext();
    }

    public boolean initHttpConnections(int connectionsPerRoute,
                                       int maxConnections) {
        return this.connectionAdaptor.initHttpConnections(connectionsPerRoute,
                maxConnections);
    }

    public void initKeyStoreStream(InputStream keyStoreStream, String password)
            throws NSPException {
        this.connectionAdaptor.initKeyStoreStream(keyStoreStream, password);
    }

    protected String getPostData(Map<String, String> params) {
        if ((params == null) || (params.size() == 0)) {
            return "";
        }
        StringBuffer data = new StringBuffer();
        for (Map.Entry entry : params.entrySet()) {
            String k = (String) entry.getKey();
            String v = (String) entry.getValue();
            try {
                data.append(URLEncoder.encode(k, "UTF-8")).append('=')
                        .append(URLEncoder.encode(v, "UTF-8")).append('&');
            } catch (UnsupportedEncodingException e) {
                e.getCause();
            }
        }
        return data.toString();
    }

    protected NSPResponse request(String httpurl, String data,
                                  Map<String, String> headers, String host) throws NSPException {
        int connectTimeout = 0;
        int readTimeout = 0;

        Map timesMap = getTimeout();

        if (null != timesMap) {
            connectTimeout = ((Integer) timesMap.get("connectTimeout"))
                    .intValue();
            readTimeout = ((Integer) timesMap.get("readTimeout")).intValue();
        }

        return request(httpurl, data, headers, host, connectTimeout,
                readTimeout);
    }

    private NSPResponse request(String httpurl, String data,
                                Map<String, String> headers, String host, int connectTimeout,
                                int readTimeout) throws NSPException {
        CloseableHttpClient httpClient = null;

        if ((null != httpurl) && (httpurl.toLowerCase().startsWith("https"))
                && (null == this.connectionAdaptor.getTrustStore())) {
            throw new NSPException(1,
                    "Must load keystore with \"initKeyStoreStream\" method");
        }

        if (null == data) {
            throw new NSPException(1, "Content of request is empty.");
        }
        try {
            httpClient = this.connectionAdaptor.getHttpClient();
        } catch (Exception e) {
            throw new NSPException(2, "Service unavailable.", e);
        }

        if (null == httpClient) {
            throw new NSPException(2, "Service unavailable.");
        }

        HttpPost httpPost = HttpConnectionAdaptor.getHttpPost(httpurl,
                connectTimeout, readTimeout, false);

        if (null == httpPost) {
            throw new NSPException(2, "Service unavailable.");
        }

        if (headers != null) {
            for (Map.Entry entry : headers.entrySet()) {
                httpPost.addHeader((String) entry.getKey(),
                        (String) entry.getValue());
            }
        }
        if (host != null) {
            httpPost.setHeader("Host", host);
        }

        CloseableHttpResponse httpResponse = null;
        NSPResponse response = new NSPResponse();
        try {
            httpPost.setEntity(new StringEntity(data, "UTF-8"));
            httpPost.addHeader("Content-Type","application/x-www-form-urlencoded");
            httpPost.addHeader("Accept-Encoding", "gzip");

            httpResponse = httpClient.execute(httpPost, this.context);

            Header[] rspHeaders = httpResponse.getAllHeaders();
            if (null != rspHeaders) {
                for (Header header : rspHeaders) {
                    response.putHeader(header.getName(), header.getValue());
                }
            }

            Header status = httpResponse.getFirstHeader("NSP_STATUS");
            if (null != status) {
                response.setCode(Integer.parseInt(status.getValue()));
            }
            response.setStatus(httpResponse.getStatusLine().getStatusCode());

            if ((response.getStatus() == 200) || (response.getStatus() == 302)) {
                response.setContent(EntityUtils.toByteArray(httpResponse
                        .getEntity()));
            }

        } catch (Exception ioe) {
        } finally {
            try {
                if (null != httpResponse) {
                    httpResponse.close();
                }
            } catch (Exception e) {
                e.getCause();
            }

            try {
                if ((null != httpPost) && (!(httpPost.isAborted()))) {
                    httpPost.abort();
                }
            } catch (Exception e2) {
                e2.getCause();
            }
        }

        if (response.getCode() > 0) {
            NSPError err = null;
            try {
                err = (NSPError) JSON.parseObject(response.getContent(),
                        NSPError.class, new Feature[0]);
            } catch (Exception e) {
                e.getCause();
            }
            if ((err == null) || (err.getError() == null)
                    || (err.getError().trim().length() == 0)) {
                String errInfo = Utils.newString(response.getContent());
                throw new NSPException(response.getCode(), errInfo);
            }

            throw new NSPException(response.getCode(), err.getError());
        }

        return response;
    }

    public <T> T call(String service, Map<String, Object> params,
                      Type returnType) throws NSPException {
        NSPResponse response = callService(service, params, null);
        T result = null;
        if (null == response.getContent()) {
            throw new NSPException(response.getStatus(), "Request failed.");
        }
        try {
            result = JSON.parseObject(response.getContent(), returnType,
                    new Feature[0]);
        } catch (Exception e) {
            throw new NSPException(response.getStatus(), "Parse failed.", e);
        }
        return result;
    }

    public <T> T call(String service, Map<String, Object> params,
                      Type returnType, Map<String, Object> attributes)
            throws NSPException {
        NSPResponse response = callService(service, params, attributes);
        T result = null;
        if (null == response.getContent()) {
            throw new NSPException(response.getStatus(), "Request failed.");
        }

        try {
            result = JSON.parseObject(response.getContent(), returnType,
                    new Feature[0]);
        } catch (Exception e) {
            throw new NSPException(response.getStatus(), "Parse failed.", e);
        }
        return result;
    }

    public <T> T call(String service, Object[] params, Type returnType,
                      Map<String, Object> attributes, Object observe) throws NSPException {
        NSPResponse response = callJSONRPCService(service, params, attributes);
        T result = null;
        if (null == response.getContent()) {
            throw new NSPException(response.getStatus(), "Request failed.");
        }

        try {
            result = JSON.parseObject(response.getContent(), returnType,
                    new Feature[0]);
        } catch (Exception e) {
            throw new NSPException(response.getStatus(), "Parse failed.", e);
        }
        return result;
    }

    protected abstract NSPResponse callService(String paramString,
                                               Map<String, Object> paramMap1, Map<String, Object> paramMap2)
            throws NSPException;

    protected abstract NSPResponse callJSONRPCService(String paramString,
                                                      Object[] paramArrayOfObject, Map<String, Object> paramMap)
            throws NSPException;

    public void setApiUrl(String url) {
        this.apiUrl = url;
        hostApi = Utils.getHost(url);
    }

    public void setTokenUrl(String tokenUrl) {
        this.tokenUrl = tokenUrl;
        hostAuth = Utils.getHost(tokenUrl);
    }

    public void setTimeout(int connectTimeout, int readTimeout) {
        Object obj = times.get();
        if (null == obj) {
            times.set(new HashMap(connectTimeout, readTimeout) {
            });
            return;
        }

        Map timesMap = (Map) obj;
        timesMap.put("connectTimeout", Integer.valueOf(connectTimeout));
        timesMap.put("readTimeout", Integer.valueOf(readTimeout));
    }

    public Map<String, Integer> getTimeout() {
        Object obj = times.get();
        if (null != obj) {
            return ((Map) obj);
        }

        return null;
    }
}