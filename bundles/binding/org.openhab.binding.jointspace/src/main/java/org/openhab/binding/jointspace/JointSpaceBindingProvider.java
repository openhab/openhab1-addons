/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.jointspace;

import org.openhab.core.binding.BindingProvider;

/**
 * @author David Lenz
 * @since 1.5.0
 */
public interface JointSpaceBindingProvider extends BindingProvider {

	/**
	 * Returns the TV Command string that is stored for given @code itemName in
	 * response to a given @code command
	 * 
	 * @param itemName
	 * @param command
	 * @return Stored string, or null if unknown combination of itemName and
	 *         command
	 */
	String getTVCommand(String itemName, String command);

}
