package org.jpc.examples.osm.model;

import java.util.List;
import java.util.Map;

public interface OsmService extends Osm {

	public List<Node> getNodes(Map<String, String> tags);
	
	public List<Node> getNearNodes(Coordinates coordinates, double distanceKm);
	
	public List<Node> getNearNodes(Coordinates coordinates, double distanceKm, Map<String, String> tags);

	public List<Way> getWays(Map<String, String> tags);
	
	public List<Way> getNearWays(Coordinates coordinates, double distanceKm);
	
	public List<Way> getNearWays(Coordinates coordinates, double distanceKm, Map<String, String> tags);
	
}
