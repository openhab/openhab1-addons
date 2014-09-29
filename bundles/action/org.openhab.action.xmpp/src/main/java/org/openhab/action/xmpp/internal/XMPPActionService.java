/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.action.xmpp.internal;

import org.openhab.core.scriptengine.action.ActionService;
	

/**
 * This class registers an OSGi service for the XMPP action.
 * 
 * @author Kai Kreuzer
 * @since 1.3.0
 */
public class XMPPActionService implements ActionService {
	
	public XMPPActionService() {}
	
	public void activate() {}
	
	public void deactivate() {}

	@Override
	public String getActionClassName() {
		return XMPP.class.getCanonicalName();
	}

	@Override
	public Class<?> getActionClass() {
		return XMPP.class;
	}
	
}
