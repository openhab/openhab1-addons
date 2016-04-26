/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.core.persistence.test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;

import org.joda.time.LocalDate;
import org.openhab.core.items.Item;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.persistence.FilterCriteria;
import org.openhab.core.persistence.FilterCriteria.Ordering;
import org.openhab.core.persistence.HistoricItem;
import org.openhab.core.persistence.QueryablePersistenceService;
import org.openhab.core.types.State;

/**
 * A simple persistence service used for unit tests
 * 
 * @author Kai Kreuzer
 * @since 1.1.0
 */
public class DimmerTestPersistenceService implements QueryablePersistenceService {
	private List<HistoricItem> values = new ArrayList<HistoricItem>();
	
	class DummyHistoricItem implements HistoricItem {
		private Date date;
		private State state;
		private String name;
		
		public DummyHistoricItem(Date date, State state, String name) {
			super();
			this.date = date;
			this.state = state;
			this.name = name;
		}

		@Override
		public Date getTimestamp() {
			return this.date;
		}

		@Override
		public State getState() {
			return this.state;
		}

		@Override
		public String getName() {
			return this.name;
		}

		@Override
		public String toString() {
			return "DummyHistoricItem [name=" + name + ", date=" + date
					+ ", state=" + state + "]";
		}
		
		
	}
	
	public DimmerTestPersistenceService() {
		String name = "test";
		this.values.add(new DummyHistoricItem(new LocalDate(2012, 1, 5).toDate(), new DecimalType(2), name));
		this.values.add(new DummyHistoricItem(new LocalDate(2012, 1, 6).toDate(), new DecimalType(0), name));
		this.values.add(new DummyHistoricItem(new LocalDate(2012, 1, 7).toDate(), new DecimalType(2), name));
		this.values.add(new DummyHistoricItem(new LocalDate(2012, 1, 8).toDate(), new DecimalType(50), name));
		this.values.add(new DummyHistoricItem(new LocalDate(2012, 1, 9).toDate(), new DecimalType(70), name));
		this.values.add(new DummyHistoricItem(new LocalDate(2012, 1, 10).toDate(), new DecimalType(0), name));
		this.values.add(new DummyHistoricItem(new LocalDate(2012, 1, 11).toDate(), new DecimalType(2), name));
		this.values.add(new DummyHistoricItem(new LocalDate(2012, 1, 12).toDate(), new DecimalType(0), name));
		this.values.add(new DummyHistoricItem(new LocalDate(2012, 1, 13).toDate(), new DecimalType(100), name));
		this.values.add(new DummyHistoricItem(new LocalDate(2012, 1, 14).toDate(), new DecimalType(0), name));
		this.values.add(new DummyHistoricItem(new LocalDate(2012, 1, 15).toDate(), new DecimalType(0), name));
		this.values.add(new DummyHistoricItem(new LocalDate(2012, 1, 16).toDate(), new DecimalType(0), name));
		this.values.add(new DummyHistoricItem(new LocalDate(2012, 1, 17).toDate(), new DecimalType(0), name));
		
	}
	
	@Override
	public String getName() {
		return "dimmer";
	}

	@Override
	public void store(Item item) {
	}

	@Override
	public void store(Item item, String alias) {
	}

	@Override
	public Iterable<HistoricItem> query(final FilterCriteria filter) {		
		List<HistoricItem> list = new ArrayList<HistoricItem>();
		for (HistoricItem item : this.values) {
			if (filter.getBeginDate() != null
					&& item.getTimestamp().before(filter.getBeginDate())) {
				continue;
			}
			
			if (filter.getEndDate() != null
					&& item.getTimestamp().after(filter.getEndDate())) {
				continue;
			}
			
			list.add(item);
		}
		
		Collections.sort(list, new Comparator<HistoricItem>() {
			@Override
			public int compare(HistoricItem arg0, HistoricItem arg1) {
				if (filter.getOrdering() == Ordering.ASCENDING) {
					return (int) (arg0.getTimestamp().getTime() - arg1.getTimestamp().getTime());
				} else {
					return (int) (arg1.getTimestamp().getTime() - arg0.getTimestamp().getTime());
				}
			}
		});
		
		return list;
	}
}
