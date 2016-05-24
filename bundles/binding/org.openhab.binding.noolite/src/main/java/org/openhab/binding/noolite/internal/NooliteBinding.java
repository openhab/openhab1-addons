/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.noolite.internal;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.openhab.binding.noolite.NooliteBindingProvider;
import org.openhab.binding.noolite.internal.noolite4j.RX2164;
import org.openhab.binding.noolite.internal.noolite4j.watchers.BatteryState;
import org.openhab.binding.noolite.internal.noolite4j.watchers.Notification;
import org.openhab.binding.noolite.internal.noolite4j.watchers.Watcher;
import org.openhab.core.binding.AbstractActiveBinding;
import org.openhab.core.events.EventPublisher;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implement this class if you are going create an actively polling service like
 * querying a Website/Device.
 * 
 * @author Petros
 * @since 1.0.0
 */
public class NooliteBinding extends AbstractActiveBinding<NooliteBindingProvider> {

    private static final Logger logger = LoggerFactory.getLogger(NooliteBinding.class);
    // private boolean isSetPublisher;

    /**
     * The BundleContext. This is only valid when the bundle is ACTIVE. It is
     * set in the activate() method and must not be accessed anymore once the
     * deactivate() method was called or before activate() was called.
     */
    private BundleContext bundleContext;

    private static EventPublisher ep;
    /**
     * the refresh interval which is used to poll values from the Noolite server
     * (optional, defaults to 60000ms)
     */
    private long refreshInterval = 60000;
    private boolean rx, tx;
    TXChoose pc;
    RX2164 rxw = new RX2164();;

    public NooliteBinding() {
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
    public void activate(final BundleContext bundleContext, final Map<String, Object> configuration) {
	this.bundleContext = bundleContext;

	// the configuration is guaranteed not to be null, because the component
	// definition has the
	// configuration-policy set to require. If set to 'optional' then the
	// configuration may be null

	// to override the default refresh interval one has to add a
	// parameter to openhab.cfg like <bindingName>:refresh=<intervalInMs>
	String refreshIntervalString = (String) configuration.get("refresh");
	if (StringUtils.isNotBlank(refreshIntervalString)) {
	    refreshInterval = Long.parseLong(refreshIntervalString);
	}

	String rxS = (String) configuration.get("RX");

	if (StringUtils.isNotBlank(rxS)) {

	    if (rxS.equals("On")) {
		rx = true;
	    }
	}

	String PC = (String) configuration.get("PC");

	if (StringUtils.isNotBlank(PC)) {

	    if (PC.equals("8")) {
		tx = true;
		pc = new TXChoose((byte) 8);
	    } else if (PC.equals("16")) {
		tx = true;
		pc = new TXChoose((byte) 16);
	    } else if (PC.equals("32")) {
		tx = true;
		pc = new TXChoose((byte) 32);
	    }
	}

	setProperlyConfigured(true);

	if (tx) {

	    pc.open();
	}

	if (rx) {

	    Watcher watcher = new Watcher() {

		@Override
		public void onNotification(Notification notification) {
		    updateValues(notification);
		}
	    };
	    rxw.open();
	    rxw.addWatcher(watcher);
	    rxw.start();
	}
    }

    protected void updateValues(Notification notification) {

	for (NooliteBindingProvider provider : providers) {
	    for (String itemname : provider.getItemNames()) {

		if ((provider.getType(itemname).equals("Receive"))
			&& (provider.getChannel(itemname).equals("bindflag"))) {
		    if (notification.getType().name().equals("BIND"))
			eventPublisher.postUpdate(itemname, OnOffType.OFF);
		}

		if ((provider.getType(itemname).equals("Receive")) && (provider.getChannel(itemname).equals("test"))) {
		    eventPublisher.postUpdate(itemname, StringType.valueOf(String.valueOf(notification.getChannel())));
		}

		if ((provider.getType(itemname).equals("Receive"))
			&& (String.valueOf(notification.getChannel()).equals(provider.getChannel(itemname)))) {

		    logger.debug("Channel #" + provider.getChannel(itemname));
		    logger.debug("Type: " + provider.getType(itemname));
		    logger.debug("DeviceType: " + provider.getDeviceType(itemname));
		    logger.debug("Command: " + notification.getType().name());
		    
		    String[] DeviceType = provider.getDeviceType(itemname).split("_");
		    if (notification.getType().name().equals("UNBIND"))
			rxw.unbindChannel(notification.getChannel());

		    if (DeviceType[0].contains("PT11")) {
			if (notification.getType().name().equals("TURN_OFF")) {
			    break;
			} else if (DeviceType[1].equals("t")) {
			    eventPublisher.postUpdate(itemname,
				    DecimalType.valueOf(String.valueOf(notification.getValue("temp"))));
			} else if (DeviceType[1].equals("h")) {
			    eventPublisher.postUpdate(itemname,
				    DecimalType.valueOf(String.valueOf(notification.getValue("humi"))));
			} else if (DeviceType[1].equals("batt")) {
			    BatteryState battery = (BatteryState) notification.getValue("battery");
			    eventPublisher.postUpdate(itemname, StringType.valueOf(String.valueOf(battery.name())));
			}
		    } else if (DeviceType[0].contains("PU")) {
			eventPublisher.postUpdate(itemname,
				StringType.valueOf(String.valueOf(notification.getType().name())));
		    }

		}

	    }
	}
    }

    /**
     * Called by the SCR when the configuration of a binding has been changed
     * through the ConfigAdmin service.
     * 
     * @param configuration
     *            Updated configuration properties
     */
    public void modified(final Map<String, Object> configuration) {
	// update the internal configuration accordingly
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
	if (rx)
	    rxw.close();
	if (tx)
	    pc.close();
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
	return "Noolite Refresh Service";
    }

    /**
     * @{inheritDoc}
     */
    @Override
    protected void execute() {

    }

    /**
     * @{inheritDoc}
     */
    @Override
    protected void internalReceiveCommand(String itemName, Command command) {

	logger.debug("internalReceiveCommand({},{}) is called!", itemName, command);

	for (NooliteBindingProvider provider : providers) {
	    for (String itemname : provider.getItemNames()) {
		if ((itemname.equals(itemName)) && (provider.getChannel(itemName).equals("bind"))) {

		    if (provider.getType(itemName).equals("Receive")) {
			rxw.bindChannel(Byte.parseByte(command.toString()));
		    } else {
			pc.bindChannel(Byte.parseByte(command.toString()));
		    }
		    logger.debug("binding " + command.toString() + " channel");

		} else if ((itemname.equals(itemName)) && (provider.getChannel(itemName).equals("unbind"))) {

		    if (provider.getType(itemName).equals("Receive")) {
			rxw.unbindChannel(Byte.parseByte(command.toString()));
			logger.debug("unbinding Receive " + command.toString() + " channel");
		    } else {
			pc.unbindChannel(Byte.parseByte(command.toString()));
			logger.debug("unbinding Send " + command.toString() + " channel");
		    }
		    logger.debug("unbinding " + command.toString() + " channel");

		} else if ((itemname.equals(itemName)) && (provider.getChannel(itemName).equals("unbindAll"))) {

		    if (provider.getType(itemName).equals("Receive")) {
			rxw.unbindAllChannels();
		    }
		    logger.debug("unbinding all channels");

		} else if ((itemname.equals(itemName))
			&& (provider.getItemType(itemname).toString().contains("SwitchItem")) && !(provider.getChannel(itemName).equals("bind"))) {

		    if (command.toString().equals("ON")) {
			pc.turnOn(Byte.parseByte(provider.getChannel(itemName)));
			logger.debug(provider.getChannel(itemName));
		    } else if (command.toString().equals("OFF")) {
			pc.turnOff(Byte.parseByte(provider.getChannel(itemName)));
			logger.debug(provider.getChannel(itemName));
		    }
		} else if ((itemname.equals(itemName))
			&& (provider.getItemType(itemname).toString().contains("DimmerItem")) ) {
		    pc.setLevel(Byte.parseByte(provider.getChannel(itemName)),(byte) Integer.parseInt(command.toString()));
			//logger.debug(provider.getChannel(itemName));
		} else if ((itemname.equals(itemName))
			&& (provider.getItemType(itemname).toString().contains("StringItem")) ) {
		    String[] Colors = command.toString().split(",");
		    
		    byte red = (byte)(Integer.parseInt(Colors[0])*2.55);
		    byte green = (byte)(Integer.parseInt(Colors[1])*2.55);
		    byte blue = (byte)(Integer.parseInt(Colors[2])*2.55);
		    
		    pc.setLevelRGB(Byte.parseByte(provider.getChannel(itemName)), red , green, blue);
		    
		}

	    }
	}
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

}
