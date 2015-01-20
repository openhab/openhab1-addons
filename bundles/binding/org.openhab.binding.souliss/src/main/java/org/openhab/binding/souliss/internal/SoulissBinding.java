/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.souliss.internal;

import java.awt.Color;
import java.io.IOException;
import java.util.Dictionary;
import java.util.Enumeration;

import org.openhab.binding.souliss.SoulissBindingProvider;
import org.openhab.binding.souliss.internal.network.typicals.Constants;
import org.openhab.binding.souliss.internal.network.typicals.MonitorThread;
import org.openhab.binding.souliss.internal.network.typicals.RefreshHEALTYThread;
import org.openhab.binding.souliss.internal.network.typicals.RefreshSUBSCRIPTIONThread;
import org.openhab.binding.souliss.internal.network.typicals.SoulissGenericTypical;
import org.openhab.binding.souliss.internal.network.typicals.SoulissT11;
import org.openhab.binding.souliss.internal.network.typicals.SoulissT12;
import org.openhab.binding.souliss.internal.network.typicals.SoulissT16;
import org.openhab.binding.souliss.internal.network.typicals.SoulissT19;
import org.openhab.binding.souliss.internal.network.typicals.SoulissT22;
import org.openhab.binding.souliss.internal.network.typicals.StateTraslator;

import org.openhab.binding.souliss.internal.network.typicals.SoulissNetworkParameter;
import org.openhab.binding.souliss.internal.network.udp.SendDispatcherThread;
import org.openhab.binding.souliss.internal.network.udp.UDPServerThread;

import org.openhab.core.binding.AbstractActiveBinding;
import org.openhab.core.binding.BindingProvider;
import org.openhab.core.events.EventPublisher;
import org.openhab.core.library.types.PercentType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.types.Command;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.osgi.service.event.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * This class load parameters from cfg files (method updated)
 * 
 * @author Tonino Fazio
 */
public class SoulissBinding<E> extends AbstractActiveBinding<SoulissBindingProvider> implements ManagedService {

	private static Logger LOGGER = LoggerFactory.getLogger(SoulissBinding.class);
	
	
	   /** to keep track of all binding providers */
   
    protected EventPublisher eventPublisher = null;
   
   
    @Override
	public void setEventPublisher(EventPublisher eventPublisher) {
            this.eventPublisher = eventPublisher;
    }

    @Override
	public void unsetEventPublisher(EventPublisher eventPublisher) {
            this.eventPublisher = null;
    }


	@Override
	public void bindingChanged(BindingProvider provider, String itemName) {
		// TODO Auto-generated method stub
		super.bindingChanged(provider, itemName);
	}


	@Override
	public void activate() {
		// TODO Auto-generated method stub
		super.activate();
	}



	@Override
	public void deactivate() {
		// TODO Auto-generated method stub
		super.deactivate();
	}

	/**
	 * Read parameters from cfg file 
	 */
	@Override
	public void updated(Dictionary<String, ?> config)
			throws ConfigurationException {
		if (config != null) {
			Enumeration<String> enumConfig= config.keys();
						
			while (enumConfig.hasMoreElements()){
				String sName=enumConfig.nextElement();
				LOGGER.info("PARAMETER: " + sName + " = " + (String) config.get(sName));
				switch (sName) {
				case "IP_WAN":
					SoulissNetworkParameter.IPAddress=(String) config.get(sName);
					break;
				case "IP_LAN":
					SoulissNetworkParameter.IPAddressOnLAN=(String) config.get(sName);
					break;
				case "REFRESH_DBSTRUCT_TIME":
					SoulissNetworkParameter.REFRESH_DBSTRUCT_TIME=Integer.parseInt((String) config.get(sName));
					break;
				case "REFRESH_SUBSCRIPTION_TIME":
					SoulissNetworkParameter.REFRESH_SUBSCRIPTION_TIME=Integer.parseInt((String) config.get(sName));
					break;
				case "REFRESH_HEALTY_TIME":
					SoulissNetworkParameter.REFRESH_HEALTY_TIME=Integer.parseInt((String) config.get(sName));
					break;
				case "REFRESH_MONITOR_TIME":
					SoulissNetworkParameter.REFRESH_MONITOR_TIME=Integer.parseInt((String) config.get(sName));
					break;
				case "SEND_DELAY":
					SoulissNetworkParameter.SEND_DELAY=Integer.parseInt((String) config.get(sName));
					break;
				case "SEND_MIN_DELAY":
					SoulissNetworkParameter.SEND_MIN_DELAY=Integer.parseInt((String) config.get(sName));
					break;
				case "USER_INDEX":
					SoulissNetworkParameter.UserIndex=Integer.parseInt((String) config.get(sName));
					break;
				case "NODE_INDEX":
					SoulissNetworkParameter.NodeIndex=Integer.parseInt((String) config.get(sName));
					break;
				case "NODE_NUMBERS":
					SoulissNetworkParameter.nodes=Integer.parseInt((String) config.get(sName));
					break;
				case "SERVERPORT":
					if (config.get(sName).equals(""))
						SoulissNetworkParameter.serverPort=null;
					else
					SoulissNetworkParameter.serverPort=Integer.parseInt((String) config.get(sName));
					break;
				default:
					break;
				}
	}
		SoulissNetworkParameter.setConfigured(true);
		setProperlyConfigured(true);
	}
	}

	@Override
	public void handleEvent(Event event) {
		// TODO Auto-generated method stub
		super.handleEvent(event);
		
	}


	@Override
	/**
	 * Get the souliss' typical from the hash table and send a command
	 */
	public void receiveCommand(String itemName, Command command) {

		// Get the typical defined in the hash table
		SoulissGenericTypical T =SoulissGenericBindingProvider.SoulissTypicalsRecipients.getTypicalFromItem(itemName);
		LOGGER.info("receiveCommand - " + itemName + " = " + command + " - Typical: "+ T.getType());
		
		switch (T.getType()){
		case Constants.Souliss_T11: 
			SoulissT11 T11 =  (SoulissT11) T;
			T11.CommandSEND(StateTraslator.commandsOHtoSOULISS(T.getType(),command.toString()));
			break;
		case Constants.Souliss_T12: 
			SoulissT12 T12 =  (SoulissT12) T;
			T12.CommandSEND(StateTraslator.commandsOHtoSOULISS(T.getType(),command.toString()));
			break;
		case Constants.Souliss_T14:
			
			break;
		case Constants.Souliss_T16:
			SoulissT16 T16 =  (SoulissT16) T;
			String cmd=command.getClass().getSimpleName();
			if(cmd.equals(Constants.Openhab_RGB_TYPE)){
				Short R=0,G=0,B=0;
				String HSB[]=command.toString().split(",");
				short RGB[] = HSBtoRGB(Float.parseFloat(HSB[0]), Float.parseFloat(HSB[1]), Float.parseFloat(HSB[2]));
				T16.CommandSEND(StateTraslator.commandsOHtoSOULISS(T.getType(), command.getClass().getSimpleName()), RGB[0], RGB[1], RGB[2]);
			} else
				T16.CommandSEND(StateTraslator.commandsOHtoSOULISS(T.getType(), command.toString()));
			break;
		case Constants.Souliss_T18:
			
			break;
		case Constants.Souliss_T19:
			SoulissT19 T19 =  (SoulissT19) T;
			if(command instanceof  PercentType){
				int percentToShort = (((PercentType) command).shortValue()*255/100);
				T19.CommandSEND(Constants.Souliss_T1n_Set, Short.parseShort(String.valueOf(percentToShort)));
			} else
				T19.CommandSEND(StateTraslator.commandsOHtoSOULISS(T.getType(), command.toString()));
			break;
		case Constants.Souliss_T21: 
			
			break;
		case Constants.Souliss_T22:
			SoulissT22 T22 =  (SoulissT22) T;
			T22.CommandSEND(StateTraslator.commandsOHtoSOULISS(T.getType(),command.toString()));
			break;
		case Constants.Souliss_T_TemperatureSensor:
			//T=new Souliss_T_TemperatureSensor(sSoulissNodeIPAddress, sSoulissNodeVNetAddress, iSlot);
			break;
		case Constants.Souliss_T_HumiditySensor:
			//T=new Souliss_T_HumiditySensor(sSoulissNodeIPAddress, sSoulissNodeVNetAddress, iSlot);
			break;
		case Constants.Souliss_T32_IrCom_AirCon:
			//T=new Souliss_T32(sSoulissNodeIPAddress, sSoulissNodeVNetAddress, iSlot);
			break;
		case Constants.Souliss_T41_Antitheft_Main:
			//T=new SoulissT41(sSoulissNodeIPAddress, sSoulissNodeVNetAddress, iSlot);
			break;
		case Constants.Souliss_T42_Antitheft_Peer:
			//T=new SoulissT42(sSoulissNodeIPAddress, sSoulissNodeVNetAddress, iSlot);
			break;
		default:
			LOGGER.debug("Typical Unknown");	
		}
	}

/**
 * Convert color format from HSB to RGB
 * @param H
 * @param S
 * @param B
 * @return short RGBList[] contain RGB components
 */
	private short[] HSBtoRGB(Float H, Float S, Float B) {
		
		int RGB =Color.HSBtoRGB(H, S, B);
		Color c=new Color(RGB);
		short RGBList[]={(short) c.getRed(),(short) c.getGreen(), (short) c.getBlue()};
		return RGBList;
	}

//	@Override
//	protected void internalReceiveCommand(String itemName, Command command) {
//		// TODO Auto-generated method stub
//		super.internalReceiveCommand(itemName, command);
////		LOGGER.info("openHAB Event Bus -> External System - COMMAND. " + itemName + " = " + command );
//	}

//	@Override
//	public void receiveUpdate(String itemName, State newState) {
//		// TODO Auto-generated method stub
//		super.receiveUpdate(itemName, newState);
////		LOGGER.info("receiveUpdate - " + itemName + " = " + newState );
//	}
//
//	@Override
//	protected boolean providesBindingFor(String itemName) {
//		// TODO Auto-generated method stub
//		return super.providesBindingFor(itemName);
//	}

//	@Override
//	protected void internalReceiveUpdate(String itemName, State newState) {
//		// TODO Auto-generated method stub
//		super.internalReceiveUpdate(itemName, newState);
////		LOGGER.info("openHAB Event Bus -> External System. " + itemName + " = " + newState );
////	    for (SoulissBindingProvider provider : providers) {
////	    	LOGGER.info("Checking provider with names " + provider.getItemNames());
////	 
////	    }
////	
//			}

	@Override
	protected void execute() {
		// TODO Auto-generated method stub
	}

	@Override
	protected long getRefreshInterval() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected String getName() {
		// TODO Auto-generated method stub
	return null;
	}

	/* (non-Javadoc)
	 * @see org.openhab.core.binding.AbstractActiveBinding#setProperlyConfigured(boolean)
	 */
	@Override
	protected void setProperlyConfigured(boolean properlyConfigured) {
		if (properlyConfigured){
			LOGGER.info("START");
			
			try {
			
			// Start listening on the UDP socket
			UDPServerThread Q=null;
			Q=new UDPServerThread(SoulissGenericBindingProvider.SoulissTypicalsRecipients);
			Q.start();
			
			// Start the thread that send network packets to the Souliss network
			new SendDispatcherThread(SoulissNetworkParameter.SEND_DELAY,SoulissNetworkParameter.SEND_MIN_DELAY).start();	
			// Start the thread that send back to openHAB the souliss' typical values
			new MonitorThread(SoulissGenericBindingProvider.SoulissTypicalsRecipients, SoulissNetworkParameter.REFRESH_MONITOR_TIME, eventPublisher).start();
			// Start the thread that subscribe data from the Souliss network
			new RefreshSUBSCRIPTIONThread(Q.getSocket(),SoulissNetworkParameter.IPAddress, SoulissNetworkParameter.IPAddressOnLAN, SoulissNetworkParameter.nodes,SoulissNetworkParameter.REFRESH_SUBSCRIPTION_TIME).start();
			new RefreshHEALTYThread(Q.getSocket(), SoulissNetworkParameter.IPAddress, SoulissNetworkParameter.IPAddressOnLAN, SoulissNetworkParameter.nodes,SoulissNetworkParameter.REFRESH_HEALTY_TIME).start();
			
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				LOGGER.error(e.getMessage());
			}
			
			}
		
	//	super.setProperlyConfigured(properlyConfigured);
			
	}

	public void putOnOHEventBus(String itemName, String value) {
		
		eventPublisher.postUpdate(itemName, new StringType(value));
			}
}
