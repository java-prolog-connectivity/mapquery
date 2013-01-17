package org.jpc.examples.osm.model.imp;

import static java.util.Arrays.asList;
import static org.jpc.util.concurrent.ThreadLocalPrologEngine.getPrologEngine;

import java.util.Map;

import org.jpc.converter.toterm.tolistterm.MapToTermConverter;
import org.jpc.engine.logtalk.LogtalkObject;
import org.jpc.examples.osm.model.Coordinate;
import org.jpc.examples.osm.model.Node;
import org.jpc.query.Query;
import org.jpc.term.Compound;
import org.jpc.term.IntegerTerm;
import org.jpc.term.Term;
import org.jpc.term.Variable;

public class NodeJpc implements Node {

	public static final String NODE_FUNCTOR = "node"; //node prolog functor

	private Long id;
	private Coordinate coordinate;
	private Map<String,String> tags;

	public NodeJpc(Long id, Coordinate coordinate, Map<String,String> tags) {
		this.id = id;
		this.coordinate = coordinate;
		this.tags = tags;
	}
	
	@Override
	public Long getId() {
		return id;
	}

	@Override
	public Coordinate getCoordinate() {
		return coordinate;
	}


	@Override
	public Map<String, String> getTags() {
		return tags;
	}

	
	@Override
	public Term asTerm() {
		return new Compound(NODE_FUNCTOR, asList(new IntegerTerm(id), coordinate, new MapToTermConverter().apply(tags)));
	}

	@Override
	public long distanceKm(Coordinate other) {
		String distanceVarName = "Distance";
		Term message = new Compound("distancekm", asList(other, new Variable(distanceVarName)));
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
