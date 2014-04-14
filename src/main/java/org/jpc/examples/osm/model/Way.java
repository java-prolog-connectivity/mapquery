package org.jpc.examples.osm.model;

import java.util.List;
import java.util.Map;

public interface Way extends Taggeable {

	public Long getId();
	
	public List<Node> nodes();
	
	public List<Long> getNodesIds();
	
	public Map<String, String> getTags();
	
	public long distanceKm(Coordinates other);
	
	public boolean near(Coordinates other, long deltaKm);
	
}
