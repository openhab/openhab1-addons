/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.core.library.types;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

import org.junit.Test;
import org.openhab.core.library.items.LocationItem;

/**
 * @author Gaël L'hopital
 * @since 1.7.0
 */
public class LocationItemTest {
	
	@Test
	public void testDistance() {	
		PointType pointParis = new PointType("48.8566140,2.3522219");

		assertEquals(pointParis.getLatitude().doubleValue(),48.856614,0.0000001);
		assertEquals(pointParis.getLongitude().doubleValue(),2.3522219,0.0000001);
		
		PointType pointBerlin = new PointType("52.5200066,13.4049540");
		
		LocationItem locationParis = new LocationItem("paris");
		locationParis.setState(pointParis);
		LocationItem locationBerlin = new LocationItem("berlin");
		locationBerlin.setState(pointBerlin);
		
		DecimalType distance = locationParis.distanceFrom(locationParis);
		assertEquals(distance.intValue(),0);
		
		double parisBerlin = locationParis.distanceFrom(locationBerlin).doubleValue();
		assertEquals(parisBerlin,878400,50);	
		
		double gravParis = pointParis.getGravity().doubleValue();
		assertEquals(gravParis,9.809,0.001);
		
		// Check canonization of position
		PointType point3 = new PointType("-100,200");
		double lat3 = point3.getLatitude().doubleValue();
		double lon3 = point3.getLongitude().doubleValue();
		assertTrue(lat3 > -90);
		assertTrue(lat3 < 90);
		assertTrue(lon3 < 180);
		assertTrue(lon3 > -180);

	}

}
