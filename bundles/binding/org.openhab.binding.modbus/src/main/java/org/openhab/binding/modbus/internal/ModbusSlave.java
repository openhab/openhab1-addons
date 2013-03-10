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

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Collection;

import net.wimpi.modbus.Modbus;
import net.wimpi.modbus.io.ModbusTCPTransaction;
import net.wimpi.modbus.msg.ModbusRequest;
import net.wimpi.modbus.msg.ModbusResponse;
import net.wimpi.modbus.msg.ReadCoilsRequest;
import net.wimpi.modbus.msg.ReadCoilsResponse;
import net.wimpi.modbus.msg.ReadInputDiscretesRequest;
import net.wimpi.modbus.msg.ReadInputDiscretesResponse;
import net.wimpi.modbus.msg.ReadInputRegistersRequest;
import net.wimpi.modbus.msg.ReadInputRegistersResponse;
import net.wimpi.modbus.msg.ReadMultipleRegistersRequest;
import net.wimpi.modbus.msg.ReadMultipleRegistersResponse;
import net.wimpi.modbus.msg.WriteCoilRequest;
import net.wimpi.modbus.msg.WriteSingleRegisterRequest;
import net.wimpi.modbus.net.TCPMasterConnection;
import net.wimpi.modbus.procimg.InputRegister;
import net.wimpi.modbus.procimg.Register;
import net.wimpi.modbus.util.BitVector;

import org.openhab.binding.modbus.ModbusBindingProvider;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.IncreaseDecreaseType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.OpenClosedType;
import org.openhab.core.library.types.UpDownType;
import org.openhab.core.types.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ModbusSlave class is an abstract class that server as a base class for
 * MobvusTCPSlave and ModbusSerialSlave instantiates physical Modbus slave. 
 * It is responsible for polling data from physical device using appropriate connection.
 * It is also responsible for updating physical devices according to OpenHAB commands  
 *
 * @author Dmitry Krasnov
 * @since 1.1.0
 */
public class ModbusSlave {

	private static final Logger logger = LoggerFactory.getLogger(ModbusSlave.class);
	
	/** name - slave name from cfg file, used for items binding */
	private String name;
	
	/**
	 * Type of data porived by the physical device
	 * "coil" and "discrete" use boolean (bit) values
	 * "input" and "holding" use byte values
	 */
	String type;
	
	/** host address */
	String host;
	
	/** connection port. Default 502 */
	int port = Modbus.DEFAULT_PORT;
	
	/** Modbus slave id */
	int id = 1;
	
	/** starting reference and number of item to fetch from the device */
	int start = 0;
	
	int length = 0;
	
	private TCPMasterConnection connection = null;
	private ModbusTCPTransaction transaction = new ModbusTCPTransaction();
	private Object storage;
	
	
	/**
	 * @param slave slave name from cfg file used for item binding
	 */
	public ModbusSlave(String slave) {
		name = slave;
	}

	/**
	 * writes data to Modbus device corresponding to OpenHAB command
	 * works only with types "coil" and "holding" 
	 * 
	 * @param command OpenHAB command received
	 * @param readRegister data from readRegister are used to define value to write to the device
	 * @param writeRegister register address to write new data to
	 */
	void executeCommand(Command command, int readRegister,
			int writeRegister) {
		if (ModbusBindingProvider.TYPE_COIL.equals(type) || ModbusBindingProvider.TYPE_DISCRETE.equals(type)) {
			setCoil(command, readRegister, writeRegister);
		}
		if (ModbusBindingProvider.TYPE_HOLDING.equals(type)) {
			setRegister(command, readRegister, start + writeRegister);
		}
	}
	
	/**
	 * Calculates boolean value that will be written to the device as a result of OpenHAB command
	 * Used with item bound to "coil" type slaves
	 * 
	 * @param command OpenHAB command received by the item
	 * @return new boolean value to be written to the device
	 */
	protected static boolean translateCommand2Boolean(Command command) {
		if (command.equals(OnOffType.ON))
			return true;
		if (command.equals(OnOffType.OFF))
			return false;
		if (command.equals(OpenClosedType.OPEN))
			return true;
		if (command.equals(OpenClosedType.CLOSED))
			return false;
		throw new IllegalArgumentException("command not supported");
	}

	/**
	 * Performs physical write to device when slave type is "coil"
	 * @param command command received from OpenHAB
	 * @param readRegister reference to the register that stores current value
	 * @param writeRegister register reference to write data to
	 */
	private void setCoil(Command command, int readRegister,
			int writeRegister) {
		synchronized (storage) {
			boolean b = translateCommand2Boolean(command);
			if (((BitVector)storage).getBit(readRegister) != b) {
				if (b) {
					doSetCoil(start + writeRegister, true);
				} else {
					doSetCoil(start + writeRegister, readRegister == writeRegister ? false : true);
				}
			}
		}
	}


	/**
	 * Performs physical write to device when slave type is "holding" using Modbus FC06 function
	 * @param command command received from OpenHAB
	 * @param readRegister reference to the register that stores current value
	 * @param writeRegister register reference to write data to
	 */
	private void setRegister(Command command, int readRegister, int writeRegister) {
		Register newValue = null;
		synchronized (storage) {
			newValue = (Register) ((InputRegister[])storage)[readRegister];
		}
		
		if (command instanceof IncreaseDecreaseType) {
			if (command.equals(IncreaseDecreaseType.INCREASE))
				newValue.setValue(newValue.getValue() + 1);
			else if (command.equals(IncreaseDecreaseType.DECREASE))
				newValue.setValue(newValue.getValue() - 1);
		} else if (command instanceof UpDownType) {
			if (command.equals(UpDownType.UP))
				newValue.setValue(newValue.getValue() + 1);
			else if (command.equals(UpDownType.DOWN))
				newValue.setValue(newValue.getValue() - 1);
		} else if (command instanceof DecimalType) {
			newValue.setValue(((DecimalType)command).intValue());
		}
		
		if (connection == null) { 
			return;
		}
		
		ModbusRequest request = new WriteSingleRegisterRequest(writeRegister, newValue);
		request.setUnitID(id);
		transaction.setRequest(request);
		
		try {
			logger.debug("ModbusSlave: FC" +request.getFunctionCode()+" ref=" + writeRegister + " value=" + newValue.getValue());				
			transaction.execute();
		} catch (Exception e) {
			logger.debug("ModbusSlave:" + e.getMessage());
			return;
		}
	}

	/**
	 * Establishes connection to the device
	 */
	public void connect() {
		try {
			if (connection == null)
				connection = new TCPMasterConnection(InetAddress.getByName(host));
		} catch (UnknownHostException e) {
			connection = null;
			return;
		}
		if (!connection.isConnected())
			try {
				connection.setPort(port);
				connection.connect();
				transaction.setConnection(connection);
				transaction.setReconnecting(false);
			} catch (Exception e) {
				logger.debug("ModbusSlave: Error connecting to master: " + e.getMessage());				
				return;
			}

	}

	/**
	 * @return slave name from cfg file
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sends boolean (bit) data to the device using Modbus FC05 function
	 * @param writeRegister
	 * @param b
	 */
	public void doSetCoil(int writeRegister, boolean b) {
		if (connection == null) 
			return;
		ModbusRequest request = new WriteCoilRequest(writeRegister, b);
		request.setUnitID(id);
		transaction.setRequest(request);
		try {
			logger.debug("ModbusSlave: FC05 ref=" + writeRegister + " value=" + b);				
			transaction.execute();
		} catch (Exception e) {
			logger.debug("ModbusSlave:" + e.getMessage());
			return;
		}
	}

	/**
	 * Reads data from the connected device and updates items with the new data
	 * 
	 * @param binding ModbusBindig that stores providers information
	 */
	public void update(ModbusBinding binding) {

		if (connection == null) 
			return;

		Object local = null;

		if (ModbusBindingProvider.TYPE_COIL.equals(type)) {
			ModbusRequest request = new ReadCoilsRequest(start, length);
			ReadCoilsResponse responce = (ReadCoilsResponse) getModbusData(request);;
			local = responce.getCoils();
		} else if (ModbusBindingProvider.TYPE_DISCRETE.equals(type)) {
			ModbusRequest request = new ReadInputDiscretesRequest(start, length);
			ReadInputDiscretesResponse responce = (ReadInputDiscretesResponse) getModbusData(request);
			local = responce.getDiscretes();
		} else if (ModbusBindingProvider.TYPE_HOLDING.equals(type)) {
			ModbusRequest request = new ReadMultipleRegistersRequest(start, length);
			ReadMultipleRegistersResponse responce = (ReadMultipleRegistersResponse) getModbusData(request);
			local = responce.getRegisters();
		} else if (ModbusBindingProvider.TYPE_INPUT.equals(type)) {
			ModbusRequest request = new ReadInputRegistersRequest(start, length);
			ReadInputRegistersResponse responce = (ReadInputRegistersResponse) getModbusData(request);
			local = responce.getRegisters();
		}
		if (storage == null) 
			storage = local;
		else {
			synchronized(storage) {
				storage = local;
			}
		}
		Collection<String> items = binding.getItemNames();
		for (String item : items) {
			updateItem(binding, item);
		}
	}

	/**
	 * Updates OpenHAB item with data read from slave device
	 * works only for type "coil" and "holding"
	 * @param binding ModbusBinding
	 * @param item item to update
	 */
	private void updateItem(ModbusBinding binding, String item) {
		if (ModbusBindingProvider.TYPE_COIL.equals(type) || ModbusBindingProvider.TYPE_DISCRETE.equals(type)) {
			binding.internalUpdateItem(name, (BitVector)storage,	item);
		}
		if (ModbusBindingProvider.TYPE_HOLDING.equals(type) || ModbusBindingProvider.TYPE_INPUT.equals(type)) {
			binding.internalUpdateItem(name, (InputRegister[])storage, item);
		}
	}
	
	/**
	 * Executes Modbus transaction that reads data from the device and returns response data
	 * @param request describes what data are requested from the device
	 * @return response data
	 */
	private ModbusResponse getModbusData(ModbusRequest request) {
		request.setUnitID(id);
		transaction.setRequest(request);
		
		try {
			transaction.execute();
		} catch (Exception e) {
			logger.debug("ModbusSlave:" + e.getMessage());
			return null;
		}

		ModbusResponse r = transaction.getResponse();
		if (r.getTransactionID() != transaction.getTransactionID()) {
			return null;
		}
		
		return r;
	}

}
