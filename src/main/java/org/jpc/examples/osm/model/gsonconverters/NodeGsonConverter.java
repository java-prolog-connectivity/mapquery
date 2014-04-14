package org.jpc.examples.osm.model.gsonconverters;

import java.lang.reflect.Type;

import org.jpc.examples.osm.model.Node;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class NodeGsonConverter implements JsonSerializer<Node> {

	@Override
	public JsonElement serialize(Node node, Type srcType, JsonSerializationContext context) {
		JsonObject jsonNode = new JsonObject();
		jsonNode.addProperty(GeoJsonConstants.TYPE, GeoJsonConstants.FEATURE);
		jsonNode.addProperty(GeoJsonConstants.ID, node.getId());
		jsonNode.add(GeoJsonConstants.PROPERTIES, GsonUtil.toJsonObject(node.getTags()));
		
		JsonObject jsonGeometry = new JsonObject();
		jsonGeometry.addProperty(GeoJsonConstants.TYPE, GeoJsonConstants.POINT);
		jsonGeometry.add(GeoJsonConstants.COORDINATES, new CoordinatesGsonConverter().serialize(node.getCoordinate(), node.getCoordinate().getClass(), context));
		jsonNode.add(GeoJsonConstants.GEOMETRY, jsonGeometry);
		return jsonNode;
	}

}
