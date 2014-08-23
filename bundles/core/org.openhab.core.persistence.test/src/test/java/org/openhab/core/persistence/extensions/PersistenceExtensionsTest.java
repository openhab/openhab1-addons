/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.core.persistence.extensions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.joda.time.DateMidnight;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openhab.core.items.GenericItem;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.persistence.HistoricItem;
import org.openhab.core.persistence.PersistenceService;
import org.openhab.core.persistence.test.TestPersistenceService;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;

/**
 * @author Kai Kreuzer
 * @author Chris Jackson
 * @since 1.1.0
 */
public class PersistenceExtensionsTest {

	private PersistenceService testPersistenceService = new TestPersistenceService();
	private PersistenceExtensions ext;
	private GenericItem item;
	
	@Before
	public void setUp() {
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
	}
	
	@After
	public void tearDown() {
		ext.removePersistenceService(testPersistenceService);	
	}
	
	@Test
	public void testHistoricState() {
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
		item.setState(new DecimalType(5000));
		HistoricItem historicItem = PersistenceExtensions.minimumSince(item, new DateMidnight(1940, 1, 1), "test");
		assertNotNull(historicItem);
		assertEquals("5000", historicItem.getState().toString());
		
		historicItem = PersistenceExtensions.minimumSince(item, new DateMidnight(2005, 1, 1), "test");
		assertEquals("2005", historicItem.getState().toString());
		assertEquals(new DateMidnight(2005, 1, 1).toDate(), historicItem.getTimestamp());
	}

	@Test
	public void testMaximumSince() {
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
		item.setState(new DecimalType(3025));
		DecimalType average = PersistenceExtensions.averageSince(item, new DateMidnight(2003, 1, 1), "test");
		assertEquals("2100", average.toString());
	}
}
