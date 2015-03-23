/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.resolvbus.internal;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.Dictionary;
import java.util.Map;

import org.openhab.binding.resolvbus.ResolVBUSBindingProvider;
import org.openhab.binding.resolvbus.model.ResolVBUSConfig;
import org.openhab.binding.resolvbus.model.ResolVBUSDevice;
import org.openhab.binding.resolvbus.model.ResolVBUSField;
import org.openhab.binding.resolvbus.model.ResolVBUSInputStream;
import org.openhab.binding.resolvbus.model.ResolVBUSPacket;
import org.openhab.binding.resolvbus.model.ResolVBUSValue;

import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;

import org.apache.commons.lang.StringUtils;
import org.openhab.core.binding.AbstractActiveBinding;
import org.openhab.core.library.types.DateTimeType;
import org.openhab.core.library.types.DecimalType;

import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thoughtworks.xstream.XStream;

import com.thoughtworks.xstream.io.xml.StaxDriver;

/**
 * Implement this class if you are going create an actively polling service
 * like querying a Website/Device.
 * 
 * @author Michael Heckmann
 * @since 1.7.0
 */
public class ResolVBUSBinding extends AbstractActiveBinding<ResolVBUSBindingProvider> implements ManagedService, ResolVBUSListener{

	private static final Logger logger = 
		LoggerFactory.getLogger(ResolVBUSBinding.class);

	private ResolVBUSReceiver packetReceiver; 
	private String host;
	private int port;
	private ResolVBUSConfig config;
	private String password = "vbus";
	private String serialPort;
	private int inputMode;
	private static final int INPUT_MODE_LAN = 10;
	private static final int INPUT_MODE_SERIAL = 20;	

	
	/**
	 * The BundleContext. This is only valid when the bundle is ACTIVE. It is set in the activate()
	 * method and must not be accessed anymore once the deactivate() method was called or before activate()
	 * was called.
	 */
	private BundleContext bundleContext;

	
	/** 
	 * the refresh interval which is used to poll values from the ResolVBUS
	 * server (optional, defaults to 60000ms)
	 */
	private long refreshInterval = 60000;
	
	
	public ResolVBUSBinding() {
		
	}
		
	
	/**
	 * Called by the SCR to activate the component with its configuration read from CAS
	 * 
	 * @param bundleContext BundleContext of the Bundle that defines this component
	 * @param configuration Configuration properties for this component obtained from the ConfigAdmin service
	 */
	public void activate(final BundleContext bundleContext, final Map<String, Object> configuration) {
		this.bundleContext = bundleContext;

		// the configuration is guaranteed not to be null, because the component definition has the
		// configuration-policy set to require. If set to 'optional' then the configuration may be null
		// to override the default refresh interval one has to add a 
		// parameter to openhab.cfg like <bindingName>:refresh=<intervalInMs>

		if (configuration != null) {
			
			String refreshIntervalString = (String) configuration.get("refresh");
			String serialString = (String) configuration.get("serialport");
			String hostString = (String) configuration.get("host");
			String portString = (String) configuration.get("port");
			String pwString = (String) configuration.get("password");
			
			if (StringUtils.isNotBlank(hostString) && (StringUtils.isNotBlank(serialString))) {
				logger.debug("You cannot define a LAN and a serial/USB interface");
				return;
			}
			
			if (StringUtils.isNotBlank(refreshIntervalString)) {
				refreshInterval = Long.parseLong(refreshIntervalString);
			}
						
			if (StringUtils.isNotBlank(hostString)) {
				host = hostString;
				inputMode = INPUT_MODE_LAN;
			}
			
			if (StringUtils.isNotBlank(portString)) {
				port = Integer.parseInt(portString);
			}
						
			if (StringUtils.isNotBlank(portString)) {
				password = pwString;
			}
			
			if (StringUtils.isNotBlank(serialString)) {
				serialPort = serialString;
				inputMode = INPUT_MODE_SERIAL;
			}
			
	
			// read further config parameters here ...
		
			loadXMLConfig();
						
			// Create LAN oder Serial Receiver the parsed information to the listener
			switch (inputMode) {
			
			case INPUT_MODE_LAN: {
				packetReceiver = new ResolVBUSLANReceiver(this);
				// make sure that there is no listener running
				packetReceiver.stopListener();
				packetReceiver.initializeReceiver(host,port,password,config);
				break;
			}
			case INPUT_MODE_SERIAL: {
				packetReceiver = new ResolVBUSSerialReceiver(this);
				packetReceiver.stopListener();
				packetReceiver.initializeReceiver(serialPort,password, config);
				break;
			}
			}
	
			// start the listener
			new Thread(packetReceiver).start();
			setProperlyConfigured(true);
		
		}
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
		this.bundleContext = null;
		logger.debug("Stoppig ResolVBUS listener...");
		if (packetReceiver != null)
			packetReceiver.stopListener();
	}

	
	/**
	 * @{inheritDoc}
	 */
	@Override
	protected long getRefreshInterval() {
		return refreshInterval;
	}

	/**
	 * @{inheritDoc}
	 */
	@Override
	protected String getName() {
		return "ResolVBUS Refresh Service";
	}
	
	/**
	 * @{inheritDoc}
	 */
	@Override
	protected void execute() {
		// the frequently executed code (polling) goes here ...
//		logger.debug("execute() method is called!");
	}

	/**
	 * @{inheritDoc}
	 */
	@Override
	protected void internalReceiveCommand(String itemName, Command command) {
		// the code being executed when a command was sent on the openHAB
		// event bus goes here. This method is only called if one of the 
		// BindingProviders provide a binding for the given 'itemName'.
//		logger.debug("internalReceiveCommand({},{}) is called!", itemName, command);
	}
	
	/**
	 * @{inheritDoc}
	 */
	@Override
	protected void internalReceiveUpdate(String itemName, State newState) {
		// the code being executed when a state was sent on the openHAB
		// event bus goes here. This method is only called if one of the 
		// BindingProviders provide a binding for the given 'itemName'.
//		logger.debug("internalReceiveUpdate({},{}) is called!", itemName, newState);
	}


	public void publishUpdate(String name, String value) {

		DecimalType valueAsType = new DecimalType(value);
		eventPublisher.postUpdate(name, valueAsType);
		
	}
	
	public void publishUpdateTime(String name, double time) {

		Calendar today = Calendar.getInstance();
		today.setTimeInMillis((new Date().getTime()));
		today.set(Calendar.HOUR_OF_DAY, (int) time/60);
		today.set(Calendar.MINUTE, ((int) time) % 60);
		DateTimeType valueAsType = new DateTimeType(today);
		eventPublisher.postUpdate(name, valueAsType);
		
	}


	public void updated(Dictionary<String, ?> properties)
			throws ConfigurationException {

	}
	
	public void loadXMLConfig()  {

//		try {
//			JAXBContext jc = JAXBContext.newInstance(ResolVBUSConfig.class);
//			Unmarshaller unmarshaller = jc.createUnmarshaller();
//			config = (ResolVBUSConfig) unmarshaller.unmarshal(getClass().getResourceAsStream("/xml/VBusSpecificationResol.xml"));
//			if (config == null) {
//				logger.debug("Error reading XML Configuration");
//			}
//		} catch (JAXBException e) {
//			logger.debug("Couldn't read XML Config: "+e.getMessage()+" ");
//			e.printStackTrace();
//		}
		
		
		URL entry = FrameworkUtil.getBundle(ResolVBUSConfig.class).getEntry("xml/VBusSpecificationResol.xml");

		if (entry == null) {
			config = null;
			logger.error("Unable to load VBusSpecificationResol.xml");
			return;
		}
		
		XStream xstream = new XStream(new StaxDriver());
		xstream.alias("vbusSpecification",ResolVBUSConfig.class);
		xstream.alias("device",ResolVBUSDevice.class);
		xstream.alias("field",ResolVBUSField.class);
		xstream.alias("packet",ResolVBUSPacket.class);
		xstream.alias("value", ResolVBUSValue.class);
		
		xstream.processAnnotations(ResolVBUSConfig.class);
		
		try {
		InputStream x = entry.openStream();
		config = (ResolVBUSConfig) xstream.fromXML(x);
		} catch (IOException e) {
			logger.error("Couldn't read XML Config: "+e.getMessage());
		}
		
	}


	public void processInputStream(ResolVBUSInputStream vbusStream) {
		
		ResolVBUSPacket packet = config.getPacketWithDevice(vbusStream.getSourceAddress());
		
		if (packet == null) {
			logger.debug("No XML-Packet found for address: "+ vbusStream.getSourceAddress());
		}
		
		if (packet != null) {
			for (ResolVBUSBindingProvider provider : providers) {
				for (String itemName : provider.getItemNames()) {
					double value;
					String itemConfig = provider.getName(itemName);
					logger.debug("Updating item: "+itemName+" who has config: "+itemConfig);
					if (itemConfig != null) {
						ResolVBUSField field = packet.getFieldWithName(itemConfig);
						if (field == null) {
							logger.debug("No XML Field found for: "+itemConfig);
							continue;
						}
						
						value = ResolVBUSUtility.getValueWithVBUSField(vbusStream, field);
						
						if (field.getFormat()!= null && field.getFormat().equalsIgnoreCase("t")) {
							publishUpdateTime(itemName, value);
							continue;
						}
						
						publishUpdate(itemName, new Double(value).toString());
					}
					
					
				}
				
			}
		}
				
		
		
	}
		
}
