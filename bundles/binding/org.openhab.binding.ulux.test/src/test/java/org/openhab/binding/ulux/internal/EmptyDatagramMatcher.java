package org.openhab.binding.ulux.internal;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.openhab.binding.ulux.internal.ump.UluxMessageDatagram;

public class EmptyDatagramMatcher extends BaseMatcher<UluxMessageDatagram> {

	public EmptyDatagramMatcher() {
	}

	@Override
	public boolean matches(Object item) {
		return !((UluxMessageDatagram) item).hasMessages();
	}

	@Override
	public void describeTo(Description description) {
		description.appendText("UluxDatagram without messages");
	}

	@Factory
	public static Matcher<UluxMessageDatagram> isEmptyDatagram() {
		return new EmptyDatagramMatcher();
	}
}
