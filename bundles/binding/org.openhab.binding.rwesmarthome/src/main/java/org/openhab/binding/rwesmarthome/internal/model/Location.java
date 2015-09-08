/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.rwesmarthome.internal.model;

/**
 * Class to hold an RWE Smarthome location.
 * 
 * @author ollie-dev
 *
 */
public class Location {

	private String id;
	private String name;
	
	/**
	 * Constructor with id and name.
	 * 
	 * @param id
	 * @param name
	 */
	public Location(String id, String name) {
		super();
		this.id = id;
		this.name = name;
	}
	
	/**
	 * Returns the location id.
	 * 
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	
	/**
	 * Sets the location id.
	 * 
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	
	/**
	 * Returns the name of the location.
	 * 
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Sets the name of the location.
	 * 
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	
}
