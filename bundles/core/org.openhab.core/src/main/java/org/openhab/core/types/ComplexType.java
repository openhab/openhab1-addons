/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.core.types;

import java.util.SortedMap;

/**
 * A complex type consists out of a sorted list of primitive constituents.
 * Each constituent can be referred to by a unique name.
 * 
 * @author Kai Kreuzer
 * @since 0.1.0
 *
 */
public interface ComplexType extends Type {
	
	/**
	 * Returns all constituents with their names as a sorted map
	 * 
	 * @return all constituents with their names
	 */
	public SortedMap<String, PrimitiveType> getConstituents();
	
}
