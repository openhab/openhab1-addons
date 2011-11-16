package org.openhab.core.script.internal.engine;

import org.openhab.core.items.ItemRegistry;
import org.openhab.core.script.IItemRegistryProvider;
import org.openhab.core.script.internal.ScriptActivator;

import com.google.inject.Provider;
import com.google.inject.Singleton;

/**
 * This class allows guice-enabled classes to have access to the item registry
 * without going through OSGi declarative services.
 * Though it is very handy, this should be rather seen as a workaround - I am not
 * yet clear on how best to combine guice injection and OSGi DS.
 * 
 * @author Kai Kreuzer
 * @since 0.9.0
 *
 */
@Singleton
public class ItemRegistryProvider implements Provider<ItemRegistry>, IItemRegistryProvider {
	public ItemRegistry get() {
		ItemRegistry itemRegistry = (ItemRegistry) ScriptActivator.itemRegistryTracker.getService();
		return itemRegistry;
	}
}