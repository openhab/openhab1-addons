package org.openhab.persistence.jpa.internal.model;

import java.text.DateFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 
 * @author Manfred Bergmann
 * @since 1.6.0
 *
 */

@Entity
@Table(name = "HISTORIC_STRING_ITEM")
public class JpaPersistentStringItem extends JpaPersistentItem {

	@Column(length = 20000)
	private String value;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return DateFormat.getDateTimeInstance().format(getTimestamp()) + ": " + getName() + " -> "+ value;
	}
}
