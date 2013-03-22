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
package org.openhab.core.library.items;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.HSBType;
import org.openhab.core.library.types.IncreaseDecreaseType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.PercentType;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.openhab.core.types.UnDefType;

/**
 * A ColorItem can be used for color values, e.g. for LED lights
 * 
 * @author Kai Kreuzer
 * @since 1.2.0
 *
 */public class ColorItem extends DimmerItem {

		private static List<Class<? extends State>> acceptedDataTypes = new ArrayList<Class<? extends State>>();
		private static List<Class<? extends Command>> acceptedCommandTypes = new ArrayList<Class<? extends Command>>();

		static {
			acceptedDataTypes.add(OnOffType.class);
			acceptedDataTypes.add(PercentType.class);
			acceptedDataTypes.add(HSBType.class);
			acceptedDataTypes.add(UnDefType.class);

			acceptedCommandTypes.add(OnOffType.class);		
			acceptedCommandTypes.add(IncreaseDecreaseType.class);
			acceptedCommandTypes.add(PercentType.class);
			acceptedCommandTypes.add(HSBType.class);
		}
		
		public ColorItem(String name) {
			super(name);
		}

		public void send(HSBType command) {
			internalSend(command);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void setState(State state) {
			State currentState = this.state;
			
			if(currentState instanceof HSBType) {
				DecimalType hue = ((HSBType) currentState).getHue();
				PercentType saturation = ((HSBType) currentState).getSaturation();
				// we map ON/OFF values to dark/bright, so that the hue and saturation values are not changed 
				if(state==OnOffType.OFF) {
					super.setState(new HSBType(hue, saturation, PercentType.ZERO));
				} else if(state==OnOffType.ON) {
					super.setState(new HSBType(hue, saturation, PercentType.HUNDRED));
				} else if(state instanceof PercentType && !(state instanceof HSBType)) {
					super.setState(new HSBType(hue, saturation, (PercentType) state));
				} else {
					super.setState(state);
				}
			} else {
				// we map ON/OFF values to black/white and percentage values to grey scale 
				if(state==OnOffType.OFF) {
					super.setState(HSBType.BLACK);
				} else if(state==OnOffType.ON) {
					super.setState(HSBType.WHITE);
				} else if(state instanceof PercentType && !(state instanceof HSBType)) {
					super.setState(new HSBType(DecimalType.ZERO, PercentType.ZERO, (PercentType) state));
				} else {
					super.setState(state);
				}
			}
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public State getStateAs(Class<? extends State> typeClass) {
			if(typeClass==HSBType.class) {
				return this.state;
			} else if(typeClass==OnOffType.class) {
				if(state instanceof HSBType) {
					HSBType hsbState = (HSBType) state;
					// if brightness is not completely off, we consider the state to be on
					return hsbState.getBrightness().equals(PercentType.ZERO) ? OnOffType.OFF : OnOffType.ON;					
				}
			} else if(typeClass==DecimalType.class) {
				if(state instanceof HSBType) {
					HSBType hsbState = (HSBType) state;
					return new DecimalType(hsbState.getBrightness().toBigDecimal().divide(new BigDecimal(100), 8, RoundingMode.UP));
				}
			}
			return super.getStateAs(typeClass);
		}
}
