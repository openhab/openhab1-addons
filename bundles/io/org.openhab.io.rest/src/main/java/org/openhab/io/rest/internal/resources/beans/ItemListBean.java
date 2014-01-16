/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.io.rest.internal.resources.beans;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * This is a java bean that is used with JAXB to serialize item lists.
 *  
 * @author Kai Kreuzer
 * @since 0.9.0
 *
 */
@XmlRootElement(name="items")
public class ItemListBean {

	public ItemListBean() {}
	
	public ItemListBean(Collection<ItemBean> list) {
		entries.addAll(list);
	}
	
	@XmlElement(name="item")
	public final List<ItemBean> entries = new ArrayList<ItemBean>();
	
}
