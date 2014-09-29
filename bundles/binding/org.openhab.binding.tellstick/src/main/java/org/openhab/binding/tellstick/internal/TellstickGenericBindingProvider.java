/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.tellstick.internal;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.openhab.binding.tellstick.TellstickBindingConfig;
import org.openhab.binding.tellstick.TellstickBindingProvider;
import org.openhab.binding.tellstick.TellstickValueSelector;
import org.openhab.binding.tellstick.internal.device.SupportedMethodsException;
import org.openhab.binding.tellstick.internal.device.TellsticEventHandler;
import org.openhab.binding.tellstick.internal.device.TellstickDevice;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.DimmerItem;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is responsible for parsing the binding configuration.
 * 
 * @author jarlebh
 * @since 1.5.0
 */
public class TellstickGenericBindingProvider extends AbstractGenericBindingProvider implements TellstickBindingProvider {
	private static final Logger logger = LoggerFactory.getLogger(TellstickGenericBindingProvider.class);

	private List<TellstickDevice> allDevices = null;

	private TellsticEventHandler listener = null;

	/**
	 * {@inheritDoc}
	 */
	public String getBindingType() {
		return "tellstick";
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	public void validateItemType(Item item, String bindingConfig) throws BindingConfigParseException {
		if (!(item instanceof SwitchItem || item instanceof NumberItem || item instanceof DimmerItem)) {
			throw new BindingConfigParseException(
					"item '"
							+ item.getName()
							+ "' is of type '"
							+ item.getClass().getSimpleName()
							+ "', only Number and Switch- and DimmerItems are allowed - please check your *.items configuration");
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processBindingConfiguration(String context, Item item, String bindingConfig)
			throws BindingConfigParseException {
		super.processBindingConfiguration(context, item, bindingConfig);
		TellstickBindingConfig config = new TellstickBindingConfig();
		config.setItemName(item.getName());

		String[] configParts = bindingConfig.trim().split(":");

		if (configParts.length < 1) {
			throw new BindingConfigParseException("Tellstick binding must contain two parts separated by ':'");
		}
		TellstickDevice device;
		try {
			device = findDevice(configParts[0].trim());
		} catch (SupportedMethodsException e) {
			throw new BindingConfigParseException(e.getMessage());
		}
		validateBinding(item, configParts, device);

		if (device == null) {
			config.setId(Integer.valueOf(configParts[0].trim()));
		} else {
			config.setId(device.getId());
		}

		config.setValueSelector(TellstickValueSelector.getValueSelector(configParts[1].trim()));
		if (configParts.length > 2 && configParts[2].trim().length() > 0) {
			config.setUsageSelector(TellstickValueSelector.getValueSelector(configParts[2].trim()));
		}
		if (configParts.length > 3) {
			config.setResend(Integer.parseInt(configParts[3]));
		}

		logger.debug("Context:" + context + " Item " + item + " Conf:" + config);
		addBindingConfig(item, config);
	}

	private void validateBinding(Item item, String[] configParts, TellstickDevice device)
			throws BindingConfigParseException {
		if (device == null && !StringUtils.isNumeric(configParts[0].trim())) {
			throw new BindingConfigParseException("item '" + item.getName() + "' telldus device "
					+ configParts[0].trim() + " not found");
		}
		if (configParts.length > 3 && !StringUtils.isNumeric(configParts[3].trim())) {
			throw new BindingConfigParseException("item '" + item.getName() + "' resend config wrong"
					+ configParts[3].trim() + " not a number");
		}
	}

	private TellstickDevice findDevice(String deviceIdent) throws SupportedMethodsException {
		TellstickDevice result = null;
		if (allDevices == null) {
			updateDevices();
		}
		for (TellstickDevice device : allDevices) {
			if (device.getName().equals(deviceIdent)) {
				result = device;
				break;
			}
		}
		return result;
	}

	private void updateDevices() throws SupportedMethodsException {
		allDevices = TellstickDevice.getDevices();
		if (listener == null) {
			listener = new TellsticEventHandler(allDevices);
		} else {
			listener.setDeviceList(allDevices);
		}
	}

	public void addListener(EventListener changeListener) {
		listener.addListener(changeListener);
	}

	public void resetTellstickListener() throws SupportedMethodsException {
		try {
			listener.remove();
		} catch (Exception e) {
			logger.error("Failed to remove telldus core listeners", e);
		}
		try {
			JNA.CLibrary.INSTANCE.tdClose();
			JNA.CLibrary.INSTANCE.tdInit();
			updateDevices();
			listener.setupListeners();
		} catch (Exception e) {
			logger.error("Failed to close and init listener");
			throw new RuntimeException("Could not reset tellstick", e);
		}
	}

	@Override
	public TellstickBindingConfig getTellstickBindingConfig(int id, TellstickValueSelector valueSel) {
		TellstickBindingConfig name = null;
		for (Entry<String, BindingConfig> entry : bindingConfigs.entrySet()) {
			TellstickBindingConfig bv = (TellstickBindingConfig) entry.getValue();
			if (bv.getId() == id) {
				if (valueSel == null || valueSel.equals(bv.getValueSelector()))
					name = bv;
			}
		}
		return name;
	}

	public TellstickBindingConfig getTellstickBindingConfig(String itemName) {
		return (TellstickBindingConfig) bindingConfigs.get(itemName);
	}

	@Override
	public TellstickDevice getDevice(String itemName) {
		TellstickDevice res = null;

		TellstickBindingConfig conf = getTellstickBindingConfig(itemName);
		if (conf != null) {
			for (TellstickDevice device : getAllDevices()) {
				if (device.getId() == conf.getId()) {
					res = device;
					break;
				}
			}
		} else {
			logger.warn("Could not find conf for " + itemName);
		}
		return res;
	}

	private List<TellstickDevice> getAllDevices() {
		if (allDevices == null || allDevices.isEmpty()) {
			try {
				updateDevices();
			} catch (SupportedMethodsException e) {
				logger.error("Failed to get devices", e);
				throw new RuntimeException("Failed to list devices", e);
			}
		}
		return allDevices;
	}

	@Override
	public void removeTellstickListener() {
		if (listener != null && !listener.getAllListeners().isEmpty()) {
			for (EventListener lis : new ArrayList<EventListener>(listener.getAllListeners())) {
				listener.removeListener(lis);
			}
		}
	}
}
