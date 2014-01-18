/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
