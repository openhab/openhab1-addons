package org.openhab.binding.withings.internal.api;

import java.lang.reflect.Type;

import org.openhab.binding.withings.internal.model.Attribute;
import org.openhab.binding.withings.internal.model.Category;
import org.openhab.binding.withings.internal.model.MeasureType;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

/**
 * Holds {@link JsonDeserializer} classes for GSON deserialization of the
 * following Enum types: {@link Attribute}, {@link Category} and
 * {@link MeasureType}.
 * 
 * @author Dennis Nobel
 * @since 1.5.0
 */
public class JsonDeserializers {

	public static final class AttributeJsonDeserializer implements
			JsonDeserializer<Attribute> {

		@Override
		public Attribute deserialize(JsonElement jsonElement, Type type,
				JsonDeserializationContext jsonDeserializationContext)
				throws JsonParseException {
			return Attribute.getForType(jsonElement.getAsInt());
		}

	}

	public static final class CategoryJsonDeserializer implements
			JsonDeserializer<Category> {

		@Override
		public Category deserialize(JsonElement jsonElement, Type type,
				JsonDeserializationContext jsonDeserializationContext)
				throws JsonParseException {
			return Category.getForType(jsonElement.getAsInt());
		}

	}

	public static final class MeasureTypeJsonDeserializer implements
			JsonDeserializer<MeasureType> {
		@Override
		public MeasureType deserialize(JsonElement jsonElement, Type type,
				JsonDeserializationContext jsonDeserializationContext)
				throws JsonParseException {
			return MeasureType.getForType(jsonElement.getAsInt());
		}
	}

}
