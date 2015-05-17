/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.fritzaha.internal.hardware.callbacks;

import java.io.StringReader;
import java.math.BigDecimal;
import java.util.ArrayList;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.openhab.binding.fritzaha.internal.hardware.FritzahaWebInterface;
import org.openhab.binding.fritzaha.internal.hardware.interfaces.FritzahaOutletMeter.MeterType;
import org.openhab.binding.fritzaha.internal.model.DeviceModel;
import org.openhab.binding.fritzaha.internal.model.DevicelistModel;
import org.openhab.core.library.types.DecimalType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Callback implementation for updating multiple numbers decoded from a xml
 * response. Supports reauthorization.
 * 
 * In future versions this callback type could replace the other two used AVM
 * switch commands <b>getswitchpower</b> and <b>getswitchenergy</b>. Replacement
 * should be combined this XML reponse caching because up to 3 item bindings can
 * be updated by one request without loosing accuracy.
 * 
 * @author Robert Bausdorf
 * @since 1.6
 */
@SuppressWarnings("restriction")
public class FritzahaWebserviceUpdateXmlCallback extends FritzahaReauthCallback {
	static final Logger logger = LoggerFactory
			.getLogger(FritzahaWebserviceUpdateXmlCallback.class);

	static {
	}
	/**
	 * Item to update
	 */
	private String itemName;
	/**
	 * Meter type to update
	 */
	private MeterType type;
	/**
	 * Ain of requested device
	 */
	private String deviceAin;

	/**
	 * Constructor for retriable authentication and state updating
	 * 
	 * @param path
	 *            Path to HTTP interface
	 * @param args
	 *            Arguments to use
	 * @param webIface
	 *            Web interface to use
	 * @param httpMethod
	 *            Method used
	 * @param retries
	 *            Number of retries
	 * @param itemName
	 *            Name of item to update
	 */
	public FritzahaWebserviceUpdateXmlCallback(String path, String args,
			MeterType type, FritzahaWebInterface webIface, Method httpMethod,
			int retries, String itemName, String ain) {
		super(path, args, webIface, httpMethod, retries);
		this.itemName = itemName;
		this.type = type;
		this.deviceAin = ain;
	}

	/**
	 * {@inheritDoc}
	 */
	public void execute(int status, String response) {
		super.execute(status, response);
		if (validRequest) {
			logger.trace("Received State response " + response + " for item "
					+ itemName);
			try {
				JAXBContext jaxbContext = JAXBContext
						.newInstance(DevicelistModel.class);
				Unmarshaller jaxbUM = jaxbContext.createUnmarshaller();

				DevicelistModel model = (DevicelistModel) jaxbUM
						.unmarshal(new StringReader(response));
				ArrayList<DeviceModel> list = model.getDevicelist();
				for (DeviceModel device : list) {
					if (device.getIdentifier().equals(this.deviceAin)) {
						BigDecimal meterValueScaled = new BigDecimal(0);
						switch (type) {
						case POWER:
							meterValueScaled = device.getPowermeter()
									.getPower().scaleByPowerOfTen(-3);
							break;
						case ENERGY:
							meterValueScaled = device.getPowermeter()
									.getEnergy();
							break;
						case TEMPERATURE:
							meterValueScaled = device.getTemperature()
									.getCelsius().scaleByPowerOfTen(-1);
							break;
						default:
							logger.warn("unknown meter type: " + type);
							break;
						}
						logger.debug(device.toString());
						webIface.postUpdate(itemName, new DecimalType(
								meterValueScaled));
					} else {
						logger.trace("device " + device.getIdentifier()
								+ " was not requested");
					}
				}
			} catch (JAXBException e) {
				logger.error(e.getLocalizedMessage(), e);
			}
		}
	}
}
