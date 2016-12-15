/**
 * Copyright (c) 2010-2016 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.io.openhabcloud.internal;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.openhab.core.events.AbstractEventSubscriber;
import org.openhab.core.events.EventPublisher;
import org.openhab.core.items.Item;
import org.openhab.core.items.ItemNotFoundException;
import org.openhab.core.library.items.RollershutterItem;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.UpDownType;
import org.openhab.core.scriptengine.action.ActionService;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.openhab.core.types.TypeParser;
import org.openhab.io.openhabcloud.NotificationAction;
import org.openhab.ui.items.ItemUIRegistry;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class starts openHAB Cloud connection service and implements interface to communicate with openHAB Cloud.
 * It also acts as a persistence service to send commands and updates for selected items to openHAB Cloud
 * and processes commands for items received from openHAB Cloud.
 *
 * @author Victor Belov
 * @since 1.3.0
 *
 */
public class CloudServiceImpl extends AbstractEventSubscriber
        implements NotificationAction, ActionService, CloudClientListener {

    private static Logger logger = LoggerFactory.getLogger(CloudServiceImpl.class);

    public static String myohVersion = "1.9.0";
    private CloudClient cloudClient;

    private static final String CFG_EXPOSE = "expose";
    private static final String CFG_BASE_URL = "baseURL";
    private static final String CFG_MODE = "mode";
    private static final String CFG_PORT = "localPort";
    private static final String STATIC_CONTENT_DIR = "webapps" + File.separator + "static";
    private static final String UUID_FILE_NAME = "uuid";
    private static final String SECRET_FILE_NAME = "secret";
    private static final String VERSION_FILE_NAME = "version";
    private static final String DEFAULT_URL = "https://myopenhab.org/";

    public static String clientVersion = null;
    private String cloudBaseUrl;
    private int mLocalPort = 8080;
    private boolean remoteAccessEnabled = true;
    private Set<String> exposedItems = null;

    protected ItemUIRegistry mItemUIRegistry = null;
    protected EventPublisher mEventPublisher = null;

    public CloudServiceImpl() {
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public void sendNotification(String userId, String message, String icon, String severity) {
        logger.debug("Sending message '{}' to user id {}", message, userId);
        cloudClient.sendNotification(userId, message, icon, severity);
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public void sendLogNotification(String message, String icon, String severity) {
        logger.debug("Sending log message '{}'", message);
        cloudClient.sendLogNotification(message, icon, severity);
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public void sendBroadcastNotification(String message, String icon, String severity) {
        logger.debug("Sending broadcast message '{}'", message);
        cloudClient.sendBroadcastNotification(message, icon, severity);
    }

    protected void activate(BundleContext context, Map<String, ?> config) {
        clientVersion = StringUtils.substringBefore(context.getBundle().getVersion().toString(), ".qualifier");
        checkJavaVersion();
        modified(config);
    }

    private void checkJavaVersion() {
        String version = System.getProperty("java.version");
        if (version.charAt(2) == '7') {
            logger.warn("You are running Java {} - the openhab Cloud connection only works with Java 8!", version);
        }
        	
        if (version.charAt(2) == '8') {
            // we are on Java 8, let's check the update
            String update = version.substring(version.indexOf('_') + 1);
            try {
                Integer uVersion = Integer.valueOf(update);
                if (uVersion < 101) {
                    logger.warn(
                            "You are running Java {} - the openhab Cloud connection requires at least Java 1.8.0_101, if your cloud server uses Let's Encrypt certificates!",
                            version);
                }
            } catch (NumberFormatException e) {
                logger.debug("Could not determine update version of java {}", version);
            }
        }
    }

    protected void deactivate() {
        logger.debug("openHAB Cloud connector deactivated");
        cloudClient.shutdown();
    }

    protected void modified(Map<String, ?> config) {
        if (config != null && config.get(CFG_MODE) != null) {
            remoteAccessEnabled = "remote".equals(config.get(CFG_MODE));
            logger.debug("remoteAccessEnabled set to '{}'", remoteAccessEnabled);
        } else {
            logger.debug("remoteAccessEnabled is not set, keeping value '{}'", remoteAccessEnabled);
        }

        String localPortString = (String) config.get(CFG_PORT);
        if (StringUtils.isNotBlank(localPortString)) {
            mLocalPort = Integer.valueOf(localPortString);
            logger.debug("local port set to '{}'", mLocalPort);
        }

        if (config.get(CFG_BASE_URL) != null) {
            cloudBaseUrl = (String) config.get(CFG_BASE_URL);
        } else {
            cloudBaseUrl = DEFAULT_URL;
        }
        logger.debug("cloudBaseUrl set to '{}'", cloudBaseUrl);

        Object expCfg = config.get(CFG_EXPOSE);
        if (expCfg instanceof String) {
            String value = (String) expCfg;
            while (value.startsWith("[")) {
                value = value.substring(1);
            }
            while (value.endsWith("]")) {
                value = value.substring(0, value.length() - 1);
            }
            exposedItems = new HashSet<>();
            for (String itemName : Arrays.asList((value).split(","))) {
                exposedItems.add(itemName.trim());
            }
            logger.debug("exposedItems set to '{}'", exposedItems);
        } else {
            exposedItems = null;
        }

        logger.debug("UUID = " + getUUID() + ", secret = " + getSecret());

        if (cloudClient != null) {
            cloudClient.shutdown();
        }

        cloudClient = new CloudClient(getUUID(), getSecret(), cloudBaseUrl, remoteAccessEnabled, exposedItems);
        if (mLocalPort != 8080) {
            cloudClient.setOHBaseUrl("http://localhost:" + String.valueOf(mLocalPort));
        }
        cloudClient.setOpenHABVersion(getVersion());
        cloudClient.connect();
        cloudClient.setListener(this);
        CloudService.notificationAction = this;
    }

    @Override
    public String getActionClassName() {
        return CloudService.class.getCanonicalName();
    }

    @Override
    public Class<?> getActionClass() {
        return CloudService.class;
    }

    /**
     * 
     */
    private String getVersion() {
        File file = new File(STATIC_CONTENT_DIR + File.separator + VERSION_FILE_NAME);
        String versionString = "";

        if (file.exists()) {
            versionString = readFirstLine(file);
            logger.debug("Version file found at '{}' with content '{}'", file.getAbsolutePath(), versionString);
        }
        return versionString;
    }

    /**
     * Gets UUID from file which is created by org.openhab.core.internal.CoreActivator
     */
    private String getUUID() {
        File file = new File(STATIC_CONTENT_DIR + File.separator + UUID_FILE_NAME);
        String uuidString = "";

        if (file.exists()) {
            uuidString = readFirstLine(file);
            logger.debug("UUID file found at '{}' with content '{}'", file.getAbsolutePath(), uuidString);
        }
        return uuidString;
    }

    /**
     * Reads the first line from specified file
     */

    private String readFirstLine(File file) {
        List<String> lines = null;
        try {
            lines = IOUtils.readLines(new FileInputStream(file));
        } catch (IOException ioe) {
            // no exception handling - we just return the empty String
        }
        return lines != null && lines.size() > 0 ? lines.get(0) : "";
    }

    /**
     * Writes a String to a specified file
     */

    private void writeFile(File file, String content) {
        // create intermediary directories
        file.getParentFile().mkdirs();
        try {
            IOUtils.write(content, new FileOutputStream(file));
            logger.debug("Created file '{}' with content '{}'", file.getAbsolutePath(), content);
        } catch (FileNotFoundException e) {
            logger.error("Couldn't create file '" + file.getPath() + "'.", e);
        } catch (IOException e) {
            logger.error("Couldn't write to file '" + file.getPath() + "'.", e);
        }
    }

    /**
     * Creates a random secret and writes it to the <code>webapps/static</code>
     * directory. An existing <code>secret</code> file won't be overwritten.
     * Returns either existing secret from the file or newly created secret.
     */

    private String getSecret() {
        File file = new File(STATIC_CONTENT_DIR + File.separator + SECRET_FILE_NAME);
        String newSecretString = "";

        if (!file.exists()) {
            newSecretString = RandomStringUtils.randomAlphanumeric(20);
            logger.debug("New password = " + newSecretString);
            writeFile(file, newSecretString);
        } else {
            newSecretString = readFirstLine(file);
            logger.debug("Secret file already exists at '{}' with content '{}'", file.getAbsolutePath(),
                    newSecretString);
        }

        return newSecretString;
    }

    @Override
    public void sendCommand(String itemName, String commandString) {
        try {
            if (this.mItemUIRegistry != null) {
                Item item = this.mItemUIRegistry.getItem(itemName);
                Command command = null;
                if (item != null) {
                    if (this.mEventPublisher != null) {
                        if ("toggle".equalsIgnoreCase(commandString)
                                && (item instanceof SwitchItem || item instanceof RollershutterItem)) {
                            if (OnOffType.ON.equals(item.getStateAs(OnOffType.class))) {
                                command = OnOffType.OFF;
                            }
                            if (OnOffType.OFF.equals(item.getStateAs(OnOffType.class))) {
                                command = OnOffType.ON;
                            }
                            if (UpDownType.UP.equals(item.getStateAs(UpDownType.class))) {
                                command = UpDownType.DOWN;
                            }
                            if (UpDownType.DOWN.equals(item.getStateAs(UpDownType.class))) {
                                command = UpDownType.UP;
                            }
                        } else {
                            command = TypeParser.parseCommand(item.getAcceptedCommandTypes(), commandString);
                        }
                        if (command != null) {
                            logger.debug("Received command {} for item {}", commandString, itemName);
                            this.mEventPublisher.postCommand(itemName, command);
                        } else {
                            logger.warn("Received invalid command {} for item {}", commandString, itemName);
                        }
                    }
                } else {
                    logger.warn("Received command {} for non existing item {}", commandString, itemName);
                }
            } else {
                return;
            }
        } catch (ItemNotFoundException e) {
            logger.warn("Received openHAB Cloud command for a non-existant item {}", itemName);
        }
    }

    public void setItemUIRegistry(ItemUIRegistry itemUIRegistry) {
        this.mItemUIRegistry = itemUIRegistry;
    }

    public void unsetItemUIRegistry(ItemUIRegistry itemUIRegistry) {
        this.mItemUIRegistry = null;
    }

    public void setEventPublisher(EventPublisher eventPublisher) {
        logger.debug("setEventPublisher");
        this.mEventPublisher = eventPublisher;
    }

    public void unsetEventPublisher(EventPublisher eventPublisher) {
        logger.debug("unsetEventPublisher");
        this.mEventPublisher = null;
    }

	@Override
	public void receiveUpdate(String itemName, State newStatus) {
        if (exposedItems != null && exposedItems.contains(itemName)) {
            logger.debug("Sending item update to openhab Cloud: {} - {}", itemName, newStatus);
            cloudClient.sendItemUpdate(itemName, newStatus.toString());
        }
	}
}
