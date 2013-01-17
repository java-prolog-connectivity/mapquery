package org.jpc.examples.osm.model.imp;

import static java.util.Arrays.asList;
import static org.jpc.util.concurrent.ThreadLocalPrologEngine.getPrologEngine;

import org.jpc.engine.logtalk.LogtalkObject;
import org.jpc.examples.osm.model.Coordinate;
import org.jpc.query.Query;
import org.jpc.term.Compound;
import org.jpc.term.FloatTerm;
import org.jpc.term.IntegerTerm;
import org.jpc.term.Term;
import org.jpc.term.Variable;

public class CoordinateJpc implements Coordinate {

	public static final String COORDINATE_FUNCTOR = "coordinate"; //coordinate prolog functor
	
	private double lon;
	private double lat;
	
	public CoordinateJpc(double lon, double lat) {
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
	public Term asTerm() {
		return new Compound(COORDINATE_FUNCTOR, asList(new FloatTerm(lon), new FloatTerm(lat)));
	}

	@Override
	public long distanceKm(Coordinate other) {
		String distanceVarName = "Distance";
		Term message = new Compound("distancekm", asList(other, new Variable(distanceVarName)));
		Query query = new LogtalkObject(this, getPrologEngine()).perform(message);
		return ((IntegerTerm)query.oneSolution().get(distanceVarName)).longValue();
	}

	@Override
	public long distanceM(Coordinate other) {
		String distanceVarName = "Distance";
		Term message = new Compound("distancem", asList(other, new Variable(distanceVarName)));
		Query query = new LogtalkObject(this, getPrologEngine()).perform(message);
		return ((IntegerTerm)query.oneSolution().get(distanceVarName)).longValue();
	}

	@Override
	public boolean near(Coordinate other, long deltaKm) {
		Term message = new Compound("near", asList(other, new IntegerTerm(deltaKm)));
		Query query = new LogtalkObject(this, getPrologEngine()).perform(message);
		return query.hasSolution();
	}

}
