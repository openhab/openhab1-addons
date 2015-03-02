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

public class SML_PeriodEntry extends Sequence {

	protected OctetString objName;
	protected SML_Unit unit;
	protected Integer8 scaler;
	protected SML_Value value;
	protected SML_Signature valueSignature; // OPTIONAL

	public OctetString getObjName() {
		return objName;
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

	public void setObjName(OctetString objName) {
		this.objName = objName;
	}

	public void setUnit(SML_Unit unit) {
		this.unit = unit;
	}

	public void setScaler(Integer8 scaler) {
		this.scaler = scaler;
	}

	public void setValue(SML_Value value) {
		this.value = value;
	}

	public void setValueSignature(SML_Signature valueSignature) {
		this.valueSignature = valueSignature;
	}

	public SML_PeriodEntry(OctetString objName, SML_Unit unit, Integer8 scaler, SML_Value value,
			SML_Signature valueSignature) {
		this.objName = objName;
		this.unit = unit;
		this.scaler = scaler;
		this.value = value;
		this.valueSignature = valueSignature;

		if (this.valueSignature == null) {
			this.valueSignature = new SML_Signature();
		}

		setOptionalAndSeq();
		isSelected = true;
	}

	public SML_PeriodEntry() {
	}

	public void setOptionalAndSeq() {
		valueSignature.setOptional();
		seqArray = new ASNObject[] { objName, unit, scaler, value, valueSignature };
	}

	@Override
	protected void createElements() {
		objName = new OctetString();
		unit = new SML_Unit();
		scaler = new Integer8();
		value = new SML_Value();
		valueSignature = new SML_Signature();

		setOptionalAndSeq();
	}

}
