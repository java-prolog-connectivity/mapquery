package org.jpc.examples.osm.model.imp;

import java.util.ArrayList;
import java.util.List;

import org.jpc.examples.osm.model.Node;
import org.jpc.examples.osm.model.Osm;
import org.jpc.examples.osm.model.Way;

public class OsmFragment implements Osm {

	private List<Node> nodes;
	private List<Way> ways;
	
	public OsmFragment(List<Node> nodes) {
		this(nodes, new ArrayList<Way>());
	}
	
	public OsmFragment(List<Node> nodes, List<Way> ways) {
		this.nodes = nodes;
		this.ways = ways;
	}
	
	@Override
	public List<Node> getNodes() {
		return nodes;
	}

	@Override
	public List<Way> getWays() {
		return ways;
	}

}
