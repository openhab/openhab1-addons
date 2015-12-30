/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.harmonyhub.internal;

import java.util.HashMap;
import java.util.Map;

import net.whistlingfish.harmony.ActivityChangeListener;
import net.whistlingfish.harmony.HarmonyClient;
import net.whistlingfish.harmony.HarmonyHubListener;
import net.whistlingfish.harmony.config.Activity;

import org.openhab.binding.harmonyhub.HarmonyHubBindingProvider;
import org.openhab.core.binding.AbstractBinding;
import org.openhab.core.binding.BindingProvider;
import org.openhab.core.events.EventPublisher;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.openhab.io.harmonyhub.HarmonyHubGateway;
import org.openhab.io.harmonyhub.HarmonyHubGatewayListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Logitech Harmony Hub Binding for openHAB
 * 
 * The Logitech Harmony Hub is an advanced remote control for various audio, visual 
 * and home automation devices.
 * . 
 * @see <a href="http://www.logitech.com/en-us/harmony-remotes">Logitech Harmony Website</a>
 * @author Dan Cunningham
 * @since 1.7.0
 */
public class HarmonyHubBinding extends AbstractBinding<HarmonyHubBindingProvider> implements HarmonyHubGatewayListener {

	private static final Logger logger = 
			LoggerFactory.getLogger(HarmonyHubBinding.class);

	private HarmonyHubGateway harmonyHubGateway;

	/**
	 * Map of qualifiers to listeners
	 */
	private Map<String, HarmonyHubListener> harmonyListeners;
	
	/**
	 * @{inheritDoc}
	 */
	@Override
	public void bindingChanged(BindingProvider provider, final String itemName) {
		logger.debug("bindingChanged {} {}", provider, itemName);

		if(harmonyHubGateway == null)
			return;

		if(harmonyListeners == null) {
			harmonyListeners = new HashMap<String, HarmonyHubListener>();
		} else {
			for(String qualifier : harmonyListeners.keySet()) {
				harmonyHubGateway.removeListener(qualifier, harmonyListeners.get(qualifier));
			}
			harmonyListeners.clear();
		}

		if(provider instanceof HarmonyHubBindingProvider) {
			HarmonyHubBindingProvider harmonyProvider = (HarmonyHubBindingProvider)provider;
			final HarmonyHubBindingConfig config = harmonyProvider.getHarmonyHubBindingConfig(itemName);
			if(harmonyHubGateway != null && config != null && config.getBindingType() == HarmonyHubBindingType.CurrentActivity) {
				if(!harmonyListeners.containsKey(config.getQualifier())) {
					logger.debug("adding new listener for {}", config.getQualifier());
					HarmonyHubListener listener = new HarmonyHubListener() {
						@Override
						public void removeFrom(HarmonyClient client) {
							logger.debug("removeFrom called for {}", config.getQualifier());
						}
						@Override
						public void addTo(HarmonyClient client) {
							client.addListener(new ActivityChangeListener() {
								@Override
								public void activityStarted(Activity activity) {
									logger.debug("activityStarted called for {}", config.getQualifier());
									updateActivity(activity,config.getQualifier());
								}
							});
							//now that we can hear about activity changes, make sure we get the current value
							updateActivity(client.getCurrentActivity(), config.getQualifier());
						}
					};
					harmonyHubGateway.addListener(config.getQualifier(), listener);
					harmonyListeners.put(config.getQualifier(), listener);
				}
			}
		}
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public void allBindingsChanged(BindingProvider provider) {
		for(String itemName : provider.getItemNames()) {
			bindingChanged(provider,itemName);
		}
	}

	/**
	 * @{inheritDoc}
	 */
	@Override
	protected void internalReceiveCommand(String itemName, Command command) {
		logger.debug("internalReceiveCommand({},{}) is called!", itemName, command);

		if(harmonyHubGateway == null){
			logger.warn("A command was received, but could not be executed as no Harmony Hub has been configured");
			return;
		}

		for (HarmonyHubBindingProvider provider : providers) {
			HarmonyHubBindingConfig config = provider.getHarmonyHubBindingConfig(itemName);
			
			if(config.getBindingType().getDirection() == HarmonyHubBindingDirection.IN){
				logger.warn("item {} is not configured to send outbound commands! Please change to > or * in the binding");
				continue;
			}
			
			switch(config.getBindingType()) {
			case PressButton:
				logger.debug("PressButton command:{} q:{} p1:{} p2:{}", command.toString(), config.getQualifier(),
						config.getParam1(), config.getParam2());
				String cmd = config.getParam2();
				if(cmd == null){
					cmd = command.toString();
				}
				try {
					harmonyHubGateway.pressButton(config.getQualifier(), Integer.parseInt(config.getParam1()), cmd);
				} catch (NumberFormatException ignored) {
					harmonyHubGateway.pressButton(config.getQualifier(), config.getParam1(), cmd);
				}
				break;
			case StartActivity:
				try {
					harmonyHubGateway.startActivity(config.getQualifier(), Integer.parseInt(config.getParam1()));
				} catch (NumberFormatException ignored) {
					harmonyHubGateway.startActivity(config.getQualifier(), config.getParam1());
				}
				break;
			case CurrentActivity:
				harmonyHubGateway.startActivity(config.getQualifier(),command.toString());
				break;
			default :
				break;
			}
		}
	}

	/**
	 * For a given {@link Activity}, update all items who need to know about it
	 * @param activity
	 */
	private void updateActivity(Activity activity, String qualifier) {
		logger.debug("updateActivity {}" + activity.getLabel());
		for (HarmonyHubBindingProvider provider : providers) {
			for(String itemName : provider.getItemNames()) {
				
				HarmonyHubBindingConfig config = provider.getHarmonyHubBindingConfig(itemName);
				if(config.matchesQualifier(qualifier) && 
						config.getBindingType() == HarmonyHubBindingType.CurrentActivity) {
					updateActivityForItem(itemName, config.getItemType(), activity);
				}
			}
		}
	}
	
	/**
	 * For a given {@link Activity}, update an {@link Item} with its {@link State} state
	 * @param item
	 * @param state
	 * @param activity
	 */
	private void updateActivityForItem(String itemName, Class<? extends Item> itemType, Activity activity) {
		if(itemType.isAssignableFrom(NumberItem.class)) {
			eventPublisher.postUpdate(itemName, new DecimalType(activity.getId()));
		} else {
			eventPublisher.postUpdate(itemName, new StringType(activity.getLabel()));
		}
	}

	/**
	 * Wire up our gateway from the IO bundle
	 * @param harmonyHubGateway
	 */
	public void setHarmonyHubGateway(HarmonyHubGateway harmonyHubGateway) {
		logger.debug("addHarmonyHubGateway, configured {}", harmonyHubGateway.isProperlyConfigured());
		this.harmonyHubGateway = harmonyHubGateway;
		this.harmonyHubGateway.addHarmonyHubGatewayListener(this);
	}

	/**
	 * un-wire our gateway from the IO package
	 * @param harmonyHubGateway
	 */
	public void unsetHarmonyHubGateway(HarmonyHubGateway harmonyHubGateway) {
		this.harmonyHubGateway = null;
	}

	/**
	 * Callback from the IO bundle that it is configured
	 */
	@Override
	public void configured(boolean isConfigured) {
		if(isConfigured){
			for(HarmonyHubBindingProvider provider: providers) {
				allBindingsChanged(provider);
			}
		}
		
	}
}
