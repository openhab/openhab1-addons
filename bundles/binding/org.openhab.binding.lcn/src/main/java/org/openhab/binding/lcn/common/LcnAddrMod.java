/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.lcn.common;

/**
 * Represents an LCN module address.
 * Can be used as a key in maps.
 * Hash codes are guaranteed to be unique as long as {@link #isValid()} is true.
 *  
 * @author Tobias Jüttner
 */
public class LcnAddrMod extends LcnAddr implements Comparable<LcnAddrMod> {
	
	/** The module id. */
	private final int modId;
	
	/** Constructs an empty / invalid module address. */
	public LcnAddrMod() {
		this.modId = -1;  // Invalid
	}
	
	/**
	 * Constructs a module address with (logical) segment id and module id.
	 * 
	 * @param segId the segment id
	 * @param modId the module id
	 */
	public LcnAddrMod(int segId, int modId) {
		super(segId);
		this.modId = modId;
	}
	
	/**
	 * Gets the module id.
	 * 
	 * @return the module id
	 */
	public int getModId() {
		return this.modId;
	}	
	
	/** {@inheritDoc} */
	@Override
	public boolean isValid() {
		// segId:
		// 0 = Local, 1..2 = Not allowed (but "seen in the wild")
		// 3 = Broadcast, 4 = Status messages, 5..127, 128 = Segment-bus disabled (valid value)
		// modId:
		// 1 = LCN-PRO, 2 = LCN-GVS/LCN-W, 4 = PCHK, 5..254, 255 = Unprog. (valid, but irrelevant here)
		return this.segId >= 0 && this.segId <= 128 &&
			this.modId >= 1 && this.modId <= 254;
	}
	
	/** {@inheritDoc} */
	@Override
	public boolean isGroup() {
		return false;
	}
	
	/** {@inheritDoc} */
	@Override
	public int getId() {
		return this.modId;
	}
	
	/** {@inheritDoc} */
	@Override
	public int hashCode() {
		// Reversing the bits helps to generate better balanced trees as ids tend to be "user-sorted"
		try {
			if (this.isValid()) {
				return ReverseNumber.reverseUInt8(this.modId) << 8 + ReverseNumber.reverseUInt8(this.segId);
			}
		}
		catch (IllegalArgumentException ex) { }
		return -1;
	}
	
	/** {@inheritDoc} */
	@Override
	public boolean equals(Object obj) {		
		if (!(obj instanceof LcnAddrMod)) {
			return false;
		}
		return this.segId == ((LcnAddrMod)obj).segId && this.modId == ((LcnAddrMod)obj).modId;
	}
	
	/** {@inheritDoc} */
	@Override
	public int compareTo(LcnAddrMod other)  {
		return this.hashCode() - other.hashCode(); 
	}
	
	/** {@inheritDoc} */
	@Override
	public String toString() {
		return this.isValid() ? String.format("S%03dM%03d", this.segId, this.modId) : "Invalid";
	}

}
