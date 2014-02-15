/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.core.persistence;

import java.util.Date;

import org.openhab.core.types.State;

/**
 * This class is used to define a filter for queries to a {@link PersistenceService}.
 * 
 * <p>It is designed as a Java bean, for which the different properties are constraints
 * on the query result. These properties include the item name, begin and end date and
 * the item state. A compare operator can be defined to compare not only state equality,
 * but also its decimal value (<,>).<p>
 * <p>Additionally, the filter criteria supports ordering and paging of the result, so the
 * caller can ask to only return chunks of the result of a certain size (=pageSize) from a
 * starting index (pageNumber*pageSize).</p>
 * <p>All setter methods return the filter criteria instance, so that the methods can be
 * easily chained in order to define a filter.
 * 
 * @author Kai Kreuzer
 * @since 1.0.0
 */
public class FilterCriteria {

	/** Enumeration with all possible compare options */
	public enum Operator {
		EQ("="),
		NEQ("!="),
		GT(">"), 
		LT("<"), 
		GTE(">="), 
		LTE("<=");
		
		private final String symbol;
		
		Operator(String symbol) {
			this.symbol = symbol;
		}
		
		String getSymbol() {
			return symbol;
		}
	}
	
	/** Enumeration with all ordering options */
	public enum Ordering {
		ASCENDING, DESCENDING
	}
	
	/** filter result to only contain entries for the given item */
	private String itemName;

	/** filter result to only contain entries that are newer than the given date */
	private Date beginDate;
	
	/** filter result to only contain entries that are older than the given date */
	private Date endDate;

	/** return the result list from starting index pageNumber*pageSize only */
	private int pageNumber = 0;
	
	/** return at most this many results */
	private int pageSize = Integer.MAX_VALUE;

	/** use this operator to compare the item state */
	private Operator operator = Operator.EQ;
	
	/** how to sort the result list by date */
	private Ordering ordering = Ordering.DESCENDING;

	/** filter result to only contain entries that evaluate to true with the given operator and state */
	private State state;

	public String getItemName() {
		return itemName;
	}

	public Date getBeginDate() {
		return beginDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public int getPageNumber() {
		return pageNumber;
	}

	public int getPageSize() {
		return pageSize;
	}

	public Operator getOperator() {
		return operator;
	}

	public Ordering getOrdering() {
		return ordering;
	}

	public State getState() {
		return state;
	}

	public FilterCriteria setItemName(String itemName) {
		this.itemName = itemName;
		return this;
	}

	public FilterCriteria setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
		return this;
	}

	public FilterCriteria setEndDate(Date endDate) {
		this.endDate = endDate;
		return this;
	}

	public FilterCriteria setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
		return this;
	}

	public FilterCriteria setPageSize(int pageSize) {
		this.pageSize = pageSize;
		return this;
	}

	public FilterCriteria setOperator(Operator operator) {
		this.operator = operator;
		return this;
	}

	public FilterCriteria setOrdering(Ordering ordering) {
		this.ordering = ordering;
		return this;
	}

	public FilterCriteria setState(State state) {
		this.state = state;
		return this;
	}

}
