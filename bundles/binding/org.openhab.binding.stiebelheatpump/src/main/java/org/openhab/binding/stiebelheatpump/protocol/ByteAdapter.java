/**

 * Copyright 2014 
 * This file is part of stiebel heat pump reader.
 * It is free software: you can redistribute it and/or modify it under the terms of the 
 * GNU General Public License as published by the Free Software Foundation, 
 * either version 3 of the License, or (at your option) any later version.
 * It is  is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
 * See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with the project. 
 * If not, see http://www.gnu.org/licenses/.
 */
package org.openhab.binding.stiebelheatpump.protocol;

import javax.xml.bind.DatatypeConverter;
import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * ByteAdapter class used for conversion of bytes during xml parsing
 * 
 * @author Peter Kreutzer
 */
public class ByteAdapter extends XmlAdapter<String, Byte> {

	@Override
	public Byte unmarshal(String v) throws Exception {
		return DatatypeConverter.parseHexBinary(v)[0];
	}

	@Override
	public String marshal(Byte v) throws Exception {
		return DatatypeConverter.printHexBinary(new byte[] { v });
	}

}
