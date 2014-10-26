/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.ecobee.internal.messages;

/**
 * The delete vacation function deletes a vacation event from a thermostat. 
 * This is the only way to cancel a vacation event. 
 * This method is able to remove vacation events not yet started and 
 * scheduled in the future.
 * 
 * @see <a href="https://www.ecobee.com/home/developer/api/documentation/v1/functions/DeleteVacation.shtml">DeleteVacation</a>
 * @author John Cocula
 * @author Ecobee
 */
public class DeleteVacationFunction extends AbstractFunction {

	/**
	 * @param name the vacation event name to delete
	 */
	public DeleteVacationFunction(String name) {
		super("deleteVacation");
		if (name == null) {
			throw new IllegalArgumentException("name argument is required.");
		}

		makeParams().put("name", name);
	}
}
