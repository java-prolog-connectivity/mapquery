package org.jpc.examples.osm.model;

import java.util.List;

public interface Osm {

	public List<Node> getNodes();
	
	public List<Way> getWays();
}
