/**
 * Copyright (c) 2010-2016 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.ihc.internal;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.EventObject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.lang.StringUtils;
import org.openhab.binding.ihc.IhcBindingProvider;
import org.openhab.binding.ihc.ws.IhcClient;
import org.openhab.binding.ihc.ws.IhcClient.ConnectionState;
import org.openhab.binding.ihc.ws.IhcEnumValue;
import org.openhab.binding.ihc.ws.IhcEventListener;
import org.openhab.binding.ihc.ws.IhcExecption;
import org.openhab.binding.ihc.ws.datatypes.WSControllerState;
import org.openhab.binding.ihc.ws.datatypes.WSEnumValue;
import org.openhab.binding.ihc.ws.datatypes.WSResourceValue;
import org.openhab.core.binding.AbstractActiveBinding;
import org.openhab.core.binding.BindingChangeListener;
import org.openhab.core.binding.BindingProvider;
import org.openhab.core.items.Item;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.openhab.core.types.Type;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * IhcBinding order runtime value notifications from IHC / ELKO LS controller
 * and post values to the openHAB event bus when notification is received.
 *
 * Binding also polls resources from controller where interval is configured.
 *
 * @author Pauli Anttila
 * @author Simon Merschjohann
 * @since 1.1.0
 */
public class IhcBinding extends AbstractActiveBinding<IhcBindingProvider>
        implements ManagedService, IhcEventListener, BindingChangeListener {

    private static final Logger logger = LoggerFactory.getLogger(IhcBinding.class);

    private long refreshInterval = 1000;

    /** Holds runtime notification reorder timeout in milliseconds */
    private final int NOTIFICATIONS_REORDER_WAIT_TIME = 2000;

    /** Holds time stamps in seconds when binding items states are refreshed */
    private Map<String, Long> lastUpdateMap = new HashMap<String, Long>();

    /** IHC / ELKO LS Controller client */
    private static IhcClient ihc = null;

    /** IP address of IHC / ELKO LS Controller */
    private static String ip = null;

    /** User name for controller authentication */
    private static String username = null;

    /** Password for controller authentication */
    private static String password = null;

    /**
     * Path for IHC / ELKO lS project file, if it's empty/null project is
     * download from controller
     */
    private static String projectFile = null;

    /** Path for resource's dump. Dump is useful to find out resource id's. */
    private static String dumpResourceFile = null;

    /** Timeout for controller communication */
    private static int timeout = 5000;

    /**
     * Store current state of the controller, use to recognize when controller
     * state is changed
     */
    private WSControllerState controllerState = null;

    /**
     * Reminder to slow down resource value notification ordering from
     * controller.
     */
    private NotificationsRequestReminder reminder = null;
    private boolean reconnectRequest = false;
    private boolean valueNotificationRequest = false;

    @Override
    protected String getName() {
        return "IHC / ELKO LS refresh and notification listener service";
    }

    @Override
    protected long getRefreshInterval() {
        return refreshInterval;
    }

    public void activate(ComponentContext componentContext) {
    }

    public void deactivate(ComponentContext componentContext) {
        disconnect();
    }

    protected boolean isReconnectRequestActivated() {
        synchronized (IhcBinding.class) {
            return reconnectRequest;
        }
    }

    protected void setReconnectRequest(boolean reconnect) {
        synchronized (IhcBinding.class) {
            this.reconnectRequest = reconnect;
        }
    }

    protected boolean isValueNotificationRequestActivated() {
        synchronized (IhcBinding.class) {
            return valueNotificationRequest;
        }
    }

    protected void setValueNotificationRequest(boolean valueNotificationRequest) {
        synchronized (IhcBinding.class) {
            this.valueNotificationRequest = valueNotificationRequest;
        }
    }

    /**
     * Initialize IHC client and open connection to IHC / ELKO LS controller.
     *
     */
    public void connect() throws IhcExecption {

        if (StringUtils.isNotBlank(ip) && StringUtils.isNotBlank(username) && StringUtils.isNotBlank(password)) {

            logger.info("Connecting to IHC / ELKO LS controller [IP='{}' Username='{}' Password='{}'].",
                    new Object[] { ip, username, "******" });

            ihc = new IhcClient(ip, username, password, timeout);
            ihc.setProjectFile(projectFile);
            ihc.setDumpResourceInformationToFile(dumpResourceFile);
            ihc.openConnection();
            controllerState = ihc.getControllerState();
            ihc.addEventListener(this);

        } else {
            logger.warn(
                    "Couldn't connect to IHC controller because of missing connection parameters [IP='{}' Username='{}' Password='{}'].",
                    new Object[] { ip, username, "******" });
        }
    }

    /**
     * Disconnect connection to IHC / ELKO LS controller.
     *
     */
    public void disconnect() {
        if (ihc != null) {
            try {
                ihc.removeEventListener(this);
                ihc.closeConnection();
            } catch (IhcExecption e) {
                logger.error("Couldn't close connection to IHC controller", e);
            }
        }
    }

    /**
     * @{inheritDoc
     */
    @Override
    public void execute() {

        if (ihc == null || isReconnectRequestActivated()) {

            try {
                if (ihc != null) {
                    disconnect();
                }
                connect();
                setReconnectRequest(false);
                enableResourceValueNotifications();
            } catch (IhcExecption e) {
                logger.warn("Can't open connection to controller", e);
                return;
            }
        }

        if (ihc != null) {

            if (isValueNotificationRequestActivated()) {
                try {
                    enableResourceValueNotifications();
                } catch (IhcExecption e) {
                    logger.warn("Can't enable resource value notifications from controller", e);
                }
            }

            // Poll all requested resources from controller
            for (IhcBindingProvider provider : providers) {
                for (String itemName : provider.getItemNames()) {

                    int resourceId = provider.getResourceIdForInBinding(itemName);
                    int itemRefreshInterval = provider.getRefreshInterval(itemName) * 1000;

                    if (resourceId > 0 && itemRefreshInterval > 0) {

                        Long lastUpdateTimeStamp = lastUpdateMap.get(itemName);
                        if (lastUpdateTimeStamp == null) {
                            lastUpdateTimeStamp = 0L;
                        }

                        long age = System.currentTimeMillis() - lastUpdateTimeStamp;
                        boolean needsUpdate = age >= itemRefreshInterval;

                        if (needsUpdate) {
                            logger.debug("Item '{}' is about to be refreshed now", itemName);

                            try {
                                WSResourceValue resourceValue = null;

                                try {
                                    resourceValue = ihc.resourceQuery(resourceId);
                                } catch (IhcExecption e) {
                                    logger.warn("Value could not be read from controller - retrying one time.", e);

                                    try {
                                        resourceValue = ihc.resourceQuery(resourceId);
                                    } catch (IhcExecption ex) {
                                        logger.error("Communication error", ex);
                                        logger.debug("Reconnection request");
                                        setReconnectRequest(true);
                                    }
                                }

                                if (resourceValue != null) {
                                    Class<? extends Item> itemType = provider.getItemType(itemName);
                                    State value = IhcDataConverter.convertResourceValueToState(itemType, resourceValue);
                                    eventPublisher.postUpdate(itemName, value);
                                }

                            } catch (Exception e) {
                                logger.error("Error occurred during resource query", e);
                            }

                            lastUpdateMap.put(itemName, System.currentTimeMillis());
                        }
                    }
                }
            }
        } else {
            logger.warn("Controller is not initialized => refresh cycle aborted!");
        }

    }

    protected void addBindingProvider(IhcBindingProvider bindingProvider) {
        super.addBindingProvider(bindingProvider);
    }

    protected void removeBindingProvider(IhcBindingProvider bindingProvider) {
        super.removeBindingProvider(bindingProvider);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void allBindingsChanged(BindingProvider provider) {
        logger.debug("allBindingsChanged");
        setValueNotificationRequest(true);
    }

    /**
     * {@inheritDoc}
     *
     */
    @Override
    public void bindingChanged(BindingProvider provider, String itemName) {
        logger.trace("bindingChanged {}", itemName);
        if (reminder != null) {
            reminder.cancel();
            reminder = null;
        }

        reminder = new NotificationsRequestReminder(NOTIFICATIONS_REORDER_WAIT_TIME);
    }

    /**
     * Used to slow down resource value notification ordering process. All
     * resource values need to be ordered by one request from the controller,
     * therefore wait that all binding items are loaded.
     */
    private class NotificationsRequestReminder {
        Timer timer;

        public NotificationsRequestReminder(int milliseconds) {
            timer = new Timer();
            timer.schedule(new RemindTask(), milliseconds);
        }

        public void cancel() {
            timer.cancel();
        }

        class RemindTask extends TimerTask {

            @Override
            public void run() {
                logger.debug("Timer: enableResourceValueNotifications");
                setValueNotificationRequest(true);
                timer.cancel();
            }
        }
    }

    @Override
    public void updated(Dictionary<String, ?> config) throws ConfigurationException {

        logger.debug("Configuration updated, config {}", config != null ? true : false);

        if (config != null) {
            ip = (String) config.get("ip");
            username = (String) config.get("username");
            password = (String) config.get("password");
            timeout = Integer.parseInt((String) config.get("timeout"));
            projectFile = (String) config.get("projectFile");
            dumpResourceFile = (String) config.get("dumpResourceFile");
            setProperlyConfigured(true);
            setReconnectRequest(true);
        }
    }

    @Override
    protected void internalReceiveCommand(String itemName, Command command) {
        updateResource(itemName, command, false);
    }

    @Override
    public void internalReceiveUpdate(String itemName, State newState) {
        updateResource(itemName, newState, true);
    }

    /**
     * Update resource value to IHC controller.
     */
    private void updateResource(String itemName, Type type, boolean updateOnlyExclusiveOutBinding) {

        if (itemName != null) {
            Command cmd = null;
            try {
                cmd = (Command) type;
            } catch (Exception e) {
            }

            IhcBindingProvider provider = findFirstMatchingBindingProvider(itemName, cmd);

            if (provider == null) {
                // command not configured, skip
                return;
            }

            if (updateOnlyExclusiveOutBinding && provider.hasInBinding(itemName)) {
                logger.trace("Ignore in binding update for item '{}'", itemName);
                return;
            }

            logger.debug("Received update/command (item='{}', state='{}', class='{}')",
                    new Object[] { itemName, type.toString(), type.getClass().toString() });

            if (ihc == null) {
                logger.warn("Controller is not initialized, abort resource value update for item '{}'!", itemName);
                return;
            }

            if (ihc.getConnectionState() != ConnectionState.CONNECTED) {
                logger.warn("Connection to controller is not ok, abort resource value update for item '{}'!", itemName);
                return;
            }

            try {
                int resourceId = provider.getResourceId(itemName, (Command) type);

                logger.trace("found resourceId {} (item='{}', state='{}', class='{}')", new Object[] {
                        new Integer(resourceId).toString(), itemName, type.toString(), type.getClass().toString() });

                if (resourceId > 0) {
                    WSResourceValue value = ihc.getResourceValueInformation(resourceId);

                    ArrayList<IhcEnumValue> enumValues = null;

                    if (value instanceof WSEnumValue) {

                        enumValues = ihc.getEnumValues(((WSEnumValue) value).getDefinitionTypeID());
                    }

                    // check if configuration has a custom value defined
                    // (0->OFF, 1->ON, >1->trigger)
                    // if that is the case, the type will be overridden with a
                    // new type

                    Integer val = provider.getValue(itemName, (Command) type);
                    boolean trigger = false;
                    if (val != null) {
                        if (val == 0) {
                            type = OnOffType.OFF;
                        } else if (val == 1) {
                            type = OnOffType.ON;
                        } else {
                            trigger = true;
                        }
                    } else {
                        // the original type is kept
                    }

                    if (!trigger) {
                        value = IhcDataConverter.convertCommandToResourceValue(type, value, enumValues);

                        boolean result = updateResource(value);

                        if (result == true) {
                            logger.debug("Item updated '{}' succesfully sent", itemName);
                        } else {
                            logger.error("Item '{}' update failed", itemName);
                        }
                    } else {
                        value = IhcDataConverter.convertCommandToResourceValue(OnOffType.ON, value, enumValues);

                        boolean result = updateResource(value);

                        if (result == true) {
                            logger.debug("Item '{}' trigger started", itemName);

                            Thread.sleep(val);

                            value = IhcDataConverter.convertCommandToResourceValue(OnOffType.OFF, value, enumValues);

                            result = updateResource(value);

                            if (result == true) {
                                logger.debug("Item '{}' trigger completed", itemName);
                            } else {
                                logger.error("Item '{}' trigger stop failed", itemName);
                            }

                        } else {
                            logger.error("Item '{}' update failed", itemName);
                        }
                    }

                } else {
                    logger.error("resourceId invalid");
                }

            } catch (IhcExecption e) {
                logger.error("Can't update Item '{}' value ", itemName, e);
            } catch (Exception e) {
                logger.error("Error occurred during item update", e);
            }
        }

    }

    /**
     * Update resource value to IHC controller.
     */
    private boolean updateResource(WSResourceValue value) throws IhcExecption {
        boolean result = false;

        try {
            result = ihc.resourceUpdate(value);

        } catch (IhcExecption e) {

            logger.warn("Value could not be set - retrying one time: {}", e.getMessage());

            result = ihc.resourceUpdate(value);
        }

        return result;
    }

    /**
     * Find the first matching {@link IhcBindingProvider} according to
     * <code>itemName</code> and <code>command</code>.
     *
     * @param itemName
     *
     * @return the matching binding provider or <code>null</code> if no binding
     *         provider could be found
     */
    private IhcBindingProvider findFirstMatchingBindingProvider(String itemName, Command type) {
        IhcBindingProvider firstMatchingProvider = null;

        for (IhcBindingProvider provider : this.providers) {
            if (provider.getResourceId(itemName, type) > 0 || provider.getResourceId(itemName, null) > 0) {
                firstMatchingProvider = provider;
                break;
            }
        }

        return firstMatchingProvider;
    }

    /**
     * Order resource value notifications from IHC controller.
     */
    private void enableResourceValueNotifications() throws IhcExecption {

        logger.debug("Subscribe resource runtime value notifications");

        if (ihc != null) {

            if (ihc.getConnectionState() != ConnectionState.CONNECTED) {
                logger.debug("Controller is connecting, abort subscribe");
                return;
            }

            List<Integer> resourceIdList = new ArrayList<Integer>();

            for (IhcBindingProvider provider : providers) {
                for (String itemName : provider.getItemNames()) {
                    resourceIdList.add(provider.getResourceIdForInBinding(itemName));
                }
            }

            if (resourceIdList.size() > 0) {
                logger.debug("Enable runtime notfications for {} resources", resourceIdList.size());

                try {
                    ihc.enableRuntimeValueNotifications(resourceIdList);
                } catch (IhcExecption e) {
                    logger.debug("Reconnection request");
                    setReconnectRequest(true);
                }
            }
        } else {
            logger.warn("Controller is not initialized!");
            logger.debug("Reconnection request");
            setReconnectRequest(true);
        }

        setValueNotificationRequest(false);
    }

    @Override
    public void statusUpdateReceived(EventObject event, WSControllerState state) {

        logger.trace("Controller state {}", state.getState());

        if (controllerState.getState().equals(state.getState()) == false) {
            logger.info("Controller state change detected ({} -> {})", controllerState.getState(), state.getState());

            if (controllerState.getState().equals(IhcClient.CONTROLLER_STATE_INITIALIZE)
                    || state.getState().equals(IhcClient.CONTROLLER_STATE_READY)) {

                logger.debug("Reconnection request");
                setReconnectRequest(true);
            }

            controllerState.setState(state.getState());
        }

    }

    @Override
    public void resourceValueUpdateReceived(EventObject event, WSResourceValue value) {

        for (IhcBindingProvider provider : providers) {
            for (String itemName : provider.getItemNames()) {

                int resourceId = provider.getResourceIdForInBinding(itemName);

                if (value.getResourceID() == resourceId) {

                    if (!provider.hasInBinding(itemName)) {

                        logger.trace("{} has no inbinding...skip update to OpenHAB bus", itemName);

                    } else {

                        Class<? extends Item> itemType = provider.getItemType(itemName);
                        State state = IhcDataConverter.convertResourceValueToState(itemType, value);

                        logger.trace("Received resource value update (item='{}', state='{}')",
                                new Object[] { itemName, state });

                        eventPublisher.postUpdate(itemName, state);

                    }
                }

            }
        }
    }

    @Override
    public void errorOccured(EventObject event, IhcExecption e) {
        logger.warn("Error occurred on communication to IHC controller: {}", e.getMessage());

        logger.debug("Reconnection request");
        setReconnectRequest(true);
    }
}
