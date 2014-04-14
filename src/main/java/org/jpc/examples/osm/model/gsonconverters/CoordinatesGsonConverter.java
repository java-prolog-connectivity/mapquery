package org.jpc.examples.osm.model.gsonconverters;

import java.lang.reflect.Type;

import org.jpc.examples.osm.model.Coordinates;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class CoordinatesGsonConverter implements JsonSerializer<Coordinates> {

	@Override
	public JsonElement serialize(Coordinates coordinates, Type srcType, JsonSerializationContext context) {
		JsonArray jsonArray = new JsonArray();
		jsonArray.add(new JsonPrimitive(coordinates.getLon()));
		jsonArray.add(new JsonPrimitive(coordinates.getLat()));
		return jsonArray;
	}

}
