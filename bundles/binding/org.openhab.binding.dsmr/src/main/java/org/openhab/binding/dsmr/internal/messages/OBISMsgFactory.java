package org.openhab.binding.dsmr.internal.messages;

import java.lang.reflect.Constructor;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openhab.binding.dsmr.internal.DSMRVersion;
import org.openhab.core.types.State;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Factory for constructing OBIS messages from Strings
 * 
 * @author M. Volaart
 * @since 1.7.0
 */
public class OBISMsgFactory {
	/* logger */
	private static final Logger logger = LoggerFactory
			.getLogger(OBISMsgFactory.class);

	/* Regular expression for OBIS reduced Identifier */
	private static final String OBIS_REGEX = "^(\\d{1,3}-\\d{1,3}:\\d{1,3}\\.\\d{1,3}\\.\\d{1,3})\\((.*)\\)$";

	/* Pattern instance */
	private final Pattern obisPattern;
	
	/* applicable DSMRVersion */
	private final DSMRVersion version;

	/**
	 * Creates a new OBISMsgFactory for the specified DSMRVersion
	 * 
	 * @param version {@link DMSRVersion}
	 */
	public OBISMsgFactory(DSMRVersion version) {
		obisPattern = Pattern.compile(OBIS_REGEX);
		this.version = version;
	}

	/**
	 * Return OBISMessage from specified string or null if string couldn't be
	 * parsed correctly or no corresponding OBISMessage was found
	 * 
	 * @param obisStr
	 *            the obisStr
	 * @return OBISMessage or null if parsing failed
	 */
	public OBISMessage<? extends State> getMessage(String obisStr) {
		OBISMessage<? extends State> msg = null;

		if (obisStr != null) {
			Matcher m = obisPattern.matcher(obisStr);

			if (m.matches()) {
				logger.debug("Received valid OBIS String:" + obisStr);

				OBISMsgType msgType = getOBISMsgType(m.group(1));
				String msgValue = m.group(2);

				logger.debug("OBIS message type:" + msgType + ", value:"
						+ msgValue);

				try {
					Constructor<? extends OBISMessage<? extends State>> c = msgType.clazz
							.getConstructor(new Class[] { OBISMsgType.class,
									String.class });

					msg = c.newInstance(msgType, msgValue);
				} catch (ReflectiveOperationException roe) {
					logger.error("Internal error instantiating OBISMSG", roe);
				}
			}
		}

		logger.trace("Converted to:" + msg);

		return msg;
	}

	/**
	 * Returns the OBIS Message Type for the specified OBIS reduced identifer
	 * 
	 * @param obisId String containing the OBIS reduced Identifier
	 * @return the {@link OBISMsgType} or UNKNOWN if the OBIS reduced Identifer is unknown 
	 */
	private OBISMsgType getOBISMsgType(String obisId) {
		if (obisId.length() > 0) {
			for (OBISMsgType t : OBISMsgType.values()) {
				if (t.obisReference.equals(obisId)
						&& t.applicableVersions.contains(version)) {
					return t;
				}
			}
		}
		return OBISMsgType.UNKNOWN;
	}
}
