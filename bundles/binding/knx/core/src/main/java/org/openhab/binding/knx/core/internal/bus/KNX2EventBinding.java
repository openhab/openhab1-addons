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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.openhab.binding.knx.core.config.KNXBindingProvider;
import org.openhab.binding.knx.core.config.KNXTypeMapper;
import org.openhab.core.events.EventPublisher;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.openhab.core.types.Type;
import org.osgi.service.component.ComponentContext;

import tuwien.auto.calimero.DetachEvent;
import tuwien.auto.calimero.GroupAddress;
import tuwien.auto.calimero.datapoint.CommandDP;
import tuwien.auto.calimero.datapoint.Datapoint;
import tuwien.auto.calimero.datapoint.StateDP;
import tuwien.auto.calimero.process.ProcessEvent;
import tuwien.auto.calimero.process.ProcessListener;

public class KNX2EventBinding implements ProcessListener {

	/** to keep track of all KNX binding providers */
	protected Collection<KNXBindingProvider> providers = new HashSet<KNXBindingProvider>();

	/** to keep track of all KNX type mappers */
	protected Collection<KNXTypeMapper> typeMappers = new HashSet<KNXTypeMapper>();

	private EventPublisher eventPublisher;

	public void activate(ComponentContext componentContext) {
	}
	
	public void deactivate(ComponentContext componentContext) {
		providers.clear();
	}

	public void addKNXBindingProvider(KNXBindingProvider provider) {
		this.providers.add(provider);
	}
	
	public void removeKNXBindingProvider(KNXBindingProvider provider) {
		this.providers.remove(provider);
	}

	public void addKNXTypeMapper(KNXTypeMapper typeMapper) {
		this.typeMappers.add(typeMapper);
	}
	
	public void removeKNXTypeMapper(KNXTypeMapper typeMapper) {
		this.typeMappers.remove(typeMapper);
	}

	public void setEventPublisher(EventPublisher eventPublisher) {
		this.eventPublisher = eventPublisher;
	}
	
	public void unsetEventPublisher(EventPublisher eventPublisher) {
		this.eventPublisher = null;
	}

	public void groupWrite(ProcessEvent e) {
		GroupAddress destination = e.getDestination();
		byte[] asdu = e.getASDU();
		
		for(String itemName : getItemNames(destination)) {
			Datapoint datapoint = getDatapoint(itemName, destination);
			Type type = getType(datapoint, asdu);
			if(datapoint instanceof CommandDP) {
				eventPublisher.postCommand(itemName, (Command) type);
			} else if(datapoint instanceof StateDP) {
				eventPublisher.postUpdate(itemName, (State) type);
			}
		}
	}

	public void detached(DetachEvent e) {
	}
	
	private String[] getItemNames(GroupAddress groupAddress) {
		List<String> itemNames = new ArrayList<String>();
		for(KNXBindingProvider provider : providers) {
			CollectionUtils.addAll(itemNames, provider.getListeningItemNames(groupAddress));
		}
		return itemNames.toArray(new String[itemNames.size()]);
	}

	private Datapoint getDatapoint(String itemName, GroupAddress groupAddress) {
		for(KNXBindingProvider provider : providers) {
			Datapoint datapoint = provider.getDatapoint(itemName, groupAddress);
			if(datapoint!=null) return datapoint;
		}
		return null;
	}

	private Type getType(Datapoint datapoint, byte[] asdu) {
		for(KNXTypeMapper typeMapper : typeMappers) {
			Type type = typeMapper.toType(datapoint, asdu);
			if(type!=null) return type;
		}
		return null;
	}

}
