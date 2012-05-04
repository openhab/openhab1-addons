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
package org.openhab.persistence.rrd4j.internal;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.RejectedExecutionException;

import org.openhab.core.items.Item;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.persistence.PersistenceService;
import org.rrd4j.ConsolFun;
import org.rrd4j.DsType;
import org.rrd4j.core.RrdDb;
import org.rrd4j.core.RrdDef;
import org.rrd4j.core.Sample;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * This is the implementation of the RRD4j {@link PersistenceService}. To learn
 * more about RRD4j please visit their <a href="http://code.google.com/p/rrd4j/">website</a>.
 * 
 * @author Kai Kreuzer
 * @since 1.0.0
 */
public class RRD4jService implements PersistenceService {

	protected final static String DB_FOLDER = "etc/rrd4j";
	
	private static final Logger logger = LoggerFactory.getLogger(RRD4jService.class);
		
	/**
	 * @{inheritDoc}
	 */
	public String getName() {
		return "rrd4j";
	}

	/**
	 * @{inheritDoc}
	 */
	public void store(Item item, String alias) {
		RrdDb db = getDB(item.getName());
		if(db!=null) {
			try {
				Sample sample = db.createSample();
	            sample.setTime(System.currentTimeMillis()/1000);
	            
	            DecimalType state = (DecimalType) item.getStateAs(DecimalType.class);
	            if (state!=null) {
                    double value = state.toBigDecimal().doubleValue();
                    sample.setValue("state", value);
                    sample.update();
                    logger.debug("Stored item '{}' in rrd4j database", item.getName());
	            }
	            db.close();
			} catch (Exception e) {
				logger.warn("Could not persist item '{}' to rrd4j database: {}", new String[] { item.getName(), e.getMessage() });
			}
		}
	}

	/**
	 * @{inheritDoc}
	 */
	public void store(Item item) {
		store(item, null);
	}
	
	protected synchronized RrdDb getDB(String alias) {
		RrdDb db = null;
        File file = new File(DB_FOLDER + File.separator + alias + ".rrd");
    	try {
            if (file.exists()) {
            	// recreate the RrdDb instance from the file
            	db = new RrdDb(file.getAbsolutePath());
            } else {
            	File folder = new File(DB_FOLDER);
            	if(!folder.exists()) {
            		folder.mkdir();
            	}
            	// create a new database file
                RrdDef rrdDef = new RrdDef(file.getAbsolutePath());
                rrdDef.setStep(60);
                rrdDef.setStartTime(System.currentTimeMillis()/1000-1);
                rrdDef.addDatasource("state", DsType.GAUGE, 60, Double.NaN, Double.NaN);
                rrdDef.addArchive(ConsolFun.AVERAGE, 0.5, 1, 480); // 8 hours
                rrdDef.addArchive(ConsolFun.AVERAGE, 0.5, 4, 360); // one day
                rrdDef.addArchive(ConsolFun.AVERAGE, 0.5, 15, 644); // one week
                rrdDef.addArchive(ConsolFun.AVERAGE, 0.5, 60, 720); // one month
                rrdDef.addArchive(ConsolFun.AVERAGE, 0.5, 720, 730); // one year
                rrdDef.addArchive(ConsolFun.AVERAGE, 0.5, 10080, 520); // ten years
                db = new RrdDb(rrdDef);
            }
		} catch (IOException e) {
			logger.error("Could not create rrd4j database file '{}': {}", new String[] { file.getAbsolutePath(), e.getMessage() });
		} catch(RejectedExecutionException e) {
			logger.error("Could not create rrd4j database file '{}': {}", new String[] { file.getAbsolutePath(), e.getMessage() });
		}
		return db;
	}
	
}
