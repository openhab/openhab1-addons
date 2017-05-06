/**Copyright (c) 2010-${year}, openHAB.org and others.
* 
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
**/

package org.openhab.binding.wmbus.internal;

import java.util.EventListener;

/**
 * This class represents a listener who gets final values from the WMBus class.
 * 
 * @author Christoph Parnitzke
 * 
 * @since 1.3.0 
 */
public interface FinalValuesEventListener extends EventListener {
	
	public void receiveFinalValues (FinalValuesEvent e);
	
}
