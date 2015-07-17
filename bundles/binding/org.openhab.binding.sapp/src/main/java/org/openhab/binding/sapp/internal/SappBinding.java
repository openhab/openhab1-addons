/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.sapp.internal;

import java.io.IOException;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.openhab.binding.sapp.SappBindingProvider;
import org.openhab.core.binding.AbstractActiveBinding;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.openhab.core.types.UnDefType;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.paolodenti.jsapp.core.command.Sapp7CCommand;
import com.github.paolodenti.jsapp.core.command.Sapp7DCommand;
import com.github.paolodenti.jsapp.core.command.Sapp80Command;
import com.github.paolodenti.jsapp.core.command.Sapp81Command;
import com.github.paolodenti.jsapp.core.command.Sapp82Command;
import com.github.paolodenti.jsapp.core.command.base.SappCommand;
import com.github.paolodenti.jsapp.core.command.base.SappConnection;
import com.github.paolodenti.jsapp.core.command.base.SappException;

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
	 * (optional, defaults to 60000ms)
	 */
	private long refreshInterval = 60000;

	/**
	 * syncronization object used to avoid overlapping pollings
	 */
	private boolean isInPolling = false;

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

		// the frequently executed code (polling) goes here ...

		if (!isInPolling) {
			synchronized (this) { // do not overlap execute()
				if (!isInPolling) {
					try {
						isInPolling = true;

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
									for (SappPnmas pnmas : provider.getPnmasMap().values()) { // each pnmas
										try {
											SappConnection sappConnection = new SappConnection(pnmas.getIp(), pnmas.getPort());
											sappConnection.openConnection();

											try {
												SappCommand sappCommand;

												// poll outputs
												sappCommand = new Sapp80Command();
												sappCommand.run(sappConnection);
												if (!sappCommand.isResponseOk()) {
													throw new SappException("command execution failed");
												}
												Map<Byte, Integer> changedOutputs = sappCommand.getResponse().getDataAsByteWordMap();
												if (changedOutputs.size() != 0) {
													// TODO update items
												}

												// poll inputs
												sappCommand = new Sapp81Command();
												sappCommand.run(sappConnection);
												if (!sappCommand.isResponseOk()) {
													throw new SappException("command execution failed");
												}
												Map<Byte, Integer> changedInputs = sappCommand.getResponse().getDataAsByteWordMap();
												if (changedInputs.size() != 0) {
													// TODO update items
												}

												// poll virtuals
												sappCommand = new Sapp82Command();
												sappCommand.run(sappConnection);
												if (!sappCommand.isResponseOk()) {
													throw new SappException("command execution failed");
												}
												Map<Integer, Integer> changedVirtuals = sappCommand.getResponse().getDataAsWordWordMap();
												if (changedVirtuals.size() != 0) {
													// TODO update items
													// TODO rimuovere fake
													initializeAllItemsInProvider(provider);
												}
											} finally {
												sappConnection.closeConnection();
											}
										} catch (IOException e) {
											logger.error("polling failed on pnmas " + pnmas);
										} catch (SappException e) {
											logger.error("polling failed on pnmas " + pnmas);
										}
									}
								}
							}
						}
					} finally {
						isInPolling = false;
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
			
			SappAddress controlAddress = sappBindingConfigSwitchItem.getControl();
			
			if (!provider.getPnmasMap().containsKey(controlAddress.getPnmasId())) {
				logger.error(String.format("bad pnmas id (%s) in binding (%s) ... skipping", controlAddress.getPnmasId(), sappBindingConfigSwitchItem));
				return;
			}

			try {
				if (command instanceof OnOffType) { // set bit
					switch (controlAddress.getAddressType()) {
					case ALARM:
					case INPUT:
					case OUTPUT: {
						logger.error("cannot run " + command.getClass().getSimpleName() + " on " + controlAddress.getAddressType() + " type");
						break;
					}

					case VIRTUAL: {
						SappPnmas pnmas = provider.getPnmasMap().get(controlAddress.getPnmasId());
						SappCommand sappCommand = new Sapp7DCommand(controlAddress.getAddress(), command.equals(OnOffType.ON) ? 1 : 0);
						sappCommand.run(pnmas.getIp(), pnmas.getPort());
						if (!sappCommand.isResponseOk()) {
							throw new SappException("command execution failed");
						}
						break;
					}

					default:
						break;
					}
				} else { // TODO
					logger.error("command " + command.getClass().getSimpleName() + " not yet implemented");
				}
			} catch (SappException e) {
				logger.error("could not run sappcommand: " + e.getMessage());
			}
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
			logger.debug("querying and setting item" + itemName);
			State actualState = provider.getItem(itemName).getState();
			if (actualState instanceof UnDefType) { // item just added, refresh
				queryAndSendActualState(provider, itemName);
			}
		}
	}

	private void queryAndSendActualState(SappBindingProvider provider, String itemName) {

		Item item = provider.getItem(itemName);

		if (item instanceof SwitchItem) {
			SappBindingConfigSwitchItem sappBindingConfigSwitchItem = (SappBindingConfigSwitchItem) provider.getBindingConfig(itemName);

			SappAddress statusAddress = sappBindingConfigSwitchItem.getStatus();

			switch (statusAddress.getAddressType()) {
			case VIRTUAL:
				try {
					SappPnmas pnmas = provider.getPnmasMap().get(statusAddress.getPnmasId());
					SappCommand sappCommand = new Sapp7CCommand(statusAddress.getAddress());
					sappCommand.run(pnmas.getIp(), pnmas.getPort());
					if (!sappCommand.isResponseOk()) {
						throw new SappException("command execution failed");
					}
					int result = SappBindingUtils.filter(statusAddress.getSubAddress(), sappCommand.getResponse().getDataAsWord());

					eventPublisher.postUpdate(itemName, result == 0 ? OnOffType.OFF : OnOffType.ON);
				} catch (SappException e) {
					logger.error("could not run sappcommand: " + e.getMessage());
				}
				break;

			default:
				logger.error("wrong item type " + item.getClass().getSimpleName() + " for address type " + statusAddress.getAddressType());
				break;
			}
		} else { // TODO complete with other items
			logger.error("unimplemented item type: " + item.getClass().getSimpleName());
		}
	}

	private SappBindingProvider getFirstSappBindingProvider() {
		for (SappBindingProvider provider : providers) {
			return provider;
		}
		return null;
	}
}
