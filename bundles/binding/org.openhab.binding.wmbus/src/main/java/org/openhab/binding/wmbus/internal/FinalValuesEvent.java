/**Copyright (c) 2010-${year}, openHAB.org and others.
* 
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
**/

package org.openhab.binding.wmbus.internal;

import java.util.EventObject;
import java.util.List;

/**
 * This class represents the event type thrown by the WMBus class.
 * 
 * @author Christoph Parnitzke
 *
 * @since 1.3.0
 */
public class FinalValuesEvent extends EventObject
{
	private static final long serialVersionUID = 8948316124071493591L;
	public List<Value> FinalValues; //The list holding all data send by the device
    public String meterID; //The model name of the sending device
    public String serialN; //The serial number of the sending device
    public FinalValuesEvent(Object source, List<Value> finalValues, String meterID, String serialN)
    {
    	super(source);
        this.FinalValues = finalValues;
        this.meterID = meterID;
        this.serialN = serialN;
    }
}
