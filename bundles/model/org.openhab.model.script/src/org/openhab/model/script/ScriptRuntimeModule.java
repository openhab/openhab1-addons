/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
/*
 * generated by Xtext
 */
package org.openhab.model.script;

import org.eclipse.xtext.xbase.featurecalls.IdentifiableSimpleNameProvider;
import org.eclipse.xtext.xbase.interpreter.IExpressionInterpreter;
import org.eclipse.xtext.xbase.scoping.featurecalls.StaticImplicitMethodsFeatureForTypeProvider.ExtensionClassNameProvider;
import org.openhab.core.scriptengine.Script;
import org.openhab.model.script.internal.engine.ScriptImpl;
import org.openhab.model.script.interpreter.ScriptInterpreter;
import org.openhab.model.script.jvmmodel.ScriptIdentifiableSimpleNameProvider;
import org.openhab.model.script.scoping.ActionClassLoader;
import org.openhab.model.script.scoping.ActionClasspathBasedTypeScopeProvider;
import org.openhab.model.script.scoping.ActionClasspathTypeProviderFactory;
import org.openhab.model.script.scoping.ScriptExtensionClassNameProvider;
import org.openhab.model.script.scoping.ScriptScopeProvider;
import org.openhab.model.script.scoping.StateAndCommandProvider;

/**
 * Use this class to register components to be used at runtime / without the Equinox extension registry.
 */
@SuppressWarnings("restriction")
public class ScriptRuntimeModule extends org.openhab.model.script.AbstractScriptRuntimeModule {

	public Class<? extends ExtensionClassNameProvider> bindExtensionClassNameProvider() {
		return ScriptExtensionClassNameProvider.class;
	}
	
	public Class<? extends IdentifiableSimpleNameProvider> bindIdentifiableSimpleNameProvider() {
		return ScriptIdentifiableSimpleNameProvider.class;
	}

	public Class<? extends Script> bindScript() {
		return ScriptImpl.class;
	}
	
	public Class<? extends IExpressionInterpreter> bindIExpressionInterpreter() {
		return ScriptInterpreter.class;
	}
	
	public Class<StateAndCommandProvider> bindStateAndCommandProvider() {
		return StateAndCommandProvider.class;
	}

	@Override
	public Class<? extends org.eclipse.xtext.scoping.IScopeProvider> bindIScopeProvider() {
		return ScriptScopeProvider.class;
	}

	/* we need this so that our pluggable actions can be resolved at design time */
	@Override
	public Class<? extends org.eclipse.xtext.common.types.access.IJvmTypeProvider.Factory> bindIJvmTypeProvider$Factory() {
		return ActionClasspathTypeProviderFactory.class;
	}
	
	/* we need this so that our pluggable actions can be resolved when being parsed at runtime */
	@Override
	public Class<? extends org.eclipse.xtext.common.types.xtext.AbstractTypeScopeProvider> bindAbstractTypeScopeProvider() {
		return ActionClasspathBasedTypeScopeProvider.class;
	}

	/* we need this so that our pluggable actions can be resolved when being executed at runtime */
	@Override
	public ClassLoader bindClassLoaderToInstance() {
		return new ActionClassLoader(getClass().getClassLoader());
	}

}
