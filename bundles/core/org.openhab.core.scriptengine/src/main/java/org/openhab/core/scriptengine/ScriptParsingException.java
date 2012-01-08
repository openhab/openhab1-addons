/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010-2012, openHAB.org <admin@openhab.org>
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
package org.openhab.core.scriptengine;

import java.util.List;

import org.eclipse.emf.ecore.resource.Resource.Diagnostic;
import org.eclipse.xtext.diagnostics.AbstractDiagnostic;
import org.eclipse.xtext.diagnostics.ExceptionDiagnostic;
import org.eclipse.xtext.validation.Issue;

/**
 * Exception that is thrown on errors during script execution.
 * 
 * @author Kai Kreuzer
 * @since 0.9.0
 *
 */
public class ScriptParsingException extends ScriptException {

	private static final long serialVersionUID = -3784970293118871807L;

	public ScriptParsingException(String message, String scriptAsString) {
		super(message, scriptAsString);
	}

	public ScriptParsingException(String message, String scriptAsString, Throwable t) {
		super(message, scriptAsString, t);
	}

	public ScriptParsingException addDiagnosticErrors(List<Diagnostic> errors) {
		for (Diagnostic emfDiagnosticError : errors) {
			if (emfDiagnosticError instanceof AbstractDiagnostic) {
				AbstractDiagnostic e = (AbstractDiagnostic) emfDiagnosticError;
				this.getErrors().add(new ScriptError(e.getMessage(), e.getLine(), e.getOffset(), e.getLength()));				
			} else if (emfDiagnosticError instanceof ExceptionDiagnostic) {
				ExceptionDiagnostic e = (ExceptionDiagnostic) emfDiagnosticError;
				this.getErrors().add(new ScriptError(e.getMessage(), e.getLine(), e.getOffset(), e.getLength()));				
			} else {
				this.getErrors().add(new ScriptError(emfDiagnosticError.getMessage(), -1, -1, -1));				
			}
		}
		return this;
	}

	public ScriptParsingException addValidationIssues(Iterable<Issue> validationErrors) {
		for (Issue validationError : validationErrors) {
			this.getErrors().add(new ScriptError(validationError.getMessage(), validationError.getLineNumber(), validationError.getOffset(), validationError.getLength()));				
		}
		return this;
	}

}
