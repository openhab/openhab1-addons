/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.openhab.binding.resolvbus.model;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @author Michael Heckmann
 * @since 1.7.0
 */

public class ResolVBUSValue {

	private BigInteger offset;
	private BigInteger bitSize;
	private BigDecimal factor;
	
	public BigInteger getOffset() {
		return offset;
	}
	public void setOffset(BigInteger offset) {
		this.offset = offset;
	}
	public BigInteger getBitSize() {
		return bitSize;
	}
	public void setBitsize(BigInteger bitSize) {
		this.bitSize = bitSize;
	}
	public BigDecimal getFactor() {
		return factor;
	}
	public void setFactor(BigDecimal factor) {
		this.factor = factor;
	}
	public void setBitSize(BigInteger bitSize) {
		this.bitSize = bitSize;
	}

	
	
	
}
