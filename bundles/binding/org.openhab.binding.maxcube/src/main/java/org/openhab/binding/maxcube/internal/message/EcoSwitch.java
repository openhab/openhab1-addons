/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.openhab.binding.maxcube.internal.message;

/**
*  MAX!Cube EcoSwitch. 
* 
* @author Marcel Verpaalen
* @since 1.6.0
*/

public class EcoSwitch extends ShutterContact {

	/**
	 * Class constructor. 
	 * @param c
	 */
	public EcoSwitch(Configuration c) {
		super(c);
	}
	@Override
	public DeviceType getType() {
		// TODO Auto-generated method stub
		return DeviceType.EcoSwitch;
	}

}
