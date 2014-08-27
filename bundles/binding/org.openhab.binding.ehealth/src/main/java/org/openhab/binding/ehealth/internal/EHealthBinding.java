/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.ehealth.internal;

import static org.apache.commons.lang.StringUtils.defaultIfBlank;

import java.util.Dictionary;

import org.openhab.binding.ehealth.EHealthBindingProvider;
import org.openhab.binding.ehealth.protocol.EHealthDatagram;
import org.openhab.binding.ehealth.protocol.EHealthSensorPropertyName;
import org.openhab.binding.ehealth.protocol.SerialConnector;
import org.openhab.binding.ehealth.protocol.SerialEventProcessor;
import org.openhab.core.binding.AbstractBinding;
import org.openhab.core.events.EventPublisher;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.types.State;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Binding to receive data from the Libelium eHealth sensor kit.
 * 
 * @author Thomas Eichstaedt-Engelen
 * @since 1.6.0
 */
public class EHealthBinding extends AbstractBinding<EHealthBindingProvider> implements ManagedService, SerialEventProcessor {

	private static final Logger logger = LoggerFactory.getLogger(EHealthBinding.class);

	private String serialPort = null;

	private SerialConnector serialConnector = null;
	
	private EHealthDatagram previousDatagram;
	
	
	public void activate() {
	}

	public void deactivate() {
		serialConnector.disconnect();
	}

	public void setEventPublisher(EventPublisher eventPublisher) {
		this.eventPublisher = eventPublisher;
	}

	public void unsetEventPublisher(EventPublisher eventPublisher) {
		this.eventPublisher = null;
	}
	
	
	/**
	 * Processes a datagram being read from the serial port. A datagram comprises of eleven
	 * data elements separated by '@'.
	 * 
	 * @param data the new datagram to process/parse
	 */
	@Override
	public void processSerialData(String data) {
		logger.trace("Received raw datatagram '{}'", data);
		
    	String[] dataElements = data.split("@");
    	if (dataElements.length == 11) {
		    int airFlow = Integer.valueOf(defaultIfBlank(dataElements[0], "0"));  
			float temperature = Float.valueOf(defaultIfBlank(dataElements[1], "0"));
			
			float skinConductance = Float.valueOf(defaultIfBlank(dataElements[2], "0"));
			float skinResistance = Float.valueOf(defaultIfBlank(dataElements[3], "0"));
			float skinConductanceVoltage = Float.valueOf(defaultIfBlank(dataElements[4], "0"));
			
			int bpm = Integer.valueOf(defaultIfBlank(dataElements[5], "0"));
			int oxygenSaturation = Integer.valueOf(defaultIfBlank(dataElements[6], "0"));
			
			int bodyPosition = Integer.valueOf(defaultIfBlank(dataElements[7], "0"));
			
			float ecg = Float.valueOf(defaultIfBlank(dataElements[8], "0"));
			int emg = Integer.valueOf(defaultIfBlank(dataElements[9], "0"));
			
			int glucose = Integer.valueOf(defaultIfBlank(dataElements[10], "0"));
			
			EHealthDatagram datagram = new EHealthDatagram(
				airFlow, temperature, skinConductance, skinResistance, skinConductanceVoltage, bpm, oxygenSaturation, bodyPosition, ecg, emg, glucose);
			
			logger.trace("new datagram '{}'", datagram);
			
			postUpdate(datagram);
	    } else {
	    	logger.debug("The datagram '{}' doesn't match the protocol syntax", data);
	    }
	}

	
	private void postUpdate(EHealthDatagram datagram) {
		for (EHealthBindingProvider provider : providers) {
			for (String itemName : provider.getItemNames()) {
				EHealthSensorPropertyName propertyName = provider.getSensorPropertyName(itemName);
				
				// do only publish a new state if it has changed ...
				if (datagram.hasChanged(previousDatagram, propertyName)) {
					Number sensorValue = datagram.getRawData().get(propertyName);
					State newState = new DecimalType(sensorValue.toString());
					eventPublisher.postUpdate(itemName, newState);
				}
			}
		}
		
		this.previousDatagram = datagram;
	}


	/**
	 * @{inheritDoc}
	 */
	@Override
	public void updated(Dictionary<String, ?> config) throws ConfigurationException {

		logger.debug("Configuration updated, config {}", config != null ? true : false);

		if (config != null) {
			serialPort = (String) config.get("serialPort");
		}
		
		serialConnector = new SerialConnector(serialPort);
		try {
			serialConnector.connect();
			serialConnector.addSerialEventProcessor(this);
		} catch (EHealthException e) {
			logger.error("Error connecting to serial port '{}'", serialPort, e);
		}
	}

}
