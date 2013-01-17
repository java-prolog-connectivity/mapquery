package org.jpc.examples.osm.model.gsonconverters;

import java.util.Map;
import java.util.Map.Entry;

import com.google.gson.JsonObject;

public class GsonUtil {

	public static JsonObject toJsonObject(Map<String, String> properties) {
		JsonObject jsonObject = new JsonObject();
		for(Entry<String,String> entry : properties.entrySet()) {
			jsonObject.addProperty(entry.getKey(), entry.getValue());
		}
		return jsonObject;
	}

}
