/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.insteonplm.internal.utils;

/**
 * Generic pair class.
 *
 * @author Daniel Pfrommer
 * @since 1.5.0
 */

public class Pair<K, V> {
	private K m_key;
	private V m_value;

	/**
	 * Constructs a new <code>Pair</code> with a given key/value
	 * 
	 * @param key  the key
	 * @param value the value
	 */
	public Pair(K key, V value) {
		setKey(key);
		setValue(value);
	}

	public K getKey() 				{ return m_key; }
	public V getValue() 			{ return m_value; }

	public void setKey(K key) 		{ m_key = key; }
	public void setValue(V value) 	{ m_value = value; }
}