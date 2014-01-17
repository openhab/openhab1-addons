/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.io.net.internal;

import org.openhab.core.scriptengine.action.ActionService;
import org.openhab.io.net.actions.Exec;
	

/**
 * This class registers an OSGi service for the Exec action.
 * 
 * @author Kai Kreuzer
 * @since 1.3.0
 */
public class ExecActionService implements ActionService {

	public ExecActionService() {
	}
	
	public void activate() {
	}
	
	public void deactivate() {
		// deallocate Resources here that are no longer needed and 
		// should be reset when activating this binding again
	}

	@Override
	public String getActionClassName() {
		return Exec.class.getCanonicalName();
	}

	@Override
	public Class<?> getActionClass() {
		return Exec.class;
	}

}
