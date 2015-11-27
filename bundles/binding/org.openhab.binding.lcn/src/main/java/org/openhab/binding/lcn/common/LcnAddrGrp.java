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
 * Represents an LCN group address.
 * Can be used as a key in maps.
 * Hash codes are guaranteed to be unique as long as {@link #isValid()} is true.
 *  
 * @author Tobias Jüttner
 */
public class LcnAddrGrp extends LcnAddr implements Comparable<LcnAddrMod> {
	
	/** The group id. */
	private final int grpId;
	
	/** Constructs an empty / invalid group address. */
	public LcnAddrGrp() {
		this.grpId = -1;  // Invalid
	}
	
	/**
	 * Constructs a group address with (logical) segment id and group id.
	 * 
	 * @param segId the segment id
	 * @param grpId the group id
	 */
	public LcnAddrGrp(int segId, int grpId) {
		super(segId);
		this.grpId = grpId;
	}
	
	/**
	 * Gets the group id.
	 * 
	 * @return the group id
	 */
	public int getGrpId() {
		return this.grpId;
	}	
	
	/** {@inheritDoc} */
	@Override
	public boolean isValid() {
		// segId:
		// 0 = Local, 1..2 = Not allowed (but "seen in the wild")
		// 3 = Broadcast, 4 = Status messages, 5..127, 128 = Segment-bus disabled (valid value)
		// grpId:
		// 3 = Broadcast, 4 = Status messages, 5..254
		return this.segId >= 0 && this.segId <= 128 && this.grpId >= 3 && this.grpId <= 254;
	}
	
	/** {@inheritDoc} */
	@Override
	public boolean isGroup() {
		return true;
	}
	
	/** {@inheritDoc} */
	@Override
	public int getId() {
		return this.grpId;
	}
	
	/** {@inheritDoc} */
	@Override
	public int hashCode() {
		// Reversing the bits helps to generate better balanced trees as ids tend to be "user-sorted"
		try {
			if (this.isValid()) {
				return ReverseNumber.reverseUInt8(this.grpId) << 8 + ReverseNumber.reverseUInt8(this.segId);
			}
		} catch (IllegalArgumentException ex) { }
		return -1;
	}
	
	/** {@inheritDoc} */
	@Override
	public boolean equals(Object obj) {		
		if (!(obj instanceof LcnAddrGrp)) {
			return false;
		}
		return this.segId == ((LcnAddrGrp)obj).segId && this.grpId == ((LcnAddrGrp)obj).grpId;
	}
	
	/** {@inheritDoc} */
	@Override
	public int compareTo(LcnAddrMod other)  {
		return this.hashCode() - other.hashCode(); 
	}
	
	/** {@inheritDoc} */
	@Override
	public String toString() {
		return this.isValid() ? String.format("S%03dG%03d", this.segId, this.grpId) : "Invalid";
	}

}
