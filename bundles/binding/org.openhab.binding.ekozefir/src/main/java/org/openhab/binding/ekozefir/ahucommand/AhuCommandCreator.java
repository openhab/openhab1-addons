/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.ekozefir.ahucommand;

import org.openhab.core.types.Command;

/**
 * Interface for creating ahu commands.
 * 
 * @author Michal Marasz
 * @since 1.6.0
 */
public interface AhuCommandCreator {
	/**
	 * Convert openhab command to message for ahu.
	 * 
	 * @param command openhab command
	 * @return message for ahu
	 */
	AhuCommand create(Command command);

	/**
	 * Get type of creator.
	 * 
	 * @return type of creator
	 */
	String getId();
}
