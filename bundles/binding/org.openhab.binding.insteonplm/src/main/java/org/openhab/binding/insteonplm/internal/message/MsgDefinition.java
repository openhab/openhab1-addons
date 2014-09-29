/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.insteonplm.internal.message;

import java.util.HashMap;

/**
 * Definition (layout) of an Insteon message. Says which bytes go where.
 * For more info, see the public Insteon Developer's Guide, 2nd edition,
 * and the Insteon Modem Developer's Guide.
 * 
 * @author Daniel Pfrommer
 * @since 1.5.0
 */

public class MsgDefinition {
	private HashMap<String, Field>	m_fields = new HashMap<String, Field>();
	
	MsgDefinition() {
	}
	
	/* Copy constructor, needed to make a copy of a message
	 * @param m the definition to copy
	 */
	MsgDefinition(MsgDefinition m) {
		m_fields    = new HashMap<String, Field>(m.m_fields);
	}
	
	public HashMap<String, Field> getFields() { return m_fields; }

	public boolean containsField(String name) {
		return m_fields.containsKey(name);
	}

	public void addField(Field field) {
		m_fields.put(field.getName(), field);
	}

	/**
	 * Finds field of a given name
	 * @param name name of the field to search for
	 * @return reference to field
	 * @throws FieldException if no such field can be found
	 */
	public Field getField(String name) throws FieldException {
		Field f = m_fields.get(name);
		if (f == null) throw new FieldException("field " + name + " not found");
		return f;
	}
}
