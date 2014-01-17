/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.model.script.ui.contentassist;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.xbase.ui.hover.XbaseHoverDocumentationProvider;

public class ActionDocumentationProvider extends XbaseHoverDocumentationProvider {

	@Override
	public String getDocumentation(EObject o) {
		String doc = super.getDocumentation(o);
		return doc;
	}

}
