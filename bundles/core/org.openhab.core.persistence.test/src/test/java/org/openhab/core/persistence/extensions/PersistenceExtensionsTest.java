/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010-2013, openHAB.org <admin@openhab.org>
 *
 * See the contributors.txt file in the distribution for a
 * full listing of individual contributors.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see <http://www.gnu.org/licenses>.
 *
 * Additional permission under GNU GPL version 3 section 7
 *
 * If you modify this Program, or any covered work, by linking or
 * combining it with Eclipse (or a modified version of that library),
 * containing parts covered by the terms of the Eclipse Public License
 * (EPL), the licensors of this Program grant you additional permission
 * to convey the resulting work.
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
import org.openhab.core.types.UnDefType;

/**
 * @author Kai Kreuzer
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
		State state = PersistenceExtensions.historicState(item, new DateMidnight(2012, 1, 1), "test");
		assertEquals("2012", state.toString());

		state = PersistenceExtensions.historicState(item, new DateMidnight(2011, 12, 31), "test");
		assertEquals("2011", state.toString());

		state = PersistenceExtensions.historicState(item, new DateMidnight(2011, 1, 1), "test");
		assertEquals("2011", state.toString());

		state = PersistenceExtensions.historicState(item, new DateMidnight(2000, 1, 1), "test");
		assertEquals("2000", state.toString());
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
