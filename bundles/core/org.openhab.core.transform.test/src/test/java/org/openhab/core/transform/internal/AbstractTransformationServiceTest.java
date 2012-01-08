/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010-2012, openHAB.org <admin@openhab.org>
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
package org.openhab.core.transform.internal;


/**
 * @author Thomas.Eichstaedt-Engelen
 * @since 0.7.0
 */
public abstract class AbstractTransformationServiceTest {
	
	protected String source = "<?xml version=\"1.0\"?><xml_api_reply version=\"1\"><weather module_id=\"0\"" +
		" tab_id=\"0\" mobile_row=\"0\" mobile_zipped=\"1\" row=\"0\" section=\"0\" ><forecast_information>" +
		"<city data=\"Krefeld, North Rhine-Westphalia\"/><postal_code data=\"Krefeld Germany\"/>" +
		"<latitude_e6 data=\"\"/><longitude_e6 data=\"\"/><forecast_date data=\"2011-03-01\"/>" +
		"<current_date_time data=\"2011-03-01 15:20:00 +0000\"/><unit_system data=\"SI\"/></forecast_information>" +
		"<current_conditions><condition data=\"Meistens bew�lkt\"/><temp_f data=\"46\"/><temp_c data=\"8\"/>" +
		"<humidity data=\"Feuchtigkeit: 66 %\"/><icon data=\"/ig/images/weather/mostly_cloudy.gif\"/>" +
		"<wind_condition data=\"Wind: N mit 26 km/h\"/></current_conditions><forecast_conditions><day_of_week data=\"Di.\"/>" +
		"<low data=\"-1\"/><high data=\"6\"/><icon data=\"/ig/images/weather/sunny.gif\"/><condition data=\"Klar\"/>" +
		"</forecast_conditions><forecast_conditions><day_of_week data=\"Mi.\"/><low data=\"-1\"/><high data=\"8\"/>" +
		"<icon data=\"/ig/images/weather/sunny.gif\"/><condition data=\"Klar\"/></forecast_conditions><forecast_conditions>" +
		"<day_of_week data=\"Do.\"/><low data=\"-1\"/><high data=\"8\"/><icon data=\"/ig/images/weather/sunny.gif\"/>" +
		"<condition data=\"Klar\"/></forecast_conditions><forecast_conditions><day_of_week data=\"Fr.\"/><low data=\"0\"/>" +
		"<high data=\"8\"/><icon data=\"/ig/images/weather/sunny.gif\"/><condition data=\"Klar\"/></forecast_conditions>" +
		"</weather></xml_api_reply>";

}
