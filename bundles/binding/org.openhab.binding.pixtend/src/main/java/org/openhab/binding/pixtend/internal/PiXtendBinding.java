/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.pixtend.internal;

import java.util.List;
import java.util.Map;

import org.openhab.binding.pixtend.PiXtendBindingProvider;
import org.openhab.core.binding.AbstractActiveBinding;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.OpenClosedType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.goroot.pixtend4j.pixtend4j.PiXtend;
import de.goroot.pixtend4j.pixtend4j.PiXtendFactory;

/**
 * Implement this class if you are going create an actively polling service like
 * querying a Website/Device.
 *
 * @author Michael Kolb
 * @since 1.7.1
 */
public class PiXtendBinding extends AbstractActiveBinding<PiXtendBindingProvider> {

	private static final Logger logger = LoggerFactory.getLogger(PiXtendBinding.class);

	/**
	 * The BundleContext. This is only valid when the bundle is ACTIVE. It is
	 * set in the activate() method and must not be accessed anymore once the
	 * deactivate() method was called or before activate() was called.
	 */
	protected BundleContext bundleContext;
	private PiXtend connector;

	/**
	 * the refresh interval which is used to poll values from the PiXtend server
	 */
	private static final long REFRESH_INTERVAL = 200;

	public PiXtendBinding() {
	}

	/**
	 * Called by the SCR to activate the component with its configuration read
	 * from CAS
	 *
	 * @param bundleContext
	 *            BundleContext of the Bundle that defines this component
	 * @param configuration
	 *            Configuration properties for this component obtained from the
	 *            ConfigAdmin service
	 */
	public void activate(final BundleContext bundleContext, final Map<String, Object> configuration) {
		this.bundleContext = bundleContext;

		connector = new PiXtendFactory().newAutomaticModeInstance();

		// read further config parameters here ...
		setProperlyConfigured(true);
	}

	/**
	 * Called by the SCR when the configuration of a binding has been changed
	 * through the ConfigAdmin service.
	 *
	 * @param configuration
	 *            Updated configuration properties
	 */
	public void modified(final Map<String, Object> configuration) {
		// update the internal configuration accordingly
	}

	/**
	 * Called by the SCR to deactivate the component when either the
	 * configuration is removed or mandatory references are no longer satisfied
	 * or the component has simply been stopped.
	 *
	 * @param reason
	 *            Reason code for the deactivation:<br>
	 *            <ul>
	 *            <li>0 – Unspecified
	 *            <li>1 – The component was disabled
	 *            <li>2 – A reference became unsatisfied
	 *            <li>3 – A configuration was changed
	 *            <li>4 – A configuration was deleted
	 *            <li>5 – The component was disposed
	 *            <li>6 – The bundle was stopped
	 *            </ul>
	 */
	public void deactivate(final int reason) {
		this.bundleContext = null;
		connector = null;
		// deallocate resources here that are no longer needed and
		// should be reset when activating this binding again
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	protected long getRefreshInterval() {
		return REFRESH_INTERVAL;
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	protected String getName() {
		return "PiXtend Refresh Service";
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	protected void execute() {
		for (PiXtendPort port : PiXtendPort.values()) {
			if (port.isReadable()) {
				try {
					Object value = readPort(port);
					publish(port, value);
				} catch (PiXtendIOException e) {
					logger.error("Error reading port '{}'", port, e);
				}
			}
		}
	}

	private Object readPort(PiXtendPort port) throws PiXtendIOException {
		Object result = null;
		switch (port) {
		case AI0:
			result = connector.getAnalogInput(0);
			break;
		case AI1:
			result = connector.getAnalogInput(1);
			break;
		case AI2:
			result = connector.getAnalogInput(2);
			break;
		case AI3:
			result = connector.getAnalogInput(3);
			break;
		case DI0:
			result = connector.getDigitalInput(0);
			break;
		case DI1:
			result = connector.getDigitalInput(1);
			break;
		case DI2:
			result = connector.getDigitalInput(2);
			break;
		case DI3:
			result = connector.getDigitalInput(3);
			break;
		case DI4:
			result = connector.getDigitalInput(4);
			break;
		case DI5:
			result = connector.getDigitalInput(5);
			break;
		case DI6:
			result = connector.getDigitalInput(6);
			break;
		case DI7:
			result = connector.getDigitalInput(7);
			break;
		case FIRMWARE_VERSION:
			result = connector.getUcVersion();
			break;
		case GPIO0:
			result = connector.getGpioValue(0);
			break;
		case GPIO1:
			result = connector.getGpioValue(1);
			break;
		case GPIO2:
			result = connector.getGpioValue(2);
			break;
		case GPIO3:
			result = connector.getGpioValue(3);
			break;
		case REG_STATUS:
			result = connector.getUcStatusRegister();
			break;
		default:
			throw new PiXtendIOException("Reading port '" + port + "' is not implemented");

		}
		return result;
	}

	private State createState(Object value) {
		if (value instanceof Boolean) {
			return OpenClosedType.valueOf(value.toString());
		} else if (value instanceof Short || value instanceof Byte || value instanceof Float || value instanceof Integer) {
			return DecimalType.valueOf(value.toString());
		} else {
			return StringType.valueOf(value.toString());
		}

	}

	private void publish(PiXtendPort port, Object newValue) {
		for (PiXtendBindingProvider provider : providers) {
			List<String> configuredItems = provider.getItemsForDataPort(port);
			for (String item : configuredItems) {
				eventPublisher.postUpdate(item, createState(newValue));
			}
		}
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	protected void internalReceiveCommand(String itemName, Command command) {
		logger.debug("internalReceiveCommand({},{}) is called!", itemName, command);
		for (PiXtendBindingProvider provider : providers) {
			PiXtendPort port = provider.getOutPort(itemName);
			if (port != null) {
				try {
					writePort(port, command);
				} catch (PiXtendIOException e) {
					logger.error("Error writing command '{}' received for item '{}'", command, itemName, e);
				} catch (IncompatibleCommandException e) {
					logger.error("Command '{}' received for item '{}' is incompatible with bound port", command, itemName, e);
				}
			}
		}
	}

	private void writePort(PiXtendPort port, Command command) throws PiXtendIOException, IncompatibleCommandException {
		// this item is bound to a port
		if (!port.isWritable()) {
			throw new PiXtendIOException("Cannot write command '" + command + "' , to port '" + port + "'. Port is not writable.");
		}

		switch (port) {
		case AO0:
			connector.setAnalogOutput(0, command2short(command));
			break;
		case AO1:
			connector.setAnalogOutput(1, command2short(command));
			break;
		case DO0:
			connector.setDigitalOutput(0, command2Boolean(command));
			break;
		case DO1:
			connector.setDigitalOutput(1, command2Boolean(command));
			break;
		case DO2:
			connector.setDigitalOutput(2, command2Boolean(command));
			break;
		case DO3:
			connector.setDigitalOutput(3, command2Boolean(command));
			break;
		case DO4:
			connector.setDigitalOutput(4, command2Boolean(command));
			break;
		case DO5:
			connector.setDigitalOutput(5, command2Boolean(command));
			break;
		case GPIO0:
			connector.setGpioValue(0, command2Boolean(command));
			break;
		case GPIO1:
			connector.setGpioValue(1, command2Boolean(command));
			break;
		case GPIO2:
			connector.setGpioValue(2, command2Boolean(command));
			break;
		case GPIO3:
			connector.setGpioValue(3, command2Boolean(command));
			break;
		case REL0:
			connector.setRelais(0, command2Boolean(command));
			break;
		case REL1:
			connector.setRelais(1, command2Boolean(command));
			break;
		case REL2:
			connector.setRelais(2, command2Boolean(command));
			break;
		case REL3:
			connector.setRelais(3, command2Boolean(command));
			break;
		case RESET_UC:
			if (command2Boolean(command)) {
				connector.resetUc();
			}
			break;
		default:
			throw new PiXtendIOException("Writing to '" + port + "' is not implemented");
		}
	}

	private short command2short(Command command) throws IncompatibleCommandException {
		if (command instanceof DecimalType) {
			short value = ((DecimalType) command).shortValue();
			return value;
		} else {
			throw new IncompatibleCommandException("Command " + command + " cannot be converted to datatype short");
		}
	}

	private boolean command2Boolean(Command command) throws IncompatibleCommandException {
		if (command instanceof OnOffType) {
			return OnOffType.ON.equals(command);
		} else {
			throw new IncompatibleCommandException("Command " + command + " cannot be converted to datatype boolean");
		}
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	protected void internalReceiveUpdate(String itemName, State newState) {
		logger.debug("internalReceiveUpdate({},{}) is called!", itemName, newState);
	}

}
