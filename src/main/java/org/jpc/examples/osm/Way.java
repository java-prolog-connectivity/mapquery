package org.jpc.examples.osm;

import java.util.List;
import java.util.Map;

import org.jpc.term.TermConvertable;

public interface Way extends Taggeable, TermConvertable {

	public String getId();
	
	public List<String> getNodesIds();
	
	public Map<String, String> getTags();
	
	public long distanceKm(Coordinates other);
	
	public boolean near(Coordinates other, long deltaKm);
	
}
