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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.xtext.common.types.TypesFactory;
import org.eclipse.xtext.common.types.access.IJvmTypeProvider;
import org.eclipse.xtext.resource.EObjectDescription;
import org.eclipse.xtext.resource.IEObjectDescription;
import org.eclipse.xtext.scoping.IScope;
import org.eclipse.xtext.scoping.impl.MapBasedScope;
import org.eclipse.xtext.xbase.jvmmodel.IJvmModelAssociations;
import org.eclipse.xtext.xbase.scoping.XbaseScopeProvider;
import org.openhab.core.items.Item;
import org.openhab.core.items.ItemRegistry;
import org.openhab.core.types.Type;
import org.openhab.model.script.internal.engine.ItemRegistryProvider;

import com.google.inject.Inject;

/**
 * This scope provider adds all items, states and commands to the scope, so that
 * these are always available in the scripts.
 * 
 * @author Kai Kreuzer
 * @since 0.9.0
 *
 */
@SuppressWarnings("all")
public class ScriptScopeProvider extends XbaseScopeProvider {

	@Inject
	private IJvmTypeProvider.Factory typeProviderFactory;
	
	@Inject
	private ItemRegistryProvider itemRegistryProvider;
	
	@Inject
	private StateAndCommandProvider stateAndCommandProvider;

	public ScriptScopeProvider() {
	}

	@Override
	public IScope createSimpleFeatureCallScope(EObject context,
			EReference reference, Resource resource,
			boolean includeCurrentBlock, int idx) {
		IScope parent =  super.createSimpleFeatureCallScope(context, reference, resource,
				includeCurrentBlock, idx);
		List<IEObjectDescription> descriptions = new ArrayList<IEObjectDescription>();
		descriptions.addAll(createItemFeatures(resource.getResourceSet()));
		descriptions.addAll(createTypeFeatures(resource.getResourceSet()));

		return MapBasedScope.createScope(parent, descriptions);
	}
	
	private Collection<? extends IEObjectDescription> createTypeFeatures(ResourceSet rs) {
				
		List<IEObjectDescription> descriptions = new ArrayList<IEObjectDescription>();
		IJvmTypeProvider provider = typeProviderFactory.findOrCreateTypeProvider(rs);
		for(Type type : stateAndCommandProvider.getAllTypes()) {
			descriptions.add(EObjectDescription.create(type.toString(), provider.findTypeByName(type.getClass().getCanonicalName())));
		}
		
		return descriptions;
	}

	private List<IEObjectDescription> createItemFeatures(ResourceSet rs) {
		IJvmTypeProvider provider = typeProviderFactory.findOrCreateTypeProvider(rs);
		List<IEObjectDescription> descriptions = new ArrayList<IEObjectDescription>();
		ItemRegistry itemRegistry = itemRegistryProvider.get();
		if(itemRegistry!=null) {
			for(Item item : itemRegistry.getItems()) {
				descriptions.add(EObjectDescription.create(item.getName(), provider.findTypeByName(item.getClass().getCanonicalName())));
			}
		}
		return descriptions;
	}

}