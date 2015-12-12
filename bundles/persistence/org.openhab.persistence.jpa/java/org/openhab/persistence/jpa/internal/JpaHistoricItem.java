/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.persistence.jpa.internal;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.openhab.core.items.Item;
import org.openhab.core.library.items.ColorItem;
import org.openhab.core.library.items.ContactItem;
import org.openhab.core.library.items.DateTimeItem;
import org.openhab.core.library.items.DimmerItem;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.items.RollershutterItem;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.core.library.items.LocationItem;
import org.openhab.library.tel.items.CallItem;
import org.openhab.core.library.types.DateTimeType;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.HSBType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.OpenClosedType;
import org.openhab.core.library.types.PercentType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.library.types.PointType;
import org.openhab.library.tel.types.CallType;
import org.openhab.core.persistence.HistoricItem;
import org.openhab.core.types.State;
import org.openhab.persistence.jpa.internal.model.JpaPersistentItem;

/**
 * The historic item as returned when querying the service.
 * @author mbergmann
 * @since 1.6.0
 *
 */
public class JpaHistoricItem implements HistoricItem {

	final private String name;
	final private State state;
	final private Date timestamp;
		
	public JpaHistoricItem(String name, State state, Date timestamp) {
		this.name = name;
		this.state = state;
		this.timestamp = timestamp;
	}
	
	public String getName() {
		return name;
	}
	
	public Date getTimestamp() {
		return timestamp;
	}
	
	public State getState() {
		return state;
	}
	
	@Override
	public String toString() {
		return DateFormat.getDateTimeInstance().format(timestamp) + ": " + name + " -> "+ state.toString();
	}

	/**
	 * This method maps a jpa result item to this historic item.
	 * @param jpaQueryResult the result which jpa items
	 * @param item used for query information, like the state (State)
	 * @return list of historic items
	 */
	public static List<HistoricItem> fromResultList(List<JpaPersistentItem> jpaQueryResult, Item item) {
		List<HistoricItem> ret = new ArrayList<HistoricItem>();
		for(JpaPersistentItem i : jpaQueryResult) {
			HistoricItem hi = fromPersistedItem(i, item);
			ret.add(hi);
		}
		return ret;
	}
    
    /**
        Converts the string value of the persisted item to the state of a HistoricItem.
        @param pItem the persisted JpaPersistentItem
        @param item the source reference Item
        @return historic item
    */
    public static HistoricItem fromPersistedItem(JpaPersistentItem pItem, Item item) {
		State state;
		if (item instanceof NumberItem) {
			state = new DecimalType(Double.valueOf(pItem.getValue()));
		} else if (item instanceof DimmerItem) {
			state = new PercentType(Integer.valueOf(pItem.getValue()));
		} else if (item instanceof SwitchItem) {
			state = OnOffType.valueOf(pItem.getValue());
		} else if (item instanceof ContactItem) {
			state = OpenClosedType.valueOf(pItem.getValue());
		} else if (item instanceof RollershutterItem) {
			state = PercentType.valueOf(pItem.getValue());
		} else if (item instanceof ColorItem) {
			state = new HSBType(pItem.getValue());
		} else if (item instanceof DateTimeItem) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(new Date(Long.valueOf(pItem.getValue())));
			state = new DateTimeType(cal);
        } else if (item instanceof LocationItem) {
            PointType pType = null;
            String[] comps = pItem.getValue().split(";");
            if(comps.length >= 2) {
                pType = new PointType(new DecimalType(comps[0]), new DecimalType(comps[1]));

                if(comps.length == 3) {
                    pType.setAltitude(new DecimalType(comps[2]));
                }
            }
            state = pType;
            
        } else if (item instanceof CallItem) {
            state = new CallType(pItem.getValue());
		} else {
			state = new StringType(pItem.getValue());
		}
        
        return new JpaHistoricItem(item.getName(), state, pItem.getTimestamp());
    }
}
