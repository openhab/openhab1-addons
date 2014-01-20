/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.homematic.internal.xmlrpc.impl;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.openhab.binding.homematic.internal.xmlrpc.AbstractXmlRpcObject;

/**
 * A ParameterDescription describes a single attribute of a Paramset. This
 * description includes the type, the default value, upper and lower limits of
 * the value and many more.
 * 
 * @author Mathias Ewald
 * @since 1.2.0
 */
public class ParameterDescription extends AbstractXmlRpcObject {

    public enum Type {
        FLOAT, SPECIAL, INTEGER, BOOL, ENUM, VALUE_LIST, STRING, ACTION
    }

    public enum Operation {
        READ, WRITE, EVENT
    }

    public enum Flag {
        VISIBLE, INTERNAL, TRANSFORM, SERVICE, STICKY
    }

    private Type type;
    private Set<Operation> operations;
    private Set<Flag> flags;
    private Object defaultValue;
    private Object maxValue;
    private Object minValue;
    private String unit;
    private Integer tabOrder;
    private String control;

    public ParameterDescription(Map<String, Object> values) {
        super(values);

        String typeStr = values.get("TYPE").toString();
        type = ParameterDescription.Type.STRING;
        if ("FLOAT".equals(typeStr)) {
            type = ParameterDescription.Type.FLOAT;
        }
        if ("SPECIAL".equals(typeStr)) {
            type = ParameterDescription.Type.SPECIAL;
        }
        if ("INTEGER".equals(typeStr)) {
            type = ParameterDescription.Type.INTEGER;
        }
        if ("BOOL".equals(typeStr)) {
            type = ParameterDescription.Type.BOOL;
        }
        if ("ENUM".equals(typeStr)) {
            type = ParameterDescription.Type.ENUM;
        }
        if ("VALUE_LIST".equals(typeStr)) {
            type = ParameterDescription.Type.VALUE_LIST;
        }
        if ("STRING".equals(typeStr)) {
            type = ParameterDescription.Type.STRING;
        }
        if ("ACTION".equals(typeStr)) {
            type = ParameterDescription.Type.ACTION;
        }

        Integer operationVal = Integer.parseInt(values.get("OPERATIONS").toString());
        operations = new HashSet<ParameterDescription.Operation>();
        if ((operationVal & 1) == 1) {
            operations.add(ParameterDescription.Operation.READ);
        }
        if ((operationVal & 2) == 2) {
            operations.add(ParameterDescription.Operation.WRITE);
        }
        if ((operationVal & 4) == 4) {
            operations.add(ParameterDescription.Operation.EVENT);
        }

        Integer flagsVal = Integer.parseInt(values.get("FLAGS").toString());
        flags = new HashSet<ParameterDescription.Flag>();
        if ((flagsVal & 1) == 1) {
            flags.add(ParameterDescription.Flag.VISIBLE);
        }
        if ((flagsVal & 2) == 2) {
            flags.add(ParameterDescription.Flag.INTERNAL);
        }
        if ((flagsVal & 4) == 4) {
            flags.add(ParameterDescription.Flag.TRANSFORM);
        }
        if ((flagsVal & 8) == 8) {
            flags.add(ParameterDescription.Flag.SERVICE);
        }
        if ((flagsVal & 16) == 16) {
            flags.add(ParameterDescription.Flag.STICKY);
        }

        defaultValue = values.get("DEFAULT");

        maxValue = values.get("MAX");

        minValue = values.get("MIN");

        unit = values.get("UNIT").toString();

        tabOrder = Integer.parseInt(values.get("TAB_ORDER").toString());

        if (values.get("CONTROL") != null) {
            control = values.get("CONTROL").toString();
        }
    }

    public Type getType() {
        return type;
    }

    public Set<Operation> getOperations() {
        return operations;
    }

    public Set<Flag> getFlags() {
        return flags;
    }

    public Object getDefaultValue() {
        return defaultValue;
    }

    public Object getMaxValue() {
        return maxValue;
    }

    public Object getMinValue() {
        return minValue;
    }

    public String getUnit() {
        return unit;
    }

    public Integer getTabOrder() {
        return tabOrder;
    }

    public String getControl() {
        return control;
    }

    @Override
    public String toString() {
        return "ParameterDescription [control=" + control + ", defaultValue=" + defaultValue + ", flags=" + flags + ", maxValue="
                + maxValue + ", minValue=" + minValue + ", operations=" + operations + ", tabOrder=" + tabOrder + ", type=" + type
                + ", unit=" + unit + "]";
    }

}
