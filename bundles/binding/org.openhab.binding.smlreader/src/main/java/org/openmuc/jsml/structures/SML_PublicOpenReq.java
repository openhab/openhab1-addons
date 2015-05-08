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

public class SML_PublicOpenReq extends Sequence {

	protected OctetString codepage;
	protected OctetString clientId;
	protected OctetString reqFileId;
	protected OctetString serverId;
	protected OctetString username;
	protected OctetString password;
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

	public OctetString getUsername() {
		return username;
	}

	public OctetString getPassword() {
		return password;
	}

	public Unsigned8 getSmlVersion() {
		return smlVersion;
	}

	public SML_PublicOpenReq(OctetString codepage, OctetString clientId, OctetString reqFileId, OctetString serverId,
			OctetString username, OctetString password, Unsigned8 smlVersion) {
		if (codepage != null) {
			this.codepage = codepage;
		}
		else {
			this.codepage = new OctetString();
		}

		this.clientId = clientId;
		this.reqFileId = reqFileId;

		if (serverId != null) {
			this.serverId = serverId;
		}
		else {
			this.serverId = new OctetString();
		}

		if (username != null) {
			this.username = username;
		}
		else {
			this.username = new OctetString();
		}

		if (password != null) {
			this.password = password;
		}
		else {
			this.password = new OctetString();
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

	public SML_PublicOpenReq() {
	}

	public void setOptionalAndSeq() {
		codepage.setOptional();
		serverId.setOptional();
		username.setOptional();
		password.setOptional();
		smlVersion.setOptional();

		seqArray = new ASNObject[] { codepage, clientId, reqFileId, serverId, username, password, smlVersion };

	}

	@Override
	protected void createElements() {
		codepage = new OctetString();
		clientId = new OctetString();
		reqFileId = new OctetString();
		serverId = new OctetString();
		username = new OctetString();
		password = new OctetString();
		smlVersion = new Unsigned8();

		setOptionalAndSeq();
	}

}
