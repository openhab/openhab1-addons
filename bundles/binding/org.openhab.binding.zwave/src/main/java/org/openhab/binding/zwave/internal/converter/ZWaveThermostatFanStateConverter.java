package org.openhab.binding.zwave.internal.converter;

import java.util.Map;

import org.openhab.binding.zwave.internal.converter.state.BigDecimalDecimalTypeConverter;
import org.openhab.binding.zwave.internal.converter.state.ZWaveStateConverter;
import org.openhab.binding.zwave.internal.protocol.SerialMessage;
import org.openhab.binding.zwave.internal.protocol.ZWaveController;
import org.openhab.binding.zwave.internal.protocol.ZWaveNode;
import org.openhab.binding.zwave.internal.protocol.commandclass.ZWaveThermostatFanStateCommandClass;
import org.openhab.binding.zwave.internal.protocol.event.ZWaveCommandClassValueEvent;
import org.openhab.core.events.EventPublisher;
import org.openhab.core.items.Item;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ZWaveThermostatFanStateConverter class. Converter for communication with the 
 * {@link ZWaveThermostatFanStateCommandClass}. Implements polling of the fan
 * state and receiving of fan state events.
 *  @author Dan Cunningham
 *	@since 1.6.0
 */
public class ZWaveThermostatFanStateConverter extends
ZWaveCommandClassConverter<ZWaveThermostatFanStateCommandClass> {

	private static final Logger logger = LoggerFactory.getLogger(ZWaveThermostatFanStateConverter.class);
	private static final int REFRESH_INTERVAL = 0; // refresh interval in seconds for the thermostat setpoint;

	/**
	 * Constructor. Creates a new instance of the {@link ZWaveThermostatFanStateConverter} class.
	 * @param controller the {@link ZWaveController} to use for sending messages.
	 * @param eventPublisher the {@link EventPublisher} to use to publish events.
	 */
	public ZWaveThermostatFanStateConverter(ZWaveController controller,
			EventPublisher eventPublisher) {
		super(controller, eventPublisher);
		this.addStateConverter(new BigDecimalDecimalTypeConverter());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	void executeRefresh(ZWaveNode node,
			ZWaveThermostatFanStateCommandClass commandClass, int endpointId,
			Map<String, String> arguments) {
		logger.debug("NODE {}: Generating poll message for {} endpoint {}", node.getNodeId(), commandClass.getCommandClass().getLabel(), endpointId);
		SerialMessage serialMessage = node.encapsulate(commandClass.getValueMessage(), commandClass, endpointId);

		if (serialMessage == null) {
			logger.warn("NODE {}: Generating message failed for command class = {}, endpoint = {}", node.getNodeId(), commandClass.getCommandClass().getLabel(), endpointId);
			return;
		}

		this.getController().sendData(serialMessage);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	void handleEvent(ZWaveCommandClassValueEvent event, Item item,
			Map<String, String> arguments) {
		ZWaveStateConverter<?,?> converter = this.getStateConverter(item, event.getValue());

		if (converter == null) {
			logger.warn("NODE {}: No converter found for item = {} endpoint = {}, ignoring event.",  event.getNodeId(), item.getName(),event.getEndpoint());
			return;
		}

		State state = converter.convertFromValueToState(event.getValue());
		
		this.getEventPublisher().postUpdate(item.getName(), state);

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	void receiveCommand(Item item, Command command, ZWaveNode node,
			ZWaveThermostatFanStateCommandClass commandClass, int endpointId,
			Map<String, String> arguments) {
			logger.warn("NODE {}: We do not take commands: item = {} endpoint = {}, ignoring.", node.getNodeId(), item.getName(),endpointId);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	int getRefreshInterval() {
		return REFRESH_INTERVAL;
	}

}
