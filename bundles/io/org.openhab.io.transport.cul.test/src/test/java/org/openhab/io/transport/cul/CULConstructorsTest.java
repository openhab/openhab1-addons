/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.openhab.io.transport.cul;

import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;
import org.openhab.io.transport.cul.internal.CULNetworkHandlerImpl;
import org.openhab.io.transport.cul.internal.CULSerialHandlerImpl;

/**
 * provides Tests for CULHandler Constructors
 * 
 * @author Gernot Eger
 * @since 1.7.0
 */
public class CULConstructorsTest {
	
	
	@Test
	public void testCreateSerialHandler() throws Exception{
		CULHandler handler=new CULSerialHandlerImpl("myDevice",CULMode.SLOW_RF);

		Assert.assertNotNull(handler);
		
		Map<String,String> properties=new HashMap<String,String>();
		
		properties.put("baudrate", "9600");
		properties.put("parity","EVEN");  
		handler=new CULSerialHandlerImpl("myDevice",CULMode.SLOW_RF,properties);
		Assert.assertTrue(handler.arePropertiesEqual(properties));		
			
	}
	
	@Test
	public void testCreateNetworkHandler() throws Exception{
		CULHandler handler=new CULNetworkHandlerImpl("myDevice",CULMode.SLOW_RF);

		Assert.assertNotNull(handler);
		Map<String,String> properties=new HashMap<String,String>();

		//meaningless properties: will be ignored
		properties.put("baudrate", "9600");
		properties.put("parity","EVEN"); 
		handler=new CULNetworkHandlerImpl("myDevice",CULMode.SLOW_RF,properties);
		Assert.assertTrue(handler.arePropertiesEqual(properties));

	}
	
}
