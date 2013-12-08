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
package org.openhab.binding.k8055.internal;

import java.util.Dictionary;

import org.openhab.binding.k8055.k8055BindingProvider;
import org.openhab.binding.k8055.internal.k8055GenericBindingProvider.k8055BindingConfig;

import org.apache.commons.lang.StringUtils;
import org.openhab.core.binding.AbstractActiveBinding;
import org.openhab.core.library.items.ContactItem;
import org.openhab.core.library.items.DimmerItem;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.OpenClosedType;
import org.openhab.core.library.types.PercentType;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.NativeLibrary; 


/**
 * K8055 Binding that polls the USB Hardware
 * 
 * @author Anthony Green
 * @since 1.3.0
 */
public class k8055Binding extends AbstractActiveBinding<k8055BindingProvider> implements ManagedService {

	private static final Logger logger = 
			LoggerFactory.getLogger(k8055Binding.class);

	/**
	 * Indicates whether this binding is properly configured which means all
	 * necessary configurations are set. Only Bindings which are properly
	 * configured get's started and will call the execute method though.
	 */
	private boolean isProperlyConfigured = false;

	/** 
	 * the refresh interval which is used to poll values from the k8055
	 * server (optional, defaults to 1000ms)
	 */
	private long refreshInterval = 1000;

	// Details of the available hardware
	static final int NUM_DIGITAL_INPUTS = 5;
	static final int NUM_DIGITAL_OUTPUTS = 8;
	static final int NUM_ANALOG_INPUTS = 2;
	static final int NUM_ANALOG_OUTPUTS = 2;

	static LibK8055 sysLibrary;	
	int boardNo = 0;

	/**
	 * Previously read digital input state
	 */
	long lastDigitalInputs = -1;
	
	/**
	 * Are we currently connected to the hardware?
	 */
	boolean connected;	

	public k8055Binding() {
	}

	protected boolean connect () {
		if (!connected) {
			if (sysLibrary.OpenDevice(boardNo) == boardNo) {
				connected = true;
				
				// We don't really know the existing state - so this results in the state of all inputs being republished
				lastDigitalInputs = -1;
				
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				logger.info("K8055: Connect to board: " + boardNo + " succeeeded.");
			} else {
				logger.error("K8055: Connect to board: " + boardNo + " failed.");
			}
		};

		return connected;
	}

	public void activate() {
		logger.debug("activate() method is called!");

		try {
			if (sysLibrary == null ) {
				logger.debug("Loading native code library...");
				sysLibrary = (LibK8055) Native.synchronizedLibrary((Library) Native.loadLibrary("k8055", LibK8055.class));
				logger.debug("Done loading native code library");
			}
		} catch (Exception e) {
			logger.error("Failed to load K8055 native library " + e.getMessage(), e);
			isProperlyConfigured = false;
		}
		connected = false;
		connect();
		logger.debug("activate() method completed!");
	}

	public void deactivate() {
		logger.debug("deactivate() method is called!");
		if (sysLibrary != null) {
			try {
				sysLibrary.CloseDevice();
			} catch (Exception e) {
				logger.warn("Failed to close connection to hardware", e);
			}
		}
		sysLibrary = null;
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
		return "k8055 Refresh Service";
	}

	/**
	 * @{inheritDoc}
	 */
	@Override
	public boolean isProperlyConfigured() {
		return isProperlyConfigured;
	}


	/**
	 * @{inheritDoc}
	 */
	@Override
	protected void execute() {
		logger.debug("execute() method is called!");
		try {
			if (!connect()) { 
				logger.error("driver.K8055.accessHardware():  Unable to access hardware.");
				return;
			}

			// Read all of the digital inputs in one go
			long inputs = sysLibrary.ReadAllDigital();
			
			if (inputs < 0){throw new Exception("Connection failure");}

			for (k8055BindingProvider provider : this.providers) {
				for (String itemName : provider.getItemNames()) {
					k8055BindingConfig config = provider.getItemConfig(itemName);
					if (IOType.DIGITAL_IN.equals(config.ioType)){
						boolean newstate = (inputs & (1 << (config.ioNumber - 1))) != 0;
						boolean oldstate = (lastDigitalInputs & (1 << (config.ioNumber - 1))) != 0;
						
						// For quick response times a very short poll interval is required.  To avoid flooding
						// masses of events to the event bus, just post updates when changes occur (or when first connecting to hardware)
						if (lastDigitalInputs < 0 || newstate != oldstate) {
							if (newstate) {
								eventPublisher.postUpdate(itemName, OpenClosedType.CLOSED);
							} else {
								eventPublisher.postUpdate(itemName, OpenClosedType.OPEN);
							}
						}
					} else if (IOType.ANALOG_IN.equals(config.ioType)) {
						int state = sysLibrary.ReadAnalogChannel(config.ioNumber);
						eventPublisher.postUpdate(itemName, new DecimalType(state));
					}
				}
			}
			
			lastDigitalInputs = inputs;
			

		} catch (Exception e){
			// Connection failure
			logger.error("K8055 Connection failure");
			connected = false;
			sysLibrary.CloseDevice();
		}

	}

	/**
	 * @{inheritDoc}
	 */
	@Override
	protected void internalReceiveCommand(String itemName, Command command) {
		logger.debug("Received command for: " + itemName + " - " + command);
		k8055BindingConfig itemConfig = getConfigForItemName(itemName);

		if (itemConfig.ioType == IOType.DIGITAL_OUT) {
			logger.debug("Updating hardware Digital output: " + itemConfig.ioNumber);
			if (connect()) {
				if (OnOffType.ON.equals(command)) {
					sysLibrary.SetDigitalChannel(itemConfig.ioNumber);
				} else if (OnOffType.OFF.equals(command)) {
					sysLibrary.ClearDigitalChannel(itemConfig.ioNumber);
				} else {
					logger.error("Received unknown command: " + command + " for item: " + itemName);
				}
			} else {
				logger.error("Not connected to hardware.  Command: " + command + " for item: " + itemName + " ignored");
			}
		} else if (itemConfig.ioType == IOType.ANALOG_OUT) {
			logger.debug("Updating hardware Analog output: " + itemConfig.ioNumber);
			// TODO: Implement Increase/decrease commands
			if (connect()) {
				if (OnOffType.ON.equals(command)) {
					sysLibrary.SetAnalogChannel(itemConfig.ioNumber);
					
				} else if (OnOffType.OFF.equals(command)) {
					sysLibrary.ClearAnalogChannel(itemConfig.ioNumber);
					
				} else if (command instanceof PercentType) {
					// Convert 0-100% to 0-255
					int value = Math.round((((PercentType)command ).shortValue()*255)/100);
					sysLibrary.OutputAnalogChannel(itemConfig.ioNumber, value);
					
				} else if (command instanceof DecimalType) {
					// Force number into 0-255 range.
					sysLibrary.OutputAnalogChannel(itemConfig.ioNumber, Math.min(Math.max(((DecimalType)command).intValue(),0),255));
				} else {
					logger.error("Received unknown command: " + command + " for item: " + itemName);
				}
			} else {
				logger.error("Not connected to hardware.  Command: " + command + " for item: " + itemName + " ignored");
			}
		}



		// the code being executed when a command was sent on the openHAB
		// event bus goes here. This method is only called if one of the 
		// BindingProviders provide a binding for the given 'itemName'.
		logger.debug("internalReceiveCommand() is called!");
	}

	/**
	 * @{inheritDoc}
	 */
	@Override
	protected void internalReceiveUpdate(String itemName, State newState) {
		// the code being executed when a state was sent on the openHAB
		// event bus goes here. This method is only called if one of the 
		// BindingProviders provide a binding for the given 'itemName'.
		logger.debug("internalReceiveUpdate() is called!");
	}





	/**
	 * @{inheritDoc}
	 */
	@Override
	public void updated(Dictionary<String, ?> config) throws ConfigurationException {
		logger.debug("updated() called");
		if (config != null) {

			// to override the default refresh interval one has to add a 
			// parameter to openhab.cfg like <bindingName>:refresh=<intervalInMs>
			String refreshIntervalString = (String) config.get("refresh");
			if (StringUtils.isNotBlank(refreshIntervalString)) {
				refreshInterval = Long.parseLong(refreshIntervalString);
			}

			String boardNum = (String) config.get("boardno");
			if (StringUtils.isNotBlank(boardNum)) {
				try {
					boardNo = Integer.parseInt(boardNum);
				} catch (NumberFormatException e) {
					logger.error("Invalid board number: " + boardNum);
					throw new ConfigurationException("boardno", boardNum + " is not a valid board number.");
				}
			}

			isProperlyConfigured = true;
		}
	}

	/**
	 * Lookup of the configuration of the named item.
	 * 
	 * @param itemName
	 *            The name of the item.
	 * @return The configuration, null otherwise.
	 */
	private k8055BindingConfig getConfigForItemName(String itemName) {
		for (k8055BindingProvider provider : this.providers) {
			if (provider.getItemConfig(itemName) != null) {
				return provider.getItemConfig(itemName);
			}
		}
		return null;
	}

	/**
	 * Describes the interface of the K8055 C Library.  (Used by JNA)
	 * 
	 * @author anthony
	 *
	 */	

	public interface LibK8055 extends Library {
		/* prototypes */
		int OpenDevice(int board_address);
		int CloseDevice();
		int ReadAnalogChannel(int Channelno);
		int ReadAllAnalog(int[] data1, int[] data2);
		int OutputAnalogChannel(int channel, int data);
		int OutputAllAnalog(int data1,int data2);
		int ClearAllAnalog();
		int ClearAnalogChannel(int channel);
		int SetAnalogChannel(int channel);
		int SetAllAnalog();
		int WriteAllDigital(int data);
		int ClearDigitalChannel(int channel);
		int ClearAllDigital();
		int SetDigitalChannel(int channel);
		int SetAllDigital();
		int ReadDigitalChannel(int channel);
		long ReadAllDigital();
		int ResetCounter(int counternr);
		long ReadCounter(int counterno);
		int SetCounterDebounceTime(int counterno, int debouncetime);
	}

}
