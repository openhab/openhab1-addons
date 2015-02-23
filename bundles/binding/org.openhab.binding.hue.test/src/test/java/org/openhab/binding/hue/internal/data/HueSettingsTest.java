/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.hue.internal.data;

import static org.junit.Assert.assertEquals;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.openhab.binding.hue.internal.HueTapStatesHandler;
import org.openhab.binding.hue.internal.hardware.HueTapState;


/**
 * @author Thomas.Eichstaedt-Engelen
 * @since 0.6.0
 */
public class HueSettingsTest {
		
	@Before
	public void init() {

	}
	
//	@Test(expected=BindingConfigParseException.class)
//	public void testParseBindingConfig_wrongDirection() throws BindingConfigParseException {
//		provider.parseBindingConfig(testItem, "?");
//	}


	@Test
	public void testSettings1() throws Exception{
		String settings1=readFileToString("/basic1.json");
		
		HueSettings s1=new HueSettings(settings1);
		
		assertEquals(3,s1.getLightsCount());
	}
	
	
	@Test
	public void testTapStates() throws Exception{
		String settingsString=readFileToString("/basic1.json");
		
		HueSettings s1=new HueSettings(settingsString);
		Map<Integer,HueTapState> states=s1.getTapStates();
		assertEquals(1,states.size());
		
		
		// a new handler
		HueTapStatesHandler handler=new HueTapStatesHandler();		
		Map<Integer,HueTapState> pressed=handler.findPressedTapDevices(s1);
		assertEquals(0,pressed.size());
		
		// again, no actions inbetween
		pressed=handler.findPressedTapDevices(s1);
		assertEquals(0,pressed.size());
		
		String settingsString2=settingsString.replace("2015-01-04T09:46:57", "2015-01-04T09:46:58");
		HueSettings s2=new HueSettings(settingsString2);
		pressed=handler.findPressedTapDevices(s2);
		assertEquals(1,pressed.size());
			
		assertEquals(3,s1.getLightsCount());
	}
	
	
	/**
	 * Read Resource file into a text file
	 * @param resourcePath
	 * @return
	 * @throws Exception
	 */
	public String readFileToString(String resourcePath) throws Exception {
        java.net.URL url = this.getClass().getResource(resourcePath);
        java.nio.file.Path resPath = java.nio.file.Paths.get(url.toURI());
        String text = new String(java.nio.file.Files.readAllBytes(resPath), "UTF8"); 
        return text;
  }
}
