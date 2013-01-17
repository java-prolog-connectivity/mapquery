package org.jpc.examples.osm.model;

import org.jpc.term.TermConvertable;

public interface Coordinate extends TermConvertable {

	public double getLon();
	
	public double getLat();

	public long distanceKm(Coordinate other);
	
	public long distanceM(Coordinate other);
	
	public boolean near(Coordinate other, long deltaKm);

}
