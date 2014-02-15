/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.cups;

import org.cups4j.WhichJobsEnum;
import org.openhab.core.binding.BindingProvider;



/**
 * This interface is implemented by classes that can provide mapping information
 * between openHAB items and Cups items (printers).
 * 
 * Implementing classes should register themselves as a service in order to be 
 * taken into account.
 * 
 * @author Tobias Bräutigam
 * @since 1.1.0
 */
public interface CupsBindingProvider extends BindingProvider {

	/**
	 * @return the corresponding printerName to the given <code>itemName</code>
	 */
	public String getPrinterName(String itemName);
	
	/**
	 * @return the corresponding {@link WhichJobsEnum} to the given <code>itemName</code>
	 */
	public WhichJobsEnum getWhichJobs(String itemName);
	
}
