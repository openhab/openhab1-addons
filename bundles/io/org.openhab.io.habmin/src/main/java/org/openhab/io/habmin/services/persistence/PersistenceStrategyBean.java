/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.io.habmin.services.persistence;


import javax.xml.bind.annotation.XmlRootElement;

/**
 * This is a java bean that is used with JAXB to serialize item lists.
 *  
 * @author Chris Jackson
 * @since 1.3.0
 *
 */
@XmlRootElement(name="strategy")
public class PersistenceStrategyBean {

	public PersistenceStrategyBean() {}

	public String name;
	public String cron;
}
