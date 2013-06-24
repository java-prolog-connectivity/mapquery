package org.jpc.examples.osm.model;

import java.util.List;
import java.util.Map;

public interface OsmService extends Osm {

	public List<Node> getNodes(Map<String, String> tags);
	
	public List<Node> getNearNodes(Coordinate coordinate, double distanceKm);
	
	public List<Node> getNearNodes(Coordinate coordinate, double distanceKm, Map<String, String> tags);

	public List<Way> getWays(Map<String, String> tags);
	
	public List<Way> getNearWays(Coordinate coordinate, double distanceKm);
	
	public List<Way> getNearWays(Coordinate coordinate, double distanceKm, Map<String, String> tags);
	
}
