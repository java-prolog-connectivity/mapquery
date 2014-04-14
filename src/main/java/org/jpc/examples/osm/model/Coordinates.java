package org.jpc.examples.osm.model;


public interface Coordinates {

	public double getLon();
	
	public double getLat();

	public long distanceKm(Coordinates other);
	
	public long distanceM(Coordinates other);
	
	public boolean near(Coordinates other, long deltaKm);

}
