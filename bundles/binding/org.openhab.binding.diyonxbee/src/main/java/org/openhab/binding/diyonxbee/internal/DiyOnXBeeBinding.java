/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.diyonxbee.internal;

import java.io.File;
import java.io.IOException;
import java.util.Dictionary;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.openhab.binding.diyonxbee.DiyOnXBeeBindingProvider;
import org.openhab.core.binding.AbstractBinding;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.openhab.core.types.TypeParser;
import org.osgi.framework.BundleContext;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rapplogic.xbee.api.ApiId;
import com.rapplogic.xbee.api.PacketListener;
import com.rapplogic.xbee.api.XBee;
import com.rapplogic.xbee.api.XBeeException;
import com.rapplogic.xbee.api.XBeeResponse;
import com.rapplogic.xbee.api.zigbee.ZNetRxResponse;
import com.rapplogic.xbee.util.ByteUtils;
	

/**
 * 
 * @author juergen.richtsfeld@gmail.com
 * @since 1.8
 */
public class DiyOnXBeeBinding extends AbstractBinding<DiyOnXBeeBindingProvider> implements PacketListener, ManagedService {

	private static final Logger logger = 
		LoggerFactory.getLogger(DiyOnXBeeBinding.class);

	/**
	 * The BundleContext. This is only valid when the bundle is ACTIVE. It is set in the activate()
	 * method and must not be accessed anymore once the deactivate() method was called or before activate()
	 * was called.
	 */
//	private BundleContext bundleContext;

	
	/** 
	 * the serialPort used to communicate with the XBee
	 */
	private String serialPort = "";
	
	/**
	 * The baud rate to use when communicating with the XBee module. Defaults to 9600
	 */
	private int baudRate = 9600;

	private XBee xbee;
	
	
	public DiyOnXBeeBinding() {
	}
		
	
	public void activate() {
		logger.debug("Activate");
	}
	
	/**
	 * Called by the SCR to activate the component with its configuration read from CAS
	 * 
	 * @param bundleContext BundleContext of the Bundle that defines this component
	 * @param configuration Configuration properties for this component obtained from the ConfigAdmin service
	 */
	public void activate(final BundleContext bundleContext, final Map<String, Object> configuration) {
//		this.bundleContext = bundleContext;

		// the configuration is guaranteed not to be null, because the component definition has the
		// configuration-policy set to require. If set to 'optional' then the configuration may be null
	}
	
	/**
	 * Called by the SCR when the configuration of a binding has been changed through the ConfigAdmin service.
	 * @param configuration Updated configuration properties
	 */
	public void modified(final Map<String, Object> configuration) {
		// update the internal configuration accordingly
	}
	
	/**
	 * Called by the SCR to deactivate the component when either the configuration is removed or
	 * mandatory references are no longer satisfied or the component has simply been stopped.
	 * @param reason Reason code for the deactivation:<br>
	 * <ul>
	 * <li> 0 – Unspecified
     * <li> 1 – The component was disabled
     * <li> 2 – A reference became unsatisfied
     * <li> 3 – A configuration was changed
     * <li> 4 – A configuration was deleted
     * <li> 5 – The component was disposed
     * <li> 6 – The bundle was stopped
     * </ul>
	 */
	public void deactivate(final int reason) {
//		this.bundleContext = null;
		// deallocate resources here that are no longer needed and 
		// should be reset when activating this binding again
		
		if(xbee != null) {
			xbee.removePacketListener(this);
			xbee.close();
			xbee = null;
		}
	}

	/**
	 * @{inheritDoc}
	 */
	@Override
	protected void internalReceiveCommand(String itemName, Command command) {
		// the code being executed when a command was sent on the openHAB
		// event bus goes here. This method is only called if one of the 
		// BindingProviders provide a binding for the given 'itemName'.
		logger.debug("internalReceiveCommand({},{}) is called!", itemName, command);
	}
	
	/**
	 * @{inheritDoc}
	 */
	@Override
	protected void internalReceiveUpdate(String itemName, State newState) {
		// the code being executed when a state was sent on the openHAB
		// event bus goes here. This method is only called if one of the 
		// BindingProviders provide a binding for the given 'itemName'.
		logger.debug("internalReceiveUpdate({},{}) is called!", itemName, newState);
	}


	@Override
	public void processResponse(XBeeResponse response) {
    	if(response.getApiId() == ApiId.ZNET_RX_RESPONSE) {
    		final ZNetRxResponse rxResponse = (ZNetRxResponse) response;
    		final String message = ByteUtils.toString(rxResponse.getData());
    		final String remoteAddress = FormatUtil.readableAddress(rxResponse.getRemoteAddress64().getAddress());
    		
    		final String[] lines = message.split("\\r\\n");
    		
    		for (final String line : lines) {
				logger.debug("received message: '" + line + " from '" + remoteAddress);
				
				final int idxEquals = line.indexOf('=');
				if(idxEquals > 0) {
					final String key = line.substring(0, idxEquals);
					final String value = line.substring(idxEquals + 1, line.length());

					boolean updated = false; 
					for (final DiyOnXBeeBindingProvider provider : providers) {
						
						for (final String itemName : provider.getItemNames()) {
							final String id = provider.getId(itemName);
							final String remote = provider.getRemote(itemName);
							final boolean isSensor = provider.isSensor(itemName);
							
							if(isSensor && key.equals(id) && remote.equals(remoteAddress)) {
								final List<Class<? extends State>> availableTypes = provider.getAvailableItemTypes(itemName);
								final State state = TypeParser.parseState(availableTypes, value);
								if(state != null) {
									updated = true;
									eventPublisher.postUpdate(itemName, state);
								}
							}
						}
					}
					if(!updated) {
						logger.warn("unmatched item: key='" + key + "', value='" + value + "' from " + remoteAddress);
					}
				}
			}
    	}
	}


	@Override
	public void updated(Dictionary<String, ?> properties) throws ConfigurationException {
		{			
			final String serialPort = (String) properties.get("serialPort");
			if (StringUtils.isNotBlank(serialPort)) {
				this.serialPort = serialPort;
			}
		}

		{
			final String baudRate  = (String) properties.get("baudRate");
			if (StringUtils.isNotBlank(baudRate)) {
				this.baudRate = Integer.parseInt(baudRate);
			}
		}

		if(xbee != null) {
			xbee.removePacketListener(this);
			xbee.close();
			xbee = null;
		}
		
		xbee = new XBee();
		
		String canonical = null;
		try {
			File device = new File(serialPort);
			canonical = device.getCanonicalPath();
		} catch (IOException e1) {
			logger.info("unable to get canonical path for '" + serialPort + "'");
			canonical = serialPort;
		}
		try {
			logger.info("opening XBee communication on '" + canonical + "'");
			xbee.open(canonical, baudRate);
		} catch (XBeeException e) {
			logger.error("failed to open connection to XBee module", e);
			xbee = null;
			return;
		}

		xbee.addPacketListener(this);
	}
}
