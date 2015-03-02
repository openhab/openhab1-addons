/*
 * Copyright 2009-14 Fraunhofer ISE
 *
 * This file is part of jSML.
 * For more information visit http://www.openmuc.org
 *
 * jSML is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 2.1 of the License, or
 * (at your option) any later version.
 *
 * jSML is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with jSML.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package org.openmuc.jsml.structures;

public class SML_TimestampLocal extends Sequence {

	protected SML_Timestamp timestamp;
	protected Integer16 localOffset;
	protected Integer16 seasonTimeOffset;

	public SML_Timestamp getTimestamp() {
		return timestamp;
	}

	public Integer16 getLocalOffset() {
		return localOffset;
	}

	public Integer16 getSeasonTimeOffset() {
		return seasonTimeOffset;
	}

	public SML_TimestampLocal(SML_Timestamp timestamp, Integer16 localOffset, Integer16 seasonTimeOffset) {

		if (timestamp == null) {
			throw new IllegalArgumentException("SML_TimestampLocal: timestamp is not optional and must not be null!");
		}
		if (localOffset == null) {
			throw new IllegalArgumentException("SML_TimestampLocal: localOffset is not optional and must not be null!");
		}
		if (seasonTimeOffset == null) {
			throw new IllegalArgumentException(
					"SML_TimestampLocal: seasonTimeOffset is not optional and must not be null!");
		}

		this.timestamp = timestamp;
		this.localOffset = localOffset;
		this.seasonTimeOffset = seasonTimeOffset;

		setOptionalAndSeq();
		isSelected = true;
	}

	public SML_TimestampLocal() {
	}

	public void setOptionalAndSeq() {
		seqArray = new ASNObject[] { timestamp, localOffset, seasonTimeOffset };
	}

	@Override
	protected void createElements() {
		timestamp = new SML_Timestamp();
		localOffset = new Integer16();
		seasonTimeOffset = new Integer16();
		setOptionalAndSeq();
	}

	@Override
	public void print() {
		if (!isOptional || isSelected) {
			System.out.println("SML_TimestampLocal: ");
			super.print();
		}
	}

}
