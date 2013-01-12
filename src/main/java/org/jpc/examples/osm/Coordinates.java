package org.jpc.examples.osm;

import org.jpc.term.TermConvertable;

public interface Coordinates extends TermConvertable {

	public double getLon();
	
	public double getLat();

	public long distanceKm(Coordinates other);
	
	public long distanceM(Coordinates other);
	
	public boolean near(Coordinates other, long deltaKm);

}
