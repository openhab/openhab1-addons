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

public class SML_GetProfileListRes extends Sequence {

	protected OctetString serverId;
	protected SML_Time actTime;
	protected Unsigned32 regPeriod;
	protected SML_TreePath parameterTreePath;
	protected SML_Time valTime;
	protected Unsigned64 status;
	protected List_of_SML_PeriodEntry period_List;
	protected OctetString rawdata; // OPTIONAL,
	protected SML_Signature periodSignature; // OPTIONAL

	public void setStatus(Unsigned64 status) {
		this.status = status;
	}

	public OctetString getServerId() {
		return serverId;
	}

	public SML_Time getActTime() {
		return actTime;
	}

	public Unsigned32 getRegPeriod() {
		return regPeriod;
	}

	public SML_TreePath getParameterTreePath() {
		return parameterTreePath;
	}

	public SML_Time getValTime() {
		return valTime;
	}

	public Unsigned64 getStatus() {
		return status;
	}

	public List_of_SML_PeriodEntry getPeriod_List() {
		return period_List;
	}

	public OctetString getRawdata() {
		return rawdata;
	}

	public SML_Signature getPeriodSignature() {
		return periodSignature;
	}

	public SML_GetProfileListRes(OctetString serverId, SML_Time actTime, Unsigned32 regPeriod,
			SML_TreePath parameterTreePath, SML_Time valTime, Unsigned64 status, List_of_SML_PeriodEntry periodList,
			OctetString rawdata, SML_Signature periodSignature) {

		this.serverId = serverId;
		this.actTime = actTime;
		this.regPeriod = regPeriod;
		this.parameterTreePath = parameterTreePath;
		this.valTime = valTime;
		this.status = status;
		period_List = periodList;
		this.rawdata = rawdata;
		this.periodSignature = periodSignature;

		if (rawdata == null) {
			this.rawdata = new OctetString();
		}
		if (periodSignature == null) {
			this.periodSignature = new SML_Signature();
		}

		setOptionalAndSeq();
		isSelected = true;
	}

	public SML_GetProfileListRes() {
	}

	public void setOptionalAndSeq() {
		rawdata.setOptional();
		periodSignature.setOptional();

		seqArray = new ASNObject[] { serverId, actTime, regPeriod, parameterTreePath, valTime, status, period_List,
				rawdata, periodSignature };
	}

	@Override
	protected void createElements() {
		serverId = new OctetString();
		actTime = new SML_Time();
		regPeriod = new Unsigned32();
		parameterTreePath = new SML_TreePath();
		valTime = new SML_Time();
		status = new Unsigned64();
		period_List = new List_of_SML_PeriodEntry();
		rawdata = new OctetString();
		periodSignature = new SML_Signature();

		setOptionalAndSeq();
	}

	@Override
	public void print() {
		if (!isOptional || isSelected) {
			System.out.println("SML_GetProfileListRes: ");
			super.print();
		}
	}
}
