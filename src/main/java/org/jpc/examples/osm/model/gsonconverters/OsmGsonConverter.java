package org.jpc.examples.osm.model.gsonconverters;

import java.lang.reflect.Type;

import org.jpc.examples.osm.model.Node;
import org.jpc.examples.osm.model.Osm;
import org.jpc.examples.osm.model.Way;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class OsmGsonConverter implements JsonSerializer<Osm> {

	@Override
	public JsonElement serialize(Osm osm, Type srcType, JsonSerializationContext context) {
		JsonObject jsonOsm = new JsonObject();
		jsonOsm.addProperty(GeoJsonConstants.TYPE, GeoJsonConstants.FEATURE_COLLECTION);
		JsonArray jsonFeatures = new JsonArray();
		
		for(Node node : osm.getNodes()) {
			jsonFeatures.add(new NodeGsonConverter().serialize(node, node.getClass(), context));
		}
		
		for(Way way : osm.getWays()) {
			jsonFeatures.add(new WayGsonConverter().serialize(way, way.getClass(), context));
		}
		jsonOsm.add(GeoJsonConstants.FEATURES, jsonFeatures);
		return jsonOsm;
	}

}
