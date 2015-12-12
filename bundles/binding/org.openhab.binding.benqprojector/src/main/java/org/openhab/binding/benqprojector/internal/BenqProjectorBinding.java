/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.benqprojector.internal;

import java.util.Dictionary;

import org.openhab.binding.benqprojector.BenqProjectorBindingProvider;
import org.openhab.binding.benqprojector.internal.transport.BenqProjectorNetworkTransport;
import org.openhab.binding.benqprojector.internal.transport.BenqProjectorSerialTransport;
import org.openhab.binding.benqprojector.internal.transport.BenqProjectorTransport;

import org.apache.commons.lang.StringUtils;
import org.openhab.core.binding.AbstractActiveBinding;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.IncreaseDecreaseType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.openhab.core.types.UnDefType;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This binding allows interaction with BenQ Projectors supporting and RS232
 * interface
 * 
 * @author Paul Hampson (cyclingengineer)
 * @since 1.6.0
 */
public class BenqProjectorBinding extends
		AbstractActiveBinding<BenqProjectorBindingProvider> implements
		ManagedService {

	private static final Logger logger = LoggerFactory
			.getLogger(BenqProjectorBinding.class);

	/**
	 * Transport for communicating with projector
	 */
	BenqProjectorTransport transport;

	/**
	 * the refresh interval which is used to poll values from the BenqProjector
	 * server (optional, defaults to 60000ms)
	 * 
	 */
	private long refreshInterval = 60000;

	/**
	 * Min & Max volume limits
	 */
	private final int MAX_VOLUME = 10;
	private final int MIN_VOLUME = 0;

	public BenqProjectorBinding() {
	}

	public void activate() {
	}

	public void deactivate() {
		transport.closeConnection();
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
		return "BenqProjector Refresh Service";
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	protected void execute() {
		for (BenqProjectorBindingProvider binding : super.providers) {
			for (String itemName : binding.getItemNames()) {
				logger.debug("Polling projector status for " + itemName);
				BenqProjectorBindingConfig cfg = binding
						.getConfigForItemName(itemName);
				State s = queryProjector(cfg);
				if (!(s instanceof UnDefType)) {
					eventPublisher.postUpdate(itemName, s);
					logger.debug(itemName + " status is " + s);
				} else {
					logger.debug(itemName
							+ " not updated as result was undefined");
				}
			}
		}
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	protected void internalReceiveCommand(String itemName, Command command) {
		for (BenqProjectorBindingProvider binding : super.providers) {
			if (binding.providesBindingFor(itemName)) {
				logger.debug("Process command " + command + " for " + itemName);
				BenqProjectorBindingConfig cfg = binding
						.getConfigForItemName(itemName);
				String resp = sendCommandToProjector(cfg, command);
				State s = cfg.mode.parseResponse(resp);
				if (!(s instanceof UnDefType)) {
					eventPublisher.postUpdate(itemName, s);
					logger.debug(itemName + " status is " + s);
				} else {
					logger.debug(itemName
							+ " not updated as result was undefined");
				}
			}
		}
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	public void updated(Dictionary<String, ?> config)
			throws ConfigurationException {
		if (config != null) {
			// to override the default refresh interval one has to add a
			// parameter to openhab.cfg like
			// <bindingName>:refresh=<intervalInMs>
			String refreshIntervalString = (String) config.get("refresh");
			if (StringUtils.isNotBlank(refreshIntervalString)) {
				refreshInterval = Long.parseLong(refreshIntervalString);
			}

			/* decide which transport to use - default is network */
			String modeString = (String) config.get("mode");
			if (StringUtils.isNotBlank(modeString)) {
				if (modeString.equalsIgnoreCase("serial")) {
					transport = new BenqProjectorSerialTransport();
				} else {
					/* default to network */
					transport = new BenqProjectorNetworkTransport();
				}
			} else {
				transport = new BenqProjectorNetworkTransport();
			}

			String deviceIdString = (String) config.get("deviceId");
			if (StringUtils.isNotBlank(deviceIdString)) {
				setProperlyConfigured(transport.setupConnection(deviceIdString));
			}
		}
	}

	/**
	 * Run query on the projector
	 * 
	 * @param cfg
	 *            Configuration of item to run query on
	 */
	private State queryProjector(BenqProjectorBindingConfig cfg) {
		String resp = transport.sendCommandExpectResponse(cfg.mode
				.getItemModeCommandQueryString());
		return cfg.mode.parseResponse(resp);
	}

	/**
	 * Send the command to the projector via configured transport and return the
	 * response string
	 * 
	 * @param cfg
	 *            Item binding configuration
	 * @param c
	 *            command to be sent
	 * @return Response string from projector
	 */
	private String sendCommandToProjector(BenqProjectorBindingConfig cfg,
			Command c) {
		Boolean cmdSent = false;
		String response = "";
		switch (cfg.mode) {
		case POWER:
		case MUTE:
			if (c instanceof OnOffType) {
				if ((OnOffType) c == OnOffType.ON) {
					response = transport.sendCommandExpectResponse(cfg.mode
							.getItemModeCommandSetString("ON"));
					cmdSent = true;
				} else if ((OnOffType) c == OnOffType.OFF) {
					response = transport.sendCommandExpectResponse(cfg.mode
							.getItemModeCommandSetString("OFF"));
					cmdSent = true;
				}
			}
			break;
		case VOLUME:
			if (c instanceof DecimalType) {
				/* get current volume */
				State currentVolState = queryProjector(cfg);
				int currentVol = ((DecimalType) currentVolState).intValue();

				int volLevel = ((DecimalType) c).intValue();
				if (volLevel > this.MAX_VOLUME) {
					volLevel = this.MAX_VOLUME;
				} else if (volLevel < this.MIN_VOLUME) {
					volLevel = this.MIN_VOLUME;
				}

				if (currentVol == volLevel)
					cmdSent = true;

				while (currentVol != volLevel) {
					if (currentVol < volLevel) {
						transport.sendCommandExpectResponse(cfg.mode
								.getItemModeCommandSetString("+"));
						currentVol++;
						cmdSent = true;
					} else {
						transport.sendCommandExpectResponse(cfg.mode
								.getItemModeCommandSetString("-"));
						currentVol--;
						cmdSent = true;
					}
				}
			} else if (c instanceof IncreaseDecreaseType) {
				if ((IncreaseDecreaseType) c == IncreaseDecreaseType.INCREASE) {
					transport.sendCommandExpectResponse(cfg.mode
							.getItemModeCommandSetString("+"));
					cmdSent = true;
				} else if ((IncreaseDecreaseType) c == IncreaseDecreaseType.DECREASE) {
					transport.sendCommandExpectResponse(cfg.mode
							.getItemModeCommandSetString("-"));
					cmdSent = true;
				}
			}
			/* get final volume */
			response = transport.sendCommandExpectResponse(cfg.mode
					.getItemModeCommandQueryString());
			break;
		case LAMP_HOURS:
			logger.warn("Cannot send command to set lamp hours - not a valid operation!");
			break;
		case SOURCE_NUMBER:
			if (c instanceof DecimalType) {
				DecimalType sourceIdx = (DecimalType) c;
				String cmd = BenqProjectorSourceMapping
						.getStringFromMapping(sourceIdx.intValue());
				if (cmd.isEmpty() == false) {
					response = transport.sendCommandExpectResponse(cfg.mode
							.getItemModeCommandSetString(cmd));
					cmdSent = true;
				}
			}
			break;
		case SOURCE_STRING:
			if (c instanceof StringType) {
				StringType sourceStr = (StringType) c;
				int mappingIdx = BenqProjectorSourceMapping
						.getMappingFromString(sourceStr.toString());
				if (mappingIdx != -1) // double check this is a valid mapping
				{
					response = transport.sendCommandExpectResponse(cfg.mode
							.getItemModeCommandSetString(sourceStr.toString()));
					cmdSent = true;
				}
			}
			break;
		default:
			logger.error("Unexpected Item Mode!");
			break;
		}

		if (cmdSent == false) {
			logger.error("Unable to convert item command to projector state: Command="
					+ c);
		}
		return response;
	}
}
