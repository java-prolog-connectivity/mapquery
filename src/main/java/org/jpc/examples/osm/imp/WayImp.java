package org.jpc.examples.osm.imp;

import static java.util.Arrays.asList;
import static org.jpc.util.ThreadLocalLogicEngine.getLogicEngine;

import java.util.List;
import java.util.Map;

import org.jpc.converter.toterm.tolistterm.IterableToTermConverter;
import org.jpc.converter.toterm.tolistterm.MapToTermConverter;
import org.jpc.engine.logtalk.LogtalkObject;
import org.jpc.examples.osm.Coordinates;
import org.jpc.examples.osm.Way;
import org.jpc.query.Query;
import org.jpc.term.Atom;
import org.jpc.term.Compound;
import org.jpc.term.IntegerTerm;
import org.jpc.term.Term;
import org.jpc.term.Variable;

public class WayImp implements Way {

	public static final String WAY_FUNCTOR = "way"; //way prolog functor
	
	private String id;
	private List<String> nodesIds;
	private Map<String,String> tags;
	
	public WayImp(String id, List<String> nodesIds, Map<String,String> tags) {
		this.id = id;
		this.nodesIds = nodesIds;
		this.tags = tags;
	}
	
	@Override
	public String getId() {
		return id;
	}

	@Override
	public List<String> getNodesIds() {
		return nodesIds;
	}

	@Override
	public Map<String, String> getTags() {
		return tags;
	}

	@Override
	public Term asTerm() {
		return new Compound(WAY_FUNCTOR, asList(new Atom(id), new IterableToTermConverter().apply(nodesIds), new MapToTermConverter().apply(tags)));
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
