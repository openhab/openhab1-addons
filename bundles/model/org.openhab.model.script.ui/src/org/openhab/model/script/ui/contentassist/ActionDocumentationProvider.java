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
