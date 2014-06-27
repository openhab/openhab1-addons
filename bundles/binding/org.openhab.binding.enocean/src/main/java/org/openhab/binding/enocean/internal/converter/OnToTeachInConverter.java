package org.openhab.binding.enocean.internal.converter;

import org.enocean.java.common.values.TeachIn;
import org.openhab.core.library.types.OnOffType;

public class OnToTeachInConverter extends
		StateConverter<TeachIn, OnOffType> {
			
			@Override
			protected OnOffType convertToImpl(TeachIn source) {
				return OnOffType.ON;
			}
			
			@Override
			protected TeachIn convertFromImpl(OnOffType source) {
				return TeachIn.LEARN_SENDER;
			}
}
