/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.persistence.cosm.internal;

import flexjson.JSONContext;
import flexjson.transformer.AbstractTransformer;


/**
 * This class defines what kind of content should be written for a {@link CosmEventBean}.
 * 
 * @author Dmitry Krasnov
 * @since 1.1.0
 */
public class CosmEventTransformer extends AbstractTransformer {

	public void transform(Object obj) {
		if (obj instanceof CosmEventBean) {
			CosmEventBean pachubeEventBean = (CosmEventBean) obj;
			JSONContext context = getContext();
			context.writeOpenObject();

			context.writeName("id"); 
			context.write("\""+pachubeEventBean.getId()+"\""); 
			context.writeComma(); 
			context.writeName("current_value"); 
			context.write("\""+pachubeEventBean.getValue()+"\""); 
			if(null!=pachubeEventBean.getMaxValue() && !pachubeEventBean.getMaxValue().isEmpty())	{ 
				context.writeComma(); 
				context.writeName("max_value"); 
				context.write("\""+pachubeEventBean.getMaxValue()+"\""); 
			} 
			if(null!=pachubeEventBean.getMinValue() && !pachubeEventBean.getMinValue().isEmpty()) 	{ 
				context.writeComma(); 
				context.writeName("min_value"); 
				context.write("\""+pachubeEventBean.getMinValue()+"\""); 
			} 
			context.writeCloseObject(); 
		}
	} 
	
}
