/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.weather.internal.metadata;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.openhab.binding.weather.internal.converter.Converter;

/**
 * Holds property mapping infos.
 * 
 * @author Gerhard Riegler
 * @since 1.6.0
 */
public class ProviderMappingInfo {
	private String source;
	private String target;
	private Converter<?> converter;

	public ProviderMappingInfo(String source, String target, Converter<?> converter) {
		this.source = source;
		this.target = target;
		this.converter = converter;
	}

	/**
	 * Returns the source property name.
	 */
	public String getSource() {
		return source;
	}

	/**
	 * Returns the target property name.
	 */
	public String getTarget() {
		return target;
	}

	/**
	 * Returns the converter to convert between source and target.
	 */
	public Converter<?> getConverter() {
		return converter;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).append("source", source)
				.append("target", target).append("converter", converter.getType()).toString();
	}

}
