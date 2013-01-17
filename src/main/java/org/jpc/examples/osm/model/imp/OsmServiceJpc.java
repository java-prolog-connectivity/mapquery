package org.jpc.examples.osm.model.imp;

import static java.util.Arrays.asList;
import static org.jpc.engine.logtalk.LogtalkObject.logtalkMessage;
import static org.jpc.util.concurrent.ThreadLocalPrologEngine.getPrologEngine;

import java.util.List;
import java.util.Map;

import org.jpc.converter.toterm.tolistterm.MapToTermConverter;
import org.jpc.engine.logtalk.LogtalkObject;
import org.jpc.examples.osm.model.Coordinate;
import org.jpc.examples.osm.model.Node;
import org.jpc.examples.osm.model.OsmService;
import org.jpc.examples.osm.model.Way;
import org.jpc.examples.osm.model.jpcconverters.TermToNodeConverter;
import org.jpc.examples.osm.model.jpcconverters.TermToWayConverter;
import org.jpc.query.Query;
import org.jpc.term.Atom;
import org.jpc.term.Compound;
import org.jpc.term.FloatTerm;
import org.jpc.term.Term;
import org.jpc.term.Variable;

public class OsmServiceJpc implements OsmService {
	
	public static final String OSM_NAME = "osm"; //osm prolog functor
	
	
	@Override
	public List<Node> getNodes() {
		Term message = new Compound("node",asList(new Variable("Node")));
		Query query = new LogtalkObject(this, getPrologEngine()).perform(message);
		return query.select("Node").adapt(new TermToNodeConverter()).allSolutions();
	}

	@Override
	public List<Node> getNodes(Map<String, String> tags) {
		String nodeVariableName = "Node";
		Variable nodeVariable = new Variable(nodeVariableName);
		Query query = getPrologEngine().query(
				logtalkMessage(this, new Compound("node", asList(nodeVariable))),
				logtalkMessage(nodeVariable, new Compound("has_tags", asList(new MapToTermConverter().apply(tags)))));
		return query.select(nodeVariableName).adapt(new TermToNodeConverter()).allSolutions();
	}

	@Override
	public List<Node> getNearNodes(Coordinate coordinate, double distanceKm) {
		String nodeVariableName = "Node";
		Variable nodeVariable = new Variable(nodeVariableName);
		Query query = getPrologEngine().query(
				logtalkMessage(this, new Compound("node", asList(nodeVariable))),
				logtalkMessage(nodeVariable, new Compound("near", asList(coordinate, new FloatTerm(distanceKm)))));
		return query.select(nodeVariableName).adapt(new TermToNodeConverter()).allSolutions();
	}

	@Override
	public List<Node> getNearNodes(Coordinate coordinate, double distanceKm, Map<String, String> tags) {
		String nodeVariableName = "Node";
		Variable nodeVariable = new Variable(nodeVariableName);
		Query query = getPrologEngine().query(
				logtalkMessage(this, new Compound("node", asList(nodeVariable))),
				logtalkMessage(nodeVariable, new Compound("has_tags", asList(new MapToTermConverter().apply(tags)))),
				logtalkMessage(nodeVariable, new Compound("near", asList(coordinate, new FloatTerm(distanceKm)))));
		return query.select(nodeVariableName).adapt(new TermToNodeConverter()).allSolutions();
	}

	@Override
	public List<Way> getWays() {
		Term message = new Compound("way",asList(new Variable("Way")));
		Query query = new LogtalkObject(this, getPrologEngine()).perform(message);
		return query.select("Way").adapt(new TermToWayConverter()).allSolutions();
	}

	@Override
	public List<Way> getWays(Map<String, String> tags) {
		String wayVariableName = "Way";
		Variable wayVariable = new Variable(wayVariableName);
		Query query = getPrologEngine().query(
				logtalkMessage(this, new Compound("way", asList(wayVariable))),
				logtalkMessage(wayVariable, new Compound("has_tags", asList(new MapToTermConverter().apply(tags)))));
		return query.select(wayVariableName).adapt(new TermToWayConverter()).allSolutions();
	}

	@Override
	public List<Way> getNearWays(Coordinate coordinate, double distanceKm) {
		String wayVariableName = "Way";
		Variable wayVariable = new Variable(wayVariableName);
		Query query = getPrologEngine().query(
				logtalkMessage(this, new Compound("way", asList(wayVariable))),
				logtalkMessage(wayVariable, new Compound("near", asList(coordinate, new FloatTerm(distanceKm)))));
		return query.select(wayVariableName).adapt(new TermToWayConverter()).allSolutions();
	}

	@Override
	public List<Way> getNearWays(Coordinate coordinate, double distanceKm, Map<String, String> tags) {
		String wayVariableName = "Way";
		Variable wayVariable = new Variable(wayVariableName);
		Query query = getPrologEngine().query(
				logtalkMessage(this, new Compound("way", asList(wayVariable))),
				logtalkMessage(wayVariable, new Compound("has_tags", asList(new MapToTermConverter().apply(tags)))),
				logtalkMessage(wayVariable, new Compound("near", asList(coordinate, new FloatTerm(distanceKm)))));
		return query.select(wayVariableName).adapt(new TermToWayConverter()).allSolutions();
	}

	@Override
	public Term asTerm() {
		return new Atom(OSM_NAME);
	}
}
