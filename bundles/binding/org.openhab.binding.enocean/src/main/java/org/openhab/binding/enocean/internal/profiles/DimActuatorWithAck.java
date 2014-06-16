package org.openhab.binding.enocean.internal.profiles;

import org.enocean.java.common.Parameter;
import org.enocean.java.common.ParameterAddress;
import org.enocean.java.common.values.AcknowledgementState;
import org.enocean.java.common.values.Value;
import org.openhab.binding.enocean.internal.converter.DimValueConverter;
import org.openhab.binding.enocean.internal.converter.IncreaseDecreasePercentageCommandConverterWithTimeout;
import org.openhab.binding.enocean.internal.converter.OnOffStateConverter;
import org.openhab.binding.enocean.internal.converter.OnToTeachInConverter;
import org.openhab.core.events.EventPublisher;
import org.openhab.core.items.Item;
import org.openhab.core.library.types.IncreaseDecreaseType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.PercentType;
import org.openhab.binding.enocean.internal.converter.DataExchangeInterface;

public class DimActuatorWithAck extends BasicProfile {
	
	private String itemName;
	
	public DimActuatorWithAck(Item item, EventPublisher eventPublisher) {
		super(item, eventPublisher);
		converterFactory.addCommandConverter(Parameter.DIM_VALUE_ACK.name(), IncreaseDecreaseType.class, IncreaseDecreasePercentageCommandConverterWithTimeout.class);
		converterFactory.addStateConverter(Parameter.DIM_VALUE_ACK.name(), PercentType.class, DimValueConverter.class);
		converterFactory.addStateConverter(Parameter.DIM_VALUE_ACK.name(), OnOffType.class, OnOffStateConverter.class);
		converterFactory.addStateConverter(Parameter.TEACH_IN.name(), OnOffType.class, OnToTeachInConverter.class);
		DataExchangeInterface.ackReceived.setAckReceived(item.getName(), false);
		DataExchangeInterface.ackReceived.setEventPublisher(eventPublisher);
		this.itemName = item.getName();
	}

	@Override
	public void valueChanged(ParameterAddress parameterAddress, Value valueObject) {
		if(valueObject.equals(AcknowledgementState.ACK)) {
			DataExchangeInterface.ackReceived.setAckReceived(itemName, true);
		}
	}
}