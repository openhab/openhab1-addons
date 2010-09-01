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

import org.openhab.binding.knx.core.config.KNXBindingProvider;
import org.openhab.binding.knx.core.config.KNXTypeMapper;
import org.openhab.binding.knx.core.internal.connection.KNXConnection;
import org.openhab.core.events.AbstractEventSubscriber;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.openhab.core.types.Type;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tuwien.auto.calimero.datapoint.Datapoint;
import tuwien.auto.calimero.exception.KNXException;
import tuwien.auto.calimero.process.ProcessCommunicator;

public class Event2KNXBinding extends AbstractEventSubscriber {
	
	private static final Logger logger = LoggerFactory.getLogger(Event2KNXBinding.class);

	/** to keep track of all KNX binding providers */
	protected Collection<KNXBindingProvider> providers = new HashSet<KNXBindingProvider>();

	/** to keep track of all KNX type mappers */
	protected Collection<KNXTypeMapper> typeMappers = new HashSet<KNXTypeMapper>();

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

	@Override
	public void receiveCommand(String itemName, Command command) {
		ProcessCommunicator pc = KNXConnection.getCommunicator();
		if(pc!=null) {
			Datapoint datapoint = getDatapoint(itemName, command.getClass());
			if(datapoint!=null) {
				try {
					pc.write(datapoint, toDPTValue(command));
				} catch (KNXException e) {
					logger.error("Command could not be sent to the KNX bus!", e);
				}
			}
		}
	}
	
	@Override
	public void receiveUpdate(String itemName, State newState) {
		ProcessCommunicator pc = KNXConnection.getCommunicator();
		if(pc!=null) {
			Datapoint datapoint = getDatapoint(itemName, newState.getClass());
			if(datapoint!=null) {
				try {
					pc.write(datapoint, toDPTValue(newState));
				} catch (KNXException e) {
					logger.error("Update could not be sent to the KNX bus!", e);
				}
			}
		}
	}
	
	private Datapoint getDatapoint(String itemName, Class<? extends Type> typeClass) {
		for(KNXBindingProvider provider : providers) {
			Datapoint datapoint = provider.getDatapoint(itemName, typeClass);
			if(datapoint!=null) return datapoint;
		}
		return null;
	}

	private String toDPTValue(Type type) {
		for(KNXTypeMapper typeMapper : typeMappers) {
			String value = typeMapper.toDPValue(type);
			if(value!=null) return value;
		}
		return null;
	}

}
