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
package org.openhab.binding.urtsi.internal;

/**
 * The ArrayHelper is needed due to a limitation in Xtend:
 * https://groups.google.com/forum/#!msg/xtend-lang/p33jDUNh0Hg/z4u7B7BHhJgJ
 * 
 * After a Xtend update to >= 2.4.0 it can be replaced by using ArrayLiterals: 
 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=377904)
 * @author Oliver Libutzki
 * @since 1.3.0
 *
 */
public class ArrayHelper {

	/**
	 * The method returns a byte[] of the given size. 
	 * @param size of the resulting byte[]
	 * @return
	 */
	public static byte[] getByteArray(int size) {
		return new byte[size];
	}
}
