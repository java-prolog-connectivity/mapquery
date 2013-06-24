package org.jpc.examples.osm.model.imp;

import static java.util.Arrays.asList;
import static org.jpc.engine.logtalk.LogtalkObject.logtalkMessage;
import static org.jpc.engine.provider.PrologEngineProviderManager.getPrologEngine;
import static org.jpc.examples.osm.model.jpcconverters.OsmContext.getOsmContext;
import static org.jpc.util.PrologUtil.termSequence;

import java.util.List;
import java.util.Map;

import org.jpc.engine.logtalk.LogtalkObject;
import org.jpc.examples.osm.model.Coordinate;
import org.jpc.examples.osm.model.Node;
import org.jpc.examples.osm.model.OsmService;
import org.jpc.examples.osm.model.Way;
import org.jpc.query.Query;
import org.jpc.term.Compound;
import org.jpc.term.Term;
import org.jpc.term.Variable;

public class OsmServiceJpc implements OsmService {
	
	@Override
	public List<Node> getNodes() {
		Term message = new Compound("node",asList(new Variable("Node")));
		Query query = new LogtalkObject(this, getPrologEngine(), getOsmContext()).perform(message);
		return query.<Node>selectObject("Node").allSolutions();
	}

	@Override
	public List<Node> getNodes(Map<String, String> tags) {
		String nodeVariableName = "Node";
		Variable nodeVariable = new Variable(nodeVariableName);
		Query query = getPrologEngine().query(termSequence(
				logtalkMessage(getOsmContext().toTerm(this), getOsmContext().compound("node", asList(nodeVariable))),
				logtalkMessage(nodeVariable, getOsmContext().compound("has_tags", asList(tags)))), getOsmContext());
		return query.<Node>selectObject(nodeVariableName).allSolutions();
	}

	@Override
	public List<Node> getNearNodes(Coordinate coordinate, double distanceKm) {
		String nodeVariableName = "Node";
		Variable nodeVariable = new Variable(nodeVariableName);
		Query query = getPrologEngine().query(termSequence(
				logtalkMessage(getOsmContext().toTerm(this), getOsmContext().compound("node", asList(nodeVariable))),
				logtalkMessage(nodeVariable, getOsmContext().compound("near", asList(coordinate, distanceKm)))), getOsmContext());
		return query.<Node>selectObject(nodeVariableName).allSolutions();
	}

	@Override
	public List<Node> getNearNodes(Coordinate coordinate, double distanceKm, Map<String, String> tags) {
		String nodeVariableName = "Node";
		Variable nodeVariable = new Variable(nodeVariableName);
		Query query = getPrologEngine().query(termSequence(
				logtalkMessage(getOsmContext().toTerm(this), getOsmContext().compound("node", asList(nodeVariable))),
				logtalkMessage(nodeVariable, getOsmContext().compound("has_tags", asList(tags))),
				logtalkMessage(nodeVariable, getOsmContext().compound("near", asList(coordinate, distanceKm)))), getOsmContext());
		return query.<Node>selectObject(nodeVariableName).allSolutions();
	}

	@Override
	public List<Way> getWays() {
		Term message = new Compound("way",asList(new Variable("Way")));
		Query query = new LogtalkObject(this, getPrologEngine(), getOsmContext()).perform(message);
		return query.<Way>selectObject("Way").allSolutions();
	}

	@Override
	public List<Way> getWays(Map<String, String> tags) {
		String wayVariableName = "Way";
		Variable wayVariable = new Variable(wayVariableName);
		Query query = getPrologEngine().query(termSequence(
				logtalkMessage(getOsmContext().toTerm(this), getOsmContext().compound("way", asList(wayVariable))),
				logtalkMessage(wayVariable, getOsmContext().compound("has_tags", asList(tags)))), getOsmContext());
		return query.<Way>selectObject(wayVariableName).allSolutions();
	}

	@Override
	public List<Way> getNearWays(Coordinate coordinate, double distanceKm) {
		String wayVariableName = "Way";
		Variable wayVariable = new Variable(wayVariableName);
		Query query = getPrologEngine().query(termSequence(
				logtalkMessage(getOsmContext().toTerm(this), getOsmContext().compound("way", asList(wayVariable))),
				logtalkMessage(wayVariable, getOsmContext().compound("near", asList(coordinate, distanceKm)))), getOsmContext());
		return query.<Way>selectObject(wayVariableName).allSolutions();
	}

	@Override
	public List<Way> getNearWays(Coordinate coordinate, double distanceKm, Map<String, String> tags) {
		String wayVariableName = "Way";
		Variable wayVariable = new Variable(wayVariableName);
		Query query = getPrologEngine().query(termSequence(
				logtalkMessage(getOsmContext().toTerm(this), getOsmContext().compound("way", asList(wayVariable))),
				logtalkMessage(wayVariable, getOsmContext().compound("has_tags", asList(tags))),
				logtalkMessage(wayVariable, getOsmContext().compound("near", asList(coordinate, distanceKm)))), getOsmContext());
		return query.<Way>selectObject(wayVariableName).allSolutions();
	}

}
