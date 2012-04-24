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
