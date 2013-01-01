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
package org.openhab.io.gcal.internal.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.math.LongRange;
import org.quartz.Calendar;


/**
 * This implementation of the Quartz Calendar stores a list of TimeRanges that
 * are excluded from scheduling.
 * 
 * @author Thomas.Eichstaedt-Engelen
 */
public class TimeRangeCalendar implements Calendar, Serializable {
	
	private static final long serialVersionUID = 6840341227522646373L;
	
	private Calendar baseCalendar;
	private String description;
	
    private static final long ONE_MILLI = 1;
	
    private List<LongRange> excludedRanges = new ArrayList<LongRange>();
    
    
    public TimeRangeCalendar() {
    	this.baseCalendar = null;
    	this.description = "A special Quartz calendar which caries the excluded time ranges.";
    }
    
    
	public Calendar getBaseCalendar() {
		return baseCalendar;
	}
	
	public void setBaseCalendar(Calendar baseCalendar) {
		this.baseCalendar = baseCalendar;
	}

	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}
	
    /**
     * Determine whether the given time (in milliseconds) is 'included' by the
     * Calendar.
     */
    public boolean isTimeIncluded(long timeStamp) {
		return findTimeRange(timeStamp) != null;
    }

    /**
     * Determine the next time (in milliseconds) that is 'included' by the
     * Calendar after the given time.
     */
    public long getNextIncludedTime(long timeStamp) {
       long nextIncludedTime = timeStamp + ONE_MILLI;
        
        while (isTimeIncluded(nextIncludedTime)) {
        	LongRange timeRange = findTimeRange(timeStamp);
            if (nextIncludedTime >= timeRange.getMinimumLong() && 
                nextIncludedTime <= timeRange.getMaximumLong()) {
                
                nextIncludedTime = timeRange.getMaximumLong() + ONE_MILLI;
            } else if ((getBaseCalendar() != null) && (!getBaseCalendar().isTimeIncluded(nextIncludedTime))){
                nextIncludedTime = getBaseCalendar().getNextIncludedTime(nextIncludedTime);
            } else {
                nextIncludedTime++;
            }
        }
        
        return nextIncludedTime;
    }
    
    /**
     * Find first TimeRange which contains the given <code>timeStamp</code>
     * @param timeStamp the TimeStamp to find
     * @return the first TimeRange which contains the given <code>timeStamp</code>
     * or <code>null</code> if no matching TimeRange exists
     */
    protected LongRange findTimeRange(long timeStamp) {
    	for (LongRange range : excludedRanges) {
    		if (range.containsLong(timeStamp)) {
    			return range;
    		}
    	}
		return null;
    }

    /**
     * Adds the given <code>timeRange</code> to the excluded ranges.
     */
    public void addTimeRange(LongRange timeRange) {
		this.excludedRanges.add(timeRange);
    }

    /**
     * Removes the given <code>timeRange</code> from the excluded ranges.
     */
    public void removeExcludedDate(LongRange dateToRemove) {
        this.excludedRanges.remove(dateToRemove);
    }
    
    /**
     * Returns an unmodifiable List of the excluded TimeRanges
     */
    protected List<LongRange> getExcludedRanges() {
		return Collections.unmodifiableList(excludedRanges);
	}
    
    
    @Override
    public Object clone() {
    	TimeRangeCalendar clone = new TimeRangeCalendar();
        clone.excludedRanges = new ArrayList<LongRange>(excludedRanges);
        return clone;
    }
    
	
}
