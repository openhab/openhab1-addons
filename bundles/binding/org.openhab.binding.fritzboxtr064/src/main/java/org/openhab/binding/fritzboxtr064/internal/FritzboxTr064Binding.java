/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.fritzboxtr064.internal;

import java.util.Map;


import org.openhab.binding.fritzboxtr064.FritzboxTr064BindingProvider;
import org.openhab.binding.fritzboxtr064.internal.FritzboxTr064GenericBindingProvider.FritzboxTr064BindingConfig;

import org.apache.commons.lang.StringUtils;
import org.openhab.core.binding.AbstractActiveBinding;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.ContactItem;
import org.openhab.core.library.items.StringItem;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.OpenClosedType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
	

/**
 * Implement this class if you are going create an actively polling service
 * like querying a Website/Device.
 * 
 * @author gitbock
 * @since 0.1.0
 */
public class FritzboxTr064Binding extends AbstractActiveBinding<FritzboxTr064BindingProvider> {

	private static final Logger logger = 
		LoggerFactory.getLogger(FritzboxTr064Binding.class);

	/**
	 * The BundleContext. This is only valid when the bundle is ACTIVE. It is set in the activate()
	 * method and must not be accessed anymore once the deactivate() method was called or before activate()
	 * was called.
	 */
	private BundleContext bundleContext;
	
	//URL to connect to fbox. Provided in main cfg file
	private String _url;
	
	//Username to use to connect to fbox
	private String _user; 
		
	//PW
	private String _pw; 
	
	/** 
	 * the refresh interval which is used to poll values from the FritzboxTr064
	 * server (optional, defaults to 60000ms)
	 */
	private long refreshInterval = 60000;
	
	/***
	 * holds Fbox connection
	 */
	private Tr064Comm _fboxComm = null;
	
	
	public FritzboxTr064Binding() {
	}
		
	
	/**
	 * Called by the SCR to activate the component with its configuration read from CAS
	 * 
	 * @param bundleContext BundleContext of the Bundle that defines this component
	 * @param configuration Configuration properties for this component obtained from the ConfigAdmin service
	 */
	public void activate(final BundleContext bundleContext, final Map<String, Object> configuration) {
		this.bundleContext = bundleContext;
		logger.debug("FritzBox TR064 Binding activated!");
			
		// to override the default refresh interval one has to add a 
		// parameter to openhab.cfg like <bindingName>:refresh=<intervalInMs>
		String refreshIntervalString = (String) configuration.get("refresh");
		if (StringUtils.isNotBlank(refreshIntervalString)) {
			refreshInterval = Long.parseLong(refreshIntervalString);
			logger.debug("Custom refresh interval set to " + refreshInterval);
		}

		//Check if fritzbox parameters were provided in config, otherwise does not make sense going on...
		String fboxurl = (String) configuration.get("url");
		String fboxuser = (String) configuration.get("user");
		String fboxpw = (String) configuration.get("pass");
		if(fboxurl == null){
			logger.error("Fritzbox URL was not provided in config. Shutting down binding.");
			//how to shutdown??
			setProperlyConfigured(false);
			return;
		}
		if(fboxuser == null){
			logger.error("Fritzbox User was not provided in config. Using default username.");
		}
		if(fboxpw == null){
			logger.error("Fritzbox Password was not provided in config. Shutting down binding.");
			//how to shutdown??
			setProperlyConfigured(false);
			return;
		}
		this._pw = fboxpw;
		this._user = fboxuser;
		this._url = fboxurl;
		setProperlyConfigured(true);
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
		return "FritzboxTr064 Refresh Service";
	}
	
	/**
	 * @{inheritDoc}
	 */
	@Override
	protected void execute() {
		logger.debug("FritzboxTr064 executing...");
		//No need to check for set user/pw/url because otherwise binding would not execute at all (?)
		if(_fboxComm == null){
			_fboxComm = new Tr064Comm(_url, _user, _pw);
		}
		for (FritzboxTr064BindingProvider provider : providers) { 
			for(String itemName : provider.getItemNames() ){ //check each item relevant for this binding
				FritzboxTr064BindingConfig conf = provider.getBindingConfigByItemName(itemName); // extract itemconfig for current item
				String tr064result = _fboxComm.getTr064Value(conf.getConfigString()); //try to get value for this item config string from fbox
				if(tr064result == null){ //if value cannot be read
					tr064result = "ERR";
				}
				Class<? extends Item> itemType = conf.getItemType();
				if(itemType.isAssignableFrom(StringItem.class)){
					eventPublisher.postUpdate(itemName, new StringType(tr064result));
				}
				if(itemType.isAssignableFrom(ContactItem.class)){
					State newState = tr064result.equals("1") ? OpenClosedType.OPEN : OpenClosedType.CLOSED;
					eventPublisher.postUpdate(itemName, newState );
				}
				if(itemType.isAssignableFrom(SwitchItem.class)){
					State newState = tr064result.equals("1") ? OnOffType.ON : OnOffType.OFF;
					eventPublisher.postUpdate(itemName, newState );
				}
				
			}
			
		
		}
	}
	
		

	/**
	 * @{inheritDoc}
	 */
	@Override
	protected void internalReceiveCommand(String itemName, Command command) {
		logger.debug("internalReceiveCommand({},{}) is called!", itemName, command);
		if(_fboxComm == null){
			_fboxComm = new Tr064Comm(_url, _user, _pw);
		}
		//Search Item Binding config for this itemName
		for (FritzboxTr064BindingProvider provider : providers) { 
			FritzboxTr064BindingConfig conf = provider.getBindingConfigByItemName(itemName);
			if(conf != null){
				_fboxComm.setTr064Value(conf.getConfigString(), command); //pass config String because config string needed for finding item map
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
