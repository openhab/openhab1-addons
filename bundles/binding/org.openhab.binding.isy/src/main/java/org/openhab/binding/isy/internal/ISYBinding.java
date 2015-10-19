/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.isy.internal;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.openhab.binding.isy.ISYBindingConfig;
import org.openhab.binding.isy.ISYBindingConfig.Type;
import org.openhab.binding.isy.ISYBindingProvider;
import org.openhab.binding.isy.ISYControl;
import org.openhab.binding.isy.ISYModelChangeListener;
import org.openhab.core.binding.AbstractActiveBinding;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.OpenClosedType;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.universaldevices.device.model.UDControl;
import com.universaldevices.device.model.UDGroup;
import com.universaldevices.device.model.UDNode;
import com.universaldevices.resources.errormessages.Errors;

/**
 * Implement this class if you are going create an actively polling service like
 * querying a Website/Device.
 *
 * @author Tim Diekmann
 * @since 1.7.0
 */
public class ISYBinding extends AbstractActiveBinding<ISYBindingProvider>
		implements ISYModelChangeListener {

	private final Logger logger = LoggerFactory
			.getLogger(ISYBinding.class);

	/**
	 * the refresh interval which is used to poll values from the isy server
	 * (optional, defaults to 60000ms)
	 */
	private long refreshInterval = 60000;

	/**
	 * the IP address of the ISY router
	 */
	private String ipAddress;

	/**
	 * the port of the REST API of the ISY router
	 */
	private int port;

	/**
	 * If true, use UPNP without credentials
	 */
	private boolean useUpnp;

	/**
	 * UUID of the ISY
	 */
	private String uuid;

	/**
	 * User name for authentication with the ISY 994i
	 */
	private String user;

	/**
	 * User password for authentication with the ISY 994i
	 */
	private String password;

	private InsteonClient insteonClient;

	public ISYBinding() {
	}

	/**
	 * Called by SCR when the binding is resolved
	 *
	 * @param config
	 */
	public void activate(final Map<Object, Object> config) {
		String refreshIntervalString = (String) config.get("refresh");
		if (StringUtils.isNotBlank(refreshIntervalString)) {
			this.refreshInterval = Long.parseLong(refreshIntervalString);
		}

		// use UPNP ?
		String upnp = (String) config.get("upnp");
		if (StringUtils.isNotBlank(upnp)) {
			this.useUpnp = Boolean.parseBoolean(upnp);
		} else {
			this.useUpnp = false;
		}

		// ISY uuid
		String uuidstr = (String) config.get("uuid");
		if (StringUtils.isNotBlank(uuidstr)) {
			this.uuid = uuidstr;
		} else if (!this.useUpnp) {
			throw new RuntimeException("Need non-null uuid");
		}

		// ISY IP
		String ip = (String) config.get("ip");
		if (StringUtils.isNotBlank(ip)) {
			this.ipAddress = ip;
		} else if (!this.useUpnp) {
			throw new RuntimeException("Need non-null IP address");
		}

		// ISY port
		String portStr = (String) config.get("port");
		if (StringUtils.isNotBlank(portStr)) {
			try {
				this.port = Integer.parseInt(portStr);
				if (this.port < 0 || this.port > 65535) {
					throw new NumberFormatException("Invalid port");
				}
			} catch (NumberFormatException ne) {
				throw new RuntimeException("not a valid port number", ne);
			}
		} else if (!this.useUpnp) {
			throw new RuntimeException("Need valid port number");
		}

		// ISY user
		String userstr = (String) config.get("user");
		if (StringUtils.isNotBlank(userstr)) {
			this.user = userstr;
		} else if (!this.useUpnp) {
			throw new RuntimeException("Need non-null user");
		}

		// ISY password
		String passwordstr = (String) config.get("password");
		if (StringUtils.isNotBlank(passwordstr)) {
			this.password = passwordstr;
		}

		setProperlyConfigured(true);

		// create the Insteon client using their SDK
		//

		this.insteonClient = new InsteonClient(this.user, this.password, this);
		Errors.addErrorListener(new ErrorHandler());

		if (this.useUpnp) {
			this.logger.info("ISY: using UPNP to communicate with ISY");

			this.insteonClient.start();
		} else {
			this.logger.info("ISY: using URL to communicate with ISY");

			try {
				this.insteonClient.start(this.uuid, String.format(
						"http://%s:%s", this.ipAddress, this.port));
			} catch (Exception e) {
				throw new RuntimeException(e);
			}

		}
	}

	/**
	 * Called by SCR when the binding configuration was modified
	 */
	public void modified(final Map<Object, Object> config) {
		String refreshIntervalString = (String) config.get("refresh");
		if (StringUtils.isNotBlank(refreshIntervalString)) {
			this.refreshInterval = Long.parseLong(refreshIntervalString);
		}

		// use UPNP ?
		String upnp = (String) config.get("upnp");
		if (StringUtils.isNotBlank(upnp)) {
			this.useUpnp = Boolean.parseBoolean(upnp);
		} else {
			this.useUpnp = false;
		}

		// ISY uuid
		String uuidstr = (String) config.get("uuid");
		if (StringUtils.isNotBlank(uuidstr)) {
			this.uuid = uuidstr;
		} else if (!this.useUpnp) {
			throw new RuntimeException("Need non-null uuid");
		}

		// ISY IP
		String ip = (String) config.get("ip");
		if (StringUtils.isNotBlank(ip)) {
			this.ipAddress = ip;
		} else if (!this.useUpnp) {
			throw new RuntimeException("Need non-null IP address");
		}

		// ISY port
		String portStr = (String) config.get("port");
		if (StringUtils.isNotBlank(portStr)) {
			try {
				this.port = Integer.parseInt(portStr);
				if (this.port < 0 || this.port > 65535) {
					throw new NumberFormatException("Invalid port");
				}
			} catch (NumberFormatException ne) {
				throw new RuntimeException("not a valid port number", ne);
			}
		} else if (!this.useUpnp) {
			throw new RuntimeException("Need valid port number");
		}

		// ISY user
		String userstr = (String) config.get("user");
		if (StringUtils.isNotBlank(userstr)) {
			this.user = userstr;
		} else if (!this.useUpnp) {
			throw new RuntimeException("Need non-null user");
		}

		// ISY password
		String passwordstr = (String) config.get("password");
		if (StringUtils.isNotBlank(passwordstr)) {
			this.password = passwordstr;
		}

		setProperlyConfigured(true);

		// create the Insteon client using their SDK
		//

		if (this.insteonClient != null) {
			this.insteonClient.stop();
		}

		this.insteonClient = new InsteonClient(this.user, this.password, this);
		Errors.addErrorListener(new ErrorHandler());

		if (this.useUpnp) {
			this.logger.info("ISY: using UPNP to communicate with ISY");

			this.insteonClient.start();
		} else {
			this.logger.info("ISY: using URL to communicate with ISY");

			try {
				this.insteonClient.start(this.uuid, String.format(
						"http://%s:%s", this.ipAddress, this.port));
			} catch (Exception e) {
				throw new RuntimeException(e);
			}

		}
	}

	@Override
	public void deactivate() {
		if (this.insteonClient != null) {
			this.insteonClient.stop();
			this.insteonClient = null;
		}
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	protected long getRefreshInterval() {
		return this.refreshInterval;
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	protected String getName() {
		return "ISY Refresh Service";
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	protected void execute() {
		// the frequently executed code (polling) goes here ...
		if (this.logger.isDebugEnabled()) {
			this.logger.debug("refreshing status");
		}

		queryStatus();
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	protected void internalReceiveCommand(final String itemName,
			final Command command) {
		// the code being executed when a command was sent on the openHAB
		// event bus goes here. This method is only called if one of the
		// BindingProviders provide a binding for the given 'itemName'.
		if (this.logger.isDebugEnabled()) {
			this.logger.debug("internalReceiveCommand({},{}) is called!", itemName,
					command);
		}

		ISYBindingConfig config = getBindingConfigByName(itemName);

		if (config != null) {
			processCommand(config, command);
		} else {
			this.logger.warn("No configured Insteon address found for item [{}]",
					itemName);
		}
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	protected void internalReceiveUpdate(final String itemName,
			final State newState) {
		// the code being executed when a state was sent on the openHAB
		// event bus goes here. This method is only called if one of the
		// BindingProviders provide a binding for the given 'itemName'.
		if (this.logger.isDebugEnabled()) {
			this.logger.debug("internalReceiveCommand({},{}) is called!", itemName,
					newState);
		}
	}

	/**
	 * Callback from the ISY client when a node changed its state. Find the
	 * corresponding openHAB item and send a notification on the event bus.
	 *
	 * @param control
	 *            {@link UDControl} ISY command
	 * @param action
	 *            Mostly a string with the value, e.g. "0", "255"
	 * @param node
	 *            {@link UDNode} ISY node can be a device or a group
	 */
	@Override
	public void onModelChanged(final UDControl control, final Object action,
			final UDNode node) {
		Collection<ISYBindingConfig> configs;
		State state = null;
		ISYControl ctrl;

		try {
			ctrl = ISYControl.valueOf(control.name);
		} catch (IllegalArgumentException ie) {
			ctrl = ISYControl.UNDEFINED;
		}

		switch (ctrl) {
		case ST:
			// only propagate status updates

			configs = getBindingConfigFromAddress(node.address, control.name);

			for (ISYBindingConfig config : configs) {

				switch (config.type) {
				case SWITCH:
					if ("0".equals(action)) {
						state = OnOffType.OFF;
					} else {
						state = OnOffType.ON;
					}
					break;
				case GROUP:
					if ("0".equals(action)) {
						state = OnOffType.OFF;
					} else {
						state = OnOffType.ON;
					}
					break;
				case CONTACT:
					if ("0".equals(action)) {
						state = OpenClosedType.CLOSED;
					} else {
						state = OpenClosedType.OPEN;
					}
					break;
				case THERMOSTAT:
					state = new DecimalType(
							new DecimalType((String) action).doubleValue() * 0.5);
					break;
				case NUMBER:
					state = new DecimalType((String) action);
					break;
				default:
					break;
				}

				if (state != null) {
					this.eventPublisher
							.postUpdate(config.item.getName(), state);
				}
			}
			break;

		case DON:
		case DFON:
			for (ISYBindingConfig config : getBindingConfigFromAddress(
					node.address, control.name)) {

				if (config.item.getAcceptedDataTypes().contains(
						OpenClosedType.class)) {
					if ("1".equals(action)) {
						state = OpenClosedType.OPEN;
					}
				} else if (config.item.getAcceptedDataTypes().contains(
						OnOffType.class)) {
					if (!"0".equals(action)) {
						state = OnOffType.ON;
					}
				} else if (config.item.getAcceptedDataTypes().contains(
						DecimalType.class)) {
					state = new DecimalType((String) action);
				}

				if (state != null) {
					this.eventPublisher
							.postUpdate(config.item.getName(), state);
				}
			}
			break;

		case DOF:
		case DFOF:
			for (ISYBindingConfig config : getBindingConfigFromAddress(
					node.address, control.name)) {

				if (config.item.getAcceptedDataTypes().contains(
						OpenClosedType.class)) {
					if ("1".equals(action)) {
						state = OpenClosedType.CLOSED;
					}
				} else if (config.item.getAcceptedDataTypes().contains(
						OnOffType.class)) {
					if ("1".equals(action)) {
						state = OnOffType.OFF;
					}
				} else if (config.item.getAcceptedDataTypes().contains(
						DecimalType.class)) {
					state = new DecimalType((String) action);
				}

				if (state != null) {
					this.eventPublisher
							.postUpdate(config.item.getName(), state);
				}
			}
			break;

		case CLISPH:
			for (ISYBindingConfig config : getBindingConfigFromAddress(
					node.address, control.name)) {

				switch (config.type) {
				case THERMOSTAT:
					state = new DecimalType(
							new DecimalType((String) action).doubleValue() * 0.5);
					break;
				default:
					state = null;
					break;
				}

				if (state != null) {
					this.eventPublisher
							.postUpdate(config.item.getName(), state);
				}
			}
			break;

		case CLIHUM:
			for (ISYBindingConfig config : getBindingConfigFromAddress(
					node.address, control.name)) {

				switch (config.type) {
				case THERMOSTAT:
					state = new DecimalType((String) action);
					break;
				default:
					state = null;
					break;
				}

				if (state != null) {
					this.eventPublisher
							.postUpdate(config.item.getName(), state);
				}
			}
			break;

		case CLIMD:
			for (ISYBindingConfig config : getBindingConfigFromAddress(
					node.address, control.name)) {
				state = new DecimalType((String) action);

				this.eventPublisher.postUpdate(config.item.getName(), state);
			}
			break;

		default:
			if (this.logger.isDebugEnabled()) {
				this.logger.debug("Unsupported control {}", control.name);
			}
		}
	}

	private Collection<ISYBindingConfig> getBindingConfigFromAddress(
			final String address, final String cmd) {
		for (ISYBindingProvider provider : this.providers) {
			Collection<ISYBindingConfig> config = provider
					.getBindingConfigFromAddress(address, cmd);

			if (config != null) {
				return config;
			}
		}

		return Collections.EMPTY_LIST;
	}

	private ISYBindingConfig getBindingConfigByName(final String itemName) {
		for (ISYBindingProvider provider : this.providers) {
			ISYBindingConfig config = provider
					.getBindingConfigFromItemName(itemName);

			if (config != null) {
				return config;
			}
		}

		return null;
	}

	/**
	 * Translate the openHAB command into a Insteon command
	 *
	 * @param address
	 *            Insteon address of device/node/group/scene
	 * @param command
	 *            openHAB command
	 */
	private void processCommand(final ISYBindingConfig config,
			final Command command) {
		if (!this.insteonClient.isISYReady()) {
			this.logger.warn("ISY is not ready");

			return;
		}

		UDNode node = null;

		// find out whether node is group or node
		try {
			node = this.insteonClient.getNodes().get(config.controller);

		} catch (Exception e) {
			this.logger.warn("no such device, " + config.controller, e);
		}

		if (node == null) {
			try {
				node = this.insteonClient.getGroups().get(config.controller);

			} catch (Exception e) {
				this.logger.warn("no such group found, " + config.controller, e);
			}
		}

		if (node != null) {
			executeCommand(config, node, command);

		} else {
			this.logger.warn("No node found for address " + config.controller);
		}
	}

	private void executeCommand(final ISYBindingConfig config,
			final UDNode node, final Command command) {
		if (command instanceof OnOffType) {
			OnOffType type = (OnOffType) command;

			switch (type) {
			case ON:
				if (Type.THERMOSTAT.equals(config.type)) {
					// turn thermostat off
					this.insteonClient.changeNodeState(ISYControl.CLIMD.name(),
							"1",
							node.address);

					// provide immediate feedback such that the UI gets updated
					// or else the switch button will not be refreshed until the
					// new state has been reached
					this.eventPublisher.postUpdate(config.item.getName(), type);
				} else if (node instanceof UDGroup) {
					this.insteonClient.turnSceneOn(node.address);
				} else {
					this.insteonClient.turnDeviceFastOn(node.address);
				}
				break;

			case OFF:
				if (Type.THERMOSTAT.equals(config.type)) {
					// turn thermostat off
					this.insteonClient.changeNodeState(ISYControl.CLIMD.name(),
							"0",
							node.address);
					// provide immediate feedback such that the UI gets updated
					// or else the switch button will not be refreshed until the
					// new state has been reached
					this.eventPublisher.postUpdate(config.item.getName(), type);
				} else if (node instanceof UDGroup) {
					this.insteonClient.turnSceneFastOff(node.address);
				} else {
					this.insteonClient.turnDeviceFastOff(node.address);
				}
				break;
			}
		} else if (command instanceof DecimalType) {
			DecimalType type = (DecimalType) command;

			switch (config.type) {
			case THERMOSTAT:
				switch (config.cmd) {
				case CLISPH:
					DecimalType value = new DecimalType(
							type.doubleValue() * 2.0);
					this.insteonClient.changeNodeState(
							ISYControl.CLISPH.name(),
							value.format("%d"), node.address);
					break;
				case CLIMD:
					this.insteonClient.changeNodeState(ISYControl.CLIMD.name(),
							type.format("%s"), node.address);
					break;
				default:
					break;
				}
				break;
			default:
				break;
			}
		}
	}

	private void queryStatus() {
		for (ISYBindingProvider provider : this.providers) {
			for (String itemName : provider.getItemNames()) {
				ISYBindingConfig config = provider
						.getBindingConfigFromItemName(itemName);

				updateStatus(itemName, config);
			}
		}
	}

	private void updateStatus(final String itemName,
			final ISYBindingConfig config) {
		if (this.insteonClient != null && this.insteonClient.isISYReady()) {
			try {
				UDNode node = this.insteonClient.getNodes().get(config.address);
				if (node == null) {
					node = this.insteonClient.getGroups().get(config.address);
				}

				if (node != null) {
					Object value = this.insteonClient.getCurrValue(node,
							config.cmd.name());

					UDControl control = new UDControl();
					control.name = config.cmd.name();

					onModelChanged(control, value, node);
				}
			} catch (Exception ex) {
				this.logger.error("Failed to update status of " + itemName, ex);
			}
		} else {
			if (this.logger.isInfoEnabled()) {
				this.logger.info("ISY is not ready yet");
			}
		}
	}

}
