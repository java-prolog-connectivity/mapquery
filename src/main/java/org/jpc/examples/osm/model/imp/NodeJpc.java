package org.jpc.examples.osm.model.imp;

import static java.util.Arrays.asList;
import static org.jpc.engine.provider.PrologEngineProviderManager.getPrologEngine;
import static org.jpc.examples.osm.model.jpcconverters.OsmContext.getOsmContext;

import java.util.Map;

import org.jpc.engine.logtalk.LogtalkObject;
import org.jpc.examples.osm.model.Coordinate;
import org.jpc.examples.osm.model.Node;
import org.jpc.query.Query;
import org.jpc.term.Term;
import org.jpc.term.Variable;

public class NodeJpc implements Node {

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
	public long distanceKm(Coordinate other) {
		String distanceVarName = "Distance";
		Term message = getOsmContext().compound("distancekm", asList(other, new Variable(distanceVarName)));
		Query query = new LogtalkObject(this, getPrologEngine()).perform(message);
		return query.<Long>selectObject(distanceVarName).oneSolutionOrThrow();
	}

	@Override
	public boolean near(Coordinate other, long deltaKm) {
		Term message = getOsmContext().compound("near", asList(other, deltaKm));
		Query query = new LogtalkObject(this, getPrologEngine()).perform(message);
		return query.hasSolution();
	}
	
}
