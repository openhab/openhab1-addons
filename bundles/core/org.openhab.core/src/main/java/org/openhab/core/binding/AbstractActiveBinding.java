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
package org.openhab.core.binding;

import java.util.Collection;
import java.util.HashSet;

import org.openhab.core.events.EventPublisher;
import org.openhab.core.service.AbstractActiveService;


/**
 * Base class for active bindings which polls something and send events 
 * frequently.
 * 
 * @author Thomas.Eichstaedt-Engelen
 * @since 0.6.0
 */
public abstract class AbstractActiveBinding<P extends BindingProvider> extends AbstractActiveService implements BindingChangeListener {

	/** to keep track of all binding providers */
	protected Collection<P> providers = new HashSet<P>();
	
	protected EventPublisher eventPublisher = null;
	
	public void setEventPublisher(EventPublisher eventPublisher) {
		this.eventPublisher = eventPublisher;
	}

	public void unsetEventPublisher(EventPublisher eventPublisher) {
		this.eventPublisher = null;
	}

	/**
	 * Adds <code>provider</code> to the list of {@link BindingProvider}s and 
	 * adds <code>this</code> as {@link BindingChangeListener}. If 
	 * <code>provider</code> contains any binding an the refresh-Thread is
	 * stopped it will be started.
	 * 
	 * @param provider the new {@link BindingProvider} to add
	 */
	public void addBindingProvider(P provider) {
		this.providers.add(provider);
		provider.addBindingChangeListener(this);
		if (provider.providesBinding()) {
			start();
		}		
	}

	/**
	 * Removes <code>provider</code> from the list of providers. If there is no
	 * provider left the refresh thread is getting interrupted.
	 * 
	 * @param provider the {@link BindingProvider} to remove
	 */
	public void removeBindingProvider(P provider) {
		this.providers.remove(provider);
		
		// if there are no binding providers there is no need to run this 
		// refresh thread any longer ...
		if (this.providers.size() == 0) {
			interrupt();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void bindingChanged(BindingProvider provider, String itemName) {
		interrupt();
		if (bindingsExist()) {
			start();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void allBindingsChanged(BindingProvider provider) {
		if (!bindingsExist()) {
			interrupt();
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void start() {
		// if we have no bindings, there is no need to start
		if(bindingsExist()) {
			super.start();
		}
	}
	
	/**
	 * @return <code>true</code> if any of the {@link BindingProvider}s provides
	 * a binding
	 */
	protected boolean bindingsExist() {
		for (BindingProvider provider : providers) {
			if (provider.providesBinding()) {
				return true;
			}
		}
		return false;
	}
}