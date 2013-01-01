/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010-2013, openHAB.org <admin@openhab.org>
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
import java.util.Arrays;

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
	private String[] backupFileNames = new String[] {
		"20120802173012", "20120701134442", "20120801191254", "20120805172351", "20120803095643"};
	private int[] expectedResultIndexes = new int[] { 0, 4, 3};
	
	
	@Before
	public void init() {
		Db4oConfiguration.maxBackups = 3;
		backupJob = new Db4oPersistenceService.BackupJob();
	}
	

	@Test
	public void testRemoveObsoleteBackupFiles() throws IOException {
		String testDbDirName = "./target/etc/db4o/";
		
		File db4oDir = new File(testDbDirName);
		db4oDir.mkdirs();
		FileUtils.cleanDirectory(db4oDir);
		
		for (int index = 0; index < backupFileNames.length; index++) {
			new File(testDbDirName + backupFileNames[index] + "_store.db4o.bak").createNewFile();
		}
		Assert.assertEquals(backupFileNames.length, db4oDir.listFiles().length);

		// Method under Test
		backupJob.removeObsoleteBackupFiles(testDbDirName);
		
		// Expected results ...
		File[] result = db4oDir.listFiles();
		Arrays.sort(result);
		
		Assert.assertEquals(Db4oConfiguration.maxBackups, result.length);
		for (int index = 0; index < result.length; index++) {
			Assert.assertEquals(backupFileNames[expectedResultIndexes[index]] + "_store.db4o.bak", result[index].getName());
		}
	}


}
