/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.io.cv.internal.resources.beans;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * This is a java bean that is used with JAXB to serialize item lists.
 *  
 * @author Tobias Bräutigam
 * @since 1.4.0
 *
 */
@XmlRootElement(name="items")
public class ItemListBean {
	@XmlAnyElement
	public List<JAXBElement> entries = new ArrayList<JAXBElement>();
	
	public ItemListBean() {}
	
	public ItemListBean(Collection<ItemBean> beans) {
		for (ItemBean bean : beans) {
            entries.add(new JAXBElement(bean.entries.get(0).getName(), 
                    String.class, bean.entries.get(0).getValue()));
        }
	}
	
}
