/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.myq.internal;
import org.openhab.core.binding.BindingConfig;
/**
 * This is a helper class holding binding specific configuration details
 * 
 * @author scooter_seh
 * @since 1.8.0
 */
public class myqBindingConfig implements BindingConfig 
{
	// put member fields here which holds the parsed values
	public enum ITEMTYPE {	Switch, StringStatus, ContactStatus
	};

	ITEMTYPE type;
	String id;
	String MyQName;		
}