/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.io.rest.internal.resources.beans;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * This is a java bean that is used with JAXB to serialize sitemaps
 * to XML or JSON.
 *  
 * @author Kai Kreuzer
 * @author Chris Jackson
 * @since 0.8.0
 *
 */
@XmlRootElement(name="sitemap")
public class SitemapBean {

	public String name;
	public String icon;
	public String label;
	
	public String link;

	public PageBean homepage;

}
