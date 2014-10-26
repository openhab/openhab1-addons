/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.ecobee.internal.messages;

/**
 * The resume program function removes the currently running event 
 * providing the event is not a mandatory demand response event. 
 * The top active event is removed from the stack and the thermostat 
 * resumes its program, or enters the next event in the stack if one 
 * exists. This function may need to be called multiple times in order 
 * to resume all events. Sending 3 resume functions in a row will 
 * resume the thermostat to its program in all instances.
 * 
 * <p>
 * Note that vacation events cannot be resumed, you must delete the 
 * vacation event using the {@link DeleteVacationFunction}.
 * 
 * @see <a href="https://www.ecobee.com/home/developer/api/documentation/v1/functions/ResumeProgram.shtml">ResumeProgram</a>
 * @author John Cocula
 */
public final class ResumeProgramFunction extends AbstractFunction {

	public ResumeProgramFunction() {
		super("resumeProgram");
	}
}
