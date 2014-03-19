/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.model.script.internal.engine;

import static com.google.common.collect.Iterables.filter;

import java.io.IOException;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.Resource.Diagnostic;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.xtext.diagnostics.Severity;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.resource.XtextResourceSet;
import org.eclipse.xtext.util.CancelIndicator;
import org.eclipse.xtext.util.StringInputStream;
import org.eclipse.xtext.validation.CheckMode;
import org.eclipse.xtext.validation.IResourceValidator;
import org.eclipse.xtext.validation.Issue;
import org.eclipse.xtext.xbase.XExpression;
import org.openhab.core.scriptengine.Script;
import org.openhab.core.scriptengine.ScriptEngine;
import org.openhab.core.scriptengine.ScriptExecutionException;
import org.openhab.core.scriptengine.ScriptParsingException;
import org.openhab.model.script.ScriptStandaloneSetup;

import com.google.common.base.Predicate;
import com.google.inject.Injector;

/**
 * This is the implementation of a {@link ScriptEngine} which is made available as an OSGi service.
 * 
 * @author Kai Kreuzer
 * @since 0.9.0
 *
 */
@SuppressWarnings("restriction")
public class ScriptEngineImpl implements ScriptEngine {

	protected Injector guiceInjector;
	protected XtextResourceSet resourceSet;

	public ScriptEngineImpl() {}
	
	public void activate() {
		this.guiceInjector = new ScriptStandaloneSetup().createInjectorAndDoEMFRegistration();
		this.resourceSet = guiceInjector.getInstance(XtextResourceSet.class);
		resourceSet.addLoadOption(XtextResource.OPTION_RESOLVE_ALL, Boolean.TRUE);
	}
	
	public void deactivate() {
		this.guiceInjector = null;
		this.resourceSet = null;
	}
		
	/**
	 * {@inheritDoc}
	 */
	public Script newScriptFromString(String scriptAsString)
			throws ScriptParsingException {
		return newScriptFromXExpression(parseScriptIntoXTextEObject(scriptAsString));
	}

	/**
	 * {@inheritDoc}
	 */
	public Script newScriptFromXExpression(XExpression expression) {
		ScriptImpl script = guiceInjector.getInstance(ScriptImpl.class);
		script.setXExpression(expression);
		return script;
	}

	/**
	 * {@inheritDoc}
	 */
	public Object executeScript(String scriptAsString)
			throws ScriptParsingException, ScriptExecutionException {
		return newScriptFromString(scriptAsString).execute();
	}

	private XExpression parseScriptIntoXTextEObject(String scriptAsString) throws ScriptParsingException {
		Resource resource = resourceSet.createResource(computeUnusedUri(resourceSet)); // IS-A XtextResource
		try {
			resource.load(new StringInputStream(scriptAsString), resourceSet.getLoadOptions());
		} catch (IOException e) {
			throw new ScriptParsingException("Unexpected IOException; from close() of a String-based ByteArrayInputStream, no real I/O; how is that possible???", scriptAsString, e);
		}
		
		List<Diagnostic> errors = resource.getErrors();
		if (errors.size() != 0) {
			throw new ScriptParsingException("Failed to parse expression (due to managed SyntaxError/s)", scriptAsString).addDiagnosticErrors(errors);
		}
		
		EList<EObject> contents = resource.getContents();

		if (!contents.isEmpty()) {
			Iterable<Issue> validationErrors = getValidationErrors(contents.get(0));
			if(!validationErrors.iterator().hasNext()) {
				return (XExpression) contents.get(0);
			} else {
				throw new ScriptParsingException("Failed to parse expression (due to managed ValidationError/s)", scriptAsString).addValidationIssues(validationErrors);
			}
		} else {
			return null;
		}
	}

	protected URI computeUnusedUri(ResourceSet resourceSet) {
		String name = "__synthetic";
		final int MAX_TRIES=1000;
		for(int i=0; i<MAX_TRIES; i++) {
			// NOTE: The "filename extension" (".script") must match the file.extensions in the *.mwe2
			URI syntheticUri = URI.createURI(name+Math.random()+ "." + Script.SCRIPT_FILEEXT);
			if (resourceSet.getResource(syntheticUri, false)==null)
				return syntheticUri;
		} 
		throw new IllegalStateException();
	}

	protected List<Issue> validate(EObject model) {
		IResourceValidator validator = ((XtextResource) model.eResource()).getResourceServiceProvider().getResourceValidator();
		return validator.validate(model.eResource(), CheckMode.ALL, CancelIndicator.NullImpl);
	}
	
	protected Iterable<Issue> getValidationErrors(final EObject model) {
		final List<Issue> validate = validate(model);
		Iterable<Issue> issues = filter(validate, new Predicate<Issue>() {
			public boolean apply(Issue input) {
				return Severity.ERROR == input.getSeverity();
			}
		});
		return issues;
	}

}
