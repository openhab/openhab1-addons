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
package org.openhab.model.rule.internal;

import org.openhab.core.scriptengine.ScriptEngine;
import org.openhab.model.core.ModelRepository;
import org.openhab.model.rule.RulesStandaloneSetup;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Extension of the default OSGi bundle activator
 * 
 * @author Kai Kreuzer
 * @since 0.9.0
 */
public class RuleModelActivator implements BundleActivator {

	private final static Logger logger = LoggerFactory.getLogger(RuleModelActivator.class);

	public static ServiceTracker<ModelRepository, ModelRepository> modelRepositoryTracker;
	public static ServiceTracker<ScriptEngine, ScriptEngine> scriptEngineTracker;

	public void start(BundleContext bc) throws Exception {
		modelRepositoryTracker = new ServiceTracker<ModelRepository, ModelRepository>(bc, ModelRepository.class, null);
		modelRepositoryTracker.open();

		scriptEngineTracker = new ServiceTracker<ScriptEngine, ScriptEngine>(bc, ScriptEngine.class, null);
		scriptEngineTracker.open();		

		RulesStandaloneSetup.doSetup();
		logger.info("Registered 'rules' configuration parser");	
	}

	public void stop(BundleContext context) throws Exception {
	}

}
