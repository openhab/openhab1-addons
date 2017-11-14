/**
 * Copyright (c) 2010-2017, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.cardio2e.internal;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.Objects;

import org.openhab.binding.cardio2e.Cardio2eBindingProvider;
import org.openhab.binding.cardio2e.internal.Cardio2eGenericBindingProvider.Cardio2eBindingConfig;
import org.openhab.binding.cardio2e.internal.Cardio2eGenericBindingProvider.Cardio2eBindingConfigItem;
import org.openhab.binding.cardio2e.internal.code.Cardio2eCurtainTransaction;
import org.openhab.binding.cardio2e.internal.code.Cardio2eDateTimeTransaction;
import org.openhab.binding.cardio2e.internal.code.Cardio2eDecodedTransactionEvent;
import org.openhab.binding.cardio2e.internal.code.Cardio2eDecoder;
import org.openhab.binding.cardio2e.internal.code.Cardio2eDecoder.Cardio2eDecodedTransactionListener;
import org.openhab.binding.cardio2e.internal.code.Cardio2eHvacControlTransaction;
import org.openhab.binding.cardio2e.internal.code.Cardio2eHvacSystemModes;
import org.openhab.binding.cardio2e.internal.code.Cardio2eHvacTemperatureTransaction;
import org.openhab.binding.cardio2e.internal.code.Cardio2eLightingTransaction;
import org.openhab.binding.cardio2e.internal.code.Cardio2eLoginTransaction;
import org.openhab.binding.cardio2e.internal.code.Cardio2eObjectTypes;
import org.openhab.binding.cardio2e.internal.code.Cardio2eRelayTransaction;
import org.openhab.binding.cardio2e.internal.code.Cardio2eScenarioTransaction;
import org.openhab.binding.cardio2e.internal.code.Cardio2eSecurityTransaction;
import org.openhab.binding.cardio2e.internal.code.Cardio2eTransaction;
import org.openhab.binding.cardio2e.internal.code.Cardio2eTransactionTypes;
import org.openhab.binding.cardio2e.internal.code.Cardio2eZoneBypassStates;
import org.openhab.binding.cardio2e.internal.code.Cardio2eZoneStates;
import org.openhab.binding.cardio2e.internal.code.Cardio2eZonesBypassTransaction;
import org.openhab.binding.cardio2e.internal.code.Cardio2eZonesTransaction;
import org.openhab.binding.cardio2e.internal.com.Cardio2eCom;
import org.openhab.binding.cardio2e.internal.com.Cardio2eCom.Cardio2eComEventListener;
import org.openhab.binding.cardio2e.internal.com.Cardio2eConnectionEvent;
import org.openhab.binding.cardio2e.internal.com.Cardio2eReceivedDataEvent;
import org.apache.commons.lang.StringUtils;
import org.openhab.core.binding.AbstractActiveBinding;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.library.types.DateTimeType;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.UpDownType;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implement this class if you are going create an actively polling service like
 * querying a Website/Device.
 * 
 * @author Manuel Alberto Guerrero Díaz
 * @since 1.11.0
 */
public class Cardio2eBinding extends
		AbstractActiveBinding<Cardio2eBindingProvider> {

	private static final Logger logger = LoggerFactory
			.getLogger(Cardio2eBinding.class);
	private String port;
	private String programCode;
	private String securityCode;
	private boolean zoneStateDetection;
	private long zoneUnchangedMinRefreshDelay;
	private short datetimeMaxOffset;
	private boolean firstUpdateWillSetDatetime;
	private short allowedDatetimeUpdateHour;
	private boolean testMode;
	private Cardio2eCom com = null;
	private Cardio2eDecoder decoder = null;
	private ReceivedDataListener receivedDataListener = null;
	private DecodedTransactionListener decodedTransactionListener = null;
	private boolean loggedIn = false;
	private boolean executedDatetimeUpdate = false;
	private LiveDateTime pendingDateTimeUpdate = new LiveDateTime();
	private long lastDateTimeRequestTimestamp = 0;
	private static final long DATE_TIME_GET_MIN_DELAY = 30000;

	/**
	 * The BundleContext. This is only valid when the bundle is ACTIVE. It is
	 * set in the activate() method and must not be accessed anymore once the
	 * deactivate() method was called or before activate() was called.
	 */
	@SuppressWarnings("unused")
	private BundleContext bundleContext;

	/**
	 * The refresh interval that is used to periodically request date and time
	 * values from Cardio2e (fixed value set to 30000ms)
	 */
	private long refreshInterval = 30000;

	public Cardio2eBinding() {
	}

	/**
	 * Called by the SCR to activate the component with its configuration read
	 * from CAS
	 * 
	 * @param bundleContext
	 *            BundleContext of the Bundle that defines this component
	 * @param configuration
	 *            Configuration properties for this component obtained from the
	 *            ConfigAdmin service
	 */
	public void activate(final BundleContext bundleContext,
			final Map<String, Object> configuration) {
		this.bundleContext = bundleContext;

		logger.debug("activate");
		initializeCardio2eBinding(configuration);
	}

	/**
	 * Called by the SCR when the configuration of a binding has been changed
	 * through the ConfigAdmin service.
	 * 
	 * @param configuration
	 *            Updated configuration properties
	 */
	public void modified(final Map<String, Object> configuration) {
		// Update the internal configuration accordingly
		logger.info("Configuration was been modified. Cardio2e binding will be deactivated and reactivated to apply new configuration...");
		purgeCardio2eBinding();
		initializeCardio2eBinding(configuration);
	}

	/**
	 * Called by the SCR to deactivate the component when either the
	 * configuration is removed or mandatory references are no longer satisfied
	 * or the component has simply been stopped.
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
		this.bundleContext = null;
		// deallocate resources here that are no longer needed and
		// should be reset when activating this binding again
		logger.debug("deactivate");
		purgeCardio2eBinding();
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
		return "Cardio2e Refresh Service";
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	protected void execute() {
		// The frequently executed code (polling) goes here ...
		logger.debug("execute() method is called!");
		if (loggedIn) {
			// Cardio 2é date and time cyclic ask code used for progressive
			// update only
			if ((datetimeMaxOffset >= 0)
					&& (pendingDateTimeUpdate.getDateTime() != null)) {
				getCardioDateTime();
			}
		}
	}

	public void initializeCardio2eBinding(
			final Map<String, Object> configuration) {
		int minDelayBetweenReceivingAndSending = 0;
		int minDelayBetweenSendings = 0;
		// Set default values
		port = null;
		programCode = "00000";
		securityCode = null;
		zoneStateDetection = false;
		zoneUnchangedMinRefreshDelay = 600000;
		datetimeMaxOffset = 15;
		firstUpdateWillSetDatetime = false;
		allowedDatetimeUpdateHour = -1;
		testMode = false;
		Cardio2eTransactionParser.filterUnnecessaryCommand = false;
		Cardio2eTransactionParser.filterUnnecessaryReverseModeUpdate = true;
		Cardio2eLightingTransaction.setSmartSendingEnabledClass(false);
		Cardio2eRelayTransaction.setSmartSendingEnabledClass(false);
		Cardio2eHvacControlTransaction.setSmartSendingEnabledClass(false);
		Cardio2eDateTimeTransaction.setSmartSendingEnabledClass(false);
		Cardio2eScenarioTransaction.setSmartSendingEnabledClass(false);
		Cardio2eSecurityTransaction.setSmartSendingEnabledClass(false);
		Cardio2eZonesBypassTransaction.setSmartSendingEnabledClass(false);
		Cardio2eCurtainTransaction.setSmartSendingEnabledClass(false);
		// Load config values
		String portString = Objects.toString(configuration.get("port"), null);
		if (StringUtils.isNotBlank(portString)) {
			port = portString;
			logger.info("Serial port set from config file: {}", port);
		}
		if (port != null) {
			String programcodeString = Objects.toString(
					configuration.get("programcode"), null);
			if (StringUtils.isNotBlank(programcodeString)) {
				programCode = programcodeString;
				logger.info("Program code updated from config file");
			}

			String securitycodeString = Objects.toString(
					configuration.get("securitycode"), null);
			if (StringUtils.isNotBlank(securitycodeString)) {
				securityCode = securitycodeString;
				logger.info("Security code updated from config file");
			}

			String zonesString = Objects.toString(configuration.get("zones"),
					null);
			if (StringUtils.isNotBlank(zonesString)) {
				zoneStateDetection = Boolean.parseBoolean(zonesString);
				logger.info("Zone state detection {} in config file",
						(zoneStateDetection ? "enabled" : "disabled"));
			}

			String zoneUnchangedMinRefreshDelayString = Objects.toString(
					configuration.get("zoneUnchangedMinRefreshDelay"), null);
			if (StringUtils.isNotBlank(zoneUnchangedMinRefreshDelayString)) {
				zoneUnchangedMinRefreshDelay = Long
						.parseLong(zoneUnchangedMinRefreshDelayString);
				logger.info(
						"Zone state unchanged minimum refresh delay updated to {} milliseconds from config file",
						zoneUnchangedMinRefreshDelayString);
			}

			String datetimeMaxOffsetString = Objects.toString(
					configuration.get("datetimeMaxOffset"), null);
			if (StringUtils.isNotBlank(datetimeMaxOffsetString)) {
				datetimeMaxOffset = Short.parseShort(datetimeMaxOffsetString);
				if (datetimeMaxOffset < -1) {
					logger.info("Date and time direct update selected from config file (always sends updates, no filters, even if current date and time of Cardio 2é matches the update)");
				} else {
					if (datetimeMaxOffset > 0) {
						logger.info(
								"Date and time maximum offset updated to {} minutes from config file",
								datetimeMaxOffsetString);
					} else {
						if (datetimeMaxOffset == 0)
							logger.info("Date and time maximum offset disabled from config file");
						if (datetimeMaxOffset == -1)
							logger.info("Date and time both, progressive update and maximum offset, disabled from config file");
					}
				}
			}

			String firstUpdateWillSetDatetimeString = Objects.toString(
					configuration.get("firstUpdateWillSetDatetime"), null);
			if (StringUtils.isNotBlank(firstUpdateWillSetDatetimeString)) {
				firstUpdateWillSetDatetime = Boolean
						.parseBoolean(firstUpdateWillSetDatetimeString);
				logger.info(
						"In the configuration file was {} that the first update always sets the date and time",
						(firstUpdateWillSetDatetime ? "enabled" : "disabled"));
			}

			String allowedDatetimeUpdateHourString = Objects.toString(
					configuration.get("allowedDatetimeUpdateHour"), null);
			if (StringUtils.isNotBlank(allowedDatetimeUpdateHourString)) {
				allowedDatetimeUpdateHour = Short
						.parseShort(allowedDatetimeUpdateHourString);
				if ((allowedDatetimeUpdateHour >= 0)
						&& (allowedDatetimeUpdateHour <= 23)) {
					logger.info(
							"Allowed date and time update hour set to '{}' in config file",
							allowedDatetimeUpdateHour);
				} else {
					allowedDatetimeUpdateHour = -1;
					logger.info("Allowed date and time update hour limit disabled in config file");
				}
			}

			String testmodeString = Objects.toString(
					configuration.get("testmode"), null);
			if (StringUtils.isNotBlank(testmodeString)) {
				testMode = Boolean.parseBoolean(testmodeString);
				logger.info("Test mode {} in config file",
						(testMode ? "enabled" : "disabled"));
			}

			String minDelayBetweenReceivingAndSendingString = Objects.toString(
					configuration.get("minDelayBetweenReceivingAndSending"),
					null);
			if (StringUtils
					.isNotBlank(minDelayBetweenReceivingAndSendingString)) {
				minDelayBetweenReceivingAndSending = Integer
						.parseInt(minDelayBetweenReceivingAndSendingString);
				if (minDelayBetweenReceivingAndSending > 0)
					logger.info(
							"Minimum delay between receiving and sending updated to {} ms. from config file",
							minDelayBetweenReceivingAndSending);
			}

			String minDelayBetweenSendingsString = Objects.toString(
					configuration.get("minDelayBetweenSendings"), null);
			if (StringUtils.isNotBlank(minDelayBetweenSendingsString)) {
				minDelayBetweenSendings = Integer
						.parseInt(minDelayBetweenSendingsString);
				if (minDelayBetweenSendings > 0)
					logger.info(
							"Minimum delay between sendings updated to {} ms. from config file",
							minDelayBetweenSendings);
			}

			String filterUnnecessaryCommandString = Objects.toString(
					configuration.get("filterUnnecessaryCommand"), null);
			if (StringUtils.isNotBlank(filterUnnecessaryCommandString)) {
				Cardio2eTransactionParser.filterUnnecessaryCommand = Boolean
						.parseBoolean(filterUnnecessaryCommandString);
				logger.info(
						"Filter unnecessary command {} in config file",
						(Cardio2eTransactionParser.filterUnnecessaryCommand ? "enabled"
								: "disabled"));
			}

			String filterUnnecessaryReverseModeUpdateFilterString = Objects
					.toString(configuration
							.get("filterUnnecessaryReverseModeUpdate"), null);
			if (StringUtils
					.isNotBlank(filterUnnecessaryReverseModeUpdateFilterString)) {
				Cardio2eTransactionParser.filterUnnecessaryReverseModeUpdate = Boolean
						.parseBoolean(filterUnnecessaryReverseModeUpdateFilterString);
				logger.info(
						"Filter unnecessary reverse mode update {} in config file",
						(Cardio2eTransactionParser.filterUnnecessaryReverseModeUpdate ? "enabled"
								: "disabled"));
			}

			String smartSendingEnabledObjectTypesString = Objects.toString(
					configuration.get("smartSendingEnabledObjectTypes"), null);
			if (StringUtils.isNotBlank(smartSendingEnabledObjectTypesString)) {
				smartSendingEnabledObjectTypesString = smartSendingEnabledObjectTypesString
						.toUpperCase();
				if (smartSendingEnabledObjectTypesString
						.contains(Cardio2eObjectTypes.LIGHTING.name())) {
					Cardio2eLightingTransaction
							.setSmartSendingEnabledClass(true);
					logger.info("Smart sending enabled for LIGHTING object type in config file");
				}
				if (smartSendingEnabledObjectTypesString
						.contains(Cardio2eObjectTypes.RELAY.name())) {
					Cardio2eRelayTransaction.setSmartSendingEnabledClass(true);
					logger.info("Smart sending enabled for RELAY object type in config file");
				}
				if (smartSendingEnabledObjectTypesString
						.contains(Cardio2eObjectTypes.HVAC_CONTROL.name())) {
					Cardio2eHvacControlTransaction
							.setSmartSendingEnabledClass(true);
					logger.info("Smart sending enabled for HVAC_CONTROL object type in config file");
				}
				if (smartSendingEnabledObjectTypesString
						.contains(Cardio2eObjectTypes.DATE_AND_TIME.name())) {
					Cardio2eDateTimeTransaction
							.setSmartSendingEnabledClass(true);
					logger.info("Smart sending enabled for DATE_AND_TIME object type in config file");
				}
				if (smartSendingEnabledObjectTypesString
						.contains(Cardio2eObjectTypes.SCENARIO.name())) {
					Cardio2eScenarioTransaction
							.setSmartSendingEnabledClass(true);
					logger.info("Smart sending enabled for SCENARIO object type in config file");
				}
				if (smartSendingEnabledObjectTypesString
						.contains(Cardio2eObjectTypes.SECURITY.name())) {
					Cardio2eSecurityTransaction
							.setSmartSendingEnabledClass(true);
					logger.info("Smart sending enabled for SECURITY object type in config file");
				}
				if (smartSendingEnabledObjectTypesString
						.contains(Cardio2eObjectTypes.ZONES_BYPASS.name())) {
					Cardio2eZonesBypassTransaction
							.setSmartSendingEnabledClass(true);
					logger.info("Smart sending enabled for ZONES_BYPASS object type in config file");
				}
				if (smartSendingEnabledObjectTypesString
						.contains(Cardio2eObjectTypes.CURTAIN.name())) {
					Cardio2eCurtainTransaction
							.setSmartSendingEnabledClass(true);
					logger.info("Smart sending enabled for CURTAIN object type in config file");
				}
			}

			// Read further config parameters here ...

			com = new Cardio2eCom();
			decoder = com.decoder;
			receivedDataListener = new ReceivedDataListener();
			decodedTransactionListener = new DecodedTransactionListener();
			com.addReceivedDataListener(receivedDataListener);
			decoder.addDecodedTransactionListener(decodedTransactionListener);
			com.testMode = testMode;
			decoder.decodeZonesStateTransaction = zoneStateDetection;
			com.setSerialPort(port);
			if (minDelayBetweenReceivingAndSending > 0)
				com.setMinDelayBetweenReceivingAndSending(minDelayBetweenReceivingAndSending);
			if (minDelayBetweenSendings > 0)
				com.setMinDelayBetweenSendings(minDelayBetweenSendings);

			try {
				com.sendTransaction(new Cardio2eLoginTransaction(programCode)); // Login
																				// request
			} catch (Exception ex) {
				logger.warn("Failed to send login request: '{}'", ex.toString());
			}

			if (testMode)
				loggedIn = true;

			setProperlyConfigured(true);

			logger.debug("Cardio2e binding activated");
		} else
			logger.warn("Cardio2e binding cannot be activated because no serial port is set in config file");
	}

	public void purgeCardio2eBinding() {
		if (com != null) {
			com.disconnect();
			loggedIn = false;
			executedDatetimeUpdate = false;
			pendingDateTimeUpdate.setDateTime(null);
			com.removeReceivedDataListener(receivedDataListener);
			decoder.removeDecodedTransactionListener(decodedTransactionListener);
			decodedTransactionListener = null;
			receivedDataListener = null;
			decoder = null;
			com = null;
			port = null;
			logger.debug("Cardio2e binding deactivated");
		}
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	protected void internalReceiveCommand(String itemName, Command command) {
		// The code being executed when a command was sent on the openHAB
		// event bus goes here. This method is only called if one of the
		// BindingProviders provide a binding for the given 'itemName'.
		logger.debug(
				"internalReceiveCommand() is called, with itemName '{}' and Command '{}'!",
				itemName, command);
		if (loggedIn) {
			Cardio2eTransaction transaction;
			Cardio2eBindingConfig config = null;
			boolean reverseOrder;
			Cardio2eBindingProvider provider = null;
			if (!providers.isEmpty()) {
				provider = providers.iterator().next(); // Multiple providers
														// are not supported, so
														// will use first
														// available provider
				logger.debug("Checking provider with names {}",
						provider.getItemNames());
				transaction = null;
				reverseOrder = false;
				if (command instanceof UpDownType) { // Rollershutter hardware
														// security patch for
														// pair of relay control
														// (reverse config order
														// to do stop down
														// before up)
					if ((UpDownType) command == UpDownType.UP) {
						reverseOrder = true;
					}
				}
				if (reverseOrder) { // Get config in reverse or normal order
					config = provider.getReverseOrderConfig(itemName);
				} else {
					config = provider.getConfig(itemName);
				}
				for (Cardio2eBindingConfigItem configItem : config) {
					try {
						synchronized (configItem.transaction) {
							transaction = configItem.transaction.deepClone();
						}
						if (!(configItem.reverseMode)) {
							switch (transaction.getObjectType()) {
							case SECURITY:
							case SCENARIO:
								Cardio2eTransactionParser.commandToTransaction(
										command, transaction, securityCode);
								break;
							case DATE_AND_TIME:
								pendingDateTimeUpdate.setDateTime(null);
								logger.debug("Cancelled any date and time update task");
							default:
								Cardio2eTransactionParser.commandToTransaction(
										command, transaction, null);
							}
							if (transaction.mustBeSent) {
								transaction.primitiveStringTransaction = transaction
										.toString();
								logger.debug(
										"Adding '{}' to Cardio 2é RS-232 send queue",
										transaction.primitiveStringTransaction
												.substring(
														0,
														transaction.primitiveStringTransaction
																.length() - 1));
								com.sendTransaction(transaction);
							} else {
								logger.debug("No command sent to Cardio 2é because it is not necessary to send current object value again");
							}
						} else {
							logger.debug("No command sent to Cardio 2é because item is configured as 'reverse mode'");
						}
					} catch (Exception ex) {
						logger.warn("Error in processing command: '{}'",
								ex.toString());
					}
				}
			} else {
				logger.warn("No command will sent to Cardio 2é because no provider is available");
			}
		} else {
			logger.warn("Can not send command to Cardio 2é because we are not logged in");
		}
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	protected void internalReceiveUpdate(String itemName, State newState) {
		// The code being executed when a state was sent on the openHAB
		// event bus goes here. This method is only called if one of the
		// BindingProviders provide a binding for the given 'itemName'.
		logger.debug(
				"internalReceiveUpdate() is called, with itemName '{}' and State '{}'!",
				itemName, newState);
		if (loggedIn) {
			Cardio2eTransaction transaction;
			Cardio2eBindingProvider provider = null;
			if (!providers.isEmpty()) {
				provider = providers.iterator().next(); // Multiple providers
														// are not supported, so
														// will use first
														// available provider
				logger.debug("Checking provider with names '{}'",
						provider.getItemNames());
				transaction = null;
				for (Cardio2eBindingConfigItem configItem : provider
						.getConfig(itemName)) {
					try {
						if (configItem.reverseMode) {
							synchronized (configItem) {
								transaction = configItem.transaction
										.deepClone();
								Cardio2eTransactionParser.stateToTransaction(
										newState, transaction);
								if (transaction.mustBeSent) {
									configItem.setPendingUpdates(configItem
											.getPendingUpdates() + 1);
									transaction.primitiveStringTransaction = transaction
											.toString();
									logger.debug(
											"Adding '{}' to Cardio 2é RS-232 send queue",
											transaction.primitiveStringTransaction
													.substring(
															0,
															transaction.primitiveStringTransaction
																	.length() - 1));
									com.sendTransaction(transaction);
								} else {
									logger.debug("No state sent to Cardio 2é because it is not necessary to send current object value again");
								}
							}
						} else {
							if (configItem.transaction.getObjectType() == Cardio2eObjectTypes.DATE_AND_TIME) {
								DateTimeType newDateTimeState = (DateTimeType) newState;
								if ((allowedDatetimeUpdateHour == -1)
										|| (newDateTimeState.getCalendar().get(
												Calendar.HOUR_OF_DAY) == allowedDatetimeUpdateHour)
										|| ((firstUpdateWillSetDatetime) && (!executedDatetimeUpdate))) {
									// Allow date and time update if
									// allowedDatetimeUpdateHour is -1 or
									// allowedDatetimeUpdateHour is newState
									// hour (or first update when
									// firstUpdateWillSetDatetime)
									if ((datetimeMaxOffset <= -2)
											|| ((firstUpdateWillSetDatetime) && (!executedDatetimeUpdate))) {
										// Direct update
										executedDatetimeUpdate = true;
										pendingDateTimeUpdate.setDateTime(null);
										transaction = new Cardio2eDateTimeTransaction(
												Cardio2eTransactionTypes.SET);
										Cardio2eTransactionParser
												.stateToTransaction(
														newDateTimeState,
														transaction);
										transaction.primitiveStringTransaction = transaction
												.toString();
										logger.debug(
												"Adding '{}' to Cardio 2é RS-232 send queue",
												transaction.primitiveStringTransaction
														.substring(
																0,
																transaction.primitiveStringTransaction
																		.length() - 1));
										com.sendTransaction(transaction);
									} else {
										// Will ask Cardio 2é for current date
										// and time (GET transaction). When
										// Cardio answers, will send new value
										// (SET) if offset is less than
										// datetimeMaxOffset.
										pendingDateTimeUpdate
												.setDateTime((DateTimeType) newDateTimeState);
										logger.debug("Executing date and time update task. Now will ask Cardio for current date and time...");
										getCardioDateTime();
									}
								} else {
									logger.debug(
											"Date and time update ignored because time is not betwen {}:00 and {}:59",
											allowedDatetimeUpdateHour,
											allowedDatetimeUpdateHour);
								}
							} else {
								logger.debug("No state sent to Cardio 2é because item is not configured as 'reverse mode'");
							}
						}
					} catch (Exception ex) {
						logger.warn("Error in processing state: '{}'",
								ex.toString());
					}
				}
			} else {
				logger.warn("No state will sent to Cardio 2é because no provider is available");
			}
		} else {
			logger.warn("Can not send state to Cardio 2é because we are not logged in");
		}
	}

	private void getCardioDateTime() {
		if ((datetimeMaxOffset < 0)
				|| ((System.currentTimeMillis() - lastDateTimeRequestTimestamp) >= DATE_TIME_GET_MIN_DELAY)) {
			lastDateTimeRequestTimestamp = System.currentTimeMillis();
			try {
				logger.debug("Date and time update in course (adding GET request to Cardio 2é RS-232 send queue)");
				com.sendTransaction(new Cardio2eDateTimeTransaction(
						Cardio2eTransactionTypes.GET));
			} catch (Exception ex) {
				logger.warn("Failed to send GET date and time request: '{}'",
						ex.toString());
			}
		}
	}

	private class LiveDateTime {
		private DateTimeType dateTime = null;
		private long timestamp;

		public LiveDateTime() {
		}

		public void setDateTime(DateTimeType dateTime) {
			synchronized (this) {
				timestamp = System.currentTimeMillis();
				this.dateTime = dateTime;
			}
		}

		public DateTimeType getDateTime() {
			synchronized (this) {
				return this.dateTime;
			}
		}

		public DateTimeType getUpdatedDateTime() {
			synchronized (this) {
				DateTimeType dateTimeUpdate = null;
				if (this.dateTime != null) {
					dateTimeUpdate = new DateTimeType();
					dateTimeUpdate.getCalendar().setTimeInMillis(
							this.dateTime.getCalendar().getTimeInMillis()
									+ (System.currentTimeMillis() - timestamp));
				}
				return dateTimeUpdate;
			}
		}
	}

	private class ReceivedDataListener implements Cardio2eComEventListener {
		public ReceivedDataListener() {
		}

		public void receivedData(Cardio2eReceivedDataEvent e) {
		}

		public void isConnected(Cardio2eConnectionEvent e) {
			logger.info("Cardio is {}.", (e.getIsConnected() ? "CONNECTED"
					: "DISCONNECTED"));
		}
	}

	private class DecodedTransactionListener implements
			Cardio2eDecodedTransactionListener {
		public DecodedTransactionListener() {
		}

		public void decodedTransaction(Cardio2eDecodedTransactionEvent e) {
			Cardio2eTransaction transaction = e.getDecodedTransaction();
			Cardio2eBindingConfig config;
			Command command;
			State newState;
			switch (transaction.getTransactionType()) {
			case INFORMATION:
				switch (transaction.getObjectType()) {
				case LOGIN: // Decoded login confirmation "@I P E"
					loggedIn = true;
					logger.info("Cardio login succeed.");
					break;
				case LIGHTING:
					Cardio2eLightingTransaction receivedLightingTransaction;
					Cardio2eLightingTransaction configLightingTransaction;
					for (Cardio2eBindingProvider provider : providers) {
						Map<String, BindingConfig> matchBindingConfigs = provider
								.getMatchBindingConfigs(transaction);
						for (String itemName : matchBindingConfigs.keySet()) {
							try {
								config = (Cardio2eBindingConfig) matchBindingConfigs
										.get(itemName);
								for (Cardio2eBindingConfigItem configItem : config) {
									receivedLightingTransaction = (Cardio2eLightingTransaction) transaction;
									configLightingTransaction = (Cardio2eLightingTransaction) configItem.transaction;
									configLightingTransaction
											.setLightIntensity(receivedLightingTransaction
													.getLightIntensity()); // Stores
																			// last
																			// received
																			// light
																			// intensity
																			// (example:
																			// for
																			// increase
																			// /
																			// decrease
																			// commands)
									if (configItem.reverseMode) {
										int pendingUpdates = configItem
												.getPendingUpdates();
										if (pendingUpdates > 0) {
											configItem
													.setPendingUpdates(pendingUpdates - 1);
										} else if (loggedIn) {
											command = Cardio2eTransactionParser
													.transactionToCommand(
															configLightingTransaction,
															provider.getItem(itemName));
											logger.debug(
													"Sending LIGHT #{} {} command to the bus.",
													configLightingTransaction
															.getObjectNumber(),
													command);
											eventPublisher.postCommand(
													itemName, command);
										}
									} else {
										newState = Cardio2eTransactionParser
												.transactionToState(
														configLightingTransaction,
														provider.getItem(itemName));
										logger.debug(
												"Sending LIGHT #{} {} state to the bus.",
												configLightingTransaction
														.getObjectNumber(),
												newState);
										eventPublisher.postUpdate(itemName,
												newState);
									}
								}
							} catch (Exception ex) {
								logger.warn(
										"Error in processing decoded transaction: '{}'",
										ex);
							}
						}
					}
					break;
				case RELAY:
					Cardio2eRelayTransaction receivedRelayTransaction;
					Cardio2eRelayTransaction configRelayTransaction;
					for (Cardio2eBindingProvider provider : providers) {
						Map<String, BindingConfig> matchBindingConfigs = provider
								.getMatchBindingConfigs(transaction);
						for (String itemName : matchBindingConfigs.keySet()) {
							try {
								config = (Cardio2eBindingConfig) matchBindingConfigs
										.get(itemName);
								for (Cardio2eBindingConfigItem configItem : config) {
									configRelayTransaction = (Cardio2eRelayTransaction) configItem.transaction;
									receivedRelayTransaction = (Cardio2eRelayTransaction) transaction;
									configRelayTransaction
											.setRelayState(receivedRelayTransaction
													.getRelayState());
									if (configItem.reverseMode) {
										if (configItem.getPendingUpdates() > 0) {
											configItem
													.setPendingUpdates(configItem
															.getPendingUpdates() - 1);
										} else if (loggedIn) {
											command = Cardio2eTransactionParser
													.transactionToCommand(
															configRelayTransaction,
															provider.getItem(itemName));
											logger.debug(
													"Sending RELAY #{} {} command to the bus.",
													configRelayTransaction
															.getObjectNumber(),
													command);
											eventPublisher.postCommand(
													itemName, command);
										}
									} else if ((configRelayTransaction
											.getRelayON())
											|| (!configRelayTransaction
													.getBlindMode())) { // Relay
																		// blind
																		// mode
																		// do
																		// not
																		// report
																		// OFF
																		// states
										newState = Cardio2eTransactionParser
												.transactionToState(
														configRelayTransaction,
														provider.getItem(itemName));
										logger.debug(
												"Sending RELAY #{} {} state to the bus.",
												configRelayTransaction
														.getObjectNumber(),
												newState);
										eventPublisher.postUpdate(itemName,
												newState);
									}
								}
							} catch (Exception ex) {
								logger.warn(
										"Error in processing decoded transaction: '{}'",
										ex);
							}
						}
					}
					break;
				case HVAC_TEMPERATURE:
					Cardio2eHvacTemperatureTransaction receivedHvacTemperatureTransaction;
					for (Cardio2eBindingProvider provider : providers) {
						Map<String, BindingConfig> matchBindingConfigs = provider
								.getMatchBindingConfigs(transaction);
						for (String itemName : matchBindingConfigs.keySet()) {
							try {
								receivedHvacTemperatureTransaction = (Cardio2eHvacTemperatureTransaction) transaction;
								newState = Cardio2eTransactionParser
										.transactionToState(
												receivedHvacTemperatureTransaction,
												provider.getItem(itemName));
								logger.debug(
										"Sending HVAC_TEMPERATURE #{} {} state to the bus.",
										receivedHvacTemperatureTransaction
												.getObjectNumber(), newState);
								eventPublisher.postUpdate(itemName, newState);
							} catch (Exception ex) {
								logger.warn(
										"Error in processing decoded transaction: '{}'",
										ex);
							}
						}
					}
					break;
				case HVAC_CONTROL:
					Cardio2eHvacControlTransaction receivedHvacControlTransaction;
					Cardio2eHvacControlTransaction configHvacControlTransaction;
					for (Cardio2eBindingProvider provider : providers) {
						Map<String, BindingConfig> matchBindingConfigs = provider
								.getMatchBindingConfigs(transaction);
						for (String itemName : matchBindingConfigs.keySet()) {
							try {
								config = (Cardio2eBindingConfig) matchBindingConfigs
										.get(itemName);
								for (Cardio2eBindingConfigItem configItem : config) {
									receivedHvacControlTransaction = (Cardio2eHvacControlTransaction) transaction;
									synchronized (configItem.transaction) {
										configHvacControlTransaction = (Cardio2eHvacControlTransaction) configItem.transaction;
										configHvacControlTransaction
												.setHvacHeatingSetPoint(receivedHvacControlTransaction
														.getHvacHeatingSetPoint());
										configHvacControlTransaction
												.setHvacCoolingSetPoint(receivedHvacControlTransaction
														.getHvacCoolingSetPoint());
										configHvacControlTransaction
												.setHvacFanState(receivedHvacControlTransaction
														.getHvacFanState());
										configHvacControlTransaction
												.setHvacSystemMode(receivedHvacControlTransaction
														.getHvacSystemMode());
									}
									newState = Cardio2eTransactionParser
											.transactionToState(
													configHvacControlTransaction,
													provider.getItem(itemName));
									if (configHvacControlTransaction.singleHvacSystemMode == null) {
										logger.debug(
												"Sending HVAC #{} {} state to the bus.",
												configHvacControlTransaction
														.getObjectNumber(),
												newState);
									} else {
										logger.debug(
												"Sending HVAC #{} {} {} state to the bus.",
												configHvacControlTransaction
														.getObjectNumber(),
												(configHvacControlTransaction.singleHvacSystemMode == Cardio2eHvacSystemModes.OFF ? "FAN"
														: configHvacControlTransaction.singleHvacSystemMode),
												newState);
									}
									eventPublisher.postUpdate(itemName,
											newState);
								}
							} catch (Exception ex) {
								logger.warn(
										"Error in processing decoded transaction: '{}'",
										ex);
							}
						}
					}
					break;
				case ZONES:
					Cardio2eZonesTransaction receivedZonesTransaction;
					Cardio2eZonesTransaction configZonesTransaction;
					for (Cardio2eBindingProvider provider : providers) {
						Map<String, BindingConfig> matchBindingConfigs = provider
								.getMatchBindingConfigs(transaction);
						for (String itemName : matchBindingConfigs.keySet()) {
							try {
								config = (Cardio2eBindingConfig) matchBindingConfigs
										.get(itemName);
								for (Cardio2eBindingConfigItem configItem : config) {
									configZonesTransaction = (Cardio2eZonesTransaction) configItem.transaction;
									receivedZonesTransaction = (Cardio2eZonesTransaction) transaction;
									short configZoneNumber = configZonesTransaction
											.getObjectNumber();
									short receivedFirstZoneNumber = receivedZonesTransaction
											.getObjectNumber();
									if (configZoneNumber >= receivedFirstZoneNumber) {
										Cardio2eZoneStates[] receivedZoneStates = receivedZonesTransaction
												.getZoneStates();
										int receivedZoneNumberOffset = (configZoneNumber - receivedFirstZoneNumber);
										if (receivedZoneStates.length > receivedZoneNumberOffset) {
											Cardio2eZoneStates receivedZoneState = receivedZoneStates[(configZoneNumber - receivedFirstZoneNumber)];
											long timestamp = System
													.currentTimeMillis();
											if ((configZonesTransaction
													.getZoneState() != receivedZoneState)
													|| ((timestamp - configItem.lastStateSentTimestamp) >= zoneUnchangedMinRefreshDelay)) {
												configZonesTransaction
														.setZoneState(receivedZoneState);
												newState = Cardio2eTransactionParser
														.transactionToState(
																configZonesTransaction,
																provider.getItem(itemName));
												logger.debug(
														"Sending ZONE #{} {} state to the bus.",
														configZonesTransaction
																.getObjectNumber(),
														newState);
												eventPublisher.postUpdate(
														itemName, newState);
												configItem.lastStateSentTimestamp = timestamp;
											}
										}
									}
								}
							} catch (Exception ex) {
								logger.warn(
										"Error in processing decoded transaction: '{}'",
										ex);
							}
						}
					}
					break;
				case ZONES_BYPASS:
					Cardio2eZonesBypassTransaction receivedZonesBypassTransaction;
					Cardio2eZonesBypassTransaction configZonesBypassTransaction;
					for (Cardio2eBindingProvider provider : providers) {
						Map<String, BindingConfig> matchBindingConfigs = provider
								.getMatchBindingConfigs(transaction);
						for (String itemName : matchBindingConfigs.keySet()) {
							try {
								config = (Cardio2eBindingConfig) matchBindingConfigs
										.get(itemName);
								for (Cardio2eBindingConfigItem configItem : config) {
									configZonesBypassTransaction = (Cardio2eZonesBypassTransaction) configItem.transaction;
									receivedZonesBypassTransaction = (Cardio2eZonesBypassTransaction) transaction;
									short configZoneBypassNumber = configZonesBypassTransaction
											.getObjectNumber();
									short receivedFirstZoneBypassNumber = receivedZonesBypassTransaction
											.getObjectNumber();
									if (configZoneBypassNumber >= receivedFirstZoneBypassNumber) {
										Cardio2eZoneBypassStates[] receivedZoneBypassStates = receivedZonesBypassTransaction
												.getZoneBypassStates();
										int receivedZoneBypassNumberOffset = (configZoneBypassNumber - receivedFirstZoneBypassNumber);
										if (receivedZoneBypassStates.length > receivedZoneBypassNumberOffset) {
											Cardio2eZoneBypassStates receivedZoneBypassState = receivedZoneBypassStates[(configZoneBypassNumber - receivedFirstZoneBypassNumber)];
											long timestamp = System
													.currentTimeMillis();
											if ((configZonesBypassTransaction
													.getZoneBypassState() != receivedZoneBypassState)
													|| ((timestamp - configItem.lastStateSentTimestamp) >= zoneUnchangedMinRefreshDelay)) {
												configZonesBypassTransaction
														.setZoneBypassState(receivedZoneBypassState);
												newState = Cardio2eTransactionParser
														.transactionToState(
																configZonesBypassTransaction,
																provider.getItem(itemName));
												logger.debug(
														"Sending ZONE #{} {} state to the bus.",
														configZonesBypassTransaction
																.getObjectNumber(),
														newState);
												eventPublisher.postUpdate(
														itemName, newState);
												configItem.lastStateSentTimestamp = timestamp;
											}
										}
									}
								}
							} catch (Exception ex) {
								logger.warn(
										"Error in processing decoded transaction: '{}'",
										ex);
							}
						}
					}
					break;
				case SECURITY:
					Cardio2eSecurityTransaction receivedSecurityTransaction;
					for (Cardio2eBindingProvider provider : providers) {
						Map<String, BindingConfig> matchBindingConfigs = provider
								.getMatchBindingConfigs(transaction);
						for (String itemName : matchBindingConfigs.keySet()) {
							if (provider.getItem(itemName)
									.getAcceptedDataTypes()
									.contains(OnOffType.class)) { // Only sends
																	// states to
																	// Switch
																	// Items
								try {
									receivedSecurityTransaction = (Cardio2eSecurityTransaction) transaction;
									newState = Cardio2eTransactionParser
											.transactionToState(
													receivedSecurityTransaction,
													provider.getItem(itemName));
									logger.debug(
											"Sending SECURITY armed {} state to the bus.",
											newState);
									eventPublisher.postUpdate(itemName,
											newState);
								} catch (Exception ex) {
									logger.warn(
											"Error in processing decoded transaction: '{}'",
											ex.toString());
								}
							}
						}
					}
					break;
				case DATE_AND_TIME:
					// No updates will be sent to the openhab bus.
					boolean canCleanpendingDateTimeUpdate = true;
					synchronized (pendingDateTimeUpdate) {
						try {
							DateTimeType storedUpdate = pendingDateTimeUpdate
									.getDateTime();
							if (storedUpdate != null) {
								DateTimeType pendingUpdate = pendingDateTimeUpdate
										.getUpdatedDateTime();
								Cardio2eDateTimeTransaction receivedDateTimeTransaction = (Cardio2eDateTimeTransaction) transaction;
								Cardio2eDateTimeTransaction newDateTimeTransaction = new Cardio2eDateTimeTransaction();
								Cardio2eTransactionParser.stateToTransaction(
										pendingUpdate, newDateTimeTransaction);
								logger.debug(
										"Received date and time from Cardio 2é : '{}'",
										newDateTimeTransaction.getDateTime()
												.toString());
								logger.debug(
										"Received date and time state from bus : '{}'",
										storedUpdate.toString());
								logger.debug(
										"Updated target value would should be  : '{}'",
										pendingUpdate.toString());
								if (receivedDateTimeTransaction
										.getDateTime()
										.toDate()
										.equals(newDateTimeTransaction
												.getDateTime().toDate())) {
									// New and received transactions dates are
									// equals, will not send new datetime
									logger.debug("Cardio 2é current date and time match new updated state, so we will not send it");
									pendingDateTimeUpdate.setDateTime(null);
								} else {
									// There is a different
									// pendingDateTimeUpdate and offset is not
									// greater than datetimeMaxOffset (or
									// datetimeMaxOffset<=0) will update Cardio
									// 2é date and time
									long dateTimeOffset = 0;
									if (datetimeMaxOffset > 0)
										dateTimeOffset = Math
												.abs(receivedDateTimeTransaction
														.getDateTime().toDate()
														.getTime()
														- newDateTimeTransaction
																.getDateTime()
																.toDate()
																.getTime()) / 60000;
									if ((datetimeMaxOffset <= 0)
											|| (dateTimeOffset <= datetimeMaxOffset)) {
										newDateTimeTransaction.smartSendingEnabledTransaction = true;
										newDateTimeTransaction.smartSendingEnqueueFirst = true;
										if (datetimeMaxOffset > 0)
											logger.debug(
													"Date and time {} minutes offset is OK (maximum is {} minutes)",
													dateTimeOffset,
													datetimeMaxOffset);
										if ((datetimeMaxOffset >= 0)
												&& (receivedDateTimeTransaction
														.getDateTime().toDate()
														.before(newDateTimeTransaction
																.getDateTime()
																.toDate()))) {
											// Progressive update is enabled,
											// and received date is before than
											// new one: will do a progressive
											// update, minute by minute
											Date receivedDatePlusMinute = new Date();
											receivedDatePlusMinute
													.setTime((receivedDateTimeTransaction
															.getDateTime()
															.toDate().getTime() + 60000));
											newDateTimeTransaction
													.getDateTime()
													.fromDate(
															receivedDatePlusMinute);
											canCleanpendingDateTimeUpdate = false;
											logger.debug("Doing progressive aproach: will advance a minute");
										}
										newDateTimeTransaction.primitiveStringTransaction = newDateTimeTransaction
												.toString();
										logger.debug(
												"Sending '{}'",
												newDateTimeTransaction.primitiveStringTransaction
														.substring(
																0,
																newDateTimeTransaction.primitiveStringTransaction
																		.length() - 1));
										com.sendTransaction(newDateTimeTransaction);
									} else {
										logger.debug(
												"Date and time {} minutes offset is too much (maximum is {} minutes). New date and time will be discarded",
												dateTimeOffset,
												datetimeMaxOffset);
									}
								}
							}
						} catch (Exception ex) {
							logger.warn(
									"Error in processing decoded transaction: '{}'",
									ex);
						} finally {
							if (canCleanpendingDateTimeUpdate) {
								pendingDateTimeUpdate.setDateTime(null);
								logger.debug("Finished date and time update task");
							}
						}
					}
					break;
				case CURTAIN:
					Cardio2eCurtainTransaction receivedCurtainTransaction;
					Cardio2eCurtainTransaction configCurtainTransaction;
					for (Cardio2eBindingProvider provider : providers) {
						Map<String, BindingConfig> matchBindingConfigs = provider
								.getMatchBindingConfigs(transaction);
						for (String itemName : matchBindingConfigs.keySet()) {
							try {
								config = (Cardio2eBindingConfig) matchBindingConfigs
										.get(itemName);
								for (Cardio2eBindingConfigItem configItem : config) {
									receivedCurtainTransaction = (Cardio2eCurtainTransaction) transaction;
									configCurtainTransaction = (Cardio2eCurtainTransaction) configItem.transaction;
									configCurtainTransaction
											.setClosingPercentage(receivedCurtainTransaction
													.getClosingPercentage()); // Stores
																				// last
																				// received
																				// closing
																				// percentage
									if (configItem.reverseMode) {
										int pendingUpdates = configItem
												.getPendingUpdates();
										if (pendingUpdates > 0) {
											configItem
													.setPendingUpdates(pendingUpdates - 1);
										} else if (loggedIn) {
											command = Cardio2eTransactionParser
													.transactionToCommand(
															configCurtainTransaction,
															provider.getItem(itemName));
											logger.debug(
													"Sending CURTAIN #{} {} command to the bus.",
													configCurtainTransaction
															.getObjectNumber(),
													command);
											eventPublisher.postCommand(
													itemName, command);
										}
									} else {
										newState = Cardio2eTransactionParser
												.transactionToState(
														configCurtainTransaction,
														provider.getItem(itemName));
										logger.debug(
												"Sending CURTAIN #{} {} state to the bus.",
												configCurtainTransaction
														.getObjectNumber(),
												newState);
										eventPublisher.postUpdate(itemName,
												newState);
									}
								}
							} catch (Exception ex) {
								logger.warn(
										"Error in processing decoded transaction: '{}'",
										ex);
							}
						}
					}
					break;
				default:
					logger.debug(
							"No actions defined for received {} information type transactions",
							transaction.getObjectType());
				}
				break;
			case ACK:
				if (transaction.getObjectType() == Cardio2eObjectTypes.SECURITY) { // Reports
																					// NO
																					// error
																					// code
																					// for
																					// security
																					// object
																					// NACK
					for (Cardio2eBindingProvider provider : providers) {
						Map<String, BindingConfig> matchBindingConfigs = provider
								.getMatchBindingConfigs(transaction);
						for (String itemName : matchBindingConfigs.keySet()) {
							if (provider.getItem(itemName)
									.getAcceptedDataTypes()
									.contains(DecimalType.class)) { // Only
																	// sends
																	// NACK NO
																	// error
																	// value to
																	// Number
																	// Items
								try {
									logger.debug("Sending SECURITY NO error code (ZERO) state to the bus.");
									eventPublisher.postUpdate(itemName,
											DecimalType.ZERO);
								} catch (Exception ex) {
									logger.warn(
											"Error in reporting no error code for SECURITY ACK transaction: '{}'",
											ex);
								}
							}
						}
					}
				}
				break;
			case NACK:
				switch (transaction.getObjectType()) {
				case LOGIN:
				case DATE_AND_TIME:
				case SCENARIO:
					logger.debug("NACK: {} transaction NOT ACCEPTED. {}.",
							transaction.getObjectType(),
							transaction.getErrorCodeDescription());
					break;
				default:
					if (transaction.getObjectType() == Cardio2eObjectTypes.SECURITY) { // Reports
																						// error
																						// code
																						// for
																						// security
																						// object
																						// NACK
						Cardio2eSecurityTransaction receivedSecurityTransaction;
						for (Cardio2eBindingProvider provider : providers) {
							Map<String, BindingConfig> matchBindingConfigs = provider
									.getMatchBindingConfigs(transaction);
							for (String itemName : matchBindingConfigs.keySet()) {
								if (provider.getItem(itemName)
										.getAcceptedDataTypes()
										.contains(DecimalType.class)) { // Only
																		// sends
																		// NACK
																		// error
																		// values
																		// to
																		// Number
																		// Items
									try {
										receivedSecurityTransaction = (Cardio2eSecurityTransaction) transaction;
										newState = Cardio2eTransactionParser
												.transactionToState(
														receivedSecurityTransaction,
														provider.getItem(itemName));
										logger.debug(
												"Sending SECURITY NACK error code {} to the bus.",
												newState);
										eventPublisher.postUpdate(itemName,
												newState);
									} catch (Exception ex) {
										logger.warn(
												"Error in reporting error code for SECURITY NACK transaction: '{}'",
												ex);
									}
								}
							}
						}
					}
					try { // Ask current state to Cardio 2é
						if (transaction.getObjectNumber() == -1) {
							logger.debug(
									"NACK: {} transaction NOT ACCEPTED. {}. Will not ask for current state because no object information received.",
									transaction.getObjectType(),
									transaction.getErrorCodeDescription());
						} else {
							logger.debug(
									"NACK: {} transaction NOT ACCEPTED. {}. Will ask for current state.",
									transaction.getObjectType(),
									transaction.getErrorCodeDescription());
							transaction
									.setTransactionType(Cardio2eTransactionTypes.GET);
							com.sendTransaction(transaction);
						}
					} catch (Exception ex) {
						logger.warn("Error in requesting current state: '{}'",
								ex.toString());
					}
				}
				break;
			default:
				logger.debug(
						"No actions defined for received {} type transactions",
						transaction.getTransactionType());
			}
		}
	}

}