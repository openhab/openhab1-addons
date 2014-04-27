/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.model.script.internal;

import org.openhab.core.events.EventPublisher;
import org.openhab.core.items.ItemRegistry;
import org.openhab.core.scriptengine.ScriptEngine;
import org.openhab.core.scriptengine.action.ActionService;
import org.openhab.model.core.ModelRepository;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

/**
 * Extension of the default OSGi bundle activator
 * 
 * @author Kai Kreuzer
 * @since 0.9.0
 */
public class ScriptActivator implements BundleActivator {
	
	public static ServiceTracker<ItemRegistry, ItemRegistry> itemRegistryTracker;
	public static ServiceTracker<EventPublisher, EventPublisher> eventPublisherTracker;
	public static ServiceTracker<ModelRepository, ModelRepository> modelRepositoryTracker;
	public static ServiceTracker<ScriptEngine, ScriptEngine> scriptEngineTracker;
	public static ServiceTracker<ActionService, ActionService> actionServiceTracker;

	/**
	 * Called whenever the OSGi framework starts our bundle
	 */
	public void start(BundleContext bc) throws Exception {
		actionServiceTracker = new ServiceTracker<ActionService, ActionService>(bc, ActionService.class, null);
		actionServiceTracker.open();

		itemRegistryTracker = new ServiceTracker<ItemRegistry, ItemRegistry>(bc, ItemRegistry.class, null);
		itemRegistryTracker.open();

		eventPublisherTracker = new ServiceTracker<EventPublisher, EventPublisher>(bc, EventPublisher.class, null);
		eventPublisherTracker.open();

		modelRepositoryTracker = new ServiceTracker<ModelRepository, ModelRepository>(bc, ModelRepository.class, null);
		modelRepositoryTracker.open();

		scriptEngineTracker = new ServiceTracker<ScriptEngine, ScriptEngine>(bc, ScriptEngine.class, null);
		scriptEngineTracker.open();
	}

	/**
	 * Called whenever the OSGi framework stops our bundle
	 */
	public void stop(BundleContext bc) throws Exception {
		itemRegistryTracker.close();
		eventPublisherTracker.close();
		modelRepositoryTracker.close();
		scriptEngineTracker.close();
		actionServiceTracker.close();
	}

}
