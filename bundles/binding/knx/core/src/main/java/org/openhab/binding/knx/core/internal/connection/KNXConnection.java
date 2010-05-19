package org.openhab.binding.knx.core.internal.connection;

import java.util.Dictionary;

import org.openhab.binding.knx.core.internal.bus.KNX2EventBinding;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tuwien.auto.calimero.exception.KNXException;
import tuwien.auto.calimero.link.KNXNetworkLinkIP;
import tuwien.auto.calimero.link.medium.KNXMediumSettings;
import tuwien.auto.calimero.process.ProcessCommunicator;
import tuwien.auto.calimero.process.ProcessCommunicatorImpl;

/**
 * This class establishes the connection to the KNX bus.
 * It uses the ConfigAdmin service to retrieve the relevant configuration data.
 * 
 * @author Kai Kreuzer
 *
 */
public class KNXConnection implements ManagedService {

	private static final Logger logger = LoggerFactory.getLogger(KNXConnection.class);
	
	private static ProcessCommunicator pc = null;

	/**
	 * Returns the KNXNetworkLink for talking to the KNX bus.
	 * The link can be null, if it has not (yet) been established successfully.
	 * 
	 * @return the KNX network link
	 */
	public static ProcessCommunicator getCommunicator() {
		return pc;
	}

	@SuppressWarnings("rawtypes")
	public void updated(Dictionary config) throws ConfigurationException {
		if (config != null) {
			String ip = (String) config.get("ip");
			if (ip != null) {
				try {
					KNXNetworkLinkIP link = new KNXNetworkLinkIP(ip,
							new KNXMediumSettings(null) {
								public short getMedium() {
									return KNXMediumSettings.MEDIUM_TP1;
								}
							});
					pc = new ProcessCommunicatorImpl(link);
					pc.addProcessListener(new KNX2EventBinding());
				} catch (KNXException e) {
					logger.error("Error connecting to KNX bus", e);
				}
			} else {
				logger.error("No IP address could be found in configuration!");
			}
		} else {
			pc = null;
		}
	}
}
