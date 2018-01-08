/**
 * Copyright (c) 2010-2016 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.plex.internal;

import static org.apache.commons.lang.StringUtils.*;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutionException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.SystemUtils;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.DeserializationConfig.Feature;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.jboss.netty.handler.timeout.TimeoutException;
import org.openhab.binding.plex.internal.communication.AbstractSessionItem;
import org.openhab.binding.plex.internal.communication.Connection;
import org.openhab.binding.plex.internal.communication.Device;
import org.openhab.binding.plex.internal.communication.MediaContainer;
import org.openhab.binding.plex.internal.communication.Player;
import org.openhab.binding.plex.internal.communication.Server;
import org.openhab.binding.plex.internal.communication.SessionUpdate;
import org.openhab.binding.plex.internal.communication.Track;
import org.openhab.binding.plex.internal.communication.User;
import org.openhab.binding.plex.internal.communication.websocket.NotificationContainer;
import org.openhab.binding.plex.internal.communication.websocket.Update;
import org.openhab.core.library.types.IncreaseDecreaseType;
import org.openhab.core.library.types.PercentType;
import org.openhab.core.types.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ning.http.client.AsyncCompletionHandler;
import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.AsyncHttpClient.BoundRequestBuilder;
import com.ning.http.client.AsyncHttpClientConfig;
import com.ning.http.client.AsyncHttpClientConfig.Builder;
import com.ning.http.client.Response;
import com.ning.http.client.providers.netty.NettyAsyncHttpProvider;
import com.ning.http.client.websocket.WebSocket;
import com.ning.http.client.websocket.WebSocketTextListener;
import com.ning.http.client.websocket.WebSocketUpgradeHandler;
import com.ning.http.util.Base64;

/**
 * Manages the web socket connection to a Plex server. Also responsible for sending HTTP GET commands.
 *
 * @author Jeroen Idserda
 * @since 1.7.0
 */
public class PlexConnector extends Thread {

    private static final Logger logger = LoggerFactory.getLogger(PlexConnector.class);

    private static final int REQUEST_TIMEOUT_MS = 10000;

    private static final int RECONNECT_DELAY = 5000;

    private static final int VOLUME_STEP = 5;

    /**
     * Generated random client ID for openHAB
     */
    private static final String CLIENT_ID = "3e4e9b32-d366-47e2-a378-03044e9d1338";

    private static final String SIGN_IN_URL = "https://plex.tv/users/sign_in.xml";

    private static final String API_RESOURCES_URL = "https://plex.tv/api/resources?includeHttps=1";

    private static final String TOKEN_HEADER = "X-Plex-Token";

    private final AsyncHttpClient client;

    private final WebSocketUpgradeHandler handler;

    private final PlexConnectionProperties connection;

    private final PlexUpdateReceivedCallback callback;

    private boolean running = true;

    /**
     * Websocket URI
     */
    private final String wsUri;

    /**
     * URL for accessing session information
     */
    private final String sessionsUrl;

    /**
     * URL for fetching the connected clients
     */
    private final String clientsUrl;

    private boolean connected;

    private WebSocket webSocket;

    private Map<String, PlexSession> sessions = new HashMap<String, PlexSession>();

    /**
     * Client cache
     */
    private static Integer CACHE_VALID_TIME = 900000; // 15 minutes

    private MediaContainer clientCache;

    private Date lastClientCacheUpdate;

    /**
     * Create a connector for a single connection to a Plex server
     *
     * @param connection
     *            Connection properties
     * @param callback
     *            Called when a state update is received
     * @throws UnknownHostException
     *             If hostname is not resolvable.
     */
    public PlexConnector(PlexConnectionProperties connection, PlexUpdateReceivedCallback callback)
            throws UnknownHostException {
        this.connection = connection;
        this.callback = callback;

        requestToken();
        resolveServer();

        this.wsUri = String.format("%s://%s:%d/:/websockets/notifications",
                connection.getUri().getScheme().equals("https") ? "wss" : "ws", connection.getUri().getHost(),
                connection.getUri().getPort());
        this.sessionsUrl = String.format("%s/status/sessions", connection.getUri().toString());
        this.clientsUrl = String.format("%s/clients", connection.getUri().toString());

        this.client = new AsyncHttpClient(new NettyAsyncHttpProvider(createAsyncHttpClientConfig()));
        this.handler = createWebSocketHandler();
    }

    /**
     * Check if the connection to the Plex server is active
     *
     * @return true if an active connection to the Plex server exists, false otherwise
     */
    public boolean isConnected() {
        if (webSocket == null || !webSocket.isOpen()) {
            return false;
        }

        return connected;
    }

    /**
     * Run initial connection attempt to Plex in this separate thread
     */
    @Override
    public void run() {
        connect();
    }

    /**
     * Attempts to create a web socket connection to the Plex server and begins listening for updates
     *
     * @throws IOException
     * @throws InterruptedException
     * @throws ExecutionException
     */
    private void open() throws IOException, InterruptedException, ExecutionException {
        close();
        webSocket = client.prepareGet(addDefaultQueryParameters(wsUri)).execute(handler).get();
    }

    /**
     * Closes the web socket connection
     */
    public void close() {
        if (webSocket != null) {
            running = false;
            webSocket.close();
            webSocket = null;
        }
    }

    /**
     * Send command to Plex
     *
     * @param config
     *            The binding configuration for the item
     * @param command
     *            Command to send
     *
     * @throws IOException
     *             When it's not possible to send HTTP GET command
     */
    public void sendCommand(PlexBindingConfig config, Command command) throws IOException {
        String cmd = null;
        String property = config.getProperty();

        if (property.equals(PlexProperty.VOLUME.getName())) {
            cmd = getVolumeCommand(config, command);
        } else if (property.equals(PlexProperty.PROGRESS.getName())) {
            cmd = getProgressCommand(config, command);
        } else if (property.equals(PlexProperty.PLAYPAUSE.getName())) {
            cmd = getPlayPauseCommand(config);
        } else {
            cmd = config.getProperty();
        }

        if (cmd != null) {
            Server host = getHost(config.getMachineIdentifier());
            if (host != null && !isBlank(host.getHost())) {
                String uri = String.format("http://%s:%s/player/%s", host.getHost(), host.getPort(), cmd);
                uri = appendParametersForCommand(uri, config.getMachineIdentifier());
                internalSendCommand(config.getMachineIdentifier(), uri);
            } else {
                logger.debug("Cannot send command, host is unknown for machine ID {}", config.getMachineIdentifier());
            }
        }
    }

    /**
     * Finds a PlexSession for a certain client identified by machineIdentifier
     *
     * @param machineIdentifier
     *            Plex client ID
     * @return Session for the machineIdentifier or null
     */
    public PlexSession getSessionByMachineId(String machineIdentifier) {
        for (Entry<String, PlexSession> session : sessions.entrySet()) {
            if (session.getValue().getMachineIdentifier().equals(machineIdentifier)) {
                return session.getValue();
            }
        }

        return null;
    }

    private String getVolumeCommand(PlexBindingConfig config, Command command) {
        int newVolume = 100;

        PlexSession session = getSessionByMachineId(config.getMachineIdentifier());
        if (session != null) {
            newVolume = session.getVolume();
        }

        if (command.getClass().equals(PercentType.class)) {
            PercentType percentType = (PercentType) command;
            newVolume = percentType.intValue();
        } else if (command.getClass().equals(IncreaseDecreaseType.class)) {
            if (command.equals(IncreaseDecreaseType.DECREASE)) {
                newVolume = Math.max(0, newVolume - VOLUME_STEP);
            } else {
                newVolume = Math.min(100, newVolume + VOLUME_STEP);
            }
        }

        if (session != null) {
            session.setVolume(newVolume);
            callback.updateReceived(session);
        }

        String url = String.format("playback/setParameters?volume=%d", newVolume);
        return url;
    }

    private String getProgressCommand(PlexBindingConfig config, Command command) {
        PlexSession session = getSessionByMachineId(config.getMachineIdentifier());
        String url = null;

        if (session != null) {
            int offset = 0;

            if (command.getClass().equals(PercentType.class)) {
                PercentType percent = (PercentType) command;
                offset = new BigDecimal(session.getDuration()).multiply(
                        percent.toBigDecimal().divide(new BigDecimal("100"), new MathContext(5, RoundingMode.HALF_UP)))
                        .intValue();

                offset = Math.max(0, offset);
                offset = Math.min(session.getDuration(), offset);

                url = String.format("playback/seekTo?offset=%d", offset);
            } else if (command.getClass().equals(IncreaseDecreaseType.class)) {
                if (command.equals(IncreaseDecreaseType.DECREASE)) {
                    url = PlexProperty.STEP_BACK.getName();
                } else {
                    url = PlexProperty.STEP_FORWARD.getName();
                }
            }
        }

        return url;
    }

    private String getPlayPauseCommand(PlexBindingConfig config) {
        String command = PlexProperty.PAUSE.getName();

        PlexSession session = getSessionByMachineId(config.getMachineIdentifier());
        if (session != null) {
            if (PlexPlayerState.Paused.equals(session.getState())) {
                command = PlexProperty.PLAY.getName();
            }
        }

        return command;
    }

    private WebSocketUpgradeHandler createWebSocketHandler() {
        WebSocketUpgradeHandler.Builder builder = new WebSocketUpgradeHandler.Builder();
        builder.addWebSocketListener(new PlexWebSocketListener());
        return builder.build();
    }

    private AsyncHttpClientConfig createAsyncHttpClientConfig() {
        Builder builder = new AsyncHttpClientConfig.Builder();
        builder.setRequestTimeoutInMs(REQUEST_TIMEOUT_MS);
        return builder.build();
    }

    private synchronized void refreshSessions() {
        logger.debug("Refreshing Plex sessions");

        MediaContainer container = getDocument(sessionsUrl, MediaContainer.class);

        if (container != null) {
            Map<String, PlexSession> previousSessions = new HashMap<String, PlexSession>(sessions);
            sessions.clear();

            addSessionFor(container.getVideos());
            addSessionFor(container.getTracks());

            setVolumeFromPreviousSessions(previousSessions);
        }
    }

    private void addSessionFor(List<? extends AbstractSessionItem> items) {
        for (AbstractSessionItem item : items) {
            PlexSession session = new PlexSession();
            fillSession(session, item);
            sessions.put(session.getSessionKey(), session);
        }
    }

    private void fillSession(PlexSession session, AbstractSessionItem item) {
        Player player = item.getPlayer();

        session.setSessionKey(item.getSessionKey());
        session.setState(PlexPlayerState.of(player.getState()));
        if (!isBlank(item.getGrandparentTitle())) {
            session.setTitle(item.getGrandparentTitle() + " - " + item.getTitle());
        } else {
            session.setTitle(item.getTitle());
        }
        session.setType(item.getType());
        session.setMachineIdentifier(player.getMachineIdentifier());
        if (isNumeric(item.getDuration())) {
            session.setDuration(Integer.valueOf(item.getDuration()));
        }
        session.setCover(getCover(item));
        session.setKey(item.getKey());
    }

    private String getCover(AbstractSessionItem item) {
        String cover = null;

        // Only use grandparentThumb if it's present in the session item
        // and if the session item is not a music track
        if (!isBlank(item.getGrandparentThumb()) && !item.getClass().equals(Track.class)) {
            cover = item.getGrandparentThumb();
        } else if (!isBlank(item.getThumb())) {
            cover = item.getThumb();
        }

        if (!isBlank(cover)) {
            cover = addDefaultQueryParameters(String.format("%s%s", connection.getUri().toString(), cover));
        }

        return cover;
    }

    private Server getHost(String machineIdentifier) {

        if (clientCache == null || new Date().getTime() - lastClientCacheUpdate.getTime() > CACHE_VALID_TIME
                || clientCache.getServer(machineIdentifier) == null) {
            lastClientCacheUpdate = new Date();
            clientCache = getDocument(clientsUrl, MediaContainer.class);
        }

        Server server = clientCache.getServer(machineIdentifier);
        if (server != null) {
            return server;
        }

        return null;
    }

    private void internalSendCommand(String machineIdentifier, String url) throws IOException {
        logger.debug("Calling url {}", url);

        BoundRequestBuilder builder = client.prepareGet(url).setHeaders(getDefaultHeaders())
                .setHeader("X-Plex-Target-Client-Identifier", machineIdentifier);

        builder.execute(new AsyncCompletionHandler<Response>() {
            @Override
            public Response onCompleted(Response response) throws Exception {
                if (response.getStatusCode() != 200) {
                    logger.error("Error while sending command to Plex: {}\r\n{}", response.getStatusText(),
                            response.getResponseBody());
                }
                return response;
            }

            @Override
            public void onThrowable(Throwable t) {
                logger.error("Error sending command to Plex", t);
            }
        });
    }

    private PlexSession getSession(String sessionKey, String key) {
        if (!sessions.containsKey(sessionKey) || !sessions.get(sessionKey).getKey().equals(key)) {
            refreshSessions();
        }

        if (sessions.containsKey(sessionKey)) {
            return sessions.get(sessionKey);
        }

        return null;
    }

    private void setVolumeFromPreviousSessions(Map<String, PlexSession> previousSessions) {
        for (Entry<String, PlexSession> sessionEntry : sessions.entrySet()) {
            PlexSession newSession = sessionEntry.getValue();
            String machineIdentifier = newSession.getMachineIdentifier();
            for (Entry<String, PlexSession> session : previousSessions.entrySet()) {
                if (session.getValue().getMachineIdentifier().equals(machineIdentifier)) {
                    newSession.setVolume(session.getValue().getVolume());
                    return;
                }
            }
        }
    }

    private void connect() {
        int delay = 0;
        logger.debug("Connecting web socket to Plex");
        while (!isConnected()) {
            try {
                Thread.sleep(delay);
                open();
            } catch (IOException e) {
                logger.debug("Error connecting to Plex", e);
            } catch (InterruptedException e) {
                logger.debug("Interrupted while connecting to Plex", e);
            } catch (ExecutionException e) {
                logger.debug("Error connecting to Plex", e);
            }
            delay = RECONNECT_DELAY;
        }
    }

    /**
     * Listener for web socket. Receives and parses status updates from Plex.
     *
     * @author Jeroen Idserda
     * @since 1.7.0
     */
    private class PlexWebSocketListener implements WebSocketTextListener {

        private final ObjectMapper mapper = new ObjectMapper();

        @Override
        public void onOpen(WebSocket webSocket) {
            logger.info("Plex websocket connected to {}:{}", connection.getUri().getHost(),
                    connection.getUri().getPort());
            connected = true;
        }

        @Override
        public void onError(Throwable e) {
            if (e instanceof ConnectException) {
                logger.debug("[{}]: Websocket connection error", connection.getUri().getHost());
            } else if (e instanceof TimeoutException) {
                logger.debug("[{}]: Websocket timeout error", connection.getUri().getHost());
            } else {
                logger.debug("[{}]: Websocket error: {}", connection.getUri().getHost(), e.getMessage());
            }
        }

        @Override
        public void onClose(WebSocket webSocket) {
            logger.warn("[{}]: Websocket closed", connection.getUri().getHost());
            webSocket = null;
            connected = false;

            if (running) {
                connect();
            }
        }

        @Override
        public void onMessage(String message) {
            logger.debug("[{}]: Message received: {}", connection.getUri().getHost(), message);
            SessionUpdate update = getSessionUpdateFrom(message);

            if (update != null && isNotBlank(update.getSessionKey())) {
                String sessionKey = update.getSessionKey();
                String key = update.getKey();
                String state = update.getState();
                PlexSession session = getSession(sessionKey, key);

                if (!isBlank(state) && session != null) {
                    PlexPlayerState playerState = PlexPlayerState.of(state);
                    session.setState(playerState);
                    session.setViewOffset(update.getViewOffset());
                    callback.updateReceived(session);
                }
            }
        }

        private SessionUpdate getSessionUpdateFrom(String message) {
            try {
                switch (connection.getApiLevel()) {
                    case v1:
                        mapper.configure(Feature.UNWRAP_ROOT_VALUE, false);
                        Update update = mapper.readValue(message, Update.class);
                        if (update.getType().equals("playing") && update.getChildren().size() > 0) {
                            return update.getChildren().get(0);
                        }
                        break;
                    case v2:
                        mapper.configure(Feature.UNWRAP_ROOT_VALUE, true);
                        NotificationContainer notificationContainer = mapper.readValue(message,
                                NotificationContainer.class);
                        if (notificationContainer.getStateNotifications().size() > 0) {
                            return notificationContainer.getStateNotifications().get(0);
                        }
                        break;
                }
            } catch (JsonParseException e) {
                logger.error("Error parsing JSON", e);
            } catch (JsonMappingException e) {
                logger.error("Error mapping JSON", e);
            } catch (IOException e) {
                logger.error("An I/O error occurred while decoding JSON", e);
            }

            return null;
        }

        @Override
        public void onFragment(String fragment, boolean last) {
        }

    }

    public void refresh() {
        MediaContainer container = getDocument(clientsUrl, MediaContainer.class);
        if (container != null) {
            callback.serverListUpdated(container);
        }
    }

    private <T> T getDocument(String uri, Class<T> type) {
        return doHttpRequest("GET", uri, new HashMap<String, String>(), type);
    }

    private <T> T postDocument(String uri, Map<String, String> parameters, Class<T> type) {
        return doHttpRequest("POST", uri, parameters, type);
    }

    private <T> T doHttpRequest(String method, String uri, Map<String, String> parameters, Class<T> type) {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(uri).openConnection();
            connection.setRequestMethod(method);
            connection.setConnectTimeout(REQUEST_TIMEOUT_MS);
            connection.setReadTimeout(REQUEST_TIMEOUT_MS);

            for (Entry<String, String> entry : parameters.entrySet()) {
                connection.addRequestProperty(entry.getKey(), entry.getValue());
            }

            for (Entry<String, Collection<String>> entry : getDefaultHeaders().entrySet()) {
                connection.addRequestProperty(entry.getKey(), StringUtils.join(entry.getValue(), ", "));
            }

            JAXBContext jc = JAXBContext.newInstance(type);
            InputStream xml = connection.getInputStream();

            @SuppressWarnings("unchecked")
            T obj = (T) jc.createUnmarshaller().unmarshal(xml);
            connection.disconnect();

            return obj;
        } catch (MalformedURLException e) {
            logger.debug(e.getMessage(), e);
        } catch (IOException e) {
            logger.debug(e.getMessage(), e);
        } catch (JAXBException e) {
            logger.debug(e.getMessage(), e);
        }

        return null;
    }

    private void resolveServer() throws UnknownHostException {
        MediaContainer container = getDocument(API_RESOURCES_URL, MediaContainer.class);

        // We need the IP-address to find this server in the server list on plex.tv
        String ip = resolveHostname(connection.getHost());

        if (container != null) {
            for (Device device : container.getDevices()) {
                if (contains(device.getProvides(), "server")) {
                    for (Connection deviceConnection : device.getConnections()) {
                        boolean uriSet = (connection.getUri() != null);
                        boolean portEqual = String.valueOf(connection.getPort()).equals(deviceConnection.getPort());
                        boolean hostEqual = ip.equals(deviceConnection.getAddress());

                        if (!uriSet && portEqual && hostEqual) {
                            connection.setUri(deviceConnection.getUri());
                            connection.setApiLevel(PlexApiLevel.getApiLevel(device.getProductVersion()));
                            logger.debug("Server found, version {}, api level {}", device.getProductVersion(),
                                    connection.getApiLevel());
                        }
                    }
                }
            }
        }

        if (connection.getUri() == null) {
            logger.warn(
                    "Server not found in plex.tv device list, setting URI from configured data. Try configuring IP-address of host.");
            connection.setUri(String.format("http://%s:%d", ip, connection.getPort()));
        }

    }

    private void requestToken() {
        boolean tokenPresent = !isEmpty(connection.getToken());
        boolean usernamePresent = !isEmpty(connection.getUsername());
        boolean passwordPresent = !isEmpty(connection.getPassword());

        if (!tokenPresent && usernamePresent && passwordPresent) {
            Map<String, String> parameters = new HashMap<String, String>();
            String authString = Base64.encode((connection.getUsername() + ":" + connection.getPassword()).getBytes());
            parameters.put("Authorization", "Basic " + authString);

            User user = postDocument(SIGN_IN_URL, parameters, User.class);
            if (user != null) {
                logger.debug("Plex login successful");
                connection.setToken(user.getAuthenticationToken());
            } else {
                logger.warn("Invalid credentials for Plex account");
            }
        }
    }

    private String appendParametersForCommand(String uri, String machineIdentifier) {
        List<String> parameters = new ArrayList<String>();

        if (!isEmpty(machineIdentifier)) {
            PlexSession session = getSessionByMachineId(machineIdentifier);
            if (session != null) {
                String type = "video";
                if ("track".equals(session.getType())) {
                    type = "music";
                }
                parameters.add(String.format("%s=%s", "type", type));
            }
        }

        if (!parameters.isEmpty()) {
            uri += (!uri.contains("?") ? "?" : "&") + StringUtils.join(parameters, "&");
        }

        return uri;
    }

    private Map<String, Collection<String>> getDefaultHeaders() {
        Map<String, Collection<String>> headers = new HashMap<String, Collection<String>>();

        headers.put("X-Plex-Client-Identifier", Arrays.asList(CLIENT_ID));
        headers.put("X-Plex-Product", Arrays.asList("openHAB"));
        headers.put("X-Plex-Version", Arrays.asList(PlexActivator.getVersion().toString()));
        headers.put("X-Plex-Device", Arrays.asList(SystemUtils.JAVA_RUNTIME_NAME));
        headers.put("X-Plex-Device-Name", Arrays.asList("openHAB"));
        headers.put("X-Plex-Provides", Arrays.asList("controller"));
        headers.put("X-Plex-Platform", Arrays.asList("Java"));
        headers.put("X-Plex-Platform-Version", Arrays.asList(SystemUtils.JAVA_VERSION));

        if (connection.hasToken()) {
            headers.put(TOKEN_HEADER, Arrays.asList(connection.getToken()));
        }

        return headers;
    }

    private String addDefaultQueryParameters(String uri) {
        if (connection.hasToken()) {
            uri += "?" + TOKEN_HEADER + "=" + connection.getToken();
        }

        return uri;
    }

    private String resolveHostname(String host) throws UnknownHostException {
        InetAddress address = InetAddress.getByName(host);
        return address.getHostAddress();
    }
}
