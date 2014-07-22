/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.modbus.internal;

import java.util.Collection;

import net.wimpi.modbus.io.ModbusTransaction;
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
import net.wimpi.modbus.msg.WriteMultipleRegistersRequest;
import net.wimpi.modbus.msg.WriteSingleRegisterRequest;
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
public abstract class ModbusSlave implements ModbusSlaveConnection {

	private static final Logger logger = LoggerFactory.getLogger(ModbusSlave.class);

	/** name - slave name from cfg file, used for items binding */
	protected String name = null;
	
	private static boolean writeMultipleRegisters = false;
	
	public static void setWriteMultipleRegisters(boolean setwmr) {
		writeMultipleRegisters = setwmr;
	}

	/**
	 * Type of data porived by the physical device
	 * "coil" and "discrete" use boolean (bit) values
	 * "input" and "holding" use byte values
	 */
	private String type;

	/** Modbus slave id */
	private int id = 1;

	/** starting reference and number of item to fetch from the device */
	private int start = 0;

	private int length = 0;

	private Object storage;
	protected ModbusTransaction transaction = null; 



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
		if (ModbusBindingProvider.TYPE_COIL.equals(getType()) || ModbusBindingProvider.TYPE_DISCRETE.equals(getType())) {
			setCoil(command, readRegister, writeRegister);
		}
		if (ModbusBindingProvider.TYPE_HOLDING.equals(getType())) {
			setRegister(command, readRegister, getStart() + writeRegister);
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
					doSetCoil(getStart() + writeRegister, true);
				} else {
					doSetCoil(getStart() + writeRegister, readRegister == writeRegister ? false : true);
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
	protected void setRegister(Command command, int readRegister, int writeRegister) {
		
		if (!isConnected()) { 
			return;
		}

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
		} else if (command instanceof OnOffType) {
			if (command.equals(OnOffType.ON))
				newValue.setValue(1);
			else if (command.equals(OnOffType.OFF))
				newValue.setValue(0);
		}

		
		ModbusRequest request = null;
		if (writeMultipleRegisters) {
			Register [] regs = new Register[1];
			regs[0] = newValue;
			request = new WriteMultipleRegistersRequest(writeRegister, regs);			
		} else {
			request = new WriteSingleRegisterRequest(writeRegister, newValue);
		}
		request.setUnitID(getId());
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
		if (!connect()) {
			logger.info("ModbusSlave not connected");
			return;
		}
		ModbusRequest request = new WriteCoilRequest(writeRegister, b);
		request.setUnitID(getId());
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
		if (!connect()) {
			resetConnection();
			logger.info("ModbusSlave not connected");
			return;
		}
		
		try {

		Object local = null;


			if (ModbusBindingProvider.TYPE_COIL.equals(getType())) {
				ModbusRequest request = new ReadCoilsRequest(getStart(), getLength());
				if (this instanceof ModbusSerialSlave) {
					request.setHeadless();
				}
				request.setUnitID(id);
				ReadCoilsResponse responce = (ReadCoilsResponse) getModbusData(request);
				local = responce.getCoils();
			} else if (ModbusBindingProvider.TYPE_DISCRETE.equals(getType())) {
				ModbusRequest request = new ReadInputDiscretesRequest(getStart(), getLength());
				ReadInputDiscretesResponse responce = (ReadInputDiscretesResponse) getModbusData(request);
				local = responce.getDiscretes();
			} else if (ModbusBindingProvider.TYPE_HOLDING.equals(getType())) {
				ModbusRequest request = new ReadMultipleRegistersRequest(getStart(), getLength());
				ReadMultipleRegistersResponse responce = (ReadMultipleRegistersResponse) getModbusData(request);
				local = responce.getRegisters();
			} else if (ModbusBindingProvider.TYPE_INPUT.equals(getType())) {
				ModbusRequest request = new ReadInputRegistersRequest(getStart(), getLength());
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
		} catch (Exception e) {
			resetConnection();
			logger.info("ModbusSlave error getting responce from slave");
		}

	}

	/**
	 * Updates OpenHAB item with data read from slave device
	 * works only for type "coil" and "holding"
	 * @param binding ModbusBinding
	 * @param item item to update
	 */
	private void updateItem(ModbusBinding binding, String item) {
		if (ModbusBindingProvider.TYPE_COIL.equals(getType()) || ModbusBindingProvider.TYPE_DISCRETE.equals(getType())) {
			binding.internalUpdateItem(name, (BitVector)storage,	item);
		}
		if (ModbusBindingProvider.TYPE_HOLDING.equals(getType()) || ModbusBindingProvider.TYPE_INPUT.equals(getType())) {
			binding.internalUpdateItem(name, (InputRegister[])storage, item);
		}
	}

	/**
	 * Executes Modbus transaction that reads data from the device and returns response data
	 * @param request describes what data are requested from the device
	 * @return response data
	 */
	private ModbusResponse getModbusData(ModbusRequest request) {
		request.setUnitID(getId());
		transaction.setRequest(request);

		try {
			transaction.execute();
		} catch (Exception e) {
			logger.debug("ModbusSlave:" + e.getMessage());
			return null;
		}

		ModbusResponse r = transaction.getResponse();
		if ((r.getTransactionID() != transaction.getTransactionID()) && !r.isHeadless()) {
			return null;
		}

		return r;
	}

	int getStart() {
		return start;
	}

	void setStart(int start) {
		this.start = start;
	}

	int getLength() {
		return length;
	}

	void setLength(int length) {
		this.length = length;
	}

	int getId() {
		return id;
	}

	void setId(int id) {
		this.id = id;
	}

	String getType() {
		return type;
	}

	void setType(String type) {
		this.type = type;
	}

}
