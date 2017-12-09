/**
 * Copyright (c) 2010-2016 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.sonance.internal;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.openhab.binding.sonance.SonanceBindingProvider;
import org.openhab.core.binding.AbstractActiveBinding;
import org.openhab.core.binding.BindingProvider;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.IncreaseDecreaseType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.UpDownType;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Sonance Binding communicates with Sonance Amplifiers like the DSP 2-150,
 * DSP 8-130 and the DSP 2-750
 *
 * @author Laurens Van Acker
 * @since 1.8.0
 */
public class SonanceBinding extends AbstractActiveBinding<SonanceBindingProvider> {
    private Map<String, Socket> socketCache = new HashMap<String, Socket>();
    private Map<String, DataOutputStream> outputStreamCache = new HashMap<String, DataOutputStream>();
    private Map<String, BufferedReader> bufferedReaderCache = new HashMap<String, BufferedReader>();
    private static Pattern volumePattern = Pattern.compile(".*Vol=(-?\\d{1,2}).*");

    private static final Logger logger = LoggerFactory.getLogger(SonanceBinding.class);

    /**
     * the refresh interval which is used to poll values from the Sonance server
     * (optional, defaults to 60000ms)
     */
    private long refreshInterval = 60000;

    /**
     * Nothing happens in the constructor
     */
    public SonanceBinding() {
    }

    /**
     * Called by the SCR to activate the component with the refresh Interval. To
     * override the default refresh interval one has to add a parameter to
     * openhab.cfg like Sonance:refresh=<intervalInMs>.
     *
     * @param bundleContext
     *            BundleContext of the Bundle that defines this component
     * @param configuration
     *            Configuration properties for this component obtained from the
     *            ConfigAdmin service
     */
    public void activate(final BundleContext bundleContext, final Map<String, Object> configuration) {
        String refreshIntervalString = (String) configuration.get("refresh");
        if (StringUtils.isNotBlank(refreshIntervalString)) {
            refreshInterval = Long.parseLong(refreshIntervalString);
        }

        setProperlyConfigured(true);
    }

    /**
     * Deallocate socket connection, output stream and buffered reader caches
     *
     * @param reason
     *            Reason code for the deactivation:<br>
     *            <ul>
     *            <li>0 – Unspecified
     *            <li>1 – The component was disabled
     *            <li>2 – A reference became unsatisfied
     *            <li>3 – A configuration was changed
     *            <li>4 – A configuration was deleted
     *            <li>5 – The component was disposed
     *            <li>6 – The bundle was stopped
     *            </ul>
     */
    public void deactivate(final int reason) {
        socketCache.clear();
        outputStreamCache.clear();
        bufferedReaderCache.clear();
    }

    /*
     * {@inheritDoc}
     */
    @Override
    protected long getRefreshInterval() {
        return refreshInterval;
    }

    /*
     * {@inheritDoc}
     */
    @Override
    protected String getName() {
        return "Sonance Refresh Service";
    }

    /*
     * {@inheritDoc}
     */
    @Override
    protected void execute() {
        if (!bindingsExist()) {
            logger.debug("There is no existing Sonance binding configuration => refresh cycle aborted!");
            return;
        }

        logger.info("Refreshing all items");

        List<String> offlineEndPoints = new ArrayList<String>();

        for (SonanceBindingProvider provider : providers) {
            for (String itemName : provider.getItemNames()) {
                String group = provider.getGroup(itemName);
                String ip = provider.getIP(itemName);
                int port = provider.getPort(itemName);

                String key = ip + ":" + port;
                if (!offlineEndPoints.contains(key)) {
                    try {
                        if (!socketCache.containsKey(key)) {
                            socketCache.put(key, new Socket(ip, port));
                            outputStreamCache.put(key, new DataOutputStream(socketCache.get(key).getOutputStream()));
                            bufferedReaderCache.put(key,
                                    new BufferedReader(new InputStreamReader(socketCache.get(key).getInputStream())));
                            logger.debug("New socket created ({}:{})", ip, port);
                        }

                        if (provider.isMute(itemName)) {
                            sendMuteCommand(itemName, SonanceConsts.MUTE_QUERY + group, outputStreamCache.get(key),
                                    bufferedReaderCache.get(key));
                        } else if (provider.isVolume(itemName)) {
                            sendVolumeCommand(itemName, SonanceConsts.VOLUME_QUERY + group, outputStreamCache.get(key),
                                    bufferedReaderCache.get(key));
                        } else if (provider.isPower(itemName)) {
                            sendPowerCommand(itemName, SonanceConsts.POWER_QUERY, outputStreamCache.get(key),
                                    bufferedReaderCache.get(key));

                        }
                    } catch (UnknownHostException e) {
                        logger.error("UnknownHostException occurred when connecting to amplifier {}:{}.", ip, port);
                    } catch (IOException e) {
                        logger.debug("Amplifier ({},{}) is offline, status can't be updated at this moment.", ip, port);
                        try {
                            socketCache.get(key).close();
                        } catch (Exception ex) {
                        }
                        socketCache.remove(key);
                        outputStreamCache.remove(key);
                        bufferedReaderCache.remove(key);
                        offlineEndPoints.add(key); // Stop trying to fetch other
                                                   // values from this end
                                                   // point until next execute
                                                   // cycle
                    }
                }
            }
        }
    }

    protected void addBindingProvider(SonanceBindingProvider bindingProvider) {
        super.addBindingProvider(bindingProvider);
    }

    protected void removeBindingProvider(SonanceBindingProvider bindingProvider) {
        super.removeBindingProvider(bindingProvider);
    }

    /*
     * {@inheritDoc}
     */
    @Override
    public void bindingChanged(BindingProvider provider, String itemName) {
        super.bindingChanged(provider, itemName);

        for (Map.Entry<String, Socket> entry : socketCache.entrySet()) {
            try {
                entry.getValue().close();
            } catch (IOException e) {
                logger.error("Can't close a socket when binding changed.");
            }
        }

        // Cleanup all sockets
        socketCache.clear();
        outputStreamCache.clear();
        bufferedReaderCache.clear();
    }

    /*
     * {@inheritDoc}
     */
    @Override
    protected void internalReceiveCommand(String itemName, Command command) {
        logger.debug("Command received ({}, {})", itemName, command);

        SonanceBindingProvider provider = findFirstMatchingBindingProvider(itemName);
        String group = provider.getGroup(itemName);
        String ip = provider.getIP(itemName);
        int port = provider.getPort(itemName);

        Socket s = null;
        try {
            s = new Socket(ip, port);
            DataOutputStream outToServer = new DataOutputStream(s.getOutputStream());
            BufferedReader i = new BufferedReader(new InputStreamReader(s.getInputStream()));

            if (provider.isMute(itemName)) {
                if (command.equals(OnOffType.OFF)) {
                    sendMuteCommand(itemName, SonanceConsts.MUTE_ON + group, outToServer, i);
                } else if (command.equals(OnOffType.ON)) {
                    sendMuteCommand(itemName, SonanceConsts.MUTE_OFF + group, outToServer, i);
                } else {
                    logger.error("I don't know what to do with the command \"{}\"", command);
                }
            } else if (provider.isPower(itemName)) {
                if (command.equals(OnOffType.OFF)) {
                    sendPowerCommand(itemName, SonanceConsts.POWER_OFF, outToServer, i);
                } else if (command.equals(OnOffType.ON)) {
                    sendPowerCommand(itemName, SonanceConsts.POWER_ON, outToServer, i);
                } else {
                    logger.error("I don't know what to do with the command \"{}\"", command);
                }
            } else if (provider.isVolume(itemName)) {
                if (command.equals(UpDownType.UP)) {
                    sendVolumeCommand(itemName, SonanceConsts.VOLUME_UP + group, outToServer, i);
                } else if (command.equals(UpDownType.DOWN)) {
                    sendVolumeCommand(itemName, SonanceConsts.VOLUME_DOWN + group, outToServer, i);
                } else {
                    try {
                        Double d = Double.parseDouble(command.toString());
                        setVolumeCommand(itemName, group, d.intValue(), outToServer, i, ip + ":" + port);
                    } catch (NumberFormatException nfe) {
                        logger.error("I don't know what to do with the volume command \"{}\" ({})", command,
                                nfe.getMessage());
                    }
                }
            }
            s.close();
        } catch (IOException e) {
            logger.debug("IO Exception when sending command. Exception: {}", e.getMessage());
        } finally {
            closeSilently(s);
        }
    }

    /**
     * Closes a socket
     *
     * @param s
     *            socket to close
     */
    private void closeSilently(Socket s) {
        try {
            if (s != null) {
                s.close();
            }
        } catch (IOException e) {
        }
    }

    /*
     * {@inheritDoc}
     */
    @Override
    protected void internalReceiveUpdate(String itemName, State newState) {
        logger.debug("Update received ({},{})", itemName, newState);

        SonanceBindingProvider provider = findFirstMatchingBindingProvider(itemName);
        String group = provider.getGroup(itemName);
        String ip = provider.getIP(itemName);
        int port = provider.getPort(itemName);

        ip = null;
        group = null; // cleanup

        Socket s = null;
        try {
            s = new Socket(ip, port);
            DataOutputStream outToServer = new DataOutputStream(s.getOutputStream());
            BufferedReader i = new BufferedReader(new InputStreamReader(s.getInputStream()));

            if (provider.isMute(itemName)) {
                if (newState.equals(OnOffType.OFF)) {
                    sendMuteCommand(itemName, SonanceConsts.MUTE_ON + group, outToServer, i);
                } else if (newState.equals(OnOffType.ON)) {
                    sendMuteCommand(itemName, SonanceConsts.MUTE_OFF + group, outToServer, i);
                } else {
                    logger.error("I don't know what to do with this new state \"{}\"", newState);
                }
            }
            if (provider.isPower(itemName)) {
                if (newState.equals(OnOffType.OFF)) {
                    sendPowerCommand(itemName, SonanceConsts.POWER_OFF, outToServer, i);
                } else if (newState.equals(OnOffType.ON)) {
                    sendPowerCommand(itemName, SonanceConsts.POWER_ON, outToServer, i);
                } else {
                    logger.error("I don't know what to do with this new state \"{}\"", newState);
                }
            } else if (provider.isVolume(itemName)) {
                if (newState.equals(IncreaseDecreaseType.INCREASE)) {
                    sendVolumeCommand(itemName, SonanceConsts.VOLUME_UP + group, outToServer, i);
                } else if (newState.equals(IncreaseDecreaseType.DECREASE)) {
                    sendVolumeCommand(itemName, SonanceConsts.VOLUME_DOWN + group, outToServer, i);
                } else {
                    logger.error("I don't know what to do with this new state \"{}\"", newState);
                }
                s.close();
            }
        } catch (IOException e) {
            logger.error("IO Exception when received internal command. Message: {}", e.getMessage());
        } finally {
            closeSilently(s);
        }
    }

    /**
     * Send volume commands to groups (music zones)
     *
     * @param itemName
     *            item name to send update to
     * @param command
     *            Sonance IP code to execute
     * @param outToServer
     *            date output stream we can write to
     * @param i
     *            bufered reader where we can read from
     * @throws IOException
     *             throws an exception when we can't reach to amplifier
     */
    private void sendVolumeCommand(String itemName, String command, DataOutputStream outToServer, BufferedReader i)
            throws IOException {
        char[] cbuf = new char[50]; // Response is always 50 characters

        logger.debug("Sending volume command {}", command);

        outToServer.write(hexStringToByteArray(command));
        i.read(cbuf, 0, 50);

        Matcher m = volumePattern.matcher(new String(cbuf));

        if (m.find()) {
            String volume = m.group(1);
            eventPublisher.postUpdate(itemName, new DecimalType(volume));
            logger.debug("Setting volume for item {} on {}", itemName, volume);
        } else {
            logger.error("Error sending regular volume command {}, received this: {}", command, new String(cbuf));
        }
    }

    /**
     * Enable or disable specific groups (music zones)
     *
     * @param itemName
     *            item name to send update to
     * @param command
     *            Sonance IP code to execute
     * @param outToServer
     *            date output stream we can write to
     * @param i
     *            bufered reader where we can read from
     * @throws IOException
     *             throws an exception when we can't reach to amplifier
     */
    private void sendMuteCommand(String itemName, String command, DataOutputStream outToServer, BufferedReader i)
            throws IOException {
        char[] cbuf = new char[50]; // Response is always 50 characters

        logger.debug("Sending mute command {}", command);
        outToServer.write(hexStringToByteArray(command));
        i.read(cbuf, 0, 50);

        String result = new String(cbuf);

        logger.trace("Received this result: {}", result);

        if (result.contains("Mute=on") || result.contains("MuteOn")) {
            eventPublisher.postUpdate(itemName, OnOffType.OFF);
            logger.debug("Setting mute item {} on OFF", itemName);
        } else if (result.contains("Mute=off") || result.contains("MuteOff")) {
            eventPublisher.postUpdate(itemName, OnOffType.ON);
            logger.debug("Setting mute item {} on ON", itemName);
        } else {
            logger.error("Error sending mute command {}, received this: {}", command, result);
        }
    }

    /**
     * Wake up or put amplifier to sleep
     *
     * @param itemName
     *            item name to send update to
     * @param command
     *            Sonance IP code to execute
     * @param outToServer
     *            date output stream we can write to
     * @param i
     *            bufered reader where we can read from
     * @throws IOException
     *             throws an exception when we can't reach to amplifier
     */
    private void sendPowerCommand(String itemName, String command, DataOutputStream outToServer, BufferedReader i)
            throws IOException {
        char[] cbuf = new char[50]; // Response is always 50 characters

        logger.debug("Sending power command {}", command);
        outToServer.write(hexStringToByteArray(command));
        i.read(cbuf, 0, 50);

        String result = new String(cbuf);

        logger.trace("Received power response: {}", result);

        if (result.contains("Off")) {
            eventPublisher.postUpdate(itemName, OnOffType.OFF);
            logger.debug("Setting power item {} on OFF", itemName);
        } else if (result.contains("On")) {
            eventPublisher.postUpdate(itemName, OnOffType.ON);
            logger.debug("Setting power item {} on ON", itemName);
        } else {
            logger.trace("Error sending power command {}, received this: {}", command, result);
        }
    }

    /**
     * Sets the group to the specified target volume. Amplifier doesn't support
     * direct volume commands, so a loop is needed
     *
     * @param itemName
     *            item to publish result to
     * @param group
     *            target group
     * @param targetVolume
     *            target volume
     * @param outToServer
     *            data output stream where we can write to
     * @param i
     *            buffered reader where we can read from
     * @param endpoint
     *            ip:port
     * @throws IOException
     *             throws an IOException when we can't reach the amplifier
     */
    private void setVolumeCommand(String itemName, String group, int targetVolume, DataOutputStream outToServer,
            BufferedReader i, String endpoint) throws IOException {
        char[] cbuf = new char[50]; // Response is always 50 characters

        String question = String.format("%s%s%s", SonanceConsts.DIRECT_VOLUME_QUERY,
                Integer.toHexString(183 + targetVolume), group);
        logger.trace("Sending this to amplifier: {}", question);

        outToServer.write(hexStringToByteArray(question));
        i.read(cbuf, 0, 50);

        String result = new String(cbuf);
        logger.trace("Received this as response : {}", result);
        Matcher m = volumePattern.matcher(result);

        if (m.find()) {
            double currentVolume = Integer.parseInt(m.group(1));
            eventPublisher.postUpdate(itemName, new DecimalType(currentVolume));
            logger.debug("Updating {} with new volume {}", itemName, currentVolume);
        } else {
            logger.error("Error sending volume command, received this: {}", new String(cbuf));
        }
    }

    /**
     * Get binding provider for that item
     *
     * @param itemName
     *            name of the item where we need to binding provder for
     * @return SonanceBindingProvider
     */
    protected SonanceBindingProvider findFirstMatchingBindingProvider(String itemName) {
        SonanceBindingProvider firstMatchingProvider = null;
        for (SonanceBindingProvider provider : providers) {
            if (provider.providesBindingFor(itemName)) {
                firstMatchingProvider = provider;
                break;
            }
        }
        return firstMatchingProvider;
    }

    /**
     * Function to convert strings to hexadecimal bytes.
     *
     * @param s
     *            the string to convert to a hexadecimal byte array
     * @return hexadecimal byte array
     */
    private static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }
}
