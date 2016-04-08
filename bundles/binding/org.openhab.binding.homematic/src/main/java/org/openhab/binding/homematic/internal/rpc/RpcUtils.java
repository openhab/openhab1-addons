/**
 * Copyright (c) 2010-2016, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.homematic.internal.rpc;

import java.util.Map;

/**
 * Helper class with common RPC funtions.
 *
 * @author Gerhard Riegler
 * @since 1.9.0
 */
public class RpcUtils {

    /**
     * Dumps decoded RPC data.
     */
    public static String dumpRpcMessage(String methodName, Object[] responseData) {
        StringBuilder sb = new StringBuilder();
        if (methodName != null) {
            sb.append(methodName);
            sb.append("()\n");
        }
        dumpCollection(responseData, sb, 0);
        return sb.toString();
    }

    private static void dumpCollection(Object[] c, StringBuilder sb, int indent) {
        if (indent > 0) {
            for (int in = 0; in < indent - 1; in++) {
                sb.append('\t');
            }
            sb.append("[\n");
        }
        for (Object o : c) {
            if (o instanceof Map) {
                dumpMap((Map<?, ?>) o, sb, indent + 1);
            } else if (o instanceof Object[]) {
                dumpCollection((Object[]) o, sb, indent + 1);
            } else {
                for (int in = 0; in < indent; in++) {
                    sb.append('\t');
                }
                sb.append(o);
                sb.append('\n');
            }
        }
        if (indent > 0) {
            for (int in = 0; in < indent - 1; in++) {
                sb.append('\t');
            }
            sb.append("]\n");
        }
    }

    private static void dumpMap(Map<?, ?> c, StringBuilder sb, int indent) {
        if (indent > 0) {
            for (int in = 0; in < indent - 1; in++) {
                sb.append('\t');
            }
            sb.append("{\n");
        }
        for (Map.Entry<?, ?> me : c.entrySet()) {
            Object o = me.getValue();
            for (int in = 0; in < indent; in++) {
                sb.append('\t');
            }
            sb.append(me.getKey());
            sb.append('=');
            if (o instanceof Map<?, ?>) {
                sb.append("\n");
                dumpMap((Map<?, ?>) o, sb, indent + 1);
            } else if (o instanceof Object[]) {
                sb.append("\n");
                dumpCollection((Object[]) o, sb, indent + 1);
            } else {
                sb.append(o);
                sb.append('\n');
            }
        }
        if (indent > 0) {
            for (int in = 0; in < indent - 1; in++) {
                sb.append('\t');
            }
            sb.append("}\n");
        }
    }

}
