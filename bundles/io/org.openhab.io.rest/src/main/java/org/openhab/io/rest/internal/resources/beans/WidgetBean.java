/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.io.rest.internal.resources.beans;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * This is a java bean that is used with JAXB to serialize widgets
 * to XML or JSON.
 *  
 * @author Kai Kreuzer
 * @author Chris Jackson
 * @since 0.8.0
 *
 */
@XmlRootElement(name="widget")
public class WidgetBean {

	public String widgetId;
	public String type;
	public String name;
	
	public String label;
	public String icon;
	public String labelcolor;
	public String valuecolor;

	// widget-specific attributes
	@XmlElement(name="mapping")
	public List<MappingBean> mappings = new ArrayList<MappingBean>();
	public Boolean switchSupport;
	public Integer sendFrequency;
	public String separator;
	public Integer refresh;
	public Integer height;
	public BigDecimal minValue;
	public BigDecimal maxValue;
	public BigDecimal step;
	public String url;
	public String encoding;
	public String service;
	public String period;
	
	public ItemBean item;
	public PageBean linkedPage;


	// only for frames, other linkable widgets link to a page
	@XmlElement(name="widget")
	public final List<WidgetBean> widgets = new ArrayList<WidgetBean>();
	
	public WidgetBean() {}
		
}