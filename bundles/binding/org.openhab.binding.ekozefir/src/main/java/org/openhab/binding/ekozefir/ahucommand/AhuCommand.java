/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.ekozefir.ahucommand;

/**
 * Interface for command sending to ahu.
 * 
 * @author Michal Marasz
 * @sine 1.6.0
 * 
 */
public interface AhuCommand {
	/**
	 * Transform command to message for ahu.
	 * 
	 * @return bytes for ahu
	 */
	byte[] createMessage();

}
