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
package org.openhab.core.library.types;

import java.math.BigDecimal;

/** 
 * The PercentType extends the {@link DecimalType} by putting constraints for its value on top (0-100).
 * 
 * @author Kai Kreuzer
 * @since 0.1.0
 *
 */
public class PercentType extends DecimalType {
	
	private static final long serialVersionUID = -9066279845951780879L;
	
	final static public PercentType ZERO = new PercentType(0); 
	final static public PercentType HUNDRED = new PercentType(100); 
	
	public PercentType() {
		super();
	}
	
	public PercentType(int value) {
		super(value);
		validateValue(this.value);
	}

	public PercentType(String value) {
		super(value);
		validateValue(this.value);
	}

	public PercentType(BigDecimal value) {
		super(value);
		validateValue(this.value);
	}
	
	private void validateValue(BigDecimal value) {
		if(BigDecimal.ZERO.compareTo(value) > 0 || new BigDecimal(100).compareTo(value) < 0) {
			throw new IllegalArgumentException("Value must be between 0 and 100");
		}
	}
	
	public static PercentType valueOf(String value) {
		return new PercentType(value);
	}

}
