package org.openhab.io.multimedia.actions;

import java.lang.reflect.Method;

import org.openhab.core.scriptengine.action.ActionService;

public class AudioActionService implements ActionService {

	@Override
	public String getActionClassName() {
		return Audio.class.getCanonicalName();
	}

	@Override
	public Class<?> getActionClass() {
		return Audio.class;
	}

	@Override
	public String getActionHelpText(Method m) {
		return null;
	}

}
 