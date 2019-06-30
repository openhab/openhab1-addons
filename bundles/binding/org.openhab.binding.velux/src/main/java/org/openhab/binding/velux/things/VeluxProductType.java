/**
 * Copyright (c) 2010-2019 Contributors to the openHAB project
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.openhab.binding.velux.things;

import java.util.HashMap;
import java.util.Map;

/**
 * <B>Velux</B> product characteristics: Product Type.
 * <P>
 * See <a href=
 * "https://velcdn.azureedge.net/~/media/com/api/klf200/technical%20specification%20for%20klf%20200%20api.pdf#page=112">KLF200
 * List of actuator types and their use of Main Parameter and Functional Parameters</a>
 * <P>
 * Methods in handle this type of information:
 * <UL>
 * <LI>{@link #getNodeType()} to retrieve the value of the characteristic.</LI>
 * <LI>{@link #getTypeId()} to retrieve a value of the the characteristic.</LI>
 * <LI>{@link #getSubtype()} to retrieve a value of the characteristic.</LI>
 * <LI>{@link #get(int)} to convert a value into the characteristic.</LI>
 * <LI>{@link #dump} to retrieve a human-readable description of all values.</LI>
 * </UL>
 *
 * @see VeluxKLFAPI
 *
 * @author Guenther Schreiner - initial contribution.
 * @since 1.13.0
 */
public enum VeluxProductType {
    VP_INTERIOR_VENETIAN_BLIND((short) 0x0040, 1, 0),
    ROLLER_SHUTTER((short) 0x0080, 2, 0),
    OTHER((short) 0x00C0, 3, 0),
    WINDOW_OPENER((short) 0x0101, 4, 1),
    SOLAR_SLIDER((short) 0x0280, 10, 2),
    UNDEFTYPE((short) 0xffff, -1, -1);

    // Type definitions, class-internal variables

    private static String outputSeparator = ",";
    private static String outputSeparator2 = "/";

    // Class internal

    private short nodeType;
    private int typeId;
    private int subtype;

    // Reverse-lookup map for getting a VeluxProductType from an TypeId
    private static final Map<Integer, VeluxProductType> LOOKUPTYPEID2ENUM = new HashMap<Integer, VeluxProductType>();

    static {
        for (VeluxProductType typeId : VeluxProductType.values()) {
            LOOKUPTYPEID2ENUM.put((int) typeId.getNodeType(), typeId);
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
        VeluxProductType typeId = LOOKUPTYPEID2ENUM.get(nodeType);
        return (typeId == null) ? VeluxProductType.UNDEFTYPE : typeId;
    }

    public static String dump() {
        StringBuilder sb = new StringBuilder();
        for (VeluxProductType typeId : VeluxProductType.values()) {
            sb.append(typeId).append(outputSeparator2).append(typeId.getNodeType()).append(outputSeparator2)
                    .append(typeId.getTypeId()).append(outputSeparator2).append(typeId.getSubtype())
                    .append(outputSeparator);
        }
        if (sb.lastIndexOf(outputSeparator) > 0) {
            sb.deleteCharAt(sb.lastIndexOf(outputSeparator));
        }
        return sb.toString();
    }

}
