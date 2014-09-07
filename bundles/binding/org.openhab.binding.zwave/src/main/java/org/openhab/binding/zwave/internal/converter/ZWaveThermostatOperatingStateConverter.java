package org.openhab.binding.zwave.internal.converter;

import java.util.Map;

import org.openhab.binding.zwave.internal.converter.state.BigDecimalDecimalTypeConverter;
import org.openhab.binding.zwave.internal.converter.state.ZWaveStateConverter;
import org.openhab.binding.zwave.internal.protocol.SerialMessage;
import org.openhab.binding.zwave.internal.protocol.ZWaveController;
import org.openhab.binding.zwave.internal.protocol.ZWaveNode;
import org.openhab.binding.zwave.internal.protocol.commandclass.ZWaveThermostatOperatingStateCommandClass;
import org.openhab.binding.zwave.internal.protocol.event.ZWaveCommandClassValueEvent;
import org.openhab.core.events.EventPublisher;
import org.openhab.core.items.Item;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 *  @author Dan Cunningham
 *	@since 1.6.0
 */
public class ZWaveThermostatOperatingStateConverter extends
ZWaveCommandClassConverter<ZWaveThermostatOperatingStateCommandClass> {

	private static final Logger logger = LoggerFactory.getLogger(ZWaveThermostatOperatingStateConverter.class);
	private static final int REFRESH_INTERVAL = 0; // refresh interval in seconds for the thermostat setpoint;

	public ZWaveThermostatOperatingStateConverter(ZWaveController controller,
			EventPublisher eventPublisher) {
		super(controller, eventPublisher);
		this.addStateConverter(new BigDecimalDecimalTypeConverter());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	void executeRefresh(ZWaveNode node,
			ZWaveThermostatOperatingStateCommandClass commandClass, int endpointId,
			Map<String, String> arguments) {
		logger.debug("Generating poll message for {} for node {} endpoint {}", commandClass.getCommandClass().getLabel(), node.getNodeId(), endpointId);
		SerialMessage serialMessage = node.encapsulate(commandClass.getValueMessage(), commandClass, endpointId);

		if (serialMessage == null) {
			logger.warn("Generating message failed for command class = {}, node = {}, endpoint = {}", commandClass.getCommandClass().getLabel(), node.getNodeId(), endpointId);
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
			logger.warn("No converter found for item = {}, node = {} endpoint = {}, ignoring event.", item.getName(), event.getNodeId(), event.getEndpoint());
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
			ZWaveThermostatOperatingStateCommandClass commandClass, int endpointId,
			Map<String, String> arguments) {
		logger.warn("We do not take commands: item = {}, node = {} endpoint = {}, ignoring.", item.getName(), node.getNodeId(), endpointId);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	int getRefreshInterval() {
		return REFRESH_INTERVAL;
	}

}
