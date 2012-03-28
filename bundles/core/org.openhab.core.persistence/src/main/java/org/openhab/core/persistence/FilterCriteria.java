/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010-2012, openHAB.org <admin@openhab.org>
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
package org.openhab.core.persistence;

import java.util.Date;

import org.openhab.core.types.State;

/**
 * This class is used to define a filter for queries to a {@link PersistenceService} 
 * 
 * @author Kai Kreuzer
 * @since 1.0.0
 */
public class FilterCriteria {

	enum Operator {
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

	private String itemName;
	private Date beginDate;
	private Date endDate;
	private Long pageNumber;
	private Long pageSize;
	private Operator operator;
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

	public Long getPageNumber() {
		return pageNumber;
	}

	public Long getPageSize() {
		return pageSize;
	}

	public Operator getOperator() {
		return operator;
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

	public FilterCriteria setPageNumber(Long pageNumber) {
		this.pageNumber = pageNumber;
		return this;
	}

	public FilterCriteria setPageSize(Long pageSize) {
		this.pageSize = pageSize;
		return this;
	}

	public FilterCriteria setOperator(Operator operator) {
		this.operator = operator;
		return this;
	}

	public FilterCriteria setState(State state) {
		this.state = state;
		return this;
	}

}
