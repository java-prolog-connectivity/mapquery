package org.jpc.examples.osm.imp;

import static java.util.Arrays.asList;
import static org.jpc.util.ThreadLocalLogicEngine.getLogicEngine;

import java.util.Map;

import org.jpc.converter.toterm.tolistterm.MapToTermConverter;
import org.jpc.engine.logtalk.LogtalkObject;
import org.jpc.examples.osm.Coordinates;
import org.jpc.examples.osm.Node;
import org.jpc.query.Query;
import org.jpc.term.Atom;
import org.jpc.term.Compound;
import org.jpc.term.IntegerTerm;
import org.jpc.term.Term;
import org.jpc.term.Variable;

public class NodeImp implements Node {

	public static final String NODE_FUNCTOR = "node"; //node prolog functor

	private String id;
	private Coordinates coordinates;
	private Map<String,String> tags;

	public NodeImp(String id, Coordinates coordinates, Map<String,String> tags) {
		this.id = id;
		this.coordinates = coordinates;
		this.tags = tags;
	}
	
	@Override
	public String getId() {
		return id;
	}

	@Override
	public Coordinates getCoordinates() {
		return coordinates;
	}


	@Override
	public Map<String, String> getTags() {
		return tags;
	}

	
	@Override
	public Term asTerm() {
		return new Compound(NODE_FUNCTOR, asList(new Atom(id), coordinates, new MapToTermConverter().apply(tags)));
	}

	@Override
	public long distanceKm(Coordinates other) {
		String distanceVarName = "Distance";
		Term message = new Compound("distancekm", asList(other, new Variable(distanceVarName)));
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
