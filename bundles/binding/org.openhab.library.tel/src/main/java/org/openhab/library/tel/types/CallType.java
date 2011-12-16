/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2011, openHAB.org <admin@openhab.org>
 *
 * See the contributors.txt file in the distribution for a
 * full listing of individual contributors.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see <http://www.gnu.org/licenses>.
 *
 * Additional permission under GNU GPL version 3 section 7
 *
 * If you modify this Program, or any covered work, by linking or
 * combining it with Eclipse (or a modified version of that library),
 * containing parts covered by the terms of the Eclipse Public License
 * (EPL), the licensors of this Program grant you additional permission
 * to convey the resulting work.
 */

package org.openhab.library.tel.types;

import java.util.SortedMap;
import java.util.TreeMap;

import org.openhab.core.library.types.StringType;
import org.openhab.core.types.Command;
import org.openhab.core.types.ComplexType;
import org.openhab.core.types.PrimitiveType;
import org.openhab.core.types.State;


public class CallType implements ComplexType, Command, State {
	
	enum CallTypeKeys {
		ORIG_NUM, DEST_NUM;
	}

	public static final State EMPTY = new CallType();
	

	private SortedMap<String, PrimitiveType> callDetails;

	
	public CallType() {
		callDetails = new TreeMap<String, PrimitiveType>();
	}
	
	public CallType(StringType origNum, StringType destNum, StringType stateDesc) {
		this();
		callDetails.put(CallTypeKeys.ORIG_NUM.toString(), origNum);
		callDetails.put(CallTypeKeys.DEST_NUM.toString(), destNum);
	}
	
	
	public SortedMap<String, PrimitiveType> getConstituents() {
		return callDetails;
	}
	
	public PrimitiveType getOrigNum() {
		return callDetails.get(CallTypeKeys.ORIG_NUM.toString());
	}
	
	public PrimitiveType getDestNum() {
		return callDetails.get(CallTypeKeys.DEST_NUM.toString());
	}
	
	
	@Override
	public String toString() {
		return "CallType [callDetails=" + callDetails + "]";
	}
	

}
