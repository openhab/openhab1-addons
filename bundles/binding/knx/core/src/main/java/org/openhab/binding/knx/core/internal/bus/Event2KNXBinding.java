package org.openhab.binding.knx.core.internal.bus;

import java.util.Collection;
import java.util.HashSet;

import org.openhab.binding.knx.core.config.KNXConfigProvider;
import org.openhab.binding.knx.core.internal.connection.KNXConnection;
import org.openhab.core.events.AbstractEventSubscriber;
import org.openhab.core.items.ItemChangeListener;
import org.openhab.core.items.ItemProvider;
import org.openhab.core.types.Command;
import org.osgi.service.component.ComponentContext;

import tuwien.auto.calimero.GroupAddress;
import tuwien.auto.calimero.datapoint.CommandDP;
import tuwien.auto.calimero.datapoint.Datapoint;
import tuwien.auto.calimero.process.ProcessCommunicator;

public class Event2KNXBinding extends AbstractEventSubscriber {

	/** to keep track of all KNX config providers */
	protected Collection<KNXConfigProvider> providers = new HashSet<KNXConfigProvider>();

	public void activate(ComponentContext componentContext) {
	}
	
	public void deactivate(ComponentContext componentContext) {
		providers.clear();
	}

	public void addKNXConfigProvider(KNXConfigProvider provider) {
		this.providers.add(provider);
	}
	
	public void removeKNXConfigProvider(KNXConfigProvider provider) {
		this.providers.remove(provider);
	}
	
	@Override
	public void receiveCommand(String itemName, Command command) {
		ProcessCommunicator pc = KNXConnection.getCommunicator();
		if(pc!=null) {
			Datapoint dataPoint = getDataPoint(itemName, command);
			if(dataPoint!=null) {
				pc.write(dataPoint, command.toString());
		}
	}
	
	private Datapoint getDataPoint(String itemName, Command command) {
		for(KNXConfigProvider provider : providers) {
			Datapoint dataPoint = provider.getDataPoint(itemName, command);
			if(dataPoint!=null) return dataPoint;
		}
		return null;
	}

	private Datapoint getDataPoint(String itemName, Command command) {
		for(KNXConfigProvider provider : providers) {
			Datapoint dataPoint = provider.getDataPoint(itemName, command);
			if(dataPoint!=null) return dataPoint;
		}
		return null;
	}
}
