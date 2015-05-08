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

public class SML_GetListRes extends Sequence {

	protected OctetString clientId; // OPTIONAL,
	protected OctetString serverId;
	protected OctetString listName; // OPTIONAL,
	protected SML_Time actSensorTime; // OPTIONAL,
	protected SML_List valList;
	protected SML_Signature listSignature; // OPTIONAL,
	protected SML_Time actGatewayTime; // OPTIONAL

	public OctetString getClientId() {
		return clientId;
	}

	public OctetString getServerId() {
		return serverId;
	}

	public OctetString getListName() {
		return listName;
	}

	public SML_Time getActSensorTime() {
		return actSensorTime;
	}

	public SML_List getValList() {
		return valList;
	}

	public SML_Signature getListSignature() {
		return listSignature;
	}

	public SML_Time getActGatewayTime() {
		return actGatewayTime;
	}

	public SML_GetListRes(OctetString clientId, OctetString serverId, OctetString listName, SML_Time actSensorTime,
			SML_List valList, SML_Signature listSignature, SML_Time actGatewayTime) {

		if (serverId == null) {
			throw new IllegalArgumentException("SML_GetListRes: serverId is not optional and must not be null!");
		}
		if (valList == null) {
			throw new IllegalArgumentException("SML_GetListRes: valList is not optional and must not be null!");
		}

		this.clientId = clientId;
		this.serverId = serverId;
		this.listName = listName;
		this.actSensorTime = actSensorTime;
		this.valList = valList;
		this.listSignature = listSignature;
		this.actGatewayTime = actGatewayTime;

		if (this.clientId == null) {
			this.clientId = new OctetString();
		}
		if (this.listName == null) {
			this.listName = new OctetString();
		}
		if (this.actSensorTime == null) {
			this.actSensorTime = new SML_Time();
		}
		if (this.listSignature == null) {
			this.listSignature = new SML_Signature();
		}
		if (this.actGatewayTime == null) {
			this.actGatewayTime = new SML_Time();
		}

		setOptionalAndSeq();
		isSelected = true;
	}

	public SML_GetListRes() {
	}

	public void setOptionalAndSeq() {
		clientId.setOptional();
		listName.setOptional();
		actSensorTime.setOptional();
		listSignature.setOptional();
		actGatewayTime.setOptional();

		seqArray = new ASNObject[] { clientId, serverId, listName, actSensorTime, valList, listSignature,
				actGatewayTime };
	}

	@Override
	protected void createElements() {
		clientId = new OctetString();
		serverId = new OctetString();
		listName = new OctetString();
		actSensorTime = new SML_Time();
		valList = new SML_List();
		listSignature = new SML_Signature();
		actGatewayTime = new SML_Time();
		setOptionalAndSeq();
	}

	@Override
	public void print() {
		if (!isOptional || isSelected) {
			System.out.println("SML_GetListRes: ");
			super.print();
		}
	}
}
