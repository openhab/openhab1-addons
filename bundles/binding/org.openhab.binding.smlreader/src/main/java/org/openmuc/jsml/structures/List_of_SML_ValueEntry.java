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

public class List_of_SML_ValueEntry extends SequenceOf {
	protected SML_ValueEntry[] value_List_Entries;

	public List_of_SML_ValueEntry(SML_ValueEntry[] value_List_Entries) {
		this.value_List_Entries = value_List_Entries;
		seqArray = value_List_Entries;
		isSelected = true;
	}

	public List_of_SML_ValueEntry() {
	}

	@Override
	protected void createElements(int length) {
		value_List_Entries = new SML_ValueEntry[length];
		for (int i = 0; i < length; i++) {
			value_List_Entries[i] = new SML_ValueEntry();
		}
		seqArray = value_List_Entries;
	}

	public SML_ValueEntry[] getvalue_List_Entries() {
		return value_List_Entries;
	}
}
