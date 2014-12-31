package org.openhab.binding.dsmr.internal.messages;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.openhab.core.library.types.DateTimeType;

/**
 * Class representing the OBIS Date Value
 * <p>
 * The OBIS Date Value has the following format:
 * <p>
 * <ul>
 * <li>DSMR v3.0: yyMMddHHmmss
 * <li>DSMR v4.0 and later: yyMMddHHmmssX
 * </ul>
 * Where X is 'S' (DST is active) or 'W' (DST is not active).
 * <p>
 * When creating a new instance the constructor will convert the String value to a DateTimeType.
 * 
 * @author M. Volaart
 * @since 1.7.0
 */
public class OBISDateMsg extends OBISMessage<DateTimeType> {
	/**
	 * Constructor for creating a new OBISDateMsg.
	 * 
	 * @param type the {@link OBISMsgType} type of the OBIS value
	 * @param value String representing the value
	 * 
	 * @throws ParseException if parsing of the String value failed
	 */
	public OBISDateMsg(OBISMsgType type, String value) throws ParseException {
		super(type);

		SimpleDateFormat formatter = new SimpleDateFormat("yyMMddHHmmss");
		// TODO: DSMR v4.0 specifies DST character as the last character. We
		// just skip this setting
		Date date = formatter.parse(value.substring(0, value.length() - 1));

		Calendar c = Calendar.getInstance();
		c.setTime(date);

		super.openHabValue = new DateTimeType(c);
	}
}
