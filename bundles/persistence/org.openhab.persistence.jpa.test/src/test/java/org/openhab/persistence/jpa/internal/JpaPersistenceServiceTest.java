/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.persistence.jpa.internal;

import static org.junit.Assert.*;

import java.io.File;
import java.util.Collections;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;
import org.openhab.core.items.GenericItem;
import org.openhab.core.library.types.StringType;
import org.openhab.core.persistence.FilterCriteria;
import org.openhab.core.persistence.HistoricItem;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;


/**
 * @author Manfred Bergmann
 * @since 1.6.0
 */
public class JpaPersistenceServiceTest {
	
	@BeforeClass
	static public void initClass() {
		System.setProperty("derby.system.home", "./target/testdb");
		File testdbFolder = new File("./target/testdb");
		if(!testdbFolder.exists()) {
			testdbFolder.mkdir();
		}
		
		JpaConfiguration.dbConnectionUrl = "jdbc:derby:data;create=true";
		JpaConfiguration.dbDriverClass = "org.apache.derby.jdbc.EmbeddedDriver";
		JpaConfiguration.dbUserName = "APP";
		JpaConfiguration.dbPassword = "APP";
		JpaConfiguration.isInitialized = true;
	}
	
	@Test
	public void testPersistStringItem() {
		GenericItem testItem = new GenericItem("testItem") {
			
			@Override
			public List<Class<? extends State>> getAcceptedDataTypes() {
				return Collections.emptyList();
			}
			
			@Override
			public List<Class<? extends Command>> getAcceptedCommandTypes() {
				return Collections.emptyList();
			}
		};
		
		testItem.setState(StringType.valueOf("foobar"));
		
		JpaPersistenceService service = new TestJpaPersistenceService();
		service.store(testItem);
		
		FilterCriteria crit = new FilterCriteria();
		crit.setItemName("testItem");
		crit.setState(testItem.getState());
		Iterable<HistoricItem> result = service.query(crit);
		
		int len = 0;
		while(result.iterator().hasNext()) {
			len++;
		}
		
		assertTrue(len == 1);
	}

}
