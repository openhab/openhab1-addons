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
import java.io.FileReader;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.openhab.core.jsr223.internal.engine.RuleExecutionRunnable;
import org.openhab.core.jsr223.internal.shared.ChangedEventTrigger;
import org.openhab.core.jsr223.internal.shared.UpdatedEventTrigger;
import org.openhab.core.jsr223.internal.shared.CommandEventTrigger;
import org.openhab.core.jsr223.internal.shared.Event;
import org.openhab.core.jsr223.internal.shared.EventTrigger;
import org.openhab.core.jsr223.internal.shared.Openhab;
import org.openhab.core.jsr223.internal.shared.Rule;
import org.openhab.core.jsr223.internal.shared.RuleSet;
import org.openhab.core.jsr223.internal.shared.ShutdownTrigger;
import org.openhab.core.jsr223.internal.shared.StartupTrigger;
import org.openhab.core.jsr223.internal.shared.TimerTrigger;
import org.openhab.core.jsr223.internal.shared.TriggerType;
import org.openhab.core.library.types.DateTimeType;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.HSBType;
import org.openhab.core.library.types.IncreaseDecreaseType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.OpenClosedType;
import org.openhab.core.library.types.PercentType;
import org.openhab.core.library.types.PointType;
import org.openhab.core.library.types.StopMoveType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.library.types.UpDownType;
import org.openhab.core.persistence.*;
import org.openhab.core.persistence.extensions.PersistenceExtensions;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.openhab.core.types.UnDefType;
import org.openhab.library.tel.types.CallType;
import org.openhab.model.script.actions.BusEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A Script holds information about a script-file. Furthermore it feeds information and objects to the Jsr223
 * Script-Engine to allow interoperability with openHAB.
 * 
 * @author Simon Merschjohann
 * @author Helmut Lehmeyer
 * @since 1.7.0
 */
public class Script{
	static private final Logger logger = LoggerFactory.getLogger(Script.class);
	ArrayList<Rule> rules = new ArrayList<Rule>();
	private ScriptManager scriptManager;
	private ScriptEngine engine = null;
	private String fileName;

	public Script(ScriptManager scriptManager, File file) throws FileNotFoundException, ScriptException, NoSuchMethodException {
		this.scriptManager = scriptManager;
		this.fileName = file.getName();
		loadScript(file);
	}
	

	public void loadScript(File file) throws FileNotFoundException, ScriptException, NoSuchMethodException {
		logger.info("Loading Script " + file.getName());
		String extension = getFileExtension(file);
		ScriptEngineManager factory = new ScriptEngineManager();
		engine = factory.getEngineByExtension(extension);
		if (engine != null) {
			logger.info("EngineName: " + engine.getFactory().getEngineName());
			initializeSciptGlobals();
			engine.eval(new FileReader(file));
			Invocable inv = (Invocable) engine;
			RuleSet ruleSet = (RuleSet) inv.invokeFunction("getRules");
			rules.addAll(ruleSet.getRules());
		}
	}

	private void initializeSciptGlobals() {
		if(engine.getFactory().getEngineName().toLowerCase().endsWith("nashorn")){
			initializeNashornGlobals();
		}else{
			initializeGeneralGlobals();
		}
	}
	
	/**
	 * initializes Globals for Oracle Nashorn in conjunction with Java 8
	 * 
	 * To prevent Class loading Problems use this directive in start.sh/-.bat: -Dorg.osgi.framework.bundle.parent=ext
	 * further information: 
	 * http://apache-felix.18485.x6.nabble.com/org-osgi-framework-bootdelegation-and-org-osgi-framework-system-packages-extra-td4946354.html
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=466683
	 * http://spring.io/blog/2009/01/19/exposing-the-boot-classpath-in-osgi/
	 * http://osdir.com/ml/users-felix-apache/2015-02/msg00067.html
	 * http://stackoverflow.com/questions/30225398/java-8-scriptengine-across-classloaders
	 * 
	 * later we will get Autoimports for Classes in Nashorn:
	 * further information: 
	 * http://nashorn.36665.n7.nabble.com/8u60-8085937-add-autoimports-sample-script-to-easily-explore-Java-classes-in-interactive-mode-td4705.html
	 * 
	 * Later in a pure Java 8/9 environment: 
	 * http://mail.openjdk.java.net/pipermail/nashorn-dev/2015-February/004177.html
	 * Using Nashorn with interfaces loaded from custom classloaders, "script function" as a Java lambda:
	 * 
	 * 		engine.put("JavaClass", (Function<String, Class>)
	 * 				s -> {
	 * 					try {
	 * 						// replace this whatever Class finding logic here
	 * 						// say, using your own class loader(s) based search
	 * 						Class<?> c = Class.forName(s);
	 * 						logger.error("Class " + c.getName());
	 * 						logger.error("s " + s);
	 * 						return Class.forName(s);
	 * 					} catch (ClassNotFoundException cnfe) {
	 * 						throw new RuntimeException(cnfe);
	 * 					}
	 * 				});
	 * 		engine.eval("var System = JavaClass('java.lang.System').static");
	 * 		engine.eval("System.out.println('hello world')");
	 * 
	 */
	private void initializeNashornGlobals() {
		if( !Script.class.getClassLoader().getParent().toString().contains("ExtClassLoader") ){				
			logger.warn("Found wrong classloader: To prevent Class loading Problems use this directive in start.sh/-.bat: -Dorg.osgi.framework.bundle.parent=ext");
		}
		try {
			
			logger.info("initializeSciptGlobals for : " + engine.getFactory().getEngineName());			
			engine.put("ItemRegistry", 		scriptManager.getItemRegistry());
			engine.put("ir", 				scriptManager.getItemRegistry());
			engine.eval("var shared = org.openhab.core.jsr223.internal.shared,\n"
				+"RuleSet 				= Java.type('org.openhab.core.jsr223.internal.shared.RuleSet'),\n"
				+"Rule 					= Java.type('org.openhab.core.jsr223.internal.shared.Rule'),\n"
				+"ChangedEventTrigger 	= Java.type('org.openhab.core.jsr223.internal.shared.ChangedEventTrigger'),\n"
				+"CommandEventTrigger 	= Java.type('org.openhab.core.jsr223.internal.shared.CommandEventTrigger'),\n"
				+"Event 				= Java.type('org.openhab.core.jsr223.internal.shared.Event'),\n"
				+"EventTrigger			= Java.type('org.openhab.core.jsr223.internal.shared.EventTrigger'),\n"
				+"ShutdownTrigger 		= Java.type('org.openhab.core.jsr223.internal.shared.ShutdownTrigger'),\n"
				+"StartupTrigger 		= Java.type('org.openhab.core.jsr223.internal.shared.StartupTrigger'),\n"
				+"TimerTrigger 			= Java.type('org.openhab.core.jsr223.internal.shared.TimerTrigger'),\n"
				+"TriggerType 			= Java.type('org.openhab.core.jsr223.internal.shared.TriggerType'),\n"
				+"PersistenceExtensions	= Java.type('org.openhab.core.persistence.extensions.PersistenceExtensions'),\n"
				+"pe					= Java.type('org.openhab.core.persistence.extensions.PersistenceExtensions'),\n"
				+"HistoricItem			= Java.type('org.openhab.core.persistence.HistoricItem'),\n"
				+"oh 					= Java.type('org.openhab.core.jsr223.internal.shared.Openhab'),\n"
				+"State 				= Java.type('org.openhab.core.types.State'),\n"
				+"Command 				= Java.type('org.openhab.core.types.Command'),\n"
				+"DateTime 				= Java.type('org.joda.time.DateTime'),\n"
				+"StringUtils 			= Java.type('org.apache.commons.lang.StringUtils'),\n"
				+"URLEncoder 			= Java.type('java.net.URLEncoder'),\n"

				+"CallType 				= Java.type('org.openhab.library.tel.types.CallType'),\n"
				+"DateTimeType 			= Java.type('org.openhab.core.library.types.DateTimeType'),\n"
				+"DecimalType 			= Java.type('org.openhab.core.library.types.DecimalType'),\n"
				+"HSBType 				= Java.type('org.openhab.core.library.types.HSBType'),\n"
				+"IncreaseDecreaseType 	= Java.type('org.openhab.core.library.types.IncreaseDecreaseType'),\n"
				+"OnOffType 			= Java.type('org.openhab.core.library.types.OnOffType'),\n"
				+"OpenClosedType 		= Java.type('org.openhab.core.library.types.OpenClosedType'),\n"
				+"PercentType 			= Java.type('org.openhab.core.library.types.PercentType'),\n"
				+"PointType 			= Java.type('org.openhab.core.library.types.PointType'),\n"
				+"StopMoveType 			= Java.type('org.openhab.core.library.types.StopMoveType'),\n"
				+"UpDownType 			= Java.type('org.openhab.core.library.types.UpDownType'),\n"
				+"StringType 			= Java.type('org.openhab.core.library.types.StringType'),\n"
				+"UnDefType 			= Java.type('org.openhab.core.types.UnDefType'),\n"
				
				//As of now, Nashorn does not support calling super class methods.
				//http://nashorn-dev.openjdk.java.narkive.com/VX59ksgk/calling-super-methods-when-extending-classes
				//therefore:
				+"BusEvent 				= Java.type('org.openhab.model.script.actions.BusEvent'),\n"
				+"be 					= Java.type('org.openhab.model.script.actions.BusEvent'),\n"
				
				+"transform 			= oh.getAction('Transformation').static.transform,\n"
				
				//Item
				+"getItem 				= ItemRegistry.getItem,\n"
				+"postUpdate 			= BusEvent.postUpdate,\n"
				+"sendCommand 			= BusEvent.sendCommand,\n"
				
				//System
				+"FileUtils 			= Java.type('org.apache.commons.io.FileUtils'),\n"
				+"FilenameUtils			= Java.type('org.apache.commons.io.FilenameUtils'),\n"
				+"File 					= Java.type('java.io.File'),\n"
				
				+"ohEngine				= 'javascript';\n"
				
				//Helper Functions and Libs eventually later a lib Folder for default (Auto) loaded Libraries
				//Bas64: https://gist.github.com/ncerminara/11257943
				//+"load('configurations/scripts/jslib/b64.js');\n"
			);
			
		} catch (ScriptException e) {
			logger.error("ScriptException in initializeSciptGlobals while importing default-classes: ", e);
		}
	}
	
	private void initializeGeneralGlobals() {
		engine.put("RuleSet", 				RuleSet.class);
		engine.put("Rule", 					Rule.class);
		engine.put("ChangedEventTrigger", 	ChangedEventTrigger.class);
		engine.put("UpdatedEventTrigger", 	UpdatedEventTrigger.class);
		engine.put("CommandEventTrigger", 	CommandEventTrigger.class);
		engine.put("Event", 				Event.class);
		engine.put("EventTrigger", 			EventTrigger.class);
		engine.put("ShutdownTrigger", 		ShutdownTrigger.class);
		engine.put("StartupTrigger", 		StartupTrigger.class);
		engine.put("TimerTrigger", 			TimerTrigger.class);
		engine.put("TriggerType", 			TriggerType.class);
		engine.put("BusEvent", 				BusEvent.class);
		engine.put("be", 					BusEvent.class);
		engine.put("PersistenceExtensions", PersistenceExtensions.class);
		engine.put("pe", 					PersistenceExtensions.class);
		engine.put("HistoricItem", 			HistoricItem.class);
		engine.put("oh", 					Openhab.class);
		engine.put("State", 				State.class);
		engine.put("Command", 				Command.class);
		engine.put("ItemRegistry", 			scriptManager.getItemRegistry());
		engine.put("ir", 					scriptManager.getItemRegistry());
		engine.put("DateTime", 				DateTime.class);
		engine.put("StringUtils", 			StringUtils.class);
		engine.put("URLEncoder", 			URLEncoder.class);	
		engine.put("FileUtils", 			FileUtils.class);	
		engine.put("FilenameUtils", 		FilenameUtils.class);	
		engine.put("File", 					File.class);			

		// default types, TODO: auto import would be nice
		engine.put("CallType", 				CallType.class);
		engine.put("DateTimeType", 			DateTimeType.class);
		engine.put("DecimalType", 			DecimalType.class);
		engine.put("HSBType", 				HSBType.class);
		engine.put("IncreaseDecreaseType", 	IncreaseDecreaseType.class);
		engine.put("OnOffType", 			OnOffType.class);
		engine.put("OpenClosedType", 		OpenClosedType.class);
		engine.put("PercentType", 			PercentType.class);
		engine.put("PointType", 			PointType.class);
		engine.put("StopMoveType", 			StopMoveType.class);
		engine.put("UpDownType", 			UpDownType.class);
		engine.put("StringType", 			StringType.class);
		engine.put("UnDefType", 			UnDefType.class);
	}

	private String getFileExtension(File file) {
		String extension = null;
		if (file.getName().contains(".")) {
			String name = file.getName();
			extension = name.substring(name.lastIndexOf('.') + 1, name.length());
		}
		return extension;
	}

	public List<Rule> getRules() {
		return this.rules;
	}

	public void executeRule(Rule rule, Event event) {
		Thread t = new Thread(new RuleExecutionRunnable(rule, event));
		t.start();
	}

	public String getFileName() {
		return fileName;
	}

	public ScriptEngine getEngine() {
		return engine;
	}

}
