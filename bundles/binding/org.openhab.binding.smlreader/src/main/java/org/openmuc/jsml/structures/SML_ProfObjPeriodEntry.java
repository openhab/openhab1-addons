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

public class SML_ProfObjPeriodEntry extends Sequence {

	protected SML_Time valTime;
	protected Unsigned64 status;
	protected List_of_SML_ValueEntry value_List;
	protected SML_Signature periodSignature; // OPTIONAL

	public SML_Time getValTime() {
		return valTime;
	}

	public Unsigned64 getStatus() {
		return status;
	}

	public List_of_SML_ValueEntry getValue_List() {
		return value_List;
	}

	public SML_Signature getPeriodSignature() {
		return periodSignature;
	}

	public void setValTime(SML_Time valTime) {
		this.valTime = valTime;
	}

	public void setStatus(Unsigned64 status) {
		this.status = status;
	}

	public void setValue_List(List_of_SML_ValueEntry value_List) {
		this.value_List = value_List;
	}

	public void setPeriodSignature(SML_Signature periodSignature) {
		this.periodSignature = periodSignature;
	}

	public SML_ProfObjPeriodEntry(SML_Time valTime, Unsigned64 status, List_of_SML_ValueEntry value_List,
			SML_Signature periodSignature) {
		this.valTime = valTime;
		this.status = status;
		this.value_List = value_List;
		this.periodSignature = periodSignature;

		if (this.periodSignature == null) {
			this.periodSignature = new SML_Signature();
		}

		setOptionalAndSeq();
		isSelected = true;
	}

	public SML_ProfObjPeriodEntry() {
	}

	public void setOptionalAndSeq() {
		periodSignature.setOptional();
		seqArray = new ASNObject[] { valTime, status, value_List, periodSignature };
	}

	@Override
	protected void createElements() {
		valTime = new SML_Time();
		status = new Unsigned64();
		value_List = new List_of_SML_ValueEntry();
		periodSignature = new SML_Signature();

		setOptionalAndSeq();
	}

}
