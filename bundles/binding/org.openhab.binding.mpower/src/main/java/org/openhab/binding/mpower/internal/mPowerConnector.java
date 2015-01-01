package org.openhab.binding.mpower.internal;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.SimpleHttpConnectionManager;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.lang.RandomStringUtils;
import org.openhab.core.library.types.OnOffType;

import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.AsyncHttpClient.BoundRequestBuilder;
import com.ning.http.client.cookie.Cookie;
import com.ning.http.client.websocket.WebSocket;
import com.ning.http.client.websocket.WebSocketUpgradeHandler;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class mPowerConnector {
	private AsyncHttpClient webSocketHTTPClient;
	private String host;
	private WebSocket webSocket;
	private HttpClient httpClient;
	private mPowerBinding binding;
	private String address;

	public mPowerConnector(String host, String address, mPowerBinding bind) {
		this.binding = bind;
		this.host = host;
		this.address = address;
		// ProxyServer ps = new ProxyServer(ProxyServer.Protocol.HTTP,
		// "127.0.0.1", 8888, "", "");

		httpClient = new HttpClient(new SimpleHttpConnectionManager(true));
		HttpClientParams params = httpClient.getParams();
		params.setConnectionManagerTimeout(5000);
		params.setSoTimeout(30000);
		// params.setContentCharset("ISO-8859-1");

	}

	public void start() {
		String id = getSession();
		// id = "1588dd65c023ab6bca2261ef20029152";
		webSocketHTTPClient = new AsyncHttpClient();
		// ProxyServer ps = new ProxyServer(ProxyServer.Protocol.HTTP,
		// "127.0.0.1", 8888, "", "");
		try {

			WebSocketUpgradeHandler.Builder builder = new WebSocketUpgradeHandler.Builder();

			builder.addWebSocketListener(new WebSocketListener(this.address, this.binding));
			builder.setProtocol("mfi-protocol");

			BoundRequestBuilder brb = webSocketHTTPClient.prepareGet("ws://"
					+ this.host + ":7681/?c=" + id);
			Cookie cookie = new Cookie("AIROS_SESSIONID", id, id, "mpower.lan",
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
		String user = "ubnt";
		String password = "ubnt";
		String targetDummy = "/index.cgi";
		// httpClient.getHostConfiguration().setProxy("127.0.0.1", 8888);
		String id = RandomStringUtils.randomNumeric(32);
		PostMethod post = new PostMethod("http://mpower.lan/login.cgi");
		post.setRequestHeader("Cookie", "AIROS_SESSIONID=" + id);

		Part[] parts = { new StringPart("username", user),
				new StringPart("password", password),
				new StringPart("uri", targetDummy) };

		post.setRequestEntity(new MultipartRequestEntity(parts, post
				.getParams()));
		try {
			int returnCode = httpClient.executeMethod(post);
			String result = post.getResponseBodyAsString();
			int a = result.length() + returnCode;
			return id;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private void logout() {

	}
}
