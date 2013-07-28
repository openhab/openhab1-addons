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
package org.openhab.model.script.scoping;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.util.Collection;

import org.apache.commons.lang.StringUtils;
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
@SuppressWarnings("restriction")
@Singleton
public class ScriptExtensionClassNameProvider extends ExtensionClassNameProvider {

	private int trackingCount = -1;
	
	@Override
	protected Collection<String> getLiteralClassNames() {
		int currentTrackingCount = ScriptActivator.actionServiceTracker.getTrackingCount();
		
		// if something has changed about the tracked services, recompute the list
		if(trackingCount != currentTrackingCount) {
			trackingCount = currentTrackingCount;
			return computeLiteralClassNames();
		} else {
			return super.getLiteralClassNames();
		}
	}

	@Override
	protected Collection<String> computeLiteralClassNames() {
		Collection<String> extensions = super.computeLiteralClassNames();
		
		// add all actions that are contributed as OSGi services
		Object[] services = ScriptActivator.actionServiceTracker.getServices();
		if(services!=null) {
			for(Object service : services) {
				ActionService actionService = (ActionService) service;
				extensions.add(actionService.getActionClassName());
			}
		}
		
		extensions.add(BusEvent.class.getCanonicalName());
		extensions.add(ScriptExecution.class.getCanonicalName());
		extensions.add(LogAction.class.getCanonicalName());

		// jodatime static functions
		extensions.add(DateTime.class.getCanonicalName());
		extensions.add(DateMidnight.class.getCanonicalName());
		return extensions;
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
