/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
 package org.openhab.binding.onewire.internal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

import org.openhab.core.library.types.DecimalType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class to filter sensor data. For now directly implements a specific filter,
 * but can serve as base class later on.
 * 
 * @author Bernd Pfrommer
 * @since 1.5.0
 */
public class Filter {
	/**
	 * Implements a Tukey style outlier filter
	 */
	private static final Logger logger = LoggerFactory.getLogger(Filter.class);
	/** number of data points to use for outlier estimation. */
	private final static int WINDOW_SIZE = 40;
	/** what n-tile to use for boundaries */
	private final static int NTILE = 6;
	/** drop criterion for filter */
	private final static double ALPHA = 2.5;
	/** list to keep track of age of data points */
	private LinkedList<DecimalType> dataList = new LinkedList<DecimalType>();
	/** ordered set of data points, needed to compute n-tiles */
	private SortedArrayList<DecimalType> sortedList = new SortedArrayList<DecimalType>();
	/**
	 * Restricts the data point value to be between
	 * 
	 *   lowerbound = qbottom - alpha * r  and
	 *   upperbound = qtop + alpha * r
	 *   
	 * where qtop = top n-tile, qbottom = bottom ntile, and the range r = qtop - qbottom.
	 * The original Tukey filter drops points if they are outside of 1.5 * range, i.e. alpha = 1.5,
	 * and takes quartiles. 
	 * 
	 * Another implementation wrinkle: for slow changing data such as e.g. temperature,
	 * the binding may pick up the same data point over and over again. This compresses the
	 * range artificially, and will lead to spurious filtering.
	 * 
	 * For that reason a point is added to the sample set only if it is not present there
	 *
	 * @param v the new raw data point as received
	 * @return the filtered data point vf, i.e. v is boxed into:  lowerbound <= vf <= upperbound  
	 */
	public DecimalType filter(DecimalType v) {
		DecimalType y = v;
		if (dataList.size() > WINDOW_SIZE) {
			removeOldestPoint(v);
			// calculate filter parameters
			double qbottom = sortedList.get(sortedList.getNtile(1, NTILE)).doubleValue();
			double qtop = sortedList.get(sortedList.getNtile(NTILE - 1, NTILE)).doubleValue();
			double r = qtop - qbottom; // range
			double lowerbound = qbottom - ALPHA * r;
			double upperbound = qtop + ALPHA * r;
			// box it 
			double yd = y.doubleValue();
			yd = Math.max(yd,  lowerbound);
			yd = Math.min(yd,  upperbound);
			if (yd != y.doubleValue()) {
				logger.debug("boxed outlier: range: {}-{}={} orig data: {} filt data: {}",
						qbottom, qtop, qtop - qbottom, v.doubleValue(), yd);
			}
			y = new DecimalType(yd);
		}
		addNewPoint(v);
		return y;	// filtered value
	}
	
	/**
	 * Removes the oldest data point only if the new point
	 * is not known yet
	 * @param v the value to use for test if unknown
	 */
	private void removeOldestPoint(DecimalType v) {
		if (isNewPoint(v)) {
			// removes the oldest element in the set
			DecimalType oldest = dataList.remove();
			sortedList.removeSorted(oldest);
		}
	}
	/**
	 * Adds data point only if it's really new
	 * @param v the point to add
	 */
	private void addNewPoint(DecimalType v) {
		if (isNewPoint(v)) {
			dataList.add(v);
			sortedList.addSorted(v);
		}
	}
	/**
	 * Checks if the data point is contained in the data set
	 * @param v the value to check for
	 * @return true if the value is already in the data set
	 */
	private boolean isNewPoint(DecimalType v) {
		return (!sortedList.hasElement(v));
	}
	
	/**
	 * Maintains a O(log(N)) sorted ArrayList (so long as you don't directly access the add()
	 * method, but just use addSorted() and removeSorted().
	 * 
	 * @author Bernd Pfrommer
	 *
	 * @param <T> type to store in sorted ArrayList
	 */
	@SuppressWarnings("serial")
	public class SortedArrayList<T extends Comparable<T>> extends ArrayList<T> {
		/**
		 * Add element to list while preserving the order. Runtime O(log(N))
		 * @param a the element to add
		 */
		public void addSorted(T a) {
			int idx = Collections.binarySearch(this, a);
			add(idx >= 0 ? idx : Math.abs(idx + 1), a);
		}
		/**
		 * Remove element from list (performs O(log(N)) search to find it
		 * @param a the element to remove
		 */
		public void removeSorted(T a) {
			int idx = Collections.binarySearch(this, a);
			if (idx >= 0) {
				remove(idx);
			}
		}
		/**
		 * O(log(N)) test for presence of element
		 * @param a the element value to search for
		 * @return true if element is in list, false if otherwise
		 */
		public boolean hasElement(T a) {
			return (Collections.binarySearch(this, a) >= 0);
		}
		/**
		 * Finds array index for specified n-tile
		 * @param q which n-tile to find
		 * @return array index for given n-tile
		 */
		public int getNtile(int q, int n) {
			if (q > 0 && q < n) { 
				return (size() * q) / n;
			}
			if (q >= n) { 
				return (size() - 1);
			}
			return 0;
		}
	}
}
