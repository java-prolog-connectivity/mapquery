package org.jpc.examples.osm.model.imp;

import static java.util.Arrays.asList;
import static org.jpc.engine.logtalk.LogtalkObject.logtalkMessage;
import static org.jpc.engine.provider.PrologEngineProviderManager.getPrologEngine;
import static org.jpc.examples.osm.model.jpcconverters.OsmContext.getOsmContext;
import static org.jpc.util.PrologUtil.termSequence;

import java.util.List;
import java.util.Map;

import org.jpc.engine.logtalk.LogtalkObject;
import org.jpc.examples.osm.model.Coordinates;
import org.jpc.examples.osm.model.Node;
import org.jpc.examples.osm.model.OsmService;
import org.jpc.examples.osm.model.Way;
import org.jpc.query.Query;
import org.jpc.term.Compound;
import org.jpc.term.Term;
import org.jpc.term.Var;

public class OsmServiceJpc implements OsmService {
	
	@Override
	public List<Node> getNodes() {
		Term message = new Compound("node",asList(new Var("Node")));
		Query query = new LogtalkObject(this, getPrologEngine(), getOsmContext()).perform(message);
		return query.<Node>selectObject("Node").allSolutions();
	}

	@Override
	public List<Node> getNodes(Map<String, String> tags) {
		String nodeVariableName = "Node";
		Var nodeVariable = new Var(nodeVariableName);
		Query query = getPrologEngine().query(termSequence(
				logtalkMessage(getOsmContext().toTerm(this), getOsmContext().toCompound("node", asList(nodeVariable))),
				logtalkMessage(nodeVariable, getOsmContext().toCompound("has_tags", asList(tags)))), getOsmContext());
		return query.<Node>selectObject(nodeVariableName).allSolutions();
	}

	@Override
	public List<Node> getNearNodes(Coordinates coordinates, double distanceKm) {
		String nodeVariableName = "Node";
		Var nodeVariable = new Var(nodeVariableName);
		Query query = getPrologEngine().query(termSequence(
				logtalkMessage(getOsmContext().toTerm(this), getOsmContext().toCompound("node", asList(nodeVariable))),
				logtalkMessage(nodeVariable, getOsmContext().toCompound("near", asList(coordinates, distanceKm)))), getOsmContext());
		return query.<Node>selectObject(nodeVariableName).allSolutions();
	}

	@Override
	public List<Node> getNearNodes(Coordinates coordinates, double distanceKm, Map<String, String> tags) {
		String nodeVariableName = "Node";
		Var nodeVariable = new Var(nodeVariableName);
		Query query = getPrologEngine().query(termSequence(
				logtalkMessage(getOsmContext().toTerm(this), getOsmContext().toCompound("node", asList(nodeVariable))),
				logtalkMessage(nodeVariable, getOsmContext().toCompound("has_tags", asList(tags))),
				logtalkMessage(nodeVariable, getOsmContext().toCompound("near", asList(coordinates, distanceKm)))), getOsmContext());
		return query.<Node>selectObject(nodeVariableName).allSolutions();
	}

	@Override
	public List<Way> getWays() {
		Term message = new Compound("way",asList(new Var("Way")));
		Query query = new LogtalkObject(this, getPrologEngine(), getOsmContext()).perform(message);
		return query.<Way>selectObject("Way").allSolutions();
	}

	@Override
	public List<Way> getWays(Map<String, String> tags) {
		String wayVariableName = "Way";
		Var wayVariable = new Var(wayVariableName);
		Query query = getPrologEngine().query(termSequence(
				logtalkMessage(getOsmContext().toTerm(this), getOsmContext().toCompound("way", asList(wayVariable))),
				logtalkMessage(wayVariable, getOsmContext().toCompound("has_tags", asList(tags)))), getOsmContext());
		return query.<Way>selectObject(wayVariableName).allSolutions();
	}

	@Override
	public List<Way> getNearWays(Coordinates coordinates, double distanceKm) {
		String wayVariableName = "Way";
		Var wayVariable = new Var(wayVariableName);
		Query query = getPrologEngine().query(termSequence(
				logtalkMessage(getOsmContext().toTerm(this), getOsmContext().toCompound("way", asList(wayVariable))),
				logtalkMessage(wayVariable, getOsmContext().toCompound("near", asList(coordinates, distanceKm)))), getOsmContext());
		return query.<Way>selectObject(wayVariableName).allSolutions();
	}

	@Override
	public List<Way> getNearWays(Coordinates coordinates, double distanceKm, Map<String, String> tags) {
		String wayVariableName = "Way";
		Var wayVariable = new Var(wayVariableName);
		Query query = getPrologEngine().query(termSequence(
				logtalkMessage(getOsmContext().toTerm(this), getOsmContext().toCompound("way", asList(wayVariable))),
				logtalkMessage(wayVariable, getOsmContext().toCompound("has_tags", asList(tags))),
				logtalkMessage(wayVariable, getOsmContext().toCompound("near", asList(coordinates, distanceKm)))), getOsmContext());
		return query.<Way>selectObject(wayVariableName).allSolutions();
	}

}
