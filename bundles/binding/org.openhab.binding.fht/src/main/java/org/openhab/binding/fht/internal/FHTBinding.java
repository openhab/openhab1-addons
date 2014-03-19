/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.fht.internal;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.openhab.binding.fht.FHTBindingConfig;
import org.openhab.binding.fht.FHTBindingConfig.Datapoint;
import org.openhab.binding.fht.FHTBindingProvider;
import org.openhab.core.binding.AbstractActiveBinding;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.OpenClosedType;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.openhab.io.transport.cul.CULCommunicationException;
import org.openhab.io.transport.cul.CULDeviceException;
import org.openhab.io.transport.cul.CULHandler;
import org.openhab.io.transport.cul.CULListener;
import org.openhab.io.transport.cul.CULManager;
import org.openhab.io.transport.cul.CULMode;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Implements the connection to the FHT devices via CUL. Some commands aren't
 * send immediatley, but are queued and send in execute(). For every FHT-80b
 * there can be only one command in the queue, so we don't overuse the RF band
 * and flood the send buffer of CUL devices.
 * 
 * @author Till Klocke
 * @since 1.4.0
 */
public class FHTBinding extends AbstractActiveBinding<FHTBindingProvider> implements ManagedService, CULListener {

	private static final Logger logger = LoggerFactory.getLogger(FHTBinding.class);

	private final static SimpleDateFormat configDateFormat = new SimpleDateFormat("mm:HH:dd:MM:yy");

	/**
	 * Config key for the device address. i.e. serial:/dev/ttyACM0
	 */
	private final static String KEY_DEVICE = "device";
	/**
	 * Our housecode we need to simulate a central device.
	 */
	private final static String KEY_HOUSECODE = "housecode";
	/**
	 * Do we want to update the time and date of our FHTs?
	 */
	private final static String KEY_UPDATE_TIME = "time.update";
	/**
	 * Cron expression for Quartz to schedule the time update.
	 */
	private final static String KEY_UPDATE_CRON = "time.update.cron";
	/**
	 * Do we want to actively requests reports from FHT-80b?
	 */
	private final static String KEY_REPORTS = "reports";
	/**
	 * Cron expression for Quartz to schedule the request of reports.
	 */
	private final static String KEY_REPORTS_CRON = "reports.cron";

	private String deviceName;
	private String housecode;
	private boolean doTimeUpdate = false;
	private String timeUpdatecronExpression;
	private String reportsCronExpression;
	private boolean requestReports;

	private CULHandler cul;

	private JobKey updateTimeJobKey;
	private JobKey requestReportJobKey;

	/**
	 * the refresh interval which is used to poll values from the FHT server
	 * (optional, defaults to 60000ms)
	 */
	private long refreshInterval = 60000;

	private Map<String, FHTDesiredTemperatureCommand> temperatureCommandQueue = new HashMap<String, FHTDesiredTemperatureCommand>();
	private HashMap<String, Integer> valueCache = new HashMap<String, Integer>();

	public FHTBinding() {
	}

	public void activate() {
		bindCULHandler();
	}

	public void deactivate() {
		if (cul != null) {
			CULManager.close(cul);
		}
		unscheduleJob(requestReportJobKey);
		unscheduleJob(updateTimeJobKey);
	}

	private void bindCULHandler() {
		if (!StringUtils.isEmpty(deviceName)) {
			try {
				cul = CULManager.getOpenCULHandler(deviceName, CULMode.SLOW_RF);
				cul.registerListener(this);
				cul.send("T01" + housecode);
			} catch (CULDeviceException e) {
				logger.error("Can't open CUL", e);
			} catch (CULCommunicationException e) {
				logger.error("Can't set our own housecode", e);
			}
		}
	}

	private boolean checkCULDevice() {
		if (cul == null) {
			logger.error("CUL device is not accessible");
			return false;
		}
		return true;
	}

	private void setNewDeviceName(String newDeviceName) {
		if (!StringUtils.isEmpty(newDeviceName)) {
			if (cul != null) {
				cul.unregisterListener(this);
				CULManager.close(cul);
			}
			deviceName = newDeviceName;
			bindCULHandler();
		}
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	protected long getRefreshInterval() {
		return refreshInterval;
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	protected String getName() {
		return "FHT Refresh Service";
	}

	/**
	 * Here we send waiting commands to the FHT-80b. Since we can only send
	 * every 2 minutes a limited amount of commads, we collect commands and
	 * discard older commands in favor of newer ones, so we send as less packets
	 * as possible.
	 */
	@Override
	protected void execute() {
		if (!checkCULDevice()) {
			return;
		}
		logger.debug("Processing " + temperatureCommandQueue.size() + " waiting FHT temperature commands");
		Map<String, FHTDesiredTemperatureCommand> copyMap = new HashMap<String, FHTDesiredTemperatureCommand>(
				temperatureCommandQueue);
		for (Entry<String, FHTDesiredTemperatureCommand> entry : copyMap.entrySet()) {
			FHTDesiredTemperatureCommand waitingCommand = entry.getValue();
			String commandString = "T" + waitingCommand.getAddress() + waitingCommand.getCommand();
			try {
				cul.send(commandString);
				temperatureCommandQueue.remove(entry.getKey());
			} catch (CULCommunicationException e) {
				logger.error("Can't send desired temperature via CUL", e);
			}
		}
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	protected void internalReceiveCommand(String itemName, Command command) {
		if (!checkCULDevice()) {
			return;
		}
		logger.debug("internalReceiveCommand() is called!");
		FHTBindingConfig config = null;
		for (FHTBindingProvider provider : providers) {
			config = provider.getConfigByItemName(itemName);
			if (config != null) {
				break;
			}
		}
		if (config != null) {
			if (Datapoint.DESIRED_TEMP == config.getDatapoint() && command instanceof DecimalType) {
				setDesiredTemperature(config, (DecimalType) command);
			} else {
				logger.error("You can only manipulate the desired temperature via commands, all other data points are read only");
			}
		}
	}

	private void setDesiredTemperature(FHTBindingConfig config, DecimalType command) {
		double temperature = command.doubleValue();
		if ((temperature >= 5.5) && (temperature <= 30.5)) {
			int temp = (int) (temperature * 2.0);

			FHTDesiredTemperatureCommand commandItem = new FHTDesiredTemperatureCommand(config.getFullAddress(), "41"
					+ Integer.toHexString(temp));
			logger.debug("Queuing new desired temperature");
			temperatureCommandQueue.put(config.getFullAddress(), commandItem);
		} else {
			logger.error("The desired temperature is outside of the valid range");
		}
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	public void updated(Dictionary<String, ?> config) throws ConfigurationException {
		if (config != null) {

			// to override the default refresh interval one has to add a
			// parameter to openhab.cfg like
			// <bindingName>:refresh=<intervalInMs>
			String refreshIntervalString = (String) config.get("refresh");
			if (StringUtils.isNotBlank(refreshIntervalString)) {
				refreshInterval = Long.parseLong(refreshIntervalString);
			}

			housecode = parseMandatoryValue(KEY_HOUSECODE, config);
			doTimeUpdate = Boolean.parseBoolean((String) config.get(KEY_UPDATE_TIME));
			if (doTimeUpdate) {
				timeUpdatecronExpression = (String) config.get(KEY_UPDATE_CRON);
				if (StringUtils.isEmpty(timeUpdatecronExpression)) {
					setProperlyConfigured(false);
					throw new ConfigurationException(KEY_UPDATE_CRON,
							"Time update was configured but no cron expression");
				}
				updateTimeJobKey = scheduleJob(UpdateFHTTimeJob.class, timeUpdatecronExpression);
			} else {
				unscheduleJob(updateTimeJobKey);
			}

			requestReports = Boolean.parseBoolean((String) config.get(KEY_REPORTS));
			if (requestReports) {
				reportsCronExpression = (String) config.get(KEY_REPORTS_CRON);
				if (StringUtils.isEmpty(reportsCronExpression)) {
					setProperlyConfigured(false);
					throw new ConfigurationException(KEY_REPORTS_CRON,
							"Reports are requested, bu no cron expression is supplied");
				}
				requestReportJobKey = scheduleJob(RequestReportsJob.class, reportsCronExpression);
			} else {
				unscheduleJob(requestReportJobKey);
			}

			// At last the device, after we received all other config values
			String deviceName = parseMandatoryValue(KEY_DEVICE, config);
			setNewDeviceName(deviceName);
			setProperlyConfigured(true);
		}
	}

	private String parseMandatoryValue(String key, Dictionary<String, ?> config) throws ConfigurationException {
		String value = (String) config.get(key);
		if (StringUtils.isEmpty(value)) {
			setProperlyConfigured(false);
			throw new ConfigurationException(key, "Configuration option " + key + " is mandatory");
		}
		return value;
	}

	@Override
	public void dataReceived(String data) {
		if (data != null && data.startsWith("T")) {
			handleFHTMessage(data);
		}

	}

	private void handleFHTMessage(String data) {
		logger.debug("Received FHT message");
		if (data.length() >= 13) {
			logger.debug("Received FHT report");
			String device = data.substring(1, 5); // dev
			String command = data.substring(5, 7); // cde
			FHTCommand cde = FHTCommand.getEventById(Integer.parseInt(command, 16));
			String origin = data.substring(7, 9); // ??
			String argument = data.substring(9, 11); // val
			if (cde != null) {
				switch (cde) {
				case FHT_DESIRED_TEMP:
					double desiredTemperature = ((double) Integer.parseInt(argument, 16)) / 2.0;
					receivedNewDesiredTemperature(device, desiredTemperature);
					break;

				case FHT_MEASURED_TEMP_LOW:
					valueCache.put(device + "lowtemp", new Integer(Integer.parseInt(argument, 16)));
					break;
				case FHT_MEASURED_TEMP_HIGH:
					Integer lowtemp = valueCache.get(device + "lowtemp");

					if (lowtemp != null) {
						double temperature = (double) lowtemp + ((double) Integer.parseInt(argument, 16)) * 256.0;
						temperature /= 10.0;
						receivedNewMeasuredTemperature(device, temperature);
					}

					break;
				case FHT_STATE:
					receivedFHTState(device, argument);
					break;
				case FHT_ACTUATOR_0:
				case FHT_ACTUATOR_1:
				case FHT_ACTUATOR_2:
				case FHT_ACTUATOR_3:
				case FHT_ACTUATOR_4:
				case FHT_ACTUATOR_5:
				case FHT_ACTUATOR_6:
				case FHT_ACTUATOR_7:
				case FHT_ACTUATOR_8:
					double valve = (((double) Integer.parseInt(argument, 16)) / 255.0) * 100.0;
					receivedNewValveOpening(device, cde.getId(), valve);
					break;
				default:
					logger.warn("Unknown message: FHT " + device + ": " + command + "=" + argument + "\r\n");

				}
			} else {
				logger.warn("Received unkown FHT command: ", command);
			}
		} else if (data.length() == 11) {
			// is FHT8b frame
			logger.debug("We received probably a FHT 8b frame");
			String device = data.substring(1, 7);
			String argument = data.substring(7, 9);
			FHTState state = null;

			if ((argument.startsWith("1")) || (argument.startsWith("9"))) {
				state = FHTState.BATTERY_LOW;
			}

			if (argument.substring(1).equals("1")) {
				state = FHTState.WINDOW_OPEN;
			}

			if (argument.substring(1).equals("2")) {
				state = FHTState.WINDOW_CLOSED;
			}

			if (state != null) {
				receivedNewFHT8bState(device, state);
			} else {
				logger.warn("Received unknown state (" + argument + ") from device " + device);
			}
		} else {
			logger.warn("Received unparseable message");
		}
	}

	private void receivedFHTState(String device, String state) {
		logger.debug("Received state " + state + " for FHT device " + device);
		int stateValue = Integer.parseInt(state, 16);
		FHTBindingConfig config = getConfig(device, Datapoint.BATTERY);
		OnOffType batteryAlarm = null;
		if (stateValue % 2 == 0) {
			batteryAlarm = OnOffType.OFF;
		} else {
			stateValue = stateValue - 1;
			batteryAlarm = OnOffType.ON;
		}
		if (config != null) {
			logger.debug("Updating item " + config.getItem().getName() + " with battery state");
			eventPublisher.postUpdate(config.getItem().getName(), batteryAlarm);
		}

		OpenClosedType windowState = null;
		if (stateValue == 0) {
			windowState = OpenClosedType.CLOSED;
		} else {
			windowState = OpenClosedType.OPEN;
		}
		config = getConfig(device, Datapoint.WINDOW);
		if (config != null) {
			logger.debug("Updating item " + config.getItem().getName() + " with window state");
			eventPublisher.postUpdate(config.getItem().getName(), windowState);
		} else {
			logger.debug("Received FHT state from unknown device " + device);
		}
	}

	private void receivedNewFHT8bState(String device, FHTState state) {
		FHTBindingConfig config = null;
		if (state == FHTState.BATTERY_LOW) {
			config = getConfig(device, Datapoint.BATTERY);
		} else {
			config = getConfig(device, Datapoint.WINDOW);
		}
		if (config != null) {
			logger.debug("Updating item " + config.getItem().getName() + " with new FHT state " + state.toString());
			State newState = null;
			if (state == FHTState.BATTERY_LOW) {
				// Battery alarm goes on
				newState = OnOffType.ON;
			} else if (state == FHTState.WINDOW_OPEN) {
				newState = OpenClosedType.OPEN;
			} else if (state == FHTState.WINDOW_CLOSED) {
				newState = OpenClosedType.CLOSED;
			}
			if (newState != null) {
				eventPublisher.postUpdate(config.getItem().getName(), newState);
			} else {
				logger.warn("Unknown FHT8b state, which is unmapped to openHAB state " + state.toString());
			}
		} else {
			logger.debug("Received FHT8b state for unknown device with address " + device);
		}
	}

	private void receivedNewValveOpening(String device, int actuatorNumber, double valve) {
		String fullAddress = device + "0" + actuatorNumber;
		FHTBindingConfig config = getConfig(fullAddress, Datapoint.VALVE);
		if (config != null) {
			logger.debug("Updating item " + config.getItem().getName() + "with new valve opening");
			DecimalType state = new DecimalType(valve);
			eventPublisher.postUpdate(config.getItem().getName(), state);
		} else {
			logger.debug("Received valve opening of unkown actuator with address " + fullAddress);
		}
	}

	private void receivedNewMeasuredTemperature(String deviceAddress, double temperature) {
		FHTBindingConfig config = getConfig(deviceAddress, Datapoint.MEASURED_TEMP);
		if (config != null) {
			logger.debug("Updating item " + config.getItem().getName() + " with new measured temperature "
					+ temperature);
			DecimalType state = new DecimalType(temperature);
			eventPublisher.postUpdate(config.getItem().getName(), state);
		} else {
			logger.debug("Received new measured temp for unkown device with address " + deviceAddress);
		}
	}

	private void receivedNewDesiredTemperature(String deviceAddress, double temperature) {
		FHTBindingConfig config = getConfig(deviceAddress, Datapoint.DESIRED_TEMP);
		if (config != null) {
			logger.debug("Updating item " + config.getItem().getName() + " with new desired temperature " + temperature);
			DecimalType state = new DecimalType(temperature);
			eventPublisher.postUpdate(config.getItem().getName(), state);
		} else {
			logger.debug("Received new desired temperature for currently unknown device with address " + deviceAddress);
		}
	}

	private FHTBindingConfig getConfig(String deviceAddress, Datapoint datapoint) {
		for (FHTBindingProvider provider : providers) {
			FHTBindingConfig config = provider.getConfigByFullAddress(deviceAddress, datapoint);
			if (config != null) {
				return config;
			}
		}
		return null;
	}

	@Override
	public void error(Exception e) {
		logger.error("Received error from CUL", e);

	}

	/**
	 * The user may configure this binding to update the internal clock of
	 * FHT80b devices via rf command. The method takes care of scheduling this
	 * job.
	 */
	private JobKey scheduleJob(Class<? extends Job> jobClass, String cronExpression) {
		JobKey jobKey = null;
		try {
			Scheduler sched = StdSchedulerFactory.getDefaultScheduler();
			JobDetail detail = JobBuilder.newJob(jobClass).withIdentity("FHT time update job", "cul").build();

			CronTrigger trigger = TriggerBuilder.newTrigger().forJob(detail)
					.withSchedule(CronScheduleBuilder.cronSchedule(cronExpression)).build();
			jobKey = detail.getKey();
			sched.scheduleJob(detail, trigger);
		} catch (SchedulerException e) {
			logger.error("Can't schedule time update job", e);
		}
		return jobKey;
	}

	private void unscheduleJob(JobKey jobKey) {
		if (jobKey == null) {
			return;
		}
		try {
			Scheduler sched = StdSchedulerFactory.getDefaultScheduler();
			sched.deleteJob(jobKey);
		} catch (SchedulerException e) {
			logger.error("Error while unscheduling time update job", e);
		}

	}

	private void updateTime(FHTBindingConfig config) {
		Date date = new Date();
		String[] rawDateValues = configDateFormat.format(date).split(":");
		String device = config.getFullAddress();
		writeRegisters(device, new WriteRegisterCommand("64", Utils.convertDecimalStringToHexString(rawDateValues[0])),
				new WriteRegisterCommand("63", Utils.convertDecimalStringToHexString(rawDateValues[1])),
				new WriteRegisterCommand("62", Utils.convertDecimalStringToHexString(rawDateValues[2])),
				new WriteRegisterCommand("61", Utils.convertDecimalStringToHexString(rawDateValues[3])),
				new WriteRegisterCommand("60", Utils.convertDecimalStringToHexString(rawDateValues[4])));
	}

	private void writeRegister(String device, String register, String value) {
		StringBuffer sendBuffer = new StringBuffer(8);
		sendBuffer.append('F');
		sendBuffer.append(device);
		sendBuffer.append(register); // register to write
		sendBuffer.append(value);
		try {
			cul.send(sendBuffer.toString());
		} catch (CULCommunicationException e) {
			logger.error("Error while writing register " + register + " on device " + device);
		}
	}

	/**
	 * It possible to chain up to 8 commands together to send to the CUL. Lists
	 * with more than 8 commands will be discarded silently.
	 * 
	 * @param deviceAddress
	 * @param commands
	 */
	private void writeRegisters(String deviceAddress, WriteRegisterCommand... commands) {
		if (commands == null || commands.length == 0) {
			logger.warn("No commands to write to the CUL");
			return;
		}
		if (commands.length > 8) {
			logger.error("We can only send 8 commands at once to the CUL. Discarding all commands");
			return;
		}
		StringBuffer sendBuffer = new StringBuffer(8);
		sendBuffer.append('F');
		sendBuffer.append(deviceAddress);
		for (WriteRegisterCommand command : commands) {
			sendBuffer.append(command.register);
			sendBuffer.append(command.value);
		}
		try {
			cul.send(sendBuffer.toString());
		} catch (CULCommunicationException e) {
			logger.error("Error while writing multiple write register commands to the CUL", e);
		}
	}

	private void requestReport(FHTBindingConfig config) {
		writeRegister(config.getFullAddress(), "66", "FF");
	}

	private class UpdateFHTTimeJob implements Job {

		private long updateInterval = 300000;

		@Override
		public void execute(JobExecutionContext arg0) throws JobExecutionException {
			List<FHTBindingConfig> configs = new ArrayList<FHTBindingConfig>();
			for (FHTBindingProvider provider : providers) {
				configs.addAll(provider.getAllFHT80bBindingConfigs());
			}

			for (FHTBindingConfig config : configs) {
				updateTime(config);
				try {
					Thread.sleep(updateInterval);
				} catch (InterruptedException e) {
					logger.error("Error while waiting between time updates", e);
				}
			}

		}

	}

	private class RequestReportsJob implements Job {

		private long requestInterval = 120000;

		@Override
		public void execute(JobExecutionContext arg0) throws JobExecutionException {
			List<FHTBindingConfig> configs = new ArrayList<FHTBindingConfig>();
			for (FHTBindingProvider provider : providers) {
				configs.addAll(provider.getAllFHT80bBindingConfigs());
			}

			for (FHTBindingConfig config : configs) {
				requestReport(config);
				try {
					Thread.sleep(requestInterval);
				} catch (InterruptedException e) {
					logger.error("Error while waiting between report requests", e);
				}
			}

		}

	}

}
