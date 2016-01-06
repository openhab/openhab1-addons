/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.nibeheatpump.internal;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.xml.bind.DatatypeConverter;

import org.apache.commons.lang.StringUtils;
import org.openhab.binding.nibeheatpump.NibeHeatPumpBindingProvider;
import org.openhab.binding.nibeheatpump.protocol.NibeHeatPumpConnector;
import org.openhab.binding.nibeheatpump.protocol.NibeHeatPumpDataParser;
import org.openhab.binding.nibeheatpump.protocol.NibeHeatPumpSimulator;
import org.openhab.binding.nibeheatpump.protocol.NibeHeatPumpDataParser.VariableInformation;
import org.openhab.binding.nibeheatpump.protocol.NibeHeatPumpSerialConnector;
import org.openhab.binding.nibeheatpump.protocol.NibeHeatPumpUDPConnector;
import org.openhab.core.binding.AbstractBinding;
import org.openhab.core.library.types.DecimalType;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * Binding to receive data from Nibe heat pumps.
 *
 * @author Pauli Anttila
 * @author John Cocula -- work around bad data from F750
 * @since 1.3.0
 */
public class NibeHeatPumpBinding extends
		AbstractBinding<NibeHeatPumpBindingProvider> implements ManagedService {

	private static final Logger logger = LoggerFactory.getLogger(NibeHeatPumpBinding.class);

	/* configuration variables for communication */
	private int udpPort = 9999;
	private String serialPort = null;
	private boolean simulateHeatPump = false;

	/* configuration variables for parsing */
	private int modelNo = 1145;
	
	/** Thread to handle messages from heat pump */
	private NibeHeatPumpMessageListener messageListener = null;


	public void activate() {
		logger.debug("Activate");
	}

	public void deactivate() {
		logger.debug("Deactivate");
		messageListener.setInterrupted(true);
		messageListener.interrupt();
	}
	
	
	/**
	 * @{inheritDoc
	 */
	@Override
	public void updated(Dictionary<String, ?> config) throws ConfigurationException {

		logger.debug("Configuration updated, config {}", config != null ? true : false);

		if (config != null) {
			String PortString = (String) config.get("udpPort");
			if (StringUtils.isNotBlank(PortString)) {
				udpPort = Integer.parseInt(PortString);
			}
			
			String modelNoString = (String) config.get("modelNo");
			if (StringUtils.isNotBlank(modelNoString)) {
				modelNo = Integer.parseInt(modelNoString);
			}

			serialPort = (String) config.get("serialPort");

			String testPortString = (String) config.get("simulate");
			if (StringUtils.isNotBlank(testPortString)) {
				simulateHeatPump = Boolean.parseBoolean(testPortString);
			}
			
			if (messageListener != null) {

				logger.debug("Close previous message listener");

				messageListener.setInterrupted(true);
				try {
					messageListener.join();
				} catch (InterruptedException e) {
					logger.info("Previous message listener closing interrupted", e);
				}
			}
			
			messageListener = new NibeHeatPumpMessageListener();
			messageListener.start();
		}
	}

	/**
	 * The NibeHeatPumpMessageListener runs as a separate thread. The Thread is listening 
	 * message from heat pump and send updates to openHAB bus.
	 */
	private class NibeHeatPumpMessageListener extends Thread {

		private boolean interrupted = false;
		
		public void setInterrupted(boolean interrupted) {
			this.interrupted = interrupted;
		}

		@Override
		public void run() {
			logger.debug("Nibe heatpump message listener started");

			NibeHeatPumpConnector connector;
			
			if (simulateHeatPump == true) {
				connector = new NibeHeatPumpSimulator();
			} else if (serialPort != null) {
				connector = new NibeHeatPumpSerialConnector(serialPort);
			} else {
				connector = new NibeHeatPumpUDPConnector(udpPort);
			}
			
			try {
				connector.connect();
			} catch (NibeHeatPumpException e) {
				logger.error("Error occured when connecting to heat pump", e);
				
				logger.warn("Closing Nibe heatpump message listener");

				// exit
				interrupted = true;
			}

			// as long as no interrupt is requested, continue running
			while (!interrupted) {
				try {
					// Wait a packet (blocking)
					byte[] data = connector.receiveDatagram();

					if (logger.isDebugEnabled()) {
						logger.debug("Received data (len={}): {}", data.length, DatatypeConverter.printHexBinary(data));
					}

					Hashtable<Integer, Short> regValues = NibeHeatPumpDataParser.ParseData(data);

					if (regValues != null) {

						Enumeration<Integer> keys = regValues.keys();

						while (keys.hasMoreElements()) {

							int key = keys.nextElement();
							double value = regValues.get(key);

							VariableInformation variableInfo;
							if ( modelNo == 750 )	{
								variableInfo = NibeHeatPumpDataParser.VARIABLE_INFO_F750.get(key);
							} else {
								variableInfo = NibeHeatPumpDataParser.VARIABLE_INFO_F1145_F1245.get(key);
							}

							if (variableInfo == null) {
								logger.debug("Unknown variable {}", key);
							} else {
								// 32bit handling:
								if ( variableInfo.dataType == NibeHeatPumpDataParser.NibeDataType.U32 || variableInfo.dataType == NibeHeatPumpDataParser.NibeDataType.S32 )
								{
									logger.debug("{}:32bit dataType", key);
									int keyValue, keyPlusOneValue;
									try {
										keyValue        = (int) regValues.get(key) & 0xffff;		// Handling the short-value as unsigned when casting to int
										keyPlusOneValue = (int) regValues.get(key + 1) & 0xffff;
									} catch (Exception ex) {
										logger.error("Received bad data key={}; skipping.", key);
										continue;
									}
									if (logger.isDebugEnabled()) {
										logger.debug("{}: {} {}", key, Integer.toHexString(keyValue), Integer.toHexString(keyPlusOneValue));
									}
									value = (int) keyPlusOneValue << 16 | keyValue;
								}
								
								value = value / variableInfo.factor;
								BigDecimal bd = new BigDecimal(value).setScale( (int) Math.log10(variableInfo.factor) , RoundingMode.HALF_EVEN );
								org.openhab.core.types.State state = new DecimalType(bd);	// Updates the item with correct resolution based on 'variableInfo.factor'

								logger.debug("{}:{}={}", key, variableInfo.variable, value);

								for (NibeHeatPumpBindingProvider provider : providers) {
									for (String itemName : provider
											.getItemNames()) {
										int itemId = provider
												.getItemId(itemName);
										if (key == itemId) {
											eventPublisher.postUpdate(itemName,
													state);
										}
									}
								}
							}
						}

					}
				} catch (NibeHeatPumpException e) {
					logger.error("Error occured when received data from heat pump", e);
				}
			}
			
			try {
				connector.disconnect();
			} catch (NibeHeatPumpException e) {
				logger.error("Error occured when disconnecting form heat pump", e);
			}
		}

	}

}
