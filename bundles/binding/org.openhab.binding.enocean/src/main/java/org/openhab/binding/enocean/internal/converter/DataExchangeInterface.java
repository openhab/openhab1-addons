package org.openhab.binding.enocean.internal.converter;

import org.openhab.binding.enocean.internal.converter.WaitForAckState;
import org.openhab.binding.enocean.internal.converter.CurrentItem;
public interface DataExchangeInterface {
	WaitForAckState ackReceived = new WaitForAckState();
	CurrentItem currentItem = new CurrentItem();
}
