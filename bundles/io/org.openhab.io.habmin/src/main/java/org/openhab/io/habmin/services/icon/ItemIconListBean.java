/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.io.habmin.services.icon;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * This is a java bean that is used with JAXB to serialize items
 * to XML or JSON.
 *
 * @author Chris Jackson
 * @since 1.3.0
 *
 */
@XmlRootElement(name="icons")
public class ItemIconListBean {

	public String name;
	public String description;
	public String author;
	public String license;
	public Integer width;
	public Integer height;
	
	public ItemIconListBean() {}

	public ItemIconListBean(Collection<ItemIconBean> list) {
		entries.addAll(list);
	}

	@XmlElement(name="icon")
	public final List<ItemIconBean> entries = new ArrayList<ItemIconBean>();
}
