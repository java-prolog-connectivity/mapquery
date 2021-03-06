package org.jpc.examples.osm.model.imp;

import static java.util.Arrays.asList;
import static org.jpc.examples.osm.model.jpcconverters.OsmContext.getOsmContext;
import static org.jpc.engine.provider.PrologEngineProviderManager.getPrologEngine;

import java.util.List;
import java.util.Map;

import org.jpc.engine.logtalk.LogtalkObject;
import org.jpc.examples.osm.model.Coordinates;
import org.jpc.examples.osm.model.Node;
import org.jpc.examples.osm.model.Way;
import org.jpc.query.Query;
import org.jpc.term.Compound;
import org.jpc.term.Term;
import org.jpc.term.Var;

public class WayJpc implements Way {
	
	private Long id;
	private List<Long> nodesIds;
	private Map<String,String> tags;
	
	public WayJpc(Long id, List<Long> nodesIds, Map<String,String> tags) {
		this.id = id;
		this.nodesIds = nodesIds;
		this.tags = tags;
	}
	
	@Override
	public Long getId() {
		return id;
	}

	@Override
	public List<Long> getNodesIds() {
		return nodesIds;
	}

	/**
	 * Use with care: it can be an expensive call if there are many nodes in the way object and if the osm source is big
	 * @return
	 */
	public List<Node> nodes() {
		String nodeVarName = "Node";
		Term message = new Compound("node", asList(new Var(nodeVarName)));
		Query query = new LogtalkObject(this, getPrologEngine(), getOsmContext()).perform(message);
		return query.<Node>selectObject(nodeVarName).allSolutions();
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
