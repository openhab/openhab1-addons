/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.ekozefir.response;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.openhab.binding.ekozefir.ResponseListenerCreators;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;

/**
 * Implementation of ResponseListenerCreators.
 * 
 * @author Michal Marasz
 * @since 1.6.0
 * 
 */
public class EkozefirResponseListenerCreators implements ResponseListenerCreators {

	private static final Logger logger = LoggerFactory.getLogger(EkozefirResponseListenerCreators.class);
	private final Map<String, ResponseListenerCreator> creators = Maps.newHashMap();

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void add(ResponseListenerCreator creator) {
		Objects.requireNonNull(creator);
		logger.debug("Adding response listener creator {}", creator);
		creators.put(creator.getId(), creator);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void remove(ResponseListenerCreator creator) {
		Objects.requireNonNull(creator);
		logger.debug("Remove response listener creator {}", creator);
		creators.remove(creator.getId());

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Optional<ResponseListenerCreator> getOfType(String name) {
		Objects.requireNonNull(name);
		if (creators.containsKey(name)) {
			return Optional.of(creators.get(name));
		}
		return Optional.empty();
	}

}
