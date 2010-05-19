package org.openhab.ui.webapp.internal.http;

import org.apache.wicket.Page;
import org.apache.wicket.protocol.http.WebApplication;

public class MainApplication extends WebApplication {

	@Override
	public Class<? extends Page> getHomePage() {
		return HelloWorld.class;
	}

}
