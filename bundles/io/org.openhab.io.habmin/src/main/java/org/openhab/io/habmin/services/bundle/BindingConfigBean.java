/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.io.habmin.services.bundle;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * This is a java bean that is used with JAXB to serialize items
 * to XML or JSON.
 *
 * @author Chris Jackson
 * @since 1.3.0
 *
 */
@XmlRootElement(name="config")
public class BindingConfigBean {

	public String name;
	public String label;
	public String value;
	public String description; 
	public boolean optional;
	public String defaultvalue;
	public String minimum;
	public String maximum;
	public ArrayList<String> options;

	public BindingConfigBean() {};

	public void copy(BindingConfigBean bn) {
		this.name = bn.name;
		this.label = bn.label;
		this.value = bn.value;
		this.description = bn.description;
		this.optional = bn.optional;
		this.defaultvalue = bn.defaultvalue;
		this.minimum = bn.minimum;
		this.maximum = bn.maximum;
		this.options = bn.options; 
	}
		
}
