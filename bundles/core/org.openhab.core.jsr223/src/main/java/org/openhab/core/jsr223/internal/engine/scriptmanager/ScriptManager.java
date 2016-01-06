/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.core.jsr223.internal.engine.scriptmanager;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.openhab.config.core.ConfigDispatcher;
import org.openhab.core.items.ItemRegistry;
import org.openhab.core.jsr223.internal.engine.RuleTriggerManager;
import org.openhab.core.jsr223.internal.shared.Event;
import org.openhab.core.jsr223.internal.shared.EventTrigger;
import org.openhab.core.jsr223.internal.shared.Rule;
import org.openhab.core.jsr223.internal.shared.StartupTrigger;
import org.openhab.core.jsr223.internal.shared.TriggerType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Main component of script engine. It checks for available Script engines, 
 * loads scripts from the scripts directory
 * and listens for script changes (which lead to script reloading)
 * 
 * @author Simon Merschjohann
 * @since 1.7.0
 */
public class ScriptManager {
	static private final Logger logger = LoggerFactory.getLogger(ScriptManager.class);

	public HashMap<String, Script> scripts = new HashMap<String, Script>();
	public HashMap<Rule, Script> ruleMap = new HashMap<Rule, Script>();

	private ItemRegistry itemRegistry;

	private RuleTriggerManager triggerManager;

	private Thread scriptUpdateWatcher;

	private static ScriptManager instance;

	public ScriptManager(RuleTriggerManager triggerManager, ItemRegistry itemRegistry) {
		this.triggerManager = triggerManager;
		instance = this;
		logger.info("Available engines:");
		for (ScriptEngineFactory f : new ScriptEngineManager().getEngineFactories()) {
			logger.info(f.getEngineName());
		}

		this.setItemRegistry(itemRegistry);

		File folder = getFolder("scripts");

		if (folder.exists() && folder.isDirectory()) {
			loadScripts(folder);

			scriptUpdateWatcher = new Thread(new ScriptUpdateWatcher(this, folder));
			scriptUpdateWatcher.start();
		} else {
			logger.warn("Script directory: jsr_scripts missing, no scripts will be added!");
		}
	}

	public void loadScripts(File folder) {
		for (File file : folder.listFiles()) {
			loadScript(file);
		}
	}

	private Script loadScript(File file) {
		Script script = null;
		try {
			script = new Script(this, file);
			scripts.put(file.getName(), script);
			List<Rule> newRules = script.getRules();
			for (Rule rule : newRules) {
				ruleMap.put(rule, script);
			}

			// add all rules to the needed triggers
			triggerManager.addRuleModel(newRules);

		} catch(NoSuchMethodException e) {
			logger.error("Script file misses mandotary function: getRules()", e);
		} catch (FileNotFoundException e) {
			logger.error("script file not found", e);
		} catch (ScriptException e) {
			logger.error("script exception", e);
		} catch (Exception e) {
			logger.error("unknown exception", e);
		}

		return script;
	}

	public static ScriptManager getInstance() {
		return instance;
	}

	public Collection<Rule> getAllRules() {
		return ruleMap.keySet();
	}

	public ItemRegistry getItemRegistry() {
		return itemRegistry;
	}

	public void setItemRegistry(ItemRegistry itemRegistry) {
		this.itemRegistry = itemRegistry;
	}

	public synchronized void executeRules(Rule[] rules, org.openhab.core.jsr223.internal.shared.Event event) {
		for (Rule rule : rules) {
			ruleMap.get(rule).executeRule(rule, event);
		}
	}

	public synchronized void executeRules(Iterable<Rule> rules, org.openhab.core.jsr223.internal.shared.Event event) {
		for (Rule rule : rules) {
			ruleMap.get(rule).executeRule(rule, event);
		}
	}

	/**
	 * returns the {@link File} object for a given foldername
	 * 
	 * @param foldername
	 *            the foldername to get the {@link File} for
	 * @return the corresponding {@link File}
	 */
	private File getFolder(String foldername) {
		File folder = new File(ConfigDispatcher.getConfigFolder() + File.separator + foldername);
		return folder;
	}

	public Script getScript(Rule rule) {
		return ruleMap.get(rule);
	}

	public void scriptsChanged(List<File> addedScripts, List<File> removedScripts, List<File> modifiedScripts) {

		for (File scriptFile : removedScripts) {
			removeScript(scriptFile.getName());
		}

		for (File scriptFile : addedScripts) {
			Script script = loadScript(scriptFile);
			runStartupRules(script);
		}

		for (File scriptFile : modifiedScripts) {
			removeScript(scriptFile.getName());
			Script script = loadScript(scriptFile);
			runStartupRules(script);
		}
	}

	private void runStartupRules(Script script) {
		if (script != null) {
			ArrayList<Rule> toTrigger = new ArrayList<Rule>();
			for (Rule rule : script.getRules()) {
				for (EventTrigger trigger : rule.getEventTrigger()) {
					if (trigger instanceof StartupTrigger) {
						toTrigger.add(rule);
						break;
					}
				}
			}
			if (toTrigger.size() > 0)
				executeRules(toTrigger, new Event(TriggerType.STARTUP, null, null, null, null));
		}
	}

	private void removeScript(String scriptName) {
		if(scripts.containsKey(scriptName)) {
			Script script = scripts.remove(scriptName);

			List<Rule> allRules = script.getRules();

			triggerManager.removeRuleModel(allRules);
			for (Rule rule : allRules) {
				ruleMap.remove(rule);
			}
		}
	}

}
