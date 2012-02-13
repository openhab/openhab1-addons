/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010-2012, openHAB.org <admin@openhab.org>
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
package org.openhab.core.binding.autoupdate.internal;

import org.openhab.core.binding.autoupdate.AutoUpdateBindingProvider;
import org.openhab.core.events.AbstractEventSubscriberBinding;
import org.openhab.core.events.EventPublisher;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
	

/**
 * <p>The AutoUpdate-Binding is no 'normal' binding as it doesn't connect any hardware
 * to openHAB. In fact it takes care of updating the State of an item with respect
 * to the received command automatically or not. By default the State is getting
 * updated automatically which is desired behavior in most of the cases. However
 * it could be useful to disable this default behavior.</p>
 * <p>For example when implementing validation steps before changing a State one
 * needs to control the State-Update himself.</p>
 * <p><b>Note:</b>When autoupdate is disabled the administrator is responsible
 * for sending the proper State updates himself</p> 
 * 
 * @author Thomas.Eichstaedt-Engelen
 * @since 0.9.1
 */
public class AutoUpdateBinding extends AbstractEventSubscriberBinding<AutoUpdateBindingProvider> {

	private static final Logger logger = LoggerFactory.getLogger(AutoUpdateBinding.class);
	
	protected EventPublisher eventPublisher = null;
	
	
	public void setEventPublisher(EventPublisher eventPublisher) {
		this.eventPublisher = eventPublisher;
	}

	public void unsetEventPublisher(EventPublisher eventPublisher) {
		this.eventPublisher = null;
	}
	
	
	/**
	 * @{inheritDoc}
	 */
	@Override
	public void receiveCommand(String itemName, Command command) {
		for (AutoUpdateBindingProvider provider : providers) {
			boolean autoUpdate = provider.autoUpdate(itemName);
			if (autoUpdate && command instanceof State) {
				eventPublisher.postUpdate(itemName, (State) command);
			} else {
				logger.trace("Item '{}' is not configured to updated it's State automatically -> please update State manually");
			}
		}
	}
	
}
