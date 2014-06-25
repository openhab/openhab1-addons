package org.openhab.persistence.jpa.internal.model;

import java.util.Date;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.openhab.core.persistence.HistoricItem;
import org.openhab.core.types.State;

/**
 * 
 * @author Manfred Bergmann
 * @since 1.6.0
 *
 */

@MappedSuperclass
@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)
abstract public class JpaPersistentItem implements HistoricItem {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;

	private String name;
	private String realName;
	@Temporal(TemporalType.TIMESTAMP)
	private Date timestamp;
		
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
	
	abstract public String getValue();
	abstract public void setValue(String value);
	
	@Override
	public State getState() {
		// TODO Auto-generated method stub
		return null;
	}

}