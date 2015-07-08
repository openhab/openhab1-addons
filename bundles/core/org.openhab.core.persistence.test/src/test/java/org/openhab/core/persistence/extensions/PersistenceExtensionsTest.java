/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.core.persistence.extensions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.List;

import org.joda.time.DateMidnight;
import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openhab.core.items.GenericItem;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.persistence.HistoricItem;
import org.openhab.core.persistence.HistoricItemRange;
import org.openhab.core.persistence.PersistenceService;
import org.openhab.core.persistence.test.DimmerTestPersistenceService;
import org.openhab.core.persistence.test.TestPersistenceService;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;

/**
 * @author Kai Kreuzer
 * @author Chris Jackson
 * @since 1.1.0
 */
public class PersistenceExtensionsTest {

	private TestPersistenceService testPersistenceService;
	private PersistenceExtensions ext;
	private GenericItem item;
	
	@Before
	public void setUp() {
		testPersistenceService = new TestPersistenceService();
		ext = new PersistenceExtensions();
		ext.addPersistenceService(testPersistenceService);
		item = new GenericItem("Test") {
			@Override
			public List<Class<? extends State>> getAcceptedDataTypes() {
				return null;
			}
			
			@Override
			public List<Class<? extends Command>> getAcceptedCommandTypes() {
				return null;
			}
		};
		
		ext.addPersistenceService(new DimmerTestPersistenceService());
	}
	
	@After
	public void tearDown() {
		ext.removePersistenceService(testPersistenceService);	
	}
	
	@Test
	public void testHistoricState() {
		testPersistenceService.setUseStaticEndValue(false);
		
		HistoricItem historicItem = PersistenceExtensions.historicState(item, new DateMidnight(2012, 1, 1), "test");
		assertEquals("2012", historicItem.getState().toString());

		historicItem = PersistenceExtensions.historicState(item, new DateMidnight(2011, 12, 31), "test");
		assertEquals("2011", historicItem.getState().toString());

		historicItem = PersistenceExtensions.historicState(item, new DateMidnight(2011, 1, 1), "test");
		assertEquals("2011", historicItem.getState().toString());

		historicItem = PersistenceExtensions.historicState(item, new DateMidnight(2000, 1, 1), "test");
		assertEquals("2000", historicItem.getState().toString());
	}

	@Test
	public void testMinimumSince() {
		testPersistenceService.setUseStaticEndValue(true);
		
		item.setState(new DecimalType(5000));
		HistoricItem historicItem = PersistenceExtensions.minimumSince(item, new DateMidnight(1940, 1, 1), "test");
		assertNotNull(historicItem);
		assertEquals("5000", historicItem.getState().toString());
		
		historicItem = PersistenceExtensions.minimumSince(item, new DateMidnight(2005, 1, 1), "test");
		assertEquals("2005", historicItem.getState().toString());
		assertEquals(new DateMidnight(2005, 1, 1).toDate(), historicItem.getTimestamp());
	}
	
	@Test
	public void testMaximumBetween() {
		testPersistenceService.setUseStaticEndValue(true);
		
		item.setState(new DecimalType(1));
		HistoricItem historicItem = PersistenceExtensions.maximumBetween(item, new DateMidnight(2012, 1, 1), new DateMidnight(2012, 1, 1), "test");
		assertNotNull(historicItem);
		assertEquals("1", historicItem.getState().toString());
		
		historicItem = PersistenceExtensions.maximumBetween(item, new DateMidnight(2005, 1, 1), new DateMidnight(2012, 1, 1), "test");
		assertEquals("2012", historicItem.getState().toString());
		assertEquals(new DateMidnight(2012, 1, 1).toDate(), historicItem.getTimestamp());
		
		testPersistenceService.setUseStaticEndValue(false);
		historicItem = PersistenceExtensions.maximumBetween(item, new DateMidnight(2005, 1, 1), new DateMidnight(2010, 1, 1), "test");
		assertEquals("2010", historicItem.getState().toString());
		assertEquals(new DateMidnight(2010, 1, 1).toDate(), historicItem.getTimestamp());
	}

	@Test
	public void testMaximumSince() {
		testPersistenceService.setUseStaticEndValue(true);
		
		item.setState(new DecimalType(1));
		HistoricItem historicItem = PersistenceExtensions.maximumSince(item, new DateMidnight(2012, 1, 1), "test");
		assertNotNull(historicItem);
		assertEquals("1", historicItem.getState().toString());
		
		historicItem = PersistenceExtensions.maximumSince(item, new DateMidnight(2005, 1, 1), "test");
		assertEquals("2012", historicItem.getState().toString());
		assertEquals(new DateMidnight(2012, 1, 1).toDate(), historicItem.getTimestamp());
	}

	@Test
	public void testAverageSince() {
		testPersistenceService.setUseStaticEndValue(true);
		
		item.setState(new DecimalType(3025));
		DecimalType average = PersistenceExtensions.averageSince(item, new DateMidnight(2003, 1, 1), "test");
		assertEquals("2100", average.toString());
	}
	
	@Test
	public void testAccurateAverageBetween() {
		testPersistenceService.setUseStaticEndValue(false);
		
		item.setState(new DecimalType(3025));
		DecimalType average = PersistenceExtensions.accurateAverageBetween(item, new DateMidnight(2003, 1, 1), new DateMidnight(2100, 1, 1), "test");
		assertTrue("was: " + average, average.toString().startsWith("2050"));
	}
	
	@Test
	public void testAverageBetween() {
		testPersistenceService.setUseStaticEndValue(false);
		
		item.setState(new DecimalType(3025));
		DecimalType average = PersistenceExtensions.averageBetween(item, new DateMidnight(2001, 1, 1), new DateMidnight(2003, 1, 1), "test");
		assertTrue("was: " + average, average.toString().startsWith("2002"));
	}
	
	@Test
	public void testHistoricRuntime() {
		item.setState(OnOffType.ON);
		HistoricItemRange range = PersistenceExtensions.historicRuntime(item, new DateMidnight(2012, 1, 8), "dimmer");
		assertNotNull(range);
		assertNotNull(range.getBegin());
		assertNotNull(range.getEnd());
		assertEquals(new DecimalType(2), range.getBegin().getState());
		assertEquals(new DecimalType(0), range.getEnd().getState());
		assertEquals(new DateMidnight(2012, 1, 7).toDate(), range.getBegin().getTimestamp());
		assertEquals(new DateMidnight(2012, 1, 10).toDate(), range.getEnd().getTimestamp());
	}
	
	@Test
	public void testHistoricRuntime2() {
		item.setState(OnOffType.OFF);
		HistoricItemRange range = PersistenceExtensions.historicRuntime(item, new DateMidnight(2012, 1, 18), "dimmer");
		assertNotNull(range);
		assertNotNull(range.getBegin());
		assertNotNull(range.getEnd());
		assertEquals(new DecimalType(100), range.getBegin().getState());
		assertEquals(new DecimalType(0), range.getEnd().getState());
	}
	
	@Test
	public void testHistoricRuntime3() {
		item.setState(OnOffType.ON);
		HistoricItemRange range = PersistenceExtensions.historicRuntime(item, new DateMidnight(2012, 1, 1), "dimmer");
		System.out.println(range);
		assertNotNull(range);
		assertNull(range.getBegin());
		assertNull(range.getEnd());
	}
}
