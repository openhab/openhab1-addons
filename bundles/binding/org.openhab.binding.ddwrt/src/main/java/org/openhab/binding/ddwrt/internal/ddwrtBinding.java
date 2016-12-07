/**
 * Copyright (c) 2010-2016 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.ddwrt.internal;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.JobKey.jobKey;
import static org.quartz.TriggerBuilder.newTrigger;
import static org.quartz.TriggerKey.triggerKey;

import java.io.IOException;
import java.util.Dictionary;
import java.util.HashMap;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.net.telnet.TelnetClient;
import org.openhab.binding.ddwrt.ddwrtBindingProvider;
import org.openhab.core.binding.AbstractActiveBinding;
import org.openhab.core.binding.BindingProvider;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.items.StringItem;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.types.Command;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.TriggerKey;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The ddwrt binding connects to a AVM ddwrt on the monitor port 1012 and
 * listens to event notifications from this box. There are event for incoming
 * and outgoing calls, as well as for connections and disconnections.
 *
 * @author Kai Kreuzer
 * @author Jan N. Klug
 * @author Markus Eckhardt
 *
 * @since 1.9.0
 */
public class ddwrtBinding extends AbstractActiveBinding<ddwrtBindingProvider> implements ManagedService {

    private static HashMap<String, String> queryMap = new HashMap<>();

    // TODO: configurable?
    // daily cron schedule
    private final String cronSchedule = "0 0 0 * * ?";

    static {
        queryMap.put(ddwrtBindingProvider.TYPE_ROUTER_TYPE, "nvram get DD_BOARD");
        queryMap.put(ddwrtBindingProvider.TYPE_WLAN_24, "ifconfig");
        queryMap.put(ddwrtBindingProvider.TYPE_WLAN_50, "ifconfig");
        queryMap.put(ddwrtBindingProvider.TYPE_WLAN_GUEST, "ifconfig");
    }

    @Override
    public void bindingChanged(BindingProvider provider, String itemName) {
        super.bindingChanged(provider, itemName);

        conditionalDeActivate();

    }

    private void conditionalDeActivate() {
        logger.info("ddwrt conditional deActivate: {}", bindingsExist());

        if (bindingsExist()) {
            activate();
        } else {
            deactivate();
        }
    }

    private static final Logger logger = LoggerFactory.getLogger(ddwrtBinding.class);

    /* The IP address to connect to */
    protected static String ip;

    /* The port to connect to, default 23 */
    protected static String port;

    /* The password of the ddwrt to access via Telnet */
    protected static String password;

    /* The username, if used for telnet connections */
    protected static String username;

    /* The interface for 2.4 GHz network, e.g. ath0 */
    protected static String interface_24;

    /* The interface for 5.0 GHz network, e.g. ath1 */
    protected static String interface_50;

    /* The interface for guest network, e.g. virt_ath0 */
    protected static String interface_guest;

    /**
     * Reference to this instance to be used with the reconnection job which is
     * static.
     */
    private static ddwrtBinding INSTANCE;

    public ddwrtBinding() {
        INSTANCE = this;
    }

    @Override
    public void activate() {
        super.activate();
        setProperlyConfigured(true);
    }

    @Override
    public void deactivate() {
    }

    @Override
    public void internalReceiveCommand(String itemName, Command command) {

        if (password != null && !password.isEmpty()) {
            String type = null;
            for (ddwrtBindingProvider provider : providers) {
                type = provider.getType(itemName);
                if (type != null) {
                    break;
                }
            }

            logger.trace("ddwrt type: {}", type);

            if (type == null) {
                return;
            }

            TelnetCommandThread thread = new TelnetCommandThread(type, command);
            thread.start();

        }
    }

    protected void addBindingProvider(ddwrtBindingProvider bindingProvider) {
        super.addBindingProvider(bindingProvider);
    }

    protected void removeBindingProvider(ddwrtBindingProvider bindingProvider) {
        super.removeBindingProvider(bindingProvider);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("rawtypes")
    public void updated(Dictionary config) throws ConfigurationException {

        logger.trace("updated");
        if (config != null) {
            String ip = (String) config.get("ip");
            if (StringUtils.isNotBlank(ip)) {
                if (!ip.equals(ddwrtBinding.ip)) {
                    // only do something if the ip has changed
                    ddwrtBinding.ip = ip;
                    String port = (String) config.get("port");
                    if (!StringUtils.isNotBlank(port)) {
                        port = "23";
                    }
                    ddwrtBinding.port = port;
                    conditionalDeActivate();

                    // schedule a daily reconnection as sometimes the ddwrt
                    // stops sending data
                    // and thus blocks the monitor thread
                    try {
                        Scheduler sched = StdSchedulerFactory.getDefaultScheduler();

                        logger.trace("updated sched");
                        JobKey jobKey = jobKey("Reconnect", "ddwrt");
                        TriggerKey triggerKey = triggerKey("Reconnect", "ddwrt");

                        if (sched.checkExists(jobKey)) {
                            logger.debug("Daily reconnection job already exists");
                        } else {
                            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronSchedule);

                            JobDetail job = newJob(ReconnectJob.class).withIdentity(jobKey).build();

                            CronTrigger trigger = newTrigger().withIdentity(triggerKey).withSchedule(scheduleBuilder)
                                    .build();

                            sched.scheduleJob(job, trigger);
                            logger.debug("Scheduled a daily reconnection to ddwrt on {}:{}", ip, port);
                        }
                    } catch (SchedulerException e) {
                        logger.warn("Could not create daily reconnection job", e);
                    }
                }
            }
            String username = (String) config.get("username");
            if (StringUtils.isNotBlank(username)) {
                ddwrtBinding.username = username;
            }

            String password = (String) config.get("password");
            if (StringUtils.isNotBlank(password)) {
                ddwrtBinding.password = password;
            }

            String interface_24 = (String) config.get("interface_24");
            if (StringUtils.isNotBlank(interface_24)) {
                ddwrtBinding.interface_24 = interface_24;
            }

            String interface_50 = (String) config.get("interface_50");
            if (StringUtils.isNotBlank(interface_50)) {
                ddwrtBinding.interface_50 = interface_50;
            }

            String interface_guest = (String) config.get("interface_guest");
            if (StringUtils.isNotBlank(interface_guest)) {
                ddwrtBinding.interface_guest = interface_guest;
            }

        }
    }

    private static class TelnetCommandThread extends Thread {

        private static HashMap<String, String> commandMap = new HashMap<>();

        static {
            commandMap.put(ddwrtBindingProvider.TYPE_ROUTER_TYPE, "nvram get DD_BOARD");
            commandMap.put(ddwrtBindingProvider.TYPE_WLAN_24, "ifconfig");
            commandMap.put(ddwrtBindingProvider.TYPE_WLAN_50, "ifconfig");
            commandMap.put(ddwrtBindingProvider.TYPE_WLAN_GUEST, "ifconfig");
        }

        public TelnetCommandThread(String type, Command command) {
            super();
            this.type = type;
            this.command = command;
        }

        private String type;

        private Command command;

        @Override
        public void run() {
            try {
                TelnetClient client = new TelnetClient();
                logger.trace("TelnetCommandThread IP ({})", ip);
                client.connect(ip);

                String state = null;
                if (command == OnOffType.ON) {
                    state = "up";
                } else {
                    state = "down";
                }

                String cmdString = null;
                if (commandMap.containsKey(type)) {
                    if (type.startsWith("routertype")) {
                        cmdString = commandMap.get(type);
                    } else if (type.startsWith("wlan24") && !interface_24.isEmpty()) {
                        cmdString = commandMap.get(type) + " " + interface_24 + " " + state;
                    } else if (type.startsWith("wlan50") && !interface_50.isEmpty()) {
                        cmdString = commandMap.get(type) + " " + interface_50 + " " + state;
                    } else if (type.startsWith("wlanguest") && !interface_guest.isEmpty()) {
                        cmdString = commandMap.get(type) + " " + interface_guest + " " + state;
                    }
                }
                if (cmdString == null) {
                    return;
                }
                logger.trace("TelnetCommandThread command ({})", cmdString);

                /*
                 * This is a approach with receive/send in serial way. This
                 * could be done via a sperate thread but for just sending one
                 * command it is not necessary
                 */
                logger.trace("TelnetCommandThread Username ({})", username);
                if (username != null) {
                    receive(client); // user:
                    send(client, username);
                }
                receive(client); // password:
                logger.trace("TelnetCommandThread password ({})", password);
                send(client, password);
                receive(client); // welcome text
                send(client, cmdString);
                Thread.sleep(1000L); // response not needed - may be interesting
                                     // for reading status

                // There is a ddwrt problem on restarting of virtual networks. So we have to restart the lan service.
                if (type.startsWith("wlanguest") && !interface_guest.isEmpty() && command == OnOffType.ON) {
                    cmdString = "stopservice lan && startservice lan";
                    send(client, cmdString);
                    Thread.sleep(25000L); // response not needed but time for restarting
                }

                logger.trace("TelnetCommandThread ok send");
                client.disconnect();

            } catch (

            Exception e) {
                logger.warn("Error processing command", e);
            }
        }

        private void send(TelnetClient client, String data) {
            logger.trace("Sending data ({})...", data);
            try {
                data += "\r\n";
                client.getOutputStream().write(data.getBytes());
                client.getOutputStream().flush();
            } catch (IOException e) {
                logger.warn("Error sending data", e);
            }
        }

        private String receive(TelnetClient client) {
            StringBuffer strBuffer;
            try {
                strBuffer = new StringBuffer();

                byte[] buf = new byte[4096];
                int len = 0;

                Thread.sleep(750L);

                while ((len = client.getInputStream().read(buf)) != 0) {
                    strBuffer.append(new String(buf, 0, len));

                    Thread.sleep(750L);

                    if (client.getInputStream().available() == 0) {
                        break;
                    }
                }

                return strBuffer.toString();

            } catch (Exception e) {
                logger.warn("Error receiving data", e);
            }

            return null;
        }
    }

    /**
     * A quartz scheduler job to simply do a reconnection to the ddwrt.
     */
    public static class ReconnectJob implements Job {

        @Override
        public void execute(JobExecutionContext arg0) throws JobExecutionException {
            INSTANCE.conditionalDeActivate();
        }

    }

    @Override
    protected void execute() {

        logger.trace("execute");
        if (password == null) {
            return;
        } else if (password.trim().isEmpty()) {
            return;
        }

        try {
            TelnetClient client = null;

            for (ddwrtBindingProvider provider : providers) {
                for (String item : provider.getItemNames()) {
                    String query = null;

                    String type = provider.getType(item);
                    if (queryMap.containsKey(type)) {
                        if (type.startsWith("routertype")) {
                            query = queryMap.get(type);
                        } else if (type.startsWith("wlan24") && !interface_24.isEmpty()) {
                            query = queryMap.get(type) + " " + interface_24 + " | grep UP";
                        } else if (type.startsWith("wlan50") && !interface_50.isEmpty()) {
                            query = queryMap.get(type) + " " + interface_50 + " | grep UP";
                        } else if (type.startsWith("wlanguest") && !interface_guest.isEmpty()) {
                            query = queryMap.get(type) + " " + interface_guest + " | grep UP";
                        }
                    } else {
                        continue;
                    }
                    if (query == null) {
                        continue;
                    }
                    logger.trace("execute query ({})  ({}) ({})", query, ip, username);
                    if (client == null) {
                        client = new TelnetClient();
                        client.connect(ip);
                        if (username != null) {
                            receive(client);
                            send(client, username);
                        }
                        receive(client);
                        send(client, password);
                        receive(client);
                    }

                    send(client, query);

                    String answer = receive(client);
                    String[] lines = answer.split("\r\n");

                    if (lines.length >= 2) {
                        answer = lines[1].trim();
                    }

                    Class<? extends Item> itemType = provider.getItemType(item);

                    org.openhab.core.types.State state = null;

                    if (itemType.isAssignableFrom(SwitchItem.class)) {
                        if (lines.length > 2) {
                            if (lines[1].contains("UP")) {
                                state = OnOffType.ON;
                            } else {
                                state = OnOffType.OFF;
                            }
                        } else {
                            state = OnOffType.OFF;
                        }
                    } else if (itemType.isAssignableFrom(NumberItem.class)) {
                        state = new DecimalType(answer);
                    } else if (itemType.isAssignableFrom(StringItem.class)) {
                        state = new StringType(answer);
                    }

                    if (state != null) {
                        eventPublisher.postUpdate(item, state);
                    }

                }
            }
            if (client != null) {
                client.disconnect();
            }
        } catch (Exception e) {
            logger.warn("Could not get item state ", e);
        }

    }

    @Override
    protected long getRefreshInterval() {
        return 60000L;
    }

    @Override
    protected String getName() {
        return "ddwrt refresh Service";
    }

    /**
     * Send line via Telnet to ddwrt
     *
     * @param client
     *            the telnet client
     * @param data
     *            the data to send
     */
    private static void send(TelnetClient client, String data) {
        try {
            data += "\r\n";
            client.getOutputStream().write(data.getBytes());
            client.getOutputStream().flush();
        } catch (IOException e) {
            logger.warn("Error sending data", e);
        }
    }

    /**
     * Receive answer from ddwrt - careful! This blocks if there is no answer
     * from ddwrt
     *
     * @param client
     *            the telnet client
     * @return
     */
    private static String receive(TelnetClient client) {

        StringBuffer strBuffer;
        try {
            strBuffer = new StringBuffer();

            byte[] buf = new byte[4096];
            int len = 0;

            Thread.sleep(750L);

            while ((len = client.getInputStream().read(buf)) != 0) {
                strBuffer.append(new String(buf, 0, len));

                Thread.sleep(750L);

                if (client.getInputStream().available() == 0) {
                    break;
                }

            }

            return strBuffer.toString();

        } catch (Exception e) {
            logger.warn("Error receiving data", e);
        }

        return null;
    }

}
