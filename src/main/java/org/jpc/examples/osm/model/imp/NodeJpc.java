package org.jpc.examples.osm.model.imp;

import static java.util.Arrays.asList;
import static org.jpc.engine.provider.PrologEngineProviderManager.getPrologEngine;
import static org.jpc.examples.osm.model.jpcconverters.OsmContext.getOsmContext;

import java.util.Map;

import org.jpc.engine.logtalk.LogtalkObject;
import org.jpc.examples.osm.model.Coordinates;
import org.jpc.examples.osm.model.Node;
import org.jpc.query.Query;
import org.jpc.term.Term;
import org.jpc.term.Var;

public class NodeJpc implements Node {

	private Long id;
	private Coordinates coordinates;
	private Map<String,String> tags;

	public NodeJpc(Long id, Coordinates coordinates, Map<String,String> tags) {
		this.id = id;
		this.coordinates = coordinates;
		this.tags = tags;
	}
	
	@Override
	public Long getId() {
		return id;
	}

	@Override
	public Coordinates getCoordinate() {
		return coordinates;
	}

	@Override
	public Map<String, String> getTags() {
		return tags;
	}

	@Override
	public long distanceKm(Coordinates other) {
		String distanceVarName = "Distance";
		Term message = getOsmContext().toCompound("distancekm", asList(other, new Var(distanceVarName)));
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
