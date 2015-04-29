package org.openhab.binding.ulux.internal;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.openhab.binding.ulux.internal.ump.UluxAudioDatagram;
import org.openhab.binding.ulux.internal.ump.UluxDatagram;

public class AudioDatagramMatcher extends BaseMatcher<UluxDatagram> {

	public AudioDatagramMatcher() {
	}

	@Override
	public boolean matches(Object item) {
		return item instanceof UluxAudioDatagram;
	}

	@Override
	public void describeTo(Description description) {
		description.appendText(UluxAudioDatagram.class.getName());
	}

	@Factory
	public static Matcher<UluxDatagram> isAudioDatagram() {
		return new AudioDatagramMatcher();
	}
}
