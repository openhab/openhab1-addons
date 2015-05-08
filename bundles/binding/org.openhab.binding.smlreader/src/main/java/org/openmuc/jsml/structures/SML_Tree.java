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

public class SML_Tree extends Sequence {

	protected OctetString parameterName;
	protected SML_ProcParValue parameterValue; // OPTIONAL
	protected List_of_SML_Tree child_List; // OPTIONAL

	public OctetString getParameterName() {
		return parameterName;
	}

	public SML_ProcParValue getParameterValue() {
		return parameterValue;
	}

	public List_of_SML_Tree getChild_List() {
		return child_List;
	}

	public SML_Tree(OctetString parameterName, SML_ProcParValue parameterValue, List_of_SML_Tree child_List) {
		this.parameterName = parameterName;
		if (parameterValue != null) {
			this.parameterValue = parameterValue;
		}
		else {
			this.parameterValue = new SML_ProcParValue();
		}
		if (child_List != null) {
			this.child_List = child_List;
		}
		else {
			this.child_List = new List_of_SML_Tree();
		}

		setOptionalAndSeq();
		isSelected = true;
	}

	public SML_Tree() {
	}

	public void setOptionalAndSeq() {
		parameterValue.setOptional();
		child_List.setOptional();
		seqArray = new ASNObject[] { parameterName, parameterValue, child_List };
	}

	@Override
	protected void createElements() {
		parameterName = new OctetString();
		parameterValue = new SML_ProcParValue();
		child_List = new List_of_SML_Tree();
		setOptionalAndSeq();
	}

	@Override
	public void print() {
		if (!isOptional || isSelected) {
			System.out.println("SML_Tree: ");
			super.print();
		}
	}

}
