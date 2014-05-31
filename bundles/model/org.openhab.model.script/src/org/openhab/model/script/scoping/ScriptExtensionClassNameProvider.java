/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.model.script.scoping;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.util.Collection;

import org.apache.commons.lang.StringUtils;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.InputOutput;
import org.eclipse.xtext.xbase.scoping.featurecalls.StaticImplicitMethodsFeatureForTypeProvider.ExtensionClassNameProvider;
import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.openhab.core.items.Item;
import org.openhab.core.persistence.extensions.PersistenceExtensions;
import org.openhab.core.scriptengine.action.ActionService;
import org.openhab.core.types.Type;
import org.openhab.model.script.actions.BusEvent;
import org.openhab.model.script.actions.LogAction;
import org.openhab.model.script.actions.ScriptExecution;
import org.openhab.model.script.internal.ScriptActivator;
import org.openhab.model.script.lib.NumberExtensions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Multimap;
import com.google.inject.Singleton;

/**
 * This class registers all statically available functions as well as the
 * extensions for specific jvm types.
 * 
 * @author Kai Kreuzer
 * @since 0.9.0
 *
 */
@SuppressWarnings({ "restriction", "deprecation" })
@Singleton
public class ScriptExtensionClassNameProvider extends ExtensionClassNameProvider {

	private final static Logger logger = LoggerFactory.getLogger(ScriptExtensionClassNameProvider.class);
	
	private int trackingCount = -1;
	
	@Override
	protected synchronized Collection<String> getLiteralClassNames() {
		int currentTrackingCount = ScriptActivator.actionServiceTracker.getTrackingCount();
		
		// if something has changed about the tracked services, recompute the list
		if(trackingCount != currentTrackingCount) {
			String actions = "";
			for(Object obj : ScriptActivator.actionServiceTracker.getServices()) {
				actions += obj.getClass().getSimpleName() + ", ";
			}
			logger.debug("Script actions have changed: " + actions);
			trackingCount = currentTrackingCount;
			return computeLiteralClassNames();
		} else {
			return super.getLiteralClassNames();
		}
	}

	@Override
	protected Collection<String> computeLiteralClassNames() {

		// we completely define the content ourselves, but need the collection
		// instance from the super class as it is a private field
		Collection<String> literalClassNames = super.getLiteralClassNames();
		
		if(literalClassNames==null) {
			literalClassNames = super.computeLiteralClassNames();
		}

		literalClassNames.clear();
		
		// add all actions that are contributed as OSGi services
		Object[] services = ScriptActivator.actionServiceTracker.getServices();
		if(services!=null) {
			for(Object service : services) {
				ActionService actionService = (ActionService) service;
				literalClassNames.add(actionService.getActionClassName());
			}
		}
		
		literalClassNames.add(CollectionLiterals.class.getName());
		literalClassNames.add(InputOutput.class.getName());

		literalClassNames.add(BusEvent.class.getCanonicalName());
		literalClassNames.add(ScriptExecution.class.getCanonicalName());
		literalClassNames.add(LogAction.class.getCanonicalName());

		// jodatime static functions
		literalClassNames.add(DateTime.class.getCanonicalName());
		literalClassNames.add(DateMidnight.class.getCanonicalName());
		return literalClassNames;
	}
	
	@Override
	protected Multimap<Class<?>, Class<?>> simpleComputeExtensionClasses() {
		Multimap<Class<?>, Class<?>> result = super.simpleComputeExtensionClasses();
		result.removeAll(Comparable.class);
		result.removeAll(Double.class);
		result.removeAll(Integer.class);
		result.removeAll(BigInteger.class);
		result.removeAll(BigDecimal.class);
		result.removeAll(double.class);
		result.put(Number.class, NumberExtensions.class);
		result.put(Type.class, NumberExtensions.class);
		result.put(Comparable.class, NumberExtensions.class);
		result.put(String.class, StringUtils.class);
		result.put(String.class, URLEncoder.class);
		result.put(Item.class, PersistenceExtensions.class);
		result.put(Item.class, BusEvent.class);
		return result;
	}
}
