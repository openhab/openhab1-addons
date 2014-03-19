/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.persistence.sense.internal;

import flexjson.JSONContext;
import flexjson.transformer.AbstractTransformer;


/**
 * This class defines what kind of content should be written for a {@link SenseEventBean}.
 * 
 * @author Kai Kreuzer
 * @since 1.0.0
 */
public class SenseEventTransformer extends AbstractTransformer {

	public void transform(Object obj) {
		if (obj instanceof SenseEventBean) {
			SenseEventBean senseEventBean = (SenseEventBean) obj;
			JSONContext context = getContext();
			context.writeOpenObject();
			context.writeName("feed_id");
			context.write("" + senseEventBean.getFeedId());
			context.writeComma();
			context.writeName("value");
			context.write("\"" + senseEventBean.getValue() + "\"");
			context.writeCloseObject();
		}
	}

}
