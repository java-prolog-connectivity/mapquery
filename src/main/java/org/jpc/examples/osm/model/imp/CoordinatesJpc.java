package org.jpc.examples.osm.model.imp;

import static java.util.Arrays.asList;
import static org.jpc.examples.osm.model.jpcconverters.OsmContext.getOsmContext;
import static org.jpc.engine.provider.PrologEngineProviderManager.getPrologEngine;

import org.jpc.engine.logtalk.LogtalkObject;
import org.jpc.examples.osm.model.Coordinates;
import org.jpc.query.Query;
import org.jpc.term.Term;
import org.jpc.term.Var;

public class CoordinatesJpc implements Coordinates {
	
	private double lon;
	private double lat;
	
	public CoordinatesJpc(double lon, double lat) {
		this.lon = lon;
		this.lat = lat;
	}
	
	@Override
	public double getLon() {
		return lon;
	}

	@Override
	public double getLat() {
		return lat;
	}

	@Override
	public long distanceKm(Coordinates other) {
		String distanceVarName = "Distance";
		Term message = getOsmContext().toCompound("distancekm", asList(other, new Var(distanceVarName)));
		Query query = new LogtalkObject(this, getPrologEngine()).perform(message);
		return query.<Long>selectObject(distanceVarName).oneSolutionOrThrow();
	}

	@Override
	public long distanceM(Coordinates other) {
		String distanceVarName = "Distance";
		Term message = getOsmContext().toCompound("distancem", asList(other, new Var(distanceVarName)));
		Query query = new LogtalkObject(this, getPrologEngine()).perform(message);
		return query.<Long>selectObject(distanceVarName).oneSolutionOrThrow();
	}

	@Override
	public boolean near(Coordinates other, long deltaKm) {
		Term message = getOsmContext().toCompound("near", asList(other, deltaKm));
		Query query = new LogtalkObject(this, getPrologEngine()).perform(message);
		return query.hasSolution();
	}

}
