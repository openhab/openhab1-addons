/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010-2013, openHAB.org <admin@openhab.org>
 *
 * See the contributors.txt file in the distribution for a
 * full listing of individual contributors.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see <http://www.gnu.org/licenses>.
 *
 * Additional permission under GNU GPL version 3 section 7
 *
 * If you modify this Program, or any covered work, by linking or
 * combining it with Eclipse (or a modified version of that library),
 * containing parts covered by the terms of the Eclipse Public License
 * (EPL), the licensors of this Program grant you additional permission
 * to convey the resulting work.
 */
package org.openhab.binding.modbus.internal;

import java.util.Collection;
import java.util.Collections;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.ArrayUtils;
import org.openhab.binding.modbus.ModbusBindingProvider;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Entries in openhab config file should look like below. Most of config 
 * parameters are related to specific slaves, the only exception is
 * <p>
 * modbus:poll=<value>
 * </p>
 * which sets refresh interval to Modbus polling service. Value in milliseconds
 * - optional, default is 200
 * <p>
 * modbus:<slave-type>.<slave-name>.<slave-parameter>
 * </p><p>
 * <slave-type> should be either "tcp" or "serial".
 * <slave-name> is unique name per slave you are connecting to.
 * <slave-parameter> are pairs key=value
 * </p><p>
 * Valid keys are
 * <ul>
 * <li>connection - mandatory. For tcp slaves connection should be ip address or ip:port, 
 * if port is omitted it is defaulted to 502. For serial slave connection should be com-port-name or com-port-name:baud, if baud is omitted it is defaulted to 9600</li>
 * <li>id - slave id, optional, default 1</li>
 * <li>start - slave start address, optional, default 0</li>
 * <li>length -  number of data item to read, default 0 (but set it to something meaningful :)</li>
 * <li>type - data type, can be "coil" "discrete" "holding" "input"</li>
 * </ul>
 * </p><p>
 * Minimal construction in openhab.config will look like
 * <pre>
 * modbus:tcp.slave1.connection=192.168.1.50
 * modbus:tcp.slave1.length=10
 * modbus:tcp.slave1.type=coil
 * </pre>
 * connects to slave on ip=192.168.1.51 and reads 10 coils starting from address 0.
 * <p>
 * More complex setup could look like
 * <pre>
 * modbus:poll=300
 * modbus:serial.slave1.connection=COM15:19200
 * modbus:serial.slave1.id=41
 * modbus:serial.slave1.start=0
 * modbus:seria.slave1.length=32
 * modbus:serial.slave1.type=coil
 * </pre>
 * @author Dmitry Krasnov
 * @since 1.1.0
 */
public class ModbusConfiguration  implements ManagedService {

	private static final Logger logger = LoggerFactory.getLogger(ModbusConfiguration.class);

	private static final String TCP_PREFIX = "tcp";
	private static final String SERIAL_PREFIX = "serial";

	private static final Pattern EXTRACT_MODBUS_CONFIG_PATTERN =
			Pattern.compile("^("+TCP_PREFIX+"|"+SERIAL_PREFIX+"|)\\.(.*?)\\.(connection|id|poll|start|length|type)$");

	/** Stores instances of all the slaves defined in cfg file */
	private static Map<String, ModbusSlave> modbusSlaves = Collections.synchronizedMap(new HashMap<String, ModbusSlave>());

	/** slaves update interval in milliseconds */
	public static int poll = 0;


	public int getPoll() {
		return poll;
	}

	public static ModbusSlave getSlave(String slave) {
		return Collections.synchronizedMap(modbusSlaves).get(slave);
	}

	public static Collection<ModbusSlave> getAllSlaves() {
		return Collections.synchronizedMap(modbusSlaves).values();
	}

	@Override
	@SuppressWarnings("rawtypes")
	public void updated(Dictionary config) throws ConfigurationException {

		// remove all known items if configuration changed
		Collections.synchronizedMap(modbusSlaves).clear();

		if (config != null) {

			Enumeration keys = config.keys();
			while (keys.hasMoreElements()) {

				String key = (String) keys.nextElement();

				// the config-key enumeration contains additional keys that we
				// don't want to process here ...
				if ("service.pid".equals(key)) {
					continue;
				}

				Matcher matcher = EXTRACT_MODBUS_CONFIG_PATTERN.matcher(key);
				if (!matcher.matches()) {
					if ("poll".equals(key)) {
						poll = Integer.valueOf((String) config.get(key));
					} else if ("writemultipleregisters".equals(key)) {
						ModbusSlave.setWriteMultipleRegisters(Boolean.valueOf(config.get(key).toString()));
					} else {
						logger.debug("given modbus-slave-config-key '"
								+ key
								+ "' does not follow the expected pattern 'poll' or '<slaveId>.<connection|id|start|length|type>'");
					}
					continue;
				}

				matcher.reset();
				matcher.find();

				String slave = matcher.group(2);

				ModbusSlave modbusSlave = Collections.synchronizedMap(modbusSlaves).get(slave);
				if (modbusSlave == null) {
					if (matcher.group(1).equals(TCP_PREFIX)) {
						modbusSlave = new ModbusTcpSlave(slave);						
					} else 
						if (matcher.group(1).equals(SERIAL_PREFIX)) {
							modbusSlave = new ModbusSerialSlave(slave);						
						} else 					throw new ConfigurationException(slave,
								"the given slave type '" + slave + "' is unknown");

					Collections.synchronizedMap(modbusSlaves).put(slave, modbusSlave);
				}

				String configKey = matcher.group(3);
				String value = (String) config.get(key);
				if ("connection".equals(configKey)) {
					String [] chunks = value.split(":");
					if (modbusSlave instanceof ModbusTcpSlave) {
						((ModbusTcpSlave)modbusSlave).setHost(chunks[0]);
						if (chunks.length == 2) {
							((ModbusTcpSlave)modbusSlave).setPort(Integer.valueOf(chunks[1]));							
						}
					} else if (modbusSlave instanceof ModbusSerialSlave) {
						((ModbusSerialSlave)modbusSlave).setPort(chunks[0]);
						if (chunks.length == 2) {
							((ModbusSerialSlave)modbusSlave).setBaud(Integer.valueOf(chunks[1]));							
						}
						
					}
				} else if ("start".equals(configKey)) {
					modbusSlave.setStart(Integer.valueOf(value));
				} else if ("length".equals(configKey)) {
					modbusSlave.setLength(Integer.valueOf(value));
				} else if ("id".equals(configKey)) {
					modbusSlave.setId(Integer.valueOf(value));
				} else if ("type".equals(configKey)) {
					if (ArrayUtils.contains(ModbusBindingProvider.SLAVE_DATA_TYPES, value)) {
						modbusSlave.setType(value);
					}
					else {
						throw new ConfigurationException(configKey,
								"the given slave type '" + value + "' is invalid");
					}
				} else {
					throw new ConfigurationException(configKey,
							"the given configKey '" + configKey + "' is unknown");
				}
			}
		}

		// connect instances to modbus slaves
		for (ModbusSlave slave : Collections.synchronizedMap(modbusSlaves).values()) {
			slave.connect();
		}
	}

}
