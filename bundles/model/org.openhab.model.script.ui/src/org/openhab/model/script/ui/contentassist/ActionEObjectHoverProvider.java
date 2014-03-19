/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.model.script.ui.contentassist;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.common.types.JvmGenericType;
import org.eclipse.xtext.ui.editor.hover.html.DefaultEObjectHoverProvider;
import org.eclipse.xtext.xbase.XFeatureCall;
import org.openhab.core.scriptengine.action.ActionDoc;
import org.openhab.core.scriptengine.action.ActionService;
import org.openhab.core.scriptengine.action.ParamDoc;
import org.openhab.model.script.ui.internal.ScriptUIActivator;

/**
 * Provides a hover with a kind of JavaDoc for an action method.
 * 
 * @author Kai Kreuzer
 * @since 1.3.0
 *
 */
@SuppressWarnings("restriction")
public class ActionEObjectHoverProvider extends DefaultEObjectHoverProvider {

	@Override
    protected String getFirstLine(EObject o) {
        if (o instanceof XFeatureCall) {
            Method m = getActionMethod((XFeatureCall) o);
            if(m!=null) {
            	return getActionSignature(m);
            }
        }
        return super.getFirstLine(o);
    }
	
	@Override
	protected boolean hasHover(EObject o) {
		if(o instanceof XFeatureCall) {
			return getActionMethod((XFeatureCall) o) != null;
		} else {
			return super.hasHover(o);
		}
	}
	
	protected String getDocumentation(EObject o) {
        if (o instanceof XFeatureCall) {
            Method m = getActionMethod((XFeatureCall) o);
            if(m!=null) {
            	return getDocumentation(m);
            }
        }
       	return super.getDocumentation(o);
	}

	private String getDocumentation(Method m) {
		ActionDoc actionDoc = m.getAnnotation(ActionDoc.class);
		if(actionDoc!=null) {
			StringBuilder sb = new StringBuilder();
			sb.append("<p>&nbsp;&nbsp;&nbsp;" + actionDoc.text() + "</p>");
			if(m.getParameterTypes().length > 0) {
				sb.append("<p><b>Parameters:</b><br>");
				List<String> params = new ArrayList<String>();
				for(int i = 0; i< m.getParameterTypes().length; i++) {
					ParamDoc paramDoc = getParamDoc(m, i);
					String paramName = paramDoc!=null ? paramDoc.name() : "p" + i;
					params.add("&nbsp;&nbsp;&nbsp;" + paramName + (StringUtils.isEmpty(paramDoc.text()) ? "" : ": " + paramDoc.text()));
				}
				sb.append(StringUtils.join(params, "<br/>"));
				sb.append("</p>");
			}
			if(!StringUtils.isEmpty(actionDoc.returns())) {
				sb.append("<p><b>Returns:</b><br>");
				sb.append("&nbsp;&nbsp;&nbsp;" + actionDoc.returns() + "<p>");
			}
			return sb.toString();
		}
		return null;
	}

	private Method getActionMethod(XFeatureCall call) {
		if(call.getFeature().eContainer() instanceof JvmGenericType) {
			JvmGenericType type = (JvmGenericType) call.getFeature().eContainer();
			Object[] services = ScriptUIActivator.actionServiceTracker.getServices();
			if(services!=null) {
				for(Object service : services) {
					ActionService actionService = (ActionService) service;
					if(actionService.getActionClassName().equals(type.getIdentifier())) {
						for(Method m : actionService.getActionClass().getMethods()) {
							if(m.toString().contains((call.getFeature().getIdentifier()))) {
								return m;
							}
						}
					}
				}
			}
		}
		return null;
	}
	
	private String getActionSignature(Method m) {
		StringBuilder sb = new StringBuilder();
		sb.append("<b>");
		sb.append(m.getName());
		sb.append("(");
		List<String> params = new ArrayList<String>();
		int i = 0;
		for(Class<?> paramType : m.getParameterTypes()) {
			ParamDoc paramDoc = getParamDoc(m, i);
			String paramName = paramDoc!=null ? paramDoc.name() : "p" + i;
			params.add(paramType.getSimpleName() + " " + paramName);
			i++;
		}
		sb.append(StringUtils.join(params, ", "));
		sb.append(") : ");
		sb.append(m.getReturnType().getName());
		sb.append("</b>");
		return sb.toString();
	}

	private ParamDoc getParamDoc(Method m, int i) {
		Annotation[][] paramAnnotations = m.getParameterAnnotations();
		Annotation[] annotations = paramAnnotations[i];
		for(Annotation a : annotations) {
			if(a instanceof ParamDoc) {
				return (ParamDoc) a;
			}
		}
		return null;
	}

}
