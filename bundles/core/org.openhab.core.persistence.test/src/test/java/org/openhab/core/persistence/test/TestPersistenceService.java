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
package org.openhab.core.persistence.test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

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
public class TestPersistenceService implements QueryablePersistenceService {

	@Override
	public String getName() {
		return "test";
	}

	@Override
	public void store(Item item) {
	}

	@Override
	public void store(Item item, String alias) {
	}

	@SuppressWarnings("deprecation")
	@Override
	public Iterable<HistoricItem> query(FilterCriteria filter) {		
		int startValue = 1950;
		int endValue = 2012;
		
		if(filter.getBeginDate()!=null) startValue = filter.getBeginDate().getYear() + 1900;
		if(filter.getEndDate()!=null) endValue = filter.getEndDate().getYear() + 1900;
		
		if(endValue<=startValue || startValue<1950) return Collections.emptyList();
		
		ArrayList<HistoricItem> results = new ArrayList<HistoricItem>(endValue - startValue);
		for(int i = startValue; i <= endValue; i++) {
			final int year = i;
			results.add(new HistoricItem() {
				public Date getTimestamp() {
					return new Date(year-1900, 0, 1);
				}
				
				@Override
				public State getState() {
					return new DecimalType(year);
				}
				
				@Override
				public String getName() {
					return "Test";
				}
			});
		}
		if(filter.getOrdering()==Ordering.DESCENDING) {
			Collections.reverse(results);
		}
		return results;
	}

}
