/**Copyright (c) 2010-${year}, openHAB.org and others.
* 
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
**/

package org.openhab.binding.wmbus.internal;

/**
 * This class represents a single value send by a WMBus device. <br>
 * It is also used by the WMBus class to build the finalValuesEvent.
 * 
 * @author Christoph Parnitzke
 *
 * @since 1.3.0
 */
public class Value 
{
    public int storageNum=0;
    public FunctionValue Func = FunctionValue.Instant;
    public String description = "";
    public String value = "";    
    public int tariff=0;
}
