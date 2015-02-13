/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.lightwaverf.internal;

import java.net.UnknownHostException;
import java.util.List;

import org.openhab.binding.lightwaverf.LightwaveRFBindingProvider;
import org.openhab.core.binding.AbstractBinding;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Neil Renaud
 * @since 1.6
 */
public class LightwaveRFBinding extends AbstractBinding<LightwaveRFBindingProvider> implements LightwaveRFMessageListener {

    private static final int POLL_TIME = 250;
    // LightwaveRF WIFI hub port.
    private static final int LIGHTWAVE_PORT_TO_SEND_TO = 9760;
    private static final int LIGHTWAVE_PORT_TO_RECEIVE_ON = 9761;
    // LightwaveRF WIFI hub IP Address or broadcast address
    private static final String LIGHTWAVE_IP = "255.255.255.255";
    private static final boolean SEND_REGISTER_ON_STARTUP = true;

	
	private final Logger logger = LoggerFactory.getLogger(LightwaveRFBinding.class);
	private LightwaverfConvertor messageConvertor; 
	private LightwaveRFReceiver receiver = null;
	private LightwaveRFSender sender = null;

	@Override
	public void activate() {
		try{
			messageConvertor = new LightwaverfConvertor();
			receiver = new LightwaveRFReceiver(messageConvertor, LIGHTWAVE_PORT_TO_RECEIVE_ON);
			receiver.start();
			receiver.addListener(this);
			sender = new LightwaveRFSender(LIGHTWAVE_IP, LIGHTWAVE_PORT_TO_SEND_TO, POLL_TIME);
			sender.start();
			if(SEND_REGISTER_ON_STARTUP){
				sender.sendUDP(messageConvertor.getRegistrationCommand());
			}
		}
		catch(UnknownHostException e){
			logger.error("Error creating LightwaveRFSender", e);
		}
	}

	@Override
	public void deactivate() {
		receiver.stop();
		receiver = null;
		sender.stop();
		sender = null;
	}

	@Override
	protected void internalReceiveCommand(String itemName, Command command) {
		// the code being executed when a command was sent on the openHAB
		// event bus goes here. This method is only called if one of the 
		// BindingProviders provide a binding for the given 'itemName'.
		logger.debug("internalReceiveCommand(" + itemName + ", " + command +") is called!");
        String roomId = getRoomId(itemName);
        String deviceId = getDeviceId(itemName);
        LightwaveRFCommand lightwaverfMessageString = messageConvertor.convertToLightwaveRfMessage(roomId, deviceId, command);
		sender.sendUDP(lightwaverfMessageString);
	}

	@Override
	protected void internalReceiveUpdate(String itemName, State newState) {
		// the code being executed when a state was sent on the openHAB
		// event bus goes here. This method is only called if one of the 
		// BindingProviders provide a binding for the given 'itemName'.
		logger.debug("internalReceiveUpdate(" + itemName + ", " + newState + ") is called!");
                String roomId = getRoomId(itemName);
                String deviceId = getDeviceId(itemName);
                LightwaveRFCommand lightwaverfMessageString = messageConvertor.convertToLightwaveRfMessage(roomId, deviceId, newState);
                sender.sendUDP(lightwaverfMessageString);
	}

//	LightwaveRFGenericBindingProvider bindingProvider;
	
	private String getRoomId(String itemName){
		for(LightwaveRFBindingProvider provider : providers){
			String roomId = provider.getRoomId(itemName);
			if(roomId != null){
				return roomId;
			}
		}
		return null;
	}

	private String getDeviceId(String itemName){
		for(LightwaveRFBindingProvider provider : providers){
			String deviceId = provider.getDeviceId(itemName);
			if(deviceId != null){
				return deviceId;
			}
		}
		return null;
	}
	
	private List<String> getItemName(String roomId, String deviceId){
		for(LightwaveRFBindingProvider provider : providers){
			List<String> itemNames = provider.getBindingItemsForRoomDevice(roomId, deviceId);
			if(itemNames != null && !itemNames.isEmpty()){
				return itemNames; 
			}
			
		}
		return null;
	}

	public void messageRecevied(LightwaveRFCommand command) {
		List<String> itemNames = getItemName(command.getRoomId(), command.getDeviceId());
		State state = command.getState();
		for(String itemName : itemNames){
			eventPublisher.postUpdate(itemName, state);
		}
	}
}
