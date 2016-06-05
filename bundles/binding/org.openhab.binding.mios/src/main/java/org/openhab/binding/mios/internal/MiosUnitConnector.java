/**
 * Copyright (c) 2010-2016 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.mios.internal;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.openhab.binding.mios.internal.config.DeviceBindingConfig;
import org.openhab.binding.mios.internal.config.MiosBindingConfig;
import org.openhab.binding.mios.internal.config.SceneBindingConfig;
import org.openhab.core.transform.TransformationException;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ning.http.client.AsyncCompletionHandler;
import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.AsyncHttpClientConfig;
import com.ning.http.client.AsyncHttpClientConfig.Builder;
import com.ning.http.client.Response;
import com.ning.http.client.providers.jdk.JDKAsyncHttpProvider;

/**
 * Manages the HTTP Connection for a single MiOS Unit.
 *
 * This code has two functions to perform:
 * <ul>
 * <li>Inbound <i>changes</i> from the MiOS Unit.<br>
 * Manages an internal thread to periodically poll the configured MiOS Unit and timeout at the specified interval. All
 * changes are internally transformed from their original (JSON) form, and dispatched to the relevant parts of openHAB.
 * <li>Outbound <i>commands</i> to the MiOS Unit.<br>
 * Exposes a method to transform & transmit openHAB Commands to the configured MiOS Unit, in a non-blocking manner.<br>
 * Callers wishing to utilize this functionality use the <code>invokeCommand</code> method.
 * </ul>
 *
 * @author Mark Clark
 * @since 1.6.0
 */
public class MiosUnitConnector {

    private static final Logger logger = LoggerFactory.getLogger(MiosUnitConnector.class);

    private static final String ENCODING_CHARSET = "utf-8";

    private static final String BIND_COMMAND_VALUE = "??";
    private static final String BIND_ITEM_INCREMENT = "?++";
    private static final String BIND_ITEM_DECREMENT = "?--";
    private static final String BIND_ITEM_VALUE = "?";

    private static final String SCENE_URL = "http://%s:%d/data_request?id=action&serviceId=urn:micasaverde-com:serviceId:HomeAutomationGateway1&action=RunScene&SceneNum=%d";

    private static final String DEVICE_URL = "http://%s:%d/data_request?id=action&DeviceNum=%d&serviceId=%s&action=%s";
    private static final String DEVICE_URL_PARAMS = DEVICE_URL + "&%s";

    private static final Pattern DEVICE_PATTERN = Pattern.compile("(?<serviceName>.+)/" + "(?<serviceAction>.+)"
            + "\\(((?<serviceParam>[a-zA-Z]+[a-zA-Z0-9]*)(=(?<serviceValue>.+))?)?\\)");

    private static final Pattern ACTION_PATTERN = Pattern.compile("(?<serviceName>.+)/" + "(?<serviceAction>.+)");

    // the MiOS instance and openHAB event publisher handles
    private final MiosUnit unit;
    private final MiosBinding binding;

    private final AsyncHttpClient client;
    private LongPoll pollCall;
    private Thread pollThread;
    boolean running;

    // Create a place to keep the last "status" attribute, for each Device, so we can compare incoming values for
    // Duplicates (Devices & Scenes)
    //
    // The MiOS system will send duplicate values, so we need this data to avoid pumelling openHAB with unnecessary
    // changes (and it can only be done here due to the async nature of openHAB store writes). This map is keyed by the
    // DeviceId/SceneId form the MiOS system, which can be either a String, or an Integer.
    private HashMap<String, Integer> deviceStatusCache = new HashMap<String, Integer>(100);
    private HashMap<Integer, Integer> sceneStatusCache = new HashMap<Integer, Integer>(100);

    /**
     * @param unit
     *            The host to connect to. Give a reachable hostname or IP address, without protocol or port
     */
    public MiosUnitConnector(MiosUnit unit, MiosBinding binding) {
        logger.debug("Constructor: unit '{}', binding '{}'", unit, binding);

        this.unit = unit;
        this.binding = binding;

        Builder builder = new AsyncHttpClientConfig.Builder();
        builder.setRequestTimeoutInMs(unit.getTimeout());

        // Use the JDK Provider for now, we're not looking for server-level
        // scalability, and we'd like to lighten the load for folks wanting to
        // run atop RPi units.
        this.client = new AsyncHttpClient(new JDKAsyncHttpProvider(builder.build()));

        pollCall = new LongPoll();
        pollThread = new Thread(pollCall);
    }

    /***
     * Check if the connection to the MiOS instance is active
     *
     * @return true if an active connection to the MiOS instance exists, false otherwise
     */
    public boolean isConnected() {
        return isRunning() && pollCall.isConnected();
    }

    /**
     * Attempts to create a connection to the MiOS host and begin listening for updates over the async HTTP connection.
     *
     * @throws ExecutionException
     * @throws InterruptedException
     * @throws IOException
     */
    public void open() throws IOException, InterruptedException, ExecutionException {
        running = true;
        pollThread.start();
    }

    public void restart() {
        pollCall.restart();
    }

    /***
     * Close this connection to the MiOS instance
     */
    public void close() {
        running = false;
    }

    /**
     * Is the underlying Polling thread running?
     *
     * @return true if it's running.
     */

    public boolean isRunning() {
        return running;
    }

    private static String toBindValue(String value, Command command, State state) {
        // TODO: Allow for more complex Bind expressions, to allow for different
        // increment/decrement values, and various other transformations that
        // may be required.

        // Perform a simple item-value substitution on the resulting string.
        if (value == null) {
            return state.toString();
        } else if (value.contains(BIND_COMMAND_VALUE)) {
            return value.replace(BIND_COMMAND_VALUE, command.toString());
        } else if (value.contains(BIND_ITEM_INCREMENT)) {
            String tmp = String.valueOf(Integer.parseInt(state.toString()) + 1);
            return value.replace(BIND_ITEM_INCREMENT, tmp);
        } else if (value.contains(BIND_ITEM_DECREMENT)) {
            String tmp = String.valueOf(Integer.parseInt(state.toString()) - 1);
            return value.replace(BIND_ITEM_DECREMENT, tmp);
        } else if (value.contains(BIND_ITEM_VALUE)) {
            return value.replace(BIND_ITEM_VALUE, state.toString());
        } else {
            return value;
        }
    }

    private void callDevice(DeviceBindingConfig config, Command command, State state) throws TransformationException {

        logger.debug("callDevice: Need to remote-invoke Device '{}' action '{}' and current state '{}')",
                new Object[] { config.toProperty(), command, state });

        String newCommand = config.transformCommand(command);
        if (newCommand == null) {
            logger.warn("callDevice: Command '{}' not supported, or missing command: mapping, for Item '{}'",
                    command.toString(), config.getItemName());
            return;
        } else if (newCommand.equals("")) {
            logger.trace("callDevice: Item '{}' has disabled the use of Command '{}' via its configuration '{}'",
                    new Object[] { config.getItemName(), command.toString(), config.toProperty() });
            return;
        }

        Matcher matcher = DEVICE_PATTERN.matcher(newCommand);

        if (matcher.matches()) {
            try {
                MiosUnit u = getMiosUnit();

                String serviceName = DeviceBindingConfig.mapServiceAlias(matcher.group("serviceName"));
                String serviceAction = matcher.group("serviceAction");
                String serviceParam = matcher.group("serviceParam");
                String serviceValue = matcher.group("serviceValue");

                logger.debug(
                        "callDevice: decoded as serviceName '{}' serviceAction '{}' serviceParam '{}' serviceValue '{}'",
                        new Object[] { serviceName, serviceAction, serviceParam, serviceValue });

                // Perform any necessary bind-variable style transformations on
                // the value, before we put it into the URL.
                serviceValue = toBindValue(serviceValue, command, state);

                // If the parameters to the URL are specified, then we need to
                // build the parameter section of the URL, encoding parameter
                // names and values... trust no-one 8)
                if (serviceParam != null) {
                    String p = URLEncoder.encode(serviceParam, ENCODING_CHARSET) + '='
                            + URLEncoder.encode(serviceValue, ENCODING_CHARSET);
                    callMios(String.format(DEVICE_URL_PARAMS, u.getHostname(), u.getPort(), config.getMiosId(),
                            URLEncoder.encode(serviceName, ENCODING_CHARSET),
                            URLEncoder.encode(serviceAction, ENCODING_CHARSET), p));
                } else {
                    callMios(String.format(DEVICE_URL, u.getHostname(), u.getPort(), config.getMiosId(),
                            URLEncoder.encode(serviceName, ENCODING_CHARSET),
                            URLEncoder.encode(serviceAction, ENCODING_CHARSET)));
                }

            } catch (UnsupportedEncodingException uee) {
                logger.debug("Really, trust me, this won't happen ;)   exception='{}'", uee);
            }
        } else {
            logger.error("callDevice: The parameter is in the wrong format.  BindingConfig '{}', UPnP Action '{}'",
                    config, newCommand);
        }

    }

    private void callScene(SceneBindingConfig config, Command command, State state) throws TransformationException {
        logger.debug("callScene: Need to remote-invoke Scene '{}'", config.toProperty());

        String newCommand = config.transformCommand(command);

        if (newCommand != null) {
            MiosUnit u = getMiosUnit();
            callMios(String.format(SCENE_URL, u.getHostname(), u.getPort(), config.getMiosId()));
        } else {
            logger.warn("callScene: Command '{}' not supported, or missing command: declaration, for Item '{}'",
                    command.toString(), config.getItemName());
        }
    }

    private void callMios(String url) {
        logger.debug("callMios: Would like to fire off the URL '{}'", url);

        try {
            @SuppressWarnings("unused")
            Future<Integer> f = getAsyncHttpClient().prepareGet(url).execute(new AsyncCompletionHandler<Integer>() {
                @Override
                public Integer onCompleted(Response response) throws Exception {

                    // Yes, their content-type really does come back with just "xml", but we'll add "text/xml" for
                    // when/if they ever fix that bug...
                    if (!(response.getStatusCode() == 200 && ("text/xml".equals(response.getContentType())
                            || "xml".equals(response.getContentType())))) {
                        logger.debug("callMios: Error in HTTP Response code {}, content-type {}:\n{}",
                                new Object[] { response.getStatusCode(), response.getContentType(),
                                        response.hasResponseBody() ? response.getResponseBody() : "No Body" });
                    }
                    return response.getStatusCode();
                }

                @Override
                public void onThrowable(Throwable t) {
                    logger.warn("callMios: Exception Throwable occurred fetching content: {}", t.getMessage(), t);
                }
            }

            );

            // TODO: Run it and walk away?
            //
            // Work out a better model to gather information about the
            // success/fail of the call, log details (etc) so things can be
            // diagnosed.
        } catch (Exception e) {
            logger.warn("callMios: Exception Error occurred fetching content: {}", e.getMessage(), e);
        }
    }

    /**
     * Request that a MiOS Scene be triggered.
     *
     * Used by openHAB Action code, this method fires off the MiOS Scene associated with the Scene Item.
     *
     * @param config
     *            A Scene [Item] Binding Configuration.
     */
    public void invokeScene(SceneBindingConfig config) {

        logger.debug("invokeScene: Invoking remote Scene '{}'", config.toProperty());

        MiosUnit u = getMiosUnit();
        callMios(String.format(SCENE_URL, u.getHostname(), u.getPort(), config.getMiosId()));
    }

    /**
     * Request that a MiOS Device Action be triggered.
     *
     * Used by openHAB Action code, this method fires off the MiOS Action associated with the Device Item.
     *
     * @param config
     *            A Device [Item] Binding Configuration.
     * @param actionName
     *            a UPnP-style Action to fire off on the remote MiOS Unit.
     * @param params
     *            a NV-Pair style list of parameters to be used by the Action call.
     */
    public void invokeAction(DeviceBindingConfig config, String actionName, List<Entry<String, Object>> params) {
        Matcher matcher = ACTION_PATTERN.matcher(actionName);
        if (matcher.matches()) {
            try {
                MiosUnit u = getMiosUnit();

                String serviceName = DeviceBindingConfig.mapServiceAlias(matcher.group("serviceName"));
                String serviceAction = matcher.group("serviceAction");

                if (params != null) {
                    String p = "";

                    for (Entry<String, Object> entry : params) {
                        if (p.length() != 0) {
                            p += '&';
                        }

                        p += URLEncoder.encode(entry.getKey(), ENCODING_CHARSET) + '='
                                + URLEncoder.encode(entry.getValue().toString(), ENCODING_CHARSET);
                    }

                    callMios(String.format(DEVICE_URL_PARAMS, u.getHostname(), u.getPort(), config.getMiosId(),
                            URLEncoder.encode(serviceName, ENCODING_CHARSET),
                            URLEncoder.encode(serviceAction, ENCODING_CHARSET), p));
                } else {
                    callMios(String.format(DEVICE_URL, u.getHostname(), u.getPort(), config.getMiosId(),
                            URLEncoder.encode(serviceName, ENCODING_CHARSET),
                            URLEncoder.encode(serviceAction, ENCODING_CHARSET)));
                }

            } catch (UnsupportedEncodingException uee) {
                logger.debug("Really, trust me, this won't happen ;)   exception='{}'", uee);
            }
        }
    }

    /**
     * Request a Command be delivered to the MiOS Unit under control.
     * <p>
     * The MiOS Binding uses this call to deliver "single valued" openHAB Commands to the target MiOS Unit.
     * <p>
     * Configurable transformations, defined at the BindingConfig level, are used to transform this openHAB Command into
     * the specific form required by the MiOS Unit.
     * <p>
     * openHAB Commands can only be targeted to Device and Scene Bindings, all other requests will cause a warning to be
     * logged.
     */
    public void invokeCommand(MiosBindingConfig config, Command command, State state) throws Exception {
        if (config instanceof SceneBindingConfig) {
            callScene((SceneBindingConfig) config, command, state);
        } else if (config instanceof DeviceBindingConfig) {
            callDevice((DeviceBindingConfig) config, command, state);
        } else {
            logger.warn("Unhandled command execution for Command ('{}') on binding '{}'", command, config);
        }
    }

    private MiosUnit getMiosUnit() {
        return unit;
    }

    private MiosBinding getMiosBinding() {
        return binding;
    }

    private AsyncHttpClient getAsyncHttpClient() {
        return client;
    }

    /**
     * MiOS Poll code.
     *
     * This code will stand up a thread to serially poll the target MiOS Unit. The initial poll request will return the
     * full content from the MiOS unit. Subsequent calls are requested to only return the incremental/changed contents.
     *
     * If a processing error (Timeout, etc) is detected, then a full content poll is again initiated (since things can
     * change during the interval)
     *
     * Upon successful polling, a series of calls are made to openHAB to push the data to the respective bindings, so
     * that data values will change within the user's Rules (etc).
     *
     * @author Mark Clark
     * @since 1.6.0
     */
    private class LongPoll implements Runnable {
        private boolean connected = false;

        private Integer loadTime = null;
        private Integer dataVersion = null;
        private int failures = 0;

        private final ObjectMapper mapper = new ObjectMapper();

        private static final String BASE_URL = "http://%s:%d/data_request";
        private static final String STATUS2_URL = BASE_URL + "?id=status2";
        private static final String STATUS2_INCREMENTAL_URL = STATUS2_URL
                + "&LoadTime=%d&DataVersion=%d&Timeout=%d&MinimumDelay=%d";

        public LongPoll() {
        }

        @SuppressWarnings("unchecked")
        private List<Object> getList(Map<String, Object> data, String param) {
            if (!data.containsKey(param)) {
                return new ArrayList<Object>();
            }

            return (List<Object>) data.get(param);
        }

        private String getUri(boolean incremental) {
            MiosUnit unit = getMiosUnit();

            if (incremental) {
                AsyncHttpClientConfig c = getAsyncHttpClient().getConfig();

                // Use a timeout on the MiOS URL call that's about 2/3 of what
                // the connection timeout is.
                int t = Math.min(c.getIdleConnectionTimeoutInMs(), unit.getTimeout()) / 500 / 3;
                int d = unit.getMinimumDelay();

                return String.format(Locale.US, STATUS2_INCREMENTAL_URL, unit.getHostname(), unit.getPort(), loadTime,
                        dataVersion, new Integer(t), new Integer(d));
            } else {
                return String.format(Locale.US, STATUS2_URL, unit.getHostname(), unit.getPort());
            }
        }

        private Object fixTimestamp(Object value) {
            if (value == null) {
                return value;
            } else if (value instanceof Integer) {
                Calendar cal = Calendar.getInstance();
                cal.setTimeInMillis(((Integer) value).intValue() * 1000l);
                return cal;
            } else if (value instanceof String && !value.equals("")) {
                Calendar cal = Calendar.getInstance();
                cal.setTimeInMillis(Integer.parseInt((String) value) * 1000l);
                return cal;
            } else {
                return value;
            }
        }

        private void publish(String property, Object value, boolean incremental) {
            String p = getMiosUnit().formatProperty(property);

            try {
                getMiosBinding().postPropertyUpdate(p, value, incremental);
            } catch (Exception e) {
                logger.error("Exception '{}' raised pushing property '{}' value '{}' into openHAB",
                        new Object[] { e.getMessage(), p, value, e });
            }
        }

        private void processSystem(Map<String, Object> system, boolean incremental) {
            for (Map.Entry<String, Object> entry : system.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();

                if (!key.equals("devices") && !key.equals("scenes") && !key.equals("rooms")
                        && !key.equals("sections")) {
                    if (value instanceof String || value instanceof Integer || value instanceof Double
                            || value instanceof Boolean) {
                        // Skip over Lua code blocks and
                        // fix [known] date-time values on the way through, so
                        // they use the native type.
                        if (key.equals("DeviceSync") || key.equals("LoadTime") || key.equals("zwave_heal")
                                || key.equals("TimeStamp")) {
                            value = fixTimestamp(value);
                        }

                        String property = "system:/" + key;
                        publish(property, value, incremental);
                    } else {
                        // No need to bring these into openHAB, they'll only
                        // bulk up
                        // the system.
                        if (key.equals("StartupCode") || key.equals("startup")) {
                            continue;
                        }

                        logger.debug("processSystem: skipping key={}, class={}", key, value.getClass());
                    }
                }
            }
        }

        private void processDevices(List<Object> devices, boolean incremental) {
            for (Object d : devices) {
                @SuppressWarnings("unchecked")
                Map<String, Object> device = (Map<String, Object>) d;

                // Note that the "name" field is not present in status2
                // responses,
                // like it is in user_data2 responses.

                //
                // These can be either an Integer or a String, either way it's
                // an int. Newer ones tend to be Strings, so we'll convert them
                // all to String values.
                //
                String deviceId;
                Object v = device.get("id");

                if (v instanceof String) {
                    deviceId = (String) v;
                } else if (v instanceof Integer) {
                    deviceId = ((Integer) v).toString();
                } else {
                    throw new IllegalArgumentException("WTF?");
                }

                // Enumerate Device Attributes
                for (Entry<String, Object> da : device.entrySet()) {
                    String key = da.getKey();
                    Object value = da.getValue();

                    if (value instanceof String || value instanceof Integer || value instanceof Double
                            || value instanceof Boolean) {
                        if (key.equals("time_created")) {
                            // fix [known] date-time values on the way through,
                            // so they use the native type.
                            value = fixTimestamp(value);
                        } else if (key.equals("id")) {
                            // Skip "id", since it's done at the end.
                            continue;
                        }

                        boolean force = false;
                        boolean statusAttr = key.equals("status");

                        // Handle a bug in MiOS's JSON, where they send the STATUS Attribute even if it hasn't
                        // changed. This resulted in a lot of unnecessary writes to openHAB. We can do this
                        // because this thread is the single-source of values for the STATUS attribute.
                        if (statusAttr) {
                            Integer lastValue = deviceStatusCache.get(deviceId);
                            if (!value.equals(lastValue)) {
                                deviceStatusCache.put(deviceId, (Integer) value);
                                force = true;
                            }
                        }

                        if (!statusAttr || force) {
                            String property = "device:" + deviceId + '/' + key;
                            publish(property, value, incremental);
                        }
                    } else {
                        // No need to bring these into openHAB, they'll only
                        // bulk up
                        // the system.
                        if (key.equals("states") || key.equals("ControlURLs") || key.equals("Jobs")
                                || key.equals("tooltip")) {
                            continue;
                        }

                        logger.debug("processDevices: skipping key={}, class={}", key, value.getClass());
                    }
                }

                // Enumerate Device State Variables
                List<Object> deviceStates = getList(device, "states");
                boolean variablesProcessed = false;

                for (Object ds : deviceStates) {
                    @SuppressWarnings("unchecked")
                    Map<String, Object> state = (Map<String, Object>) ds;
                    String var = (String) state.get("service") + "/" + (String) state.get("variable");
                    String property = "device:" + deviceId + "/service/" + var;

                    // Can be String or Integer
                    Object value = state.get("value");
                    publish(property, value, incremental);
                    variablesProcessed = true;
                }

                // The Device's ID attribute is last in the list and only
                // sent when at-least-one UPnP State Variable has been changed.
                //
                // This is done to allow folks something to "trigger" off in a
                // rule if they have cross-Item validation dependencies.
                //
                // Otherwise they'll come out in JSON order
                // (unpredictable) which may not be the order they're
                // normally changed at the MiOS end. Making this last
                // would be like having an "end of [device] transaction"
                // marker.
                if (variablesProcessed) {
                    publish("device:" + deviceId + "/id", deviceId, incremental);
                }
            }
        }

        private void processScenes(List<Object> scenes, boolean incremental) {
            for (Object s : scenes) {
                @SuppressWarnings("unchecked")
                Map<String, Object> scene = (Map<String, Object>) s;

                // Note that the "name" field is not present in status2
                // responses,
                // like it is in user_data2 responses.

                // These can be either an Integer or a String, either way it's
                // an int. Newer ones tend to be Strings, so we'll convert them
                // all to String values.
                Integer sceneId = (Integer) scene.get("id");

                // Enumerate Scene Attributes
                for (Entry<String, Object> da : scene.entrySet()) {
                    String key = da.getKey();
                    Object value = da.getValue();

                    if (value instanceof String || value instanceof Integer || value instanceof Double
                            || value instanceof Boolean) {
                        if (key.equals("Timestamp")) {
                            // fix [known] date-time values on the way through,
                            // so they use the native type.
                            value = fixTimestamp(value);

                            // Fix the key so it's consistent with the one used
                            // at the System level.
                            // TODO: Document this.
                            key = "TimeStamp";
                        }

                        boolean force = false;
                        boolean statusAttr = key.equals("status");

                        // Handle a bug in MiOS's JSON, where they send the STATUS Attribute even if it hasn't
                        // changed. This resulted in a lot of unnecessary writes to openHAB. We can do this
                        // because this thread is the single-source of values for the STATUS attribute.
                        if (statusAttr) {
                            Integer lastValue = sceneStatusCache.get(sceneId);
                            if (!value.equals(lastValue)) {
                                sceneStatusCache.put(sceneId, (Integer) value);
                                force = true;
                            }
                        }

                        if (!statusAttr || force) {
                            String property = "scene:" + sceneId + "/" + key;
                            publish(property, value, incremental);
                        }
                    } else {
                        // No need to bring these into openHAB, they'll only
                        // bulk up
                        // the system.
                        if (key.equals("timers") || key.equals("triggers") || key.equals("groups")
                                || key.equals("tooltip") || key.equals("Jobs") || key.equals("lua")) {
                            continue;
                        }

                        logger.debug("processScenes: skipping key={}, class={}", key, value.getClass());
                    }
                }
            }
        }

        private void processRooms(List<Object> rooms, boolean incremental) {
            // TODO: Implement
        }

        private void processSections(List<Object> sections, boolean incremental) {
            // TODO: Implement
        }

        private void processResponse(Map<String, Object> response, boolean incremental) {

            Integer lt = (Integer) response.get("LoadTime");
            Integer dv = (Integer) response.get("DataVersion");

            if (lt != null && dv != null) {
                connected = true;
                List<Object> devices = getList(response, "devices");
                List<Object> scenes = getList(response, "scenes");
                List<Object> rooms = getList(response, "rooms");
                List<Object> sections = getList(response, "sections");

                logger.debug(
                        "processResponse: success! loadTime={}, dataVersion={} devices({}) scenes({}) rooms({}) sections({})",
                        new Object[] { lt, dv, Integer.toString(devices.size()), Integer.toString(scenes.size()),
                                Integer.toString(rooms.size()), Integer.toString(sections.size()) });

                processDevices(devices, incremental);
                processScenes(scenes, incremental);
                processRooms(rooms, incremental);
                processSections(sections, incremental);
                processSystem(response, incremental);

                // Only reset these once we've successfully loaded/parsed the
                // content.
                this.loadTime = lt;
                this.dataVersion = dv;
                this.failures = 0;
            } else {
                throw new RuntimeException("Processing error on MiOS JSON Response - malformed");
            }
        }

        public boolean isConnected() {
            return connected;
        }

        // TODO: Need to make this stream-oriented, so we don't have to keep the
        // entire (large) MiOS JSON string in memory at the same time as the
        // parsed JSON result.
        @SuppressWarnings("unchecked")
        private Map<String, Object> readJson(String json) {
            if (json == null) {
                return new HashMap<String, Object>();
            }

            try {
                return mapper.readValue(json, Map.class);
            } catch (JsonParseException e) {
                // TODO: Replace RuntimeException with a more specialized
                // exception.
                throw new RuntimeException("Failed to parse JSON", e);
            } catch (JsonMappingException e) {
                // TODO: Replace RuntimeException with a more specialized
                // exception.
                throw new RuntimeException("Failed to map JSON", e);
            } catch (IOException e) {
                // TODO: Replace RuntimeException with a more specialized
                // exception.
                throw new RuntimeException("Failed to read JSON", e);
            }
        }

        private boolean fullReload;
        private long lastFullReload;
        private Object fullReloadLock = new Object();

        public void restart() {
            synchronized (this.fullReloadLock) {
                this.lastFullReload = System.currentTimeMillis();
                this.fullReload = true;
            }
            logger.debug("restart: Resetting, requesting forced reload. lastFullReload={}", this.lastFullReload);
        }

        @Override
        public void run() {
            restart();
            MiosUnit unit = getMiosUnit();
            int startupDelay = unit.getStartupDelay();
            int errorCount = unit.getErrorCount();
            long loopCount = 0l;

            do {
                try {
                    //
                    // On the first loop, and each time we're restarted through openHAB config change, add
                    // a delay to bundle the incoming openHAB Configuration events.
                    //
                    while (this.fullReload) {
                        int sleepTime;
                        synchronized (this.fullReloadLock) {
                            sleepTime = (int)(this.lastFullReload + startupDelay - System.currentTimeMillis());
                            if (sleepTime <= 0) {
                                this.fullReload = false;
                            }
                        }

                        if (sleepTime <= 0) {
                            loopCount = 0l;
                            logger.debug("run: finishing sleep cycle.");
                            break;
                        }

                        logger.debug("run: sleeping, delaying reload sleepTime={}", sleepTime);
                        Thread.sleep(sleepTime);
                    }

                    // Force a full poll of the dataSet every time the MiOS Unit
                    // configuration indicates to do so, or if we get an error.
                    boolean force = this.fullReload || (errorCount != 0) && (failures != 0) && ((failures % errorCount) == 0);
                    boolean incremental = (!force && loadTime != null && dataVersion != null);
                    String uri = getUri(incremental);

                    logger.debug("run: URI Built was '{}' loop '{}'", uri, loopCount);

                    Future<Response> f = getAsyncHttpClient().prepareGet(uri).execute();
                    Response r = f.get();

                    // Force the Response Charset to be UTF-8, since MiOS isn't setting
                    // anything in their HTTP response Header.
                    Map<String, Object> json = readJson(r.getResponseBody("UTF-8"));

                    if (json.containsKey("error")) {
                        throw new IOException(json.get("error").toString());
                    }

                    connected = true;
                    processResponse(json, incremental);

                    // Reset the Loop count only once we've successfully
                    // processed the Response. Otherwise there's a potential for
                    // it to never perform a full (initial) fetch call.
                    int c = getMiosUnit().getRefreshCount();
                    loopCount = loopCount + 1;
                    if (c != 0) {
                        loopCount = (loopCount % c);
                    }
                } catch (InterruptedException ie) {
                    logger.debug("run: Thread shutdown by Interrupted");
                } catch (Exception e) {
                    connected = false;
                    this.failures++;
                    logger.debug(
                            "run: Exception Error occurred fetching/processing content: {},{}.  Total failures ({})",
                            new Object[] { e.getMessage(), e, Integer.toString(failures) });

                    // TODO: Make the pause configurable and/or add a backoff
                    // mechanism.
                    //
                    // Pause a little before restarting, to give things time to
                    // recover.
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException tex) {
                    }
                }
            } while (isRunning());

            logger.info("run: Shutting down MiOS Polling thread.  Unit={}", getMiosUnit().getName());
            connected = false;
        }
    }
}
