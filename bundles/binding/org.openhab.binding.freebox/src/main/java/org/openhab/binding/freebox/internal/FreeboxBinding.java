/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.freebox.internal;

import static org.apache.commons.lang.StringUtils.isBlank;

import java.util.Calendar;
import java.util.Collection;
import java.util.Dictionary;
import java.util.List;

import org.openhab.binding.freebox.FreeboxBindingProvider;
import org.openhab.binding.freebox.FreeboxBindingConfig;
import org.openhab.core.binding.AbstractActiveBinding;
import org.openhab.core.library.items.StringItem;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.items.DateTimeItem;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.DateTimeType;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.openhab.core.items.Item;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.osgi.framework.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.matmaul.freeboxos.login.*;
import org.matmaul.freeboxos.FreeboxOsClient;
import org.matmaul.freeboxos.FreeboxException;
import org.matmaul.freeboxos.connection.ConnectionStatus;
import org.matmaul.freeboxos.system.SystemConfiguration;
import org.matmaul.freeboxos.call.CallEntry;
import org.matmaul.freeboxos.wifi.*;


/**
 * Implement this class if you are going create an actively polling service
 * like querying a Website/Device.
 * 
 * @author clinique
 * @since 1.5.0
 */
public class FreeboxBinding extends AbstractActiveBinding<FreeboxBindingProvider> implements ManagedService {
	private static final Logger logger = LoggerFactory.getLogger(FreeboxBinding.class);
	private static String appVersion;
	private static String appID;
	private static String appName;
	
	private static String deviceName;
	private static String appToken;
	private static String serverAddress;
	
	private static FreeboxOsClient fbClient;
	private static LoginManager loginManager;
	
	private static SystemConfiguration sc;
	private static ConnectionStatus cs;
	private static WifiGlobalConfig wc;

	/** 
	 * the refresh interval which is used to poll values from the Freebox
	 * server (optional, defaults to 60000ms)
	 */
	private long refreshInterval = 20000;
	
	
	public FreeboxBinding() {}
	
	public void activate() {
		Bundle bundle = FrameworkUtil.getBundle(getClass());
		appVersion = String.format("%d.%d",bundle.getVersion().getMajor(),bundle.getVersion().getMinor()); // something like 1.5
		appID = bundle.getSymbolicName();																// org.openhab.binding.freebox
		appName = bundle.getHeaders().get("Bundle-Name");												// "openHAB Freebox Binding"
	}
	
	public void deactivate() {
		// deallocate resources here that are no longer needed and 
		// should be reset when activating this binding again
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
		return "Freebox Refresh Service";
	}
	
	private void setItemValue(Item item, String value) {
		
			StringType stValue = new StringType(value);
			if (!item.getState().equals(stValue)) {
				synchronized (item) {
					eventPublisher.postUpdate(item.getName(), stValue);
					((StringItem)item).setState(stValue);
				}
			}			
	}

	private void setItemValue(Item genItem, boolean value) {
		SwitchItem item = (SwitchItem) genItem;
			OnOffType status = ( value ? OnOffType.ON : OnOffType.OFF);
			if (!item.getState().equals(status)) {
				synchronized (item) {
					eventPublisher.postUpdate(item.getName(), status);
					item.setState(status);
				}
			}				
				
	}
	
	private void setItemValue(Item genItem, Long value) {
		NumberItem item = (NumberItem) genItem;
			DecimalType dtValue = new DecimalType(value);
			if (!item.getState().equals(dtValue)) {
				synchronized (item) {
					eventPublisher.postUpdate(item.getName(), dtValue);
					item.setState(dtValue);
				}
			}				
				
	}
	
	private void setDateTimeValue(Item genItem, long value) {
		DateTimeItem item = (DateTimeItem) genItem;
		
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(value * 1000);
	    
		DateTimeType dtValue = new DateTimeType(c);
		if (!item.getState().equals(dtValue)) {
			synchronized (item) {
				eventPublisher.postUpdate(item.getName(), dtValue);
				item.setState(dtValue);
			}
		}	
		
	}

	private void setItemValue(Item genItem, int value) {
		NumberItem item = (NumberItem) genItem;
			DecimalType dtValue = new DecimalType(value);
			if (!item.getState().equals(dtValue)) {
				synchronized (item) {
					eventPublisher.postUpdate(item.getName(), dtValue);
					item.setState(dtValue);
				}
			}				
				
	}
	
	/**
	 * @{inheritDoc}
	 */
	@SuppressWarnings("incomplete-switch")
	@Override
	protected void execute() {
		if (!loginManager.isConnected())
			return;
		
		try {
			
			sc = fbClient.getSystemManager().getConfiguration();
			cs = fbClient.getConnectionManager().getStatus();
			wc = fbClient.getWifiManager().getGlobalConfig();
			List<CallEntry> appels = fbClient.getCallManager().getCallEntries();
	
			for (FreeboxBindingProvider provider : providers) {
				Collection<String> items = provider.getItemNames();
				
				for (CallEntry call: appels) {
					if (call.is_new_()) {
						for (String itemName: items) {
							FreeboxBindingConfig bindingConfig = provider.getConfig(itemName);
							switch (bindingConfig.commandType) {
							case CALLSTATUS : setItemValue(bindingConfig.item, call.getType());
								break;
							case CALLDURATION: setItemValue(bindingConfig.item, call.getDuration());
								break;
							case CALLNUMBER: setItemValue(bindingConfig.item, call.getNumber());
								break;
							case CALLTIMESTAMP: setDateTimeValue(bindingConfig.item, call.getDateTime());
								break;
							}
						}						
						call.setNew(false);
						fbClient.getCallManager().setCallEntry(call);
					}
				}				
				
				for (String itemName: items) {
					FreeboxBindingConfig bindingConfig = provider.getConfig(itemName);
					
					switch (bindingConfig.commandType) {
						case BYTESDOWN: setItemValue(bindingConfig.item,cs.getBytes_down());
							break;
						case BYTESUP: setItemValue(bindingConfig.item,cs.getBytes_up());
							break;
						case CPUB: setItemValue(bindingConfig.item,sc.getTemp_cpub());
							break;
						case CPUM: setItemValue(bindingConfig.item,sc.getTemp_cpum());
							break;
						case FAN: setItemValue(bindingConfig.item,sc.getFan_rpm());
							break;
						case FWVERSION: setItemValue(bindingConfig.item,sc.getFirmware_version());
							break;
						case IPV4: setItemValue(bindingConfig.item,cs.getIpv4());
							break;
						case LINESTATUS: setItemValue(bindingConfig.item, cs.getState());
							break;
						case RATEDOWN: setItemValue(bindingConfig.item,cs.getRate_down());
							break;
						case RATEUP: setItemValue(bindingConfig.item,cs.getRate_up());
							break;
						case SW: setItemValue(bindingConfig.item,sc.getTemp_sw());
							break;
						case UPTIME: setItemValue(bindingConfig.item,sc.getUptimeVal());
							break;
						case WIFISTATUS : setItemValue(bindingConfig.item,wc.getEnabled());
							break;
						default:
							break;
					
					}
				}
																									
				}				
		} catch (FreeboxException e) {
			logger.error(e.getMessage());			
		}
	}

	/**
	 * @{inheritDoc}
	 */
	@Override
	protected void internalReceiveCommand(String itemName, Command command) {
		for (FreeboxBindingProvider provider : providers) {
			FreeboxBindingConfig config = provider.getConfig(itemName);
			if (config == null) continue;
			
			if (config.commandType == CommandType.WIFISTATUS) {
				try {
					wc = fbClient.getWifiManager().getGlobalConfig();
					wc.setEnabled(command.equals(OnOffType.ON) ? true : false);						
					fbClient.getWifiManager().setGlobalConfig(wc);
				} catch (FreeboxException e) {
					logger.error(e.toString());
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
		logger.debug("internalReceiveUpdate() is called!");
	}
		
	/**
	 * @{inheritDoc}
	 */
	@Override
	public void updated(Dictionary<String, ?> config) throws ConfigurationException {
		if (config != null) {
			
			deviceName = (String) config.get("device");
			if (isBlank(deviceName)) {	// The only mandatory parameter is tested first
				throw new ConfigurationException("freebox",
					"The parameter 'device' is missing! Please refer to your 'openhab.cfg'");
			}

			String refreshConfig = (String) config.get("refresh");
			if (!isBlank(refreshConfig)) {
				refreshInterval = Long.parseLong(refreshConfig);
			}
			
			serverAddress = (String) config.get("server");
			if (isBlank(serverAddress)) serverAddress = "mafreebox.freebox.fr";
			
			appToken = (String) config.get("apptoken");	
			try {
				start();
				setProperlyConfigured(true);
			} catch (FreeboxException e) {
				logger.error(e.getMessage());
				setProperlyConfigured(false);
			}
			
		}
	}

	/**
	 * Handles connection to the Freebox, including validation of the Apptoken
	 * if none is provided in 'openhab.cfg'
	 * @throws FreeboxException
	 */
	private void start() throws FreeboxException {
		logger.debug("Appname : " + appName);
		logger.debug("AppVersion : " + appVersion);
		logger.debug("DeviceName : " + deviceName);
		logger.debug("AppID :" + appID);
		
		fbClient = new FreeboxOsClient(appID,serverAddress);
		loginManager = fbClient.getLoginManager();
		
		TrackAuthorizeStatus authorizeStatus = TrackAuthorizeStatus.UNKNOWN;
		if (isBlank(appToken)) {
			Authorize authorize = loginManager.newAuthorize(appName,appVersion,deviceName);
			appToken = authorize.getAppToken();
			logger.info("####################################################################");
			logger.info("# Please accept activation request directly on your freebox        #");
			logger.info("# Once done, record current Apptoken in your 'openhab.cfg'         #");
			logger.info("# " + appToken +" #");
			logger.info("####################################################################");
				
			do {
				try {
					Thread.sleep(2000);
					authorizeStatus = loginManager.trackAuthorize();
				} catch (InterruptedException e) {
					logger.error(e.getMessage());
				}
					
			} while (authorizeStatus == TrackAuthorizeStatus.PENDING);	
				
		} else {
			authorizeStatus = TrackAuthorizeStatus.GRANTED;
		}
		
		if (authorizeStatus != TrackAuthorizeStatus.GRANTED)
			throw new FreeboxException(authorizeStatus.toString());

		logger.debug("Apptoken valide : [" + appToken + "]");	
		loginManager.setAppToken(appToken);
		loginManager.openSession();			
	}
	

}
