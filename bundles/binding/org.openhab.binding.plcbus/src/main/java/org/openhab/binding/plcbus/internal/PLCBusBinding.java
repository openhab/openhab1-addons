/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.plcbus.internal;

import java.util.Dictionary;

import org.openhab.binding.plcbus.PLCBusBindingProvider;
import org.openhab.binding.plcbus.internal.protocol.IPLCBusController;
import org.openhab.binding.plcbus.internal.protocol.ISerialPortGateway;
import org.openhab.binding.plcbus.internal.protocol.PLCBusController;
import org.openhab.binding.plcbus.internal.protocol.SerialPortGateway;
import org.openhab.binding.plcbus.internal.protocol.StatusResponse;
import org.openhab.core.binding.AbstractBinding;
import org.openhab.core.binding.BindingProvider;
import org.openhab.core.library.types.IncreaseDecreaseType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.StopMoveType;
import org.openhab.core.library.types.UpDownType;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.openhab.core.types.UnDefType;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Binding for PLC Bus
 * 
 * @author Robin Lenz
 * @since 1.1.0
 */
public class PLCBusBinding extends AbstractBinding<PLCBusBindingProvider> implements
		ManagedService {

	private static Logger logger = LoggerFactory.getLogger(PLCBusBinding.class);

	private ISerialPortGateway serialPortGateway;
	

	public void activate(ComponentContext componentContext) {
	}

	public void deactivate(ComponentContext componentContext) {
		for (PLCBusBindingProvider provider : providers) {
			provider.removeBindingChangeListener(this);
		}
		providers.clear();

		if (serialPortGateway != null) {
			serialPortGateway.close();
		}
	}
	

	/**
	 * {@inheritDoc}
	 */
	public void bindingChanged(BindingProvider provider, String itemName) {
		super.bindingChanged(provider, itemName);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void internalReceiveCommand(String itemName, Command command) {

		PLCBusBindingConfig config = tryGetConfigFor(itemName);

		if (config == null) {
			logger.error("No config found for item %s", itemName);
			return;
		}

		IPLCBusController controller = PLCBusController.create(serialPortGateway);
		if (command == OnOffType.ON) {
			controller.switchOn(config.getUnit());
		} else if (command == OnOffType.OFF) {
			controller.switchOff(config.getUnit());
		} else if (command == IncreaseDecreaseType.INCREASE) {
			controller.bright(config.getUnit(), config.getSeconds());
		} else if (command == IncreaseDecreaseType.DECREASE) {
			controller.dim(config.getUnit(), config.getSeconds());
		} else if (command == StopMoveType.STOP) {
			controller.fadeStop(config.getUnit());
		} else if (command == UpDownType.UP) {
			controller.switchOn(config.getUnit());
		} else if (command == UpDownType.DOWN) {
			controller.switchOff(config.getUnit());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void internalReceiveUpdate(String itemName, State newState) {

		PLCBusBindingConfig config = tryGetConfigFor(itemName);

		if (config == null) {
			logger.error("No config found for %s", itemName);
			return;
		}

		IPLCBusController controller = PLCBusController.create(serialPortGateway);

		if (newState == UnDefType.UNDEF) {
			StatusResponse response = controller.requestStatusFor(config.getUnit());

			State status = (response.isUnitOn()) ? OnOffType.ON : OnOffType.OFF;
			this.eventPublisher.postUpdate(itemName, status);
		}
	}

	private PLCBusBindingConfig tryGetConfigFor(String itemName) {
		for (PLCBusBindingProvider provider : this.providers) {
			PLCBusBindingConfig config = provider.getConfigFor(itemName);
			if (config != null) {
				return config;
			}
		}
		return null;
	}

	@Override
	public void updated(Dictionary<String, ?> config) throws ConfigurationException {

		if (config == null) {
			return;
		}
		serialPortGateway = SerialPortGateway.create((String) config.get("port"));

		if (serialPortGateway == null) {
			logger.error("No Serialport config in openhab.cfg found");
		}
	}
	
}
