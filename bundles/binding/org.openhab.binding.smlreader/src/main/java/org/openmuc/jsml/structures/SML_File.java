/*
 * Copyright 2009-14 Fraunhofer ISE
 *
 * This file is part of jSML.
 * For more information visit http://www.openmuc.org
 *
 * jSML is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 2.1 of the License, or
 * (at your option) any later version.
 *
 * jSML is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with jSML.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package org.openmuc.jsml.structures;

import java.util.ArrayList;
import java.util.List;

public class SML_File {

	private final List<SML_Message> messages;

	/**
	 * Creates an empty SML_File.
	 */
	public SML_File() {
		messages = new ArrayList<SML_Message>();
	}

	/**
	 * Creates a SML_File with the given list of SML_Messages.
	 * 
	 * @param smlFile
	 */
	public SML_File(List<SML_Message> smlFile) {
		messages = smlFile;
	}

	/**
	 * Creates a new SML_File and adds the given SML_Message. Useful for attentionResponse.
	 * 
	 * @param smlFile
	 */
	public SML_File(SML_Message smlFile) {
		this();
		add(smlFile);
	}

	/**
	 * add SML_Message to SML_File
	 * 
	 * @param message
	 */
	public void add(SML_Message message) {
		messages.add(message);
	}

	/**
	 * 
	 * @return all saved SML_Messages in this SML_File
	 */
	public List<SML_Message> getMessages() {
		return messages;
	}
}
