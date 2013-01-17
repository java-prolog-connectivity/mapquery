package org.jpc.examples.osm.model.gsonconverters;

import java.lang.reflect.Type;

import org.jpc.examples.osm.model.Coordinate;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class CoordinateGsonConverter implements JsonSerializer<Coordinate> {

	@Override
	public JsonElement serialize(Coordinate coordinate, Type srcType, JsonSerializationContext context) {
		JsonArray jsonArray = new JsonArray();
		jsonArray.add(new JsonPrimitive(coordinate.getLon()));
		jsonArray.add(new JsonPrimitive(coordinate.getLat()));
		return jsonArray;
	}

}
