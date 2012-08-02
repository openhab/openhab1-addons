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
package org.openhab.persistence.db4o.internal;

import java.io.File;
import java.io.IOException;

import junit.framework.Assert;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;
import org.openhab.persistence.db4o.internal.Db4oPersistenceService.BackupJob;


/**
 * @author Thomas.Eichstaedt-Engelen
 * @since 1.0.0
 */
public class Db4oPersistenceServiceTest {
	
	private BackupJob backupJob;
	
	@Before
	public void init() {
		Db4oConfiguration.maxBackups = 3;
		backupJob = new Db4oPersistenceService.BackupJob();
	}
	

	@Test
	public void test() throws IOException {
		String testDbDirName = "./target/etc/db4o/";
		
		File db4oDir = new File(testDbDirName);
		db4oDir.mkdirs();
		
		FileUtils.cleanDirectory(db4oDir);
		
		new File(testDbDirName + "20120802173012_store.db4o.bak").createNewFile();
		new File(testDbDirName + "20120701134442_store.db4o.bak").createNewFile();
		new File(testDbDirName + "20120801191254_store.db4o.bak").createNewFile();
		new File(testDbDirName + "20120805172351_store.db4o.bak").createNewFile();
		new File(testDbDirName + "20120803095643_store.db4o.bak").createNewFile();
		
		Assert.assertEquals(5, db4oDir.listFiles().length);
		
		backupJob.removeObsoleteBackupFiles(testDbDirName);
		
		File[] result = db4oDir.listFiles();
		Assert.assertEquals(3, result.length);
		Assert.assertEquals("20120802173012_store.db4o.bak", result[0].getName());
		Assert.assertEquals("20120803095643_store.db4o.bak", result[1].getName());
		Assert.assertEquals("20120805172351_store.db4o.bak", result[2].getName());
	}


}
