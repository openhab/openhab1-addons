/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.core.library.types;

import java.math.BigDecimal;

/** 
 * The PercentType extends the {@link DecimalType} by putting constraints for its value on top (0-100).
 * 
 * @author Kai Kreuzer
 * @since 0.1.0
 *
 */
public class PercentType extends DecimalType {
	
	private static final long serialVersionUID = -9066279845951780879L;
	
	final static public PercentType ZERO = new PercentType(0); 
	final static public PercentType HUNDRED = new PercentType(100); 
	
	public PercentType() {
		super();
	}
	
	public PercentType(int value) {
		super(value);
		validateValue(this.value);
	}

	public PercentType(String value) {
		super(value);
		validateValue(this.value);
	}

	public PercentType(BigDecimal value) {
		super(value);
		validateValue(this.value);
	}
	
	private void validateValue(BigDecimal value) {
		if(BigDecimal.ZERO.compareTo(value) > 0 || new BigDecimal(100).compareTo(value) < 0) {
			throw new IllegalArgumentException("Value must be between 0 and 100");
		}
	}
	
	public static PercentType valueOf(String value) {
		return new PercentType(value);
	}

}
