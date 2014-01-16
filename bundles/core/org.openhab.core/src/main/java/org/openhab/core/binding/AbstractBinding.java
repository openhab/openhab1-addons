/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.core.binding;

import java.util.Collection;
import java.util.concurrent.CopyOnWriteArraySet;

import org.openhab.core.events.AbstractEventSubscriber;
import org.openhab.core.events.EventPublisher;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;


/**
 * Base class for bindings which send events.
 * 
 * @author Thomas.Eichstaedt-Engelen
 * @author Kai Kreuzer
 * @since 1.0.0
 */
public abstract class AbstractBinding<P extends BindingProvider> extends AbstractEventSubscriber implements BindingChangeListener {
	
	/** to keep track of all binding providers */

	protected Collection<P> providers = new CopyOnWriteArraySet<P>();
	
	protected EventPublisher eventPublisher = null;
	
	
	public void setEventPublisher(EventPublisher eventPublisher) {
		this.eventPublisher = eventPublisher;
	}

	public void unsetEventPublisher(EventPublisher eventPublisher) {
		this.eventPublisher = null;
	}

	public void activate() {};

	public void deactivate() {};

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
        allBindingsChanged(provider);
    }

	/**
	 * Removes <code>provider</code> from the list of providers. If there is no
	 * provider left the refresh thread is getting interrupted.
	 * 
	 * @param provider the {@link BindingProvider} to remove
	 */
	public void removeBindingProvider(P provider) {
		this.providers.remove(provider);
		provider.removeBindingChangeListener(this);
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
	
	/**
	 * @{inheritDoc}
	 */
	@Override
	public void receiveCommand(String itemName, Command command) {
		// does any provider contain a binding config?
		if (!providesBindingFor(itemName)) {
			return;
		}
		internalReceiveCommand(itemName, command);
	}
	
	/**
	 * Is called by <code>receiveCommand()</code> only if one of the 
	 * {@link BindingProvider}s provide a binding for <code>itemName</code>.
	 * 
	 * @param itemName the item on which <code>command</code> will be executed
	 * @param command the {@link Command} to be executed on <code>itemName</code>
	 */
	protected void internalReceiveCommand(String itemName, Command command) {};
	
	/**
	 * @{inheritDoc}
	 */
	@Override
	public void receiveUpdate(String itemName, State newState) {
		// does any provider contain a binding config?
		if (!providesBindingFor(itemName)) {
			return;
		}
		internalReceiveUpdate(itemName, newState);
	}
	
	/**
	 * Is called by <code>receiveUpdate()</code> only if one of the 
	 * {@link BindingProvider}s provide a binding for <code>itemName</code>.
	 * 
	 * @param itemName the item on which <code>command</code> will be executed
	 * @param newState the {@link State} to be update
	 */
	protected void internalReceiveUpdate(String itemName, State newState) {};

	/**
	 * checks if any of the bindingProviders contains an adequate mapping
	 * 
	 * @param itemName the itemName to check
	 * @return <code>true</code> if any of the bindingProviders contains an
	 *         adequate mapping for <code>itemName</code> and <code>false</code>
	 *         otherwise
	 */
	protected boolean providesBindingFor(String itemName) {
		for (P provider : providers) {
			if (provider.providesBindingFor(itemName)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void allBindingsChanged(BindingProvider provider) {
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void bindingChanged(BindingProvider provider, String itemName) {
	}

}
