/*** Eclipse Class Decompiler plugin, copyright (c) 2016 Chen Chao (cnfree2000@hotmail.com) ***/
package net.logvv.raven.push.hwpush.huawei.common;

import org.apache.http.Consts;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.DnsResolver;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.conn.SystemDefaultDnsResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.*;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HttpConnectionAdaptor {
	private static final Logger LOG = LoggerFactory.getLogger(HttpConnectionAdaptor.class);
	private static final String SSL_TYPE = "TLSv1";
	private static final String TAG = HttpConnectionAdaptor.class.getName();
	private static final int DEFAULTHTTPSPORT = 443;
	private static final String HTTP_HOST_NAME = "api.vmall.com";
	private static final int DEFAULTHTTPPORT = 80;
	private static final int DEFAULT_MAX_TOTAL_CONNECTIONS = 500;
	private static final int DEFAULT_MAX_CONNECTIONS_PER_ROUTE = 50;
	private static final int DEFAULT_MAX_CONNECTIONS_LOCAL_ROUTE = 100;
	private static final String TRUST_MANAGER_TYPE = "X509";
	private static RequestConfig defaultRequestConfig = null;

	protected boolean isSSLClient = false;

	protected int mConnectionsPerRoute = 50;

	protected int mMaxConnections = 500;

	private CloseableHttpClient httpClient = null;

	private SSLConnectionSocketFactory sf = null;

	private Registry<ConnectionSocketFactory> socketFactoryRegistry = null;

	private KeyStore trustStore = null;

	public static RequestConfig getDefaultRequestConfig() {
		return defaultRequestConfig;
	}

	public static void setDefaultRequestConfig(RequestConfig defaultConfig) {
		defaultRequestConfig = defaultConfig;
	}

	public KeyStore getTrustStore() {
		return this.trustStore;
	}

	public int getMaxConnections() {
		return this.mMaxConnections;
	}

	public int getConnectionsPerRoute() {
		return this.mConnectionsPerRoute;
	}

	public synchronized boolean initHttpConnections(int connectionsPerRoute,
			int maxConnections) {
		if ((connectionsPerRoute <= 0) || (maxConnections <= 0)) {
			return false;
		}

		if (connectionsPerRoute != this.mConnectionsPerRoute) {
			this.sf = null;
			this.httpClient = null;
		}

		this.mMaxConnections = Math.max(connectionsPerRoute, maxConnections);

		this.mConnectionsPerRoute = connectionsPerRoute;

		return true;
	}

	public synchronized void initKeyStoreStream(InputStream keyStoreStream,
			String password) throws NSPException {
		if (null == keyStoreStream) {
			throw new NSPException(1, "KeyStoreStream cann't be null.");
		}

		try {
			KeyStore ks = null;
			ks = KeyStore.getInstance(KeyStore.getDefaultType());
			ks.load(keyStoreStream, password.toCharArray());
			this.trustStore = ks;

			this.sf = null;
			this.httpClient = null;
		} catch (Exception e) {
			throw new NSPException(1, "InitKeyStoreStream failed.", e);
		}
	}

	public synchronized void resetConnetion() {
		this.sf = null;
		this.httpClient = null;
	}

	private void initSocketFactory() {
		try {
			if (null == this.sf) {
				if (null != this.trustStore) {
					TrustManager tm = getTrustManager();

					SSLContext sslContext = SSLContexts
							.custom()
							.loadTrustMaterial(this.trustStore,
									new TrustSelfSignedStrategy()).build();

					sslContext.init(null, new TrustManager[] { tm },
							new SecureRandom());

					createSocketFactory(sslContext);
				} else {
					KeyStore ts = KeyStore.getInstance(KeyStore
							.getDefaultType());
					ts.load(null, null);

					SSLContext sslContext = SSLContexts
							.custom()
							.loadTrustMaterial(ts,
									new TrustSelfSignedStrategy()).build();

					TrustManager tm = new X509TrustManager() {
						public void checkClientTrusted(X509Certificate[] chain,
								String authType) throws CertificateException {
						}

						public void checkServerTrusted(X509Certificate[] chain,
								String authType) throws CertificateException {
						}

						public X509Certificate[] getAcceptedIssuers() {
							return new X509Certificate[0];
						}
					};
					sslContext.init(null, new TrustManager[] { tm }, null);

					createSocketFactory(sslContext);
				}

                this.socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
						.register("http", PlainConnectionSocketFactory.INSTANCE)
						.register("https", this.sf)
						.build();

			}

		} catch (Exception e) {
			LOG.error(TAG, "SSLContext initSocketFactory failed:", e);
		}
	}

	private TrustManager getTrustManager() throws NoSuchAlgorithmException,
			KeyStoreException, NSPException {
		final List<X509TrustManager> m509TrustManager = new ArrayList();

		TrustManagerFactory trustManagerFactory = TrustManagerFactory
				.getInstance("X509");
		trustManagerFactory.init(this.trustStore);
		TrustManager[] tms = trustManagerFactory.getTrustManagers();
		for (int i = 0; i < tms.length; ++i) {
			if (!(tms[i] instanceof X509TrustManager))
				continue;
			m509TrustManager.add((X509TrustManager) tms[i]);
		}

		if (m509TrustManager.size() == 0) {
			throw new NSPException(2, "Couldn't find a X509TrustManager!");
		}

		ArrayList list = new ArrayList();
		for (X509TrustManager tm : m509TrustManager) {
			list.addAll(Arrays.asList(tm.getAcceptedIssuers()));
		}

		final X509Certificate[] certificates = new X509Certificate[list.size()];

		TrustManager tm = new X509TrustManager() {
			public void checkClientTrusted(X509Certificate[] chain,
					String authType) throws CertificateException {
			}

			public void checkServerTrusted(X509Certificate[] chain,
					String authType) throws CertificateException {
				X509TrustManager defaultX509 = m509TrustManager.get(0);
				defaultX509.checkServerTrusted(chain, authType);
			}

			public X509Certificate[] getAcceptedIssuers() {
				return certificates;
			}
		};
		return tm;
	}

	private void createSocketFactory(SSLContext sslContext) {
		this.sf = new SSLConnectionSocketFactory(sslContext,
				new String[] { "TLSv1" }, null,
				SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER) {
			protected void prepareSocket(SSLSocket socket) throws IOException {
				if (null == socket)
					return;
				HttpConnectionAdaptor.setEnableSafeCipherSuites(socket);
			}
		};
	}

	public static void setEnableSafeCipherSuites(SSLSocket sslsock) {
		String[] enabledCiphers = sslsock.getEnabledCipherSuites();

		List enabledCiphersList = new ArrayList();

		String rc4Cipher = "_RC4_".toLowerCase();
		String desCipher = "_DES".toLowerCase();
		String tlsEMPTY = "TLS_EMPTY_RENEGOTIATION_INFO_SCSV".toLowerCase();

		for (String str : enabledCiphers) {
			if ((null == str) || (str.toLowerCase().contains(rc4Cipher))
					|| (str.toLowerCase().contains(desCipher))
					|| (str.toLowerCase().contains(tlsEMPTY))) {
				continue;
			}
			enabledCiphersList.add(str);
		}

		String[] safeEnabledCiphers = (String[]) enabledCiphersList
				.toArray(new String[enabledCiphersList.size()]);

		sslsock.setEnabledCipherSuites(safeEnabledCiphers);
	}

	private CloseableHttpClient createHttpClient() throws NSPException {
		try {
			initSocketFactory();

			DnsResolver dnsResolver = new SystemDefaultDnsResolver() {
				public InetAddress[] resolve(String host)
						throws UnknownHostException {
					if (host.equalsIgnoreCase("localhost")) {
						return new InetAddress[] { InetAddress
								.getByAddress(new byte[] { 127, 0, 0, 1 }) };
					}

					return super.resolve(host);
				}
			};
			PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(
					this.socketFactoryRegistry, dnsResolver);

			SocketConfig socketConfig = SocketConfig.custom()
					.setTcpNoDelay(true).setSoKeepAlive(true).setSoLinger(0)
					.setSoReuseAddress(true).build();

			connManager.setDefaultSocketConfig(socketConfig);

			ConnectionConfig connectionConfig = ConnectionConfig.custom()
					.setCharset(Consts.UTF_8).build();
			connManager.setDefaultConnectionConfig(connectionConfig);
			connManager.setConnectionConfig(new HttpHost("localhost", 80),
					ConnectionConfig.DEFAULT);

			connManager.setMaxTotal(this.mMaxConnections);

			if ((this.mMaxConnections <= this.mConnectionsPerRoute)
					&& (this.mMaxConnections > 0)) {
				this.mConnectionsPerRoute = this.mMaxConnections;
			}

			connManager.setDefaultMaxPerRoute(this.mConnectionsPerRoute);

			connManager.setMaxPerRoute(new HttpRoute(new HttpHost("localhost",
					80)), 100);

			CloseableHttpClient httpclient = HttpClients
					.custom()
					.setConnectionManager(connManager)
					.setDefaultRequestConfig(
							RequestConfig.copy(defaultRequestConfig).build())
					.build();

			return ((CloseableHttpClient) new WeakReference(httpclient).get());
		} catch (Exception e) {
			throw new NSPException(2, "Service unavailable.", e);
		}
	}

	private static URLInfo getURLInfo(String url) throws NSPException {
		if (Utils.isEmptyString(url)) {
			throw new NSPException(2, "Url is empty.");
		}

		URLInfo clientInfo = new URLInfo();

		int port = -1;
		String host = "";
		try {
			URL urlAddr = new URL(url);
			host = urlAddr.getHost();
			port = urlAddr.getPort();
		} catch (Exception e) {
			e.getCause();
		}

		if (Utils.isEmptyString(host)) {
			host = getUrlHost(url);
		}

		boolean isHttps = url.toLowerCase().startsWith("https");

		if (port <= 0) {
			port = (isHttps) ? 443 : 80;
		}

		if (isHttps) {
			clientInfo.isHttps = true;
		} else {
			clientInfo.isHttps = false;
		}

		clientInfo.port = port;

		if (!(Utils.isEmptyString(host))) {
			clientInfo.host = host;
		}

		return clientInfo;
	}

	private static String getUrlHost(String url) {
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

		String host = url.substring(doubleslash, end);
		return host;
	}

	public static HttpPost getHttpPost(String urlStr, int connectMillisTimeout,
			int readMillisTimeout, boolean handleRedirects) {
		HttpPost httpPost = (HttpPost) new WeakReference(new HttpPost(urlStr))
				.get();
		if (null == httpPost) {
			return null;
		}
		setHttpParams(httpPost, connectMillisTimeout, readMillisTimeout,
				handleRedirects);
		return httpPost;
	}

	public static HttpGet getHttpGet(String urlStr, int connectMillisTimeout,
			int readMillisTimeout, boolean handleRedirects) {
		HttpGet httpGet = (HttpGet) new WeakReference(new HttpGet(urlStr))
				.get();
		if (null == httpGet) {
			return null;
		}
		setHttpParams(httpGet, connectMillisTimeout, readMillisTimeout,
				handleRedirects);
		return httpGet;
	}

	public static HttpHost getHttpHost(String urlStr) throws NSPException {
		URLInfo clientInfo = getURLInfo(urlStr);
		String shemeName = getShemeName(clientInfo);

		return ((HttpHost) new WeakReference(new HttpHost(clientInfo.host,
				clientInfo.port, shemeName)).get());
	}

	private static String getShemeName(URLInfo clientInfo) {
		String shemeName = "https";
		if (clientInfo.isHttps) {
			shemeName = "https";
		} else {
			shemeName = "http";
		}
		return shemeName;
	}

	public static void setHttpParams(HttpRequestBase httpBase,
			int connectMillisTimeout, int readMillisTimeout,
			boolean handleRedirects) {
		RequestConfig requestConfig = RequestConfig.copy(defaultRequestConfig)
				.setConnectTimeout(connectMillisTimeout)
				.setSocketTimeout(readMillisTimeout)
				.setRedirectsEnabled(handleRedirects).build();

		httpBase.setConfig(requestConfig);
		httpBase.setHeader("accept-encoding", "gzip");
	}

	public synchronized CloseableHttpClient getHttpClient() throws NSPException {
		if (null == this.httpClient) {
			this.httpClient = createHttpClient();
		}

		return this.httpClient;
	}

	static {
		defaultRequestConfig = RequestConfig.custom()
				.setCookieSpec("best-match").setExpectContinueEnabled(false)
				.setStaleConnectionCheckEnabled(true)
				.setCookieSpec("compatibility").build();
	}

	private static class URLInfo {
		boolean isHttps;
		int port;
		String host;

		private URLInfo() {
			this.isHttps = true;

			this.port = 443;

			this.host = "api.vmall.com";
		}
	}
}