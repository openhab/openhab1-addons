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
 * Represents an LCN address (module or group).
 * 
 * @author Tobias Jüttner
 */
public abstract class LcnAddr {
	
	/** The logical segment id (not replaced with 0 for "local"). */
	protected final int segId;

	/** Constructs an empty / invalid address. */
	public LcnAddr() {
		this.segId = -1;  // Invalid
	}
	
	/**
	 * Constructs an address with a (logical) segment id.
	 * 
	 * @param segId the segment id
	 */
	public LcnAddr(int segId) {
		this.segId = segId;
	}
	
	/**
	 * Gets the (logical) segment id.
	 * 
	 * @return the segment id
	 */
	public int getSegId() {
		return this.segId;
	}
	
	/**
	 * Gets the physical segment id ("local" segment replaced with 0).
	 * Can be used to send data into the LCN bus.  
	 * 
	 * @param localSegId the segment id of the local segment (managed by {@link Connection})
	 * @return the physical segment id
	 */
	public int getPhysicalSegId(int localSegId) {
		return this.segId == localSegId ? 0 : this.segId;
	}
	
	/**
	 * Checks the address against the LCN specification for valid addresses.
	 * 
	 * @return true if address is valid
	 */
	public abstract boolean isValid();
	
	/**
	 * Queries the concrete address type.
	 * 
	 * @return true if address is a group address (module address otherwise)
	 */
	public abstract boolean isGroup();
	
	/**
	 * Gets the address' module or group id (discarding the concrete type).
	 * 
	 * @return the module or group id
	 */
	public abstract int getId();	
	
	/** {@inheritDoc} */
	@Override
	public abstract boolean equals(Object obj);		
	
	/**
	 * Converts the address to its text representation.
	 * Should only by used for debugging.
	 * 
	 * @return the address as text
	 */
	@Override
	public abstract String toString();
	
	/** Helper to bitwise reverse numbers. */
	protected static final class ReverseNumber {
		
		/** Cache with all reversed 8 bit values. */
		private static final int[] reversedUInt8 = new int[256];
		
		/** Initializes static data once this class is first used. */
		static {
			for (int i = 0; i < 256; ++i) {
				int reversed = 0;
				for (int j = 0; j < 8; ++j) {
					if ((i & (1 << j)) != 0) {
						reversed |= (0x80 >> j);
					}
				}
				reversedUInt8[i] = reversed;
			}
		}
		
		/**
		 * Reverses the given 8 bit value bitwise.
		 * 
		 * @param value the value to reverse bitwise (treated as unsigned 8 bit value)
		 * @return the reversed value
		 * @throws IllegalArgumentException if value is out of range (not unsigned 8 bit)
		 */
		static int reverseUInt8(int value) throws IllegalArgumentException{
			if (value < 0 || value > 255) {
				throw new IllegalArgumentException();
			}
			return reversedUInt8[value];
		}
		
	}
	
}
