package org.openhab.binding.knx.core.config;

import org.openhab.core.items.Item;
import org.openhab.core.types.Command;
import org.openhab.core.types.Type;

import tuwien.auto.calimero.datapoint.Datapoint;

public interface KNXConfigProvider {

	public Datapoint getDataPoint(String itemName, Command command);
	
	public Item getItem(Datapoint dataPoint);

	public Type getType(Datapoint dataPoint);

}
