package net.logvv.raven.push.xiaomi.xmpush.server;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//
import org.apache.commons.codec.binary.Base64;

import javax.net.ssl.*;
import java.io.*;
import java.net.*;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HttpBase {
    protected static final String UTF8 = "UTF-8";
    protected static final int BACKOFF_INITIAL_DELAY = 1000;
    private static final String JDK_VERSION = System.getProperty("java.version", "UNKNOWN");
    private static final String OS = System.getProperty("os.name").toLowerCase();
    protected static final int MAX_BACKOFF_DELAY = 1024000;
    protected final Random random = new Random();
    protected final String security;
    protected final String token;
    protected long lastRequestCostTime = 0L;
    protected HttpBase.ERROR lastRequestError;
    protected String lastRequestUrl;
    protected String lastRequestHost;
    protected String remoteHost;
    protected String remoteIp;
    protected Exception lastException;
    protected static final Logger logger = Logger.getLogger(HttpBase.class.getName());
    private static final String LOCAL_HOST_NAME = getLocalHostName();
    private static String LOCAL_IP;
    private static boolean useProxy = false;
    private static boolean needAuth = false;
    private static String proxyHost;
    private static int proxyPort;
    private static String user;
    private static String password;
    private static final SSLHandler sslVerifier = new SSLHandler();

    public HttpBase(String security) {
        this.lastRequestError = HttpBase.ERROR.SUCCESS;
        this.lastRequestUrl = null;
        this.lastRequestHost = null;
        this.remoteHost = "";
        this.remoteIp = "";
        this.lastException = null;
        this.security = security;
        this.token = null;
    }

    public HttpBase(String security, String token) {
        this.lastRequestError = HttpBase.ERROR.SUCCESS;
        this.lastRequestUrl = null;
        this.lastRequestHost = null;
        this.remoteHost = "";
        this.remoteIp = "";
        this.lastException = null;
        this.security = security;
        this.token = token;
    }

    private static String getLocalHostName() {
        String host = null;

        try {
            host = InetAddress.getLocalHost().getHostName();
            LOCAL_IP = InetAddress.getLocalHost().getHostAddress();
        } catch (Exception var2) {
            ;
        }

        return host;
    }

    protected IOException exception(int attemptNum) {
        String msg = "Failed to send http request after " + attemptNum + " attempts: remote server " + this.remoteHost + "(" + this.remoteIp + ")";
        if(this.lastException != null) {
            msg = msg + "\nException " + this.lastException.getClass().getCanonicalName() + " : " + this.lastException.getLocalizedMessage() + "\n" + this.lastException.getCause();
            StackTraceElement[] traces = this.lastException.getStackTrace();
            if(traces != null) {
                StackTraceElement[] var4 = traces;
                int var5 = traces.length;

                for(int var6 = 0; var6 < var5; ++var6) {
                    StackTraceElement trace = var4[var6];
                    msg = msg + "\n" + "  " + trace.getClassName() + trace.getMethodName() + " (" + trace.getFileName() + ":" + trace.getLineNumber() + ")";
                }
            }
        }

        this.lastException = null;
        return new IOException(msg);
    }

    private HttpURLConnection httpRequest(HttpBase.HttpAction action, Constants.RequestPath requestPath) throws IOException {
        ServerSwitch.Server server = ServerSwitch.getInstance().selectServer(requestPath);
        long start = System.currentTimeMillis();
        boolean succ = false;

        HttpURLConnection var8;
        try {
            HttpURLConnection e = action.action(server);
            succ = true;
            this.lastRequestError = HttpBase.ERROR.SUCCESS;
            Constants.autoSwitchHost = e.getHeaderField("X-PUSH-DISABLE-AUTO-SELECT-DOMAIN") == null;
            Constants.accessTimeOut = e.getHeaderFieldInt("X-PUSH-CLIENT-TIMEOUT-MS", 5000);
            var8 = e;
        } catch (SocketTimeoutException var13) {
            this.lastRequestError = HttpBase.ERROR.SocketTimeoutException;
            throw var13;
        } catch (IOException var14) {
            this.lastRequestError = HttpBase.ERROR.IOException;
            throw var14;
        } finally {
            this.lastRequestCostTime = System.currentTimeMillis() - start;
            if(this.lastRequestCostTime > (long) Constants.accessTimeOut) {
                this.lastRequestError = HttpBase.ERROR.CostTooMuchTime;
                server.decrPriority();
            } else if(succ) {
                server.incrPriority();
            } else {
                server.decrPriority();
            }

            this.lastRequestUrl = requestPath.getPath();
            this.lastRequestHost = server.getHost();
        }

        return var8;
    }

    protected HttpURLConnection doPost(final Constants.RequestPath requestPath, final String body) throws IOException {
        return this.httpRequest(new HttpBase.HttpAction() {
            public HttpURLConnection action(ServerSwitch.Server server) throws IOException {
                return HttpBase.this.doPost(server, requestPath, "application/x-www-form-urlencoded;charset=UTF-8", body);
            }
        }, requestPath);
    }

    protected HttpURLConnection doGet(final Constants.RequestPath requestPath, final String parameter) throws IOException {
        return this.httpRequest(new HttpBase.HttpAction() {
            public HttpURLConnection action(ServerSwitch.Server server) throws IOException {
                return HttpBase.this.doGet(server, requestPath, "application/x-www-form-urlencoded;charset=UTF-8", parameter);
            }
        }, requestPath);
    }

    protected HttpURLConnection doPost(ServerSwitch.Server server, Constants.RequestPath requestPath, String contentType, String body) throws IOException {
        if(requestPath != null && body != null) {
            logger.fine("Sending post to " + server.getHost() + " " + requestPath.getPath());
            logger.finest("post body: " + body);
            HttpURLConnection conn = this.getConnection(server, requestPath);
            this.prepareConnection(conn);
            byte[] bytes = body.getBytes();
            conn.setConnectTimeout(20000);
            conn.setReadTimeout(20000);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setFixedLengthStreamingMode(bytes.length);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", contentType);
            conn.setRequestProperty("Authorization", "key=" + this.security);
            if(this.token != null) {
                conn.setRequestProperty("X-PUSH-AUDIT-TOKEN", this.token);
            }

            OutputStream out = conn.getOutputStream();

            try {
                out.write(bytes);
            } finally {
                close(out);
            }

            return conn;
        } else {
            throw new IllegalArgumentException("arguments cannot be null");
        }
    }

    protected HttpURLConnection doGet(ServerSwitch.Server server, Constants.RequestPath requestPath, String contentType, String parameter) throws IOException {
        if(requestPath != null && parameter != null) {
            logger.fine("Sending get to " + server.getHost() + " " + requestPath.getPath());
            logger.finest("get parameter: " + parameter);
            HttpURLConnection conn = this.getConnection(server, requestPath, parameter);
            this.prepareConnection(conn);
            conn.setConnectTimeout(20000);
            conn.setReadTimeout(20000);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", contentType);
            conn.setRequestProperty("Authorization", "key=" + this.security);
            conn.getInputStream();
            return conn;
        } else {
            throw new IllegalArgumentException("arguments cannot be null");
        }
    }

    protected void prepareConnection(HttpURLConnection conn) {
        conn.setRequestProperty("X-PUSH-SDK-VERSION", "2016.08.15");
        conn.setRequestProperty("X-PUSH-JDK-VERSION", JDK_VERSION);
        conn.setRequestProperty("X-PUSH-OS", OS);
        conn.setRequestProperty("X-PUSH-REMOTEIP", this.remoteIp);
        if(LOCAL_HOST_NAME != null) {
            conn.setRequestProperty("X-PUSH-CLIENT-HOST", LOCAL_HOST_NAME);
        }

        if(LOCAL_IP != null) {
            conn.setRequestProperty("X-PUSH-CLIENT-IP", LOCAL_IP);
        }

        if(Constants.INCLUDE_LAST_METRICS) {
            if(this.lastRequestCostTime > 0L) {
                conn.setRequestProperty("X-PUSH-LAST-REQUEST-DURATION", this.lastRequestCostTime + "");
                this.lastRequestCostTime = 0L;
            }

            if(this.lastRequestUrl != null) {
                conn.setRequestProperty("X-PUSH-LAST-REQUEST-URL", this.lastRequestUrl);
                this.lastRequestUrl = null;
            }

            if(this.lastRequestHost != null) {
                conn.setRequestProperty("X-PUSH-LAST-REQUEST-HOST", this.lastRequestHost);
                this.lastRequestHost = null;
            }

            conn.setRequestProperty("X-PUSH-LAST-ERROR", this.lastRequestError.name());
            this.lastRequestError = HttpBase.ERROR.SUCCESS;
        }

    }

    protected static StringBuilder newBody(String name, String value) {
        return (new StringBuilder((String)nonNull(name))).append('=').append((String)nonNull(value));
    }

    private static void close(Closeable closeable) {
        if(closeable != null) {
            try {
                closeable.close();
            } catch (IOException var2) {
                logger.log(Level.FINEST, "IOException closing stream", var2);
            }
        }

    }

    protected static StringBuilder newBodyWithArrayParameters(String name, List<String> parameters) throws UnsupportedEncodingException {
        StringBuilder sb = new StringBuilder();

        for(int i = 0; i < parameters.size(); ++i) {
            if(i == 0) {
                sb.append((String)nonNull(name)).append("=").append(URLEncoder.encode((String)nonNull(parameters.get(i)), "UTF-8"));
            } else {
                ((StringBuilder)nonNull(sb)).append('&').append((String)nonNull(name)).append('=').append(URLEncoder.encode((String)nonNull(parameters.get(i)), "UTF-8"));
            }
        }

        if(parameters.size() == 0) {
            sb.append(name).append("=").append("");
        }

        return sb;
    }

    protected static void addParameter(StringBuilder body, String name, String value) {
        ((StringBuilder)nonNull(body)).append('&').append((String)nonNull(name)).append('=').append((String)nonNull(value));
    }

    protected HttpURLConnection getConnection(ServerSwitch.Server server, Constants.RequestPath requestPath) throws IOException {
        return this.getConnection(server, requestPath, (String)null);
    }

    protected HttpURLConnection getConnection(Constants.RequestPath requestPath) throws IOException {
        return this.getConnection((ServerSwitch.Server)null, requestPath, (String)null);
    }

    protected HttpURLConnection getConnection(ServerSwitch.Server server, Constants.RequestPath requestPath, String parameter) throws IOException {
        if(server == null) {
            server = ServerSwitch.getInstance().selectServer(requestPath);
        }

        String urlSpec = ServerSwitch.buildFullRequestURL(server, requestPath);
        if(parameter != null) {
            urlSpec = urlSpec + "?" + parameter;
        }

        URL url = new URL(urlSpec);
        if(useProxy) {
            return this.setProxy(url);
        } else {
            this.remoteHost = "";
            this.remoteIp = "";

            try {
                this.remoteHost = url.getHost();
                InetAddress conn = InetAddress.getByName(this.remoteHost);
                this.remoteIp = conn.getHostAddress();
            } catch (Exception var7) {
                this.lastException = var7;
                logger.log(Level.WARNING, "Get remote ip failed for " + this.remoteHost, var7);
            }

            if(Constants.USE_HTTPS) {
                HttpsURLConnection conn1 = (HttpsURLConnection)url.openConnection();
                conn1.setHostnameVerifier(new HostnameVerifier() {
                    public boolean verify(String s, SSLSession sslSession) {
                        return true;
                    }
                });
                return conn1;
            } else {
                return (HttpURLConnection)url.openConnection();
            }
        }
    }

    private HttpsURLConnection setProxy(URL url) throws IOException {
        System.setProperty("sun.security.ssl.allowUnsafeRenegotiation", "true");

        try {
            SSLContext proxy = SSLContext.getInstance("SSL");
            proxy.init((KeyManager[])null, new TrustManager[]{sslVerifier}, (SecureRandom)null);
            HttpsURLConnection.setDefaultSSLSocketFactory(proxy.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier(sslVerifier);
        } catch (Exception var5) {
            logger.fine("https config ssl failure: " + var5);
            this.lastException = var5;
        }

        Proxy proxy1 = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHost, proxyPort));
        HttpsURLConnection httpsURLConnection = (HttpsURLConnection)url.openConnection(proxy1);
        if(needAuth) {
            String encoded = new String(Base64.encodeBase64((user + ":" + password).getBytes()));
            httpsURLConnection.setRequestProperty("Proxy-Authorization", "Basic " + encoded);
        }

        return httpsURLConnection;
    }

    protected static String getString(InputStream stream) throws IOException {
        if(stream == null) {
            return "";
        } else {
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            StringBuilder content = new StringBuilder();

            String newLine;
            do {
                newLine = reader.readLine();
                if(newLine != null) {
                    content.append(newLine).append('\n');
                }
            } while(newLine != null);

            if(content.length() > 0) {
                content.setLength(content.length() - 1);
            }

            return content.toString();
        }
    }

    protected static String getAndClose(InputStream stream) throws IOException {
        String var1;
        try {
            var1 = getString(stream);
        } finally {
            if(stream != null) {
                close(stream);
            }

        }

        return var1;
    }

    static <T> T nonNull(T argument) {
        if(argument == null) {
            throw new IllegalArgumentException("argument cannot be null");
        } else {
            return argument;
        }
    }

    void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException var4) {
            this.lastException = var4;
            Thread.currentThread().interrupt();
        }

    }

    public static void setProxy(String host, int port) {
        setProxy(host, port, (String)null, (String)null);
    }

    public static void setProxy(String host, int port, String authUser, String authPassword) {
        if(!XMStringUtils.isBlank(host) && port > 0) {
            useProxy = true;
            needAuth = !XMStringUtils.isBlank(authUser) && !XMStringUtils.isBlank(authPassword);
            proxyHost = host;
            proxyPort = port;
            user = authUser;
            password = authPassword;
        } else {
            throw new IllegalArgumentException("proxy host or port invalid.");
        }
    }

    public static void unsetProxy() {
        useProxy = false;
        needAuth = false;
    }

    private static class SSLHandler implements X509TrustManager, HostnameVerifier {
        private SSLHandler() {
        }

        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        public boolean verify(String paramString, SSLSession paramSSLSession) {
            return true;
        }
    }

    interface HttpAction {
        HttpURLConnection action(ServerSwitch.Server var1) throws IOException;
    }

    static enum ERROR {
        SUCCESS,
        SocketTimeoutException,
        IOException,
        CostTooMuchTime;

        private ERROR() {
        }
    }
}

