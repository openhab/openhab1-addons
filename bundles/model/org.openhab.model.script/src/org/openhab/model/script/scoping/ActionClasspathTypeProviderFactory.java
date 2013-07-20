package org.openhab.model.script.scoping;

import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.xtext.common.types.access.ClasspathTypeProviderFactory;
import org.eclipse.xtext.common.types.access.impl.ClasspathTypeProvider;

import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * This class makes use of the {@link ActionClassLoader} instead of a normal one.
 * 
 * @author Kai Kreuzer
 * @since 1.3.0
 *
 */
@SuppressWarnings("restriction")
@Singleton
public class ActionClasspathTypeProviderFactory extends ClasspathTypeProviderFactory {

	@Inject
	public ActionClasspathTypeProviderFactory(ClassLoader classLoader) {
		super(new ActionClassLoader(classLoader));
	}

	@Override
	protected ClasspathTypeProvider createClasspathTypeProvider(ResourceSet resourceSet) {
		return new ClasspathTypeProvider(new ActionClassLoader(super.getClassLoader()), resourceSet, getIndexedJvmTypeAccess());
	}

}
