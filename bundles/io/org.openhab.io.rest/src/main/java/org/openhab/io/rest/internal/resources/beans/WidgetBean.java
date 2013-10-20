/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010-2013, openHAB.org <admin@openhab.org>
 *
 * See the contributors.txt file in the distribution for a
 * full listing of individual contributors.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see <http://www.gnu.org/licenses>.
 *
 * Additional permission under GNU GPL version 3 section 7
 *
 * If you modify this Program, or any covered work, by linking or
 * combining it with Eclipse (or a modified version of that library),
 * containing parts covered by the terms of the Eclipse Public License
 * (EPL), the licensors of this Program grant you additional permission
 * to convey the resulting work.
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
	public String iconcolor;
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
	public String service;
	public String period;
	
	public ItemBean item;
	public PageBean linkedPage;


	// only for frames, other linkable widgets link to a page
	@XmlElement(name="widget")
	public final List<WidgetBean> widgets = new ArrayList<WidgetBean>();
	
	public WidgetBean() {}
		
}