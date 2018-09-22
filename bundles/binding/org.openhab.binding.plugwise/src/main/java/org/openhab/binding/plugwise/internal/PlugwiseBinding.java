/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.plugwise.internal;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;
import static org.quartz.impl.matchers.GroupMatcher.jobGroupEquals;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.IllegalClassException;
import org.apache.commons.lang.ObjectUtils;
import org.joda.time.DateTime;
import org.openhab.binding.plugwise.PlugwiseBindingProvider;
import org.openhab.binding.plugwise.PlugwiseCommandType;
import org.openhab.binding.plugwise.internal.CirclePlus.SetClockJob;
import org.openhab.binding.plugwise.internal.PlugwiseGenericBindingProvider.PlugwiseBindingConfigElement;
import org.openhab.core.binding.AbstractActiveBinding;
import org.openhab.core.library.types.DateTimeType;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.openhab.core.types.Type;
import org.openhab.core.types.TypeParser;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Main binding class
 *
 * @author Karel Goderis
 * @since 1.1.0
 */
public class PlugwiseBinding extends AbstractActiveBinding<PlugwiseBindingProvider> implements ManagedService {

    public static final String STICK_JOB_DATA_KEY = "Stick";

    public static final String MAC_JOB_DATA_KEY = "MAC";

    private static final Logger logger = LoggerFactory.getLogger(PlugwiseBinding.class);

    private static final Pattern EXTRACT_PLUGWISE_CONFIG_PATTERN = Pattern
            .compile("^(.*?)\\.(mac|type|port|interval)$");

    /** the refresh interval which is used to check for changes in the binding configurations */
    private static long refreshInterval = 5000;

    private Stick stick;

    protected void addBindingProvider(PlugwiseBindingProvider bindingProvider) {
        super.addBindingProvider(bindingProvider);
    }

    protected void removeBindingProvider(PlugwiseBindingProvider bindingProvider) {
        super.removeBindingProvider(bindingProvider);
    }

    @Override
    public void updated(Dictionary<String, ?> config) throws ConfigurationException {

        if (config == null) {
            return;
        }

        validateKeyPatternsInConfig(config);

        stick = setupStick(config);

        if (stick != null) {
            setupNonStickDevices(config);
            stick.startBackgroundThreads();
            setProperlyConfigured(true);
        } else {
            logger.warn("Plugwise needs at least one Stick in order to operate");
        }

    }

    private Stick setupStick(Dictionary<String, ?> config) {

        String port = ObjectUtils.toString(config.get("stick.port"), null);

        if (port == null) {
            return null;
        }

        Stick stick = new Stick(port, this);
        logger.debug("Plugwise added Stick connected to serial port {}", port);

        String interval = ObjectUtils.toString(config.get("stick.interval"), null);
        if (interval != null) {
            stick.setInterval(Integer.valueOf(interval));
            logger.debug("Setting the interval to send ZigBee PDUs to {} ms", interval);
        }

        String retries = ObjectUtils.toString(config.get("stick.retries"), null);
        if (retries != null) {
            stick.setRetries(Integer.valueOf(retries));
            logger.debug("Setting the maximum number of attempts to send a message to ", retries);
        }

        return stick;
    }

    private void setupNonStickDevices(Dictionary<String, ?> config) {

        Set<String> deviceNames = getDeviceNamesFromConfig(config);

        for (String deviceName : deviceNames) {
            if ("stick".equals(deviceName)) {
                continue;
            }

            if (stick.getDeviceByName(deviceName) != null) {
                continue;
            }

            String MAC = ObjectUtils.toString(config.get(deviceName + ".mac"), null);
            if (MAC == null || MAC.equals("")) {
                logger.warn("Plugwise cannot add device with name {} without a MAC address", deviceName);
            } else if (stick.getDeviceByMAC(MAC) != null) {
                logger.warn(
                        "Plugwise cannot add device with name: {} and MAC address: {}, "
                                + "the same MAC address is already used by device with name: {}",
                        deviceName, MAC, stick.getDeviceByMAC(MAC).name);
            } else {
                String deviceType = ObjectUtils.toString(config.get(deviceName + ".type"), null);
                PlugwiseDevice device = createPlugwiseDevice(deviceType, MAC, deviceName);

                if (device != null) {
                    stick.addDevice(device);
                }
            }

        }

    }

    private PlugwiseDevice createPlugwiseDevice(String deviceType, String MAC, String deviceName) {

        PlugwiseDevice device = null;

        if ("circleplus".equals(deviceType) || "circleplus".equals(deviceName)) {
            // for backwards compatibility a device with the name 'circleplus' always creates a CirclePlus
            device = new CirclePlus(MAC, stick, deviceName);
            logger.debug("Plugwise created Circle+ with name: {} and MAC address: {}", deviceName, MAC);
        } else if ("circle".equals(deviceType) || deviceType == null) {
            // for backwards compatibility a device without a deviceType always creates a Circle
            device = new Circle(MAC, stick, deviceName);
            logger.debug("Plugwise created Circle with name: {} and MAC address: {}", deviceName, MAC);
        } else if ("scan".equals(deviceType)) {
            device = new Scan(MAC, stick, deviceName);
            logger.debug("Plugwise created Scan with name: {} and MAC address: {}", deviceName, MAC);
        } else if ("sense".equals(deviceType)) {
            device = new Sense(MAC, stick, deviceName);
            logger.debug("Plugwise created Sense with name: {} and MAC address: {}", deviceName, MAC);
        } else if ("stealth".equals(deviceType)) {
            device = new Stealth(MAC, stick, deviceName);
            logger.debug("Plugwise created Stealth with name: {} and MAC address: {}", deviceName, MAC);
        } else if ("switch".equals(deviceType)) {
            device = new Switch(MAC, stick, deviceName);
            logger.debug("Plugwise created Switch with name: {} and MAC address: {}", deviceName, MAC);
        } else {
            logger.warn(
                    "Plugwise cannot create device with name: '{}' because it has an unknown device type: '{}'. "
                            + "Known device types are: circle|circleplus|scan|sense|stealth|switch",
                    deviceName, deviceType);
        }
        return device;
    }

    private Set<String> getDeviceNamesFromConfig(Dictionary<String, ?> config) {

        Set<String> names = new HashSet<String>();

        Enumeration<String> keys = config.keys();
        while (keys.hasMoreElements()) {

            String key = keys.nextElement();

            // the config-key enumeration contains additional keys that we
            // don't want to process here ...
            if ("service.pid".equals(key)) {
                continue;
            }

            Matcher matcher = EXTRACT_PLUGWISE_CONFIG_PATTERN.matcher(key);
            if (!matcher.matches()) {
                continue;
            }

            matcher.reset();
            matcher.find();

            String name = matcher.group(1);
            names.add(name);
        }

        return names;
    }

    private void validateKeyPatternsInConfig(Dictionary<String, ?> config) {

        Enumeration<String> keys = config.keys();
        while (keys.hasMoreElements()) {

            String key = keys.nextElement();

            // the config-key enumeration contains additional keys that we
            // don't want to process here ...
            if ("service.pid".equals(key)) {
                continue;
            }

            Matcher matcher = EXTRACT_PLUGWISE_CONFIG_PATTERN.matcher(key);
            if (!matcher.matches()) {
                logger.warn("Given plugwise-config-key '" + key
                        + "' does not follow the expected pattern '<PlugwiseId>.<mac|type|port|interval>'");
                continue;
            }
        }

    }

    @Override
    public void activate() {
        // Nothing to do here. We start the binding when the first item bindigconfig is processed
    }

    @Override
    public void deactivate() {

        if (stick != null) {
            // unschedule all the quartz jobs
            try {
                Scheduler sched = StdSchedulerFactory.getDefaultScheduler();
                for (PlugwiseBindingProvider provider : providers) {
                    try {
                        for (JobKey jobKey : sched.getJobKeys(jobGroupEquals("Plugwise-" + provider.toString()))) {
                            sched.deleteJob(jobKey);
                        }
                    } catch (SchedulerException e) {
                        logger.error("An exception occurred while deleting the Plugwise Quartz jobs ({})",
                                e.getMessage());
                    }
                }
            } catch (SchedulerException e) {
                logger.error("An exception occurred while getting a reference to the Quartz Scheduler ({})",
                        e.getMessage());
            }

            stick.close();
        }

    }

    @Override
    protected void internalReceiveCommand(String itemName, Command command) {

        PlugwiseBindingProvider provider = findFirstMatchingBindingProvider(itemName);

        if (command != null) {

            String commandAsString = command.toString();
            List<Command> commands = new ArrayList<Command>();

            // check if the command is valid for this item by checking if a pw ID exists
            String checkID = provider.getPlugwiseID(itemName, command);

            if (checkID != null) {
                commands.add(command);
            } else {
                // ooops - command is not defined, but maybe we have something of the same Type (e.g Decimal, String
                // types)
                // commands = provider.getCommandsByType(itemName, command.getClass());
                commands = provider.getAllCommands(itemName);
            }

            for (Command someCommand : commands) {

                String plugwiseID = provider.getPlugwiseID(itemName, someCommand);
                PlugwiseCommandType plugwiseCommandType = provider.getPlugwiseCommandType(itemName, someCommand);

                if (plugwiseID != null) {
                    if (plugwiseCommandType != null) {
                        @SuppressWarnings("unused")
                        boolean result = executeCommand(plugwiseID, plugwiseCommandType, commandAsString);

                        // Each command is responsible to make sure that a result value for the action is polled from
                        // the device
                        // which then will be used to do a postUpdate

                        // if new commands would be added later on that do not have this possibility, then a kind of
                        // auto-update has to be performed here below

                    } else {
                        logger.error("wrong command type for binding [Item={}, command={}]", itemName, commandAsString);
                    }
                } else {
                    logger.error("{} is an unrecognised command for Item {}", commandAsString, itemName);
                }
            }
        }
    }

    private boolean executeCommand(String plugwiseID, PlugwiseCommandType plugwiseCommandType, String commandAsString) {

        boolean result = false;

        if (plugwiseID != null) {
            PlugwiseDevice plug = stick.getDevice(plugwiseID);

            if (plug != null) {
                switch (plugwiseCommandType) {
                    case CURRENTSTATE:
                        if (plug instanceof Circle) {
                            result = ((Circle) plug).setPowerState(commandAsString);
                        }
                    default:
                        break;
                }

            } else {
                logger.error("Plugwise device is not defined for device with ID {}", plugwiseID);
            }
        }
        return result;
    }

    /**
     * Method to post updates to the OH runtime.
     *
     *
     * @param MAC of the Plugwise device concerned
     * @param ctype is the Plugwise Command type
     * @param value is the value (to be converted) to post
     */
    public void postUpdate(String MAC, PlugwiseCommandType ctype, Object value) {

        if (MAC != null && ctype != null && value != null) {

            for (PlugwiseBindingProvider provider : providers) {

                Set<String> qualifiedItems = provider.getItemNames(MAC, ctype);
                // Make sure we also capture those devices that were pre-defined with a friendly name in a .cfg or alike
                Set<String> qualifiedItemsFriendly = provider.getItemNames(stick.getDevice(MAC).getName(), ctype);
                qualifiedItems.addAll(qualifiedItemsFriendly);

                State type = null;
                try {
                    type = createStateForType(ctype, value);
                } catch (BindingConfigParseException e) {
                    logger.error("Error parsing a value {} to a state variable of type {}", value.toString(),
                            ctype.getTypeClass().toString());
                }

                for (String item : qualifiedItems) {
                    if (type instanceof State) {
                        eventPublisher.postUpdate(item, type);
                    } else {
                        throw new IllegalClassException(
                                "Cannot process update of type " + (type == null ? "null" : type.toString()));
                    }
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    private State createStateForType(PlugwiseCommandType ctype, Object value) throws BindingConfigParseException {

        Class<? extends Type> typeClass = ctype.getTypeClass();

        // the logic below covers all possible command types and value types
        if (typeClass == DecimalType.class && value instanceof Float) {
            return new DecimalType((Float) value);
        } else if (typeClass == DecimalType.class && value instanceof Double) {
            return new DecimalType((Double) value);
        } else if (typeClass == OnOffType.class && value instanceof Boolean) {
            return ((Boolean) value).booleanValue() ? OnOffType.ON : OnOffType.OFF;
        } else if (typeClass == DateTimeType.class && value instanceof Calendar) {
            return new DateTimeType((Calendar) value);
        } else if (typeClass == DateTimeType.class && value instanceof DateTime) {
            return new DateTimeType(((DateTime) value).toCalendar(Locale.getDefault()));
        } else if (typeClass == StringType.class && value instanceof String) {
            return new StringType((String) value);
        }

        logger.debug("less efficient (generic) logic is applied for converting a Plugwise value "
                + "(command type class: {}, value class {})", typeClass.getName(), value.getClass().getName());
        List<Class<? extends State>> stateTypeList = new ArrayList<Class<? extends State>>();
        stateTypeList.add((Class<? extends State>) typeClass);
        return TypeParser.parseState(stateTypeList, value.toString());
    }

    /**
     * Find the first matching {@link PlugwiseBindingProvider}
     * according to <code>itemName</code>
     *
     * @param itemName
     *
     * @return the matching binding provider or <code>null</code> if no binding
     *         provider could be found
     */
    protected PlugwiseBindingProvider findFirstMatchingBindingProvider(String itemName) {
        PlugwiseBindingProvider firstMatchingProvider = null;
        for (PlugwiseBindingProvider provider : providers) {
            List<String> plugwiseIDs = provider.getPlugwiseID(itemName);
            if (plugwiseIDs != null && plugwiseIDs.size() > 0) {
                firstMatchingProvider = provider;
                break;
            }
        }
        return firstMatchingProvider;
    }

    @Override
    protected void execute() {
        if (isProperlyConfigured()) {
            try {
                Scheduler sched = StdSchedulerFactory.getDefaultScheduler();
                scheduleJobs(sched);
            } catch (SchedulerException e) {
                logger.error("An exception occurred while getting a reference to the Quartz Scheduler ({})",
                        e.getMessage());
            }
        }
    }

    private void scheduleJobs(Scheduler scheduler) {

        for (PlugwiseBindingProvider provider : providers) {

            for (PlugwiseBindingConfigElement element : provider.getIntervalList()) {
                PlugwiseCommandType type = element.getCommandType();

                if (type.getJobClass() == null) {
                    continue;
                }

                // check if the device already exists (via cfg definition of Role Call)

                if (stick.getDevice(element.getId()) == null) {
                    logger.debug("The Plugwise device with id {} is not yet defined", element.getId());

                    // check if the config string really contains a MAC address
                    Pattern MAC_PATTERN = Pattern.compile("(\\w{16})");
                    Matcher matcher = MAC_PATTERN.matcher(element.getId());
                    if (matcher.matches()) {
                        List<CirclePlus> cps = stick.getDevicesByClass(CirclePlus.class);
                        if (!cps.isEmpty()) {
                            CirclePlus cp = cps.get(0);
                            if (!cp.getMAC().equals(element.getId())) {
                                // a circleplus has been added/detected and it is not what is in the binding config
                                PlugwiseDevice device = new Circle(element.getId(), stick, element.getId());
                                stick.addDevice(device);
                                logger.debug("Plugwise added Circle with MAC address: {}", element.getId());
                            }
                        } else {
                            logger.warn(
                                    "Plugwise cannot guess the device that should be added. Consider defining it in the openHAB configuration file");
                        }
                    } else {
                        logger.warn(
                                "Plugwise cannot add a valid device without a proper MAC address. {} cannot be used",
                                element.getId());
                    }
                }

                if (stick.getDevice(element.getId()) != null) {

                    String jobName = element.getId() + "-" + type.getJobClass().toString();

                    if (!isExistingJob(scheduler, jobName)) {
                        // set up the Quartz jobs
                        JobDataMap map = new JobDataMap();
                        map.put(STICK_JOB_DATA_KEY, stick);
                        map.put(MAC_JOB_DATA_KEY, stick.getDevice(element.getId()).MAC);

                        JobDetail job = newJob(type.getJobClass())
                                .withIdentity(jobName, "Plugwise-" + provider.toString()).usingJobData(map).build();

                        Trigger trigger = newTrigger()
                                .withIdentity(element.getId() + "-" + type.getJobClass().toString(),
                                        "Plugwise-" + provider.toString())
                                .startNow()
                                .withSchedule(
                                        simpleSchedule().repeatForever().withIntervalInSeconds(element.getInterval()))
                                .build();

                        try {
                            scheduler.scheduleJob(job, trigger);
                        } catch (SchedulerException e) {
                            logger.error("An exception occurred while scheduling a Plugwise Quartz Job", e);
                        }
                    }
                } else {
                    logger.error("Error scheduling a Quartz Job for a non-defined Plugwise device (" + element.getId()
                            + ")");
                }
            }
        }

        List<CirclePlus> cps = stick.getDevicesByClass(CirclePlus.class);
        if (!cps.isEmpty()) {
            CirclePlus cp = cps.get(0);
            String jobName = cp.MAC + "-SetCirclePlusClock";

            if (!isExistingJob(scheduler, jobName)) {
                JobDataMap map = new JobDataMap();
                map.put(CirclePlus.CIRCLE_PLUS_JOB_DATA_KEY, cp);

                JobDetail job = newJob(SetClockJob.class).withIdentity(jobName, "Plugwise").usingJobData(map).build();
                CronTrigger trigger = newTrigger().withIdentity(jobName, "Plugwise").startNow()
                        .withSchedule(CronScheduleBuilder.cronSchedule("0 0 0 * * ?")).build();

                try {
                    Scheduler sched = StdSchedulerFactory.getDefaultScheduler();
                    sched.scheduleJob(job, trigger);
                } catch (SchedulerException e) {
                    logger.error("Error scheduling Circle+ setClock Quartz Job", e);
                }
            }
        }
    }

    private boolean isExistingJob(Scheduler scheduler, String jobName) {
        try {
            for (String group : scheduler.getJobGroupNames()) {
                for (JobKey jobKey : scheduler.getJobKeys(jobGroupEquals(group))) {
                    if (jobKey.getName().equals(jobName)) {
                        return true;
                    }
                }
            }
        } catch (SchedulerException e1) {
            logger.error("An exception occurred while querying the Quartz Scheduler ({})", e1.getMessage());
        }
        return false;
    }

    @Override
    protected long getRefreshInterval() {
        return refreshInterval;
    }

    @Override
    protected String getName() {
        return "Plugwise Refresh Service";
    }
}
