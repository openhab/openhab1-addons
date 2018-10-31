/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.velux.things;

import java.util.HashMap;
import java.util.Map;


/**
 * <B>Velux</B> product representation.
 * <P>
 * Combined set of information describing a single Velux product.
 *
 * @author Guenther Schreiner - initial contribution.
 */
public enum VeluxProductType {
	VP_INTERIOR_VENETIAN_BLIND(			(short) 0x0040,  1,	 0),
	ROLLER_SHUTTER(						(short) 0x0080,  2,  0),
	OTHER(								(short) 0x00C0,  3,  0),
	WINDOW_OPENER(						(short) 0x0101,  4,  1),
	SOLAR_SLIDER(						(short) 0x0280, 10,  2),
	UNDEFTYPE(							(short) 0xffff, -1, -1);

	// Class internal

	private short nodeType;
	private int typeId;
	private int subtype;

	// Reverse-lookup map for getting a VeluxProductType from an TypeId
	private static final Map<Integer, VeluxProductType> lookupTypeId2Enum = new HashMap<Integer, VeluxProductType>();

	static {
		for (VeluxProductType typeId : VeluxProductType.values()) {
			lookupTypeId2Enum.put((int) typeId.getNodeType(), typeId);
		}
	}

	// Constructor

	private VeluxProductType(short nodeType, int typeId, int subTypeId) {
		this.nodeType = nodeType;
		this.typeId = typeId;
		this.subtype = subTypeId;
	}

	VeluxProductType(String categoryString) {
		try {
			this.nodeType = VeluxProductType.valueOf(categoryString).getNodeType();
			this.typeId = VeluxProductType.valueOf(categoryString).getTypeId();
			this.subtype = VeluxProductType.valueOf(categoryString).getSubtype();
		} catch (IllegalArgumentException e) {
			try {
				this.nodeType = VeluxProductType.valueOf(categoryString.replaceAll("\\p{C}", "_").toUpperCase())
						.getNodeType();
				this.typeId = VeluxProductType.valueOf(categoryString.replaceAll("\\p{C}", "_").toUpperCase())
						.getTypeId();
				this.subtype = VeluxProductType.valueOf(categoryString.replaceAll("\\p{C}", "_").toUpperCase())
						.getSubtype();
			} catch (IllegalArgumentException e2) {
				this.typeId = -1;
			}
		}
	}

	// Class access methods

	public short getNodeType() {
		return nodeType;
	}

	public int getTypeId() {
		return typeId;
	}

	public int getSubtype() {
		return subtype;
	}

	public static VeluxProductType get(int nodeType) {
		return lookupTypeId2Enum.get(nodeType);
	}

	public static String dump() {
		StringBuilder sb = new StringBuilder();
		for (VeluxProductType typeId : VeluxProductType.values()) {
			sb.append(typeId).append(",").append(typeId.getNodeType()).
			append(",").append(typeId.getTypeId())
			.append(",").append(typeId.getSubtype()).append(";");
		}
		if (sb.lastIndexOf(",") > 0) {
			sb.deleteCharAt(sb.lastIndexOf(","));
		}
		return sb.toString();
	}

}

/**
 * end-of-VeluxProductType.java
 */
