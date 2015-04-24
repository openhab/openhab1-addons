package org.openhab.binding.ulux.internal;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.openhab.binding.ulux.internal.ump.UluxDatagram;
import org.openhab.binding.ulux.internal.ump.UluxVideoDatagram;

public class VideoDatagramMatcher extends BaseMatcher<UluxDatagram> {

	public VideoDatagramMatcher() {
	}

	@Override
	public boolean matches(Object item) {
		return item instanceof UluxVideoDatagram;
	}

	@Override
	public void describeTo(Description description) {
		description.appendText("UluxVideoDatagram");
	}

	@Factory
	public static Matcher<UluxDatagram> isVideoDatagram() {
		return new VideoDatagramMatcher();
	}
}
