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

public class SML_GetListReq extends Sequence {

	protected OctetString clientId;
	protected OctetString serverId; // OPTIONAL
	protected OctetString username; // OPTIONAL
	protected OctetString password; // OPTIONAL
	protected OctetString listName; // OPTIONAL

	public OctetString getClientId() {
		return clientId;
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

	public OctetString getListName() {
		return listName;
	}

	public SML_GetListReq(OctetString clientId, OctetString serverId, OctetString username, OctetString password,
			OctetString listName) {

		if (clientId == null) {
			throw new IllegalArgumentException("SML_GetListReq: clientId is not optional and must not be null!");
		}

		this.clientId = clientId;
		this.serverId = serverId;
		this.username = username;
		this.password = password;
		this.listName = listName;

		if (this.serverId == null) {
			this.serverId = new OctetString();
		}
		if (this.username == null) {
			this.username = new OctetString();
		}
		if (this.password == null) {
			this.password = new OctetString();
		}
		if (this.listName == null) {
			this.listName = new OctetString();
		}

		setOptionalAndSeq();
		isSelected = true;
	}

	public SML_GetListReq() {
	}

	public void setOptionalAndSeq() {
		serverId.setOptional();
		username.setOptional();
		password.setOptional();
		listName.setOptional();

		seqArray = new ASNObject[] { clientId, serverId, username, password, listName };
	}

	@Override
	protected void createElements() {

		clientId = new OctetString();
		serverId = new OctetString();
		username = new OctetString();
		password = new OctetString();
		listName = new OctetString();

		setOptionalAndSeq();
	}

	@Override
	public void print() {
		if (!isOptional || isSelected) {
			System.out.println("SML_GetListReq: ");
			super.print();
		}
	}
}
