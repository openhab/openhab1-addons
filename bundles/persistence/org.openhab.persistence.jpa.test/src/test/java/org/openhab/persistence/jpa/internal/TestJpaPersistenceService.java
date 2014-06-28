package org.openhab.persistence.jpa.internal;

public class TestJpaPersistenceService extends JpaPersistenceService {

	@Override
	protected String getPersistenceUnitName() {
		return "default_test";
	}
	
}
