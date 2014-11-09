/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.xbcdrc.internal;

import java.util.Dictionary;

import org.hid4java.HidDevice;
import org.hid4java.HidException;
import org.hid4java.HidManager;
import org.hid4java.HidServices;
import org.hid4java.HidServicesListener;
import org.hid4java.event.HidServicesEvent;
import org.openhab.binding.xbcdrc.XBCDrcBindingConfig;
import org.openhab.binding.xbcdrc.XBCDrcBindingProvider;
import org.apache.commons.lang.StringUtils;
import org.openhab.core.binding.AbstractActiveBinding;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * 
 * @author magcode
 * @since 1.6.0-SNAPSHOT
 */
public class XBCDrcBinding extends AbstractActiveBinding<XBCDrcBindingProvider>
		implements ManagedService, HidServicesListener {
	private long timestamp;
	HidServices hidServices;
	private static final Logger logger = LoggerFactory
			.getLogger(XBCDrcBinding.class);

	/**
	 * the refresh interval which is used to poll values from the XBCDrc server
	 * (optional, defaults to 60000ms)
	 */
	private long refreshInterval = 60000;

	public XBCDrcBinding() {
	}

	public void activate() {
		bindHid();
	}

	public void deactivate() {
		if (hidServices != null) {
			hidServices.removeUsbServicesListener(this);
		}
	}

	private void bindHid() {

		try {
			hidServices = HidManager.getHidServices();
			hidServices.addHidServicesListener(this);
			HidDevice xboxRC = hidServices.getHidDevice(0x045e, 0x0284, null);
			receiving(xboxRC);
		} catch (HidException e) {
			logger.error("Failure initializing the HID", e);
		} catch (Exception e1) {
			logger.error("Failure initializing the HID", e1);
		}
		
	}

	private void receiving(HidDevice xboxRC) {
		XBCDrcBindingConfig config = null;

		int val;
		// Prepare to read a single data packet
		boolean moreData = true;
		while (moreData) {
			byte data[] = new byte[7];
			// This method will now block for 500ms or until data is read
			val = xboxRC.read(data, 500);

			switch (val) {
			case -1:
				System.err.println(xboxRC.getLastErrorMessage());
				break;
			case 0:
				moreData = false;
				break;
			default:
				StringBuilder sb = new StringBuilder();
				for (byte b : data) {
					sb.append(String.format("%02X", b));
				}
				String code = sb.substring(1, 8);

				long currentTS = System.currentTimeMillis();

				if (currentTS > timestamp + 250) {
					if (code.startsWith("0")) {

						for (XBCDrcBindingProvider provider : providers) {
							config = provider.getConfigForRcCode(code);
							if (config != null) {
								eventPublisher.postUpdate(config.getItem().getName(), OnOffType.ON);
							} else {
								logger.info("Unknow RC code " + code);
							}
						}
						timestamp = System.currentTimeMillis();
					}
				}

				break;
			}
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
		return "XBCDrc Refresh Service";
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	protected void execute() {
		logger.debug("execute() method is called!");
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	protected void internalReceiveCommand(String itemName, Command command) {
		// the code being executed when a command was sent on the openHAB
		// event bus goes here. This method is only called if one of the
		// BindingProviders provide a binding for the given 'itemName'.
		logger.debug("internalReceiveCommand() is called!");
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	protected void internalReceiveUpdate(String itemName, State newState) {
		// the code being executed when a state was sent on the openHAB
		// event bus goes here. This method is only called if one of the
		// BindingProviders provide a binding for the given 'itemName'.
		logger.debug("internalReceiveCommand() is called!");
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

			// read further config parameters here ...

			setProperlyConfigured(true);
		}
	}

	@Override
	public void hidDeviceAttached(HidServicesEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	public void hidDeviceDetached(HidServicesEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	public void hidFailure(HidServicesEvent event) {
		logger.error("HID Failure");

	}

}
