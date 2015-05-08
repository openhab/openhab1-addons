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

public class SML_GetProcParameterRes extends Sequence {

	protected OctetString serverId;
	protected SML_TreePath parameterTreePath;
	protected SML_Tree parameterTree;

	public OctetString getServerId() {
		return serverId;
	}

	public SML_TreePath getParameterTreePath() {
		return parameterTreePath;
	}

	public SML_Tree getParameterTree() {
		return parameterTree;
	}

	public SML_GetProcParameterRes(OctetString serverId, SML_TreePath parameterTreePath, SML_Tree parameterTree) {

		if (serverId == null) {
			throw new IllegalArgumentException(
					"SML_GetProcParameterRes: serverId is not optional and must not be null!");
		}
		if (parameterTreePath == null) {
			throw new IllegalArgumentException(
					"SML_GetProcParameterRes: parameterTreePath is not optional and must not be null!");
		}
		if (parameterTree == null) {
			throw new IllegalArgumentException(
					"SML_GetProcParameterRes: parameterTree is not optional and must not be null!");
		}

		this.serverId = serverId;
		this.parameterTreePath = parameterTreePath;
		this.parameterTree = parameterTree;

		setOptionalAndSeq();
		isSelected = true;
	}

	public SML_GetProcParameterRes() {
	}

	public void setOptionalAndSeq() {
		seqArray = new ASNObject[] { serverId, parameterTreePath, parameterTree };
	}

	@Override
	protected void createElements() {
		serverId = new OctetString();
		parameterTreePath = new SML_TreePath();
		parameterTree = new SML_Tree();
		setOptionalAndSeq();
	}

	@Override
	public void print() {
		if (!isOptional || isSelected) {
			System.out.println("SML_GetProcParameterRes: ");
			super.print();
		}
	}
}
