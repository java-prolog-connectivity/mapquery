package org.jpc.examples.osm.model;

import java.util.Map;

import org.jpc.term.TermConvertable;

public interface Node extends Taggeable, TermConvertable {

	public Long getId();
	
	public Coordinate getCoordinate();
	
	public Map<String, String> getTags();

	public long distanceKm(Coordinate other);

	public boolean near(Coordinate other, long deltaKm);
	
}
