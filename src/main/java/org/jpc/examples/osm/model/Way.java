package org.jpc.examples.osm.model;

import java.util.List;
import java.util.Map;

import org.jpc.term.TermConvertable;

public interface Way extends Taggeable, TermConvertable {

	public Long getId();
	
	public List<Node> nodes();
	
	public List<String> getNodesIds();
	
	public Map<String, String> getTags();
	
	public long distanceKm(Coordinate other);
	
	public boolean near(Coordinate other, long deltaKm);
	
}
