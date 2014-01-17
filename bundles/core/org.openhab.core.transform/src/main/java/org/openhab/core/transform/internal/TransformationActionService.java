/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.core.transform.internal;

import org.openhab.core.scriptengine.action.ActionService;
import org.openhab.core.transform.actions.Transformation;
	

/**
 * This class registers an OSGi service for the Transformation action.
 * 
 * @author Kai Kreuzer
 * @since 1.3.0
 */
public class TransformationActionService implements ActionService {

	public TransformationActionService() {
	}
	
	public void activate() {
	}
	
	public void deactivate() {
		// deallocate Resources here that are no longer needed and 
		// should be reset when activating this binding again
	}

	@Override
	public String getActionClassName() {
		return Transformation.class.getCanonicalName();
	}

	@Override
	public Class<?> getActionClass() {
		return Transformation.class;
	}

}
