package org.openhab.binding.chromecast.internal;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.openhab.binding.chromecast.ChromeCastBindingProvider;
import org.openhab.binding.chromecast.internal.ChromeCastGenericBindingProvider.ChromecastBindingConfig;
import org.openhab.binding.chromecast.internal.ChromeCastGenericBindingProvider.ChromecastBindingConfig.Direction;
import org.openhab.binding.chromecast.internal.commands.ApplicationUpdater;
import org.openhab.binding.chromecast.internal.commands.IUpdater;
import org.openhab.binding.chromecast.internal.commands.StatusUpdater;
import org.openhab.binding.chromecast.internal.commands.VolumeUpdater;
import org.apache.commons.lang.StringUtils;
import org.openhab.core.binding.AbstractActiveBinding;
import org.openhab.core.items.Item;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.osgi.framework.BundleContext;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import su.litvak.chromecast.api.v2.ChromeCast;
import su.litvak.chromecast.api.v2.MediaStatus;
import su.litvak.chromecast.api.v2.Status;
	

/**
 * Implement this class if you are going create an actively polling service
 * like querying a Website/Device.
 * 
 * @author ibaton
 * @since 1.7
 */
public class ChromeCastBinding extends AbstractActiveBinding<ChromeCastBindingProvider> implements ManagedService {

	private static final Logger logger = LoggerFactory.getLogger(ChromeCastBinding.class);
	
	private static Map<String, ChromeCast> instances = new HashMap<String, ChromeCast>();
	
	private static final String COMMAND_VOLUME 		= "volume";
	private static final String COMMAND_STATUS 		= "status";
	private static final String COMMAND_APPLICATION = "app";
	
	private static final Map<Direction, Map<String, IUpdater>> UPDATERS = new HashMap<Direction, Map<String, IUpdater>>();
	static {
		Map<String, IUpdater> inMap =  new HashMap<String, IUpdater>();
		inMap.put(COMMAND_APPLICATION, new ApplicationUpdater());
		UPDATERS.put(Direction.In, inMap);
		
		UPDATERS.put(Direction.Out, new HashMap<String, IUpdater>());
		
		// IO
		Map<String, IUpdater> ioMap =  new HashMap<String, IUpdater>();
		ioMap.put(COMMAND_VOLUME, new VolumeUpdater());
		ioMap.put(COMMAND_STATUS, new StatusUpdater());
		UPDATERS.put(Direction.IO, ioMap);
	}

	/**
	 * The BundleContext. This is only valid when the bundle is ACTIVE. It is set in the activate()
	 * method and must not be accessed anymore once the deactivate() method was called or before activate()
	 * was called.
	 */
	private BundleContext bundleContext;

	
	/** 
	 * the refresh interval which is used to poll values from the ChromeCast
	 * server (optional, defaults to 5000ms)
	 */
	private long refreshInterval = 5000;
	
	
	public ChromeCastBinding() {
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
		
		
		logger.info("activate!");

		
		// to override the default refresh interval one has to add a 
		// parameter to openhab.cfg like <bindingName>:refresh=<intervalInMs>
		String refreshIntervalString = (String) configuration.get("refresh");
		if (StringUtils.isNotBlank(refreshIntervalString)) {
			refreshInterval = Long.parseLong(refreshIntervalString);
		}

		logger.info("activate!");
		if(configuration != null){
			for(String key : configuration.keySet()){
			}
		}
		
		setProperlyConfigured(true);
	}
	
	/**
	 * Called by the SCR when the configuration of a binding has been changed through the ConfigAdmin service.
	 * @param configuration Updated configuration properties
	 */
	public void modified(final Map<String, ?> configuration) {
		// update the internal configuration accordingly
		logger.info("modified!");
		
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
		
		logger.info("deactivate!");
		
		for(Entry<String, ChromeCast> entry : instances.entrySet()){
			try {
				entry.getValue().disconnect();
			}catch (Exception e) {}
		}
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
		return "ChromeCast Refresh Service";
	}
	
	/**
	 * @{inheritDoc}
	 */
	@Override
	protected void execute() {
		
		Map<String, Status> statusMap = new HashMap<String, Status>();
		Map<String, MediaStatus> mediaStatusMap = new HashMap<String, MediaStatus>();
		
		for(ChromeCastBindingProvider provider : providers){
			for (String itemName : provider.getItemNames()) {				
				ChromecastBindingConfig itemConfig = provider.getChromecastInstance(itemName);
				
				if (itemConfig != null &&
						itemConfig.getDirection() == ChromecastBindingConfig.Direction.In ||
						itemConfig.getDirection() == ChromecastBindingConfig.Direction.IO) {
				
					Class<? extends Item> itemType = itemConfig.getType();
					try {
						logger.info("execute item: " + itemName + " ip: " + itemConfig.getIP() + " Command: " + itemConfig.getCommand());
						
						if(!instances.containsKey(itemConfig.ip)){
							instances.put(itemConfig.ip, new ChromeCast(itemConfig.ip));
						}
						
						// Connect if not already connected
						ChromeCast chromeCast = instances.get(itemConfig.ip);
						if(!chromeCast.isConnected()){
							chromeCast.connect();
						}
						
						Status status = statusMap.get(itemConfig.ip);
						MediaStatus mediaStatus = mediaStatusMap.get(itemConfig.ip);
						if(status == null || mediaStatus == null){
							status = chromeCast.getStatus();
							mediaStatus = chromeCast.getMediaStatus();
							
							statusMap.put(itemConfig.ip, status);
							mediaStatusMap.put(itemConfig.ip, mediaStatus);
						}
						
						Map<String, IUpdater> updaters = UPDATERS.get(itemConfig.getDirection());
						if(updaters != null && updaters.containsKey(itemConfig.command)){
							IUpdater updater = updaters.get(itemConfig.command);
							updater.execute(eventPublisher, chromeCast, itemConfig, status, mediaStatus);
							continue;
						} else {}
					
					} catch (IOException e) {
						e.printStackTrace();
					} catch (GeneralSecurityException e) {
						e.printStackTrace();
					}catch (Exception e) {
						
					}
				}
			}
		}
	}

	
	/**
	 * @{inheritDoc}
	 */
	@Override
	protected void internalReceiveCommand(String itemName, Command command) {
		// the code being executed when a state was sent on the openHAB
		// event bus goes here. This method is only called if one of the 
		// BindingProviders provide a binding for the given 'itemName'.
		
		ChromecastBindingConfig itemConfig = null;
		for(ChromeCastBindingProvider provider : providers){			
			if(provider.containsChromecastInstance(itemName)){
				try {
					itemConfig = provider.getChromecastInstance(itemName);
					if(itemConfig != null && 
						itemConfig.getDirection() == ChromecastBindingConfig.Direction.Out ||
						itemConfig.getDirection() == ChromecastBindingConfig.Direction.IO){
					
					
					
						if(itemConfig.ip != null && !instances.containsKey(itemConfig.ip)){
							instances.put(itemConfig.ip, new ChromeCast(itemConfig.ip));
						}
						
						// Connect if not already connected
						ChromeCast chromeCast = instances.get(itemConfig.ip);
						if(!chromeCast.isConnected()){
							chromeCast.connect();
						}
													
						Map<String, IUpdater> updaters = UPDATERS.get(itemConfig.getDirection());
						if(updaters != null && updaters.containsKey(itemConfig.command)){
							IUpdater updater = updaters.get(itemConfig.command);
							updater.receiveCommand(chromeCast, itemConfig, command);
						} else {
							eventPublisher.postUpdate(itemName, Helper.createState(itemConfig.itemType, "Unknown"));
						}
					} else {
						logger.debug("No chromecast found");
					}
				} catch (Exception e) {}
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
		logger.info("internalReceiveUpdate");
		logger.info("internalReceiveUpdate({},{}) is called!", itemName, newState);
	}


	public void updated(Dictionary<String, ?> properties)
			throws ConfigurationException {
		
		if(properties != null){
			Enumeration<String> keys = properties.keys();
			while(keys.hasMoreElements()){
				String key = keys.nextElement();
				logger.info("updated " + key);
			}
		}
	}
}
