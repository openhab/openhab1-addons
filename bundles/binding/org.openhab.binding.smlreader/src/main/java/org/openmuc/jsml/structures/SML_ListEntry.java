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

public class SML_ListEntry extends Sequence {

	protected OctetString objName;
	protected SML_Status status; // OPTIONAL,
	protected SML_Time valTime; // OPTIONAL,
	protected SML_Unit unit; // OPTIONAL,
	protected Integer8 scaler; // OPTIONAL,
	protected SML_Value value;
	protected SML_Signature valueSignature; // OPTIONAL

	public OctetString getObjName() {
		return objName;
	}

	public SML_Status getStatus() {
		return status;
	}

	public SML_Time getValTime() {
		return valTime;
	}

	public SML_Unit getUnit() {
		return unit;
	}

	public Integer8 getScaler() {
		return scaler;
	}

	public SML_Value getValue() {
		return value;
	}

	public SML_Signature getValueSignature() {
		return valueSignature;
	}

	public SML_ListEntry(OctetString objName, SML_Status status, SML_Time valTime, SML_Unit unit, Integer8 scaler,
			SML_Value value, SML_Signature valueSignature) {

		if (objName == null) {
			throw new IllegalArgumentException("SML_ListEntry: objName is not optional and must not be null!");
		}
		if (value == null) {
			throw new IllegalArgumentException("SML_ListEntry: value is not optional and must not be null!");
		}

		this.objName = objName;
		this.status = status;
		this.valTime = valTime;
		this.unit = unit;
		this.scaler = scaler;
		this.value = value;
		this.valueSignature = valueSignature;

		if (this.status == null) {
			this.status = new SML_Status();
		}
		if (this.valTime == null) {
			this.valTime = new SML_Time();
		}
		if (this.unit == null) {
			this.unit = new SML_Unit();
		}
		if (this.scaler == null) {
			this.scaler = new Integer8();
		}
		if (this.valueSignature == null) {
			this.valueSignature = new SML_Signature();
		}

		setOptionalAndSeq();
		isSelected = true;
	}

	public SML_ListEntry() {
	}

	public void setOptionalAndSeq() {
		status.setOptional();
		valTime.setOptional();
		unit.setOptional();
		scaler.setOptional();
		valueSignature.setOptional();

		seqArray = new ASNObject[] { objName, status, valTime, unit, scaler, value, valueSignature };
	}

	@Override
	protected void createElements() {
		objName = new OctetString();
		status = new SML_Status();
		valTime = new SML_Time();
		unit = new SML_Unit();
		scaler = new Integer8();
		value = new SML_Value();
		valueSignature = new SML_Signature();
		setOptionalAndSeq();
	}

	@Override
	public void print() {
		if (!isOptional || isSelected) {
			System.out.println("SML_ListEntry: ");
			super.print();
		}
	}
}
