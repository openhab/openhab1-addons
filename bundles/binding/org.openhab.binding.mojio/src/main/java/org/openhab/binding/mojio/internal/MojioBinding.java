/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.mojio.internal;

import java.util.Map;
import java.util.Dictionary;

import org.openhab.binding.mojio.MojioBindingProvider;

import org.apache.commons.lang.StringUtils;
import org.openhab.core.binding.AbstractActiveBinding;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.library.types.PointType;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.osgi.service.cm.ManagedService;
import org.osgi.service.cm.ConfigurationException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.JsonNode;

import org.openhab.binding.mojio.messages.AuthorizeRequest;
import org.openhab.binding.mojio.messages.AuthorizeResponse;
import org.openhab.binding.mojio.messages.GetVehicleData;
import org.openhab.binding.mojio.messages.VehicleStatusResponse;
import org.openhab.binding.mojio.messages.VehicleType;
import org.openhab.binding.mojio.messages.VehicleLocation;
import org.openhab.binding.mojio.messages.GetMojioData;
import org.openhab.binding.mojio.messages.MojioStatusResponse;
import org.openhab.binding.mojio.messages.MojioType;
	

/**
 * Implement this class if you are going create an actively polling service
 * like querying a Website/Device.
 * 
 * @author Vladimir Pavluk
 * @since 1.0
 */
public class MojioBinding extends AbstractActiveBinding<MojioBindingProvider> implements ManagedService {

	private static final Logger logger = LoggerFactory.getLogger(MojioBinding.class);

	/**
	 * The BundleContext. This is only valid when the bundle is ACTIVE. It is set in the activate()
	 * method and must not be accessed anymore once the deactivate() method was called or before activate()
	 * was called.
	 */
	private BundleContext bundleContext;

	
	/** 
	 * the refresh interval which is used to poll values from the Mojio
	 * server (optional, defaults to 60000ms)
	 */
	private long refreshInterval = 60000;
	

  private MojioAuthTag authTag;
	

	public MojioBinding() {
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
		String refreshIntervalString = (String) configuration.get("refresh");
		if (StringUtils.isNotBlank(refreshIntervalString)) {
			refreshInterval = Long.parseLong(refreshIntervalString);
		}

		// read further config parameters here ...
		String appId = (String) configuration.get("appId");
		if (StringUtils.isBlank(appId)) {
      return; // Not properly configured
    }
		String appKey = (String) configuration.get("appKey");
		if (StringUtils.isBlank(appKey)) {
      return; // Not properly configured
    }
		String username = (String) configuration.get("username");
		if (StringUtils.isBlank(username)) {
      return; // Not properly configured
    }
		String password = (String) configuration.get("password");
		if (StringUtils.isBlank(password)) {
      return; // Not properly configured
    }

    authTag = new MojioAuthTag(appId, appKey, username, password);

    super.activate();
		setProperlyConfigured(true);
	}
	
  
  /**
   *
   * {@inheritDoc}
   *       
   */
  @SuppressWarnings("rawtypes")
  public void updated(final Dictionary configuration) throws ConfigurationException {
		String refreshIntervalString = (String) configuration.get("refresh");
		if (StringUtils.isNotBlank(refreshIntervalString)) {
			refreshInterval = Long.parseLong(refreshIntervalString);
		}

		// read further config parameters here ...
		String appId = (String) configuration.get("appId");
		if (StringUtils.isBlank(appId)) {
      return; // Not properly configured
    }
		String appKey = (String) configuration.get("appKey");
		if (StringUtils.isBlank(appKey)) {
      return; // Not properly configured
    }
		String username = (String) configuration.get("username");
		if (StringUtils.isBlank(username)) {
      return; // Not properly configured
    }
		String password = (String) configuration.get("password");
		if (StringUtils.isBlank(password)) {
      return; // Not properly configured
    }

    authTag = new MojioAuthTag(appId, appKey, username, password);

		setProperlyConfigured(true);
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
		return "Mojio Refresh Service";
	}
	
	/**
	 * @{inheritDoc}
	 */
	@Override
	protected void execute() {
		// the frequently executed code (polling) goes here ...
		logger.debug("execute() method is called!");
    for (MojioBindingProvider provider : providers) {
      for (String itemName : provider.getItemNames()) {     
        updateValue(provider, itemName);
      }
    }
	}

  protected void updateValue(MojioBindingProvider provider, String itemName) {
    String Imei = provider.getMojioIMEI(itemName);
    String[] path = provider.getValuePath(itemName);
    double rate = provider.getItemRate(itemName);
    Class itemType = provider.getItemType(itemName);
    String authToken = authTag.getAuthToken();
    VehicleType vehicle = GetVehicleData.findByMojioIMEI(authToken, Imei);
    try {
      ObjectMapper mapper = new ObjectMapper();
      String json = mapper.writeValueAsString(vehicle);

      JsonNode root = mapper.readTree(json);
      JsonNode current = root;

      for(int i = 0; i < path.length; i ++) {
        try {
          Integer intComponent = Integer.valueOf(path[i]);
          current = current.path(intComponent);
        } catch(Exception e) {
          current = current.path(path[i]);
        }
      }

      if(current.isNumber()) {
        eventPublisher.postUpdate(itemName, new DecimalType(current.getDoubleValue() * rate));
      } else if(current.isBoolean()) {
        eventPublisher.postUpdate(itemName, new StringType(current.getValueAsText()));
      } else {
        try {
          logger.debug("Reading vehicle location of type "+itemType.getName());
          VehicleLocation location = mapper.readValue(current.toString(), VehicleLocation.class);
          if(itemType.getName() == "org.openhab.core.library.items.StringItem") {
            eventPublisher.postUpdate(itemName, new StringType(""+location.latitude+","+location.longitude));
          } else if(itemType.getName() == "org.openhab.core.library.items.LocationItem") {
            eventPublisher.postUpdate(itemName, new PointType(new DecimalType(location.latitude), new DecimalType(location.longitude)));
          }
        } catch(Exception e) {
          logger.debug("Posting string value");
          eventPublisher.postUpdate(itemName, new StringType(current.toString()));
        }
      }
    } catch(Exception e) { }
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

}
