/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2011, openHAB.org <admin@openhab.org>
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

package org.openhab.core.transform.internal;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.openhab.core.transform.TransformationProcessor;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Extension of the default OSGi bundle activator
 * 
 * @author Thomas.Eichstaedt-Engelen
 * @since 0.7.0
 */
public final class TransformationActivator implements BundleActivator {

	private static Logger logger = LoggerFactory.getLogger(TransformationActivator.class); 
	
	/** instance of the {@link ServiceTracker} which tracks the availability of all {@link TransformationProcessor}s */
	private ServiceTracker processorServiceTracker;
	
	/** maps the component name to the processor instances */
	private static Map<String, TransformationProcessor> processorCache;
	
	
	public TransformationActivator() {
		processorCache = new HashMap<String, TransformationProcessor>();
	}
	
	
	public static Map<String, TransformationProcessor> getProcessorMap() {
		return processorCache;
	}
	
	/**
	 * Called whenever the OSGi framework starts our bundle
	 */
	public void start(BundleContext bc) throws Exception {
		logger.debug("Transformation Service has been started.");
		
		processorServiceTracker = new TransformationProcessorTracker(bc);
		processorServiceTracker.open();
	}

	/**
	 * Called whenever the OSGi framework stops our bundle
	 */
	public void stop(BundleContext bc) throws Exception {
		logger.debug("Transformation Service has been stopped.");
		
		if (processorServiceTracker != null) {
			processorServiceTracker.close();
		}
	}
	
	
	/**
	 * This specialization of the ServiceTracker keeps track of all 
	 * {@link TransformationProcessor}s of openHAB. It stores their references
	 * into the <code>processorCache</code>. As key we use the last part (after
	 * the last '.') of the components' name.
	 * 
	 * @author Thomas.Eichstaedt-Engelen
	 * @since 0.7.0
	 */
	class TransformationProcessorTracker extends ServiceTracker {

		public TransformationProcessorTracker(BundleContext bc) {
			super(bc, TransformationProcessor.class.getName(), null);
		}
		
		@Override
		public Object addingService(ServiceReference reference) {
			
			TransformationProcessor processor = 
					(TransformationProcessor) context.getService(reference);
			
			String processorKey = extractComponentName(reference);
			processorCache.put(processorKey, processor);
			
			return processor;
		}
		
		@Override
		public void removedService(ServiceReference reference, Object service) {
			context.ungetService(reference);
			
			String processorKey = extractComponentName(reference);
			processorCache.remove(processorKey);
		}
		
		/**
		 * Extracts the last part (after the last '.') of the 
		 * <code>component.name</code> property out of the <code reference</code>
		 * to be used as key in the <code>processorCache</code>.
		 *  
		 * @param reference the {@link ServiceReference} to extract to key from
		 * @return the last part (after the last '.') of the <code>component.name</code>
		 * property
		 */
		private String extractComponentName(ServiceReference reference) {
			String componentName = 
				(String) reference.getProperty("component.name");
			return StringUtils.substringAfterLast(componentName, ".").toUpperCase();
		}
		
	}
	
}
