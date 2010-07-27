/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010, openHAB.org <admin@openhab.org>
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

package org.openhab.binding.knx.core.internal.bus;

import java.util.Collection;
import java.util.HashSet;

import org.openhab.binding.knx.core.config.KNXConfigProvider;
import org.openhab.binding.knx.core.internal.connection.KNXConnection;
import org.openhab.core.events.AbstractEventSubscriber;
import org.openhab.core.types.Command;
import org.osgi.service.component.ComponentContext;

import tuwien.auto.calimero.datapoint.Datapoint;
import tuwien.auto.calimero.exception.KNXException;
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
				try {
					pc.write(dataPoint, command.toString());
				} catch (KNXException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	private Datapoint getDataPoint(String itemName, Command command) {
		for(KNXConfigProvider provider : providers) {
			Datapoint dataPoint = provider.getDataPoint(itemName, command);
			if(dataPoint!=null) return dataPoint;
		}
		return null;
	}
}
