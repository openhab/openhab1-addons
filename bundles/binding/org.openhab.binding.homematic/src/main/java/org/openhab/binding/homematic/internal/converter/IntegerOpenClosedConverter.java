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
package org.openhab.binding.homematic.internal.converter;

import org.openhab.core.library.types.OpenClosedType;

/**
 * Converts an Integer to an OpenClosedValue. The given Integer is considered to
 * be 0 for Open and all others are considered as closed
 * 
 * 
 */
public class IntegerOpenClosedConverter extends StateConverter<Integer, OpenClosedType> {

    @Override
    protected OpenClosedType convertToImpl(Integer source) {
        return (source == 0) ? OpenClosedType.CLOSED : OpenClosedType.OPEN;
    }

    @Override
    protected Integer convertFromImpl(OpenClosedType source) {
        return source.equals(OpenClosedType.CLOSED) ? 0 : 1;
    }

}
