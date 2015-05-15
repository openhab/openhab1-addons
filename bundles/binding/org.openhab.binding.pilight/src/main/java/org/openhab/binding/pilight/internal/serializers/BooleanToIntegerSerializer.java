/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.pilight.internal.serializers;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;

/**
 * Serializer to map boolean values to an integer (1 and 0). 
 * 
 * @author Jeroen Idserda
 * @since 1.7
 */
public class BooleanToIntegerSerializer extends JsonSerializer<Boolean> {

	@Override
	public void serialize(Boolean bool, JsonGenerator jsonGenerator,
			SerializerProvider serializerProvider) throws IOException,
			JsonProcessingException {
		if (bool != null)
			jsonGenerator.writeObject(bool ? 1 : 0);
	}
}
