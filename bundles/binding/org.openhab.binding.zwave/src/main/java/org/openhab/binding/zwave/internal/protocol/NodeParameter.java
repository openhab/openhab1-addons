/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.zwave.internal.protocol;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * This class provides a storage class for zwave configuration parameters
 * within the node class. This is then serialised to XML.
 * @author Chris Jackson
 * @since 1.4.0
 *
 */
@XStreamAlias("configurationParameter")
public class NodeParameter {
	int Index;
	int Value;
	int Size;
}
