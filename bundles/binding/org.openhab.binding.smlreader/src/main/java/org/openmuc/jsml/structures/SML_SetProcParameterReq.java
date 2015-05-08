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

public class SML_SetProcParameterReq extends Sequence {

	protected OctetString serverId; // OPTIONAL
	protected OctetString username; // OPTIONAL
	protected OctetString password; // OPTIONAL
	protected SML_TreePath parameterTreePath;
	protected SML_Tree parameterTree;

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

	public SML_Tree getParameterTree() {
		return parameterTree;
	}

	public SML_SetProcParameterReq(OctetString serverId, OctetString username, OctetString password,
			SML_TreePath parameterTreePath, SML_Tree parameterTree) {

		if (parameterTreePath == null) {
			throw new IllegalArgumentException(
					"SML_SetProcParameterReq: parameterTreePath is not optional and must not be null!");
		}
		if (parameterTree == null) {
			throw new IllegalArgumentException(
					"SML_SetProcParameterReq: parameterTree is not optional and must not be null!");
		}

		this.serverId = serverId;
		this.username = username;
		this.password = password;
		this.parameterTreePath = parameterTreePath;
		this.parameterTree = parameterTree;

		if (serverId == null) {
			this.serverId = new OctetString();
		}
		if (username == null) {
			this.username = new OctetString();
		}
		if (password == null) {
			this.password = new OctetString();
		}

		setOptionalAndSeq();
		isSelected = true;
	}

	public SML_SetProcParameterReq() {
	}

	public void setOptionalAndSeq() {
		serverId.setOptional();
		username.setOptional();
		password.setOptional();

		seqArray = new ASNObject[] { serverId, username, password, parameterTreePath, parameterTree };
	}

	@Override
	protected void createElements() {
		serverId = new OctetString();
		username = new OctetString();
		password = new OctetString();
		parameterTreePath = new SML_TreePath();
		parameterTree = new SML_Tree();

		setOptionalAndSeq();
	}

	@Override
	public void print() {
		if (!isOptional || isSelected) {
			System.out.println("SML_SetProcParameterReq: ");
			super.print();
		}
	}
}
