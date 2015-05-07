/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.lcn.logic.data;

import java.util.List;

import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;
import org.openhab.core.types.State;

/**
 * This class represents a single Item, Connection tuple with additional information about the underlying LCNCommand.
 * Also contains information whether this item has been initialized yet (i.e. a request to the module has been sent).
 * @author Patrik Pastuschek
 * @since 1.7.0
 *
 */
public class LCNItemBinding implements BindingConfig {

	/**The actual underlying openHAB item.*/
	final private Item item;
	/**IP of the target.*/
	final private String id;
	/**OpenHAB Command associated with the binding.*/
	final private String cmd;
	/**List of possible states for the item / openHABCommand.*/
	final private List<Class<? extends State>> states;
	/**The short form of the underlying LCNCommand.*/
	final private String lcnShort; 
	/**Flag that is set, if the underlying item has been initialized.*/
	private boolean isInitialized;

	/**
	 * Constructor for an ItemBinding.
	 * @param id The general ID of the target.
	 * @param cmd The openHAB command that is associated with the item.
	 * @param item The actual openHAB item.
	 * @param lcnShort A String with the LCNShortForm.
	 * @param isInitialized True if the item has been initialized, false otherwise (Initialized means, that at least one request has been sent to the underlying module).
	 */
	public LCNItemBinding(String id, String cmd, List<Class<? extends State>> states, Item item, String lcnShort, boolean isInitialized) {
		
		this.id = id;
		this.lcnShort = lcnShort;
		this.cmd = cmd;
		this.states = states;
		this.item = item;
		this.isInitialized = isInitialized;
		
	}

	/**
	 * Returns the openHAB command of the ItemBinding.
	 * @return String, the openHAB command.
	 */
	public String getOpenHabCmd() {
		return this.cmd;
	}

	/**
	 * Returns the target IP of the ItemBinding.
	 * @return String, the IP.
	 */
	public String getID() {
		return this.id;
	}

	/**
	 * Returns the actual openHAB item.
	 * @return Item, the openHAB item.
	 */
	public Item getItem() {
		return this.item;
	}
	
	/**
	 * Returns whether the item of the ItemBinding has been initialized yet.
	 * @return Boolean, true if the item has been initialized, false otherwise.
	 */
	public boolean isInitialized() {
		return this.isInitialized;
	}
	
	/**
	 * Sets the initialization state of the underlying item.
	 * @param isInitialized true if Element(Item) has been initialized, false otherwise.
	 */
	public void setInitialized(boolean isInitialized) {
		this.isInitialized = isInitialized;
	}
	
	/**
	 * Returns the short form of the LCNCommand of the binding.
	 * @return String, the LCNShortForm.
	 */
	public String getLcnshort() {
		return this.lcnShort;
	}
	
	/**
	 * Returns a list of possible states that can be associated with the binding.
	 * @return List<Class<? extends State>>, a list of possible states for the binding.
	 */
	public List<Class<? extends State>> getStates() {
		return states;
	}
}
