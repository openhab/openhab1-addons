/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.zwave.internal.converter;

import java.util.HashMap;
import java.util.Map;

import org.openhab.binding.zwave.internal.converter.state.BooleanOnOffTypeConverter;
import org.openhab.binding.zwave.internal.converter.state.BooleanOpenClosedTypeConverter;
import org.openhab.binding.zwave.internal.converter.state.DateDateTimeTypeConverter;
import org.openhab.binding.zwave.internal.converter.state.IntegerDecimalTypeConverter;
import org.openhab.binding.zwave.internal.converter.state.IntegerPercentTypeConverter;
import org.openhab.binding.zwave.internal.converter.state.StringStringTypeConverter;
import org.openhab.binding.zwave.internal.converter.state.ZWaveStateConverter;
import org.openhab.binding.zwave.internal.protocol.ZWaveController;
import org.openhab.binding.zwave.internal.protocol.ZWaveNode;
import org.openhab.core.events.EventPublisher;
import org.openhab.core.items.Item;
import org.openhab.core.types.State;
import org.openhab.core.types.UnDefType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ZWaveInfoConverter class. Converters between binding items
 * and the Z-Wave API to gather statistic information.
 * @author Jan-Willem Spuij
 * @since 1.4.0
 */
public class ZWaveInfoConverter extends ZWaveConverterBase {

	private static final Logger logger = LoggerFactory.getLogger(ZWaveInfoConverter.class);
	private static final int REFRESH_INTERVAL = 10; // refresh interval in seconds

	/**
	 * Constructor. Creates a new instance of the {@link ZWaveConverterBase}
	 * class.
	 * @param controller the {@link ZWaveController} to use to send messages.
	 * @param eventPublisher the {@link EventPublisher} that can be used to send updates.
	 */
	public ZWaveInfoConverter(ZWaveController controller, EventPublisher eventPublisher) {
		super(controller, eventPublisher);
        
		// State converters used by this converter. 
        this.addStateConverter(new StringStringTypeConverter());
        this.addStateConverter(new DateDateTimeTypeConverter());
		this.addStateConverter(new IntegerDecimalTypeConverter());
		this.addStateConverter(new IntegerPercentTypeConverter());
		this.addStateConverter(new BooleanOnOffTypeConverter());
		this.addStateConverter(new BooleanOpenClosedTypeConverter());
    }

	/**
	 * Execute refresh method. This method is called every time a binding item is
	 * refreshed and information for the corresponding node should be looked up.
	 * @param item the item to refresh.
	 * @param node the {@link ZWaveNode} that is bound to the item.
	 * @param endpointId the endpoint id to send the message.
	 * @param arguments the arguments for the converter.
	 */
	public void executeRefresh(Item item, ZWaveNode node, int endpointId, Map<String,String> arguments) {
		if(item == null)
			return;

		// not bound to an item.
		if (!arguments.containsKey("item"))
			return;

		ZWaveInformationItem informationItem = ZWaveInformationItem.getZWaveBindingAction(arguments.get("item"));
		
		// item not recognized.
		if (informationItem == null) {
			logger.warn("Incorrect information item specified. item name = {}", arguments.get("item"));
			return;
		}
		
        State state = UnDefType.UNDEF;

        // extract the appropriate information value
		Object value = getInformationItemValue(node, informationItem);        
        if (value != null) {        
            ZWaveStateConverter<?,?> converter = this.getStateConverter(item, value);
            
            if (converter == null) {
                logger.warn("No converter found for item = {}, node = {} endpoint = {}, ignoring event.", item.getName(), node.getNodeId(), endpointId);
                return;
            }
            
            state = converter.convertFromValueToState(value);
        }
        
		this.getEventPublisher().postUpdate(item.getName(), state);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	int getRefreshInterval() {
		return REFRESH_INTERVAL;
	}

    private Object getInformationItemValue(ZWaveNode node, ZWaveInformationItem informationItem) {
		switch (informationItem) {
			case REPORT_HOMEID:
				return String.format("0x%08x", node.getHomeId());
			case REPORT_NODEID:
				return node.getNodeId();
			case REPORT_MANUFACTURER:
				return String.format("0x%04x", node.getManufacturer());
			case REPORT_DEVICE_TYPE:
				return String.format("0x%04x", node.getDeviceType());
			case REPORT_DEVICE_ID:
				return String.format("0x%04x", node.getDeviceId());
			case REPORT_BASIC:
				return String.format("0x%02x", node.getDeviceClass().getBasicDeviceClass().getKey());
			case REPORT_BASIC_LABEL:
				return node.getDeviceClass().getBasicDeviceClass().getLabel();
			case REPORT_GENERIC:
				return String.format("0x%02x", node.getDeviceClass().getGenericDeviceClass().getKey());
			case REPORT_GENERIC_LABEL:
				return node.getDeviceClass().getGenericDeviceClass().getLabel();
			case REPORT_SPECIFIC:
				return String.format("0x%02x", node.getDeviceClass().getSpecificDeviceClass().getKey());
			case REPORT_SPECIFIC_LABEL:
				return node.getDeviceClass().getSpecificDeviceClass().getLabel();
			case REPORT_VERSION:
				return node.getVersion();
			case REPORT_ROUTING:
				return node.isRouting();
			case REPORT_LISTENING:
				return node.isListening();
			case REPORT_DEAD:
				return node.isDead();
			case REPORT_NAK:
				return this.getController().getNAKCount();
			case REPORT_SOF:
				return this.getController().getSOFCount();
			case REPORT_CAN:
				return this.getController().getCANCount();
			case REPORT_ACK:
				return this.getController().getACKCount();
			case REPORT_OOF:
				return this.getController().getOOFCount();
			case REPORT_TIME_OUT:
				return this.getController().getTimeOutCount();
			case REPORT_LASTUPDATE:
				return node.getLastUpdated();
		}
        
        return null;
    }
	
	/**
	 * Z-Wave information item enumeration. It Defines the information that can be 
	 * requested by the binding on the item.
	 * 
	 * @author Jan-Willem Spuij
	 * @author Brian Crosby
	 * @since 1.3.0
	 */
	public enum ZWaveInformationItem {
		
		/**
		 * Reports the Home ID.
		 */
		REPORT_HOMEID("HOME_ID"),
		
		/**
		 * Reports the Node ID.
		 */
		REPORT_NODEID("NODE_ID"),
		
		/**
		 * Reports whether the node is listening.
		 */
		REPORT_LISTENING("LISTENING"),

		/**
		 * Reports whether the node is dead or not.
		 */
		REPORT_DEAD("DEAD"),
		
		/**
		 * Reports whether the node is routing Z-Wave messages.
		 */
		REPORT_ROUTING("ROUTING"),
		
		/**
		 * Reports Node version.
		 */
		REPORT_VERSION("VERSION"),
		
		/**
		 * Reports the basic device class of the node (in hex).
		 */
		REPORT_BASIC("BASIC"),
		
		/**
		 * Reports the basic device class of the node (as text).
		 */
		REPORT_BASIC_LABEL("BASIC_LABEL"),
		
		/**
		 * Reports the Generic device class of the node (in hex).
		 */
		REPORT_GENERIC("GENERIC"),
		
		/**
		 * Reports the Generic device class of the node (as text).
		 */
		REPORT_GENERIC_LABEL("GENERIC_LABEL"),
		
		/**
		 * Reports the Specific device class of the node (in hex).
		 */
		REPORT_SPECIFIC("SPECIFIC"),
		
		/**
		 * Reports the Specific device class of the node (as text).
		 */
		REPORT_SPECIFIC_LABEL("SPECIFIC_LABEL"),
		
		/**
		 * Reports the manufacturer of the node.
		 */
		REPORT_MANUFACTURER("MANUFACTURER"),
		
		/**
		 * Reports the Device type of the node (in hex).
		 */
		REPORT_DEVICE_ID("DEVICE_ID"),
		
		/**
		 * Reports the Device type of the node (as text).
		 */
		REPORT_DEVICE_TYPE("DEVICE_TYPE"),
		
		/**
		 * Reports the date / time the node value was last updated.
		 */
		REPORT_LASTUPDATE("LAST_UPDATE"),

		/**
		 * Reports the amount of Start of frames this node has received.
		 */
		REPORT_SOF("SOF"),
		
		/**
		 * Reports the amount of Canceled frames this node has received.
		 */
		REPORT_CAN("CAN"),
		
		/**
		 * Reports the amount of not acknowledged frames this node has received.
		 */
		REPORT_NAK("NAK"),
		
		/**
		 * Reports the amount of out of order frames this node has received.
		 */
		REPORT_OOF("OOF"),
		
		/**
		 * Reports the amount of out of acknowledged frames this node has received.
		 */
		REPORT_ACK("ACK"),
		
		/**
		 * Reports the amount of out of timed out packets this node has received.
		 */
		REPORT_TIME_OUT("TIME_OUT");
		
		private String label;
		private static Map<String, ZWaveInformationItem> labelToZWaveInfoItemMapping;

		private ZWaveInformationItem(String label) {
			this.label = label;
		}
		
		private static void initMapping() {
			labelToZWaveInfoItemMapping = new HashMap<String, ZWaveInformationItem>();
			for (ZWaveInformationItem s : values()) {
				labelToZWaveInfoItemMapping.put(s.label.toLowerCase(), s);
			}
		}

		/**
		 * Returns the label of the ZWaveInformationItem enumeration
		 * @return the label
		 */
		public String getLabel() {
			return label;
		}
		
		/**
		 * Lookup function based on the binding information label.
		 * Returns null if the binding information item is not found.
		 * @param label the label to lookup
		 * @return enumeration value of the binding information item.
		 */
		public static ZWaveInformationItem getZWaveBindingAction(String label) {
			if (labelToZWaveInfoItemMapping == null) {
				initMapping();
			}
			return labelToZWaveInfoItemMapping.get(label.toLowerCase());
		}
		
	}
}
