/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.pilight.internal;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.openhab.binding.pilight.PilightBindingProvider;
import org.openhab.binding.pilight.internal.communication.Action;
import org.openhab.binding.pilight.internal.communication.Code;
import org.openhab.binding.pilight.internal.communication.Config;
import org.openhab.binding.pilight.internal.communication.Device;
import org.openhab.binding.pilight.internal.communication.DeviceType;
import org.openhab.binding.pilight.internal.communication.Status;
import org.openhab.binding.pilight.internal.communication.Values;
import org.openhab.binding.pilight.internal.types.PilightContactType;
import org.openhab.core.binding.AbstractBinding;
import org.openhab.core.binding.BindingProvider;
import org.openhab.core.library.items.ContactItem;
import org.openhab.core.library.items.DimmerItem;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.items.StringItem;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.OpenClosedType;
import org.openhab.core.library.types.PercentType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
	
/**
 * Binding that communicates with one or multiple pilight instances.
 * 
 * {@link http://www.pilight.org/}
 * 
 * @author Jeroen Idserda
 * @since 1.0
 */
public class PilightBinding extends AbstractBinding<PilightBindingProvider> implements ManagedService {

	private static final Logger logger = 
		LoggerFactory.getLogger(PilightBinding.class);
	
	private static final int MAX_DIM_LEVEL = 15;	
	
	private Map<String, PilightConnection> connections = new HashMap<String, PilightConnection>();
	
	@Override
	public void deactivate() {
		logger.debug("pilight binding deactivated");
		closeConnections();
	}
	
	/**
	 * Processes a status update received from pilight and changes the state of the
	 * corresponding openHAB item (if item config is found)
	 * 
	 * @param connection pilight connection 
	 * @param status The new Status
	 */
	private void processStatus(PilightConnection connection, Status status) {
		Integer type = status.getType();
		
		if (!type.equals(DeviceType.SERVER)) {
			String instance = connection.getInstance();
			String device = status.getDevices().get(0);
			
			List<PilightBindingConfig> configs = getConfigs(instance, device);
			
			if (!configs.isEmpty()) {
				if (type.equals(DeviceType.SWITCH) || type.equals(DeviceType.DIMMER)) {
					processSwitchEvent(configs, status);
				} else if (type.equals(DeviceType.CONTACT)) {
					processContactEvent(configs, status);
				} else if (type.equals(DeviceType.VALUE)) {
					processValueEvent(configs, status);
				}
			}
		}
	}

	private void processValueEvent(List<PilightBindingConfig> configs, Status status) {
		for (PilightBindingConfig config : configs) {
			String property = config.getProperty();
			if (status.getValues().containsKey(property)) {
				String value = status.getValues().get(property);
				State state = getState(value, config);
				
				if (state != null) {
					eventPublisher.postUpdate(config.getItemName(), state);
				}
			}
		}
	}

	protected State getState(String value, PilightBindingConfig config) {
		State state = null;
		
		if (config.getItemType().equals(StringItem.class)) {
			state = new StringType(value);
		} else if (config.getItemType().equals(NumberItem.class)) {
			state = new DecimalType(new BigDecimal(value));
		}
		
		return state;
	}

	private void processSwitchEvent(List<PilightBindingConfig> configs, Status status) {
		Integer type = status.getType();
		State state = OnOffType.valueOf(status.getValues().get("state").toUpperCase());

		if (type.equals(DeviceType.SWITCH)) {
			// noop, just use on/off state defined above
		} else if (type.equals(DeviceType.DIMMER)) {
			BigDecimal dimLevel = BigDecimal.ZERO;
			
			if (status.getValues().get("dimlevel") != null) 
				dimLevel = getPercentageFromDimLevel(status.getValues().get("dimlevel"));
			else {
				// Dimmer items can can also be switched on or off in pilight. 
				// When this happens, the dimmer value is not reported. At least we know it's on or off.
				dimLevel = state.equals(OnOffType.ON) ? new BigDecimal("100") : BigDecimal.ZERO;
			}
			
			state = new PercentType(dimLevel);
		}
		
		for (PilightBindingConfig config : configs) {
			eventPublisher.postUpdate(config.getItemName(), state);
		}
	}
	
	private void processContactEvent(List<PilightBindingConfig> configs, Status status) {
		String stateString = status.getValues().get("state").toUpperCase();
		State state = PilightContactType.valueOf(stateString).toOpenClosedType();
		
		for (PilightBindingConfig config : configs) {
			eventPublisher.postUpdate(config.getItemName(), state);
		}
	}
	
	/**
	 * @{inheritDoc}
	 */
	@Override
	protected void internalReceiveCommand(String itemName, Command command) {
		PilightBindingProvider provider = findFirstMatchingBindingProvider(itemName);
		PilightBindingConfig config = provider.getBindingConfig(itemName);
		PilightConnection connection = connections.get(config.getInstance());
		
		Action action = createUpdateCommand(command, config);
		
		if (action != null)
			sendUpdate(action, connection);
	}
	
	private void sendUpdate(Action action, PilightConnection connection) 
	{
		connection.getConnector().doUpdate(action);
	}

	private Action createUpdateCommand(Command command,	PilightBindingConfig config) {
		Action action = new Action(Action.ACTION_CONTROL);
		
		Code code = new Code();
		code.setDevice(config.getDevice());
		
		if (command instanceof OnOffType) {
			setOnOffValue((OnOffType)command, code);
		} else if (command instanceof PercentType){
			setDimmerValue((PercentType) command, code);
		} else {
			logger.error("Only OnOffType and PercentType can be changed by the pilight binding");
			return null;
		}
			
		action.setCode(code);
		return action;
	}

	private void setOnOffValue(OnOffType onOff, Code code) {
		code.setState(onOff.equals(OnOffType.ON) ? Code.STATE_ON : Code.STATE_OFF);
	}

	private void setDimmerValue(PercentType percent, Code code) {
		if (BigDecimal.ZERO.equals(percent.toBigDecimal())) {
			// pilight is not responding to commands that set both the dimlevel to 0 and state to off. 
			// So, we're only updating the state for now 
			code.setState(Code.STATE_OFF); 
		} else {
			BigDecimal dimlevel = new BigDecimal(percent.toBigDecimal().toString()).setScale(2)
					.divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP)
					.multiply(BigDecimal.valueOf(MAX_DIM_LEVEL)).setScale(0, RoundingMode.HALF_UP);
			
			Values values = new Values();
			values.setDimlevel(dimlevel.intValue());
			code.setValues(values);
			code.setState(dimlevel.compareTo(BigDecimal.ZERO) == 1 ? Code.STATE_ON : Code.STATE_OFF);
		}
	}
	
	private PilightBindingProvider findFirstMatchingBindingProvider(String itemName) {
		for (PilightBindingProvider provider : providers) {
			 if (provider.getItemNames().contains(itemName))
				 return provider;
		}
			 
		return null;
	}
	
	private List<PilightBindingConfig> getConfigs(String instance, String device) {
		for (PilightBindingProvider provider : providers) {
			List<PilightBindingConfig> configs = provider.getBindingConfigs(instance, device);
			 if (!configs.isEmpty())
				 return configs;
		}
			 
		return new ArrayList<PilightBindingConfig>();
	}
		
	/**
	 * @{inheritDoc}
	 */
	@Override
	public void updated(Dictionary<String, ?> config) throws ConfigurationException {
		closeConnections();
		
		if (config != null) {			
			this.connections = parseConfig(config);
			
			if (this.connections.size() > 0) {
				for (Entry<String, PilightConnection> entry : this.connections.entrySet()) {
					PilightConnection connection = entry.getValue();
					startConnector(connection);
				}
			}
		}
	}

	private void closeConnections() {
		for (Entry<String, PilightConnection> entry : connections.entrySet()) {
			PilightConnection connection = entry.getValue();
			if (connection.getConnector() != null) {
				connection.getConnector().close();
			}
		}
	}

	private Map<String, PilightConnection> parseConfig(Dictionary<String, ?> config) {
		Map<String, PilightConnection> connections = new HashMap<String, PilightConnection>();
		Enumeration<String> keys = config.keys();

		while (keys.hasMoreElements()) {
			String key = keys.nextElement();

			if ("service.pid".equals(key)) 
				continue;

			String[] parts = key.split("\\.");
			String instance = parts[0];

			PilightConnection connection = connections.get(instance);
			if (connection == null) {
				connection = new PilightConnection();
				connection.setInstance(instance);
			}

			String value = ((String) config.get(key)).trim();

			if ("host".equals(parts[1])) {
				connection.setHostname(value);
			}
			if ("port".equals(parts[1])) {
				connection.setPort(Integer.valueOf(value));
			}
			if ("delay".equals(parts[1])) {
				connection.setDelay(Long.valueOf(value));
			}

			connections.put(instance, connection);
		}
		return connections;
	}

	private void startConnector(PilightConnection connection) {
		connection.setConnector(new PilightConnector(connection, new IPilightMessageReceivedCallback() {
			@Override
			public void messageReceived(PilightConnection connection,
					Status status) {
				processStatus(connection, status);
			}
		}));
		connection.getConnector().start();
		setInitialState();
	}

	/**
	 * Gets the state for item in {@code bindingConfig} from {@code pilightConfig}  
	 * 
	 * @param pilightConfig The complete pilight configuration
	 * @param bindingConfig Specific pilight item in openHAB
	 * @return Current state of the item 
	 */
	private State getStateFromConfig(Config pilightConfig, PilightBindingConfig bindingConfig) {
		Device dev = pilightConfig.getDevices().get(bindingConfig.getDevice());
		
		if (dev != null) {
			if (bindingConfig.getItemType().equals(SwitchItem.class) || bindingConfig.getItemType().equals(DimmerItem.class)) {
				OnOffType state = OnOffType.valueOf(dev.getState().toUpperCase());
				
				if (dev.getDimlevel() != null && dev.getDimlevel() > 0) {
					if (state.equals(OnOffType.ON))
						return new PercentType(getPercentageFromDimLevel(dev.getDimlevel().toString()));
					else
						return new PercentType(0);
				}
				
				return state;
			} else if (bindingConfig.getItemType().equals(ContactItem.class)) {
				OpenClosedType state = PilightContactType.valueOf(dev.getState().toUpperCase()).toOpenClosedType();
				return state;
			} else if (bindingConfig.getItemType().equals(StringItem.class) || bindingConfig.getItemType().equals(NumberItem.class)) {
				String property = bindingConfig.getProperty();
				if (dev.getProperties().containsKey(property)) {
					String value = dev.getProperties().get(property);
					return getState(value, bindingConfig);
				}
			}
		}
		return null;
	}
	
	/**
	 * @{inheritDoc}
	 */
	@Override
	public void bindingChanged(BindingProvider provider, String itemName) {
		logger.debug("Binding changed for item {}", itemName);
		checkItemState(provider, itemName);
	}
	
	/**
	 * @{inheritDoc}
	 */
	@Override
	public void allBindingsChanged(BindingProvider provider) {
		logger.debug("All bindings changed");
		for (String itemName : provider.getItemNames()) {
			checkItemState(provider, itemName);
		}
	}
	
	private void setInitialState() {
		for (PilightBindingProvider provider : providers) {
			allBindingsChanged(provider);
		}
	}

	/**
	 * Synchronize itemName with the current state in pilight. 
	 * 
	 * @param provider The PilightBindingProvider 
	 * @param itemName The itemName in openHAB
	 */
	private void checkItemState(BindingProvider provider, final String itemName) {
		PilightBindingProvider pilightProvider = (PilightBindingProvider) provider;
		final PilightBindingConfig config = pilightProvider.getBindingConfig(itemName);
		
		if (config != null) {
			PilightConnection connection = connections.get(config.getInstance());
			if (connection.isConnected()) {
				connection.getConnector().refreshConfig(new IPilightConfigReceivedCallback() {
					@Override
					public void configReceived(PilightConnection connection) {
						State state = getStateFromConfig(connection.getConfig(), config);
						if (state != null) {
							eventPublisher.postUpdate(itemName, state);
						}
					}
				});
			}
		}
	}

	private BigDecimal getPercentageFromDimLevel(String string) {
		return new BigDecimal(string).setScale(2)
			.divide(new BigDecimal(MAX_DIM_LEVEL), RoundingMode.HALF_UP)
			.multiply(new BigDecimal(100));
	}
	
}
