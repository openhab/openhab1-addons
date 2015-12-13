/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.sapp.internal;

import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.openhab.binding.sapp.SappBindingProvider;
import org.openhab.binding.sapp.internal.configs.SappBindingConfigContactItem;
import org.openhab.binding.sapp.internal.configs.SappBindingConfigDimmerItem;
import org.openhab.binding.sapp.internal.configs.SappBindingConfigNumberItem;
import org.openhab.binding.sapp.internal.configs.SappBindingConfigRollershutterItem;
import org.openhab.binding.sapp.internal.configs.SappBindingConfigSwitchItem;
import org.openhab.binding.sapp.internal.configs.SappBindingConfigUtils;
import org.openhab.binding.sapp.internal.executer.SappCentralExecuter;
import org.openhab.binding.sapp.internal.executer.SappCentralExecuter.PollingResult;
import org.openhab.binding.sapp.internal.model.SappAddressDecimal;
import org.openhab.binding.sapp.internal.model.SappAddressDimmer;
import org.openhab.binding.sapp.internal.model.SappAddressOnOffControl;
import org.openhab.binding.sapp.internal.model.SappAddressOnOffStatus;
import org.openhab.binding.sapp.internal.model.SappAddressOpenClosedStatus;
import org.openhab.binding.sapp.internal.model.SappAddressRollershutterControl;
import org.openhab.binding.sapp.internal.model.SappAddressRollershutterStatus;
import org.openhab.binding.sapp.internal.model.SappAddressType;
import org.openhab.binding.sapp.internal.model.SappPnmas;
import org.openhab.core.binding.AbstractActiveBinding;
import org.openhab.core.binding.BindingProvider;
import org.openhab.core.items.Item;
import org.openhab.core.items.ItemNotFoundException;
import org.openhab.core.items.ItemRegistry;
import org.openhab.core.items.ItemRegistryChangeListener;
import org.openhab.core.library.items.ContactItem;
import org.openhab.core.library.items.DimmerItem;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.items.RollershutterItem;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.IncreaseDecreaseType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.OpenClosedType;
import org.openhab.core.library.types.PercentType;
import org.openhab.core.library.types.StopMoveType;
import org.openhab.core.library.types.UpDownType;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.paolodenti.jsapp.core.command.base.SappException;
import com.github.paolodenti.jsapp.core.util.SappUtils;

/**
 * Implement this class if you are going create an actively polling service like querying a Website/Device.
 * 
 * @author Paolo Denti
 * @since 1.8.0
 */
public class SappBinding extends AbstractActiveBinding<SappBindingProvider> implements ItemRegistryChangeListener {

	private static final Logger logger = LoggerFactory.getLogger(SappBinding.class);

	private static final String CONFIG_KEY_REFRESH = "refresh";
	private static final String CONFIG_KEY_PNMAS_ENABLED = "pnmas.ids";
	private static final String CONFIG_KEY_PNMAS_ID = "pnmas.%s.ip";
	private static final String CONFIG_KEY_PNMAS_PORT = "pnmas.%s.port";
	private static final String ALL_UPDATE_REQUEST_KEY = "*";

	/**
	 * the refresh interval which is used to poll values from the Sapp server (optional, defaults to 100ms)
	 */
	private long refreshInterval = 100;

	/**
	 * the item registry
	 */
	protected ItemRegistry itemRegistry;

	/**
	 * polling enabler; toggled by switch P type
	 */
	private boolean pollingEnabled = true;

	/**
	 * Called by the SCR to activate the component with its configuration read from CAS
	 * 
	 * @param bundleContext
	 *            BundleContext of the Bundle that defines this component
	 * @param configuration
	 *            Configuration properties for this component obtained from the ConfigAdmin service
	 */
	public void activate(final BundleContext bundleContext, final Map<String, Object> configuration) {

		logger.debug("sapp activate called");

		String refreshIntervalString = (String) configuration.get(CONFIG_KEY_REFRESH);
		if (StringUtils.isNotBlank(refreshIntervalString)) {
			refreshInterval = Long.parseLong(refreshIntervalString);
			logger.debug("set refresh interval: {}", refreshInterval);
		}

		SappBindingProvider provider = getFirstSappBindingProvider();
		if (provider != null) {

			String pnmasEnabled = (String) configuration.get(CONFIG_KEY_PNMAS_ENABLED);
			if (pnmasEnabled != null) {
				String[] pnmasIds = pnmasEnabled.split(",");
				for (String pnmasId : pnmasIds) {
					logger.debug("loading info for pnmas {}", pnmasId);

					String ip = (String) configuration.get(String.format(CONFIG_KEY_PNMAS_ID, pnmasId));
					if (ip == null) {
						logger.warn("ip not found for pnmas {}", pnmasId);
						continue;
					}

					int port;
					String portString = (String) configuration.get(String.format(CONFIG_KEY_PNMAS_PORT, pnmasId));
					if (portString == null) {
						logger.warn("port not found for pnmas {}", pnmasId);
						continue;
					} else {
						try {
							port = Integer.parseInt(portString);
						} catch (NumberFormatException e) {
							logger.warn("bad port number for pnmas {}", pnmasId);
							continue;
						}
					}

					if (provider.getPnmasMap().containsKey(pnmasId)) {
						logger.warn("pnmas {} duplicated, skipping", pnmasId);
						continue;
					}

					provider.getPnmasMap().put(pnmasId, new SappPnmas(ip, port));
				}
				for (String pnmasKey : provider.getPnmasMap().keySet()) {
					logger.debug("pnmas {} : {}:", pnmasKey, provider.getPnmasMap().get(pnmasKey));
				}
			}
		}

		provider.getSappUpdatePendingRequests().replaceAllPendingUpdateRequests(ALL_UPDATE_REQUEST_KEY);
		setProperlyConfigured(true);
	}

	/**
	 * Called by the SCR to deactivate the component when either the configuration is removed or mandatory references
	 * are no longer satisfied or the component has simply been stopped.
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

		logger.debug("sapp deactivate called");
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	protected long getRefreshInterval() {

		return refreshInterval;
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	protected String getName() {

		return "Sapp Refresh Service";
	}

	@Override
	public void allBindingsChanged(BindingProvider provider) {
		((SappBindingProvider) provider).getSappUpdatePendingRequests().replaceAllPendingUpdateRequests(ALL_UPDATE_REQUEST_KEY);
	}

	@Override
	public void bindingChanged(BindingProvider provider, String itemName) {
		((SappBindingProvider) provider).getSappUpdatePendingRequests().addPendingUpdateRequest(itemName);
	}

	/**
	 * Sets locally the item registry
	 * 
	 * @param itemRegistry
	 *            the item registry
	 */
	public void setItemRegistry(ItemRegistry itemRegistry) {
		logger.debug("setting item registry");
		this.itemRegistry = itemRegistry;
		this.itemRegistry.addItemRegistryChangeListener(this);
	}

	/**
	 * Unsets locally the item registry
	 * 
	 * @param itemRegistry
	 *            the item registry
	 */
	public void unsetItemRegistry(ItemRegistry itemRegistry) {
		logger.debug("unsetting item registry");
		this.itemRegistry.removeItemRegistryChangeListener(this);
		this.itemRegistry = null;
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	protected void execute() {

		if (isProperlyConfigured()) { // wait until provider is properly configured and all items are loaded
			SappBindingProvider provider = getFirstSappBindingProvider();
			if (provider != null) {
				if (((SappBindingProvider) provider).getSappUpdatePendingRequests().areUpdatePendingRequestsPresent()) { // reinit
																															// items
					Set<String> toBeProcessed = ((SappBindingProvider) provider).getSappUpdatePendingRequests().getAndClearPendingUpdateRequests();
					initializeAllItemsInProvider(provider, toBeProcessed);
				} else {
					if (!pollingEnabled) {
						return;
					}

					SappCentralExecuter sappCentralExecuter = SappCentralExecuter.getInstance();

					for (String pnmasId : provider.getPnmasMap().keySet()) { // each pnmas
						SappPnmas pnmas = provider.getPnmasMap().get(pnmasId);
						try {
							PollingResult pollingResult = sappCentralExecuter.executePollingSappCommands(pnmas.getIp(), pnmas.getPort());

							if (pollingResult.changedOutputs.size() != 0) {
								for (Byte outputAddress : pollingResult.changedOutputs.keySet()) {
									logger.debug("Output variation {} received, new value is {}", SappUtils.byteToUnsigned(outputAddress), pollingResult.changedOutputs.get(outputAddress));
									provider.setOutputCachedValue(SappUtils.byteToUnsigned(outputAddress), pollingResult.changedOutputs.get(outputAddress).intValue());
									updateState(pnmasId, SappAddressType.OUTPUT, SappUtils.byteToUnsigned(outputAddress), pollingResult.changedOutputs.get(outputAddress).intValue(), provider);
								}
							}

							if (pollingResult.changedInputs.size() != 0) {
								for (Byte inputAddress : pollingResult.changedInputs.keySet()) {
									logger.debug("Input variation {} received, new value is {}", SappUtils.byteToUnsigned(inputAddress), pollingResult.changedInputs.get(inputAddress));
									provider.setInputCachedValue(SappUtils.byteToUnsigned(inputAddress), pollingResult.changedInputs.get(inputAddress).intValue());
									updateState(pnmasId, SappAddressType.INPUT, SappUtils.byteToUnsigned(inputAddress), pollingResult.changedInputs.get(inputAddress).intValue(), provider);
								}
							}

							if (pollingResult.changedVirtuals.size() != 0) {
								for (Integer virtualAddress : pollingResult.changedVirtuals.keySet()) {
									logger.debug("Virtual variation {} received, new value is {}", virtualAddress, pollingResult.changedVirtuals.get(virtualAddress));
									provider.setVirtualCachedValue(virtualAddress, pollingResult.changedVirtuals.get(virtualAddress).intValue());
									updateState(pnmasId, SappAddressType.VIRTUAL, virtualAddress, pollingResult.changedVirtuals.get(virtualAddress).intValue(), provider);
								}
							}
						} catch (SappException e) {
							logger.error("polling failed on pnmas {}", pnmas);
						}
					}
				}
			}
		}
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	protected void internalReceiveCommand(String itemName, Command command) {

		// the code being executed when a command was sent on the openHAB
		// event bus goes here. This method is only called if one of the
		// BindingProviders provide a binding for the given 'itemName'.
		logger.debug("internalReceiveCommand({},{}) is called!", itemName, command);

		executeSappCommand(itemName, command);
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	protected void internalReceiveUpdate(String itemName, State newState) {

		// the code being executed when a state was sent on the openHAB
		// event bus goes here. This method is only called if one of the
		// BindingProviders provide a binding for the given 'itemName'.
		logger.debug("internalReceiveUpdate({},{}) is called!", itemName, newState);
	}

	/**
	 * executes the real command on pnmas device
	 */
	private void executeSappCommand(String itemName, Command command) {

		SappBindingProvider provider = findFirstMatchingBindingProvider(itemName);
		if (provider == null) {
			logger.error("cannot find a provider, skipping command");
		}

		try {
			Item item = itemRegistry.getItem(itemName);
			logger.debug("found item {}", item);

			if (item instanceof SwitchItem && !(item instanceof DimmerItem)) {
				SappBindingConfigSwitchItem sappBindingConfigSwitchItem = (SappBindingConfigSwitchItem) provider.getBindingConfig(itemName);
				logger.debug("found binding {}", sappBindingConfigSwitchItem);

				if (sappBindingConfigSwitchItem.isPollerSuspender()) {
					if (pollingEnabled) { // turning off polling
						pollingEnabled = false;
						updatePollingSwitchesState(provider); // force updates of polling switches because polling is
																// off
					} else { // turning on polling
						provider.getSappUpdatePendingRequests().replaceAllPendingUpdateRequests(ALL_UPDATE_REQUEST_KEY);
						pollingEnabled = true;
					}
				} else {

					SappAddressOnOffControl controlAddress = sappBindingConfigSwitchItem.getControl();

					if (!provider.getPnmasMap().containsKey(controlAddress.getPnmasId())) {
						logger.error("bad pnmas id ({}) in binding ({}) ... skipping", controlAddress.getPnmasId(), sappBindingConfigSwitchItem);
						return;
					}

					try {
						if (command instanceof OnOffType) {
							switch (controlAddress.getAddressType()) {
							case VIRTUAL: {
								// mask bits on previous value
								int previousValue = getVirtualValue(provider, controlAddress.getPnmasId(), controlAddress.getAddress(), controlAddress.getSubAddress(), false);
								int newValue = SappBindingConfigUtils.maskWithSubAddressAndSet(controlAddress.getSubAddress(), command.equals(OnOffType.ON) ? controlAddress.getOnValue() : controlAddress.getOffValue(), previousValue);

								// update pnmas
								SappPnmas pnmas = provider.getPnmasMap().get(controlAddress.getPnmasId());
								SappCentralExecuter sappCentralExecuter = SappCentralExecuter.getInstance();
								sappCentralExecuter.executeSapp7DCommand(pnmas.getIp(), pnmas.getPort(), controlAddress.getAddress(), newValue);

								break;
							}

							default:
								logger.error("cannot run {} on type {}", command.getClass().getSimpleName(), controlAddress.getAddressType());
								break;
							}
						} else {
							logger.error("command {} not applicable", command.getClass().getSimpleName());
						}
					} catch (SappException e) {
						logger.error("could not run sappcommand", e);
					}
				}
			} else if (item instanceof NumberItem) {
				SappBindingConfigNumberItem sappBindingConfigNumberItem = (SappBindingConfigNumberItem) provider.getBindingConfig(itemName);
				logger.debug("found binding {}", sappBindingConfigNumberItem);

				SappAddressDecimal address = sappBindingConfigNumberItem.getStatus();

				if (!provider.getPnmasMap().containsKey(address.getPnmasId())) {
					logger.error("bad pnmas id ({}) in binding ({}) ... skipping", address.getPnmasId(), sappBindingConfigNumberItem);
					return;
				}

				try {
					if (command instanceof DecimalType) {
						switch (address.getAddressType()) {
						case VIRTUAL: {

							// mask bits on previous value
							int previousValue = getVirtualValue(provider, address.getPnmasId(), address.getAddress(), address.getSubAddress(), false);
							int newValue = SappBindingConfigUtils.maskWithSubAddressAndSet(address.getSubAddress(), address.backScaledValue(((DecimalType) command).toBigDecimal()), previousValue);

							// update pnmas
							SappPnmas pnmas = provider.getPnmasMap().get(address.getPnmasId());
							SappCentralExecuter sappCentralExecuter = SappCentralExecuter.getInstance();
							sappCentralExecuter.executeSapp7DCommand(pnmas.getIp(), pnmas.getPort(), address.getAddress(), newValue);

							break;
						}

						default:
							logger.error("cannot run {} on type {}", command.getClass().getSimpleName(), address.getAddressType());
							break;
						}
					} else {
						logger.error("command {} not applicable", command.getClass().getSimpleName());
					}
				} catch (SappException e) {
					logger.error("could not run sappcommand", e);
				}
			} else if (item instanceof RollershutterItem) {
				SappBindingConfigRollershutterItem sappBindingConfigRollershutterItem = (SappBindingConfigRollershutterItem) provider.getBindingConfig(itemName);
				logger.debug("found binding {}", sappBindingConfigRollershutterItem);

				SappAddressRollershutterControl controlAddress = null;
				if (command instanceof UpDownType && ((UpDownType) command) == UpDownType.UP) {
					controlAddress = sappBindingConfigRollershutterItem.getUpControl();
				} else if (command instanceof UpDownType && ((UpDownType) command) == UpDownType.DOWN) {
					controlAddress = sappBindingConfigRollershutterItem.getDownControl();
				} else if (command instanceof StopMoveType && ((StopMoveType) command) == StopMoveType.STOP) {
					controlAddress = sappBindingConfigRollershutterItem.getStopControl();
				}

				if (controlAddress != null) {
					if (!provider.getPnmasMap().containsKey(controlAddress.getPnmasId())) {
						logger.error("bad pnmas id ({}) in binding ({}) ... skipping", controlAddress.getPnmasId(), sappBindingConfigRollershutterItem);
						return;
					}

					try {
						switch (controlAddress.getAddressType()) {
						case VIRTUAL: {
							// mask bits on previous value
							int previousValue = getVirtualValue(provider, controlAddress.getPnmasId(), controlAddress.getAddress(), controlAddress.getSubAddress(), false);
							int newValue = SappBindingConfigUtils.maskWithSubAddressAndSet(controlAddress.getSubAddress(), controlAddress.getActivateValue(), previousValue);

							// update pnmas
							SappPnmas pnmas = provider.getPnmasMap().get(controlAddress.getPnmasId());
							SappCentralExecuter sappCentralExecuter = SappCentralExecuter.getInstance();
							sappCentralExecuter.executeSapp7DCommand(pnmas.getIp(), pnmas.getPort(), controlAddress.getAddress(), newValue);

							break;
						}

						default:
							logger.error("cannot run {} on type {}", command.getClass().getSimpleName(), controlAddress.getAddressType());
							break;
						}
					} catch (SappException e) {
						logger.error("could not run sappcommand", e);
					}
				} else {
					logger.error("command {} not applicable", command.getClass().getSimpleName());
				}
			} else if (item instanceof DimmerItem) {
				SappBindingConfigDimmerItem sappBindingConfigDimmerItem = (SappBindingConfigDimmerItem) provider.getBindingConfig(itemName);
				logger.debug("found binding {}", sappBindingConfigDimmerItem);

				SappAddressDimmer address = sappBindingConfigDimmerItem.getStatus();

				if (!provider.getPnmasMap().containsKey(address.getPnmasId())) {
					logger.error("bad pnmas id ({}) in binding ({}) ... skipping", address.getPnmasId(), sappBindingConfigDimmerItem);
					return;
				}

				try {
					if (command instanceof OnOffType) {
						switch (address.getAddressType()) {
						case VIRTUAL: {

							// mask bits on previous value
							int previousValue = getVirtualValue(provider, address.getPnmasId(), address.getAddress(), address.getSubAddress(), false);
							int newValue = SappBindingConfigUtils.maskWithSubAddressAndSet(address.getSubAddress(), ((OnOffType) command) == OnOffType.ON ? address.getOriginalMaxScale() : address.getOriginalMinScale(), previousValue);

							// update pnmas
							SappPnmas pnmas = provider.getPnmasMap().get(address.getPnmasId());
							SappCentralExecuter sappCentralExecuter = SappCentralExecuter.getInstance();
							sappCentralExecuter.executeSapp7DCommand(pnmas.getIp(), pnmas.getPort(), address.getAddress(), newValue);

							break;
						}

						default:
							logger.error("cannot run {} on type {}", command.getClass().getSimpleName(), address.getAddressType());
							break;
						}
					} else if (command instanceof IncreaseDecreaseType) {
						switch (address.getAddressType()) {
						case VIRTUAL: {

							// mask bits on previous value
							int previousValue = getVirtualValue(provider, address.getPnmasId(), address.getAddress(), address.getSubAddress(), false);
							int newValue = SappBindingConfigUtils.maskWithSubAddressAndSet(address.getSubAddress(), ((IncreaseDecreaseType) command) == IncreaseDecreaseType.INCREASE ? Math.min(previousValue + address.getIncrement(), address.getOriginalMaxScale()) : Math.max(previousValue - address.getIncrement(), address.getOriginalMinScale()), previousValue);

							// update pnmas
							SappPnmas pnmas = provider.getPnmasMap().get(address.getPnmasId());
							SappCentralExecuter sappCentralExecuter = SappCentralExecuter.getInstance();
							sappCentralExecuter.executeSapp7DCommand(pnmas.getIp(), pnmas.getPort(), address.getAddress(), newValue);

							break;
						}

						default:
							logger.error("cannot run {} on type {}", command.getClass().getSimpleName(), address.getAddressType());
							break;
						}
					} else if (command instanceof PercentType) {
						switch (address.getAddressType()) {
						case VIRTUAL: {

							// mask bits on previous value
							int previousValue = getVirtualValue(provider, address.getPnmasId(), address.getAddress(), address.getSubAddress(), false);
							int newValue = SappBindingConfigUtils.maskWithSubAddressAndSet(address.getSubAddress(), address.backScaledValue(((PercentType) command).toBigDecimal()), previousValue);

							// update pnmas
							SappPnmas pnmas = provider.getPnmasMap().get(address.getPnmasId());
							SappCentralExecuter sappCentralExecuter = SappCentralExecuter.getInstance();
							sappCentralExecuter.executeSapp7DCommand(pnmas.getIp(), pnmas.getPort(), address.getAddress(), newValue);

							break;
						}

						default:
							logger.error("cannot run {} on type {}", command.getClass().getSimpleName(), address.getAddressType());
							break;
						}
					} else {
						logger.error("command {} not applicable", command.getClass().getSimpleName());
					}
				} catch (SappException e) {
					logger.error("could not run sappcommand", e);
				}
			} else {
				logger.error("unimplemented item type: {}", item.getClass().getSimpleName());
			}
		} catch (ItemNotFoundException e) {
			logger.error("Item {} not found", itemName);
		}
	}

	/**
	 * Find the first matching {@link ChannelBindingProvider} according to <code>itemName</code>
	 * 
	 * @param itemName
	 * 
	 * @return the matching binding provider or <code>null</code> if no binding provider could be found
	 */

	protected SappBindingProvider findFirstMatchingBindingProvider(String itemName) {

		for (SappBindingProvider provider : providers) {
			logger.debug("found provider: {}", provider.getClass());
			if (!provider.providesBindingFor(itemName)) {
				continue;
			}
			return provider;
		}

		return null;
	}

	/**
	 * initializes all items
	 */
	private void initializeAllItemsInProvider(SappBindingProvider provider, Set<String> toBeProcessed) {

		logger.debug("Updating item state for items {}", provider.getItemNames());
		for (String itemName : provider.getItemNames()) {
			if (toBeProcessed.contains(ALL_UPDATE_REQUEST_KEY) || toBeProcessed.contains(itemName)) {
				queryAndSendActualState(provider, itemName);
			}
		}

		updatePollingSwitchesState(provider);
	}

	/**
	 * reads state from device and updates item repository
	 */
	private void queryAndSendActualState(SappBindingProvider provider, String itemName) {

		logger.debug("querying and sending item {}", itemName);

		try {
			Item item = itemRegistry.getItem(itemName);

			if (item instanceof SwitchItem && !(item instanceof DimmerItem)) {
				SappBindingConfigSwitchItem sappBindingConfigSwitchItem = (SappBindingConfigSwitchItem) provider.getBindingConfig(itemName);

				SappAddressOnOffStatus statusAddress = sappBindingConfigSwitchItem.getStatus();

				if (!sappBindingConfigSwitchItem.isPollerSuspender()) {
					updateOnOffItem(provider, statusAddress, itemName, item);
				}
			} else if (item instanceof ContactItem) {
				SappBindingConfigContactItem sappBindingConfigContactItem = (SappBindingConfigContactItem) provider.getBindingConfig(itemName);

				SappAddressOpenClosedStatus statusAddress = sappBindingConfigContactItem.getStatus();

				updateOpenClosedItem(provider, statusAddress, itemName, item);
			} else if (item instanceof NumberItem) {
				SappBindingConfigNumberItem sappBindingConfigNumberItem = (SappBindingConfigNumberItem) provider.getBindingConfig(itemName);

				SappAddressDecimal statusAddress = sappBindingConfigNumberItem.getStatus();

				updateDecimalItem(provider, statusAddress, itemName, item);
			} else if (item instanceof RollershutterItem) {
				SappBindingConfigRollershutterItem sappBindingConfigRollershutterItem = (SappBindingConfigRollershutterItem) provider.getBindingConfig(itemName);

				SappAddressRollershutterStatus statusAddress = sappBindingConfigRollershutterItem.getStatus();

				updateRollershutterItem(provider, statusAddress, itemName, item);
			} else if (item instanceof DimmerItem) {
				SappBindingConfigDimmerItem sappBindingConfigDimmerItem = (SappBindingConfigDimmerItem) provider.getBindingConfig(itemName);

				SappAddressDimmer statusAddress = sappBindingConfigDimmerItem.getStatus();

				updateDimmerItem(provider, statusAddress, itemName, item);
			} else {
				logger.error("unimplemented item type: {}", item.getClass().getSimpleName());
			}
		} catch (ItemNotFoundException e) {
			logger.error("Item {} not found", itemName);

		}
	}

	/**
	 * loads Sapp provider
	 */
	private SappBindingProvider getFirstSappBindingProvider() {
		for (SappBindingProvider provider : providers) {
			return provider;
		}
		return null;
	}

	/**
	 * updates item repository for a single item
	 */
	private void updateState(String pnmasId, SappAddressType sappAddressType, int addressToUpdate, int newState, SappBindingProvider provider) {
		logger.debug("Updating {} {} with new value {}", sappAddressType, addressToUpdate, newState);
		for (String itemName : provider.getItemNames()) {
			try {
				Item item = itemRegistry.getItem(itemName);

				if (item instanceof SwitchItem && !(item instanceof DimmerItem)) {
					SappBindingConfigSwitchItem sappBindingConfigSwitchItem = (SappBindingConfigSwitchItem) provider.getBindingConfig(itemName);
					if (!sappBindingConfigSwitchItem.isPollerSuspender()) {
						SappAddressOnOffStatus statusAddress = sappBindingConfigSwitchItem.getStatus();
						if (statusAddress.getAddressType() == sappAddressType && statusAddress.getPnmasId().equals(pnmasId) && addressToUpdate == statusAddress.getAddress()) {
							logger.debug("found binding to update {}", sappBindingConfigSwitchItem);
							int result = SappBindingConfigUtils.maskWithSubAddress(statusAddress.getSubAddress(), newState);
							State stateToSet = result == statusAddress.getOnValue() ? OnOffType.ON : OnOffType.OFF;
							if (!stateToSet.equals(item.getState())) {
								eventPublisher.postUpdate(itemName, stateToSet);
							}
						}
					}
				} else if (item instanceof ContactItem) {
					SappBindingConfigContactItem sappBindingConfigContactItem = (SappBindingConfigContactItem) provider.getBindingConfig(itemName);
					SappAddressOpenClosedStatus statusAddress = sappBindingConfigContactItem.getStatus();
					if (statusAddress.getAddressType() == sappAddressType && statusAddress.getPnmasId().equals(pnmasId) && addressToUpdate == statusAddress.getAddress()) {
						logger.debug("found binding to update {}", sappBindingConfigContactItem);
						int result = SappBindingConfigUtils.maskWithSubAddress(statusAddress.getSubAddress(), newState);
						State stateToSet = result == statusAddress.getOpenValue() ? OpenClosedType.OPEN : OpenClosedType.CLOSED;
						if (!stateToSet.equals(item.getState())) {
							eventPublisher.postUpdate(itemName, stateToSet);
						}
					}
				} else if (item instanceof NumberItem) {
					SappBindingConfigNumberItem sappBindingConfigNumberItem = (SappBindingConfigNumberItem) provider.getBindingConfig(itemName);
					SappAddressDecimal address = sappBindingConfigNumberItem.getStatus();
					if (address.getAddressType() == sappAddressType && address.getPnmasId().equals(pnmasId) && addressToUpdate == address.getAddress()) {
						logger.debug("found binding to update {}", sappBindingConfigNumberItem);
						int result = SappBindingConfigUtils.maskWithSubAddress(address.getSubAddress(), newState);
						State stateToSet = new DecimalType(address.scaledValue(result, address.getSubAddress()));
						if (!stateToSet.equals(item.getState())) {
							eventPublisher.postUpdate(itemName, stateToSet);
						}
					}
				} else if (item instanceof RollershutterItem) {
					SappBindingConfigRollershutterItem sappBindingConfigRollershutterItem = (SappBindingConfigRollershutterItem) provider.getBindingConfig(itemName);
					SappAddressRollershutterStatus statusAddress = sappBindingConfigRollershutterItem.getStatus();
					if (statusAddress.getAddressType() == sappAddressType && statusAddress.getPnmasId().equals(pnmasId) && addressToUpdate == statusAddress.getAddress()) {
						logger.debug("found binding to update {}", sappBindingConfigRollershutterItem);
						int result = SappBindingConfigUtils.maskWithSubAddress(statusAddress.getSubAddress(), newState);
						State stateToSet = result == statusAddress.getOpenValue() ? PercentType.HUNDRED : (result == statusAddress.getClosedValue() ? PercentType.ZERO : PercentType.valueOf("50"));
						if (!stateToSet.equals(item.getState())) {
							eventPublisher.postUpdate(itemName, stateToSet);
						}
					}
				} else if (item instanceof DimmerItem) {
					SappBindingConfigDimmerItem sappBindingConfigDimmerItem = (SappBindingConfigDimmerItem) provider.getBindingConfig(itemName);
					SappAddressDimmer statusAddress = sappBindingConfigDimmerItem.getStatus();
					if (statusAddress.getAddressType() == sappAddressType && statusAddress.getPnmasId().equals(pnmasId) && addressToUpdate == statusAddress.getAddress()) {
						logger.debug("found binding to update {}", sappBindingConfigDimmerItem);
						int result = statusAddress.scaledValue(SappBindingConfigUtils.maskWithSubAddress(statusAddress.getSubAddress(), newState), statusAddress.getSubAddress()).round(new MathContext(0, RoundingMode.HALF_EVEN)).intValue();
						State stateToSet;
						if (result <= PercentType.ZERO.intValue()) {
							stateToSet = PercentType.ZERO;
						} else if (result >= PercentType.HUNDRED.intValue()) {
							stateToSet = PercentType.HUNDRED;
						} else {
							stateToSet = PercentType.valueOf(String.valueOf(result));
						}
						if (!stateToSet.equals(item.getState())) {
							eventPublisher.postUpdate(itemName, stateToSet);
						}
					}
				} else {
					logger.error("unimplemented item type: {}", item.getClass().getSimpleName());
				}
			} catch (ItemNotFoundException e) {
				logger.error("Item {} not found", itemName);
			}
		}
	}

	/**
	 * updates item repository for special polling switches
	 */
	private void updatePollingSwitchesState(SappBindingProvider provider) {
		logger.debug("Updating poller switch states");
		for (String itemName : provider.getItemNames()) {
			try {
				Item item = itemRegistry.getItem(itemName);

				if (item instanceof SwitchItem && !(item instanceof DimmerItem)) {
					SappBindingConfigSwitchItem sappBindingConfigSwitchItem = (SappBindingConfigSwitchItem) provider.getBindingConfig(itemName);
					if (sappBindingConfigSwitchItem.isPollerSuspender()) {
						eventPublisher.postUpdate(itemName, pollingEnabled ? OnOffType.ON : OnOffType.OFF);
					}
				}
			} catch (ItemNotFoundException e) {
				logger.error("Item {} not found", itemName);
			}
		}
	}

	/**
	 * updates item repository for OnOff items
	 */
	private void updateOnOffItem(SappBindingProvider provider, SappAddressOnOffStatus statusAddress, String itemName, Item item) {

		switch (statusAddress.getAddressType()) {
		case VIRTUAL:
			try {
				int result = SappBindingConfigUtils.maskWithSubAddress(statusAddress.getSubAddress(), getVirtualValue(provider, statusAddress.getPnmasId(), statusAddress.getAddress(), statusAddress.getSubAddress(), true));
				eventPublisher.postUpdate(itemName, result == statusAddress.getOnValue() ? OnOffType.ON : OnOffType.OFF);
			} catch (SappException e) {
				logger.error("could not run sappcommand", e);
			}
			break;

		case INPUT:
			try {
				int result = SappBindingConfigUtils.maskWithSubAddress(statusAddress.getSubAddress(), getInputValue(provider, statusAddress.getPnmasId(), statusAddress.getAddress(), statusAddress.getSubAddress(), true));
				eventPublisher.postUpdate(itemName, result == statusAddress.getOnValue() ? OnOffType.ON : OnOffType.OFF);
			} catch (SappException e) {
				logger.error("could not run sappcommand", e);
			}
			break;

		case OUTPUT:
			try {
				int result = SappBindingConfigUtils.maskWithSubAddress(statusAddress.getSubAddress(), getOutputValue(provider, statusAddress.getPnmasId(), statusAddress.getAddress(), statusAddress.getSubAddress(), true));
				eventPublisher.postUpdate(itemName, result == statusAddress.getOnValue() ? OnOffType.ON : OnOffType.OFF);
			} catch (SappException e) {
				logger.error("could not run sappcommand", e);
			}
			break;

		default:
			logger.error("item type not yet implemented {} for address type {}", item.getClass().getSimpleName(), statusAddress.getAddressType());
			break;
		}
	}

	/**
	 * updates item repository for OpenClosed items
	 */
	private void updateOpenClosedItem(SappBindingProvider provider, SappAddressOpenClosedStatus statusAddress, String itemName, Item item) {

		switch (statusAddress.getAddressType()) {
		case VIRTUAL:
			try {
				int result = SappBindingConfigUtils.maskWithSubAddress(statusAddress.getSubAddress(), getVirtualValue(provider, statusAddress.getPnmasId(), statusAddress.getAddress(), statusAddress.getSubAddress(), true));
				eventPublisher.postUpdate(itemName, result == statusAddress.getOpenValue() ? OpenClosedType.OPEN : OpenClosedType.CLOSED);
			} catch (SappException e) {
				logger.error("could not run sappcommand", e);
			}
			break;

		case INPUT:
			try {
				int result = SappBindingConfigUtils.maskWithSubAddress(statusAddress.getSubAddress(), getInputValue(provider, statusAddress.getPnmasId(), statusAddress.getAddress(), statusAddress.getSubAddress(), true));
				eventPublisher.postUpdate(itemName, result == statusAddress.getOpenValue() ? OpenClosedType.OPEN : OpenClosedType.CLOSED);
			} catch (SappException e) {
				logger.error("could not run sappcommand", e);
			}
			break;

		case OUTPUT:
			try {
				int result = SappBindingConfigUtils.maskWithSubAddress(statusAddress.getSubAddress(), getOutputValue(provider, statusAddress.getPnmasId(), statusAddress.getAddress(), statusAddress.getSubAddress(), true));
				eventPublisher.postUpdate(itemName, result == statusAddress.getOpenValue() ? OpenClosedType.OPEN : OpenClosedType.CLOSED);
			} catch (SappException e) {
				logger.error("could not run sappcommand", e);
			}
			break;

		default:
			logger.error("item type not yet implemented {} for address type {}", item.getClass().getSimpleName(), statusAddress.getAddressType());
			break;
		}
	}

	/**
	 * updates item repository for Decimal items
	 */
	private void updateDecimalItem(SappBindingProvider provider, SappAddressDecimal address, String itemName, Item item) {

		switch (address.getAddressType()) {
		case VIRTUAL:
			try {
				int result = SappBindingConfigUtils.maskWithSubAddress(address.getSubAddress(), getVirtualValue(provider, address.getPnmasId(), address.getAddress(), address.getSubAddress(), true));
				eventPublisher.postUpdate(itemName, new DecimalType(address.scaledValue(result, address.getSubAddress())));
			} catch (SappException e) {
				logger.error("could not run sappcommand", e);
			}
			break;

		case INPUT:
			try {
				int result = SappBindingConfigUtils.maskWithSubAddress(address.getSubAddress(), getInputValue(provider, address.getPnmasId(), address.getAddress(), address.getSubAddress(), true));
				eventPublisher.postUpdate(itemName, new DecimalType(address.scaledValue(result, address.getSubAddress())));
			} catch (SappException e) {
				logger.error("could not run sappcommand", e);
			}
			break;

		case OUTPUT:
			try {
				int result = SappBindingConfigUtils.maskWithSubAddress(address.getSubAddress(), getOutputValue(provider, address.getPnmasId(), address.getAddress(), address.getSubAddress(), true));
				eventPublisher.postUpdate(itemName, new DecimalType(address.scaledValue(result, address.getSubAddress())));
			} catch (SappException e) {
				logger.error("could not run sappcommand: " + e.getMessage());
			}
			break;

		default:
			logger.error("item type not yet implemented {} for address type {}", item.getClass().getSimpleName(), address.getAddressType());
			break;
		}
	}

	/**
	 * updates item repository for Rollershutter items
	 */
	private void updateRollershutterItem(SappBindingProvider provider, SappAddressRollershutterStatus statusAddress, String itemName, Item item) {

		switch (statusAddress.getAddressType()) {
		case VIRTUAL:
			try {
				int result = SappBindingConfigUtils.maskWithSubAddress(statusAddress.getSubAddress(), getVirtualValue(provider, statusAddress.getPnmasId(), statusAddress.getAddress(), statusAddress.getSubAddress(), true));
				eventPublisher.postUpdate(itemName, result == statusAddress.getOpenValue() ? PercentType.HUNDRED : (result == statusAddress.getClosedValue() ? PercentType.ZERO : PercentType.valueOf("50")));
			} catch (SappException e) {
				logger.error("could not run sappcommand", e);
			}
			break;

		case INPUT:
			logger.error("item type not yet implemented {} for address type {}", item.getClass().getSimpleName(), statusAddress.getAddressType());
			break;

		case OUTPUT:
			logger.error("item type not yet implemented {} for address type {}", item.getClass().getSimpleName(), statusAddress.getAddressType());
			break;

		default:
			logger.error("item type not yet implemented {} for address type {}", item.getClass().getSimpleName(), statusAddress.getAddressType());
			break;
		}
	}

	/**
	 * updates item repository for Dimmer items
	 */
	private void updateDimmerItem(SappBindingProvider provider, SappAddressDimmer statusAddress, String itemName, Item item) {

		switch (statusAddress.getAddressType()) {
		case VIRTUAL:
			try {
				int result = statusAddress.scaledValue(SappBindingConfigUtils.maskWithSubAddress(statusAddress.getSubAddress(), getVirtualValue(provider, statusAddress.getPnmasId(), statusAddress.getAddress(), statusAddress.getSubAddress(), true)), statusAddress.getSubAddress()).round(new MathContext(0, RoundingMode.HALF_EVEN)).intValue();
				if (result <= PercentType.ZERO.intValue()) {
					eventPublisher.postUpdate(itemName, PercentType.ZERO);
				} else if (result >= PercentType.HUNDRED.intValue()) {
					eventPublisher.postUpdate(itemName, PercentType.HUNDRED);
				} else {
					eventPublisher.postUpdate(itemName, PercentType.valueOf(String.valueOf(result)));
				}
			} catch (SappException e) {
				logger.error("could not run sappcommand", e);
			}
			break;

		case INPUT:
			logger.error("item type not yet implemented {} for address type {}", item.getClass().getSimpleName(), statusAddress.getAddressType());
			break;

		case OUTPUT:
			logger.error("item type not yet implemented {} for address type {}", item.getClass().getSimpleName(), statusAddress.getAddressType());
			break;

		default:
			logger.error("item type not yet implemented {} for address type {}", item.getClass().getSimpleName(), statusAddress.getAddressType());
			break;
		}
	}

	/**
	 * load virtual value from pnmas, if not cached, and caches it
	 */
	private int getVirtualValue(SappBindingProvider provider, String pnmasId, int address, String subAddress, boolean forceReload) throws SappException {

		Integer value = provider.getVirtualCachedValue(address);
		if (forceReload || value == null) {
			logger.debug("cached value missing, reloading for [{} {} {}]", pnmasId, address, subAddress);
			SappPnmas pnmas = provider.getPnmasMap().get(pnmasId);

			SappCentralExecuter sappCentralExecuter = SappCentralExecuter.getInstance();
			value = sappCentralExecuter.executeSapp7CCommand(pnmas.getIp(), pnmas.getPort(), address);
			provider.setVirtualCachedValue(address, value);
		}

		return value.intValue();
	}

	/**
	 * load input value from pnmas, if not cached, and caches it
	 */
	private int getInputValue(SappBindingProvider provider, String pnmasId, int address, String subAddress, boolean forceReload) throws SappException {

		Integer value = provider.getInputCachedValue(address);
		if (forceReload || value == null) {
			logger.debug("cached value missing, reloading for [{} {} {}]", pnmasId, address, subAddress);
			SappPnmas pnmas = provider.getPnmasMap().get(pnmasId);

			SappCentralExecuter sappCentralExecuter = SappCentralExecuter.getInstance();
			value = sappCentralExecuter.executeSapp74Command(pnmas.getIp(), pnmas.getPort(), (byte) address);
			provider.setInputCachedValue(address, value);
		}
		return value.intValue();
	}

	/**
	 * load output value from pnmas, if not cached, and caches it
	 */
	private int getOutputValue(SappBindingProvider provider, String pnmasId, int address, String subAddress, boolean forceReload) throws SappException {

		Integer value = provider.getOutputCachedValue(address);
		if (forceReload || value == null) {
			logger.debug("cached value missing, reloading for [{} {} {}]", pnmasId, address, subAddress);
			SappPnmas pnmas = provider.getPnmasMap().get(pnmasId);

			SappCentralExecuter sappCentralExecuter = SappCentralExecuter.getInstance();
			value = sappCentralExecuter.executeSapp75Command(pnmas.getIp(), pnmas.getPort(), (byte) address);
			provider.setOutputCachedValue(address, value);
		}
		return value.intValue();
	}

	@Override
	public void allItemsChanged(Collection<String> oldItemNames) {
		SappBindingProvider provider = getFirstSappBindingProvider();
		provider.getSappUpdatePendingRequests().replaceAllPendingUpdateRequests(ALL_UPDATE_REQUEST_KEY);
	}

	@Override
	public void itemAdded(Item item) {
	}

	@Override
	public void itemRemoved(Item item) {
	}
}
