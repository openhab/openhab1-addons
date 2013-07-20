package org.openhab.io.multimedia.actions;

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

}
 