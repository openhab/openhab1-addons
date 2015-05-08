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

public class SML_GetProfilePackRes extends Sequence {
	protected OctetString serverId;
	protected SML_Time actTime;
	protected Unsigned32 regPeriod;
	protected SML_TreePath parameterTreePath;
	protected List_of_SML_ProfObjHeaderEntry header_List;
	protected List_of_SML_ProfObjPeriodEntry period_List;
	protected OctetString rawdata; // OPTIONAL,
	protected SML_Signature periodSignature; // OPTIONAL

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

	public List_of_SML_ProfObjHeaderEntry getHeader_List() {
		return header_List;
	}

	public List_of_SML_ProfObjPeriodEntry getPeriod_List() {
		return period_List;
	}

	public OctetString getRawdata() {
		return rawdata;
	}

	public SML_Signature getPeriodSignature() {
		return periodSignature;
	}

	/**
	 * 
	 * @param serverId
	 * @param actTime
	 * @param regPeriod
	 * @param parameterTreePath
	 * @param headerList
	 * @param periodList
	 * @param rawdata
	 *            OPTIONAL
	 * @param periodSignature
	 *            OPTIONAL
	 */
	public SML_GetProfilePackRes(OctetString serverId, SML_Time actTime, Unsigned32 regPeriod,
			SML_TreePath parameterTreePath, List_of_SML_ProfObjHeaderEntry headerList,
			List_of_SML_ProfObjPeriodEntry periodList, OctetString rawdata, SML_Signature periodSignature) {

		this.serverId = serverId;
		this.actTime = actTime;
		this.regPeriod = regPeriod;
		this.parameterTreePath = parameterTreePath;
		header_List = headerList;
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

	public SML_GetProfilePackRes() {
	}

	public void setOptionalAndSeq() {
		rawdata.setOptional();
		periodSignature.setOptional();

		seqArray = new ASNObject[] { serverId, actTime, regPeriod, parameterTreePath, header_List, period_List,
				rawdata, periodSignature };
	}

	@Override
	protected void createElements() {
		serverId = new OctetString();
		actTime = new SML_Time();
		regPeriod = new Unsigned32();
		parameterTreePath = new SML_TreePath();
		header_List = new List_of_SML_ProfObjHeaderEntry();
		period_List = new List_of_SML_ProfObjPeriodEntry();
		rawdata = new OctetString();
		periodSignature = new SML_Signature();

		setOptionalAndSeq();
	}

	@Override
	public void print() {
		if (!isOptional || isSelected) {
			System.out.println("SML_GetProfilePackRes: ");
			super.print();
		}
	}
}
