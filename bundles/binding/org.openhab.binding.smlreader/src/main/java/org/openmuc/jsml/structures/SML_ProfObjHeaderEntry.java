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

public class SML_ProfObjHeaderEntry extends Sequence {

	protected OctetString objName;
	protected SML_Unit unit;
	protected Integer8 scaler;

	public OctetString getObjName() {
		return objName;
	}

	public SML_Unit getUnit() {
		return unit;
	}

	public Integer8 getScaler() {
		return scaler;
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

	public SML_ProfObjHeaderEntry(OctetString objName, SML_Unit unit, Integer8 scaler) {
		this.objName = objName;
		this.unit = unit;
		this.scaler = scaler;

		setOptionalAndSeq();
		isSelected = true;
	}

	public SML_ProfObjHeaderEntry() {
	}

	public void setOptionalAndSeq() {
		seqArray = new ASNObject[] { objName, unit, scaler };
	}

	@Override
	protected void createElements() {
		objName = new OctetString();
		unit = new SML_Unit();
		scaler = new Integer8();

		setOptionalAndSeq();
	}

}
