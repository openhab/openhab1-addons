package org.openhab.binding.netatmo.internal.messages;

import static org.apache.commons.lang.builder.ToStringStyle.SHORT_PREFIX_STYLE;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Base class for all message parts, i.e. objects within a response.
 * 
 * @author Andreas Brenk
 * @since 1.4.0
 */
public class AbstractMessagePart {

	@Override
	public String toString() {
		final ToStringBuilder builder = createToStringBuilder();

		return builder.toString();
	}

	protected final ToStringBuilder createToStringBuilder() {
		return new ToStringBuilder(this, SHORT_PREFIX_STYLE);
	}

}
