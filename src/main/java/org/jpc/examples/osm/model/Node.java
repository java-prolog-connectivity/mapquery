package org.jpc.examples.osm.model;

import java.util.Map;

public interface Node extends Taggable {

	public Long getId();
	
	public Coordinates getCoordinate();
	
	public Map<String, String> getTags();

	public long distanceKm(Coordinates other);

	public boolean near(Coordinates other, long deltaKm);
	
}
