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
package org.openhab.model.persistence.scoping;

import java.util.List;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceImpl;
import org.eclipse.xtext.resource.EObjectDescription;
import org.eclipse.xtext.resource.IEObjectDescription;
import org.eclipse.xtext.scoping.IScope;
import org.eclipse.xtext.scoping.impl.AbstractGlobalScopeProvider;
import org.eclipse.xtext.scoping.impl.SimpleScope;
import org.openhab.model.persistence.persistence.Strategy;

import com.google.common.base.Predicate;
import com.google.common.collect.Lists;

public class PersistenceGlobalScopeProvider extends AbstractGlobalScopeProvider {

	static protected Resource res = new ResourceImpl();

	static {
		res.setURI(URI.createURI("virtual://openhab.org/persistence/strategy.global"));
		res.getContents().add(GlobalStrategies.UPDATE);
		res.getContents().add(GlobalStrategies.CHANGE);
		res.getContents().add(GlobalStrategies.RESTORE);
	}
	
	@Override
	protected IScope getScope(Resource resource, boolean ignoreCase,
			EClass type, Predicate<IEObjectDescription> predicate) {
		IScope parentScope = super.getScope(resource, ignoreCase, type, predicate);
		List<IEObjectDescription> descs = Lists.newArrayList();
		for(EObject eObj : res.getContents()) {
			if (eObj instanceof Strategy) {
				Strategy strategy = (Strategy) eObj;
				descs.add(EObjectDescription.create(strategy.getName(), strategy));
			}
		}
		return new SimpleScope(parentScope, descs);
	}
	
}
