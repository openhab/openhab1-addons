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
		assertEquals(UnDefType.NULL, state);

		state = PersistenceExtensions.historicState(item, new DateMidnight(2011, 1, 1), "test");
		assertEquals("2011", state.toString());

		state = PersistenceExtensions.historicState(item, new DateMidnight(2000, 1, 1), "test");
		assertEquals("2000", state.toString());
	}

	@Test
	public void testMinimumSince() {
		item.setState(new DecimalType(5000));
		HistoricItem historicItem = PersistenceExtensions.minimumSince(item, new DateMidnight(2012, 1, 1), "test");
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
		assertEquals("2011", historicItem.getState().toString());
		assertEquals(new DateMidnight(2011, 1, 1).toDate(), historicItem.getTimestamp());
	}

	@Test
	public void testAverageSince() {
		item.setState(new DecimalType(3312));
		DecimalType average = PersistenceExtensions.averageSince(item, new DateMidnight(2000, 1, 1), "test");
		assertEquals("2106", average.toString());
	}
}
