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

public class SML_PublicOpenRes extends Sequence {

	protected OctetString codepage;
	protected OctetString clientId;
	protected OctetString reqFileId;
	protected OctetString serverId;
	protected SML_Time refTime;
	protected Unsigned8 smlVersion;

	public OctetString getCodepage() {
		return codepage;
	}

	public OctetString getClientId() {
		return clientId;
	}

	public OctetString getReqFileId() {
		return reqFileId;
	}

	public OctetString getServerId() {
		return serverId;
	}

	public SML_Time getRefTime() {
		return refTime;
	}

	public Unsigned8 getSmlVersion() {
		return smlVersion;
	}

	public SML_PublicOpenRes(OctetString codepage, OctetString clientId, OctetString reqFileId, OctetString serverId,
			SML_Time refTime, Unsigned8 smlVersion) {

		if (codepage != null) {
			this.codepage = codepage;
		}
		else {
			this.codepage = new OctetString();
		}

		if (clientId != null) {
			this.clientId = clientId;
		}
		else {
			this.clientId = new OctetString();
		}

		this.reqFileId = reqFileId;
		this.serverId = serverId;

		if (refTime != null) {
			this.refTime = refTime;
		}
		else {
			this.refTime = new SML_Time();
		}

		if (smlVersion != null) {
			this.smlVersion = smlVersion;
		}
		else {
			this.smlVersion = new Unsigned8();
		}

		setOptionalAndSeq();

		isSelected = true;
	}

	public SML_PublicOpenRes() {
	}

	public void setOptionalAndSeq() {
		codepage.setOptional();
		clientId.setOptional();
		refTime.setOptional();
		smlVersion.setOptional();

		seqArray = new ASNObject[] { codepage, clientId, reqFileId, serverId, refTime, smlVersion };

	}

	@Override
	protected void createElements() {
		codepage = new OctetString();
		clientId = new OctetString();
		reqFileId = new OctetString();
		serverId = new OctetString();
		refTime = new SML_Time();
		smlVersion = new Unsigned8();

		setOptionalAndSeq();
	}

}
