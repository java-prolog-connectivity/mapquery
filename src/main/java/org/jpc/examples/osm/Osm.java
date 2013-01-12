package org.jpc.examples.osm;

import java.util.List;
import java.util.Map;

import org.jpc.term.TermConvertable;

public interface Osm extends TermConvertable {

	public List<Node> getNodes();
	
	public List<Node> getNodes(Map<String, String> tags);
	
	public List<Node> getNearNodes(Coordinates coordinates, double distanceKm);
	
	public List<Node> getNearNodes(Coordinates coordinates, double distanceKm, Map<String, String> tags);
	
	public List<Way> getWays();

	public List<Way> getWays(Map<String, String> tags);
	
	public List<Way> getNearWays(Coordinates coordinates, double distanceKm);
	
	public List<Way> getNearWays(Coordinates coordinates, double distanceKm, Map<String, String> tags);
	
}
