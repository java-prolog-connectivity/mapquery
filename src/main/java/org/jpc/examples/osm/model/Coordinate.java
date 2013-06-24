package org.jpc.examples.osm.model;


public interface Coordinate {

	public double getLon();
	
	public double getLat();

	public long distanceKm(Coordinate other);
	
	public long distanceM(Coordinate other);
	
	public boolean near(Coordinate other, long deltaKm);

}
