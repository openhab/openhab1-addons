/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.io.habmin.services.item;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import org.openhab.io.habmin.services.persistence.ItemPersistenceBean;

/**
 * This is a java bean that is used with JAXB to serialize items
 * to XML or JSON.
 *
 * @author Chris Jackson
 * @since 1.3.0
 *
 */
@XmlRootElement(name="item")
public class ItemConfigBean {

	public String model;
	
	public String type;
	public String name;	
	public String link;

	public String icon;
	
	public String label;
	public String units;
	public String format;
	public String translateService;
	public String translateRule;

	public List<String> groups;

	public List<ItemPersistenceBean> persistence;
	public List<ItemBindingBean> bindings;
	
	public List<ItemExtendedConfigBean> extendedData;

	public ItemConfigBean() {}
}
