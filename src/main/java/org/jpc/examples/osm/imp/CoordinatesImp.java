package org.jpc.examples.osm.imp;

import static java.util.Arrays.asList;
import static org.jpc.util.ThreadLocalLogicEngine.getLogicEngine;

import org.jpc.engine.logtalk.LogtalkObject;
import org.jpc.examples.osm.Coordinates;
import org.jpc.query.Query;
import org.jpc.term.Compound;
import org.jpc.term.FloatTerm;
import org.jpc.term.IntegerTerm;
import org.jpc.term.Term;
import org.jpc.term.Variable;

public class CoordinatesImp implements Coordinates {

	public static final String COORDINATES_FUNCTOR = "coordinates"; //coordinates prolog functor
	
	private double lon;
	private double lat;
	
	public CoordinatesImp(double lon, double lat) {
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
		return new Compound(COORDINATES_FUNCTOR, asList(new FloatTerm(lon), new FloatTerm(lat)));
	}

	@Override
	public long distanceKm(Coordinates other) {
		String distanceVarName = "Distance";
		Term message = new Compound("distancekm", asList(other, new Variable(distanceVarName)));
		Query query = new LogtalkObject(this, getLogicEngine()).perform(message);
		return ((IntegerTerm)query.oneSolution().get(distanceVarName)).longValue();
	}

	@Override
	public long distanceM(Coordinates other) {
		String distanceVarName = "Distance";
		Term message = new Compound("distancem", asList(other, new Variable(distanceVarName)));
		Query query = new LogtalkObject(this, getLogicEngine()).perform(message);
		return ((IntegerTerm)query.oneSolution().get(distanceVarName)).longValue();
	}

	@Override
	public boolean near(Coordinates other, long deltaKm) {
		Term message = new Compound("near", asList(other, new IntegerTerm(deltaKm)));
		Query query = new LogtalkObject(this, getLogicEngine()).perform(message);
		return query.hasSolution();
	}

}
