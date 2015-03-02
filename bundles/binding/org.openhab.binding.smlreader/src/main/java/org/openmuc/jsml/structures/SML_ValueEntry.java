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

public class SML_ValueEntry extends Sequence {

	protected SML_Value value;
	protected SML_Signature valueSignature; // OPTIONAL

	public SML_Value getValue() {
		return value;
	}

	public SML_Signature getValueSignature() {
		return valueSignature;
	}

	public void setValue(SML_Value value) {
		this.value = value;
	}

	public void setValueSignature(SML_Signature valueSignature) {
		this.valueSignature = valueSignature;
	}

	public SML_ValueEntry(SML_Value value, SML_Signature valueSignature) {
		this.value = value;
		this.valueSignature = valueSignature;

		if (this.valueSignature == null) {
			this.valueSignature = new SML_Signature();
		}

		setOptionalAndSeq();
		isSelected = true;
	}

	public SML_ValueEntry() {
	}

	public void setOptionalAndSeq() {
		valueSignature.setOptional();
		seqArray = new ASNObject[] { value, valueSignature };
	}

	@Override
	protected void createElements() {
		value = new SML_Value();
		valueSignature = new SML_Signature();

		setOptionalAndSeq();
	}

}
