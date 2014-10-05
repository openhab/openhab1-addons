/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.vera.internal.converter;

import java.util.HashMap;
import java.util.Map;

import org.openhab.binding.vera.internal.converter.command.OnOffTypeBooleanConverter;
import org.openhab.binding.vera.internal.converter.command.OnOffTypeUnsignedIntegerOneByteConverter;
import org.openhab.binding.vera.internal.converter.command.PercentTypeUnsignedIntegerOneByteConverter;
import org.openhab.binding.vera.internal.converter.state.BooleanOnOffTypeConverter;
import org.openhab.binding.vera.internal.converter.state.BooleanOpenClosedTypeConverter;
import org.openhab.binding.vera.internal.converter.state.IntegerDateTimeTypeConverter;
import org.openhab.binding.vera.internal.converter.state.IntegerDecimalTypeConverter;
import org.openhab.binding.vera.internal.converter.state.IntegerPercentTypeConverter;
import org.openhab.binding.vera.internal.converter.state.StringStringTypeConverter;
import org.openhab.binding.vera.internal.converter.state.UnsignedIntegerOneBytePercentTypeConverter;
import org.openhab.core.library.types.DateTimeType;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.OpenClosedType;
import org.openhab.core.library.types.PercentType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;

/**
 * Base class for all converters.
 *
 * @author Matthew Bowman
 * @since 1.6.0
 */
@SuppressWarnings("rawtypes")
public abstract class VeraConverter {

	/**
	 * Lookup table for {@link VeraCommandConverter}s.
	 */
	private static final Map<Class<? extends Command>, VeraCommandConverter[]> commandConverters = 
			new HashMap<Class<? extends Command>, VeraCommandConverter[]>();
	
	/**
	 * Lookup table for {@link VeraStateConverter}s.
	 */
	private static final Map<Class<? extends State>, VeraStateConverter[]> stateConverters = 
			new HashMap<Class<? extends State>, VeraStateConverter[]>();
	
	static {

		// init the command converters
		commandConverters.put(OnOffType.class,
				new VeraCommandConverter[] { new OnOffTypeBooleanConverter(), 
											 new OnOffTypeUnsignedIntegerOneByteConverter() });
		commandConverters.put(PercentType.class, 
				new VeraCommandConverter[] { new PercentTypeUnsignedIntegerOneByteConverter() });

		// init the state converters
		stateConverters.put(DateTimeType.class,
				new VeraStateConverter[] { new IntegerDateTimeTypeConverter() });
		stateConverters.put(DecimalType.class,
				new VeraStateConverter[] { new IntegerDecimalTypeConverter() });
		stateConverters.put(OnOffType.class,
				new VeraStateConverter[] { new BooleanOnOffTypeConverter() });
		stateConverters.put(OpenClosedType.class,
				new VeraStateConverter[] { new BooleanOpenClosedTypeConverter() });
		stateConverters.put(PercentType.class, 
				new VeraStateConverter[] { new IntegerPercentTypeConverter(),
			                               new UnsignedIntegerOneBytePercentTypeConverter() });
		stateConverters.put(StringType.class,
				new VeraStateConverter[] { new StringStringTypeConverter() });

	}
	
	/**
	 * Lookup the {@link VeraStateConverter} for the given <code>(stateType,valueType)</code> pair.
	 * 
	 * @param stateType
	 * @param valueType
	 * @return the {@link VeraStateConverter}; or <code>null</code> if not found
	 */
	@SuppressWarnings("unchecked")
	public static <V, S extends State> VeraStateConverter<V,S> getStateConverter(Class<S> stateType, Class<V> valueType) {
		VeraStateConverter<?, S>[] converters = stateConverters.get(stateType);
		if (converters != null) {
			for (VeraStateConverter<?, S> converter: converters) {
				if (converter.canConvertValue(valueType)) {
					return (VeraStateConverter<V, S>) converter;
				}
			}
		}
		return null;
	}
	
	/**
	 * Lookup the {@link VeraCommandConverter} for the given <code>(commandType,valueType)</code> pair.
	 * 
	 * @param stateType
	 * @param valueType
	 * @return the {@link VeraCommandConverter}; or <code>null</code> if not found
	 */
	@SuppressWarnings("unchecked")
	public static <C extends Command,V> VeraCommandConverter<C,V> getCommandConverter(Class<C> commandType, Class<V> valueType) {
		VeraCommandConverter<C,?>[] converters = commandConverters.get(commandType);
		if (converters != null) {
			for (VeraCommandConverter<C,?> converter: converters) {
				if (converter.canConvertValue(valueType)) {
					return (VeraCommandConverter<C, V>) converter;
				}
			}
		}
		return null;
	}

	/**
	 * Tests if this converter can covert the given <code>valueType</code>.
	 * 
	 * @param valueType the <code>valueType</code> to test
	 * @return if this converter can covert the given <code>valueType</code>
	 */
	public boolean canConvertValue(Class<?> valueType) {
		return getValueType().isAssignableFrom(valueType);
	}
	
	/**
	 * Gets the value type supported by this converter.
	 * @return the value type supported by this converter
	 */
	protected abstract Class<?> getValueType();
	
}
