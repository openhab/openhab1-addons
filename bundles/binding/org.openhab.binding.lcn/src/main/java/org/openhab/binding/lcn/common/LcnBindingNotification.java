/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.lcn.common;

/**
 * Interface for all calls to be run / invoked on the refresh thread.
 * 
 * @author Tobias Jüttner
 */
public interface LcnBindingNotification {

	/** Code to execute. */
	void execute();
	
}
