/**
 * Copyright (c) 2010-2016, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.homematic.internal.rpc;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringEscapeUtils;

/**
 * A XML-RPC request for sending data to the Homematic server.
 *
 * @author Gerhard Riegler
 * @since 1.9.0
 */
public class XmlRpcRequest implements RpcRequest {

    public enum TYPE {
        REQUEST,
        RESPONSE;
    }

    private String methodName;
    private List<Object> parms;
    private StringBuilder sb;
    private TYPE type;
    public static SimpleDateFormat xmlRpcDateFormat = new SimpleDateFormat("yyyyMMdd'T'HH:mm:ss");

    public XmlRpcRequest(String methodName) {
        this(methodName, TYPE.REQUEST);
    }

    public XmlRpcRequest(String methodName, TYPE type) {
        this.methodName = methodName;
        this.type = type;
        parms = new ArrayList<Object>();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addArg(Object parameter) {
        parms.add(parameter);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public byte[] createMessage() {
        try {
            return toString().getBytes("ISO-8859-1");
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        sb = new StringBuilder();

        sb.append("<?xml");
        attr("version", "1.0");
        attr("encoding", "ISO-8859-1");
        sb.append("?>\n");

        if (type == TYPE.REQUEST) {
            sb.append("<methodCall>");
            tag("methodName", methodName);
        } else {
            sb.append("<methodResponse>");
        }

        sb.append("\n");
        sb.append("<params>");
        for (Object parameter : parms) {
            sb.append("<param><value>");
            generateValue(parameter);
            sb.append("</value></param>");
        }
        sb.append("</params>");

        if (type == TYPE.REQUEST) {
            sb.append("</methodCall>");
        } else {
            sb.append("</methodResponse>");
        }
        return sb.toString();
    }

    /**
     * Generates a XML attribute.
     */
    private void attr(String name, String value) {
        sb.append(" ").append(name).append("=\"").append(value).append("\"");
    }

    /**
     * Generates a XML tag.
     */
    private void tag(String name, String value) {
        sb.append("<").append(name).append(">").append(value).append("</").append(name).append(">");
    }

    /**
     * Generates a value tag based on the type of the value.
     */
    private void generateValue(Object value) {
        if (value == null) {
            tag("string", "void");
        } else {
            Class<?> clazz = value.getClass();
            if (clazz == String.class || clazz == Character.class) {
                sb.append(StringEscapeUtils.escapeXml(value.toString()));
            } else if (clazz == Long.class || clazz == Integer.class || clazz == Short.class || clazz == Byte.class) {
                tag("int", value.toString());
            } else if (clazz == Double.class) {
                tag("double", String.valueOf(((Double) value).doubleValue()));
            } else if (clazz == Float.class) {
                BigDecimal bd = new BigDecimal((Float) value);
                generateValue(bd.setScale(6, RoundingMode.HALF_DOWN).doubleValue());
            } else if (clazz == BigDecimal.class) {
                generateValue(((BigDecimal) value).setScale(6, RoundingMode.HALF_DOWN).doubleValue());
            } else if (clazz == Boolean.class) {
                tag("boolean", ((Boolean) value).booleanValue() ? "1" : "0");
            } else if (clazz == Date.class) {
                tag("dateTime.iso8601", xmlRpcDateFormat.format(((Date) value)));
            } else if (value instanceof Calendar) {
                generateValue(((Calendar) value).getTime());
            } else if (value instanceof byte[]) {
                tag("base64", Base64.getEncoder().encodeToString((byte[]) value));
            } else if (clazz.isArray() || value instanceof List) {
                sb.append("<array><data>");

                Object[] array = null;
                if (value instanceof List) {
                    array = ((List<?>) value).toArray();
                } else {
                    array = (Object[]) value;
                }
                for (Object arrayObject : array) {
                    sb.append("<value>");
                    generateValue(arrayObject);
                    sb.append("</value>");
                }

                sb.append("</data></array>");
            } else if (value instanceof Map) {
                sb.append("<struct>");

                for (Entry<?, ?> entry : ((Map<?, ?>) value).entrySet()) {
                    sb.append("<member>");
                    sb.append("<name>").append(entry.getKey()).append("</name>");
                    sb.append("<value>");
                    generateValue(entry.getValue());
                    sb.append("</value>");
                    sb.append("</member>");
                }

                sb.append("</struct>");
            } else {
                throw new RuntimeException("Unsupported XML-RPC Type: " + value.getClass());
            }
        }
    }
}
