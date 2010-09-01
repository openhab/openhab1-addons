/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010, openHAB.org <admin@openhab.org>
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

package org.openhab.binding.knx.core.internal.dpt;

import org.openhab.binding.knx.core.config.KNXTypeMapper;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.OpenCloseType;
import org.openhab.core.library.types.PercentType;
import org.openhab.core.library.types.StopMoveType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.library.types.UpDownType;
import org.openhab.core.types.Type;

import tuwien.auto.calimero.datapoint.Datapoint;
import tuwien.auto.calimero.dptxlator.DPTXlator;
import tuwien.auto.calimero.dptxlator.DPTXlator8BitUnsigned;
import tuwien.auto.calimero.dptxlator.DPTXlatorBoolean;
import tuwien.auto.calimero.dptxlator.DPTXlatorString;
import tuwien.auto.calimero.dptxlator.TranslatorTypes;
import tuwien.auto.calimero.exception.KNXException;

/** 
 * This class provides type mapping between all openHAB core types and KNX data point types.
 * 
 * @author Kai Kreuzer
 * @since 0.3.0
 *
 */
public class KNXCoreTypeMapper implements KNXTypeMapper {

	@Override
	public String toDPValue(Type type) {
		if(type instanceof OnOffType) return type.toString().toLowerCase();
		if(type instanceof UpDownType) return type.toString().toLowerCase();
		if(type instanceof PercentType) return mapTo8bit((PercentType) type);
		if(type instanceof DecimalType) return type.toString();
		if(type instanceof StringType) return type.toString();
		if(type==OpenCloseType.OPEN) return "open";
		if(type==OpenCloseType.CLOSE) return "closed";
		if(type==StopMoveType.MOVE) return "start";
		if(type==StopMoveType.STOP) return "stop";
		
		return null;
	}

	@Override
	public Type toType(Datapoint datapoint, byte[] data) {
		try {
			DPTXlator translator = TranslatorTypes.createTranslator(datapoint.getMainNumber(), datapoint.getDPT());
			translator.setData(data);
			String value = translator.getValue();
			String id = translator.getType().getID();
			if(id.equals(DPTXlatorBoolean.DPT_UPDOWN.getID())) return UpDownType.valueOf(value.toUpperCase());
			if(id.equals(DPTXlatorBoolean.DPT_SWITCH.getID())) return OnOffType.valueOf(value.toUpperCase());
			if(id.equals(DPTXlator8BitUnsigned.DPT_PERCENT_U8.getID())) return PercentType.valueOf(mapToPercent(value));
			if(datapoint.getMainNumber()==9) return DecimalType.valueOf(value.substring(0, value.indexOf(" ")));
			if(id.equals(DPTXlatorString.DPT_STRING_8859_1.getID())) return StringType.valueOf(value);
			if(id.equals(DPTXlatorBoolean.DPT_OPENCLOSE.getID())) return value.equals("open")?OpenCloseType.OPEN:OpenCloseType.CLOSE;
			if(id.equals(DPTXlatorBoolean.DPT_START.getID())) return value.equals("start")?StopMoveType.MOVE:StopMoveType.STOP;
		} catch (KNXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private String mapToPercent(String value) {
		int percent = Integer.parseInt(value.toString());
		return Integer.toString(percent * 100 / 255);
	}

	private String mapTo8bit(PercentType type) {
		int value = Integer.parseInt(type.toString());
		return Integer.toString(value * 255 / 100);
	}

}
