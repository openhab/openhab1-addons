/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.ekozefir;

import java.util.Optional;

/**
 * Interface for collecting all command creators for ahu and response listeners.
 * 
 * @author Michal Marasz
 * @since 1.6.0
 * 
 * @param <V>
 * @param <T>
 */
public interface Creators<V, T> {
	/**
	 * Add creator.
	 * 
	 * @param creator to add
	 */
	void add(T creator);

	/**
	 * Remove creator.
	 * 
	 * @param creator to remove
	 */
	void remove(T creator);

	/**
	 * Try to get command creator for given type.
	 * 
	 * @param type of command creator
	 * @return optional with command creator or empty {@code Optional}
	 */
	Optional<T> getOfType(V type);
}
