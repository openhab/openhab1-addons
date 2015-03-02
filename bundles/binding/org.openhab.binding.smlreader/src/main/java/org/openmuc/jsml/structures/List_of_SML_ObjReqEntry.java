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

public class List_of_SML_ObjReqEntry extends SequenceOf {

	protected OctetString[] object_List_Entry;

	public List_of_SML_ObjReqEntry(OctetString[] object_List_Entry) {
		this.object_List_Entry = object_List_Entry;
		seqArray = object_List_Entry;
		isSelected = true;
	}

	public List_of_SML_ObjReqEntry() {
	}

	@Override
	protected void createElements(int length) {
		object_List_Entry = new OctetString[length];
		for (int i = 0; i < length; i++) {
			object_List_Entry[i] = new OctetString();
		}
		seqArray = object_List_Entry;
	}

	public OctetString[] getObjectListEntry() {
		return object_List_Entry;
	}
}
