/**Copyright (c) 2010-${year}, openHAB.org and others.
* 
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
**/

package org.openhab.binding.wmbus.internal;

/**
 * This class represents the possible types of data, given by WMBus devices.
 * 
 * @author Christoph Parnitzke
 * 
 * @since 1.3.0
 */
public enum DataType
{
    ND,     // No data
    A,      // Unsigned Integer BCD
    B,      // Binary Integer
    C,      // Unsigned Integer
    D,      // Boolean
    F,      // Date and Time
    G,      // Date
    H       // Real
}