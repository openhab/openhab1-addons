/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.snmp.internal;

import java.io.IOException;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.openhab.binding.snmp.SnmpBindingProvider;
import org.openhab.core.binding.AbstractActiveBinding;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.items.StringItem;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.transform.TransformationException;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.snmp4j.CommandResponder;
import org.snmp4j.CommandResponderEvent;
import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.event.ResponseListener;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.security.Priv3DES;
import org.snmp4j.security.SecurityProtocols;
import org.snmp4j.smi.Address;
import org.snmp4j.smi.GenericAddress;
import org.snmp4j.smi.Integer32;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.UdpAddress;
import org.snmp4j.smi.Variable;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;

/**
 * The SNMP binding listens to SNMP Traps on the configured port and posts new
 * events of type ({@link StringType} to the event bus.
 * 
 * @author Thomas.Eichstaedt-Engelen
 * @author Chris Jackson - modified binding to support polling SNMP OIDs (SNMP GET) and setting values (SNMP SET).
 * @since 0.9.0
 */
public class SnmpBinding extends AbstractActiveBinding<SnmpBindingProvider>
		implements ManagedService, CommandResponder, ResponseListener {

	private Snmp snmp;
	private static final Logger logger = 
		LoggerFactory.getLogger(SnmpBinding.class);

	private static DefaultUdpTransportMapping transport;

	private static final int SNMP_DEFAULT_PORT = 162;
	/** The local port to bind on and listen to SNMP Traps */
	private static int port = SNMP_DEFAULT_PORT;

	/** The SNMP community to filter SNMP Traps */
	private static String community;

	private static int timeout = 1500;
	private static int retries = 0;

	/**
	 * the interval to find new refresh candidates (defaults to 1000
	 * milliseconds)
	 */
	private int granularity = 1000;

	private Map<String, Long> lastUpdateMap = new HashMap<String, Long>();


	public void activate() {
		logger.debug("SNMP binding activated");
		super.activate();
		setProperlyConfigured(true);
	}

	public void deactivate() {
		stopListening();
		logger.debug("SNMP binding deactivated");
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	protected long getRefreshInterval() {
		return granularity;
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	protected String getName() {
		return "SNMP Refresh Service";
	}

	/**
	 * Configures a {@link DefaultUdpTransportMapping} and starts listening on
	 * <code>SnmpBinding.port</code> for incoming SNMP Traps.
	 */
	private void listen() {
		UdpAddress address = new UdpAddress(SnmpBinding.port);
		try {
			if (transport != null) {
				transport.close();
				transport = null;
			}
			if (snmp != null) {
				snmp.close();
				snmp = null;
			}

			transport = new DefaultUdpTransportMapping(address);

			// add all security protocols
			SecurityProtocols.getInstance().addDefaultProtocols();
			SecurityProtocols.getInstance().addPrivacyProtocol(new Priv3DES());

			// Create Target
			if (SnmpBinding.community != null) {
				CommunityTarget target = new CommunityTarget();
				target.setCommunity(new OctetString(SnmpBinding.community));
			}

			snmp = new Snmp(transport);

			transport.listen();
			logger.debug("SNMP binding is listening on " + address);
		} catch (IOException ioe) {
			logger.error("SNMP binding couldn't listen to " + address, ioe);
		}
	}

	/**
	 * Stops listening for incoming SNMP Traps
	 */
	private void stopListening() {
		if (transport != null) {
			try {
				transport.close();
			} catch (IOException ioe) {
				logger.error("couldn't close connection", ioe);
			}
			transport = null;
		}

		if (snmp != null) {
			try {
				snmp.close();
			} catch (IOException ioe) {
				logger.error("couldn't close snmp", ioe);
			}
			snmp = null;
		}
	}

	/**
	 * Will be called whenever a {@link PDU} is received on the given port
	 * specified in the listen() method. It extracts a {@link Variable}
	 * according to the configured OID prefix and sends its value to the event
	 * bus.
	 */
	public void processPdu(CommandResponderEvent event) {
		Address addr = event.getPeerAddress();
		if (addr == null) {
			return;
		}
		
		String s = addr.toString().split("/")[0];
		if (s == null) {
			logger.error("TRAP: failed to translate address {}", addr);
			dispatchPdu(addr, event.getPDU());
		} else {
			// Need to change the port to 161, which is what the bindings are configured for since
			// at least some SNMP devices send traps from a random port number. Otherwise the trap
			// won't be found as the address check will fail. It feels like there should be a better
			// way to do this!!!
			Address address = GenericAddress.parse("udp:" + s + "/161");
			dispatchPdu(address, event.getPDU());
		}
	}

	/**
	 * Called when a response from a GET is received
	 * @see org.snmp4j.event.ResponseListener#onResponse(org.snmp4j.event.ResponseEvent )
	 */
	@Override
	public void onResponse(ResponseEvent event) {
		dispatchPdu(event.getPeerAddress(), event.getResponse());
	}

	private void dispatchPdu(Address address, PDU pdu) {
		if (pdu != null & address != null) {
			logger.debug("Received PDU from '{}' '{}'", address, pdu);
			for (SnmpBindingProvider provider : providers) {
				for (String itemName : provider.getItemNames()) {
					// Check the IP address
					if (!provider.getAddress(itemName).equals(address)) {
						continue;
					}

					// Check the OID
					OID oid = provider.getOID(itemName);
					Variable variable = pdu.getVariable(oid);
					if (variable != null) {
						Class<? extends Item> itemType = provider.getItemType(itemName);

						// Do any transformations
						String value = variable.toString();
						try {
							value = provider.doTransformation(itemName, value);
						} catch (TransformationException e) {
							logger.error("Transformation error with item {}: {}", itemName, e);
						}

						// Change to a state
						State state = null;
						if (itemType.isAssignableFrom(StringItem.class)) {
							state = StringType.valueOf(value);
						} else if (itemType.isAssignableFrom(NumberItem.class)) {
							state = DecimalType.valueOf(value);
						} else if (itemType.isAssignableFrom(SwitchItem.class)) {
							state = OnOffType.valueOf(value);
						}

						if (state != null) {
							eventPublisher.postUpdate(itemName, state);
						} else {
							logger.debug(
									"'{}' couldn't be parsed to a State. Valid State-Types are String and Number",
									variable.toString());
						}
					} else {
						logger.trace("PDU doesn't contain a variable with OID ‘{}‘", oid.toString());
					}
				}
			}
		}
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	public void internalReceiveCommand(String itemName, Command command) {
		logger.debug("SNMP receive command {} from {}", itemName, command);
		
		SnmpBindingProvider providerCmd = null;
		
		for (SnmpBindingProvider provider : this.providers) {
			OID oid = provider.getOID(itemName, command);
			if (oid != null) {
				providerCmd = provider;
				break;
			}
		}

		if (providerCmd == null) {
			logger.warn("No match for binding provider [itemName={}, command={}]", itemName, command);
			return;
		}

		logger.debug("SNMP command for {} to {}", itemName, providerCmd.toString());

		// Set up the target
		CommunityTarget target = new CommunityTarget();
			target.setCommunity(providerCmd.getCommunity(itemName, command));
			target.setAddress(providerCmd.getAddress(itemName, command));
			target.setRetries(retries);
			target.setTimeout(timeout);
			target.setVersion(SnmpConstants.version1);

		Variable var = providerCmd.getValue(itemName, command);
		OID oid = providerCmd.getOID(itemName, command);
	    VariableBinding varBind = new VariableBinding(oid,var);

		// Create the PDU
		PDU pdu = new PDU();
			pdu.add(varBind);
			pdu.setType(PDU.SET);
			pdu.setRequestID(new Integer32(1));

		logger.debug("SNMP: Send CMD PDU {} {}", providerCmd.getAddress(itemName, command), pdu);

		if (snmp == null) {
			logger.error("SNMP: snmp not initialised - aborting request");
		}
		else {
			sendPDU(target, pdu);
		}
	}
	
	/**
	 * @{inheritDoc
	 */
	@Override
	public void execute() {
		for (SnmpBindingProvider provider : providers) {
			for (String itemName : provider.getInBindingItemNames()) {
				int refreshInterval = provider.getRefreshInterval(itemName);

				Long lastUpdateTimeStamp = lastUpdateMap.get(itemName);
				if (lastUpdateTimeStamp == null) {
					lastUpdateTimeStamp = 0L;
				}

				long age = System.currentTimeMillis() - lastUpdateTimeStamp;
				boolean needsUpdate;
				if (refreshInterval == 0) {
					needsUpdate = false;
				} else {
					needsUpdate = age >= refreshInterval;
				}

				if (needsUpdate) {
					logger.debug("Item '{}' is about to be refreshed", itemName);

					// Set up the target
					CommunityTarget target = new CommunityTarget();
						target.setCommunity(provider.getCommunity(itemName));
						target.setAddress(provider.getAddress(itemName));
						target.setRetries(retries);
						target.setTimeout(timeout);
						target.setVersion(SnmpConstants.version1);

					// Create the PDU
					PDU pdu = new PDU();
						pdu.add(new VariableBinding(provider.getOID(itemName)));
						pdu.setType(PDU.GET);

					logger.debug("SNMP: Send PDU {} {}", provider.getAddress(itemName), pdu);

					if (snmp == null) {
						logger.error("SNMP: snmp not initialised - aborting request");
					} else {
						sendPDU(target, pdu);
					}

					lastUpdateMap.put(itemName, System.currentTimeMillis());
				}
			}
		}

	}

	/**
	 * {@inheritDoc}
	 */
	public void updated(Dictionary<String, ?> config) throws ConfigurationException {
		boolean mapping = false;
		stopListening();

		if (config != null) {
			mapping = true;

			SnmpBinding.community = (String) config.get("community");
			if (StringUtils.isBlank(SnmpBinding.community)) {
				SnmpBinding.community = "public";
				logger.info(
						"didn't find SNMP community configuration -> listen to SNMP community {}",
						SnmpBinding.community);
			}

			String portString = (String) config.get("port");
			if (StringUtils.isNotBlank(portString) && portString.matches("\\d*")) {
				SnmpBinding.port = Integer.valueOf(portString).intValue();
			} else {
				SnmpBinding.port = SNMP_DEFAULT_PORT;
				logger.info(
						"Didn't find SNMP port configuration or configuration is invalid -> listen to SNMP default port {}",
						SnmpBinding.port);
			}

			String timeoutString = (String) config.get("timeout");
			if (StringUtils.isNotBlank(timeoutString)) {
				SnmpBinding.timeout = Integer.valueOf(timeoutString).intValue();
				if (SnmpBinding.timeout < 0 | SnmpBinding.retries > 5) {
					logger.info("SNMP timeout value is invalid (" + SnmpBinding.timeout + "). Using default value.");
					SnmpBinding.timeout = 1500;
				}
			} else {
				SnmpBinding.timeout = 1500;
				logger.info(
						"Didn't find SNMP timeout or configuration is invalid -> timeout set to {}",
						SnmpBinding.timeout);
			}

			String retriesString = (String) config.get("retries");
			if (StringUtils.isNotBlank(retriesString)) {
				SnmpBinding.retries = Integer.valueOf(retriesString).intValue();
				if (SnmpBinding.retries < 0 | SnmpBinding.retries > 5) {
					logger.info("SNMP retries value is invalid ("
							+ SnmpBinding.retries + "). Using default value.");
					SnmpBinding.retries = 0;
				}
			} else {
				SnmpBinding.retries = 0;
				logger.info(
						"Didn't find SNMP retries or configuration is invalid -> retries set to {}",
						SnmpBinding.retries);
			}

		}

		for (SnmpBindingProvider provider : providers) {
			if (provider.getInBindingItemNames() != null) {
				mapping = true;
			}
		}

		// Did we find either a trap request, or any bindings
		if (mapping) {
			listen();
		}
	}

	private void sendPDU(CommunityTarget target, PDU pdu) {
		try {
			snmp.send(pdu, target, null, this);
		} catch (IOException e) {
			logger.error("Error sending PDU", e);
		}
	}
	
}
