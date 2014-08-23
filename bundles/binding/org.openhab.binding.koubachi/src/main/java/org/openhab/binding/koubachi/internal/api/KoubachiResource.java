/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.koubachi.internal.api;

/**
 * The Superclass of all Koubachi resources. Holds common fields an methods. 
 *  
 * @author Thomas.Eichstaedt-Engelen
 * @since 1.2.0
 */
public abstract class KoubachiResource {

	protected String id;

	public String getId() {
		return id;
	}

}
