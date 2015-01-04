package org.openhab.binding.mpower.internal;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.SimpleHttpConnectionManager;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.commons.httpclient.protocol.ProtocolSocketFactory;
import org.apache.commons.lang.RandomStringUtils;
import org.openhab.binding.mpower.httpclient.EasySSLProtocolSocketFactory;
import org.openhab.core.library.types.OnOffType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.AsyncHttpClient.BoundRequestBuilder;
import com.ning.http.client.cookie.Cookie;
import com.ning.http.client.websocket.WebSocket;
import com.ning.http.client.websocket.WebSocketUpgradeHandler;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * Ubiquiti mPower strip binding
 * 
 * @author magcode
 */
public class MpowerConnector {
	private static final int WSPORT = 7681;
	private static final int WSPORT_SECURE = 7682;
	private AsyncHttpClient webSocketHTTPClient;
	private String host;
	private long refreshInterval = 30000;
	private String user;
	private String password;
	private Boolean secure;
	private WebSocket webSocket;
	private HttpClient httpClient;
	private MpowerBinding binding;
	private String id;
	private static final Logger logger = LoggerFactory
			.getLogger(MpowerConnector.class);

	public MpowerConnector(String host, String id, String user,
			String password, boolean secure, long refreshInterval,
			MpowerBinding bind) {
		this.binding = bind;
		this.host = host;
		this.id = id;
		this.user = user;
		this.password = password;
		this.secure = secure;
		this.refreshInterval = refreshInterval;

		// support the self signed certificate of mPower strips
		Protocol easyhttps = new Protocol("https",
				(ProtocolSocketFactory) new EasySSLProtocolSocketFactory(), 443);
		Protocol.registerProtocol("https", easyhttps);

		httpClient = new HttpClient(new SimpleHttpConnectionManager(true));

		HttpClientParams params = httpClient.getParams();
		params.setConnectionManagerTimeout(5000);
		params.setSoTimeout(30000);

	}

	public void start() {
		String id = getSession();
		if (id == null) {
			return;
		}

		webSocketHTTPClient = new AsyncHttpClient();
		try {

			WebSocketUpgradeHandler.Builder builder = new WebSocketUpgradeHandler.Builder();

			builder.addWebSocketListener(new MpowerWebSocketListener(this.id,
					this.binding));
			builder.setProtocol("mfi-protocol");
			String protocol = secure ? "wss" : "ws";
			int port = secure ? WSPORT_SECURE : WSPORT;
			BoundRequestBuilder brb = webSocketHTTPClient.prepareGet(protocol
					+ "://" + this.host + ":" + port + "/?c=" + id);
			Cookie cookie = new Cookie("AIROS_SESSIONID", id, id, this.host,
					"/", 0, 0, false, false);
			brb.addCookie(cookie);
			// brb.setProxyServer(ps);
			brb.setHeader("Sec-WebSocket-Protocol", "mfi-protocol");
			webSocket = brb.execute(builder.build()).get();

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void stop() {
		if (webSocket != null && webSocket.isOpen()) {
			webSocket.close();
		}
		if (webSocketHTTPClient != null && !webSocketHTTPClient.isClosed()) {
			webSocketHTTPClient.close();
		}

		logout();
	}

	@SuppressWarnings("unchecked")
	public void send(int socket, OnOffType onOff) {
		JSONObject sensor = new JSONObject();
		sensor.put("output", onOff == OnOffType.ON ? 1 : 0);
		sensor.put("port", socket);
		JSONArray sensors = new JSONArray();
		sensors.add(sensor);
		JSONObject wrap = new JSONObject();
		wrap.put("sensors", sensors);
		webSocket.sendTextMessage(wrap.toJSONString());
	}

	/**
	 * Performs the login to mPower
	 * 
	 * @return
	 */
	private String getSession() {
		String targetDummy = "/index.cgi";
		String id = RandomStringUtils.randomNumeric(32);
		String protocol = this.secure ? "https" : "http";
		PostMethod post = new PostMethod(protocol + "://" + this.host
				+ "/login.cgi");
		post.setRequestHeader("Cookie", "AIROS_SESSIONID=" + id);

		Part[] parts = { new StringPart("username", this.user),
				new StringPart("password", this.password),
				new StringPart("uri", targetDummy) };

		post.setRequestEntity(new MultipartRequestEntity(parts, post
				.getParams()));
		try {
			int returnCode = httpClient.executeMethod(post);
			@SuppressWarnings("unused")
			String result = post.getResponseBodyAsString();
			if (returnCode != HttpStatus.SC_MOVED_TEMPORARILY) {
				logger.error("Could not connect. Invalid credentials");
			} else {
				logger.info("Login successful");
			}
			return id;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			post.releaseConnection();
		}
		return null;
	}

	private void logout() {

	}

	public long getRefreshInterval() {
		return refreshInterval;
	}

	public void setRefreshInterval(long refreshInterval) {
		this.refreshInterval = refreshInterval;
	}
}
