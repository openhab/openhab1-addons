/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.lcn.logic.data;


/**
 * Simple (abstract) data class, which represents a LCN module and its current state.
 * Defines some basic data structures, which are used by the LCNInputModule.
 * @author Patrik Pastuschek
 * @since 1.7.0
 *
 */
public abstract class LCNModule {

	/**The segment address of the LCNModule.*/
	public int segment; 
	/**The module address of the LCNModule.*/
	public int module;
	/**The outlet port of a module.*/
	public int outlet;
	/**Status value, used for dim values etc..*/
	public int status;
	/**String containing the firmware of the module, is actually only read in special cases.*/
	public String firmware;
	/**String containing the serial of the module, is actually only read in special cases.*/
	public String serial;
	
	/**
	 * Special comparison method, since modules have to be compared depending on their attributes.
	 * @param o The object to compare to.
	 * @param soft True if the outlet should not be compared.
	 * @param homeSegment the homeSegment to consider.
	 * @return True if the two objects are reasonably similar, false otherwise.
	 */
	public boolean equals(Object o, boolean soft, int homeSegment) {
		boolean result = true;
		
		if (o == null) {
			result = false;
		} else {
		
			try {
			
				LCNModule mod = (LCNModule) o;
				
				int seg1 = this.segment;
				int seg2 = mod.segment;
				
				if (seg1 == homeSegment) {
					seg1 = 0;
				}
				if (seg2 == homeSegment) {
					seg2 = 0;
				}
				
				if (seg1 != seg2
						|| this.module != mod.module
						|| (!soft && this.outlet != mod.outlet)) {
					
					result = false;
					
				}
				
				
			} catch (ClassCastException e) {
				result = false;
			}
			
		}
		
		return result;
	}
	
}
