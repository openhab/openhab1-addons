/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010-2013, openHAB.org <admin@openhab.org>
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
package org.openhab.core.types;

/**
 * There are situations when item states do not have any defined value.
 * This might be because they have not been initialized yet (never
 * received an state update so far) or because their state is ambiguous
 * (e.g. a dimmed light that is treated as a switch (ON/OFF) will have
 * an undefined state if it is dimmed to 50%).
 * 
 * @author Kai Kreuzer
 * @since 0.1.0
 *
 */
public enum UnDefType implements PrimitiveType, State {
	UNDEF, NULL;
	
	public String toString() {
		switch(this) {
			case UNDEF: return "Undefined";
			case NULL:  return "Uninitialized";
		}
		return "";
	}
	
	public String format(String pattern) {
		return String.format(pattern, this.toString());
	}

}
