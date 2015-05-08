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

public class SML_GetProcParameterReq extends Sequence {

	protected OctetString serverId; // OPTIONAL
	protected OctetString username; // OPTIONAL
	protected OctetString password; // OPTIONAL
	protected SML_TreePath parameterTreePath;
	protected OctetString attribute; // OPTIONAL

	public OctetString getServerId() {
		return serverId;
	}

	public OctetString getUsername() {
		return username;
	}

	public OctetString getPassword() {
		return password;
	}

	public SML_TreePath getParameterTreePath() {
		return parameterTreePath;
	}

	public OctetString getAttribute() {
		return attribute;
	}

	/**
	 * 
	 * @param serverId
	 *            OPTIONAL
	 * @param username
	 *            OPTIONAL
	 * @param password
	 *            OPTIONAL
	 * @param parameterTreePath
	 * @param attribute
	 *            OPTIONAL
	 */
	public SML_GetProcParameterReq(OctetString serverId, OctetString username, OctetString password,
			SML_TreePath parameterTreePath, OctetString attribute) {

		if (parameterTreePath == null) {
			throw new IllegalArgumentException(
					"SML_GetProcParameterReq: parameterTreePath is not optional and must not be null!");
		}

		this.serverId = serverId;
		this.username = username;
		this.password = password;
		this.parameterTreePath = parameterTreePath;
		this.attribute = attribute;

		if (this.serverId == null) {
			this.serverId = new OctetString();
		}
		if (this.username == null) {
			this.username = new OctetString();
		}
		if (this.password == null) {
			this.password = new OctetString();
		}
		if (this.attribute == null) {
			this.attribute = new OctetString();
		}

		setOptionalAndSeq();
		isSelected = true;
	}

	public SML_GetProcParameterReq() {
	}

	public void setOptionalAndSeq() {
		serverId.setOptional();
		username.setOptional();
		password.setOptional();
		attribute.setOptional();

		seqArray = new ASNObject[] { serverId, username, password, parameterTreePath, attribute };
	}

	@Override
	protected void createElements() {
		serverId = new OctetString();
		username = new OctetString();
		password = new OctetString();
		parameterTreePath = new SML_TreePath();
		attribute = new OctetString();

		setOptionalAndSeq();
	}

	@Override
	public void print() {
		if (!isOptional || isSelected) {
			System.out.println("SML_GetProcParameterReq: ");
			super.print();
		}
	}
}
