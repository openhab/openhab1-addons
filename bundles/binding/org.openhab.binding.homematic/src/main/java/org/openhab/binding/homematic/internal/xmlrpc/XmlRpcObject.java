/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.homematic.internal.xmlrpc;

import java.util.Map;

/**
 * XmlRpcObjects are objects as returned by the XML-RPC server and thus, they
 * are of java.util.Map type. This class defines the presence of a Map object
 * and provides a low level method of accessing the child objects attributes via
 * value keys. Implementing classes should provide a nicer interface for access
 * to values. They should provide getter methods for each attribute and own data
 * types for values if applicable.
 * 
 * @author Mathias Ewald
 * @since 1.2.0
 */
public interface XmlRpcObject {

    public Map<String, Object> getValues();

    public Object getValue(String attribute);

}
