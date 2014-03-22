package org.openhab.binding.xbmc.rpc;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutionException;

import org.openhab.binding.xbmc.internal.XbmcHost;
import org.openhab.binding.xbmc.rpc.calls.FilesPrepareDownload;
import org.openhab.binding.xbmc.rpc.calls.GUIShowNotification;
import org.openhab.binding.xbmc.rpc.calls.PlayerGetActivePlayers;
import org.openhab.binding.xbmc.rpc.calls.PlayerGetItem;
import org.openhab.binding.xbmc.rpc.calls.PlayerGetProperties;
import org.openhab.binding.xbmc.rpc.calls.PlayerPlayPause;
import org.openhab.binding.xbmc.rpc.calls.PlayerStop;
import org.openhab.core.events.EventPublisher;
import org.openhab.core.library.types.StringType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.AsyncHttpClientConfig;
import com.ning.http.client.AsyncHttpClientConfig.Builder;
import com.ning.http.client.Realm;
import com.ning.http.client.Realm.AuthScheme;
import com.ning.http.client.providers.netty.NettyAsyncHttpProvider;
import com.ning.http.client.websocket.WebSocket;
import com.ning.http.client.websocket.WebSocketTextListener;
import com.ning.http.client.websocket.WebSocketUpgradeHandler;

/**
 * Manages all updates and actions for a single instance of xbmc.
 * 
 * @author tlan, Ben Jones
 * 
 */
public class XbmcConnector {

	private static final Logger logger = LoggerFactory.getLogger(XbmcConnector.class);

	// the XBMC instance and openHAB event publisher handles
	private final XbmcHost xbmc;
	private final EventPublisher eventPublisher;

	private final String rsUri;
	private final String wsUri;

	// stores which property is associated with each item
	private final Map<String, String> watches = new HashMap<String, String>();

	// the async connection to the XBMC instance
	private AsyncHttpClient client;

	/**
	 * @param xbmc
	 *            The host to connect to. Give a reachable hostname or ip
	 *            address, without protocol or port
	 * @param eventPublisher
	 *            EventPublisher to push out state updates
	 */
	public XbmcConnector(XbmcHost xbmc, EventPublisher eventPublisher) {
		this.xbmc = xbmc;
		this.eventPublisher = eventPublisher;

		rsUri = String.format("http://%s:%d/jsonrpc", xbmc.getHostname(), xbmc.getPort());
		wsUri = String.format("ws://%s:%d/jsonrpc", xbmc.getHostname(), xbmc.getWSPort());
	}

	/***
	 * Check if the connection to the XBMC instance is active
	 * 
	 * @return true if an active connection to the XBMC instance exists, false otherwise
	 */
	public boolean isOpen() { 
		return client != null && !client.isClosed(); 
	}
	
	/**
	 * Attempts to create a connection to the XBMC host and begin listening
	 * for updates over the async http web socket
	 *  
	 * @throws URISyntaxException
	 *             If the result of adding protocol and port to the hostname is
	 *             not a valid uri
	 * @throws ExecutionException 
	 * @throws InterruptedException 
	 * @throws IOException 
	 */
	public void open() throws URISyntaxException, IOException, InterruptedException, ExecutionException {
		AsyncHttpClientConfig config = createAsyncHttpClientConfig();
		WebSocketUpgradeHandler handler = createWebSocketHandler();
		
		client = new AsyncHttpClient(new NettyAsyncHttpProvider(config));
		client.prepareGet(wsUri).execute(handler).get();
	}

	/***
	 * Close this connection to the XBMC instance
	 */
	public void close() {
		if (client != null) client.close();
	}
		
	private AsyncHttpClientConfig createAsyncHttpClientConfig() {
		Builder builder = new AsyncHttpClientConfig.Builder();
		return builder.setRealm(createRealm()).build();
	}

	private Realm createRealm() {
		Realm.RealmBuilder builder = new Realm.RealmBuilder();
		builder.setPrincipal(xbmc.getUsername());
		builder.setPassword(xbmc.getPassword());
		builder.setUsePreemptiveAuth(true);
		builder.setScheme(AuthScheme.BASIC);		
		return builder.build();
	}

	private WebSocketUpgradeHandler createWebSocketHandler() {
		WebSocketUpgradeHandler.Builder builder = new WebSocketUpgradeHandler.Builder();
		return builder.addWebSocketListener(new XbmcWebSocketListener()).build(); 
	}
	
	class XbmcWebSocketListener implements WebSocketTextListener {
		private final ObjectMapper mapper = new ObjectMapper();

		@Override
		public void onOpen(WebSocket webSocket) {
			logger.debug("Websocket opened");
			requestPlayerStatusUpdate();
		}
		
		@Override
		public void onError(Throwable e) {
			logger.error("Error on websocket", e);
		}
		
		@Override
		public void onClose(WebSocket webSocket) {
			logger.warn("Websocket closed");
		}
		
		@Override
		@SuppressWarnings("unchecked")
		public void onMessage(String message) {
			 Map<String, String> json;
			 try {
				 json = mapper.readValue(message, Map.class);
			 } catch (JsonParseException e) {
				 logger.error("Error parsing JSON:\n" + message);
				 return;
			 } catch (JsonMappingException e) {
				 logger.error("Error mapping JSON:\n" + message);
				 return;
			 } catch (IOException e) {
				 logger.error("An I/O error occured while decoding JSON:\n" + message);
				 return;
			 }

			// We only care about certain notifications on the websocket
			// feed, since all our actual data fetching is done via http
			if (json.containsKey("method")) {
				String method = json.get("method");
				if (method.startsWith("Player.On")) {
					processPlayerStateChanged(method);
				}
			}
		}
		
		 @Override
		public void onFragment(String fragment, boolean last) {
		}
	}
	
	/**
	 * Create a mapping between an item and an xbmc property
	 * 
	 * @param itemName
	 *            The name of the item which should receive updates
	 * @param property
	 *            The property of this xbmc instance, which is to be 
	 *            watched for changes
	 * 
	 */
	public void addItem(String itemName, String property) {
		if (!watches.containsKey(itemName)) {
			watches.put(itemName, property);

			// Request a player update, so maybe we can fill in whatever our new
			// item cares about
			if (isOpen()) {
				requestPlayerStatusUpdate();
			}
		}
	}

	private void processPlayerStateChanged(String method) {
		if ("Player.OnPlay".equals(method)) {
			requestPlayerUpdate();
		}

		if ("Player.OnPause".equals(method)) {
			requestPlayerUpdate();
		}

		if ("Player.OnStop".equals(method)) {
			updateWatch("Player.State", "Stop");
			updateWatch("Player.Title", "");
			updateWatch("Player.ShowTitle", "");
			updateWatch("Player.Fanart", "");
		}
	}

	private void requestPlayerStatusUpdate() {
		PlayerGetActivePlayers activePlayers = new PlayerGetActivePlayers(client, rsUri);
		activePlayers.execute();

		if (activePlayers.isPlaying()) {
			requestPlayerUpdate();
		}
	}

	private void requestPlayerUpdate() {
		PlayerGetActivePlayers activePlayers = new PlayerGetActivePlayers(client, rsUri);
		activePlayers.execute();

		PlayerGetProperties properties = new PlayerGetProperties(client, rsUri);
		properties.setPlayerId(activePlayers.getPlayerId());
		properties.execute();

		PlayerGetItem item = new PlayerGetItem(client, rsUri);
		item.setPlayerId(activePlayers.getPlayerId());
		item.execute();

		updateWatch("Player.State", properties.isPaused() ? "Pause" : "Play");
		updateWatch("Player.Title", item.getTitle());
		updateWatch("Player.ShowTitle", item.getShowtitle());
		updateWatch("Player.Type", activePlayers.getPlayerType());

//		updateWatch("Player.Artist", item.getItemField("artist"));
//		updateWatch("Player.Track", item.getItemField("track"));
//		updateWatch("Player.Album", item.getItemField("album"));

		if (!"".equals(item.getFanart())) {
			FilesPrepareDownload fanart = new FilesPrepareDownload(client, rsUri);
			fanart.setImagePath(item.getFanart());
			fanart.execute();
			updateWatch("Player.Fanart", String.format("http://%s:%d/%s", xbmc.getHostname(), xbmc.getPort(), fanart.getPath()));
		}
	}

	public void showNotification(String title, String message) {
		GUIShowNotification showNotification = new GUIShowNotification(client, rsUri);
		showNotification.setTitle(title);
		showNotification.setMessage(message);
		showNotification.execute();
	}
	
	public void playerPlayPause() {
		PlayerGetActivePlayers activePlayers = new PlayerGetActivePlayers(client, rsUri);
		activePlayers.execute();

		PlayerPlayPause playPause = new PlayerPlayPause(client, rsUri);
		playPause.setPlayerId(activePlayers.getPlayerId());
		playPause.execute();
	}
	
	public void playerStop() {
		PlayerGetActivePlayers activePlayers = new PlayerGetActivePlayers(client, rsUri);
		activePlayers.execute();

		PlayerStop stop = new PlayerStop(client, rsUri);
		stop.setPlayerId(activePlayers.getPlayerId());
		stop.execute();
	}
	
	private void updateWatch(String watch, String value) {
		for (Entry<String, String> e : watches.entrySet()) {
			String item = e.getKey();
			String elem = e.getValue();
			if (watch.equals(elem)) {
				eventPublisher.postUpdate(item, new StringType(value));
			}
		}
	}
}
