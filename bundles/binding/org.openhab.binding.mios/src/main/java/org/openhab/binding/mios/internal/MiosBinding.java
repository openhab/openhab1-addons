/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.mios.internal;

import java.util.Calendar;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.openhab.binding.mios.MiosActionProvider;
import org.openhab.binding.mios.MiosBindingProvider;
import org.openhab.binding.mios.internal.config.DeviceBindingConfig;
import org.openhab.binding.mios.internal.config.MiosBindingConfig;
import org.openhab.binding.mios.internal.config.SceneBindingConfig;
import org.openhab.core.binding.AbstractBinding;
import org.openhab.core.binding.BindingProvider;
import org.openhab.core.items.Item;
import org.openhab.core.items.ItemRegistry;
import org.openhab.core.library.types.DateTimeType;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.openhab.core.types.UnDefType;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The MiOS Binding is responsible for coordinating changes to openHAB Items from the corresponding/bound information
 * from each configured MiOS Unit.
 * 
 * The Binding allows information from a MiOS Unit to be bound to openHAB Items, as well a allowing openHAB Commands to
 * be propagated back to the MiOS Unit under control.
 * 
 * The following types of information from a MiOS Unit can be bound to openHAB Items:
 * <p>
 * 
 * <ul>
 * <li>Device Attributes & State Variables
 * <li>Scene Attributes
 * <li>System Attributes
 * </ul>
 * <p>
 * 
 * Similarly, through a configurable set of openHAB Transformations, any Commands sent to these Items can be proxied
 * back to the corresponding MiOS Unit.
 * <p>
 * 
 * Data flowing between the MiOS Unit and openHAB can be transformed as it flows between the two systems. This
 * transformation is configurable, and is expressed in the Item Binding using standard openHAB
 * {@code TransformationService} expressions.
 * <p>
 * 
 * Example MAP-based Transformation files are provided for commonly required transformations. <br>
 * eg. For Switch Data flowing into openHAB {@code MAP(miosSwitchIn.map)}, and for Switch Commands flowing in to MiOS
 * {@code MAP(miosSwitchOut.map)}
 * <p>
 * 
 * The Binding follows the general interaction principals outlined in the MiOS
 * {@link <a href="http://wiki.micasaverde.com/index.php/UI_Simple">UI Simple</a>} documentation.
 * <p>
 * 
 * In effect, the binding behaves like a "remote control" to one or more configured MiOS Units, utilizing a HTTP-based
 * Long-poll to receive updates occurring within each Unit, and transforming them into corresponding updates to the
 * openHAB Items that have been bound.
 * <p>
 * 
 * All updates are received asynchronously from the MiOS Units. This interaction is managed by a per MiOS Unit
 * {@link MiosUnitConnector} Polling Thread object that utilizes a {@link MiosUnit MiOS Unit} configuration object to
 * determine the location of the MiOS Unit.
 * <p>
 * 
 * @author Mark Clark
 * @since 1.6.0
 */
public class MiosBinding extends AbstractBinding<MiosBindingProvider> implements ManagedService, MiosActionProvider {

	private static final Logger logger = LoggerFactory.getLogger(MiosBinding.class);

	private Map<String, MiosUnitConnector> connectors = new HashMap<String, MiosUnitConnector>();
	private Map<String, MiosUnit> nameUnitMapper = null;

	private String getName() {
		return "MiosBinding";
	}

	/**
	 * Invoked by OSGi Framework, once per instance, during the Binding activation process.
	 * 
	 * OSGi is configured to do this in OSGI-INF/activebinding.xml
	 */
	public void activate() {

		logger.debug(getName() + " activate()");
	}

	/**
	 * Invoked by the OSGi Framework, once per instance, during the Binding deactivation process.
	 * 
	 * Internally this is used to close out any resources used by the MiOS Binding.
	 * 
	 * OSGi is configured to do this in OSGI-INF/activebinding.xml
	 */
	public void deactivate() {
		logger.debug(getName() + " deactivate()");

		// close any open connections
		for (MiosUnitConnector connector : connectors.values()) {
			if (connector.isConnected()) {
				connector.close();
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void bindingChanged(BindingProvider provider, String itemName) {
		logger.debug("bindingChanged: start provider '{}', itemName '{}'", provider, itemName);

		if (provider instanceof MiosBindingProvider) {
			registerWatch((MiosBindingProvider) provider, itemName);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void allBindingsChanged(BindingProvider provider) {
		logger.debug("allBindingsChanged: start provider '{}'", provider);

		if (provider instanceof MiosBindingProvider) {
			MiosBindingProvider miosProvider = (MiosBindingProvider) provider;

			for (String itemName : provider.getItemNames()) {
				registerWatch(miosProvider, itemName);
			}
		}
	}

	private void registerAllWatches() {
		logger.debug("registerAllWatches: start");

		for (BindingProvider provider : providers) {
			logger.debug("registerAllWatches: provider '{}'", provider.getClass());

			if (provider instanceof MiosBindingProvider) {
				MiosBindingProvider miosProvider = (MiosBindingProvider) provider;

				for (String itemName : provider.getItemNames()) {
					registerWatch(miosProvider, itemName);
				}
			}
		}
	}

	private void registerWatch(MiosBindingProvider miosProvider, String itemName) {
		logger.debug("registerWatch: start miosProvider '{}', itemName '{}'", miosProvider, itemName);

		String unitName = miosProvider.getMiosUnitName(itemName);

		// Minimally we need to do this part, so that the MiosConnector objects
		// get brought into existence (and threads started)
		// Joy! Getters with side-effects.

		// TODO: Work out a cleaner entry point for the Child connections to get
		// started.
		MiosUnitConnector connector = getMiosConnector(unitName);
	}

	private String getMiosUnitName(String itemName) {
		logger.trace("getMiosUnitName: start itemName '{}'", itemName);

		for (BindingProvider provider : providers) {
			if (provider instanceof MiosBindingProvider) {
				if (provider.getItemNames().contains(itemName)) {
					return ((MiosBindingProvider) provider).getMiosUnitName(itemName);
				}
			}
		}
		return null;
	}

	private MiosUnitConnector getMiosConnector(String unitName) {
		logger.trace("getMiosConnector: start unitName '{}'", unitName);

		// sanity check
		if (unitName == null)
			return null;

		// check if the connector for this unit already exists
		MiosUnitConnector connector = connectors.get(unitName);
		if (connector != null)
			return connector;

		MiosUnit miosUnit;

		// NOTE: We deviate from the XBMC Binding, in that we only accept
		// "names" presented in the openHAB configuration files.

		// check if we have been initialized yet - can't process
		// named units until we have read the binding config.
		if (nameUnitMapper == null) {
			logger.trace("Attempting to access the named MiOS Unit '{}' before the binding config has been loaded",
					unitName);
			return null;
		}

		miosUnit = nameUnitMapper.get(unitName);

		// Check this Unit name exists in our config
		if (miosUnit == null) {
			logger.error("Named MiOS Unit '{}' does not exist in the binding config", unitName);
			return null;
		}

		// create a new connection handler
		logger.debug("Creating new MiosConnector for '{}' on {}", unitName, miosUnit.getHostname());
		connector = new MiosUnitConnector(miosUnit, this);
		connectors.put(unitName, connector);

		// attempt to open the connection straight away
		try {
			connector.open();
		} catch (Exception e) {
			logger.error("Connection failed for '{}' on {}", unitName, miosUnit.getHostname());
		}

		return connector;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void internalReceiveCommand(String itemName, Command command) {
		try {
			logger.debug("internalReceiveCommand: itemName '{}', command '{}'", itemName, command);

			// Lookup the MiOS Unit name and property for this item
			String unitName = getMiosUnitName(itemName);

			MiosUnitConnector connector = getMiosConnector(unitName);
			if (connector == null) {
				logger.warn("Received command ({}) for item '{}' but no connector found for MiOS Unit '{}', ignoring",
						new Object[] { command.toString(), itemName, unitName });
				return;
			}

			if (!connector.isConnected()) {
				logger.warn(
						"Received command ({}) for item '{}' but the connection to the MiOS Unit '{}' is down, ignoring",
						new Object[] { command.toString(), itemName, unitName });
				return;
			}

			for (BindingProvider provider : providers) {
				if (provider instanceof MiosBindingProvider) {
					MiosBindingProviderImpl miosProvider = (MiosBindingProviderImpl) provider;
					MiosBindingConfig config = miosProvider.getMiosBindingConfig(itemName);

					if (config != null) {
						ItemRegistry reg = miosProvider.getItemRegistry();

						if (reg != null) {
							Item item = reg.getItem(config.getItemName());
							State state = item.getState();
							connector.invokeCommand(config, command, state);
						} else {
							logger.warn("internalReceiveCommand: Missing ItemRegistry for item '{}' command '{}'",
									itemName, command);
						}
					} else {
						logger.trace("internalReceiveCommand: Missing BindingConfig for item '{}' command '{}'",
								itemName, command);
					}
				}
			}

		} catch (Exception e) {
			logger.error("Error handling command", e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void internalReceiveUpdate(String itemName, State newState) {
		logger.trace("internalReceiveUpdate: itemName '{}', newState '{}', class '{}'", new Object[] { itemName,
				newState, newState.getClass() });

		// No need to implement this for MiOS Bridge Binding since anything
		// that needs to be sent back to the MiOS System will be done via a
		// Command.
		//
		// We leave this here to aid debugging. We get more
		// information out of the above logger call than we get from openHAB.
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updated(Dictionary<String, ?> properties) throws ConfigurationException {
		logger.trace(getName() + " updated()");

		Map<String, MiosUnit> units = new HashMap<String, MiosUnit>();

		Enumeration<String> keys = properties.keys();

		while (keys.hasMoreElements()) {
			String key = keys.nextElement();

			if ("service.pid".equals(key)) {
				continue;
			}

			// Only apply this pattern once, as the leading-edge may not be the
			// name of a unit. We support two forms:
			//
			// mios:lounge.host=...
			// mios:host=
			//
			// The latter form refers to the "default" Unit, which can be used
			// in bindings to make things simpler for single-unit owners.
			//
			String unitName = null;
			String value = ((String) properties.get(key)).trim();
			String[] parts = key.split("\\.", 2);

			if (parts.length != 1) {
				unitName = parts[0];
				key = parts[1];
			}

			boolean created = false;
			String hackUnitName = (unitName == null) ? MiosUnit.CONFIG_DEFAULT_UNIT : unitName;
			MiosUnit unit = units.get(hackUnitName);

			if (unit == null) {
				unit = new MiosUnit(hackUnitName);
				created = true;
			}

			if ("host".equals(key)) {
				unit.setHostname(value);
			} else if ("port".equals(key)) {
				unit.setPort(Integer.valueOf(value));
			} else if ("timeout".equals(key)) {
				unit.setTimeout(Integer.valueOf(value));
			} else if ("minimumDelay".equals(key)) {
				unit.setMinimumDelay(Integer.valueOf(value));
			} else if ("refreshCount".equals(key)) {
				unit.setRefreshCount(Integer.valueOf(value));
			} else if ("errorCount".equals(key)) {
				unit.setErrorCount(Integer.valueOf(value));
			} else {
				logger.warn("Unexpected configuration parameter {}", key);
				created = false;
			}

			// Only bother to put it back if we created a new one, otherwise
			// it's already there!
			if (created) {
				logger.debug("updated: Created Unit '{}'", hackUnitName);
				units.put(hackUnitName, unit);
			}
		}

		nameUnitMapper = units;
		registerAllWatches();
	}

	/**
	 * Push a value into all openHAB Items that match a given MiOS Property name (from the Item Binding declaration).
	 * <p>
	 * In the process, this routine will perform Datatype conversions from Java types to openHAB's type system. These
	 * conversions are as follows:
	 * <p>
	 * <ul>
	 * <li>{@code String} -> {@code StringType}
	 * <li>{@code Integer} -> {@code DecimalType}
	 * <li>{@code Double} -> {@code DecimalType}
	 * <li>{@code Boolean} -> {@code StringType} (true == ON, false == OFF)
	 * <li>{@code Calendar} -> {@code DateTimeType}
	 * </ul>
	 * 
	 * @param property
	 *            the MiOS Property name
	 * @param value
	 *            the value to push, per the supported types.
	 * @exception IllegalArgumentException
	 *                thrown if the value isn't one of the supported types.
	 */
	public void postPropertyUpdate(String property, Object value, boolean incremental) throws Exception {
		if (value instanceof String) {
			internalPropertyUpdate(property, new StringType(value == null ? "" : (String) value), incremental);
		} else if (value instanceof Integer) {
			internalPropertyUpdate(property, new DecimalType((Integer) value), incremental);
		} else if (value instanceof Calendar) {
			internalPropertyUpdate(property, new DateTimeType((Calendar) value), incremental);
		} else if (value instanceof Double) {
			internalPropertyUpdate(property, new DecimalType((Double) value), incremental);
		} else if (value instanceof Boolean) {
			postPropertyUpdate(property,
					((Boolean) value).booleanValue() ? OnOffType.ON.toString() : OnOffType.OFF.toString(), incremental);
		} else {
			throw new IllegalArgumentException(String.format("Unexpected Datatype, property=%s datatype=%s", property,
					value.getClass().toString()));
		}
	}

	private void internalPropertyUpdate(String property, State value, boolean incremental) throws Exception {
		int bound = 0;

		if (value == null) {
			logger.trace("internalPropertyUpdate: Value is null for Property '{}', ignored.", property);
			return;
		}

		for (BindingProvider provider : providers) {
			if (provider instanceof MiosBindingProvider) {
				MiosBindingProviderImpl miosProvider = (MiosBindingProviderImpl) provider;

				for (String itemName : miosProvider.getItemNamesForProperty(property)) {

					MiosBindingConfig config = miosProvider.getMiosBindingConfig(itemName);

					if (config != null) {
						// Transform whatever value we have, based upon the
						// Transformation Service specified in the Binding
						// Config.

						State newValue = config.transformIn(value);

						if (newValue != value) {
							logger.trace("internalPropertyUpdate: transformation performed, from '{}' to '{}'", value,
									newValue);
						}

						//
						// Set the value only if:
						// * we're running Incrementally OR;
						// * the CURRENT value is UNDEFINED OR;
						// * the CURRENT value is different from the NEW value
						//
						// This is to handle a case where the MiOS Unit
						// "restarts" and floods us with a bunch of the same
						// data. In this case, we don't want to flood the Items,
						// since it may re-trigger a bunch of Rules in an
						// unnecessary manner.
						//
						if (incremental) {
							logger.debug("internalPropertyUpdate: BOUND (Incr) Updating '{} {mios=\"{}\"}' to '{}'",
									itemName, property, newValue);

							eventPublisher.postUpdate(itemName, newValue);
						} else {
							ItemRegistry reg = miosProvider.getItemRegistry();
							State oldValue = reg.getItem(itemName).getState();

							if ((oldValue == null && newValue != null)
									|| (UnDefType.UNDEF.equals(oldValue) && !UnDefType.UNDEF.equals(newValue))
									|| !oldValue.equals(newValue)) {
								logger.debug(
										"internalPropertyUpdate: BOUND (Full) Updating '{} {mios=\"{}\"}' to '{}', was '{}'",
										new Object[] { itemName, property, newValue, oldValue });

								eventPublisher.postUpdate(itemName, newValue);
							} else {
								logger.trace(
										"internalPropertyUpdate: BOUND (Full) Ignoring '{} {mios=\"{}\"}' to '{}', was '{}'",
										new Object[] { itemName, property, newValue, oldValue });
							}
						}
						bound++;
					} else {
						logger.trace("internalPropertyUpdate: Found null BindingConfig for item '{}' property '{}'",
								itemName, property);
					}
				}
			}
		}

		if (bound == 0) {
			logger.trace("internalPropertyUpdate: NOT BOUND {mios=\"{}\"}, value={}", property, value);
		} else {
			logger.trace("internalPropertyUpdate: BOUND {mios=\"{}\"}, value={}, bound {} time(s)", new Object[] {
					property, value, bound });
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean invokeMiosScene(String itemName) {
		try {
			logger.debug("invokeMiosScene item {}", itemName);

			boolean sent = false;

			// Lookup the MiOS Unit name and property for this item
			String unitName = getMiosUnitName(itemName);

			MiosUnitConnector connector = getMiosConnector(unitName);
			if (connector == null) {
				logger.warn(
						"invokeMiosScene: Scene call for item '{}' but no connector found for MiOS Unit '{}', ignoring",
						itemName, unitName);
				return false;
			}

			if (!connector.isConnected()) {
				logger.warn(
						"invokeMiosScene: Scene call for item '{}' but the connection to the MiOS Unit '{}' is down, ignoring",
						itemName, unitName);
				return false;
			}

			for (BindingProvider provider : providers) {
				if (provider instanceof MiosBindingProvider) {
					MiosBindingProviderImpl miosProvider = (MiosBindingProviderImpl) provider;
					MiosBindingConfig config = miosProvider.getMiosBindingConfig(itemName);

					if ((config != null) && (config instanceof SceneBindingConfig)) {
						connector.invokeScene((SceneBindingConfig) config);
						sent = true;
					} else {
						logger.error(
								"invokeMiosScene: Missing BindingConfig for item '{}', or not bound to a MiOS Scene.",
								itemName);
					}
				}
			}

			return sent;
		} catch (Exception e) {
			logger.error("invokeMiosScene: Error handling command", e);
			return false;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean invokeMiosAction(String itemName, String actionName, List<Entry<String, Object>> params) {
		try {
			logger.debug("invokeMiosAction item {}, action {}, params {}",
					new Object[] { itemName, actionName, Integer.valueOf((params == null) ? 0 : params.size()) });

			boolean sent = false;

			// Lookup the MiOS Unit name and property for this item
			String unitName = getMiosUnitName(itemName);

			MiosUnitConnector connector = getMiosConnector(unitName);
			if (connector == null) {
				logger.warn(
						"invokeMiosAction: Action call for item '{}' but no connector found for MiOS Unit '{}', ignoring",
						itemName, unitName);
				return false;
			}

			if (!connector.isConnected()) {
				logger.warn(
						"invokeMiosAction: Action call for item '{}' but the connection to the MiOS Unit '{}' is down, ignoring",
						itemName, unitName);
				return false;
			}

			for (BindingProvider provider : providers) {
				if (provider instanceof MiosBindingProvider) {
					MiosBindingProviderImpl miosProvider = (MiosBindingProviderImpl) provider;
					MiosBindingConfig config = miosProvider.getMiosBindingConfig(itemName);

					if ((config != null) && (config instanceof DeviceBindingConfig)) {
						connector.invokeAction((DeviceBindingConfig) config, actionName, params);
						sent = true;
					} else {
						logger.error(
								"invokeMiosAction: Missing BindingConfig for item '{}', or not bound to a MiOS Device.",
								itemName);
					}
				}
			}

			return sent;
		} catch (Exception e) {
			logger.error("invokeMiosScene: Error handling command", e);
			return false;
		}
	}

}
