/**
 * Copyright (c) 2010-2016 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.io.harmonyhub;

import static java.lang.String.format;
import static java.util.Collections.list;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.whistlingfish.harmony.HarmonyClient;
import net.whistlingfish.harmony.HarmonyHubListener;
import net.whistlingfish.harmony.protocol.LoginToken;

/**
 * Handles connection and event handling for all Harmony Hub devices.
 *
 * @author Matt Tucker
 * @author Dan Cunningham
 * @since 1.7.0
 */
public class HarmonyHubGateway implements ManagedService {
    private static final Logger logger = LoggerFactory.getLogger(HarmonyHubGateway.class);

    /**
     * Our internal mapping of hubs to qualifiers
     */
    private Map<String, HarmonyHubInstance> hubs = new HashMap<String, HarmonyHubInstance>();

    private Map<String, ScheduledFuture> reconnectJobs = new HashMap<String, ScheduledFuture>();

    ScheduledExecutorService reconnectService = Executors.newSingleThreadScheduledExecutor();

    /**
     * our internal mapping of hub listeners
     */
    private final List<HarmonyHubGatewayListener> hubListener = Collections
            .synchronizedList(new ArrayList<HarmonyHubGatewayListener>());

    /**
     * Matching pattern for config params (qualifier.host|username|password)
     */
    private static final Pattern CONFIG_PATTERN = Pattern.compile("((.*)\\.)?(host|username|password|discoveryName)");

    /**
     * Discover timeout
     */

    private static final int DISCO_TIME = 30;

    /**
     * are we configured correctly?
     */
    private volatile boolean properlyConfigured = false;

    /**
     * if no qualifier is given, we use this as a key
     */
    private static String NOQUALIFIER = "";

    /**
     * Bundle starting
     */
    public void activate() {
        logger.info("HarmonyHub gateway activated");
    }

    /**
     * Bundle stopping
     */
    public void deactivate() {
        logger.info("HarmonyHub gateway deactivated");
        removeAllClients();
    }

    /**
     * Are we configured correctly
     *
     * @return our configuration state
     */
    public boolean isProperlyConfigured() {
        return properlyConfigured;
    }

    /**
     * internal method to set our config state, we notify listeners of our
     * state change
     *
     * @param isConfigured
     */
    private synchronized void setProperlyConfigured(boolean isConfigured) {
        properlyConfigured = isConfigured;
        for (HarmonyHubGatewayListener l : hubListener) {
            l.configured(isConfigured);
        }
    }

    /**
     * Add listener who want to know our configured state
     *
     * @param listener
     */
    public synchronized void addHarmonyHubGatewayListener(HarmonyHubGatewayListener listener) {
        if (!hubListener.contains(listener)) {
            hubListener.add(listener);
            listener.configured(properlyConfigured);
        }
    }

    /**
     * Remove listener who no longer want to know our configured state
     *
     * @param listener
     */
    public synchronized void removeHarmonyHubGatewayListener(HarmonyHubGatewayListener listener) {
        hubListener.remove(listener);
    }

    /**
     * Remove all clients and clear our map
     */
    private synchronized void removeAllClients() {
        for (String qualifier : hubs.keySet()) {
            HarmonyClient c = hubs.get(qualifier).getClient();
            c.disconnect();
        }
        hubs.clear();
    }

    @Override
    public synchronized void updated(Dictionary<String, ?> config) throws ConfigurationException {
        if (config != null) {
            final Map<String, HostConfig> hostConfigs = new HashMap<String, HostConfig>();

            for (String key : list(config.keys())) {
                String value = (String) config.get(key);
                Matcher m = CONFIG_PATTERN.matcher(key);
                if (!m.matches()) {
                    continue;
                }
                String qualifier = checkQualifier(m.group(2));

                String configKey = m.group(3);

                HostConfig hostConfig = hostConfigs.get(qualifier);
                if (hostConfig == null) {
                    hostConfig = new HostConfig();
                    hostConfigs.put(qualifier, hostConfig);
                }
                if (configKey.equals("host")) {
                    hostConfig.setHost(value);
                } else if (configKey.equals("username")) {
                    hostConfig.setUsername(value);
                } else if (configKey.equals("discoveryName")) {
                    hostConfig.setDiscoveryName(value);
                } else if (configKey.equals("password")) {
                    hostConfig.setPassword(value);
                }
            }

            setProperlyConfigured(false);
            // clean up existing clients
            removeAllClients();
            // don't block OH startup as this may take a bit
            new Thread(new Runnable() {
                @Override
                public void run() {
                    for (Entry<String, HostConfig> entry : hostConfigs.entrySet()) {
                        final String qualifier = entry.getKey();
                        final HostConfig hostConfig = entry.getValue();
                        connect(qualifier, hostConfig);
                    }
                }
            }).start();
        }
    }

    private void connect(final String qualifier, final HostConfig hostConfig) {
        if (hostConfig.useDiscovery()) {
            setProperlyConfigured(true);
            final HarmonyHubDiscovery disco = new HarmonyHubDiscovery(DISCO_TIME, hostConfig.getHost());
            disco.addListener(new HarmonyHubDiscoveryListener() {

                @Override
                public void hubDiscoveryFinished() {
                    logger.warn("Could not find a HarmonyHub with the discovery name {}",
                            hostConfig.getDiscoveryName());
                    scheduleConnect(qualifier, hostConfig);
                }

                @Override
                public void hubDiscovered(HarmonyHubDiscoveryResult result) {
                    logger.debug("Found HarmonyHub with discoveryName {} looking for {}", result.getFriendlyName(),
                            hostConfig.getDiscoveryName());
                    if (result.getFriendlyName().toLowerCase().equals(hostConfig.getDiscoveryName().toLowerCase())) {
                        disco.removeListener(this);
                        connectClient(qualifier, result.getHost(), result.getAccountId(), result.getSessionID());
                    }

                }
            });
            disco.startDiscovery();
        } else if (hostConfig.useCredentials()) {
            setProperlyConfigured(true);
            connectClient(qualifier, hostConfig);
        } else {
            logger.error("Config must have either a discoveryName or host/username/password");
        }
    }

    private void scheduleConnect(final String qualifier, final HostConfig hostConfig) {
        synchronized (reconnectJobs) {
            if (reconnectJobs.containsKey(qualifier)) {
                ScheduledFuture<?> job = reconnectJobs.remove(qualifier);
                job.cancel(true);
            }
            reconnectJobs.put(qualifier, reconnectService.schedule(new Runnable() {
                @Override
                public void run() {
                    connect(qualifier, hostConfig);
                }
            }, 30, TimeUnit.SECONDS));
        }
    }

    /**
     * Connects a client and adds to our map
     *
     * @param qualifier
     * @param hostConfig
     */
    private synchronized void connectClient(String qualifier, HostConfig hostConfig) {
        try {
            if (hubs.containsKey(qualifier)) {
                HarmonyHubInstance instance = hubs.get(qualifier);
                instance.client.disconnect();
                hubs.remove(qualifier);
            }
            HarmonyClient harmonyClient = HarmonyClient.getInstance();
            logger.debug("Connecting {} to {} with user {}", qualifier, hostConfig.getHost(), hostConfig.getUsername());
            harmonyClient.connect(hostConfig.getHost(), hostConfig.getUsername(), hostConfig.getPassword());
            hubs.put(qualifier, new HarmonyHubInstance(harmonyClient));
            logger.debug("Devices for qualifier {}\n{}", qualifier, harmonyClient.getDeviceLabels().toString());
            logger.debug("Activity for qualifier {}\n{}", qualifier, harmonyClient.getConfig().getActivities());
            logger.debug("Config for qualifier {}\n{}", qualifier, harmonyClient.getConfig().toJson());
        } catch (Exception e) {
            logger.error(format(//
                    "Failed creating harmony hub connection to %s", hostConfig.getHost()), e);
        }
    }

    /**
     * Connects a client and adds to our map
     *
     * @param qualifier
     * @param hostConfig
     */
    private synchronized void connectClient(String qualifier, String host, String accountId, String sessionId) {
        try {
            if (hubs.containsKey(qualifier)) {
                HarmonyHubInstance instance = hubs.get(qualifier);
                instance.client.disconnect();
                hubs.remove(qualifier);
            }
            HarmonyClient harmonyClient = HarmonyClient.getInstance();
            logger.debug("Connecting {} to {} using discovery tokens", qualifier, host);
            harmonyClient.connect(host, new LoginToken(accountId, sessionId));
            hubs.put(qualifier, new HarmonyHubInstance(harmonyClient));
            logger.debug("Devices for qualifier {}\n{}", qualifier, harmonyClient.getDeviceLabels().toString());
            logger.debug("Activity for qualifier {}\n{}", qualifier, harmonyClient.getConfig().getActivities());
            logger.debug("Config for qualifier {}\n{}", qualifier, harmonyClient.getConfig().toJson());
        } catch (Exception e) {
            logger.error(format(//
                    "Failed creating harmony hub connection to %s", host), e);
        }
    }

    protected interface ClientRunnable {
        void run(HarmonyClient client);
    }

    /**
     * Look up a client by its qualifier and have it run our runnable
     *
     * @param qualifier
     * @param runnable
     */
    private void withClient(String qualifier, final ClientRunnable runnable) {
        final HarmonyHubInstance hub = hubs.get(qualifier);
        logger.debug("running for qualifier {} and client {}", qualifier, hub);
        if (hub == null) {
            throw new IllegalArgumentException(format("No client '%s' defined", qualifier));
        }
        hub.execute(runnable);
    }

    /**
     * Simulates pressing a button on a harmony remote
     *
     * @param deviceId
     * @param button
     */
    public void pressButton(final int deviceId, final String button) {
        pressButton(null, deviceId, button);
    }

    /**
     * Simulates pressing a button on a harmony remote
     *
     * @param qualifier
     * @param deviceId
     * @param button
     */
    public void pressButton(String qualifier, final int deviceId, final String button) {
        logger.debug("pressButton for qualifer {} deviceId {} and button {}", qualifier, deviceId, button);
        if (!properlyConfigured) {
            throw new IllegalStateException(
                    "Harmony Hub Gateway is not properly configured, or the connection is not yet started");
        }
        withClient(checkQualifier(qualifier), new ClientRunnable() {
            @Override
            public void run(HarmonyClient client) {
                client.pressButton(deviceId, button);
            }
        });
    }

    /**
     * Simulates pressing a button on a harmony remote
     *
     * @param device
     * @param button
     */
    public void pressButton(final String device, final String button) {
        pressButton(null, device, button);
    }

    /**
     * Simulates pressing a button on a harmony remote
     *
     * @param qualifier
     * @param device
     * @param button
     */
    public void pressButton(String qualifier, final String device, final String button) {
        logger.debug("pressButton for qualifer {} device {} and button {}", qualifier, device, button);

        if (!properlyConfigured) {
            throw new IllegalStateException(
                    "Harmony Hub Gateway is not properly configured, or the connection is not yet started");
        }
        withClient(checkQualifier(qualifier), new ClientRunnable() {
            @Override
            public void run(HarmonyClient client) {
                try {
                    client.pressButton(Integer.parseInt(device), button);
                } catch (NumberFormatException e) {
                    client.pressButton(device, button);
                }
            }
        });
    }

    /**
     * Starts a Harmony Hub activity
     *
     * @param activityId
     */
    public void startActivity(final int activityId) {
        startActivity(null, activityId);
    }

    /**
     * Starts a Harmony Hub activity
     *
     * @param qualifier
     * @param activityId
     */
    public void startActivity(String qualifier, final int activityId) {
        logger.debug("startActivity for qualifer {} and activityId {}", qualifier, activityId);
        if (!properlyConfigured) {
            throw new IllegalStateException(
                    "Harmony Hub Gateway is not properly configured, or the connection is not yet started");
        }
        withClient(checkQualifier(qualifier), new ClientRunnable() {
            @Override
            public void run(HarmonyClient client) {
                client.startActivity(activityId);
            }
        });
    }

    /**
     * Starts a Harmony Hub activity
     *
     * @param activity
     */
    public void startActivity(final String activity) {
        startActivity(null, activity);
    }

    /**
     * Starts a Harmony Hub activity
     *
     * @param qualifier
     * @param activity
     */
    public void startActivity(String qualifier, final String activity) {
        logger.debug("startActivity for qualifer {} and activity {}", qualifier, activity);
        if (!properlyConfigured) {
            throw new IllegalStateException(
                    "Harmony Hub Gateway is not properly configured, or the connection is not yet started");
        }
        withClient(checkQualifier(qualifier), new ClientRunnable() {
            @Override
            public void run(HarmonyClient client) {
                try {
                    client.startActivity(Integer.parseInt(activity));
                } catch (NumberFormatException e) {
                    client.startActivityByName(activity);
                }
            }
        });
    }

    /**
     * Adds a {@link HarmonyHubListener} to a {@link HarmonyClient}
     *
     * @param listener
     */
    public void addListener(final HarmonyHubListener listener) {
        addListener(null, listener);
    }

    /**
     * Adds a {@link HarmonyHubListener} to a {@link HarmonyClient}
     *
     * @param qualifier
     * @param listener
     */
    public void addListener(String qualifier, final HarmonyHubListener listener) {
        withClient(checkQualifier(qualifier), new ClientRunnable() {
            @Override
            public void run(HarmonyClient client) {
                client.addListener(listener);
            }
        });
    }

    /**
     * Removes a {@link HarmonyHubListener} from a {@link HarmonyClient}
     *
     * @param listener
     */
    public void removeListener(final HarmonyHubListener listener) {
        removeListener(null, listener);
    }

    /**
     * Removes a {@link HarmonyHubListener} from a {@link HarmonyClient}
     *
     * @param qualifier
     * @param listener
     */
    public void removeListener(String qualifier, final HarmonyHubListener listener) {
        withClient(checkQualifier(qualifier), new ClientRunnable() {
            @Override
            public void run(HarmonyClient client) {
                client.removeListener(listener);
            }
        });
    }

    /**
     * If no qualifier is given we will use the default {@link NOQUALIFIER}
     *
     * @param qualifier
     * @return original qualifier or {@link NOQUALIFIER}
     */
    private String checkQualifier(String qualifier) {
        return qualifier == null ? NOQUALIFIER : qualifier;
    }

    /**
     * HarmonyHubInstance holds a {@link HarmonyClient} and a {@link ExecutorService} together
     * so that commands to one hub do not block commands to another or stop the main OH
     * processing thread.
     *
     * @author Dan Cunningham
     *
     */
    class HarmonyHubInstance {
        HarmonyClient client;
        /**
         * execute commmands in their own threads as they could block for some time
         */
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        /**
         * Creates a new HarmonyHubInstance from a given client
         *
         * @param client
         */
        public HarmonyHubInstance(HarmonyClient client) {
            super();
            this.client = client;
        }

        /**
         * Returns the HarmonyClient assocaited with this instance
         *
         * @return
         */
        public HarmonyClient getClient() {
            return client;
        }

        /**
         * Executes the {@link ClientRunnable} in this hubs {@link ExecutorService} and with
         * the associated {@link HarmonyClient}
         *
         * @param clientRunable
         */
        public void execute(final ClientRunnable clientRunable) {
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    clientRunable.run(client);
                }
            });
        }
    }
}
