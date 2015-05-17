/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.dsmr.internal.cosem;

import org.openhab.core.types.State;

/**
 * This CosemValueDescriptor provides meta data for a CosemValue
 * 
 * @author M. Volaart
 * @since 1.7.0
 */
public class CosemValueDescriptor {
	/* Class describing the type */
	private final Class<? extends CosemValue<? extends State>> cosemValueClass;

	/* String describing the unit */
	private final String unit;

	/* String describing the dsmrItemId for openHAB items */
	private final String dsmrItemId;

	/**
	 * Creates a new CosemValueDescriptor
	 * 
	 * @param cosemValueClass
	 *            CosemValue class
	 * @param unit
	 *            the unit for the CosemValue
	 * @param dsmrItemId
	 *            the DSMR Item Identifier to be used in the binding
	 *            (dsmr=<dsrmItemId>)
	 */
	public CosemValueDescriptor(
			Class<? extends CosemValue<? extends State>> cosemValueClass,
			String unit, String dsmrItemId) {
		this.cosemValueClass = cosemValueClass;
		this.unit = unit;
		this.dsmrItemId = dsmrItemId;
	}

	/**
	 * Returns the class of the CosemValue
	 * 
	 * @return the class of the CosemValue
	 */
	public Class<? extends CosemValue<? extends State>> getCosemValueClass() {
		return cosemValueClass;
	}

	/**
	 * Returns the unit
	 * 
	 * @return the unit
	 */
	public String getUnit() {
		return unit;
	}

	/**
	 * Returns the DSMR item id
	 * 
	 * @return the DSMR item id
	 */
	public String getDsmrItemId() {
		return dsmrItemId;
	}

	/**
	 * Returns String representation of this CosemValueDescriptor
	 * 
	 * @return String representation of this CosemValueDescriptor
	 */
	public String toString() {
		return "CosemValueDescriptor[class=" + cosemValueClass.toString()
				+ ", unit=" + unit + ", dsmrItemId=" + dsmrItemId + "]";
	}
}
