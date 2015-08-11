/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.sapp.internal;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.openhab.binding.sapp.SappBindingProvider;
import org.openhab.binding.sapp.internal.configs.SappBindingConfigContactItem;
import org.openhab.binding.sapp.internal.configs.SappBindingConfigNumberItem;
import org.openhab.binding.sapp.internal.configs.SappBindingConfigSwitchItem;
import org.openhab.binding.sapp.internal.configs.SappBindingConfigUtils;
import org.openhab.binding.sapp.internal.executer.SappCentralExecuter;
import org.openhab.binding.sapp.internal.executer.SappCentralExecuter.PollingResult;
import org.openhab.binding.sapp.internal.model.SappAddressDecimal;
import org.openhab.binding.sapp.internal.model.SappAddressOnOffControl;
import org.openhab.binding.sapp.internal.model.SappAddressOnOffStatus;
import org.openhab.binding.sapp.internal.model.SappAddressOpenClosedStatus;
import org.openhab.binding.sapp.internal.model.SappAddressType;
import org.openhab.binding.sapp.internal.model.SappPnmas;
import org.openhab.core.binding.AbstractActiveBinding;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.ContactItem;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.OpenClosedType;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.openhab.core.types.UnDefType;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.paolodenti.jsapp.core.command.base.SappException;
import com.github.paolodenti.jsapp.core.util.SappUtils;

/**
 * Implement this class if you are going create an actively polling service like
 * querying a Website/Device.
 * 
 * @author Paolo Denti
 * @since 1.8.0
 */
public class SappBinding extends AbstractActiveBinding<SappBindingProvider> {

	private static final Logger logger = LoggerFactory.getLogger(SappBinding.class);

	private static final String CONFIG_KEY_REFRESH = "refresh";
	private static final String CONFIG_KEY_PNMAS_ENABLED = "pnmas.ids";
	private static final String CONFIG_KEY_PNMAS_ID = "pnmas.%s.ip";
	private static final String CONFIG_KEY_PNMAS_PORT = "pnmas.%s.port";

	/**
	 * The BundleContext. This is only valid when the bundle is ACTIVE. It is
	 * set in the activate() method and must not be accessed anymore once the
	 * deactivate() method was called or before activate() was called.
	 */
	@SuppressWarnings("unused")
	private BundleContext bundleContext;

	/**
	 * the refresh interval which is used to poll values from the Sapp server
	 * (optional, defaults to 100ms)
	 */
	private long refreshInterval = 100;

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

		logger.debug("sapp activate called");
		this.bundleContext = bundleContext;

		// the configuration is guaranteed not to be null, because the component
		// definition has the
		// configuration-policy set to require. If set to 'optional' then the
		// configuration may be null

		// to override the default refresh interval one has to add a
		// parameter to openhab.cfg like <bindingName>:refresh=<intervalInMs>
		String refreshIntervalString = (String) configuration.get(CONFIG_KEY_REFRESH);
		if (StringUtils.isNotBlank(refreshIntervalString)) {
			refreshInterval = Long.parseLong(refreshIntervalString);
			logger.debug("set refresh interval: " + refreshInterval);
		}

		SappBindingProvider provider = getFirstSappBindingProvider();
		if (provider != null) {
			String pnmasEnabled = (String) configuration.get(CONFIG_KEY_PNMAS_ENABLED);
			if (pnmasEnabled != null) {
				String[] pnmasIds = pnmasEnabled.split(",");
				for (String pnmasId : pnmasIds) {
					logger.debug(String.format("loading info for pnmas %s", pnmasId));

					String ip = (String) configuration.get(String.format(CONFIG_KEY_PNMAS_ID, pnmasId));
					if (ip == null) {
						logger.warn(String.format("ip not found for pnmas %s", pnmasId));
						continue;
					}

					int port;
					String portString = (String) configuration.get(String.format(CONFIG_KEY_PNMAS_PORT, pnmasId));
					if (portString == null) {
						logger.warn(String.format("port not found for pnmas %s", pnmasId));
						continue;
					} else {
						try {
							port = Integer.parseInt(portString);
						} catch (NumberFormatException e) {
							logger.warn(String.format("bad port number for pnmas %s", pnmasId));
							continue;
						}
					}

					if (provider.getPnmasMap().containsKey(pnmasId)) {
						logger.warn(String.format("pnmas %s duplicated, skipping", pnmasId));
						continue;
					}

					provider.getPnmasMap().put(pnmasId, new SappPnmas(ip, port));
				}
				for (String pnmasKey : provider.getPnmasMap().keySet()) {
					logger.debug(String.format("pnmas %s : %s:", pnmasKey, provider.getPnmasMap().get(pnmasKey)));
				}
			}
		}

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
		logger.debug("modified called");
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

		logger.debug("sapp deactivate called");
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

		return "Sapp Refresh Service";
	}

	/**
	 * @{inheritDoc}
	 */
	@Override
	protected void execute() {

		if (isProperlyConfigured()) { // wait until provider is properly configured
			SappBindingProvider provider = getFirstSappBindingProvider();
			if (provider != null) {
				if (provider.isFullRefreshNeeded()) { // if items are in uninitialized state
					logger.debug("executing a full refresh");
					try {
						initializeAllItemsInProvider(provider);
						provider.setFullRefreshNeeded(false);
					} catch (SappException e) {
						logger.error("error while initializing items:" + e.getMessage());
					}
				} else { // poll
					SappCentralExecuter sappCentralExecuter = SappCentralExecuter.getInstance();

					for (String pnmasId : provider.getPnmasMap().keySet()) { // each pnmas
						SappPnmas pnmas = provider.getPnmasMap().get(pnmasId);
						try {
							PollingResult pollingResult = sappCentralExecuter.executePollingSappCommands(pnmas.getIp(), pnmas.getPort());

							if (pollingResult.changedOutputs.size() != 0) {
								for (Byte outputAddress : pollingResult.changedOutputs.keySet()) {
									logger.debug(String.format("Output variation %d received, new value is %d", SappUtils.byteToUnsigned(outputAddress), pollingResult.changedOutputs.get(outputAddress)));
									if (! pollingResult.changedOutputs.get(outputAddress).equals(provider.getOutputCachedValue(SappUtils.byteToUnsigned(outputAddress)))) {
										// different value, save & update state
										logger.debug(String.format("Output %d changed, new value is %d", SappUtils.byteToUnsigned(outputAddress), pollingResult.changedOutputs.get(outputAddress)));
										provider.setOutputCachedValue(SappUtils.byteToUnsigned(outputAddress), pollingResult.changedOutputs.get(outputAddress).intValue());
										updateState(pnmasId, SappAddressType.OUTPUT, SappUtils.byteToUnsigned(outputAddress), pollingResult.changedOutputs.get(outputAddress).intValue(), provider);
									}
								}
							}

							if (pollingResult.changedInputs.size() != 0) {
								for (Byte inputAddress : pollingResult.changedInputs.keySet()) {
									logger.debug(String.format("Input variation %d received, new value is %d", SappUtils.byteToUnsigned(inputAddress), pollingResult.changedInputs.get(inputAddress)));
									if (! pollingResult.changedInputs.get(inputAddress).equals(provider.getInputCachedValue(SappUtils.byteToUnsigned(inputAddress)))) {
										// different value, save & update state
										logger.debug(String.format("Input %d changed, new value is %d", SappUtils.byteToUnsigned(inputAddress), pollingResult.changedInputs.get(inputAddress)));
										provider.setInputCachedValue(SappUtils.byteToUnsigned(inputAddress), pollingResult.changedInputs.get(inputAddress).intValue());
										updateState(pnmasId, SappAddressType.INPUT, SappUtils.byteToUnsigned(inputAddress), pollingResult.changedInputs.get(inputAddress).intValue(), provider);
									}
								}
							}

							if (pollingResult.changedVirtuals.size() != 0) {
								for (Integer virtualAddress : pollingResult.changedVirtuals.keySet()) {
									logger.debug(String.format("Virtual variation %d received, new value is %d", virtualAddress, pollingResult.changedVirtuals.get(virtualAddress)));
									if (! pollingResult.changedVirtuals.get(virtualAddress).equals(provider.getVirtualCachedValue(virtualAddress))) {
										// different value, save & update state
										logger.debug(String.format("Virtual %d changed, new value is %d", virtualAddress, pollingResult.changedVirtuals.get(virtualAddress)));
										provider.setVirtualCachedValue(virtualAddress, pollingResult.changedVirtuals.get(virtualAddress).intValue());
										updateState(pnmasId, SappAddressType.VIRTUAL, virtualAddress, pollingResult.changedVirtuals.get(virtualAddress).intValue(), provider);
									}
								}
							}
						} catch (SappException e) {
							logger.error("polling failed on pnmas " + pnmas);
						}
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

		// the code being executed when a command was sent on the openHAB
		// event bus goes here. This method is only called if one of the
		// BindingProviders provide a binding for the given 'itemName'.
		logger.debug("internalReceiveCommand({},{}) is called!", itemName, command);

		executeSappCommand(itemName, command);
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

	/**
	 * executes the real command on pnmas device
	 */
	private void executeSappCommand(String itemName, Command command) {

		SappBindingProvider provider = findFirstMatchingBindingProvider(itemName);
		if (provider == null) {
			logger.error("cannot find a provider, skipping command");
		}

		Item item = provider.getItem(itemName);
		logger.debug("found item " + item);

		if (item instanceof SwitchItem) {
			SappBindingConfigSwitchItem sappBindingConfigSwitchItem = (SappBindingConfigSwitchItem) provider.getBindingConfig(itemName);
			logger.debug("found binding " + sappBindingConfigSwitchItem);

			SappAddressOnOffControl controlAddress = sappBindingConfigSwitchItem.getControl();

			if (!provider.getPnmasMap().containsKey(controlAddress.getPnmasId())) {
				logger.error(String.format("bad pnmas id (%s) in binding (%s) ... skipping", controlAddress.getPnmasId(), sappBindingConfigSwitchItem));
				return;
			}

			try {
				if (command instanceof OnOffType) {
					switch (controlAddress.getAddressType()) {
					case VIRTUAL: {
						// mask bits on previous value
						int previousValue = getVirtualValue(provider, controlAddress.getPnmasId(), controlAddress.getAddress(), controlAddress.getSubAddress());
						int newValue = SappBindingConfigUtils.maskWithSubAddressAndSet(controlAddress.getSubAddress(), command.equals(OnOffType.ON) ? controlAddress.getOnValue() : controlAddress.getOffValue(), previousValue);
						
						// update pnmas
						SappPnmas pnmas = provider.getPnmasMap().get(controlAddress.getPnmasId());
						SappCentralExecuter sappCentralExecuter = SappCentralExecuter.getInstance();
						sappCentralExecuter.executeSapp7DCommand(pnmas.getIp(), pnmas.getPort(), controlAddress.getAddress(), newValue);
						
						// update immediately changed value, without waiting for polling
						provider.setVirtualCachedValue(controlAddress.getAddress(), newValue);
						updateState(controlAddress.getPnmasId(), SappAddressType.VIRTUAL, controlAddress.getAddress(), newValue, provider);

						break;
					}

					default:
						logger.error("cannot run " + command.getClass().getSimpleName() + " on " + controlAddress.getAddressType() + " type");
						break;
					}
				} else {
					logger.error("command " + command.getClass().getSimpleName() + " not applicable");
				}
			} catch (SappException e) {
				logger.error("could not run sappcommand: " + e.getMessage());
			}
		} else {
			logger.error("unimplemented item type: " + item.getClass().getSimpleName());
		}
	}

	/**
	 * Find the first matching {@link ChannelBindingProvider} according to
	 * <code>itemName</code>
	 * 
	 * @param itemName
	 * 
	 * @return the matching binding provider or <code>null</code> if no binding
	 *         provider could be found
	 */

	protected SappBindingProvider findFirstMatchingBindingProvider(String itemName) {

		for (SappBindingProvider provider : providers) {
			logger.debug("found provider: " + provider.getClass());
			if (!provider.providesBindingFor(itemName)) {
				continue;
			}
			return provider;
		}

		return null;
	}

	private void initializeAllItemsInProvider(SappBindingProvider provider) throws SappException {

		logger.debug("Updating item state for items {}", provider.getItemNames());
		for (String itemName : provider.getItemNames()) {
			logger.debug("checking for querying and setting item " + itemName);
			State actualState = provider.getItem(itemName).getState();
			logger.debug("current state for " + itemName + " is " + provider.getItem(itemName).getState().getClass().getName());
			if (actualState instanceof UnDefType) { // item just added, refresh
				logger.debug("refresh needed: querying and setting item " + itemName);
				queryAndSendActualState(provider, itemName);
			}
		}
	}

	private void queryAndSendActualState(SappBindingProvider provider, String itemName) {

		Item item = provider.getItem(itemName);

		if (item instanceof SwitchItem) {
			SappBindingConfigSwitchItem sappBindingConfigSwitchItem = (SappBindingConfigSwitchItem) provider.getBindingConfig(itemName);

			SappAddressOnOffStatus statusAddress = sappBindingConfigSwitchItem.getStatus();

			updateOnOffItem(provider, statusAddress, itemName, item);
		} else if (item instanceof ContactItem) {
			SappBindingConfigContactItem sappBindingConfigContactItem = (SappBindingConfigContactItem) provider.getBindingConfig(itemName);

			SappAddressOpenClosedStatus statusAddress = sappBindingConfigContactItem.getStatus();

			updateOpenClosedItem(provider, statusAddress, itemName, item);
		} else if (item instanceof NumberItem) {
			SappBindingConfigNumberItem sappBindingConfigNumberItem = (SappBindingConfigNumberItem) provider.getBindingConfig(itemName);

			SappAddressDecimal statusAddress = sappBindingConfigNumberItem.getStatus();

			updateDecimalItem(provider, statusAddress, itemName, item);
		} else {
			logger.error("unimplemented item type: " + item.getClass().getSimpleName());
		}
	}

	private SappBindingProvider getFirstSappBindingProvider() {
		for (SappBindingProvider provider : providers) {
			return provider;
		}
		return null;
	}

	private void updateState(String pnmasId, SappAddressType sappAddressType, int addressToUpdate, int newState, SappBindingProvider provider) {
		logger.debug(String.format("Updating %s %d with new value %d", sappAddressType, addressToUpdate, newState));
		for (String itemName : provider.getItemNames()) {
			Item item = provider.getItem(itemName);

			if (item instanceof SwitchItem) {
				SappBindingConfigSwitchItem sappBindingConfigSwitchItem = (SappBindingConfigSwitchItem) provider.getBindingConfig(itemName);
				SappAddressOnOffStatus statusAddress = sappBindingConfigSwitchItem.getStatus();
				if (statusAddress.getAddressType() == sappAddressType && statusAddress.getPnmasId().equals(pnmasId) && addressToUpdate == statusAddress.getAddress()) {
					logger.debug("found binding to update " + sappBindingConfigSwitchItem);
					int result = SappBindingConfigUtils.maskWithSubAddress(statusAddress.getSubAddress(), newState);
					eventPublisher.postUpdate(itemName, result == statusAddress.getOnValue() ? OnOffType.ON : OnOffType.OFF);
				}
			} else if (item instanceof ContactItem) {
				SappBindingConfigContactItem sappBindingConfigContactItem = (SappBindingConfigContactItem) provider.getBindingConfig(itemName);
				SappAddressOpenClosedStatus statusAddress = sappBindingConfigContactItem.getStatus();
				if (statusAddress.getAddressType() == sappAddressType && statusAddress.getPnmasId().equals(pnmasId) && addressToUpdate == statusAddress.getAddress()) {
					logger.debug("found binding to update " + sappBindingConfigContactItem);
					int result = SappBindingConfigUtils.maskWithSubAddress(statusAddress.getSubAddress(), newState);
					eventPublisher.postUpdate(itemName, result == statusAddress.getOpenValue() ? OpenClosedType.OPEN : OpenClosedType.CLOSED);
				}
			} else if (item instanceof NumberItem) {
				SappBindingConfigNumberItem sappBindingConfigNumberItem = (SappBindingConfigNumberItem) provider.getBindingConfig(itemName);
				SappAddressDecimal address = sappBindingConfigNumberItem.getStatus();
				if (address.getAddressType() == sappAddressType && address.getPnmasId().equals(pnmasId) && addressToUpdate == address.getAddress()) {
					logger.debug("found binding to update " + sappBindingConfigNumberItem);
					int result = SappBindingConfigUtils.maskWithSubAddress(address.getSubAddress(), newState);
					result = address.scaledValue(result);
					eventPublisher.postUpdate(itemName, new DecimalType(result));
				}
			} else {
				logger.error("unimplemented item type: " + item.getClass().getSimpleName());
			}
		}
	}

	private void updateOnOffItem(SappBindingProvider provider, SappAddressOnOffStatus statusAddress, String itemName, Item item) {

		switch (statusAddress.getAddressType()) {
		case VIRTUAL:
			try {
				int result = SappBindingConfigUtils.maskWithSubAddress(statusAddress.getSubAddress(), getVirtualValue(provider, statusAddress.getPnmasId(), statusAddress.getAddress(), statusAddress.getSubAddress()));
				eventPublisher.postUpdate(itemName, result == statusAddress.getOnValue() ? OnOffType.ON : OnOffType.OFF);
			} catch (SappException e) {
				logger.error("could not run sappcommand: " + e.getMessage());
			}
			break;

		case INPUT:
			try {
				int result = SappBindingConfigUtils.maskWithSubAddress(statusAddress.getSubAddress(), getInputValue(provider, statusAddress.getPnmasId(), statusAddress.getAddress(), statusAddress.getSubAddress()));
				eventPublisher.postUpdate(itemName, result == statusAddress.getOnValue() ? OnOffType.ON : OnOffType.OFF);
			} catch (SappException e) {
				logger.error("could not run sappcommand: " + e.getMessage());
			}
			break;

		case OUTPUT:
			try {
				int result = SappBindingConfigUtils.maskWithSubAddress(statusAddress.getSubAddress(), getOutputValue(provider, statusAddress.getPnmasId(), statusAddress.getAddress(), statusAddress.getSubAddress()));
				eventPublisher.postUpdate(itemName, result == statusAddress.getOnValue() ? OnOffType.ON : OnOffType.OFF);
			} catch (SappException e) {
				logger.error("could not run sappcommand: " + e.getMessage());
			}
			break;

		default:
			logger.error("item type not yet implemented " + item.getClass().getSimpleName() + " for address type " + statusAddress.getAddressType());
			break;
		}
	}

	private void updateOpenClosedItem(SappBindingProvider provider, SappAddressOpenClosedStatus statusAddress, String itemName, Item item) {

		switch (statusAddress.getAddressType()) {
		case VIRTUAL:
			try {
				int result = SappBindingConfigUtils.maskWithSubAddress(statusAddress.getSubAddress(), getVirtualValue(provider, statusAddress.getPnmasId(), statusAddress.getAddress(), statusAddress.getSubAddress()));
				eventPublisher.postUpdate(itemName, result == statusAddress.getOpenValue() ? OpenClosedType.OPEN : OpenClosedType.CLOSED);
			} catch (SappException e) {
				logger.error("could not run sappcommand: " + e.getMessage());
			}
			break;

		case INPUT:
			try {
				int result = SappBindingConfigUtils.maskWithSubAddress(statusAddress.getSubAddress(), getInputValue(provider, statusAddress.getPnmasId(), statusAddress.getAddress(), statusAddress.getSubAddress()));
				eventPublisher.postUpdate(itemName, result == statusAddress.getOpenValue() ? OpenClosedType.OPEN : OpenClosedType.CLOSED);
			} catch (SappException e) {
				logger.error("could not run sappcommand: " + e.getMessage());
			}
			break;

		case OUTPUT:
			try {
				int result = SappBindingConfigUtils.maskWithSubAddress(statusAddress.getSubAddress(), getOutputValue(provider, statusAddress.getPnmasId(), statusAddress.getAddress(), statusAddress.getSubAddress()));
				eventPublisher.postUpdate(itemName, result == statusAddress.getOpenValue() ? OpenClosedType.OPEN : OpenClosedType.CLOSED);
			} catch (SappException e) {
				logger.error("could not run sappcommand: " + e.getMessage());
			}
			break;

		default:
			logger.error("item type not yet implemented " + item.getClass().getSimpleName() + " for address type " + statusAddress.getAddressType());
			break;
		}
	}

	private void updateDecimalItem(SappBindingProvider provider, SappAddressDecimal address, String itemName, Item item) {

		switch (address.getAddressType()) {
		case VIRTUAL:
			try {
				int result = SappBindingConfigUtils.maskWithSubAddress(address.getSubAddress(), getVirtualValue(provider, address.getPnmasId(), address.getAddress(), address.getSubAddress()));
				result = address.scaledValue(result);
				eventPublisher.postUpdate(itemName, new DecimalType(result));
			} catch (SappException e) {
				logger.error("could not run sappcommand: " + e.getMessage());
			}
			break;

		case INPUT:
			try {
				int result = SappBindingConfigUtils.maskWithSubAddress(address.getSubAddress(), getInputValue(provider, address.getPnmasId(), address.getAddress(), address.getSubAddress()));
				result = address.scaledValue(result);
				eventPublisher.postUpdate(itemName, new DecimalType(result));
			} catch (SappException e) {
				logger.error("could not run sappcommand: " + e.getMessage());
			}
			break;

		case OUTPUT:
			try {
				int result = SappBindingConfigUtils.maskWithSubAddress(address.getSubAddress(), getOutputValue(provider, address.getPnmasId(), address.getAddress(), address.getSubAddress()));
				result = address.scaledValue(result);
				eventPublisher.postUpdate(itemName, new DecimalType(result));
			} catch (SappException e) {
				logger.error("could not run sappcommand: " + e.getMessage());
			}
			break;

		default:
			logger.error("item type not yet implemented " + item.getClass().getSimpleName() + " for address type " + address.getAddressType());
			break;
		}
	}

	private int getVirtualValue(SappBindingProvider provider, String pnmasId, int address, String subAddress) throws SappException {

		Integer value = provider.getVirtualCachedValue(address);
		if (value == null) {
			SappPnmas pnmas = provider.getPnmasMap().get(pnmasId);
			
			SappCentralExecuter sappCentralExecuter = SappCentralExecuter.getInstance();
			value = sappCentralExecuter.executeSapp7CCommand(pnmas.getIp(), pnmas.getPort(), address);
			provider.setVirtualCachedValue(address, value);
		}

		return value.intValue();
	}

	private int getInputValue(SappBindingProvider provider, String pnmasId, int address, String subAddress) throws SappException {

		Integer value = provider.getInputCachedValue(address);
		if (value == null) {
			SappPnmas pnmas = provider.getPnmasMap().get(pnmasId);

			SappCentralExecuter sappCentralExecuter = SappCentralExecuter.getInstance();
			value = sappCentralExecuter.executeSapp74Command(pnmas.getIp(), pnmas.getPort(), (byte) address);
			provider.setInputCachedValue(address, value);
		}
		return value.intValue();
	}

	private int getOutputValue(SappBindingProvider provider, String pnmasId, int address, String subAddress) throws SappException {

		Integer value = provider.getOutputCachedValue(address);
		if (value == null) {
			SappPnmas pnmas = provider.getPnmasMap().get(pnmasId);

			SappCentralExecuter sappCentralExecuter = SappCentralExecuter.getInstance();
			value = sappCentralExecuter.executeSapp75Command(pnmas.getIp(), pnmas.getPort(), (byte) address);
			provider.setOutputCachedValue(address, value);
		}
		return value.intValue();
	}
}
