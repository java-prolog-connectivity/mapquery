package org.jpc.examples.osm;

import java.util.Map;

import org.jpc.term.TermConvertable;

public interface Node extends Taggeable, TermConvertable {

	public String getId();
	
	public Coordinates getCoordinates();
	
	public Map<String, String> getTags();

	public long distanceKm(Coordinates other);

	public boolean near(Coordinates other, long deltaKm);
	
}
