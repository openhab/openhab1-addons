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
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * This is a java bean that is used with JAXB to serialize page content
 * to XML or JSON.
 *  
 * @author Kai Kreuzer
 * @since 0.8.0
 *
 */
@XmlRootElement(name="page")
public class PageBean {

	public String id;
	
	public String title;
	public String icon;
	public String link;
	public PageBean parent;
	public boolean leaf;
	
	@XmlElement(name="widget")
	public List<WidgetBean> widgets = new ArrayList<WidgetBean>();
	
	public PageBean() {}
		
}
