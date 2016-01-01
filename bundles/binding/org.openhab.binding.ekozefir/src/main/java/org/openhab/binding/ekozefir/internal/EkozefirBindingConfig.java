/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.ekozefir.internal;

import java.util.Objects;

import org.openhab.core.binding.BindingConfig;

/**
 * Configuration - connect name of ahu(A,B,C,D,E,F,G,H) to specific type of
 * command / listener.
 * 
 * @author Michal Marasz
 * @since 1.6.0
 * 
 */
public class EkozefirBindingConfig implements BindingConfig {

	private final String type;
	private final Character ahuIdentifier;

	public EkozefirBindingConfig(String type, Character ahuIdentifier) {
		Objects.requireNonNull(type);
		Objects.requireNonNull(ahuIdentifier);
		this.type = type;
		this.ahuIdentifier = ahuIdentifier;
	}

	/**
	 * Get type.
	 * 
	 * @return type
	 */
	public String getType() {
		return type;
	}

	/**
	 * Get ahu identifier.
	 * 
	 * @return ahu identifier
	 */
	public Character getAhuIdentifier() {
		return ahuIdentifier;
	}

	@Override
	public String toString() {
		return "Type: " + Objects.toString(type) + " Identifier: " + Objects.toString(ahuIdentifier);
	}

}
