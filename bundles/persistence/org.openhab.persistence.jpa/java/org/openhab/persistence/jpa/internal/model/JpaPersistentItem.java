/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.persistence.jpa.internal.model;

import java.text.DateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.openhab.core.persistence.HistoricItem;
import org.openhab.core.types.State;

/**
 * This is the DAO object used for storing and retrieving to and from database.
 * 
 * @author Manfred Bergmann
 * @since 1.6.0
 *
 */

@Entity
@Table(name = "HISTORIC_ITEM")
public class JpaPersistentItem implements HistoricItem {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;

	private String name = "";
	private String realName = "";
	@Temporal(TemporalType.TIMESTAMP)
	private Date timestamp = new Date();
	@Column(length = 32672) // 32k, max varchar for apache derby
	private String value = "";

	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public Date getTimestamp() {
		return timestamp;
	}
	
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
	
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	@Override
	public State getState() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String toString() {
		return DateFormat.getDateTimeInstance().format(getTimestamp()) + ": " + getName() + " -> "+ value;
	}
}
