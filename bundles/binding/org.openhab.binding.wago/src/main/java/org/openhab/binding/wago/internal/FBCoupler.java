/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.wago.internal;

import java.io.InputStream;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.util.Collection;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import net.wimpi.modbus.io.ModbusTCPTransaction;
import net.wimpi.modbus.msg.ModbusRequest;
import net.wimpi.modbus.msg.ReadCoilsRequest;
import net.wimpi.modbus.msg.ReadCoilsResponse;
import net.wimpi.modbus.msg.ReadMultipleRegistersRequest;
import net.wimpi.modbus.msg.ReadMultipleRegistersResponse;
import net.wimpi.modbus.msg.WriteCoilRequest;
import net.wimpi.modbus.msg.WriteMultipleRegistersRequest;
import net.wimpi.modbus.net.TCPMasterConnection;
import net.wimpi.modbus.procimg.Register;
import net.wimpi.modbus.procimg.SimpleRegister;

import org.openhab.binding.wago.internal.WagoGenericBindingProvider.WagoBindingConfig;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.IncreaseDecreaseType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.OpenClosedType;
import org.openhab.core.library.types.UpDownType;
import org.openhab.core.types.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class represents the wago-field-bus-coupler with all of its modules and
 * configurations.
 * 
 * @author Kaltofen
 * @since 1.7.0
 */
public class FBCoupler {
	
	private static final Logger logger = LoggerFactory.getLogger(FBCoupler.class);
	
	final int DIStart = 0; // Start of the input-coils
	final int DOStart = 512; // Start of the output-coils
	final int IRStart = 0; // Start of the input-registers
	final int ORStart = 512; // Start of the output-registers

	String name;

	TCPMasterConnection connection;
	ModbusTCPTransaction transaction;

	String ip;
	int modbusPort = 502;
	int ftpPort = 21;
	String username = "user";
	String password = "user";

	Module modules[];

	public void setIp(String ip) {
		this.ip = ip;
	}

	public void setModbus(int port) {
		modbusPort = port;
	}

	public void setFTP(int port) {
		ftpPort = port;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setup() {
		try {
			URL url = new URL("ftp://" + username + ":" + password + "@" + ip
					+ ":" + ftpPort + "/etc/EA-config.xml;type=i");
			URLConnection urlc = url.openConnection();
			InputStream is = urlc.getInputStream();

			DocumentBuilderFactory dbFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(is);
			doc.getDocumentElement().normalize();

			NodeList moduleList = doc.getElementsByTagName("Module");

			modules = new Module[moduleList.getLength()];
			for (int i = 0; i < moduleList.getLength(); i++) {
				Node moduleXML = moduleList.item(i);
				NamedNodeMap attributes = moduleXML.getAttributes();

				String article = attributes.getNamedItem("ARTIKELNR")
						.getNodeValue();
				String type = attributes.getNamedItem("MODULETYPE")
						.getNodeValue();
				int channelcount = Integer.parseInt(attributes.getNamedItem(
						"CHANNELCOUNT").getNodeValue());

				// int DIOffset = DIStart;
				int DOOffset = DOStart;
				// int IROffset = IRStart;
				int OROffset = ORStart;
				if (type.equals("DO")) {
					modules[i] = new DOModule(article, DOOffset, channelcount);
					DOOffset += channelcount;
				} else if (type.equals("COMPLEX")) {
					if (article.equals("750-511/000-000")) { // PWM-Module
						modules[i] = new PWMModule(article, OROffset);
						OROffset += 4; // 4 registers for 2 outputs
					}
				}
			}
		} catch (Exception e) {
			logger.warn("wago-coupler setup failed.");
		}
	}

	public boolean connect() {
		try {
			if (connection == null) {
				connection = new TCPMasterConnection(InetAddress.getByName(ip));
			}
		} catch (UnknownHostException e) {
			logger.warn("unable to connect to wago-coupler.");
			return false;
		}

		if (!connection.isConnected()) {
			try {
				connection.setPort(modbusPort);
				connection.connect();
				if (transaction == null)
					transaction = new ModbusTCPTransaction();
				transaction.setConnection(connection);
				transaction.setReconnecting(false);
			} catch (Exception e) {
				logger.warn("unable to connect to wago-coupler: "
						+ e.getMessage());
				return false;
			}
		}
		return true;
	}

	public FBCoupler(String name) {
		this.name = name;
	}

	public void executeCommand(Command command, WagoBindingConfig conf) {
		Module module = modules[conf.module];
		if (module != null) {
			module.executeCommand(command, conf.channel);
		} else {
			logger.warn("module " + module + " wasn't correctly initialized.");
		}
	}

	public void update(WagoBinding binding) {
		int i = 0;
		for (Module module : modules) {
			if (module != null) {
				module.update(binding, name, i);
			}
			i++;
		}
	}

	class Module {
		String article;
		String type;
		int offset;
		int chancount;

		public String getArticle() {
			return article;
		}

		public String getType() {
			return type;
		}

		public int getChannelcount() {
			return chancount;
		}

		public void update(WagoBinding binding, String couplerName, int module) {
		}

		public void executeCommand(Command command, int channel) {
		}

		Module(String article, String type, int offset, int chancount) {
			this.article = article;
			this.type = type;
			this.offset = offset;
			this.chancount = chancount;
		}
	}

	class DOModule extends Module {
		boolean state[];

		DOModule(String article, int offset, int chancount) {
			super(article, "DO", offset, chancount);
			state = new boolean[chancount];
		}

		public void update(WagoBinding binding, String couplerName, int module) {
			if (!connect()) {
				logger.warn("coupler not connected.");
				return;
			}

			ModbusRequest request = new ReadCoilsRequest(offset, chancount);
			transaction.setRequest(request);
			try {
				transaction.execute();
			} catch (Exception e) {
				logger.debug("update of channels failed: " + e.getMessage());
				return;
			}

			ReadCoilsResponse response = (ReadCoilsResponse) transaction
					.getResponse();
			if ((response.getTransactionID() != transaction.getTransactionID())
					&& !response.isHeadless()) {
				logger.debug("update of channels failed: invalid response.");
				return;
			}

			for (int i = 0; i < chancount; i++) {
				state[i] = response.getCoils().getBit(i);
			}

			Collection<String> itemNames = binding.getItemNames();
			for (String itemName : itemNames) {
				binding.updateItem(itemName, couplerName, module, state);
			}
		}

		private boolean translateCommand2Boolean(Command command) {
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

		public void executeCommand(Command command, int channel) {
			if (!connect()) {
				logger.warn("coupler not connected.");
				return;
			}

			try {
				if (translateCommand2Boolean(command)) {
					switchON(channel);
				} else {
					switchOFF(channel);
				}

			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			}
		}

		private void setCoil(int channel, boolean state) {
			ModbusRequest request = new WriteCoilRequest(offset + channel,
					state);
			transaction.setRequest(request);
			try {
				transaction.execute();
			} catch (Exception e) {
				logger.debug("can't set channel " + channel
						+ " of digital output.");
				return;
			}
		}

		private void switchON(int channel) {
			if (state[channel] != true) {
				setCoil(channel, true);
				state[channel] = true;
				logger.debug("switching channel " + channel + " on.");
			}
		}

		private void switchOFF(int channel) {
			if (state[channel] != false) {
				setCoil(channel, false);
				state[channel] = false;
				logger.debug("switching channel " + channel + " off.");
			}
		}

		public boolean getState(int channel) {
			return state[channel];
		}
	}

	class PWMModule extends Module {
		int values[];

		public PWMModule(String article, int offset) {
			super(article, "COMPLEX", offset, 2);
			values = new int[2];
		}

		public void update(WagoBinding binding, String couplerName, int module) {
			if (!connect()) {
				logger.warn("coupler not connected.");
				return;
			}

			ModbusRequest request = new ReadMultipleRegistersRequest(offset, 4);
			transaction.setRequest(request);
			try {
				transaction.execute();
			} catch (Exception e) {
				logger.debug("update of channels failed: " + e.getMessage());
				return;
			}

			ReadMultipleRegistersResponse response = (ReadMultipleRegistersResponse) transaction
					.getResponse();
			if ((response.getTransactionID() != transaction.getTransactionID())
					&& !response.isHeadless()) {
				logger.debug("update of channels failed: invalid response.");
				return;
			}

			values[0] = response.getRegister(1).getValue() >> 4;
			values[1] = response.getRegister(3).getValue() >> 4;

			Collection<String> itemNames = binding.getItemNames();
			for (String itemName : itemNames) {
				binding.updateItemPWM(itemName, couplerName, module, values);
			}
		}

		public void executeCommand(Command command, int channel) {
			if (!connect()) {
				logger.warn("coupler not connected.");
				return;
			}

			if (command instanceof IncreaseDecreaseType
					|| command instanceof UpDownType) {
				int value = (int) ((float) values[channel] / 1023 * 100);
				if (command.equals(command
						.equals(IncreaseDecreaseType.INCREASE))
						|| command.equals(UpDownType.UP)) {
					value += 1;
				} else if (command.equals(IncreaseDecreaseType.DECREASE)
						|| command.equals(UpDownType.DOWN)) {
					value -= 1;
				}
				value = (int) ((float) value / 100 * 1023);
				if (value > 1023)
					value = 1023;
				else if (value < 0)
					value = 0;

				setValue(channel, value);
			} else if (command instanceof OnOffType) {
				if (command.equals(OnOffType.ON)) {
					setValue(channel, 1023);
				} else if (command.equals(OnOffType.OFF)) {
					setValue(channel, 0);
				}
			} else if (command instanceof DecimalType) {
				DecimalType percentage = (DecimalType) command;
				int value = (int) ((float) percentage.intValue() / 100 * 1023);
				setValue(channel, value);
			}
		}

		public void setValue(int channel, int value) {
			values[channel] = value;

			Register reg[] = new SimpleRegister[2];
			reg[0] = new SimpleRegister(0); // Must be set to 0
			reg[1] = new SimpleRegister(value << 4);// << 20); // 4 + 16 = 20

			ModbusRequest request = new WriteMultipleRegistersRequest(offset
					+ channel * 2, reg);
			transaction.setRequest(request);
			try {
				transaction.execute();
			} catch (Exception e) {
				logger.debug("can't set channel " + channel + " of PWM module.");
				return;
			}
		}

		public int getValue(int channel) {
			return values[channel];
		}
	}
}
